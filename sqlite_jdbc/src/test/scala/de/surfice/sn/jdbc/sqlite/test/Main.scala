//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite.test

import de.surfice.sn.jdbc.sqlite.SQLiteDataSource

object Main {
  def main(args: Array[String]): Unit = {
    val db = SQLiteDataSource()
    val conn = db.getConnection()
    val stmt = conn.createStatement()
    stmt.execute("create table test(id INTEGER);")
    stmt.close()
    val stmt2 = conn.createStatement()
    val rs = stmt2.executeQuery("select count(*) from test;")
    rs.next()
    rs.getInt(0)
    rs.close()
    rs.getInt(0)
    println("done")
  }
}
