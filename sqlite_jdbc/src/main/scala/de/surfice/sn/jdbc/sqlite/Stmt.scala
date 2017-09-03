//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.{Date, ResultSet, SQLException, Timestamp}

import de.surfice.sn.sqlite.{ResultCode, SQLitePreparedStatement}

class Stmt(stmt: SQLitePreparedStatement) extends ResultSet.ResultHolder {
  var _closed = false
  def closed: Boolean = _closed
  def close(): Unit = if(!_closed) stmt.close() else throw new SQLException("Statement or ResultSet already closed!")
  def raw: SQLitePreparedStatement = stmt
  def next(): Boolean =
    if(_closed)
      throw new SQLException("Statement or ResultSet already closed")
    else stmt.execute() match {
      case ResultCode.ROW => true
      case _ => false
    }

  def boolean(col: Int): Boolean = handleException( stmt.column_int(col) != 0 )
  def date(col: Int): Date = new Date( handleException(stmt.column_int64(col)) )
  def double(col: Int): Double = handleException(stmt.column_double(col))
  def float(col: Int): Float = double(col).toFloat
  def int(col: Int): Int = handleException(stmt.column_int(col))
  def long(col: Int): Long = handleException(stmt.column_int64(col))
  def string(col: Int): String = handleException(stmt.column_string(col))
  def timestamp(col: Int): Timestamp = new Timestamp( handleException(stmt.column_int64(col)) )
}
