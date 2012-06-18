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

/** Creating a composite **/
class WarContainer extends CompositeComponent {
  // Sub-components
  private[this] val container = instantiates[Jetty]
  private[this] val vm = instantiates[SmallVM]
  // Promoted Ports
  val war = promotes(container.war) 
  // Internal Connectors
  this deploys container.apt on vm.ssh
}

/** Declaring a simple component **/ 
class SmallVM extends ScalarComponent {
  val ssh = offers[Ssh]
}

/** Specializing the meta-model with user-given concepts **/
trait AptComponent extends ScalarComponent {
  def packages: List[String] 
  val apt = expects[Apt]
}

/**  Using a home-made concepts, like any other one **/
class Jetty extends AptComponent {
  def packages = List("jetty", "jetty-extra")
  val war = offers[War]
}