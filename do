#!/bin/bash
die() { echo $1; exit 100; }
MVN=`which mvn` || die "could not find maven";
VER=`cat ./pom.xml | sed -n -e 's/^ *<version>\(.*\)<\/version>$/\1/p' | head -1` || die "ver";
MDLS=`ls ./test-data/models/ | awk 'BEGIN{x=""}{x = x",./test-data/models/"$0}END{print substr(x,2)}'` || die "mdls";

mvn clean install || die "failed to build";

RES="./riscoss-remote-risk-analyser/target/riscoss-remote-risk-analyser-${VER}-jar-with-dependencies.jar";

echo -e "\n\nRunning Tests\n\n";

ls ./test-data/ | grep '.json$' | while read x; do
    echo java -jar $RES evaluate $MDLS ./test-data/$x;
    echo;
    java -jar $RES evaluate $MDLS ./test-data/$x || die "test failed";
done || exit 100;

cp $RES ./analyser-${VER}.jar;

echo "Success";


