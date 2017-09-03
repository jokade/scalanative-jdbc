//     Project: Default (Template) Project
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.sqlite

case class SQLiteException(msg: String, code: ResultCode) extends RuntimeException(
  if(code > 0 ) s"$msg (${ResultCode.messageForCode(code)})" else msg)
