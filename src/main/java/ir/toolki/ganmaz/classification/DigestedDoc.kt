package ir.toolki.ganmaz.classification

import com.google.common.collect.Multiset

class DigestedDoc(val doc: Document, df: Multiset<String>) {
    val terms: List<String> = TextUtils.tokenizeAndNormalize(doc.body())
    val termScore: MutableMap<String, Double> = hashMapOf()

    init {
        val freqs = TextUtils.mapTokens(terms)
        freqs.keys.forEach {
            val tf = 1 + Math.log10(freqs.getOrDefault(it, 0).toDouble())
            val idf = Math.log(df.elementSet().size.toDouble() / (1 + df.count(it)))
            termScore.put(it, tf * idf)
        }
    }
}