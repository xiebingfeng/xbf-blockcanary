package com.xbf.blockcanary

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
            Thread.sleep(1300)
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}
