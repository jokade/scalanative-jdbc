//     Project: scalanative-jdbc
//      Module:
// Description:

// Copyright (c) 2017. Distributed under the MIT License (see included LICENSE file).
package de.surfice.sn.jdbc

import java.sql.SQLException

import de.surfice.sn.sqlite.SQLiteException

package object sqlite {

  def handleException[T](body: =>T): T = try{
    body
  } catch {
    case SQLiteException(msg,code) => throw new SQLException(msg,null,code,null)
    case x: Throwable => throw x
  }
}
