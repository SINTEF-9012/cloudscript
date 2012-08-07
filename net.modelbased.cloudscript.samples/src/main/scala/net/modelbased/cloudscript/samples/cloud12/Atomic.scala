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
package net.modelbased.cloudscript.samples.cloud12

import net.modelbased.cloudscript.dsl._
import net.modelbased.cloudscript.library._

trait AptComponent extends ScalarComponent {
  def packages: List[String] 
  val apt = expects[Apt]
}

trait WarComponent extends ScalarComponent {
  def warFile: String 
  val war = expects[War]
}

class WarContainer extends CompositeComponent with WarOffering {
  private[this] val container = instantiates[Jetty]
  private[this] val vm = instantiates[SmallVM]
  override val war = promotes(container.war) 
  this deploys container.apt on vm.ssh
}

class SmallVM extends ScalarComponent {
  val ssh = offers[Ssh]
}

trait WarOffering extends ServiceOffering {
    val war = offers[War]
}

class Jetty extends AptComponent with WarOffering {
  def packages = List("jetty", "jetty-extra")
}

class MySQL extends AptComponent {
  def packages = List("mysql-server")
  val url = hasForProperty[String]
}

class BankApp extends WarComponent {
  def warFile = "bank-manager.war"
  val dbRef = expectsProperty[String]
}

