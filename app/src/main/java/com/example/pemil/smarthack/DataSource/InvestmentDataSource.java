package com.example.pemil.smarthack.DataSource;

import android.support.annotation.NonNull;
import android.util.Log;
import com.example.pemil.smarthack.Models.Investment;
import com.example.pemil.smarthack.Models.PredictedInvestment;
import com.example.pemil.smarthack.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvestmentDataSource {

    private DatabaseReference preferencesTable;
    private DatabaseReference investmentsTable;
    private DatabaseReference categoriesTable;
    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    public List<Investment> investments;
    private final FirebaseUser user;

    public InvestmentDataSource() {
        this.auth = FirebaseAuth.getInstance();
        this.dataBase = FirebaseDatabase.getInstance();
        this.preferencesTable = dataBase.getReference().child("preferences");
        this.investmentsTable = dataBase.getReference().child("investments");
        this.categoriesTable = dataBase.getReference().child("Symbols");
        this.user = this.auth.getCurrentUser();
        this.investments = new ArrayList<>();
    }

    public void sendInvestmentToDB(Investment investment) {
        investmentsTable.child(user.getUid()).child(investment.getName()).setValue(investment);
        Log.d("DATABASE INVESTMENT", user.toString());
    }

    public void selectInvestments() {
        final FirebaseUser user = this.auth.getCurrentUser();
        investmentsTable.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    investments.add(postSnapshot.getValue(Investment.class));
                    Log.i("investments", postSnapshot.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void selectAllInvestmentsForCurrentUser(final HashMap<String, List<String>> preferences) {
        final List<String> categories = new ArrayList<>(preferences.keySet());

        final List<PredictedInvestment> investments = new ArrayList<>();

        final List<PredictedInvestment> predictedInvestments = new ArrayList<>();
        categoriesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot cat : dataSnapshot.getChildren()) {
                    Log.d("blablabla", cat.toString());
                    if(categories.contains(cat.toString())) {
                        for(String subcategory : preferences.get(cat.toString())) {
                            Log.d("blablabla1", subcategory.toString());
                            for(DataSnapshot invest : cat.child(subcategory).getChildren()) {
                                Log.d("finallist", invest.toString());
                                investments.add(invest.getValue(PredictedInvestment.class));
                            }
                        }
                    }
                }

                Log.d("allinvestments", investments.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public DatabaseReference getInvestmentsTable() {
        return this.investmentsTable;
    }

    public FirebaseAuth getAuth() {
        return this.auth;
    }

    public FirebaseDatabase getDataBase() {
        return this.dataBase;
    }

    public List<String> getInvestmentDetails(Investment i) {
        List<String> list = new ArrayList<>();
        list.add(i.getDate());
        list.add(i.getAmt());
        list.add(i.getClose().toString());
        list.add(i.getHigh().toString());
        list.add(i.getIndustry());
        list.add(i.getLow().toString());
        list.add(i.getOpen().toString());
        list.add(i.getPredictedValue().toString());
        list.add(i.getSector());
//        list.add(i.getVolum().toString());
        return list;
    }
    public List<String> getPredInvestmentDetails(PredictedInvestment i) {
        List<String> list = new ArrayList<>();
        list.add("" + i.getClose().toString());
        list.add(i.getHigh().toString());
        list.add(i.getIndustry());
        list.add(i.getLow().toString());
        list.add(i.getOpen().toString());
        list.add(i.getPredictedValue().toString());
        list.add(i.getSector());
        double procent = i.getPredictedValue() - i.getClose();
        list.add(String.valueOf(procent) + "%");
//        list.add(i.getVolum().toString());
        return list;
    }

}

