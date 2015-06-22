# RISCOSS Risk analyser

This is the risk analisys core of the RISCOSS project.

## Prerequisites

* Java1.7
* Maven3
* **optional** Linux (the Bayesian Network risk analyser contains a C language binary)
* **optional** Download and copy the [Smile](https://dslpitt.org/genie) binary library to `riscoss-platform-jsmile/src/main/resources/`. The library to copy depends on your operating system. For Linux/X86_64 it's called `libjsmile.so` (without this the analyser will have limited functionality)

## Build

    mvn clean install
    cp ./riscoss-remote-risk-analyser/target/riscoss-remote-risk-analyser-0.0.1-SNAPSHOT-jar-with-dependencies.jar ./analyser.jar

## Run

After you finish the build, you will find there is a .jar file which can be run standalone.
There are 2 command line arguments for this jar file, one is `getInputs` and the other is `evaluate`.

Syntax: `java -jar <analyser>.jar <getInputs|evaluate> <model>[[,<model2>],<model3>] [<inputData>]`

### Example

`java -jar ./analyser.jar getInputs ./test-data/models/github_maintenance_risk-1426610842323.xml`
This will output a base set of input values which can be fed back as inputs to get a (useless) evaluation.

To make the evaluation useful, fill the values from the `getInputs` request with real data and then
feed the JSON structure back to the evaluator with the `evaluate` command.

`java -jar ./analyser.jar evaluate ./test-data/models/github_maintenance_risk-1426610842323.xml ./my_input_data.json`
