/*
 *************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import java.sql.Types;

import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PredefinedDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.PrimitiveType;
import org.eclipse.datatools.modelbase.sql.query.helper.DataTypeHelper;

/**
 * An internal utility class to process the SQM data type.
 */
public class SQLDataTypeUtility {
	private SQLDataTypeUtility() {
	}

	public static int toJDBCTypeCode(DataType varDataType) {
		if (varDataType == null)
			return Types.NULL; // unknown value

		String varDataTypeName = varDataType.getName();
		if (varDataTypeName == null && varDataType instanceof PredefinedDataType) {
			varDataTypeName = DataTypeHelper
					.getPrimitiveTypeName(((PredefinedDataType) varDataType).getPrimitiveType());
		}

		if (varDataTypeName != null) {
			int nativeTypeCode = DataTypeHelper.getJDBCTypeForNamedType(varDataTypeName);
			if (nativeTypeCode != 0) // has valid value
				return nativeTypeCode;
		}

		// covers native data type conversion not handled by DataTypeHelper
		if (varDataType instanceof PredefinedDataType) {
			int primitiveType = ((PredefinedDataType) varDataType).getPrimitiveType().getValue();
			switch (primitiveType) {
			case PrimitiveType.BOOLEAN:
				return java.sql.Types.BOOLEAN;
			case PrimitiveType.FLOAT:
				return java.sql.Types.FLOAT;
			}
			// TODO - handling of additional primitive JDBC data types
		}

		return Types.NULL; // unknown value
	}

}
