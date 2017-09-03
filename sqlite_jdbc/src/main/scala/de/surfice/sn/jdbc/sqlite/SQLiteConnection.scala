//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.{Connection, Statement}

import de.surfice.sn.sqlite.SQLite

class SQLiteConnection(sqlite: SQLite) extends Connection.AbstractConnection {
  override def createStatement(): Statement = new SQLiteStatement(sqlite)
}
