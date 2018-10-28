package com.example.pemil.smarthack.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import com.example.pemil.smarthack.DataSource.InvestmentDataSource
import com.example.pemil.smarthack.Models.ExpandableListAdapter
import com.example.pemil.smarthack.Models.Investment
import com.example.pemil.smarthack.Models.PredictedInvestment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.invest_long_version.*
import java.text.SimpleDateFormat
import com.example.pemil.smarthack.R
import java.util.*


class NewInvestmentFragment : Fragment() {

    internal lateinit var listAdapter: ExpandableListAdapter
    internal lateinit var expListView: ExpandableListView
    internal lateinit var listDataHeader: MutableList<String>
    internal lateinit var listDataChild: HashMap<String, List<String>>
    lateinit var dataSource: InvestmentDataSource

    val preferencesMap: MutableMap<String, List<String>> = mutableMapOf<String, List<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.invest_long_version, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataSource = InvestmentDataSource()
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

                val investments = HashMap<String, PredictedInvestment>()

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
                                        investments.put(
                                            invest.key.toString()!!,
                                            invest.getValue(PredictedInvestment::class.java)!!
                                        )
                                    }
                                }
                            }
                        }

                        val filteredInvestments =
                            investments.filter { it -> it.value.predictedValue - it.value.close > 0 }

                        Log.d("allinvestments", filteredInvestments.toString())


                        prepareListData(filteredInvestments)
//
                        listAdapter = ExpandableListAdapter(context, listDataHeader, listDataChild)
//
//                        // setting list adapter
                        lvExp.setAdapter(listAdapter)

                        lvExp.setOnGroupClickListener { parent, v, groupPosition, id ->

                            Toast.makeText(
                                context, "Added new investment: " +
                                        listDataChild[listDataHeader[groupPosition]].toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            val timeStamp = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                            val investment = Investment(
                                listDataHeader[groupPosition],
                                timeStamp,
                                "$5",
                                listDataChild[listDataHeader[groupPosition]]?.get(0)?.toDouble(),
                                listDataChild[listDataHeader[groupPosition]]?.get(1)?.toDouble(),
                                listDataChild[listDataHeader[groupPosition]]?.get(2),
                                listDataChild[listDataHeader[groupPosition]]?.get(3)?.toDouble(),
                                listDataChild[listDataHeader[groupPosition]]?.get(4)?.toDouble(),
                                listDataChild[listDataHeader[groupPosition]]?.get(5)?.toDouble(),
                                listDataChild[listDataHeader[groupPosition]]?.get(6),
                                null

                            )

                            dataSource.sendInvestmentToDB(investment)

                            true

                        }

                        lvExp.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->


                            val dataBase = FirebaseDatabase.getInstance()
                            val table = dataBase.reference.child("investment")
                            table.child(user.uid).child(listDataHeader[groupPosition])
                                .child(listDataChild[listDataHeader[groupPosition]]?.get(childPosition)!!)
                                .setValue(true)

                            true
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }
        )


    }

    private fun prepareListData(investments: Map<String, PredictedInvestment>) {

        listDataHeader = java.util.ArrayList(investments.keys)
        listDataChild = java.util.HashMap()


        for (key in investments.keys) {
            listDataChild.put(key, dataSource.getPredInvestmentDetails(investments[key]))
            Log.i("expd", dataSource.getPredInvestmentDetails(investments[key]).toString())

        }

    }
}