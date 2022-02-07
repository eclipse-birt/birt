
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
package org.eclipse.birt.data.oda.pojo.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * All supported type for method parameters
 */
public class MethodParameterType {

	private static MethodParameterType[] builtinTypes = new MethodParameterType[] {
			new MethodParameterType(Constants.PARAM_TYPE_boolean, Boolean.TYPE, Constants.ODA_TYPE_Boolean),
			new MethodParameterType(Constants.PARAM_TYPE_byte, Byte.TYPE, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_char, Character.TYPE, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_double, Double.TYPE, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_float, Float.TYPE, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_int, Integer.TYPE, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_long, Long.TYPE, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_short, Short.TYPE, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_Boolean, Boolean.class, Constants.ODA_TYPE_Boolean),
			new MethodParameterType(Constants.PARAM_TYPE_Byte, Byte.class, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_Character, Character.class, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_Double, Double.class, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_Float, Float.class, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_Integer, Integer.class, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_Long, Long.class, Constants.ODA_TYPE_Double),
			new MethodParameterType(Constants.PARAM_TYPE_Short, Short.class, Constants.ODA_TYPE_Integer),
			new MethodParameterType(Constants.PARAM_TYPE_String, String.class, Constants.ODA_TYPE_String),
			new MethodParameterType(Constants.PARAM_TYPE_BigDecimal, BigDecimal.class, Constants.ODA_TYPE_Decimal),
			new MethodParameterType(Constants.PARAM_TYPE_SqlDate, java.sql.Date.class, Constants.ODA_TYPE_Date),
			new MethodParameterType(Constants.PARAM_TYPE_Time, java.sql.Time.class, Constants.ODA_TYPE_Time),
			new MethodParameterType(Constants.PARAM_TYPE_Timestamp, java.sql.Timestamp.class,
					Constants.ODA_TYPE_Timestamp),
			new MethodParameterType(Constants.PARAM_TYPE_Date, Date.class, Constants.ODA_TYPE_Date), };

	@SuppressWarnings("unchecked")
	private static Map<Class, MethodParameterType> classInstanceMap = new HashMap<Class, MethodParameterType>();
	private static Map<String, MethodParameterType> nameInstanceMap = new HashMap<String, MethodParameterType>();
	static {
		for (MethodParameterType pt : builtinTypes) {
			classInstanceMap.put(pt.getJavaType(), pt);
			nameInstanceMap.put(pt.getName(), pt);
		}
	}

	private String name;
	@SuppressWarnings("unchecked")
	private Class javaType;

	private String nativeOdaDataTypeName;

	/**
	 * @return the nativeOdaDataTypeName
	 */
	public String getNativeOdaDataTypeName() {
		return nativeOdaDataTypeName;
	}

	@SuppressWarnings("unchecked")
	private MethodParameterType(String name, Class javaType, String nativeOdaDataTypeName) {
		this.name = name;
		this.javaType = javaType;
		this.nativeOdaDataTypeName = nativeOdaDataTypeName;
	}

	@SuppressWarnings("unchecked")
	public Class getJavaType() {
		return javaType;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public static MethodParameterType getInstance(String name, ClassLoader pojoClassLoader) throws OdaException {
		MethodParameterType result = nameInstanceMap.get(name);

		if (result == null) {
			// Maybe name is the full class name of Java native type, such as
			// java.lang.String, java.lang.Integer etc
			try {
				Class c = Class.forName(name);
				result = classInstanceMap.get(c);
			} catch (ClassNotFoundException e) {
			}
		}

		if (result == null) {
			try {
				Class c = pojoClassLoader.loadClass(name);
				result = new MethodParameterType(name, c, Constants.ODA_TYPE_Object);
			} catch (ClassNotFoundException e) {
				throw new OdaException(e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static MethodParameterType getInstance(Class c) {
		MethodParameterType result = classInstanceMap.get(c);
		if (result == null) {
			result = new MethodParameterType(c.getName(), c, Constants.ODA_TYPE_Object);
		}
		return result;
	}

	public static MethodParameterType[] getBuiltins() {
		return builtinTypes;
	}

	@SuppressWarnings("unchecked")
	public static String getNativeOdaDataTypeName(String name) {
		MethodParameterType mpt = nameInstanceMap.get(name);
		if (mpt != null) {
			return mpt.getNativeOdaDataTypeName();
		}
		// Maybe name is the full class name of Java native type, such as
		// java.lang.String, java.lang.Integer etc
		try {
			Class c = Class.forName(name);
			mpt = classInstanceMap.get(c);
			if (mpt != null) {
				return mpt.getNativeOdaDataTypeName();
			}
		} catch (ClassNotFoundException e) {
		}

		// If goes here, must a JAVA_OBJECT type
		return Constants.ODA_TYPE_Object;
	}
}
