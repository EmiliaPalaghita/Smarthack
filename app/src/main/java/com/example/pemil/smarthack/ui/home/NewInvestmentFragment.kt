package com.example.pemil.smarthack.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pemil.smarthack.Models.PredictedInvestment
import com.example.pemil.smarthack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewInvestmentFragment : Fragment() {

    val preferencesMap: MutableMap<String, List<String>> = mutableMapOf<String, List<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.invest_long_version, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val user = FirebaseAuth.getInstance().currentUser
        val preferencesTable = FirebaseDatabase.getInstance().reference.child("preference")
        val categoriesTable = FirebaseDatabase.getInstance().reference.child("Symbols")
        preferencesTable.child(user?.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cat in dataSnapshot.children) {
                    val subcategoryList: MutableList<String> = mutableListOf()
                    Log.d("cacamama", cat.key)
                    for (subcat in cat.children) {
                        subcategoryList.add(subcat.key.toString())
                    }
                    Log.d("pisatdelista", subcategoryList.toString())
                    preferencesMap[cat.key.toString()] = subcategoryList
                }

                val categories = ArrayList<String>(preferencesMap.keys)

                val investments = java.util.ArrayList<PredictedInvestment>()

                val predictedInvestments = java.util.ArrayList<PredictedInvestment>()
                categoriesTable.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (cat in dataSnapshot.children) {
                            Log.d("blablabla", categories.toString())
                            if (categories.contains(cat.key.toString())) {
                                Log.d("categoriesbla", cat.key.toString())
                                for (subcategory in preferencesMap[cat.key.toString()]!!) {
                                    Log.d("blablabla1", subcategory.toString())
                                    for (invest in cat.child(subcategory).children) {
                                        Log.d("finallist", invest.toString())
                                        investments.add(invest.getValue(PredictedInvestment::class.java)!!)
                                    }
                                }
                            }
                        }

                        val filteredInvestments = investments.filter { it -> it.predictedValue - it.close > 0 }

                        Log.d("allinvestments", filteredInvestments.toString())
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }
        )


    }
}