//     Project: scalanative-jdbc
//      Module: sqlite
// Description: Types and enums for bindings to the sqlite3 lib

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn

import scala.scalanative.native._

package object sqlite {

  type ResultCode = CInt
  object ResultCode {
    val LIB_ERROR = -1 // indicates an error generated in the binding layer
    val LIB_MULTIPLE_STMTS = -2 // indicates that a single statement function was called with an SQL string containing multiple statements
    /* beginning of sqlite result codes */
    val OK           = 0   /* Successful result */
    /* beginning-of-error-codes */
    val ERROR        = 1   /* Generic error */
    val INTERNAL     = 2   /* Internal logic error in SQLite */
    val PERM         = 3   /* Access permission denied */
    val ABORT        = 4   /* Callback routine requested an abort */
    val BUSY         = 5   /* The database file is locked */
    val LOCKED       = 6   /* A table in the database is locked */
    val NOMEM        = 7   /* A malloc() failed */
    val READONLY     = 8   /* Attempt to write a readonly database */
    val INTERRUPT    = 9   /* Operation terminated by sqlite3_interrupt()*/
    val IOERR        = 10   /* Some kind of disk I/O error occurred */
    val CORRUPT      = 11   /* The database disk image is malformed */
    val NOTFOUND     = 12   /* Unknown opcode in sqlite3_file_control() */
    val FULL         = 13   /* Insertion failed because database is full */
    val CANTOPEN     = 14   /* Unable to open the database file */
    val PROTOCOL     = 15   /* Database lock protocol error */
    val EMPTY        = 16   /* Not used */
    val SCHEMA       = 17   /* The database schema changed */
    val TOOBIG       = 18   /* String or BLOB exceeds size limit */
    val CONSTRAINT   = 19   /* Abort due to constraint violation */
    val MISMATCH     = 20   /* Data type mismatch */
    val MISUSE       = 21   /* Library used incorrectly */
    val NOLFS        = 22   /* Uses OS features not supported on host */
    val AUTH         = 23   /* Authorization denied */
    val FORMAT       = 24   /* Not used */
    val RANGE        = 25   /* 2nd parameter to sqlite3_bind out of range */
    val NOTADB       = 26   /* File opened that is not a database file */
    val NOTICE       = 27   /* Notifications from sqlite3_log() */
    val WARNING      = 28   /* Warnings from sqlite3_log() */
    val ROW          = 100  /* sqlite3_step() has another row ready */
    val DONE         = 101  /* sqlite3_step() has finished executing */

    def messageForCode(code: ResultCode): String =
//      fromCString(SQLite.API.sqlite3_errstr(code))
    code match {
      case 0 => "SQLITE_OK"
      case 1 => "SQLITE_ERROR"
      case 2 => "SQLITE_INTERNAL"
      case 3 => "SQLITE_PERM: access permission denied"
      case 4 => "SQLITE_ABORT: callback routine requested abort"
      case 5 => "SQLITE_BUSY: the database file is locked"
      case 6 => "SQLITE_LOCKED: a table in the database file is locked"
      case 7 => "SQLITE_NOMEM: a malloc() failed"
      case 8 => "SQLITE_READONLY: attempt to write a readonly"
      case 9 => "SQLITE_INTERRUPT: operation terminated by sqlite3_interrupt()"
      case 10 => "SQLITE_IOERR: some kind of disk I/O error occurred"
      case 11 => "SQLITE_CORRUPT: the database disk image is malformed"
      case 12 => "SQLITE_NOTFOUND: unknown opcode in sqlite3_file_control()"
      case 13 => "SQLITE_FULL: insertion failed because database is full"
      case 14 => "SQLITE_CANTOPEN: unable to open the database file"
      case 15 => "SQLITE_PROTOCOL: database lock protocol error"
      case 16 => "SQLITE_EMPTY"
      case 17 => "SQLITE_SCHEMA: the database schema changed"
      case 18 => "SQLITE_TOOBIG: string or BLOB exceeds size limit"
      case 19 => "SQLITE_CONSTRAINT: abort due to constraint violation"
      case 20 => "SQLITE_MISMATCH: data type mismatch"
      case 21 => "SQLITE_MISUSE: library used incorrectly"
      case 22 => "SQLITE_NOLFS: uses OS features not supported on host"
      case 23 => "SQLITE_AUTH: authorization denied"
      case 24 => "SQLITE_FORMAT"
      case 25 => "SQLITE_RANGE: 2nd parameter to sqlite3_bind() out of range"
      case 26 => "SQLITE_NOTADB: file opened is not a database file"
      case 27 => "SQLITE_NOTICE: notifications from sqlite3_log()"
      case 28 => "SQLITE_WARNING: warnings from sqlite3_log()"
      case 100 => "SQLITE_ROW: sqlite3_step() has another row ready"
      case 101 => "SQLITE_DONE: sqlite3_step() has finished executing"
      case x => s"unknown sqlite result code: $x"
    }
  }

  type FileFlags = CInt
  object FileFlags {
    val OPEN_READONLY         = 0x00000001  /* Ok for sqlite3_open_v2() */
    val OPEN_READWRITE        = 0x00000002  /* Ok for sqlite3_open_v2() */
    val OPEN_CREATE           = 0x00000004  /* Ok for sqlite3_open_v2() */
    val OPEN_DELETEONCLOSE    = 0x00000008  /* VFS only */
    val OPEN_EXCLUSIVE        = 0x00000010  /* VFS only */
    val OPEN_AUTOPROXY        = 0x00000020  /* VFS only */
    val OPEN_URI              = 0x00000040  /* Ok for sqlite3_open_v2() */
    val OPEN_MEMORY           = 0x00000080  /* Ok for sqlite3_open_v2() */
    val OPEN_MAIN_DB          = 0x00000100  /* VFS only */
    val OPEN_TEMP_DB          = 0x00000200  /* VFS only */
    val OPEN_TRANSIENT_DB     = 0x00000400  /* VFS only */
    val OPEN_MAIN_JOURNAL     = 0x00000800  /* VFS only */
    val OPEN_TEMP_JOURNAL     = 0x00001000  /* VFS only */
    val OPEN_SUBJOURNAL       = 0x00002000  /* VFS only */
    val OPEN_MASTER_JOURNAL   = 0x00004000  /* VFS only */
    val OPEN_NOMUTEX          = 0x00008000  /* Ok for sqlite3_open_v2() */
    val OPEN_FULLMUTEX        = 0x00010000  /* Ok for sqlite3_open_v2() */
    val OPEN_SHAREDCACHE      = 0x00020000  /* Ok for sqlite3_open_v2() */
    val OPEN_PRIVATECACHE     = 0x00040000  /* Ok for sqlite3_open_v2() */
    val OPEN_WAL              = 0x00080000  /* VFS only */
  }

  type DataType = CInt
  object DataType {
    val INTEGER  = 1
    val FLOAT    = 2
    val TEXT     = 3
    val BLOB     = 4
    val NULL     = 5
  }
}
