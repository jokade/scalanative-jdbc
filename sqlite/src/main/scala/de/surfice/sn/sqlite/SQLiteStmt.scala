//     Project: scalanative-jdbc
//      Module: sqlite
// Description: Binding to sqlite3_stmt

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.sqlite

import scalanative.native._
import de.surfice.sn.sqlite.SQLite.{API, Destructor, PreparedStatementPtr}

final class SQLiteStmt private() {
  @inline def step(): ResultCode = API.sqlite3_step(this.cast[PreparedStatementPtr])
  @inline def reset(): ResultCode = API.sqlite3_reset(this.cast[PreparedStatementPtr])
  @inline def data_count(): CInt = API.sqlite3_data_count(this.cast[PreparedStatementPtr])
  @inline def column_name(col: CInt): CString = API.sqlite3_column_name(this.cast[PreparedStatementPtr],col)
  @inline def column_type(col: CInt): DataType = API.sqlite3_column_type(this.cast[PreparedStatementPtr],col)
  @inline def column_int(col: CInt): CInt = API.sqlite3_column_int(this.cast[PreparedStatementPtr],col)
  @inline def column_int64(col: CInt): CLong = API.sqlite3_column_int64(this.cast[PreparedStatementPtr],col)
  @inline def column_double(col: CInt): CDouble = API.sqlite3_column_double(this.cast[PreparedStatementPtr],col)
  @inline def column_text(col: CInt): CString = API.sqlite3_column_text(this.cast[PreparedStatementPtr],col)
  @inline def bind_int(col: CInt, value: CInt): ResultCode = API.sqlite3_bind_int(this.cast[PreparedStatementPtr],col,value)
  @inline def bind_int64(col: CInt, value: CLong): ResultCode = API.sqlite3_bind_int64(this.cast[PreparedStatementPtr],col,value)
  @inline def bind_double(col: CInt, value: CDouble): ResultCode = API.sqlite3_bind_double(this.cast[PreparedStatementPtr],col,value)
  @inline def bind_text(col: CInt, value: CString, size: CInt, destructor: Destructor): ResultCode =
    API.sqlite3_bind_text(this.cast[PreparedStatementPtr],col,value,size,destructor)
  @inline def bind_null(col: CInt): ResultCode = API.sqlite3_bind_null(this.cast[PreparedStatementPtr],col)
  /**
   * Calls sqlite3_finalize()
   */
  @inline def close(): ResultCode = API.sqlite3_finalize(this.cast[PreparedStatementPtr])
}

object SQLiteStmt {

  implicit final class RichSQLitePreparedStatement(val stmt: SQLiteStmt) extends AnyVal {
    /**
     * Executes this prepared statement and handles any error by throwing an SQLiteException.
     *
     * @throws SQLiteException if an error occurred
     */
    @inline def execute(): ResultCode = {
      stmt.step() match {
        case rc @ (ResultCode.DONE | ResultCode.ROW) =>
          rc
        case rc =>
          SQLite.handleError(dbHandle,rc)
      }
    }

    def dbHandle: SQLite = API.sqlite3_db_handle(stmt.cast[PreparedStatementPtr]).cast[SQLite]

    /**
     * Returns the name of the specified column (starting at 0).
     *
     * @param col column index
     */
    def columnName(col: Int): String =  stmt.column_name(col) match {
      case null => throw SQLiteException(s"invalid column index: $col",ResultCode.LIB_ERROR)
      case name => fromCString(name)
    }

    def column_string(col: Int): String = stmt.column_text(col) match {
      case null => null
      case text => fromCString(text)
    }

    def bind_string(col: Int, value: String): ResultCode =
      if(value == null)
        stmt.bind_null(col)
      else Zone{ implicit z =>
        SQLite.handleError(dbHandle, API.sqlite3_bind_text(stmt.cast[PreparedStatementPtr],col,toCString(value),-1,SQLite.SQLITE_STATIC))
      }
  }

}
