package com.example.pemil.smarthack.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.example.pemil.smarthack.DataSource.InvestmentDataSource;
import com.example.pemil.smarthack.Models.ExpandableListAdapter;
import com.example.pemil.smarthack.Models.Investment;
import com.example.pemil.smarthack.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewInvestmentFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    InvestmentDataSource dataSource =  new InvestmentDataSource("");
    List<Investment> investments = new ArrayList<>();
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_investment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context= this.getContext();
        // get the listview
        expListView = view.findViewById(R.id.lvExp2);

        // preparing list data

        final FirebaseUser user = dataSource.getAuth().getCurrentUser();
        dataSource.getTable().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    investments.add(postSnapshot.getValue(Investment.class));
                    Log.i("investments", postSnapshot.toString());

                }
                Log.i("invest", investments.toString());
                prepareListData(investments);

                listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void prepareListData(List<Investment> invetments) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        for (Investment inv : invetments) {

            listDataHeader.add(inv.getName());
            listDataChild.put(inv.getName(), dataSource.getInvestmentDetails(inv));
            Log.i("expd", dataSource.getInvestmentDetails(inv).toString());

        }

    }
}
