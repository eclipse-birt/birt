/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-2.0.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - code cleanup
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IDriver for an ODA runtime driver.
 */
public class Driver implements IDriver {
	static String ODA_DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.excel"; //$NON-NLS-1$

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#getConnection(java.lang.
	 * String)
	 */
	public IConnection getConnection(String dataSourceType) throws OdaException {
		// assumes that this driver supports only one type of data source,
		// ignores the specified dataSourceType
		return new Connection();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#setLogConfiguration(org.
	 * eclipse.datatools.connectivity.oda.LogConfiguration)
	 */
	public void setLogConfiguration(LogConfiguration logConfig) throws OdaException {
		// do nothing; assumes simple driver has no logging
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#getMaxConnections()
	 */
	public int getMaxConnections() throws OdaException {
		return 0; // no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#setAppContext(java.lang.
	 * Object)
	 */
	public void setAppContext(Object context) throws OdaException {
		// do nothing; assumes no support for pass-through context
	}

}
