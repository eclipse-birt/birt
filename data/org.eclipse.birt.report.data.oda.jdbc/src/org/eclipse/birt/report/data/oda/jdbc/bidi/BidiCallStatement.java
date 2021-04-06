/***********************************************************************
 * Copyright (c) 2009 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.bidi;

import java.sql.Connection;
import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.oda.jdbc.CallStatement;
import org.eclipse.birt.report.data.oda.jdbc.utils.BidiSQLTransform;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Bidi implementation of JDBC Statement
 * 
 * @author Ira Fishbein
 *
 */
public class BidiCallStatement extends CallStatement {

	String contentBidiFormatStr;
	String metadataBidiFormatStr;

	public BidiCallStatement(Connection connection, Properties connProperties) throws OdaException {
		super(connection);
		contentBidiFormatStr = connProperties.getProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME);
		metadataBidiFormatStr = connProperties.getProperty(BidiConstants.METADATA_FORMAT_PROP_NAME);
	}

	protected IResultSetMetaData createNewResultSetMetaData(java.sql.ResultSetMetaData resultmd) throws OdaException {
		IResultSetMetaData result = new BidiResultSetMetaData((IResultSetMetaData) resultmd, contentBidiFormatStr,
				metadataBidiFormatStr);
		return result;
	}

	public void prepare(String command) throws OdaException {
		try {
			String newCommand = BidiSQLTransform.transform(command, BidiConstants.DEFAULT_BIDI_FORMAT_STR,
					contentBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR, metadataBidiFormatStr);
			super.prepare(newCommand);
		} catch (Throwable th) {
			super.prepare(command);
		}
	}

	public IResultSetMetaData getMetaData() throws OdaException {
		IResultSetMetaData meta = super.getMetaData();
		return new BidiResultSetMetaData(meta, contentBidiFormatStr, metadataBidiFormatStr);
	}
}
