package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val name = intent.getStringExtra("name")
        val statues = intent.getStringExtra("statue")

        val txtname=findViewById<TextView>(R.id.textView5)
        val txtstatues=findViewById<TextView>(R.id.textView9)
        val btn = findViewById<Button>(R.id.button2)

        txtname.text=name.toString()
        txtstatues.text=statues.toString()

        btn.setOnClickListener{
            super.onBackPressed();
        }


    }

}
