package com.example.kiptranslator

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TranslateAdapter: RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder>() {
    private var stdList: ArrayList<TranslateModel> = ArrayList()
    private var onClickDeleteItem: ((TranslateModel) -> Unit)? = null

    fun addItems(items: ArrayList<TranslateModel>){
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setonClickDeleteItem(callback: (TranslateModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TranslateViewHolder (
    LayoutInflater.from(parent.context).inflate(R.layout.maketrecycle,parent,false)
    )

    override fun onBindViewHolder(holder: TranslateViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        //holder.itemView.setOnClickListener { onClickItem?.invoke(std) }
        holder.btnDelete.setOnClickListener {onClickDeleteItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }


    class TranslateViewHolder(var view:View): RecyclerView.ViewHolder(view){
        private var sourceText = view.findViewById<TextView>(R.id.firstTranslateRecycle)
        private var targetText = view.findViewById<TextView>(R.id.secondTranslateRecycle)
         var btnDelete = view.findViewById<ImageButton>(R.id.btnDeleteRecycle)

        fun bindView(std: TranslateModel){
            sourceText.text = std.sourceText
            targetText.text = std.targetText
        }

    }

}