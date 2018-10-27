package com.example.pemil.smarthack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pemil.smarthack.parse.utils.Parser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Parser.parseExchanges(this);
    }
}
