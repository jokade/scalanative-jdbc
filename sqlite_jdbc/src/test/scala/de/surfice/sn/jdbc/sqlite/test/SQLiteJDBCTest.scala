//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc.sqlite.test

import java.sql.Connection
import java.sql.test.JDBCTestSuite
import java.sql.test.JDBCTestSuite.TestData
import scalanative.native._

import de.surfice.sn.jdbc.sqlite.{SQLiteConnection, SQLiteDataSource}
import de.surfice.sn.sqlite.SQLite

object SQLiteJDBCTest extends JDBCTestSuite {
  val memds = SQLiteDataSource()

  override def emptyDB(): Connection = memds.getConnection()

  override def initializedDB(testData: Seq[TestData]) = {
    val sqlite = SQLite()
    sqlite.execute(
      """CREATE TABLE test (
        |  id INTEGER PRIMARY KEY,
        |  stringval TEXT,
        |  dval REAL,
        |  fval FLOAT,
        |  bval BOOLEAN,
        |  lval LONG,
        |  dtval DATE,
        |  tsval TIMESTAMP
        |);
      """.stripMargin)
    val pstmt = sqlite.prepareStatement("INSERT INTO test(id,stringval,dval,fval,bval,lval,dtval,tsval) VALUES(?,?,?,?,?,?,?,?)")
    testData foreach { d =>
      pstmt.reset()
      pstmt.bind_int(1,d.id)
      pstmt.bind_string(2,d.sval)
      pstmt.bind_double(3,d.dval)
      pstmt.bind_double(4,d.fval)
      pstmt.bind_int(5,if(d.bval) 1 else 0)
      pstmt.bind_int64(6,d.lval)
      if(d.dtval == null)
        pstmt.bind_null(7)
      else
        pstmt.bind_int64(7,d.dtval.getTime)
      if(d.tsval == null)
        pstmt.bind_null(8)
      else
        pstmt.bind_int64(8,d.tsval.getTime)
      pstmt.execute()
    }
    pstmt.close()
    new SQLiteConnection(sqlite)
  }


}
