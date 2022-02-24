/*
 *************************************************************************
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
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.impl;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implementation class of IParameterMetaData for the MongoDB ODA runtime
 * driver. Input parameters are not supported; always returns 0 input parameter
 * count.
 */
public class ParameterMetaData implements IParameterMetaData {

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterCount()
	 */
	@Override
	public int getParameterCount() throws OdaException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterMode(
	 * int)
	 */
	@Override
	public int getParameterMode(int param) throws OdaException {
		return IParameterMetaData.parameterModeIn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterName(
	 * int)
	 */
	@Override
	public String getParameterName(int param) throws OdaException {
		return null; // name is not available
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterType(
	 * int)
	 */
	@Override
	public int getParameterType(int param) throws OdaException {
		return java.sql.Types.CHAR; // default value
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#
	 * getParameterTypeName(int)
	 */
	@Override
	public String getParameterTypeName(int param) throws OdaException {
		int nativeTypeCode = getParameterType(param);
		return MongoDBDriver.getNativeDataTypeName(nativeTypeCode);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(int param) throws OdaException {
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getScale(int)
	 */
	@Override
	public int getScale(int param) throws OdaException {
		return -1;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#isNullable(int)
	 */
	@Override
	public int isNullable(int param) throws OdaException {
		return IParameterMetaData.parameterNullableUnknown;
	}

}
