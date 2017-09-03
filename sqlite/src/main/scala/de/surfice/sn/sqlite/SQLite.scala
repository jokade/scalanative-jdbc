//     Project: scalanative-jdbc
//      Module: sqlite
// Description: Bindings to the sqlite lib

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.sqlite

import scala.scalanative.native._

final class SQLite private() {
  import SQLite._
  @inline def close(): ResultCode = API.sqlite3_close(this.cast[SQLitePtr])
  @inline def exec(sql: CString, callback: Callback, arg: Ptr[Byte], errmsg: Ptr[CString]): ResultCode =
    API.sqlite3_exec(this.cast[SQLitePtr],sql,callback,arg,errmsg)
  @inline def prepare_v2(zSql: CString, nByte: CInt, stmt: Ptr[PreparedStatementPtr], pzTail: Ptr[CString]): ResultCode =
    API.sqlite3_prepare_v2(this.cast[SQLitePtr],zSql,nByte,stmt,pzTail)
}


object SQLite {
  type SQLitePtr = Ptr[Byte]
  type Callback = CFunctionPtr4[Ptr[Byte],CInt,Ptr[CString],Ptr[CString],CInt]
  type ResultHandler = (CInt)=>CInt
  type PreparedStatementPtr = Ptr[Byte]

  @extern
  object API {
    def sqlite3_open(filename: CString, ppDb: Ptr[SQLitePtr]): ResultCode = extern
    def sqlite3_open_v2(filename: CString, ppDb: Ptr[SQLitePtr], flags: FileFlags, zVfs: CString): ResultCode = extern
    def sqlite3_close(db: SQLitePtr): ResultCode = extern
    def sqlite3_exec(db: SQLitePtr, sql: CString, callback: Callback, arg: Ptr[Byte], errmsg: Ptr[CString]): ResultCode = extern
    def sqlite3_malloc(size: CInt): Ptr[Byte] = extern
    def sqlite3_free(ptr: Ptr[Byte]): Unit = extern
    def sqlite3_prepare_v2(db: SQLitePtr, zSql: CString, nByte: CInt, stmt: Ptr[PreparedStatementPtr], pzTail: Ptr[CString]): ResultCode = extern
    def sqlite3_step(stmt: PreparedStatementPtr): ResultCode = extern
    def sqlite3_db_handle(stmt: PreparedStatementPtr): SQLitePtr = extern
    def sqlite3_finalize(stmt: PreparedStatementPtr): ResultCode = extern
    def sqlite3_errmsg(db: SQLitePtr): CString = extern
    def sqlite3_errstr(rc: ResultCode): CString = extern
    def sqlite3_data_count(stmt: PreparedStatementPtr): CInt = extern
    def sqlite3_column_name(stmt: PreparedStatementPtr, col: CInt): CString = extern
    def sqlite3_column_type(stmt: PreparedStatementPtr, col: CInt): DataType = extern
    def sqlite3_column_int(stmt: PreparedStatementPtr, col: CInt): CInt = extern
    def sqlite3_column_int64(stmt: PreparedStatementPtr, col: CInt): CLong = extern
    def sqlite3_column_double(stmt: PreparedStatementPtr, col: CInt): CDouble = extern
    def sqlite3_column_text(stmt: PreparedStatementPtr, col: CInt): CString = extern
  }


  /**
   * Opens the specified database file.
   *
   * @param filename Database file to be opened
   * @param flags mode flags
   *
   * @throws SQLiteException if the DB could not be opened
   */
  def apply(filename: CString, flags: FileFlags): SQLite = Zone{ implicit z =>
    val ppDb = stackalloc[SQLitePtr](sizeof[SQLitePtr])
    val resultCode = API.sqlite3_open_v2(filename,ppDb,flags,null)
    if(resultCode != ResultCode.OK)
      throw SQLiteException(s"could not open database '${fromCString(filename)}'", resultCode)
    else
      (!ppDb).cast[SQLite]
  }

  def apply(filename: CString): SQLite = apply(filename, FileFlags.OPEN_READWRITE | FileFlags.OPEN_CREATE)
  def apply(filename: String): SQLite = apply(filename, FileFlags.OPEN_READWRITE | FileFlags.OPEN_CREATE)
  def apply(filename: String, flags: FileFlags): SQLite = Zone{ implicit z => apply(toCString(filename),flags) }
  def readonly(filename: String): SQLite = apply(filename, FileFlags.OPEN_READONLY)

  /**
   * Creates a memory-only DB
   */
  def apply(): SQLite = apply(c":memory:")

  implicit final class RichSQLite(val db: SQLite) extends AnyVal {
    def prepareStatement(sql: String): SQLitePreparedStatement = prepare(db,sql)
    def execute(sql: String): Unit = executeSql(db,sql)
  }


//  private def handleResult(arg: Ptr[Byte], columns: CInt, a: Ptr[CString], b: Ptr[CString]): CInt = {
//    val handler = arg.cast[ResultHandler]
//    handler(count)
//  }

//  private val handleResultPtr = CFunctionPtr.fromFunction4(handleResult)


  private def prepare(db: SQLite, sql: String): SQLitePreparedStatement = Zone{ implicit z =>
    var zSql = toCString(sql)
    val stmt = stackalloc[PreparedStatementPtr]( sizeof[PreparedStatementPtr] )
    val pzTail = stackalloc[CString]( sizeof[CString] )
    handleError( db, API.sqlite3_prepare_v2(db.cast[SQLitePtr],zSql,-1,stmt,pzTail) )
    if(!(!pzTail) != 0)
      throw SQLiteException("cannot prepare multiple statements",ResultCode.LIB_MULTIPLE_STMTS)
    (!stmt).cast[SQLitePreparedStatement]
  }

  private def executeSql(db: SQLite, sql: String): Unit = Zone{ implicit z =>
    var zSql = toCString(sql.trim)
    val stmt = stackalloc[PreparedStatementPtr]( sizeof[PreparedStatementPtr] )
    val pzTail = stackalloc[CString]( sizeof[CString] )
    do {
      handleError( db, API.sqlite3_prepare_v2(db.cast[SQLitePtr],zSql,-1,stmt,pzTail) )
      val s = (!stmt).cast[SQLitePreparedStatement]
      handleError(db, s.execute())
      s.close()
      zSql = !pzTail
    } while(!(!pzTail) != 0)
  }

  def handleError(db: SQLite, resultCode: ResultCode): ResultCode = resultCode match {
    case ResultCode.OK | ResultCode.DONE | ResultCode.ROW =>
      resultCode
    case _ =>
      val msg = fromCString(API.sqlite3_errmsg(db.cast[SQLitePtr]))
      throw SQLiteException(msg,resultCode)
  }
}
