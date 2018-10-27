package com.example.pemil.smarthack.parse.utils;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.pemil.smarthack.R;
import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static final List<ExchangeSymbol> exchangeSymbols = new ArrayList<>();

    public static void parseExchanges(Context context) {
        List<String> exchanges = readFileFromRow(context, R.raw.exchanges);

        for(String exchange : exchanges) {
            getStockSymbols(context, exchange);
        }

        Log.d("asdasd", "asdasd");
    }

    private static void getStockSymbols(Context context, final String exchange) {
        String requestString = "https://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=%s&render=download";

        RequestQueue queue = Volley.newRequestQueue(context);
        final List<String> stockSymbols = new ArrayList<>();

        ByteRequest byteRequest = new ByteRequest(
                String.format(requestString, exchange),
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        convertToCSV(response, stockSymbols);
                        addSymbolsToList(stockSymbols, exchange);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.getStackTraceString(error);
                    }
                });

        queue.add(byteRequest);
    }

    private static void convertToCSV(byte[] csvContent, List<String> stockSymbolBuffer) {
        CSVReader csvReader = new CSVReader(new StringReader(new String(csvContent, StandardCharsets.UTF_8)));
        List<String[]> csvAsList;

        try {
            csvAsList = csvReader.readAll();

            csvAsList.remove(0);
            for(String[] row : csvAsList) {
                stockSymbolBuffer.add(row[0]);
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

    private static void addSymbolsToList(List<String> symbols, String exchange) {
        for(String symbol: symbols) {
            exchangeSymbols.add(new ExchangeSymbol(exchange, symbol));
        }
    }
}
