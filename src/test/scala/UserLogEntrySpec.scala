import kykl.log.{UserAction, UserLogEntry}
import org.scalatest.{FlatSpec, Matchers}

class UserLogEntrySpec extends FlatSpec with Matchers {

  "A UserLogEntry CSV" should "have user id, timestamp and action" in {
    UserLogEntry("1,1435456566,open") should be(new UserLogEntry(1, 1435456566, UserAction.Open))
  }

  it should "contain 3 fields" in {
    intercept[IllegalArgumentException] {
      UserLogEntry("")
    }
  }

  it should "contain integer user id" in {
    intercept[IllegalArgumentException] {
      UserLogEntry("a,123,open")
    }
  }

  it should "contain long timestamp" in {
    intercept[IllegalArgumentException] {
      UserLogEntry("1,a,open")
    }
  }

  it should "have either open or close action" in {
    intercept[IllegalArgumentException] {
      UserLogEntry("1,1435456566,install")
    }
  }

}
