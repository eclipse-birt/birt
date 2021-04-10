
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.api;

/**
 * Constants defined
 */
public class Constants {
	// Element/Attribute names in XML formatted POJO query text
	public static final String ELEMENT_ROOT = "PojoQuery"; //$NON-NLS-1$
	public static final String ATTR_POJOQUERY_VERSION = "version"; //$NON-NLS-1$
	public static final String ATTR_POJOQUERY_DATASETCLASS = "dataSetClass"; //$NON-NLS-1$
	public static final String ATTR_POJOQUERY_APPCONTEXTKEY = "appContextKey"; //$NON-NLS-1$
	public static final String ElEMENT_COLUMNMAPPING = "ColumnMapping"; //$NON-NLS-1$
	public static final String ELEMENT_CLASSCOLUMNMAPPINGS = "ClassColumnMappings"; //$NON-NLS-1$
	public static final String ATTR_COLUMN_NAME = "name"; //$NON-NLS-1$
	public static final String ATTR_COLUMN_ODADATATYPE = "odaDataType"; //$NON-NLS-1$
	public static final String ATTR_COLUMN_INDEX = "index"; //$NON-NLS-1$
	public static final String ELEMENT_FIELD = "Field"; //$NON-NLS-1$
	public static final String ATTR_FIELD_NAME = "name"; //$NON-NLS-1$
	public static final String ELEMENT_METHOD = "Method"; //$NON-NLS-1$
	public static final String ATTR_METHOD_NAME = "name"; //$NON-NLS-1$
	public static final String ELEMENT_CONSTANTPARMETER = "ConstantParameter"; //$NON-NLS-1$
	public static final String ELEMENT_VARIABLEPARMETER = "VariableParameter"; //$NON-NLS-1$
	public static final String ATTR_PARMETER_VALUE = "value"; //$NON-NLS-1$
	public static final String ATTR_VARIABLEPARMETER_NAME = "name"; //$NON-NLS-1$
	public static final String ATTR_PARAMETER_TYPE = "type"; //$NON-NLS-1$

	// data source properties name
	public static final String POJO_DATA_SET_CLASS_PATH = "pojoDataSetClassPath"; //$NON-NLS-1$
	public static final String POJO_CLASS_PATH = "pojoClassPath"; //$NON-NLS-1$

	// data set properties name
	public static final String POJO_CLASS = "pojoClass"; //$NON-NLS-1$
	public static final String METHOD_NAME_REGEX = "methodNameRegx"; //$NON-NLS-1$

	// column oda types
	public static final String ODA_TYPE_String = "String"; //$NON-NLS-1$
	public static final String ODA_TYPE_Integer = "Integer"; //$NON-NLS-1$
	public static final String ODA_TYPE_Double = "Double"; //$NON-NLS-1$
	public static final String ODA_TYPE_Decimal = "Decimal"; //$NON-NLS-1$
	public static final String ODA_TYPE_Date = "Date"; //$NON-NLS-1$
	public static final String ODA_TYPE_Time = "Time"; //$NON-NLS-1$
	public static final String ODA_TYPE_Timestamp = "Timestamp"; //$NON-NLS-1$
	public static final String ODA_TYPE_Boolean = "Boolean"; //$NON-NLS-1$
	public static final String ODA_TYPE_Blob = "Blob"; //$NON-NLS-1$
	public static final String ODA_TYPE_Clob = "Clob"; //$NON-NLS-1$
	public static final String ODA_TYPE_Object = "Java Object"; //$NON-NLS-1$

	// parameter types
	public static final String PARAM_TYPE_boolean = "boolean"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Boolean = "java.lang.Boolean"; //$NON-NLS-1$
	public static final String PARAM_TYPE_byte = "byte"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Byte = "java.lang.Byte"; //$NON-NLS-1$
	public static final String PARAM_TYPE_char = "char"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Character = "java.lang.Character"; //$NON-NLS-1$
	public static final String PARAM_TYPE_double = "double"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Double = "java.lang.Double"; //$NON-NLS-1$
	public static final String PARAM_TYPE_float = "float"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Float = "java.lang.Float"; //$NON-NLS-1$
	public static final String PARAM_TYPE_int = "int"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Integer = "java.lang.Integer"; //$NON-NLS-1$
	public static final String PARAM_TYPE_long = "long"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Long = "java.lang.Long"; //$NON-NLS-1$
	public static final String PARAM_TYPE_short = "short"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Short = "java.lang.Short"; //$NON-NLS-1$
	public static final String PARAM_TYPE_BigDecimal = "java.math.BigDecimal"; //$NON-NLS-1$
	public static final String PARAM_TYPE_String = "java.lang.String"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Date = "java.util.Date"; //$NON-NLS-1$
	public static final String PARAM_TYPE_SqlDate = "java.sql.Date"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Time = "java.sql.Time"; //$NON-NLS-1$
	public static final String PARAM_TYPE_Timestamp = "java.sql.Timestamp"; //$NON-NLS-1$

	public static final String OPEN_METHOD_NAME = "open"; //$NON-NLS-1$
	public static final String NEXT_METHOD_NAME = "next"; //$NON-NLS-1$
	public static final String CLOSE_METHOD_NAME = "close"; //$NON-NLS-1$

	public static final char CLASS_PATH_SEPERATOR = ';';
	public static final String DEFAULT_VERSION = "1.0"; //$NON-NLS-1$
	public static final String SYNCHRONIZE_CLASS_PATH = "SynchronizeClassPath"; //$NON-NLS-1$

}
