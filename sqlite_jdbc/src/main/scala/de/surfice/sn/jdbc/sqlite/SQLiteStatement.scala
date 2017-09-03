//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite

import java.sql.{ResultSet, Statement}

import de.surfice.sn.sqlite.SQLite

class SQLiteStatement(sqlite: SQLite) extends Statement.AbstractStatement {
  override def execute(sql: String): Boolean = handleException{
    sqlite.execute(sql)
    false
  }

  override def executeQuery(sql: String): ResultSet = handleException{
    val stmt = new Stmt( sqlite.prepareStatement(sql) )
    ResultSet.wrap(stmt)
  }
}
