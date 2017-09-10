//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.{ResultSet, Statement}

import de.surfice.sn.sqlite.SQLite

trait SQLiteStatement extends Statement.AbstractStatement {
  def sqlite: SQLite

  override def execute(sql: String): Boolean = handleException{
    sqlite.execute(sql)
    false
  }

  override def executeQuery(sql: String): ResultSet = handleException{
    new SQLiteStmtWrapper( sqlite.prepareStatement(sql) )
  }
}

object SQLiteStatement {
  class Impl(val sqlite: SQLite) extends SQLiteStatement

  def apply(sqlite: SQLite): SQLiteStatement = new Impl(sqlite)
}
