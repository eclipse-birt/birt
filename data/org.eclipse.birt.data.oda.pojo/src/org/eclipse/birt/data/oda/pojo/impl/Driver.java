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

import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Implementation class of IDriver for an ODA runtime driver.
 */
public class Driver implements IDriver {
	static String ODA_DATA_SOURCE_ID = "org.eclipse.birt.data.oda.pojo"; //$NON-NLS-1$

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#getConnection(java.lang.
	 * String)
	 */
	@Override
	public IConnection getConnection(String dataSourceType) throws OdaException {
		// assumes that this driver supports only one type of data source,
		// ignores the specified dataSourceType
		return new Connection();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#setLogConfiguration(org.
	 * eclipse.datatools.connectivity.oda.LogConfiguration)
	 */
	@Override
	public void setLogConfiguration(LogConfiguration logConfig) throws OdaException {
		// do nothing; assumes simple driver has no logging
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#getMaxConnections()
	 */
	@Override
	public int getMaxConnections() throws OdaException {
		return 0; // no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#setAppContext(java.lang.
	 * Object)
	 */
	@Override
	public void setAppContext(Object context) throws OdaException {
		// do nothing; assumes no support for pass-through context
	}

	/**
	 * Returns the object that represents this extension's manifest.
	 *
	 * @throws OdaException
	 */
	static ExtensionManifest getManifest() throws OdaException {
		return ManifestExplorer.getInstance().getExtensionManifest(ODA_DATA_SOURCE_ID);
	}

	/**
	 * Returns the native data type name of the specified code, as defined in this
	 * data source extension's manifest.
	 *
	 * @param nativeDataTypeCode the native data type code
	 * @return corresponding native data type name
	 * @throws OdaException if lookup fails
	 */
	static String getNativeDataTypeName(int nativeDataTypeCode) throws OdaException {
		DataTypeMapping typeMapping = getManifest().getDataSetType(null).getDataTypeMapping(nativeDataTypeCode);
		if (typeMapping != null) {
			return typeMapping.getNativeType();
		} else {
			throw new OdaException(Messages.getString("Driver.UnkownDataTypeCode", nativeDataTypeCode)); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the native data type code of the specified name, as defined in this
	 * data source extension's manifest.
	 *
	 * @param nativeDataTypeName the native data type name
	 * @return corresponding native data type code
	 * @throws OdaException if lookup fails
	 */
	public static int getNativeDataTypeCode(String nativeDataTypeName) throws OdaException {
		DataTypeMapping typeMapping = getManifest().getDataSetType(null).getDataTypeMapping(nativeDataTypeName);
		if (typeMapping != null) {
			return typeMapping.getNativeTypeCode();
		} else {
			throw new OdaException(Messages.getString("Driver.UnkownDataTypeName", nativeDataTypeName)); //$NON-NLS-1$
		}
	}

}
