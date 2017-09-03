package java.sql

import java.security.cert.CertPathValidatorException.Reason

class SQLException(reason: String, sqlState: String, vendorCode: Int, cause: Throwable) extends Exception(reason,cause) {
  def this() = this(null,null,0,null)
  def this(reason: String) = this(reason,null,0,null)
  def this(reason: String, sqlState: String) = this(reason,sqlState,0,null)
  def this(reason: String, sqlState: String, vendorCode: Int) = this(reason,sqlState,vendorCode,null)
  def this(reason: String, sqlState: String, cause: Throwable) = this(reason,sqlState,0,cause)
  def this(reason: String, cause: Throwable) = this(reason,null,0,cause)
  def this(cause: Throwable) = this(null,null,0,cause)
}

class SQLWarning(reason: String, sqlState: String, vendorCode: Int, cause: Throwable)
  extends SQLException(reason,sqlState,vendorCode,cause)

class SQLNonTransientException(reason: String, sqlState: String, vendorCode: Int, cause: Throwable)
  extends SQLException(reason,sqlState,vendorCode,cause)

class SQLFeatureNotSupportedException(reason: String, sqlState: String, vendorCode: Int, cause: Throwable)
  extends SQLNonTransientException(reason,sqlState,vendorCode,cause) {
  def this() = this(null,null,0,null)
}