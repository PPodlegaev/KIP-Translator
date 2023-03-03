package com.example.kiptranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var sqLiteHelper: SQLiteHelper

    private var adapter: TranslateAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        sqLiteHelper = SQLiteHelper(this)

        recycler = findViewById(R.id.translateList)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = TranslateAdapter()
        recycler.adapter = adapter
        val btnBack: Button = findViewById(R.id.btnBackH)
        btnBack.setOnClickListener {
            finish()
        }
        getTranslate()
    }

    private fun getTranslate(){
        val stdList = sqLiteHelper.getAllTranslate()
        adapter?.addItems(stdList)
    }
}