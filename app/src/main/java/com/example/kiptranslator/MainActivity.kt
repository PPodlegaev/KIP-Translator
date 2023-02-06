package com.example.kiptranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {


    private var items= arrayOf("English","Hindi","Bengali","Gujarati","Tamil","Telugu")
    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, items)

        val firstLangSelector: AutoCompleteTextView = findViewById(R.id.firstLangSelector)
        val secondLangSelector: AutoCompleteTextView = findViewById(R.id.secondLangSelector)
        val translate: Button = findViewById(R.id.btnTranslate)
        val sourceText: EditText = findViewById(R.id.sourceText)
        val targetText: TextView = findViewById(R.id.targetText)


        translate.setOnClickListener {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val englishGermanTranslator = Translation.getClient(options)

            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {

                    englishGermanTranslator.translate(sourceText.text.toString())
                        .addOnSuccessListener { translatedText ->

                            targetText.text=translatedText

                        }
                        .addOnFailureListener { exception ->

                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

                        }


                }
                .addOnFailureListener { exception ->

                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

                }

        }




        val btnSettings: ImageButton = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            SettingsActivity()
        }
    }

    private fun selectFrom(): String {
        val firstLangSelector: AutoCompleteTextView = findViewById(R.id.firstLangSelector)
        return when(firstLangSelector.text.toString()){

            ""-> TranslateLanguage.ENGLISH

            "English"-> TranslateLanguage.ENGLISH

            "Russian"-> TranslateLanguage.RUSSIAN

            else-> TranslateLanguage.ENGLISH

        }


    }

    private fun selectTo(): String {
        val secondLangSelector: AutoCompleteTextView = findViewById(R.id.secondLangSelector)

        return when(secondLangSelector.text.toString()){

            ""-> TranslateLanguage.RUSSIAN

            "English"-> TranslateLanguage.ENGLISH

            "Russian"-> TranslateLanguage.RUSSIAN

            else-> TranslateLanguage.RUSSIAN

        }


    }




}

