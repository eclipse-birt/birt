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

	@Override
	public int getColumnCount() throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.getColumnCount();
	}

	@Override
	public int getColumnDisplayLength(int index) throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.getColumnDisplayLength(index);
	}

	@Override
	public String getColumnLabel(int index) throws OdaException {
		if (rsMetaData == null) {
			return "";
		}
		return BidiTransform.transform(rsMetaData.getColumnLabel(index), metadataBidiFormatStr,
				BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	@Override
	public String getColumnName(int index) throws OdaException {
		if (rsMetaData == null) {
			return "";
		}
		return BidiTransform.transform(rsMetaData.getColumnName(index), metadataBidiFormatStr,
				BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	@Override
	public int getColumnType(int index) throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.getColumnType(index);
	}

	@Override
	public String getColumnTypeName(int index) throws OdaException {
		if (rsMetaData == null) {
			return "";
		}
		return rsMetaData.getColumnTypeName(index);
	}

	@Override
	public int getPrecision(int index) throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.getPrecision(index);
	}

	@Override
	public int getScale(int index) throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.getScale(index);
	}

	@Override
	public int isNullable(int index) throws OdaException {
		if (rsMetaData == null) {
			return 0;
		}
		return rsMetaData.isNullable(index);
	}

}
