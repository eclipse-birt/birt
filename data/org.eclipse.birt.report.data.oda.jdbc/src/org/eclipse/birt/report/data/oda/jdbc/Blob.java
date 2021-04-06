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
package org.eclipse.birt.report.data.oda.jdbc;

import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/*
 * The Blob object implements interface IBlob
 */

public class Blob implements IBlob {

	/** the JDBC ResultSet object */
	private java.sql.Blob blob;

	/**
	 * 
	 * @param blobObj
	 * @throws OdaException
	 */
	public Blob(java.sql.Blob blobObj) throws OdaException {
		this.blob = blobObj;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IBlob#getBinaryStream()
	 */
	public InputStream getBinaryStream() throws OdaException {
		if (blob == null)
			return null;
		try {
			return blob.getBinaryStream();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IBlob#getBytes(long, int)
	 */
	public byte[] getBytes(long position, int length) throws OdaException {
		if (blob == null)
			return null;
		try {
			return blob.getBytes(position, length);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IBlob#length()
	 */
	public long length() throws OdaException {
		if (blob == null)
			return 0;
		try {
			return blob.length();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_CLOB_VALUE, e);
		}
	}
}
