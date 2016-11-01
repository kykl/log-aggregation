package kykl.log

import scala.io.Source

/**
  * AverageSession - process a log file to compute average time spent by each user on an app.
  */
object UserTimeSpentAverageMain {
  def main(args:Array[String]) = {
    if (args.length != 1) {
      System.err.println("Usage: UserTimeSpentAverageMain [user-log-entries-csv-file]\nSample Input File: data/sample.csv")
      System.exit(1)
    }

    // TODO: this loads all entries in memory. We should implements a stream io based solution
    val logEntries = Source.fromFile(args(0)).getLines().map( csv => UserLogEntry(csv) ).toSeq
    val aggregator:LogAggregation = new LogAggregationByDropPrevOpen
    print("[")
    var sep = ""
    aggregator.average(logEntries).foreach { avg =>
      print(sep + avg)
      sep = ", "
    }
    println("]")
  }
}
