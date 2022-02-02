/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.impl;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.enablement.oda.xml.util.MappedTables;

/**
 * This class implement IResultSet class
 * 
 * @deprecated Please use DTP xml driver
 */
public class ResultSet extends org.eclipse.datatools.enablement.oda.xml.impl.ResultSet {
	/**
	 * 
	 * @param is
	 * @param ri
	 * @param tableName
	 * @param maxRows
	 * @throws OdaException
	 */
	public ResultSet(org.eclipse.datatools.enablement.oda.xml.impl.Connection conn, MappedTables mt, String tableName,
			int maxRows) throws OdaException {
		super(conn, mt, tableName, maxRows);
	}
}
