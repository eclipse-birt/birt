/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.impl;

import org.eclipse.datatools.enablement.oda.xml.util.MappedTables;

/**
 * This class describe the information of certain ResultSet.
 * @deprecated Please use DTP xml driver
 */
public class ResultSetMetaData
		extends
			org.eclipse.datatools.enablement.oda.xml.impl.ResultSetMetaData
{

	/**
	 * 
	 * @param ri
	 * @param tableName
	 */
	ResultSetMetaData( MappedTables mt, String tableName )
	{
		super( mt, tableName );
	}
}
