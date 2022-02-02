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

package org.eclipse.birt.data.oda.pojo.impl;

import java.util.Properties;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.impl.internal.ClassMethodFieldBuffer;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 */
public class Connection implements IConnection {
	private boolean isOpen = false;

	private String pojoDataSetClassPath = null;

	private ClassMethodFieldBuffer classMethodFieldBuffer = null;

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open(Properties connProperties) throws OdaException {
		if (isOpen) {
			return;
		}

		pojoDataSetClassPath = connProperties == null ? null
				: connProperties.getProperty(Constants.POJO_DATA_SET_CLASS_PATH);
		isOpen = true;
		classMethodFieldBuffer = new ClassMethodFieldBuffer();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.
	 * Object)
	 */
	public void setAppContext(Object context) throws OdaException {
		// nothing to do
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException {
		// TODO replace with data source specific implementation
		isOpen = false;
		classMethodFieldBuffer.release();
		classMethodFieldBuffer = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException {
		return isOpen;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.
	 * String)
	 */
	public IDataSetMetaData getMetaData(String dataSetType) throws OdaException {
		// this driver supports only one type of data set,
		// ignores the specified dataSetType
		return new DataSetMetaData(this);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery(String dataSetType) throws OdaException {
		// this driver supports only one type of data set,
		// ignores the specified dataSetType
		Query query = new Query(pojoDataSetClassPath);
		query.setConnection(this);
		return query;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException {
		return 0; // no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu.util
	 * .ULocale)
	 */
	public void setLocale(ULocale locale) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public ClassMethodFieldBuffer getClassMethodFieldBuffer() {
		return classMethodFieldBuffer;
	}

}
