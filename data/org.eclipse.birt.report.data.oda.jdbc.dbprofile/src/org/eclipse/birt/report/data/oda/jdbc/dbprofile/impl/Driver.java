/*
 *************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Extends the behavior of the oda.jdbc runtime driver to use a database
 * connection profile for its IConnection.
 */
public class Driver extends OdaJdbcDriver implements IDriver {

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#getConnection(java.lang.
	 * String)
	 */
	public IConnection getConnection(String dataSourceType) throws OdaException {
		// this driver supports only one type of data source,
		// ignores the specified dataSourceType
		return new Connection();
	}

}
