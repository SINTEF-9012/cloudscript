/**
 * This file is part of CloudScript [ http://cloudscript.modelbased.net ]
 *
 * Copyright (C) 2011-  SINTEF ICT
 * Contact: Sebastien Mosser <sebastien.mosser@sintef.no>
 *
 * Module: net.modelbased.cloudscript.kernel
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
package net.modelbased.cloudscript.kernel

 abstract class Connector[From <: Service, To <: Service] {
  var from: From = _
  var to: To = _
}

abstract class StraightConnector[S <: Service] extends Connector[S,S]


object Connector {
  type Key = (Class[_], Class[_])
  type Value = Class[Connector[_,_]]
  
  private var _mappings: Map[Key, Value] = Map[Key, Value]()
  
  def register[C <: Connector[_,_]](implicit manifest: Manifest[C]) {
    val from = manifest.erasure.getField("from").getClass()
    val to = manifest.erasure.getField("to").getClass()
    val value = manifest.erasure.asInstanceOf[Value]
    _mappings += (from, to) -> value
  }
  
  def apply(from: Service, to: Service): 
	  	Option[Connector[_,_]] = {
    _mappings.getOrElse((from.getClass(),to.getClass()), None) match {
      case c: Connector[_,_] => Some(c)
      case None => None
    }
  }
  
}
