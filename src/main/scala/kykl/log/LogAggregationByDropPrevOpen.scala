package kykl.log


/** User id and time spent */
case class UserTimeSpent(uid:Int, timeSpent:Double) {
  override def toString = s"{${uid}, ${timeSpent}}"
}

/** Log aggreation interface to compute average */
trait LogAggregation {

  /** Returns time spent for each users in log entries
    *
    * @param entries user log entires
    */
  def average(entries:Seq[UserLogEntry]):Seq[UserTimeSpent]
}

/** This log aggregation drops previous open entries when it cannot find matching close entries */
class LogAggregationByDropPrevOpen extends LogAggregation {
  private case class UserTotalTimeSpent(uid: Int, totalTimeSpent: Long = 0, numberOfSessions: Int = 0, lastOpen: Option[Long] = None) {
    def average = if (numberOfSessions <= 0) 0.0 else totalTimeSpent.toDouble / numberOfSessions
    override def toString = s"{${uid}, ${average}}"
  }

  override def average(entries: Seq[UserLogEntry]): Seq[UserTimeSpent] = {
    // group by user id
    entries.groupBy(_.uid).map{ kv =>
      val (uid, actions) = kv

      // reduce by sum up total session time (close timestamp - open timestamp)
      actions.foldLeft(UserTotalTimeSpent(uid)) { (result, ua) =>
        val current = result.lastOpen match {
          case None =>
            // first open
            if (ua.isOpen)
              UserTotalTimeSpent(result.uid, result.totalTimeSpent, result.numberOfSessions, Some(ua.timestamp))
            else
              result
          case Some(lastOpen) =>
            if (ua.isOpen)
              // drop previous open
              UserTotalTimeSpent(result.uid, result.totalTimeSpent, result.numberOfSessions, Some(ua.timestamp))
            else
              UserTotalTimeSpent(result.uid, result.totalTimeSpent + (ua.timestamp - lastOpen), result.numberOfSessions + 1, None)
        }
        current
      }
    }.map { session => UserTimeSpent(session.uid, session.average) }.toSeq
  }
}

