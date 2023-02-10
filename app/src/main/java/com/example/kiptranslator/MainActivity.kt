package com.example.kiptranslator

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.*
import android.content.ClipData
import android.content.ClipboardManager

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_SPEECH_INPUT = 1

    private var items= arrayOf("English","Russian")
    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, items)

        val translate: Button = findViewById(R.id.btnTranslate)
        val sourceText: EditText = findViewById(R.id.sourceText)
        val targetText: TextView = findViewById(R.id.targetText)
        val microphone: ImageButton = findViewById(R.id.btnMic)
        val copy: ImageButton = findViewById((R.id.btnCopy))
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        copy!!.setOnClickListener {
            val clipData = ClipData.newPlainText("label", targetText!!.text.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this@MainActivity, "Text Copyed", Toast.LENGTH_SHORT).show()
        }
        microphone.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            // on below line we are passing our
            // language as a default language.
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast
                    .makeText(
                        this@MainActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val sourceText: EditText = findViewById(R.id.sourceText)
        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                sourceText.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
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

