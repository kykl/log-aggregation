# log-aggregation
Process a log file to compute average time spent by each user on an app according to this document: [docs/LogAggregationinScalaWorkSampleQuestion.pdf](docs/LogAggregationinScalaWorkSampleQuestion.pdf)

This process fits really well in data stream processing using Spark, Akka Stream or Reative Stream like distributed framework, but it is done using in memory collection and functional combinators like groupBy and foldLeft for illustration purpose.

## Requirements 

* sbt 0.13.6+
* java 1.8

## Test with sbt

```bash
sbt test
```

## Run with sbt

```bash
sbt "run data/sample.csv"
```

## Build and Run

If you want to build a assembly jar file to run main program kykl.log.UserTimeSpentAverageMain, you should do this:
```bash
sbt assembly && java -jar target/scala-2.11/log-aggregation-assembly-1.0.jar data/sample.csv
```

