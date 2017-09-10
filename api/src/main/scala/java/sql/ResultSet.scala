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
  def wasNull(): Boolean
}

object ResultSet {
  abstract class AbstractResultSet extends ResultSet {
    def findColumn(columnLabel: String): Int = !!!
    def first(): Boolean = !!!
    def last(): Boolean = !!!
    def getRow(): Int = !!!
    def getBoolean(columnLabel: String): Boolean = getBoolean(findColumn(columnLabel))
    def getDate(columnLabel: String): Date = getDate(findColumn(columnLabel))
    def getDouble(columnLabel: String): Double = getDouble(findColumn(columnLabel))
    def getFloat(columnLabel: String): Float = getFloat(findColumn(columnLabel))
    def getInt(columnLabel: String): Int = getInt(findColumn(columnLabel))
    def getLong(columnLabel: String): Long = getLong(findColumn(columnLabel))
    def getString(columnLabel: String): String = getString(findColumn(columnLabel))
    def getTimestamp(columnLabel: String): Timestamp = getTimestamp(findColumn(columnLabel))
  }

}

