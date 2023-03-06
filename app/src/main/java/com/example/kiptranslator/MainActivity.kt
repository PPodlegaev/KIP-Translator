package com.example.kiptranslator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var translator: Translator? = null

    private val REQUEST_CODE_SPEECH_INPUT = 1
    private lateinit var sqLiteHelper: SQLiteHelper
    private var items= arrayOf("English","Russian")
    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    var fromLanguages = arrayOf(
        "English", "Vietnamese", "Chinese", "Korean", "French", "Italian", "Japanese",
        "Russian", "Thai"
    )
    var toLanguages = arrayOf(
        "Vietnamese", "English", "Chinese", "Korean", "French", "Italian", "Japanese",
        "Russian", "Thai"
    )
    var modelManager = RemoteModelManager.getInstance()

    var SourceLanguageCode = "en"
    var TargetLanguageCode = "ru"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sqLiteHelper = SQLiteHelper(this)
        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, items)

        val translate: Button = findViewById(R.id.btnTranslate)
        val delete: Button = findViewById(R.id.btnDeleteTranslate)

        val sourceText: EditText = findViewById(R.id.sourceText)
        val targetText: TextView = findViewById(R.id.targetText)
        val microphone: ImageButton = findViewById(R.id.btnMic)
        val copy: ImageButton = findViewById((R.id.btnCopy))
        val history: ImageButton = findViewById(R.id.btnHistory)
        val firstLangSelector: Spinner = findViewById(R.id.firstLangSelector)
        val secondLangSelector: Spinner = findViewById(R.id.secondLangSelector)
        //val switchLang: MaterialButton = findViewById(R.id.btnSwitchLang)

        val fromadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fromLanguages)
        fromadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        firstLangSelector.setAdapter(fromadapter)
        firstLangSelector.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                //"Vietnamese", "English", "Chinese", "Korean", "French", "Italian", "Japanese", "Russian", "Thai"/
                SourceLanguageCode = when (fromLanguages[i]) {
                    "Vietnamese" -> TranslateLanguage.VIETNAMESE
                    "Chinese" -> TranslateLanguage.CHINESE
                    "Korean" -> TranslateLanguage.KOREAN
                    "French" -> TranslateLanguage.FRENCH
                    "Italian" -> TranslateLanguage.ITALIAN
                    "Japanese" -> TranslateLanguage.JAPANESE
                    "Russian" -> TranslateLanguage.RUSSIAN
                    "Thai" -> TranslateLanguage.THAI
                    else -> TranslateLanguage.ENGLISH
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Enter language to translate", Toast.LENGTH_SHORT)
                    .show()
            }
        })


        val toadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, toLanguages)
        toadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        secondLangSelector.setAdapter(toadapter)
        secondLangSelector.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                //"Vietnamese", "English", "Chinese", "Korean", "French", "Italian", "Japanese", "Russian", "Thai"//
                TargetLanguageCode = when (toLanguages[i]) {
                    "English" -> TranslateLanguage.ENGLISH
                    "Chinese" -> TranslateLanguage.CHINESE
                    "Korean" -> TranslateLanguage.KOREAN
                    "French" -> TranslateLanguage.FRENCH
                    "Italian" -> TranslateLanguage.ITALIAN
                    "Japanese" -> TranslateLanguage.GERMAN
                    "Russian" -> TranslateLanguage.RUSSIAN
                    "Thai" -> TranslateLanguage.THAI
                    else -> TranslateLanguage.VIETNAMESE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Enter language target", Toast.LENGTH_SHORT)
                    .show()
            }
        })




        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        copy!!.setOnClickListener {
            if (targetText.text.isNotEmpty()) {
                val clipData = ClipData.newPlainText("label", targetText!!.text.toString())
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this@MainActivity, "Text Copyed", Toast.LENGTH_SHORT).show()
            }
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

            val string: String = sourceText.getText().toString()
            downloadModel(SourceLanguageCode, TargetLanguageCode, string)


            //val options = TranslatorOptions.Builder()
          //    .setSourceLanguage(selectFrom())
          //    .setTargetLanguage(selectTo())
          //    .build()

          //val englishGermanTranslator = Translation.getClient(options)

          //englishGermanTranslator.downloadModelIfNeeded(conditions)
          //    .addOnSuccessListener {

          //        englishGermanTranslator.translate(sourceText.text.toString())
          //            .addOnSuccessListener { translatedText ->

          //                targetText.text=translatedText

          //            }
          //            .addOnFailureListener { exception ->

          //                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

          //            }


          //    }
          //    .addOnFailureListener { exception ->

          //        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

          //    }

              //   if(sourceText.text.isNotEmpty() && targetText.text.isNotEmpty()){
              //  // below we have created
              //  // a new DBHelper class,
              //  // and passed context to it
              //  val db = DBHelper(this, null)
              //
              //               // creating variables for values
              //               // in name and age edit texts
              //               val source_text = sourceText.text.toString()
              //                   val target_text = targetText.text.toString()
              //
              //                       // calling method to add
              //                       // name to our database
              //                       db.addName(source_text, target_text)
              //
              //                       // Toast to message on the screen
              //                       Toast.makeText(this, source_text + " added to database", Toast.LENGTH_LONG).show()
              //
              //                   }


            addTranslate()
        }


        delete.setOnClickListener(View.OnClickListener {
            deletemodel(SourceLanguageCode, TargetLanguageCode)
            Toast.makeText(this@MainActivity, "Deleted model", Toast.LENGTH_SHORT).show()
        })


        val btnSettings: ImageButton = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
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

    private fun downloadModel(inputLang: String, targetLang: String, input: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputLang)
            .setTargetLanguage(targetLang)
            .build()
        translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator!!.downloadModelIfNeeded(conditions).addOnSuccessListener(OnSuccessListener<Void?> {
            Toast.makeText(
                this@MainActivity,
                "Language model is being downloaded.",
                Toast.LENGTH_SHORT
            ).show()
            translateLanguage(input)
        }).addOnFailureListener(OnFailureListener {
            Toast.makeText(
                this@MainActivity,
                "Fail to download model",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun translateLanguage(input: String) {
        val targetText: TextView = findViewById(R.id.targetText)

        translator!!.translate(input).addOnSuccessListener { s -> targetText.setText(s) }
            .addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    "Fail to translate",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

   //private fun selectFrom(): String {

   //    val firstLangSelector: AutoCompleteTextView = findViewById(R.id.firstLangSelector)
   //    return when(firstLangSelector.text.toString()){

   //        ""-> TranslateLanguage.ENGLISH

   //        "English"-> TranslateLanguage.ENGLISH

   //        "Russian"-> TranslateLanguage.RUSSIAN

   //        else-> TranslateLanguage.ENGLISH

   //    }


   //}

   //private fun selectTo(): String {
   //    val secondLangSelector: AutoCompleteTextView = findViewById(R.id.secondLangSelector)

   //    return when(secondLangSelector.text.toString()){

   //        ""-> TranslateLanguage.RUSSIAN

   //        "English"-> TranslateLanguage.ENGLISH

   //        "Russian"-> TranslateLanguage.RUSSIAN

   //        else-> TranslateLanguage.RUSSIAN

   //    }


   //}

    private fun deletemodel(Srclanguage: String, Tarlanguage: String) {
        val SrcModel = TranslateRemoteModel.Builder(Srclanguage).build()
        val TarModel = TranslateRemoteModel.Builder(Tarlanguage).build()
        modelManager.deleteDownloadedModel(SrcModel)
            .addOnSuccessListener(OnSuccessListener<Any?> { })
            .addOnFailureListener(OnFailureListener { })
        modelManager.deleteDownloadedModel(TarModel)
            .addOnSuccessListener(OnSuccessListener<Any?> { })
            .addOnFailureListener(OnFailureListener { })
    }


    private fun addTranslate(){
        val sourceText: EditText = findViewById(R.id.sourceText)
        val targetText: TextView = findViewById(R.id.targetText)
        val source = sourceText.text.toString()
        val target = targetText.text.toString()

        if(source.isNotEmpty() && target.isNotEmpty()){
            val std = TranslateModel(sourceText = source, targetText = target)
            val status = sqLiteHelper.insertTranslate(std)

            if(status > -1){
                Toast.makeText(this,"Перевод добавлен",Toast.LENGTH_SHORT).show()

            }
        }
    }



}

