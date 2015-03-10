# RISCOSS Risk Analyzer

This is the risk analisys core of the RISCOSS project.

## Prerequisites

* Java1.7
* Maven3
* **optional** Linux (the Bayesian Network risk analyzer contains a C language binary)
* **optional** Download and copy the [Smile](https://dslpitt.org/genie) binary library to `riscoss-platform-jsmile/src/main/resources/`. The library to copy depends on your operating system. For Linux/X86_64 it's called `libjsmile.so` (without this the analyser will have limited functionality)

## Build

Just type `./do`

## To Run

Syntax: `java -jar <analyzer>.jar evaluate model1,model2,model3 inputData`

Example: `java -jar ./analyser-0.24.jar evaluate ./test-data/models/Activeness1.xdsl,./test-data/models/License1.xdsl,./test-data/models/Quality1.xdsl,./test-data/models/RiskOW2.xdsl ./test-data/DocDocu_test.json`
