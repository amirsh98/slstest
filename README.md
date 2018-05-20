<h1 align="center">Ganmaz <!-- Serve Confidently. --></h1>
<h4 align="center"> 
A Simple and Stupid Lazy Text Classifier

![license](https://img.shields.io/badge/license-MIT-blue.svg)
[![Build Status](https://travis-ci.org/shahrivari/ganmaz.svg?branch=master)](https://travis-ci.org/shahrivari/ganmaz)

</h4>

Ganmaz is a simple text classifier which is based on k-NN (k Nearest Neighbors), the well-knonw 
nonparametric text classification technique.
The program is easy to use and can do classification via rest API.

For building the software, execute:<br>
'mvn package' on the project source root.<br>
The JAR file will be generated in: 'target/ganmaz-1.0.jar'

Then execute Ganmaz in command line via:<br>
java -cp  target/ganmaz-1.0.jar ir.toolki.ganmaz.RestKt<br>
The Rest server will accept texts for classification via:<br>
localhost:5050/classify/text_to_classify using HTTP GET<br>
and localhost:5050/classify using HTTP POST<br>

For testing a dataset run:
java -cp target/ganmaz-1.0.jar ir.toolki.ganmaz.RestKt --test --json sample.json

There are several command line params:<br>
 --json VAL : The path of json file (default: )<br>
 --k N      : The number of Neighbors for classifier. (default: 5)<br>
 --port N   : The port to listen (default: 5050)<br>
 --test     : Test the file. (default: false)<br>
 
 If no json file is given by the user, Ganmaz uses a default sample JSON file which provides a two
  class dataset.
  
  That's all...

