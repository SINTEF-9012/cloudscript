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
package net.modelbased.cloudscript.samples.sensapp.platform

import scala.collection.JavaConversions._
import net.modelbased.cloudscript.dsl._
import net.modelbased.cloudscript.library._
import net.modelbased.cloudscript.samples.sensapp.infrastructure._


class MonolithicHost extends CompositeComponent {
  // Internal components
  private[this] val vm = instantiates[SmallVM]
  private[this] val db = instantiates[MongoDataBase]
  private[this] val jvm = instantiates[JavaVM]
  private[this] val server = instantiates[JettyServer]
  // Properties
  val deploymentPath = externalize(server.targetPath)
  // Deployment binding
  this deploys db.ssh on vm.ssh
  this deploys jvm.ssh on vm.ssh
  this deploys server.ssh on vm.ssh
  // port promotion
  val ssh = promotes(vm.ssh)
}

// Virtual machines used to host a SensApp instance 
class MicroVM extends VirtualMachine { val name = "sensapp_host_micro"; val instType = amazon.InstanceTypes.Micro }
class SmallVM extends VirtualMachine { val name = "sensapp_host_small"; val instType = amazon.InstanceTypes.Small }

// MongoDB database (persistence support) & JVM 
class MongoDataBase extends AptExpectation { val packages = List("mongodb") }
class JavaVM extends AptExpectation { val packages = List("openjdk-7-jre") }

// Jetty  server (execution environment)
class JettyServer extends FileDownload with StartupCommand {
  val sourceFile = new java.net.URL("http://download.eclipse.org/jetty/stable-8/dist/jetty-distribution-8.1.5.v20120716.tar.gz")
  val targetPath = hasForProperty[String]
  targetPath.data = "/opt/jetty8/webapps" 
    
  val file_deploy_commands = List("tar zxvf /tmp/jetty-distribution-8.1.5.v20120716.tar.gz", 
      "sudo mv /tmp/jetty-distribution-8.1.5.v20120716 /opt/jetty-distribution-8.1.5.v20120716", 
      "sudo ln -s /opt/jetty-distribution-8.1.5.v20120716 /opt/jetty8")
  val startup_commands = List("cd /opt/jetty8/bin", "./jetty.sh start")
}


// An EC2 virtual machine, located in Dublin and running an ubuntu 12.04 LTS OS
protected abstract class VirtualMachine extends amazon.EC2VirtualMachine with SshOffering with InitCommands with BashSetup {
  val ami = amazon.AMIs.UbuntuLTS
  val region = amazon.Regions.Ireland
  val bashrc = List("export LANGUAGE=en_US.UTF-8", "export LANG=en_US.UTF-8", "export LC_ALL=en_US.UTF-8")
  val init_setup = List("sudo locale-gen en_US.UTF-8")
  val allowed = List(amazon.Rules.SSH, amazon.Rules.HTTP_ALT)
}