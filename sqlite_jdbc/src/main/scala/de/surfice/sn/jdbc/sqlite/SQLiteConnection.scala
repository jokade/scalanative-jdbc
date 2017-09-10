//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.{Connection, PreparedStatement, Statement}

import de.surfice.sn.sqlite.{ResultCode, SQLite, SQLiteException}

class SQLiteConnection(sqlite: SQLite) extends Connection.AbstractConnection {
  override def createStatement(): Statement = SQLiteStatement(sqlite)
  override def close(): Unit = handleException( SQLite.handleError(sqlite, sqlite.close()) )
  override def prepareStatement(sql: String): PreparedStatement = new SQLiteStmtWrapper(sqlite.prepareStatement(sql))
}
