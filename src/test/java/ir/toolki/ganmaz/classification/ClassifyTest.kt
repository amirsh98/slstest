package ir.toolki.ganmaz.classification

import com.google.common.math.Stats
import ir.toolki.ganmaz.sample.News
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*


class KotlinJunitTest {

    @Test
    fun randomPrecisionTest() {
        val docs = News.readSamples()
        val stats = Stats.of(Evaluator.randomPrecisionTest(docs))
        assertThat(stats.mean()).isGreaterThan(0.85)
    }

    @Test
    fun tenFoldTest() {
        val docs = News.readSamples()
        val stats = Stats.of(Evaluator.tenFold(docs))
        assertThat(stats.mean()).isGreaterThan(0.85)
    }

}
