package kykl.log

/** User's open and close actions */
object UserAction extends Enumeration {
  val Open, Close = Value
}

/** Factory for [[kykl.log.UserLogEntry]] instances. */
object UserLogEntry {
  /** Creates an user action with a comma separated string
    *
    *  @param csv comman separated uid, timestampe, action fields
    */
  def apply(csv:String):UserLogEntry = {
    val splits = csv.split(",[ ]*")
    if (splits.length != 3)
      throw new IllegalArgumentException(s"Input: '${csv}' expected 3 comma separated fields but got ${splits.length}")
    val uid = splits(0).toInt
    val timestamp = splits(1).toLong
    val action = splits(2) match {
      case "open" => UserAction.Open
      case "close" => UserAction.Close
      case _ => throw new IllegalArgumentException(s"${splits(2)} action not recognized")
    }

    new UserLogEntry(uid, timestamp, action)
  }
}

/** An user action
  *
  *  @constructor create an user action with an uid, time stamp and action
  *  @param uid the user id
  *  @param timestamp the time stamp
  *  @param action the action with values
  */
case class UserLogEntry(uid: Int, timestamp: Long, action: UserAction.Value) {

  /** Returns if this is an open action */
  def isOpen() = action == UserAction.Open
}