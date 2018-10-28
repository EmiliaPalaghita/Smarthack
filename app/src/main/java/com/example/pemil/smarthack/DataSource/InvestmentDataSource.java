package com.example.pemil.smarthack.DataSource;

import android.util.Log;
import com.example.pemil.smarthack.Models.Investment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InvestmentDataSource { private DatabaseReference table;

    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    public List<Investment> investments;
    final FirebaseUser user;

    public InvestmentDataSource() {
        this.auth = FirebaseAuth.getInstance();
        this.dataBase = FirebaseDatabase.getInstance();
        this.table = dataBase.getReference().child("investment");
        this.user = this.auth.getCurrentUser();
        this.investments = new ArrayList<>();
    }

    public void sendInvestmentToDB(Investment investment) {
        table.child(user.getUid()).child(investment.getName()).setValue(investment);
        Log.d("DATABASE INVESTMENT", user.toString());
    }

    public void selectInvestments() {
        final FirebaseUser user = this.auth.getCurrentUser();
        table.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    investments.add(postSnapshot.getValue(Investment.class));
                    Log.i("investments", postSnapshot.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public DatabaseReference getTable() {
        return this.table;
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
        list.add(i.getCategory());
        list.add(i.getSubCategory());
        list.add(i.getProfit());
        return list;
    }

}

