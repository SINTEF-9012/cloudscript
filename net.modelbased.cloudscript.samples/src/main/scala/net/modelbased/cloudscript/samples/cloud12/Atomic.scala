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

