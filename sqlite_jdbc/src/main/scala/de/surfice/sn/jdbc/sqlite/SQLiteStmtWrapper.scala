//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql._

import de.surfice.sn.sqlite.{DataType, ResultCode, SQLite, SQLiteStmt}

final class SQLiteStmtWrapper(stmt: SQLiteStmt)
  extends ResultSet.AbstractResultSet
    with SQLiteStatement
    with PreparedStatement.AbstractPreparedStatement {
  private var _closed = false
  private var _lastResultCol = 0
  def sqlite: SQLite = stmt.dbHandle
  def closed: Boolean = _closed
  override def close(): Unit = check {
    _closed = true
    SQLite.handleError(stmt.dbHandle,stmt.close())
  }
  def next(): Boolean = check{ stmt.execute() match {
      case ResultCode.ROW => true
      case _ => false
    }
  }

  def getBoolean(col: Int): Boolean = check( stmt.column_int(column(col)) != 0 )
  def getDate(col: Int): Date =
    if( stmt.column_type(column(col)) == DataType.NULL) null
    else new Date( check(stmt.column_int64(column(col))) )
  def getDouble(col: Int): Double = check(stmt.column_double(column(col)))
  def getFloat(col: Int): Float = getDouble(col).toFloat
  def getInt(col: Int): Int = check(stmt.column_int(column(col)))
  def getLong(col: Int): Long = check(stmt.column_int64(column(col)))
  def getString(col: Int): String = check(stmt.column_string(column(col)))
  def getTimestamp(col: Int): Timestamp =
    if(stmt.column_type(column(col)) == DataType.NULL ) null
    else new Timestamp( check(stmt.column_int64(column(col))) )

  def wasNull(): Boolean = stmt.column_type(_lastResultCol) == DataType.NULL

  def setBoolean(parameterIndex: Int, x: Boolean): Unit = check( stmt.bind_int(parameterIndex, if(x) 1 else 0) )
  def setDate(parameterIndex: Int, x: Date): Unit = check{
    if(x==null)
      stmt.bind_null(parameterIndex)
    else
      stmt.bind_int64(parameterIndex,x.getTime)
  }
  def setDouble(parameterIndex: Int, x: Double): Unit = check( stmt.bind_double(parameterIndex,x) )
  def setFloat(parameterIndex: Int, x: Float): Unit = check( stmt.bind_double(parameterIndex,x) )
  def setInt(parameterIndex: Int, x: Int): Unit = check( stmt.bind_int(parameterIndex,x) )
  def setLong(parameterIndex: Int, x: Long): Unit = check( stmt.bind_int64(parameterIndex,x) )
  def setNull(parameterIndex: Int, sqlType: Int): Unit = check( stmt.bind_null(parameterIndex) )
  def setString(parameterIndex: Int, x: String): Unit = check( stmt.bind_string(parameterIndex,x) )
  def setTimestamp(parameterIndex: Int, x: Timestamp): Unit = check{
    if(x==null)
      stmt.bind_null(parameterIndex)
    else
      stmt.bind_int64(parameterIndex,x.getTime)
  }

  def executeUpdate(): Int = check{
    stmt.reset()
    stmt.execute()
    0
  }

  @inline private def check[T](f: =>T): T =
    if(_closed)
      throw new SQLException("Statement or ResultSet already closed!")
    else
      handleException( f )

  @inline private def column(id: Int): Int =
    if(id<1)
      throw new SQLException(s"invalid column index: $id")
    else {
      _lastResultCol = id - 1
      _lastResultCol
    }

}
