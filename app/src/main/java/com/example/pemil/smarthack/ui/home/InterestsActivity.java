package com.example.pemil.smarthack.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.pemil.smarthack.DataSource.InvestmentDataSource;
import com.example.pemil.smarthack.Models.ExpandableListAdapter;
import com.example.pemil.smarthack.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterestsActivity extends Activity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    List<String> listDataHeader;
    InvestmentDataSource dataSource = new InvestmentDataSource();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_layout);

        context = getApplicationContext();
        listDataHeader = new ArrayList<>();

        expListView = findViewById(R.id.interestslist);
        final FirebaseUser user = dataSource.getAuth().getCurrentUser();
        dataSource.getDataBase().getReference().child("Symbols").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DEBUGDRAGU", dataSnapshot.toString());
                List<String> llist;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i("interests", postSnapshot.getKey().toString());
                    listDataHeader.add(postSnapshot.getKey().toString());
                    llist = new ArrayList<>();
                    for (DataSnapshot postPostSnapshot : postSnapshot.getChildren()) {

                        Log.i("interests", postPostSnapshot.getKey().toString());
                        llist.add(postPostSnapshot.getKey().toString());
                    }
                    Log.i("fmm", llist.toString());
                    listDataChild.put(postSnapshot.getKey().toString(), llist);
                }
                Log.i("header", listDataHeader.toString());
                Log.i("hashmapfmm", listDataChild.toString());

                listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Toast.makeText(getApplicationContext(), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_LONG).show();
                        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
                        DatabaseReference table = dataBase.getReference().child("preference");
                        table.child(user.getUid()).child(listDataHeader.get(groupPosition))
                                .child(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))
                                .setValue(true);

                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

}
