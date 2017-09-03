package java.sql.test

import java.sql.{Connection, Date, Timestamp}
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
    'read-{
      val conn = initializedDB(testData)
      val stmt = conn.createStatement()
      val rs = stmt.executeQuery("select * from test")
      val d1 = testData(0)
      rs.next() ==> true
      rs.getInt(0) ==> d1.id
      rs.getString(1) ==> d1.sval
      rs.getDouble(2) ==> d1.dval
      rs.getFloat(3) ==> d1.fval
      rs.getBoolean(4) ==> d1.bval
      rs.getLong(5) ==> d1.lval
      rs.getDate(6) ==> d1.dtval
      rs.getTimestamp(6) ==> d1.tsval
      println(d1.dtval)
      rs.close()
      rs.close()
      rs.next() ==> false
    }
  }
}

object JDBCTestSuite {
  case class TestData(id: Int,
                      sval: String,  // should be VARCHAR(20)
                      dval: Double,
                      fval: Float,
                      bval: Boolean,
                      lval: Long,
                      dtval: Date,
                      tsval: Timestamp)

  val testData = Seq(
    TestData(1,"string",1234.56789,1.23456F,true,123456789,new Date(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()))
  )
}
