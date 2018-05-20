package ir.toolki.ganmaz.classification

import com.google.common.collect.HashMultiset
import com.google.common.collect.Multiset
import com.google.common.collect.Multisets
import java.util.*

class DataSet(documents: List<Document>) {
    private val digestedDocs: MutableList<DigestedDoc> = arrayListOf()
    private var df: Multiset<String> = HashMultiset.create()

    init {
        documents.forEach { TextUtils.tokenizeAndNormalize(it.body()).toSet().forEach { df.add(it) } }
        df = Multisets.copyHighestCountFirst(df)
        documents.forEach { digestedDocs.add(DigestedDoc(it, df)) }
    }

    fun size() = digestedDocs.size

    fun score(doc: Document): List<Pair<DigestedDoc, Double>> {
        val map: MutableMap<DigestedDoc, Double> = hashMapOf()
        val digested = DigestedDoc(doc, df)
        val digestedLen = Math.sqrt(digested.termScore.values.map { it -> it * it }.sum())
        digestedDocs.forEach {
            var sumScore = 0.0
            digested.termScore.forEach { term, score ->
                if (it.termScore.containsKey(term))
                    sumScore += score * it.termScore.get(term)!!.toDouble()
            }
            val itLen = Math.sqrt(it.termScore.values.map { it -> it * it }.sum())
            sumScore /= (digestedLen * itLen)
            map.put(it, sumScore)
        }
        return map.map { it -> Pair(it.key, it.value) }.sortedBy { it -> it.second }.reversed().toList()
    }

    fun classify(doc: Document, k: Int): String {
        val ranked = score(doc)
        var labels = ranked.map { it -> it.first.doc.label }.toList()
        labels = labels.subList(0, Math.min(k, labels.size))
        val map = labels.groupBy { it }
        return Collections.max(map.entries, compareBy { it.value.size }).key
    }

    companion object {
        fun evaluate(train: List<Document>, test: List<Document>): Double {
            val dataset = DataSet(train)
            var corrects = 0
            test.forEach {
                val klass = dataset.classify(it, 5)
                if (klass == it.label)
                    corrects++
            }
            return corrects.toDouble() / test.size
        }
    }
}
