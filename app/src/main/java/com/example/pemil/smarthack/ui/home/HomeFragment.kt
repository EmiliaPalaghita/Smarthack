package com.example.pemil.smarthack.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pemil.smarthack.R
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //TODO - de luat lista de investitii si calculat pentru toate profit
        all_money_graph.title = "All investments from last 30 days"
        val point_data = PointsGraphSeries<DataPoint>(arrayOf(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0), DataPoint(3.0, 2.0), DataPoint(4.0, 6.0)))
        val line_data = LineGraphSeries<DataPoint>(arrayOf(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0), DataPoint(3.0, 2.0), DataPoint(4.0, 6.0)))
        line_data.setAnimated(true)
        point_data.setOnDataPointTapListener { _, dataPoint ->
            Toast.makeText(this.context, dataPoint.toString(), Toast.LENGTH_SHORT).show()
        }
        all_money_graph.addSeries(point_data)
        all_money_graph.addSeries(line_data)
    }
}
