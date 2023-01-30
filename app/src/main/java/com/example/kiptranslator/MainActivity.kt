package com.example.kiptranslator



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: ImageButton = findViewById(R.id.btnSettings)
        btn.setOnClickListener{
            val settings = Intent(this, SettingsActivity::class.java)
            startActivity(settings)
        }
    }

}