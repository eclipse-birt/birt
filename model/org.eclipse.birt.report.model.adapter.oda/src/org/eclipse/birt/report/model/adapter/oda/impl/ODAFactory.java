/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.adapter.oda.IODAFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

class ODAFactory implements IODAFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.api.IODAFactory#getUpdatedDataType
	 * (java.lang.String, java.lang.String, int, java.lang.String, java.lang.String)
	 */

	public String getUpdatedDataType(String dataSourceId, String dataSetId, int nativeCode, String romDataType,
			String choiceName) throws BirtException {
		DataSetType setType = null;

		try {
			ExtensionManifest manifest = ManifestExplorer.getInstance().getExtensionManifest(dataSourceId);

			if (manifest == null)
				return null;

			setType = manifest.getDataSetType(dataSetId);
		} catch (OdaException e) {
			// if there is exception, do nothing.
		}

		if (setType == null)
			return null;

		DataTypeMapping typeMapping = setType.getDataTypeMapping(nativeCode);
		if (typeMapping == null)
			return null;

		int odaDataTypeCode = typeMapping.getOdaScalarDataTypeCode();
		if (isCompatible(choiceName, romDataType, odaDataTypeCode))
			return romDataType;

		int[] odaDataTypeCodes = typeMapping.getAlternativeOdaDataTypeCodes();
		for (int i = 0; i < odaDataTypeCodes.length; i++) {
			if (isCompatible(choiceName, romDataType, odaDataTypeCodes[i]))
				return romDataType;
		}

		int apiDataType = DataTypeUtil.toApiDataType(odaDataTypeCode);
		return convertApiTypeToROMType(choiceName, apiDataType);
	}

	private static String convertApiTypeToROMColumnType(int apiDataType) {
		switch (apiDataType) {
		case DataType.INTEGER_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
		case DataType.STRING_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		case DataType.DATE_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
		case DataType.SQL_DATE_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE;
		case DataType.SQL_TIME_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME;
		case DataType.DECIMAL_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
		case DataType.DOUBLE_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
		case DataType.BOOLEAN_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN;
		case DataType.BLOB_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB;
		case DataType.JAVA_OBJECT_TYPE:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT;
		case DataType.BINARY_TYPE:
		case DataType.ANY_TYPE:
		default:
			return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
		}
	}

	private static String convertApiTypeToROMParameterType(int apiDataType) {
		switch (apiDataType) {
		case DataType.INTEGER_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_INTEGER;
		case DataType.STRING_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_STRING;
		case DataType.DATE_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_DATETIME;
		case DataType.SQL_DATE_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_DATE;
		case DataType.SQL_TIME_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_TIME;
		case DataType.DECIMAL_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_DECIMAL;
		case DataType.DOUBLE_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_FLOAT;
		case DataType.BOOLEAN_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_BOOLEAN;
		case DataType.JAVA_OBJECT_TYPE:
			return DesignChoiceConstants.PARAM_TYPE_JAVA_OBJECT;
		case DataType.ANY_TYPE:
		case DataType.BLOB_TYPE:
		case DataType.BINARY_TYPE:
		default:
			return DesignChoiceConstants.PARAM_TYPE_STRING;
		}
	}

	/**
	 * Converts DTE api data type to ROM defined data type.
	 * 
	 * @param choiceName  the ROM choice to map
	 * @param apiDataType the DTE api data type
	 * @return the ROM defined data type.
	 */

	private static String convertApiTypeToROMType(String choiceName, int apiDataType) {
		if (DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE.equalsIgnoreCase(choiceName)) {
			return convertApiTypeToROMColumnType(apiDataType);
		} else if (DesignChoiceConstants.CHOICE_PARAM_TYPE.equalsIgnoreCase(choiceName)) {
			return convertApiTypeToROMParameterType(apiDataType);
		}

		return null;
	}

	/**
	 * Checks whether the oda data type is compaible with rom data type.
	 * 
	 * @param romDataType     the rom data type in string
	 * @param odaDataTypeCode the oda data type in integer
	 * @return <code>true</code> if they are compatible. Otherwise,
	 *         <code>false</code>.
	 * @throws BirtException
	 */

	private static boolean isCompatible(String choiceName, String romDataType, int odaDataTypeCode)
			throws BirtException {
		int apiDataType = DataTypeUtil.toApiDataType(odaDataTypeCode);
		if (DataType.UNKNOWN_TYPE == apiDataType)
			return true;

		String convertedType = null;
		if (DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE.equalsIgnoreCase(choiceName)) {
			convertedType = convertApiTypeToROMColumnType(apiDataType);
		} else if (DesignChoiceConstants.CHOICE_PARAM_TYPE.equalsIgnoreCase(choiceName)) {
			convertedType = convertApiTypeToROMParameterType(apiDataType);
		}

		if (convertedType != null && convertedType.equalsIgnoreCase(romDataType))
			return true;

		return false;
	}

}
