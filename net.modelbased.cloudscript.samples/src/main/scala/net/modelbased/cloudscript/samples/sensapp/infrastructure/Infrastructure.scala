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
package net.modelbased.cloudscript.samples.sensapp.infrastructure

import net.modelbased.cloudscript.dsl._
import net.modelbased.cloudscript.library._

package amazon {
  
  // An EC2 virtual machine is a component. It has a name, runs a given AMI and is hosted in a given region
  abstract class EC2VirtualMachine extends ScalarComponent {
    val name: String
    val ami: AMI
    val region: Region
    val instType: InstanceType
    val allowed: List[Rule]
  } 
  
  case class Rule(source: String, port: Int)
  object Rules {
    val SSH = Rule("0.0.0.0/0", 22)
    val HTTP_ALT = Rule("0.0.0.0/0", 8080)
  }
  
  // A region is a name and an endpoint (let's keep it simple)
  case class Region(name: String, endpoint: String);
  
  // regions defined by the Amazon EC2 cloud: ec2-describe-regions | awk '{print("Region(\"",$2,"\",\"",$3,"\")")}' | tr -d " "
  object Regions {
    // region nicknames: http://docs.amazonwebservices.com/general/latest/gr/rande.html
    val Ireland = Region("eu-west-1","ec2.eu-west-1.amazonaws.com")
    val SaoPolo = Region("sa-east-1","ec2.sa-east-1.amazonaws.com")
    val Virginia = Region("us-east-1","ec2.us-east-1.amazonaws.com")
    val Tokyo = Region("ap-northeast-1","ec2.ap-northeast-1.amazonaws.com")
    val Oregon = Region("us-west-2","ec2.us-west-2.amazonaws.com")
    val California = Region("us-west-1","ec2.us-west-1.amazonaws.com")
    val Singapore = Region("ap-southeast-1","ec2.ap-southeast-1.amazonaws.com")

    // internal Map to bind a Region with its id
    private val _bindings: Map[String, Region] = Map("eu-west-1" -> Ireland, 
        "sa-east-1"      -> SaoPolo,    "us-east-1" -> Virginia, 
        "ap-northeast-1" -> Tokyo,      "us-west-2" -> Oregon, 
        "us-west-1"      -> California, "ap-southeast-1" -> Singapore)
    
    // Syntactic sugar to support 'Region("id")' syntax for region factory
    def apply(id: String) = _bindings(id) 
  }
  
  // Amazon Machine Image list (obviously not exhaustive, should be dynamically linked to an Amazon Web Service)
  case class AMI(name: String, id: String)
  object AMIs {
    val AmazonLinux = AMI("Amazon Linux AMI 2012.03", "ami-6d555119")
    val UbuntuLTS = AMI("Ubuntu Server 12.04 LTS","ami-ab9491df")
    // to be continued ...
  }
  
  // Instance type provided by Amazon (also not exhaustive)
  case class InstanceType(name: String, id: String)
  object InstanceTypes {
    val Micro = InstanceType("Micro", "t1.micro")
    val Small = InstanceType("Small","m1.small")
    val Medium = InstanceType("Medium","m1.medium")
    val Large = InstanceType("Large","m1.large")
    val ExtraLarge = InstanceType("Extra Large","m1.xlarge")
    
    //internal map to bind id to instances
    private val _bindings: Map[String, InstanceType] = Map("t1.micro" -> Micro,
        "m1.small" -> Small, "m1.medium" -> Medium, "m1.large" -> Large, "m1.xlarge" -> ExtraLarge)
    
    // Syntactic sugar
    def apply(id: String): InstanceType = _bindings(id)
  }    
}

