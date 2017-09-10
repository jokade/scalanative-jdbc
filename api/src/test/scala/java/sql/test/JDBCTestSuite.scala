package java.sql.test

import java.sql.{Connection, Date, ResultSet, Timestamp}
import java.sql.test.JDBCTestSuite.TestData

import utest._
import utest.framework.Tree


trait JDBCTestSuite extends TestSuite {
  import JDBCTestSuite.testData
  /**
   * Returns a connection to an empty DB
   */
  def emptyDB(): Connection

  def initializedDB(testData: Seq[TestData]): Connection

  def withDB(f: Connection=>Any): Any = {
    val conn = initializedDB(testData)
    f(conn)
    conn.close()
  }

  val tests = TestSuite {
    val conn = initializedDB(testData)
    'statement- {
      'select - {
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("select * from test")
        checkTestData(rs,testData)

        rs.next() ==> false
        rs.close()
        stmt.close()
        conn.close()
      }
    }
    'preparedStatement-{
      'insert-{
        val addData = Seq(
          TestData(4,"A longer text with \n and \r\f äöüß",Double.MinValue,Float.MinValue,true,Long.MinValue,new Date(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()))
        )

        val pstmt = conn.prepareStatement("INSERT INTO test VALUES (?,?,?,?,?,?,?,?)")
        for(d <- addData) {
          pstmt.setInt(1,d.id)
          pstmt.setString(2,d.sval)
          pstmt.setDouble(3,d.dval)
          pstmt.setFloat(4,d.fval)
          pstmt.setBoolean(5,d.bval)
          pstmt.setLong(6,d.lval)
          pstmt.setDate(7,d.dtval)
          pstmt.setTimestamp(8,d.tsval)
          pstmt.executeUpdate()
        }
        pstmt.close()

        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM test ORDER BY id")
        checkTestData(rs,testData++addData)
        rs.next ==> false
        rs.close()
        stmt.close()
        conn.close()
      }
    }
  }

  private def checkTestData(rs: ResultSet, data: Seq[TestData]): Unit = {
    var i = 1
    for (d <- data) {
      println(s"check row $i")
      rs.next() ==> true
      rs.getInt(1) ==> d.id
      rs.getString(2) ==> d.sval
      rs.getDouble(3) ==> d.dval
      rs.getFloat(4) ==> d.fval
      rs.getBoolean(5) ==> d.bval
      rs.getLong(6) ==> d.lval
      rs.getDate(7) ==> d.dtval
      rs.wasNull() ==> (d.dtval == null)
      rs.getTimestamp(8) ==> d.tsval
      rs.wasNull() ==> (d.tsval == null)
      i += 1
    }
  }
}

object JDBCTestSuite {
  case class TestData(id: Int,
                      sval: String,
                      dval: Double,
                      fval: Float,
                      bval: Boolean,
                      lval: Long,
                      dtval: Date,
                      tsval: Timestamp)

  val testData = Seq(
    TestData(1,"string",1234.56789,1.23456F,true,123456789,new Date(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis())),
    TestData(2,"äöüß$\n\r\f #",Double.MaxValue,Float.MaxValue,false,Long.MaxValue,new Date(0),new Timestamp(0)),
    TestData(3,null,Double.MinValue,Float.MinValue,false,Long.MinValue,null,null)
  )
}
