package com.example.kiptranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

       recycler = findViewById(R.id.translateList)

        val btnBack: Button = findViewById(R.id.btnBackH)
        btnBack.setOnClickListener {
            finish()
        }
    }
}