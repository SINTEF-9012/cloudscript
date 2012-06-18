/**
 * This file is part of CloudScript [ http://cloudscript.modelbased.net ]
 *
 * Copyright (C) 2011-  SINTEF ICT
 * Contact: Sebastien Mosser <sebastien.mosser@sintef.no>
 *
 * Module: net.modelbased.cloudscript.samples
 *
 * CloudScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudScript is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudScript. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.modelbased.cloudscript.samples

import net.modelbased.cloudscript.dsl._
import net.modelbased.cloudscript.library._

class Bank extends CompositeComponent {
  // Sub-Components
  private val container = instantiates[WarContainer]
  private val db  = instantiates[MySql]
  private val app = instantiates[BankApp]
  private val vm  = instantiates[SmallVM]
  // Bindings
  this deploys app.war on container.war
  this deploys db.apt on vm.ssh
  // Properties
  this sets app.dbRef using db.url
}

class BankApp extends WarComponent {
  override def warFile = "bankmanager-1.1.war"
  val war = expects[War]
  val dbRef = expectsProperty[String]
}

class MySql extends AptComponent {
  def packages = List("mysql")
  val url      = hasForProperty[String]
  val userName = expectsProperty[String]
  val password = expectsProperty[String]
}

trait WarComponent extends ScalarComponent {
  def warFile: String
}