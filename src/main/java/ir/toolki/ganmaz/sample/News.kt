package ir.toolki.ganmaz.sample

import com.google.common.io.CharStreams
import com.google.common.io.Files
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import ir.toolki.ganmaz.classification.Document
import java.io.File

class News(val title: String,
           override val id: Int,
           @SerializedName("class")
           override val label: String,
           private val body: String) : Document {

    override fun body() = "$title\n$title\n$body"

    companion object {
        val gson = GsonBuilder().setLenient().create()
        fun readSamples(): List<News> {
            return CharStreams.readLines(News::class.java.getResourceAsStream("/sample.json").bufferedReader())
                    // remove empty lines
                    .filter { it.trim().isNotEmpty() }
                    .map { gson.fromJson(it, News::class.java) }.toList()
        }


        fun readFile(path: String): List<News> {
            return Files.readLines(File(path), Charsets.UTF_8)
                    // remove empty lines
                    .filter { it.trim().isNotEmpty() }
                    .map { gson.fromJson(it, News::class.java) }.toList()
        }
    }
}