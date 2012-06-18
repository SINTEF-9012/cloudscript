/**
 * This file is part of CloudScript [ http://cloudscript.modelbased.net ]
 *
 * Copyright (C) 2011-  SINTEF ICT
 * Contact: Sebastien Mosser <sebastien.mosser@sintef.no>
 *
 * Module: net.modelbased.cloudscript.dsl
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
package net.modelbased.cloudscript.dsl
import scala.reflect.Manifest
import net.modelbased.cloudscript.kernel.{Component => KComponent}
import net.modelbased.cloudscript.kernel.{ScalarComponent => KScalar}
import net.modelbased.cloudscript.kernel.{CompositeComponent => KComposite}
import net.modelbased.cloudscript.kernel.{Service => KService}
import net.modelbased.cloudscript.kernel.{Connector => KConnector}
import net.modelbased.cloudscript.kernel.{Property => KProperty}

abstract trait ScalarComponent extends KScalar 
	with ServiceOffering with ServiceExpectation 
	with PropertyOffering with PropertyExpectation
	
abstract trait CompositeComponent extends KComposite
	with ServiceExpectation with ComponentContainment with ServicePromotion
	with ServiceConnection with PropertyOffering with PropertyExpectation
	with PropertyConnection with PropertyPromotion

	
protected trait ServiceOffering extends KScalar {
    def offers[S <: KService](implicit m: Manifest[S]): S = {
    val service = m.erasure.newInstance.asInstanceOf[S]
    addOffering(service) 
    service
  }
}

protected trait ServiceExpectation extends KComponent {
  def expects[S <: KService](implicit m: Manifest[S]): S = {
    val service = m.erasure.newInstance.asInstanceOf[S]
    this.expected = service
    service
  }
}
	
protected trait ComponentContainment extends KComposite {
  def instantiates[C <: KComponent](implicit m: Manifest[C]): C = {
    val component = m.erasure.newInstance.asInstanceOf[C]
    addContainment(component)
    component.owner = this
    component
  }
}

protected trait ServicePromotion extends KComposite {
  def promotes[T <: KService](service: T): T = {
    addOffering(service)
    service
  }
}

protected trait ServiceConnection extends KComposite {
  def deploys(source: KService) = {
    new ConnectionSource(this, source)
  }
  sealed class ConnectionSource(val owner: KComposite, source: KService) {
    def on(target: KService) = {
      val opt: Option[KConnector[_,_]] = KConnector(source, target)
      opt match {
        case None => throw new RuntimeException("No available Connector!")
        case c: KConnector[_,_] => {
          owner.addConnector(c)
        }
      }
    }
  }
}

protected trait PropertyOffering extends KComponent {
  def hasForProperty[T](implicit m: Manifest[T]): KProperty[T] = {
    val p = new KProperty[T](m.erasure.newInstance.asInstanceOf[T])
    addPropertyOffering(p)
    p
  }
}

protected trait PropertyExpectation extends KComponent {
  def expectsProperty[T](implicit m: Manifest[T]): KProperty[T] = {
    val p = new KProperty[T](m.erasure.newInstance.asInstanceOf[T])
    addPropertyExpectation(p)
    p
  }
}

protected trait PropertyConnection extends KComposite {
  def sets[T](target: KProperty[T]) = {
    new ConnectionSource[T](this, target)
  }
  sealed class ConnectionSource[T](val owner: KComposite, target: KProperty[T]) {
    def using(source: KProperty[T]) = {
      owner.addPropertyBinding(source, target)
    }
  }
}

protected trait PropertyPromotion extends KComponent {
  def externalize[P <: KProperty[_]](property: P): P = {
    addPropertyOffering(property)
    property
  }
}


