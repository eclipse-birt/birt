/**
 *************************************************************************
 * Copyright (c) 2008, 2009 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.Statement;
import org.eclipse.birt.report.data.oda.jdbc.dbprofile.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Extends the behavior of the oda.jdbc runtime driver's Statement to handle
 * data set query's properties.
 */
public class DBProfileStatement extends Statement {
	// Internal constants for data set query's private properties
	public static final String CONST_PARAMS_DELIMITER = ";"; //$NON-NLS-1$
	public static final String CONST_PARAM_NAME_DELIMITER = ","; //$NON-NLS-1$
	public static final String PROP_PRIVATE_PARAMETERMETADATA = "parameterMetaData"; //$NON-NLS-1$

	private Map<Integer, String> m_paramNameMap;

	public DBProfileStatement(java.sql.Connection connection) throws OdaException {
		super(connection);
	}

	/*
	 * @see
	 * org.eclipse.birt.report.data.oda.jdbc.Statement#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	public void setProperty(String name, String value) throws OdaException {
		if (name == null)
			throw new NullPointerException(
					Messages.bind(Messages.dBProfileStatement_nullPropertyNameArg, "setProperty( String, String )")); //$NON-NLS-1$

		if (name.equals(PROP_PRIVATE_PARAMETERMETADATA)) {
			if (value != null && value.length() > 0) {
				m_paramNameMap = new HashMap<Integer, String>();

				String conditionParams = value;

				String params[] = conditionParams.split(CONST_PARAMS_DELIMITER);
				for (int i = 0; i < params.length; i++) {
					String[] posAndName = params[i].split(CONST_PARAM_NAME_DELIMITER);
					if (posAndName.length == 2)
						m_paramNameMap.put(Integer.valueOf(posAndName[0]), posAndName[1]);
				}
			}
		} else
			super.setProperty(name, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException {
		IParameterMetaData metaData = super.getParameterMetaData();
		return new ParameterMetaData(metaData, getParameterNameMap());
	}

	protected Map<Integer, String> getParameterNameMap() {
		return m_paramNameMap;
	}

}
