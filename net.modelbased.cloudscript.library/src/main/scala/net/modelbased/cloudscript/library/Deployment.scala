/**
 * This file is part of CloudScript [ http://cloudscript.modelbased.net ]
 *
 * Copyright (C) 2011-  SINTEF ICT
 * Contact: Sebastien Mosser <sebastien.mosser@sintef.no>
 *
 * Module: net.modelbased.cloudscript.library
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
package net.modelbased.cloudscript.library
import net.modelbased.cloudscript.dsl._

// SSH deployment mechanisms
trait SshOffering    extends ScalarComponent { val ssh = offers[Ssh]  }
trait SshExpectation extends ScalarComponent { val ssh = expects[Ssh] }

// APT based mechanisms: connect through SSH, and then execute APT commands
trait AptExpectation extends SshExpectation { 
  val packages: List[String]
  def toCommand: String = packages.mkString("sudo apt-get install "," ","")
}

trait FileDownload extends SshOffering {
  val sourceFile: java.net.URL
  val targetPath: net.modelbased.cloudscript.kernel.Property[String]
  val file_deploy_commands: List[String]
  def deployAsBash: String = file_deploy_commands.mkString("#!/bin/bash\n","\n","\n") 
}

trait WarFileBasedDeployment extends SshOffering {
  val deployPath : net.modelbased.cloudscript.kernel.Property[String]
}

trait WarFileComponent extends SshExpectation {
  val file: java.net.URL
  val hostPath = expectsProperty[String]
}

trait StartupCommand extends SshOffering { 
  val startup_commands: List[String]
  def startupAsBash: String = startup_commands.mkString("#!/bin/bash\n","\n","\n") 
}
trait InitCommands extends SshOffering { 
  val init_setup: List[String] 
  def initAsBash: String = init_setup.mkString("#!/bin/bash\n","\n","\n") 
}
trait BashSetup extends SshOffering { 
  val bashrc: List[String] 
  def addToBashRC: String = bashrc.mkString("","\n","\n") 
}
