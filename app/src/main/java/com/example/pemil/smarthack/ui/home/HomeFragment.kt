package com.example.pemil.smarthack.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pemil.smarthack.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.home_fragment.*
import com.github.mikephil.charting.data.BarData



class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val chart = barchart as BarChart

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 30f))
        entries.add(BarEntry(1f, 80f))
        entries.add(BarEntry(2f, 60f))
        entries.add(BarEntry(3f, 50f))
        // gap of 2f
        entries.add(BarEntry(5f, 70f))
        entries.add(BarEntry(6f, 60f))

        val set = BarDataSet(entries, "BarDataSet")

        val data = BarData(set)
        data.barWidth = 0.9f // set custom bar width
        chart.data = data
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.invalidate() // refresh
    }
}
