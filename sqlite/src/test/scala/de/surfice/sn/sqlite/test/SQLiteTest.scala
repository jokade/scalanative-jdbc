//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.sqlite.test

import de.surfice.sn.sqlite.{ResultCode, SQLite}
import utest._

import scala.scalanative.native._

object SQLiteTest extends TestSuite {
  lazy val testdb = SQLite.readonly("./sqlite/src/test/resources/test.sqlite")
  def withMemDB(f: SQLite=>Any): Any = {
    val db = SQLite()
    f(db)
    db.close()
  }
  def withDB(f: SQLite=>Any): Any = withMemDB{ db =>
    db.execute(dbsql)
    f(db)
  }

  val tests = TestSuite {
    'testdb-{
      val stmt = testdb.prepareStatement("select id,string,double from test")
      stmt.execute() ==> ResultCode.ROW
      stmt.column_int(0) ==> 1
      stmt.column_string(1) ==> "string"
      stmt.column_double(2) ==> 1234.56789

      stmt.step() ==> ResultCode.ROW
      stmt.column_int(0) ==> 2
      stmt.column_string(1) ==> "other string"
      stmt.column_double(2) ==> -1D

      stmt.step() ==> ResultCode.DONE
      stmt.close() ==> ResultCode.OK
    }
    'memdb-{
      withDB{ db =>
        val stmt = db.prepareStatement("select id,string,double from test")

        stmt.execute() ==> ResultCode.ROW
        stmt.column_int(0) ==> 1
        stmt.column_string(1) ==> "string"
        stmt.column_double(2) ==> 1234.56789

        stmt.execute() ==> ResultCode.DONE
        stmt.close() ==> ResultCode.OK

        val stmt2 = db.prepareStatement("insert into test(string,double) values(?,?)")
        stmt2.bind_string(1,"äöüß")
        stmt2.bind_double(2,-9876.54321)
        stmt2.execute() ==> ResultCode.DONE
        stmt2.close() ==> ResultCode.OK

        val stmt3 = db.prepareStatement("select * from test")
        stmt3.execute() ==> ResultCode.ROW
        stmt3.execute() ==> ResultCode.ROW
        stmt3.column_int(0) ==> 2
        stmt3.column_string(1) ==> "äöüß"
        stmt3.column_double(2) ==> -9876.54321
        stmt3.close() ==> ResultCode.OK
      }
    }
  }

  val dbsql =
    """create table test (
      |  id INTEGER primary key autoincrement,
      |  string TEXT,
      |  double REAL
      |);
      |
      |insert into test(string,double) values('string',1234.56789);
    """.stripMargin
}

