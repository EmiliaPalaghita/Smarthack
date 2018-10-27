package com.example.pemil.smarthack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Uncomment this only when you want to populate database with new entries
         */
//        Parser.parseExchanges(this);
    }
}
