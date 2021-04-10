/**
 *************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.util.Map;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Wrapper of the oda.jdbc runtime driver's IParameterMetaData to extend its
 * metadata to include parameter name.
 */
public class ParameterMetaData implements IParameterMetaData {
	private IParameterMetaData metaData;
	private Map<Integer, String> nameMap;

	ParameterMetaData(IParameterMetaData metaData, Map<Integer, String> nameMap) {
		this.metaData = metaData;
		this.nameMap = nameMap;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterCount()
	 */
	public int getParameterCount() throws OdaException {
		return metaData.getParameterCount();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterMode(
	 * int)
	 */
	public int getParameterMode(int param) throws OdaException {
		return metaData.getParameterMode(param);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterName(
	 * int)
	 */
	public String getParameterName(int param) throws OdaException {
		if (this.nameMap != null && this.nameMap.containsKey(Integer.valueOf(param))) {
			return this.nameMap.get(Integer.valueOf(param));
		}
		return metaData.getParameterName(param);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterType(
	 * int)
	 */
	public int getParameterType(int param) throws OdaException {
		return metaData.getParameterType(param);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#
	 * getParameterTypeName(int)
	 */
	public String getParameterTypeName(int param) throws OdaException {
		return metaData.getParameterTypeName(param);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getPrecision(int)
	 */
	public int getPrecision(int param) throws OdaException {
		return metaData.getPrecision(param);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IParameterMetaData#getScale(int)
	 */
	public int getScale(int param) throws OdaException {
		return metaData.getScale(param);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#isNullable(int)
	 */
	public int isNullable(int param) throws OdaException {
		return metaData.isNullable(param);
	}

}
