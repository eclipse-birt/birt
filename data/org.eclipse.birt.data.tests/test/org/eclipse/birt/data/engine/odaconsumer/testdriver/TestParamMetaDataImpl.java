/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.sql.Types;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Provides parameter metadata to support individual test cases. See test case
 * ids defined in TestAdvQueryImpl class.
 */
public class TestParamMetaDataImpl implements IParameterMetaData {
	public static final String TEST_PARAM_NATIVE_NAME_PREFIX = "myParamNativeName";

	private int m_currentTestCase;

	public TestParamMetaDataImpl(int testCaseId) {
		m_currentTestCase = testCaseId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterCount()
	 */
	public int getParameterCount() throws OdaException {
		switch (m_currentTestCase) {
		case 1:
			return 3;
		case 2:
			return 3;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterMode(
	 * int)
	 */
	public int getParameterMode(int param) throws OdaException {
		if (m_currentTestCase == 1) {
			if (param <= getParameterCount())
				return IParameterMetaData.parameterModeOut;
		} else if (m_currentTestCase == 2) {
			if (param <= getParameterCount())
				return IParameterMetaData.parameterModeIn;
		}

		return IParameterMetaData.parameterModeUnknown;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterName(
	 * int)
	 */
	public String getParameterName(int param) throws OdaException {
		if (m_currentTestCase == 1 || m_currentTestCase == 2) {
			return TEST_PARAM_NATIVE_NAME_PREFIX + param;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterType(
	 * int)
	 */
	public int getParameterType(int param) throws OdaException {
		if (m_currentTestCase == 1 || m_currentTestCase == 2) {
			switch (param) {
			case 1:
				return Types.VARCHAR;
			case 2:
				return Types.DATE;
			case 3:
				return Types.BOOLEAN; // not mapped in plugin.xml
			}
		}

		return Types.NULL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#
	 * getParameterTypeName(int)
	 */
	public String getParameterTypeName(int param) throws OdaException {
		if (m_currentTestCase == 1 || m_currentTestCase == 2) {
			switch (param) {
			case 1:
				return "String";
			case 2:
				return "Date";
			case 3:
				return "Boolean";
			}
		}

		return "Unknown type";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getPrecision(int)
	 */
	public int getPrecision(int param) throws OdaException {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getScale(int)
	 */
	public int getScale(int param) throws OdaException {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#isNullable(int)
	 */
	public int isNullable(int param) throws OdaException {
		return IParameterMetaData.parameterNullableUnknown;
	}

}
