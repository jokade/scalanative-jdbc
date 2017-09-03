//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.Connection
import java.sql.DataSource.AbstractDataSource

import de.surfice.sn.sqlite.SQLite

class SQLiteDataSource(url: String) extends AbstractDataSource {
  override def getConnection(): Connection = new SQLiteConnection(SQLite(url))

  override def getConnection(username: String, password: String): Unit = ???
}

object SQLiteDataSource {
  def apply(): SQLiteDataSource = new SQLiteDataSource(":memory:")
  def apply(url: String): SQLiteDataSource = new SQLiteDataSource(url)
}
