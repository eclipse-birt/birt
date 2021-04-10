/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.util;

import java.io.Reader;
import java.sql.SQLException;

import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * The Blob object implements interface IBlob Just copied from JDBC ODA Driver
 */

public class Clob implements IClob {
	private java.sql.Clob clob;

	public Clob(java.sql.Clob clobObj) throws OdaException {
		this.clob = clobObj;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IClob#getCharacterStream()
	 */
	public Reader getCharacterStream() throws OdaException {
		if (clob == null)
			return null;
		try {
			return clob.getCharacterStream();
		} catch (SQLException e) {
			throw new OdaException(e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IClob#getSubString()
	 */
	public String getSubString(long position, int length) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IClob#length()
	 */
	public long length() throws OdaException {
		if (clob == null)
			return 0;
		try {
			return clob.length();
		} catch (SQLException e) {
			throw new OdaException(e);
		}
	}
}
