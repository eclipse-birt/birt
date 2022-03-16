/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 *
 * This class implements the
 * org.eclipse.datatools.connectivity.IParameterMetaData interface.
 *
 */

public class SPParameterMetaData implements IParameterMetaData {

	/** JDBC ParameterMetaData instance */
	private Object[] paramMetadataArray;

	private static Logger logger = Logger.getLogger(SPParameterMetaData.class.getName());

	/**
	 * assertNotNull(Object o)
	 *
	 * @param o the object that need to be tested null or not. if null, throw
	 *          exception
	 */
	private void assertNotNull(Object o) throws OdaException {
		if (o == null) {
			throw new JDBCException(ResourceConstants.DRIVER_NO_PARAMETERMETADATA,
					ResourceConstants.ERROR_NO_PARAMETERMETADATA);

		}
	}

	/**
	 *
	 * Constructor ParameterMetaData(java.sql.ParameterMetaData paraMeta) use JDBC's
	 * ParameterMetaData to construct it.
	 *
	 */
	public SPParameterMetaData(List paraMetadataList) throws OdaException {
		assertNotNull(paraMetadataList);
		this.paramMetadataArray = paraMetadataList.toArray();
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterCount()
	 */
	@Override
	public int getParameterCount() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getParameterCount",
				"SPParameterMetaData.getParameterCount( )");
		assertNotNull(paramMetadataArray);
		return paramMetadataArray.length;
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterMode(int)
	 */
	@Override
	public int getParameterMode(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getParameterMode",
				"SPParameterMetaData.getParameterMode( )");
		assertNotNull(paramMetadataArray);
		int result = IParameterMetaData.parameterModeUnknown;
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		if (paramDefn.getParamInOutType() == java.sql.ParameterMetaData.parameterModeIn) {
			result = IParameterMetaData.parameterModeIn;
		} else if (paramDefn.getParamInOutType() == java.sql.ParameterMetaData.parameterModeOut) {
			result = IParameterMetaData.parameterModeOut;
		} else if (paramDefn.getParamInOutType() == java.sql.ParameterMetaData.parameterModeInOut) {
			result = IParameterMetaData.parameterModeInOut;
		} else if (paramDefn.getParamInOutType() == 5) {
			result = IParameterMetaData.parameterModeOut;
		}
		return result;
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
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getParameterName",
				"SPParameterMetaData.getParameterName( )");
		assertNotNull(paramMetadataArray);
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		return paramDefn.getParamName();
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterType(int)
	 */
	@Override
	public int getParameterType(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getParameterType",
				"SPParameterMetaData.getParameterType( )");
		assertNotNull(paramMetadataArray);
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		return paramDefn.getParamType();
	}

	/*
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterTypeName(
	 * int)
	 */
	@Override
	public String getParameterTypeName(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getParameterTypeName",
				"SPParameterMetaData.getParameterTypeName( )");
		assertNotNull(paramMetadataArray);
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		return paramDefn.getParamTypeName();
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getPrecision",
				"SPParameterMetaData.getPrecision( )");
		assertNotNull(paramMetadataArray);
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		return paramDefn.getPrecision();
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#getScale(int)
	 */
	@Override
	public int getScale(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "getScale",
				"SPParameterMetaData.getScale( )");
		assertNotNull(paramMetadataArray);
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];
		return paramDefn.getScale();
	}

	/*
	 *
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#isNullable(int)
	 */
	@Override
	public int isNullable(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, SPParameterMetaData.class.getName(), "isNullable",
				"SPParameterMetaData.isNullable( )");
		assertNotNull(paramMetadataArray);
		int result = IParameterMetaData.parameterNullableUnknown;
		ParameterDefn paramDefn = (ParameterDefn) paramMetadataArray[param - 1];

		if (paramDefn.getIsNullable() == java.sql.ParameterMetaData.parameterNullable) {
			result = IParameterMetaData.parameterNullable;
		} else if (paramDefn.getIsNullable() == java.sql.ParameterMetaData.parameterNoNulls) {
			result = IParameterMetaData.parameterNoNulls;
		}
		return result;
	}
}
