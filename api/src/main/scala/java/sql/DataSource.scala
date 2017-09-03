// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package java.sql

import java.io.PrintWriter
import java.util.logging.Logger
import javax.sql.CommonDataSource

trait DataSource extends CommonDataSource {
  def getConnection(): Connection
  def getConnection(username: String, password: String)
}

object DataSource {

  abstract class AbstractDataSource extends DataSource {
    //    override def unwrap[T](iface: Class[T]): T = ???
    //    override def isWrapperFor(iface: Class[_]): Boolean = ???
    override def setLoginTimeout(seconds: Int): Unit = {}

    override def getLoginTimeout: Int = 0

    override def setLogWriter(out: PrintWriter): Unit = {}

    override def getLogWriter: PrintWriter = null

    override def getParentLogger: Logger = throw new SQLFeatureNotSupportedException()
  }

}
