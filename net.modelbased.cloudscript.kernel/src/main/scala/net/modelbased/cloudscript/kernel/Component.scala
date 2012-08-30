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

import scala.collection.JavaConversions._
import java.util.UUID

trait Component {
  
  private[this] var _owner: Component = _
  def owner = _owner
  def owner_=(c: Component) { this._owner = c }
 
  private[this] var _expected: Service = _
  def expected = _expected
  def expected_=(s: Service) { this._expected = s }
  
  private[this] var _offereds: List[Service] = List[Service]()
  def offereds: java.util.List[Service] = _offereds.reverse
  def addOffering(srv: Service) { _offereds = srv :: _offereds }
 
  private[this] var _expectedProperties: List[Property[_]] = List[Property[_]]()
  def expectedProperties = _expectedProperties
  def addPropertyExpectation(p: Property[_]) { _expectedProperties = p :: _expectedProperties}
  
  private[this] var _offeredProperties: List[Property[_]] = List[Property[_]]()
  def offeredProperties = _offeredProperties
  def addPropertyOffering(p: Property[_]) { _offeredProperties = p :: _offeredProperties} 
  
  private[this] var _uuid: String = java.util.UUID.randomUUID.toString
  def uuid = _uuid
  def uuid_=(s: String) { _uuid = s }
  
  override def toString(): String = uuid + "#" + this.getClass().getCanonicalName()
}

trait ScalarComponent extends Component {}

trait CompositeComponent extends Component {
  
  private[this] var _containeds: List[Component] = List[Component]()
  def containeds: java.util.List[Component]  = _containeds.reverse
  def addContainment(c: Component) { _containeds = c :: _containeds }
  
  private[this] var _connectors: List[Connector] = List[Connector]()
  def connectors: java.util.List[Connector] = _connectors.reverse
  def addConnector(c: Connector) { _connectors = c :: _connectors }
  
  private[this] var _propertyBinding: List[(Property[_],Property[_])] = List[(Property[_],Property[_])]()
  def propertyBindings: java.util.List[(Property[_],Property[_])]  = _propertyBinding.reverse
  def addPropertyBinding[T](source: Property[T], target: Property[T]) {
    _propertyBinding = (source,target) :: _propertyBinding
  }
}
