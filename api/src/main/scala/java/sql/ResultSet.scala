package java.sql

trait ResultSet {
  def close(): Unit
  def findColumn(columnLabel: String): Int
  def first(): Boolean
  def last(): Boolean
  def getRow(): Int
  def getBoolean(columnIndex: Int): Boolean
  def getBoolean(columnLabel: String): Boolean
  def getDate(columnIndex: Int): Date
  def getDate(columnLabel: String): Date
  def getDouble(columnIndex: Int): Double
  def getDouble(columnLabel: String): Double
  def getFloat(columnIndex: Int): Float
  def getFloat(columnLabel: String): Float
  def getInt(columnIndex: Int): Int
  def getInt(columnLabel: String): Int
  def getLong(columnIndex: Int): Long
  def getLong(columnLabel: String): Long
  def getString(columnIndex: Int): String
  def getString(columnLabel: String): String
  def getTimestamp(columnIndex: Int): Timestamp
  def getTimestamp(columnLabel: String): Timestamp
  def next(): Boolean
}

object ResultSet {
  def wrap(rh: ResultHolder): ResultSet = new Wrapper(rh)

  final class Wrapper(rh: ResultHolder) extends ResultSet {
    def close(): Unit = rh.close()
    def findColumn(columnLabel: String): Int = rh.findColumn(columnLabel)
    def first(): Boolean = rh.first()
    def last(): Boolean = rh.last()
    def getRow(): Int = rh.getRow()
    def next(): Boolean = rh.next()
    def getBoolean(columnIndex: Int): Boolean = rh.boolean(columnIndex)
    def getBoolean(columnLabel: String): Boolean = getBoolean(findColumn(columnLabel))
    def getDate(columnIndex: Int): Date = rh.date(columnIndex)
    def getDate(columnLabel: String): Date = getDate(findColumn(columnLabel))
    def getDouble(columnIndex: Int): Double = rh.double(columnIndex)
    def getDouble(columnLabel: String): Double = getDouble(findColumn(columnLabel))
    def getFloat(columnIndex: Int): Float = rh.float(columnIndex)
    def getFloat(columnLabel: String): Float = getFloat(findColumn(columnLabel))
    def getInt(columnIndex: Int): Int = rh.int(columnIndex)
    def getInt(columnLabel: String): Int = getInt(findColumn(columnLabel))
    def getLong(columnIndex: Int): Long = rh.long(columnIndex)
    def getLong(columnLabel: String): Long = getLong(findColumn(columnLabel))
    def getString(columnIndex: Int): String = rh.string(columnIndex)
    def getString(columnLabel: String): String = getString(findColumn(columnLabel))
    def getTimestamp(columnIndex: Int): Timestamp = rh.timestamp(columnIndex)
    def getTimestamp(columnLabel: String): Timestamp = getTimestamp(findColumn(columnLabel))
  }

  trait ResultHolder {
    def close(): Unit
    def findColumn(columnLabel: String): Int = !!!
    def first(): Boolean = !!!
    def last(): Boolean = !!!
    def getRow(): Int = !!!
    def next(): Boolean
    def boolean(col: Int): Boolean
    def date(col: Int): Date
    def double(col: Int): Double
    def float(col: Int): Float
    def int(col: Int): Int
    def long(col: Int): Long
    def string(col: Int): String
    def timestamp(col: Int): Timestamp
  }
}


object ResultSetWrapper {

}
