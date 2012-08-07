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
package net.modelbased.cloudscript.samples.sensapp

import net.modelbased.cloudscript.dsl._
import net.modelbased.cloudscript.library._

import net.modelbased.cloudscript.samples.sensapp.platform.MonolithicHost

class SensAppSystem extends CompositeComponent {
  // internal components
  val host = instantiates[MonolithicHost]
  val system = instantiates[SensApp]
  // Deployment binding
  this deploys system.ssh on host.ssh
  // Property binding
  this sets system.hostPath using host.deploymentPath
}


class SensApp extends WarFileComponent {
  val file = new java.net.URL("http://github.com/downloads/SINTEF-9012/sensapp/sensapp.war")
}


trait WarFileComponent extends SshExpectation {
  val file: java.net.URL
  val hostPath = expectsProperty[String]
}