package com.example.pemil.smarthack.parse.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.pemil.smarthack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVReader;
import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.*;
import com.recombee.api_client.exceptions.ApiException;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

public class Parser {
    private final HashMap<String, HashMap<String, HashMap<String, Object>>> symbols = new HashMap<>();
    private final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
    private final DatabaseReference table = dataBase.getReference("Symbols");
    private final ObservableInteger observableInteger = new ObservableInteger();
    private final RecombeeClient client = new RecombeeClient("smarthack-2018 ", "PU6qyPll2goGUbwApmeY0O2BYJFBVYTRIrbI8cV4R8v0LzFRjem5VsanNYtp66Sy");
    private RequestQueue alphaQueue;
    private Context app_context;

    public Parser() {
    }

    public void parseExchanges(Context context) throws ApiException, ParseException {
//        initRecombee();
        List<String> exchanges = readFileFromRow(context, R.raw.exchanges);
        RequestQueue queue = Volley.newRequestQueue(context);
        app_context = context;

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

    private void initRecombee() throws ApiException, ParseException {
        client.send(new ResetDatabase());
        client.send(new AddItemProperty("high", "double"));
        client.send(new AddItemProperty("low", "double"));
        client.send(new AddItemProperty("close", "double"));
        client.send(new AddItemProperty("volume", "double"));
        client.send(new AddItemProperty("predictedValue", "double"));
        client.send(new AddItemProperty("sector", "string"));
        client.send(new AddItemProperty("industry", "string"));

    }

    public String convertToAcceptableFormat(String key) {
        return key.replace("/", " ")
                .replace(".", " ")
                .replace("#", " ")
                .replace("$", " ")
                .replace("[", " ")
                .replace("]", " ");
    }

    private void getStockSymbols(final String exchange, RequestQueue requestQueue) {
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

    private void convertToDBFormat(byte[] csvContent) {
        CSVReader csvReader = new CSVReader(new StringReader(new String(csvContent, StandardCharsets.UTF_8)));
        List<String[]> csvAsList;

        try {
            csvAsList = csvReader.readAll();

            csvAsList.remove(0);
            for (String[] row : csvAsList) {
                String sector = convertToAcceptableFormat(row[6]);
                String industry = convertToAcceptableFormat(row[7]);
                String symbol = convertToAcceptableFormat(row[0]);

                if (!checkIfDataFileExists(symbol)) {
                    continue;
                }

                if (!isValidKey(sector, industry, symbol)) {
                    continue;
                }

                if (symbols.get(sector) == null) {
                    symbols.put(sector, new HashMap<String, HashMap<String, Object>>());
                }

                if (symbols.get(sector).get(industry) == null) {
                    symbols.get(sector).put(industry, new HashMap<String, Object>());
                }

                addItem(sector, industry, symbol);
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfDataFileExists(String symbol) throws IOException {
        return Arrays.asList(app_context.getResources().getAssets().list("")).contains(symbol.toLowerCase() + "a.us.txt");
    }

    private void addItem(final String sector, final String industry, String symbol) throws IOException, ApiException {
//        final ArrayList<Request> requests = new ArrayList<>();
        InputStream inputStream = app_context.getResources().getAssets().open(symbol.toLowerCase() + ".us.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        CSVReader csvReader = new CSVReader(in);

        List<String[]> strings = csvReader.readAll();
        List<String[]> lastEntries = strings.subList(strings.size() - 10, strings.size() - 1);
        List<Double> lastOpenValues = new ArrayList<>();

        for (String[] entry : lastEntries) {
            lastOpenValues.add(Double.valueOf(entry[1]));
        }

        final Double predictedNextValue = predictStockForNextDay(lastOpenValues);
        final String[] lastEntry = strings.get(strings.size() - 1);

        Map<String, Object> itemDetails = new HashMap<String, Object>() {{
                    put("open", Double.valueOf(lastEntry[1]));
                    put("high", Double.valueOf(lastEntry[2]));
                    put("low", Double.valueOf(lastEntry[3]));
                    put("close", Double.valueOf(lastEntry[4]));
                    put("volume", Double.valueOf(lastEntry[5]));
                    put("predictedValue", predictedNextValue);
                    put("sector", sector);
                    put("industry", industry);
                }};

        symbols.get(sector).get(industry).put(symbol, itemDetails);


//        final SetItemValues req = new SetItemValues(
//                symbol,
//                new HashMap<String, Object>() {{
//                    put("open", Double.valueOf(lastEntry[1]));
//                    put("high", Double.valueOf(lastEntry[2]));
//                    put("low", Double.valueOf(lastEntry[3]));
//                    put("close", Double.valueOf(lastEntry[4]));
//                    put("volume", Double.valueOf(lastEntry[5]));
//                    put("predictedValue", predictedNextValue);
//                    put("sector", sector);
//                    put("industry", industry);
//                }}).setCascadeCreate(true);
//
//        requests.add(req);
//        client.send(new Batch(requests));


    }

    private double getNextEma(double s, double y) {
        double alpha = 0.5;
        return alpha * y + (1 - alpha) * s;
    }

    private Double predictStockForNextDay(List<Double> initialValues) {
        List<Double> result = new ArrayList<>();

        double ema = initialValues.get(0);
        double xt = initialValues.get(0);
        double gamma = 0.5;

        for (int i = 1; i < initialValues.size(); i++) {
            result.add(gamma * ema + (1 - gamma) * xt);
            xt = initialValues.get(i);
            ema = getNextEma(ema, xt);
        }

        xt = result.get(result.size() - 1);
        ema = getNextEma(ema, xt);


        return gamma * ema + (1 - gamma) * xt;
    }

    private List<String> readFileFromRow(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> stringList = new ArrayList<>();

        try {
            while ((line = buffreader.readLine()) != null) {
                stringList.add(line);
            }
        } catch (IOException e) {
            return null;
        }

        return stringList;
    }

    private boolean isValidKey(String sector, String industry, String symbol) {
        return !sector.contains("n/a") && !industry.contains("n/a") && !symbol.contains("n/a");
    }
}
