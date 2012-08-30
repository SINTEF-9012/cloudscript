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
import scala.collection.JavaConversions._

import net.modelbased.cloudscript.samples.sensapp.platform.MonolithicHost

class SensAppSystem extends CompositeComponent {
  // internal components
  val host = instantiates[MonolithicHost]; host hasForUUID "sensapp-host"
  val system = instantiates[SensApp]; system hasForUUID "sensapp-system"
  // Deployment binding
  this deploys system.ssh on host.ssh
  // Property binding
  this sets system.hostPath using host.deploymentPath
}

class SensApp extends WarFileComponent {
  val file = new java.net.URL("http://github.com/downloads/SINTEF-9012/sensapp/sensapp.war")
}

object Main extends App {
  import net.modelbased.cloudscript.kernel._
  val software = new SensAppSystem
  descrComponent(software)
  //software.containeds foreach { c => descrComponent(c) }
  
  def descrComponent(c: Component) {
    println("Component: " + c)
    c.offereds foreach { s => println("  offers " + s ) }
    
    println("  expects " + (if (c.expected == null) "nothing" else c.expected))
    c match {
      case c: CompositeComponent => {
        println("  contains" + c.containeds.map { _.toString }.mkString("[",", ","]"))
        c.connectors foreach { c => 
          println("  connects: ")
          println("    from: " + c.from.offeredBy + "[" + c.from + "]")
          println("    to:   " + c.to.offeredBy + "[" + c.to + "]")
        }
        c.containeds foreach { descrComponent(_) }
      }
      case _ => 
    }
  } 
  
  
}