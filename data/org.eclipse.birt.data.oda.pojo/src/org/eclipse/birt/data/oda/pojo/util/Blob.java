/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.util;

import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.OdaException;

/*
 * The Blob object implements interface IBlob.
 * Just copied from JDBC ODA Driver
 */

public class Blob implements IBlob {
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
	@Override
	public InputStream getBinaryStream() throws OdaException {
		if (blob == null) {
			return null;
		}
		try {
			return blob.getBinaryStream();
		} catch (SQLException e) {
			throw new OdaException(e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IBlob#getBytes(long, int)
	 */
	@Override
	public byte[] getBytes(long position, int length) throws OdaException {
		if (blob == null) {
			return null;
		}
		try {
			return blob.getBytes(position, length);
		} catch (SQLException e) {
			throw new OdaException(e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IBlob#length()
	 */
	@Override
	public long length() throws OdaException {
		if (blob == null) {
			return 0;
		}
		try {
			return blob.length();
		} catch (SQLException e) {
			throw new OdaException(e);
		}
	}
}
