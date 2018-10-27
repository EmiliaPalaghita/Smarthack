package com.example.pemil.smarthack.parse.utils;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.pemil.smarthack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parser {
    private static final HashMap<String, HashMap<String, List<String>>> symbols = new HashMap<>();
    private static final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    private static final DatabaseReference table = dataBase.getReference("Symbols");
    private static final ObservableInteger observableInteger = new ObservableInteger();


    public static void parseExchanges(Context context) {
        List<String> exchanges = readFileFromRow(context, R.raw.exchanges);
        RequestQueue queue = Volley.newRequestQueue(context);
        observableInteger.setListener(new ObservableInteger.ChangeListener() {
            @Override
            public void onChange() {
                table.setValue(symbols);
            }
        });


        for (String exchange : exchanges) {
            getStockSymbols(exchange, queue);
        }
    }

    public static String convertToAcceptableFormat(String key) {
        return key.replace("/", " ")
                .replace(".", " ")
                .replace("#", " ")
                .replace("$", " ")
                .replace("[", " ")
                .replace("]", " ");
    }

    private static void getStockSymbols(final String exchange, RequestQueue requestQueue) {
        String requestString = "https://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=%s&render=download";

        ByteRequest byteRequest = new ByteRequest(
                String.format(requestString, exchange),
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        observableInteger.decrement();
                        convertToDBFormat(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.getStackTraceString(error);
                    }
                });
        requestQueue.add(byteRequest);
        observableInteger.increment();
    }

    private static void convertToDBFormat(byte[] csvContent) {
        CSVReader csvReader = new CSVReader(new StringReader(new String(csvContent, StandardCharsets.UTF_8)));
        List<String[]> csvAsList;

        try {
            csvAsList = csvReader.readAll();

            csvAsList.remove(0);
            for(String[] row : csvAsList) {
                String sector = convertToAcceptableFormat(row[6]);
                String industry = convertToAcceptableFormat(row[7]);
                String symbol = convertToAcceptableFormat(row[0]);

                if(!isValidKey(sector, industry, symbol)) {
                    continue;
                }

                if(symbols.get(sector) == null) {
                    symbols.put(sector, new HashMap<String, List<String>>());
                }

                if(symbols.get(sector).get(industry) == null) {
                    symbols.get(sector).put(industry, new ArrayList<String>());
                }

                symbols.get(sector).get(industry).add(symbol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readFileFromRow(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> stringList = new ArrayList<>();

        try {
            while (( line = buffreader.readLine()) != null) {
                stringList.add(line);
            }
        } catch (IOException e) {
            return null;
        }

        return stringList;
    }

    private static boolean isValidKey(String sector, String industry, String symbol) {
        return !sector.contains("n/a") && !industry.contains("n/a") && !symbol.contains("n/a");
    }
}
