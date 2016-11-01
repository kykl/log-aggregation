import java.util.Date

import kykl.log.{LogAggregationByDropPrevOpen, UserAction, UserLogEntry, UserTimeSpent}
import org.scalatest._

class LogAggregationSpec extends FlatSpec with Matchers {
  val aggregator = new LogAggregationByDropPrevOpen

  "A LogAggregation" should "return empty list when the input is empty" in {
    val result = aggregator.average(List[UserLogEntry]())
    result.size should be(0)
  }

  it should "return time spent: 0.0 when user log entries contains only open actions" in {
    val result = aggregator.average(List[UserLogEntry](
      UserLogEntry(1, 1435456566, UserAction.Open),
      UserLogEntry(1, 1435461234, UserAction.Open)
    ))
    result.size should be(1)
    result.last should be(UserTimeSpent(1, 0.0))
  }

  it should "return list of expected time spent as in data/sample.csv" in {
    val sample =
      """|1,1435456566,open
        |2,1435457643,open
        |3,1435458912,open
        |1,1435459567,close
        |4,1435460345,open
        |1,1435461234,open
        |2,1435462567,close
        |1,1435463456,open
        |3,1435464398,close
        |4,1435465122,close
        |1,1435466775,close""".stripMargin

    val lines = sample.split("\n")
    val entries = lines.map ( l => UserLogEntry(l) )
    val result = aggregator.average(entries)

    // [{2, 4924.0}, {4, 4777.0}, {1, 3160.0}, {3, 5486.0}]
    result should be(List[UserTimeSpent](
      UserTimeSpent(2, 4924.0),
      UserTimeSpent(4, 4777.0),
      UserTimeSpent(1, 3160.0),
      UserTimeSpent(3, 5486.0)
    ))
  }

  it should "produce result correctly with some random input" in {
    case class UserData(uid:Int, timeSpent:Seq[Int]) {
      def average() = UserTimeSpent(uid, timeSpent.reduce(_ + _).toDouble / timeSpent.length)
    }

    def getUserData(uid:Int, size:Int, range:Int):UserData = {
      UserData(uid, 1 to size map (_ => util.Random.nextInt(range)))
    }

    val userData = List(getUserData(1, 1000, 1000), getUserData(2, 2000, 10000))

    // Generate user 1 and 2 data
    var last = (new Date).getTime
    val entries = new scala.collection.mutable.ListBuffer[UserLogEntry]()
    userData.foreach { u =>
      u.timeSpent.foreach { t =>
        val close = last + t
        entries.append(UserLogEntry(u.uid, last, UserAction.Open))
        entries.append(UserLogEntry(u.uid, close, UserAction.Close))
        last = close + util.Random.nextInt(10000)
      }
    }

    // Compare result with generated data
    val result = aggregator.average(entries)
    result.foreach { r =>
      val u = userData(r.uid - 1)
      r should be(u.average)
    }
  }
}

