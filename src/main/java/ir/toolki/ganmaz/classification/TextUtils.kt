package ir.toolki.ganmaz.classification

import java.util.ArrayList

object TextUtils {
    fun tokenizeAndNormalize(text: String): List<String> {
        if (text.isNullOrEmpty())
            return ArrayList<String>()

        val tokens = ArrayList<String>()
        val newToken = StringBuilder()

        for (ch in text.toLowerCase().toCharArray())
            if (Character.isAlphabetic(ch.toInt()) || Character.isDigit(ch))
                newToken.append(ch)
            else {
                if (newToken.isNotEmpty())
                    tokens.add(newToken.toString())
                newToken.setLength(0)
            }

        if (newToken.isNotEmpty())
            tokens.add(newToken.toString())

        return tokens
    }

    fun mapTokens(tokens: List<String>) = tokens.groupBy { it }.mapValues { it.value.size }
}