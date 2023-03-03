package com.example.kiptranslator

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

            companion object{
                private const val DATABASE_VERSION = 1
                private const val DATABASE_NAME = "translate.db"
                private const val TBL_TRANSLATE = "tbl_translate"
                private const val ID = "id"
                private const val SOURCE_TEXT = "source_text"
                private const val TARGET_TEXT = "target_text"

            }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblTranslate = ("CREATE TABLE " + TBL_TRANSLATE + "("
                + ID + " INTEGER PRIMARY KEY," + SOURCE_TEXT + " TEXT,"
                + TARGET_TEXT + " TEXT" + ")")
        db?.execSQL(createTblTranslate)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_TRANSLATE")
    }

    fun insertTranslate(std: TranslateModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(SOURCE_TEXT, std.sourceText)
        contentValues.put(TARGET_TEXT, std.targetText)

        val success = db.insert(TBL_TRANSLATE, null, contentValues)
        db.close()
        return success
    }


    fun getAllTranslate():ArrayList<TranslateModel>{
        val stdList: ArrayList<TranslateModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_TRANSLATE"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var sourceText: String
        var targetText: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                sourceText = cursor.getString(cursor.getColumnIndexOrThrow("source_text"))
                targetText = cursor.getString(cursor.getColumnIndexOrThrow("target_text"))
                val std = TranslateModel(id = id, sourceText = sourceText, targetText = targetText)
                stdList.add((std))
            } while (cursor.moveToNext())
        }
        return stdList
    }
}