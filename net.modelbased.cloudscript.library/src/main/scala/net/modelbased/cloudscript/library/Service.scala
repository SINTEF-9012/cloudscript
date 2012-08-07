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

import net.modelbased.cloudscript.kernel._

/*********************
 ** Common Services **
 *********************/

class Apt extends Service
class War extends Service
class Ssh extends Service
class File extends Service


/***********************
 ** Common Connectors **
 ***********************/

//class AptConnector extends StraightConnector[Apt] 
//class WarConnector extends StraightConnector[War]

//class Apt2SshConnector extends Connector[Ssh, Apt]
/*******************************************************
 * Initializer to register Connectors in the registry **
 *******************************************************/
/*
object Initializer {
  Connector.register[Apt2SshConnector]
  Connector.register[AptConnector]
  Connector.register[WarConnector]
}
*/

