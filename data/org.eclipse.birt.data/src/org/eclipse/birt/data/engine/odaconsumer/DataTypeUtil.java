/*
 *************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Internal utility class for handling data types in the ODI layer of the Data
 * Engine.
 */
public final class DataTypeUtil {
	// trace logging variables
	private static String sm_className = DataTypeUtil.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	private DataTypeUtil() {
		// not meant to be instantiated
	}

	/**
	 * Converts an ODA data type to its corresponding Java class, as defined by the
	 * {@link DataTypeUtil} utility.
	 * 
	 * @param odaDataType an ODA data type code
	 * @return the Java class that corresponds to the specified ODA data type
	 * @throws IllegalArgumentException if the specified ODA data type is not a
	 *                                  supported type
	 */
	public static Class toTypeClass(int odaDataType) {
		final String methodName = "toTypeClass"; //$NON-NLS-1$

		Class fieldClass = null;
		try {
			fieldClass = org.eclipse.birt.core.data.DataTypeUtil.toOdiTypeClass(odaDataType);
		} catch (BirtException e) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.UNRECOGNIZED_ODA_TYPE, new Object[] { Integer.valueOf(odaDataType) });
			sm_logger.logp(Level.SEVERE, sm_className, methodName, "Invalid ODA data type: {0}", //$NON-NLS-1$
					Integer.valueOf(odaDataType));
			throw new IllegalArgumentException(localizedMessage);
		}

		// for now, preserve existing behavior of mapping to an
		// ODA IBlob and IClob classes
		if (odaDataType == Types.BLOB)
			fieldClass = IBlob.class;
		else if (odaDataType == Types.CLOB)
			fieldClass = IClob.class;

		if (sm_logger.isLoggable(Level.FINEST))
			sm_logger.logp(Level.FINEST, sm_className, methodName,
					"Converted from ODA data type {0} to Java data type class {1}.",
					new Object[] { Integer.valueOf(odaDataType), fieldClass });

		return fieldClass;
	}

	/**
	 * Converts a Java class to its corresponding ODA data type, as defined by the
	 * {@link org.eclipse.birt.core.data.DataTypeUtil} utility.
	 * 
	 * @param javaClass the Java class.
	 * @return the ODA data type that maps to the Java class.
	 */
	public static int toOdaType(Class javaClass) {
		final String methodName = "toOdaType"; //$NON-NLS-1$

		// returns Types.CHAR if the hint didn't have data type information
		int odaType = Types.CHAR; // default

		// for backward compatibility, preserve existing
		// behavior of mapping from an
		// ODA IBlob and IClob classes
		if (javaClass == IBlob.class)
			odaType = Types.BLOB;
		else if (javaClass == IClob.class)
			odaType = Types.CLOB;
		else
			odaType = org.eclipse.birt.core.data.DataTypeUtil.toOdaDataType(javaClass);

		if (sm_logger.isLoggable(Level.FINEST))
			sm_logger.logp(Level.FINEST, sm_className, methodName,
					"Converted from Java data type class {0} to ODA data type {1}.", //$NON-NLS-1$
					new Object[] { javaClass, Integer.valueOf(odaType) });

		return odaType;
	}

	/**
	 * Converts the specified native data type code to its default ODA data type
	 * code, based on the data type mapping defined by the specified ODA data source
	 * and data set types.
	 * 
	 * @param nativeTypeCode  native type code specific to the ODA data source
	 * @param odaDataSourceId the ODA data source element id
	 * @param dataSetType     the type of data set
	 * @return the converted ODA data type code, or java.sql.Types.NULL if no valid
	 *         mapping is found
	 */
	public static int toOdaType(int nativeTypeCode, String odaDataSourceId, String dataSetType) {
		if (odaDataSourceId == null || odaDataSourceId.length() == 0)
			return Types.NULL;

		return ManifestExplorer.getInstance().getDefaultOdaDataTypeCode(nativeTypeCode, odaDataSourceId, dataSetType);
	}

}
