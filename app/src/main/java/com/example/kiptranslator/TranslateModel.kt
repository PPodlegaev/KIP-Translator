package com.example.kiptranslator

import java.util.*

data class TranslateModel (
    var id: Int = getAutoId(),
    var sourceText: String = "",
    var targetText: String = ""
    ) {
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}