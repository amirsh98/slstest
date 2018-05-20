package ir.toolki.ganmaz

import com.google.common.base.Stopwatch
import ir.toolki.ganmaz.classification.DataSet
import ir.toolki.ganmaz.classification.Document
import ir.toolki.ganmaz.classification.Evaluator
import ir.toolki.ganmaz.sample.News
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.slf4j.LoggerFactory
import spark.Response
import spark.Spark.*
import kotlin.system.exitProcess

class Rest {}

var logger = LoggerFactory.getLogger(Rest().javaClass.simpleName)

fun main(args: Array<String>) {
    val opts = CmdOpts()
    val parser = CmdLineParser(opts)
    try {
        parser.parseArgument(*args)
    } catch (e: CmdLineException) {
        parser.printUsage(System.out)
        System.exit(-1)
    }

    if (opts.test) {
        if (opts.json.isNullOrEmpty()) {
            logger.error("You must provide a file!")
            exitProcess(1)
        }
        logger.info("Reading from: {}", opts.json)
        val list = News.readFile(opts.json)
        logger.info("JSON loaded from: {}", opts.json)
        logger.info("Running random test...")
        Evaluator.randomPrecisionTest(list)
        logger.info("Running 10-fold test...")
        Evaluator.tenFold(list)
        exitProcess(0)
    }


    var dataset: DataSet
    if (opts.json.isEmpty()) {
        logger.warn("No JSON given! Reading default json....")
        dataset = DataSet(News.readSamples())
    } else {
        dataset = DataSet(News.readFile(opts.json))
        logger.info("JSON loaded from: {}", opts.json)
    }

    logger.info("JSON loaded :)")
    logger.info("Total docs: {}", dataset.size())

    port(opts.port)


    fun doResponse(text: String, resp: Response): String {
        val stopWatch = Stopwatch.createStarted()
        logger.info("Classifying => {}", text)
        resp.status(200)
        resp.type("text/plain")
        val doc = object : Document {
            override val id = -1
            override val label = ""
            override fun body() = text
        }
        val klass = dataset.classify(doc, opts.k)
        logger.info("Classified as {} in {}", klass, stopWatch)
        return klass
    }


    post("/classify") { req, resp -> doResponse(req.body(), resp) }
    get("/classify/:q") { req, resp -> doResponse(req.params(":q"), resp) }

    awaitInitialization()

    logger.info("Listening on port: {}", opts.port)
    logger.info("Ready for action....")
}

class CmdOpts {
    @Option(name = "--port", usage = "The port to listen")
    var port: Int = 5050
    @Option(name = "--k", usage = "The number of Neighbors for classifier.")
    var k: Int = 5
    @Option(name = "--json", usage = "The path of json file")
    var json: String = ""
    @Option(name = "--test", usage = "Test the file.")
    var test: Boolean = false
}
