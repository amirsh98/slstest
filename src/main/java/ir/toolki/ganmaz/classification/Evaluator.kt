package ir.toolki.ganmaz.classification

import com.google.common.math.Stats
import java.util.*

object Evaluator {
    fun randomPrecisionTest(docs: List<Document>):List<Double> {
        val list = mutableListOf<Double>()
        for (i in 1..10) {
            Collections.shuffle(docs)
            val train = docs.subList(0, docs.size * 2 / 3)
            val test = docs.subList(docs.size * 2 / 3, docs.size)
            val precision = DataSet.evaluate(train, test)
            list.add(precision)
            println("ROUND-$i: The precision is $precision")
        }

        val stats = Stats.of(list)
        println("AVERAGE PRECISION: ${stats.mean()} \u00B1 ${stats.sampleStandardDeviation()}")
        println("MAX PRECISION: ${stats.max()}")
        println("MIN PRECISION: ${stats.min()}")
        return list
    }

    fun tenFold(docs: List<Document>):List<Double> {
        val list = mutableListOf<Double>()
        Collections.shuffle(docs)
        val folds = mutableListOf<List<Document>>()
        for (j in 0..9)
            folds.add(docs.subList(j * docs.size / 10, (j + 1) * docs.size / 10))

        for (i in 0..9) {
            val train = mutableListOf<Document>()
            for (j in 0..9)
                if (j != i)
                    train.addAll(folds[j])
            val test = folds[i]
            val precision = DataSet.evaluate(train, test)
            list.add(precision)
            println("FOLD-$i: The precision is $precision")
        }

        val stats = Stats.of(list)
        println("10-FOLD PRECISION: ${stats.mean()} \u00B1 ${stats.sampleStandardDeviation()}")
        return list
    }
}