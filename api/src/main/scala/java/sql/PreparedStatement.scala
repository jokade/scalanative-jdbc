package java.sql

import java.sql.Statement.AbstractStatement

trait PreparedStatement extends Statement {
  def executeUpdate(): Int
  def setBoolean(parameterIndex: Int, x: Boolean): Unit
  def setDate(parameterIndex: Int, x: Date): Unit
  def setDouble(parameterIndex: Int, x: Double): Unit
  def setFloat(parameterIndex: Int, x: Float): Unit
  def setInt(parameterIndex: Int, x: Int): Unit
  def setLong(parameterIndex: Int, x: Long): Unit
  def setNull(parameterIndex: Int, sqlType: Int): Unit
  def setString(parameterIndex: Int, x: String): Unit
  def setTimestamp(parameterIndex: Int, x: Timestamp): Unit
}

object PreparedStatement {
  trait AbstractPreparedStatement extends AbstractStatement with PreparedStatement {
  }
}
