package java.sql

trait Statement {
  def close(): Unit
  def execute(sql: String): Boolean
  def executeQuery(sql: String): ResultSet
}

object Statement {
  abstract class AbstractStatement extends Statement {
    def close(): Unit = {}
    def execute(sql: String): Boolean = ???
    def executeQuery(sql: String): ResultSet = ???
  }
}