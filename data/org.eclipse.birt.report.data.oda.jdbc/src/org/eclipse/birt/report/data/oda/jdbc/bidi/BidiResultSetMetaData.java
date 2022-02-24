/***********************************************************************
 * Copyright (c) 2009 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.bidi;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Bidi implementation of JDBC ResultSetMetaData
 * 
 * @author Ira Fishbein
 *
 */
public class BidiResultSetMetaData implements IResultSetMetaData {

	String metadataBidiFormatStr;
	IResultSetMetaData rsMetaData = null;

	public BidiResultSetMetaData(IResultSetMetaData meta, String contentBidiFormatStr, String metadataBidiFormatStr) {
		this.rsMetaData = meta;
		this.metadataBidiFormatStr = metadataBidiFormatStr;
	}

	public int getColumnCount() throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.getColumnCount();
	}

	public int getColumnDisplayLength(int index) throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.getColumnDisplayLength(index);
	}

	public String getColumnLabel(int index) throws OdaException {
		if (rsMetaData == null)
			return "";
		return BidiTransform.transform(rsMetaData.getColumnLabel(index), metadataBidiFormatStr,
				BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	public String getColumnName(int index) throws OdaException {
		if (rsMetaData == null)
			return "";
		return BidiTransform.transform(rsMetaData.getColumnName(index), metadataBidiFormatStr,
				BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	public int getColumnType(int index) throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.getColumnType(index);
	}

	public String getColumnTypeName(int index) throws OdaException {
		if (rsMetaData == null)
			return "";
		return rsMetaData.getColumnTypeName(index);
	}

	public int getPrecision(int index) throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.getPrecision(index);
	}

	public int getScale(int index) throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.getScale(index);
	}

	public int isNullable(int index) throws OdaException {
		if (rsMetaData == null)
			return 0;
		return rsMetaData.isNullable(index);
	}

}
