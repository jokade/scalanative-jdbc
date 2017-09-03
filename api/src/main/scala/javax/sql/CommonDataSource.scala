package javax.sql

import java.io.PrintWriter
import java.util.logging.Logger

trait CommonDataSource {
  def getLoginTimeout(): Int
  def getLogWriter(): PrintWriter
  def getParentLogger(): Logger
  def setLoginTimeout(seconds: Int): Unit
  def setLogWriter(out: PrintWriter): Unit
}
