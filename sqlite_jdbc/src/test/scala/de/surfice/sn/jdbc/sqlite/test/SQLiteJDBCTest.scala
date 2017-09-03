//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite.test

import java.sql.Connection
import java.sql.test.JDBCTestSuite
import java.sql.test.JDBCTestSuite.TestData

import de.surfice.sn.jdbc.sqlite.SQLiteDataSource

object SQLiteJDBCTest extends JDBCTestSuite {
  val memds = SQLiteDataSource()

  override def emptyDB(): Connection = memds.getConnection()

  override def initializedDB(testData: Seq[TestData]) = {
    val conn = emptyDB()
    val stmt = conn.createStatement()
    stmt.execute(dbsql(testData))
    stmt.close()
    conn
  }

  def dbsql(testData: Seq[TestData]): String = {
    val inserts = testData.map{ d =>
      import d._
      s"""INSERT INTO test VALUES($id,'$sval',$dval,$fval,${if(bval) "1" else "0"},$lval,${dtval.getTime},${tsval.getTime});"""
    } mkString "\n"
    s"""CREATE TABLE test(
      |  id INTEGER PRIMARY KEY,
      |  stringval VARCHAR(20),
      |  dval DOUBLE PRECISION,
      |  fval FLOAT,
      |  bval BOOLEAN,
      |  lval LONG,
      |  dtval DATE,
      |  tsval TIMESTAMP
      |);
      |
      |$inserts
    """.stripMargin
  }
}
