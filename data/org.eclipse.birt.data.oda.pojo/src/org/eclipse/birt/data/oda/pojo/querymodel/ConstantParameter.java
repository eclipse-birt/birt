
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

package org.eclipse.birt.data.oda.pojo.querymodel;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.util.DataTypeUtil;
import org.eclipse.birt.data.oda.pojo.util.MethodParameterType;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 */

public class ConstantParameter implements IMethodParameter {
	private String stringValue;
	private String dataType;
	private Object targetTypeValue;

	public ConstantParameter(String stringValue, String dataType) {
		assert dataType != null;
		this.stringValue = stringValue;
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	/**
	 * @return the string value
	 */
	public String getStringValue() {
		return stringValue;
	}

	public Element createElement(Document doc) {
		Element ele = doc.createElement(Constants.ELEMENT_CONSTANTPARMETER);
		if (stringValue != null) {
			ele.setAttribute(Constants.ATTR_PARMETER_VALUE, stringValue);
		}
		ele.setAttribute(Constants.ATTR_PARAMETER_TYPE, getDataType());
		return ele;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dataType.hashCode();
		result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConstantParameter other = (ConstantParameter) obj;
		if (!dataType.equals(other.dataType))
			return false;
		if (stringValue == null) {
			if (other.stringValue != null)
				return false;
		} else if (!stringValue.equals(other.stringValue))
			return false;
		return true;
	}

	public Object getTargetValue() {
		return targetTypeValue;
	}

	@SuppressWarnings("unchecked")
	public void prepareValue(Map<String, Object> paramValues, ClassLoader pojoClassLoader) throws OdaException {
		MethodParameterType mpt = MethodParameterType.getInstance(dataType, pojoClassLoader);
		Class c = mpt.getJavaType();
		if (Boolean.class == c || Boolean.TYPE == c) {
			targetTypeValue = DataTypeUtil.toBooleanFromString(stringValue);
		} else if (Byte.class == c || Byte.TYPE == c) {
			targetTypeValue = DataTypeUtil.toByteFromString(stringValue);
		} else if (Short.class == c || Short.TYPE == c) {
			targetTypeValue = DataTypeUtil.toShortFromString(stringValue);
		} else if (Character.class == c || Character.TYPE == c) {
			targetTypeValue = DataTypeUtil.toCharFromString(stringValue);
		} else if (Integer.class == c || Integer.TYPE == c) {
			targetTypeValue = DataTypeUtil.toIntegerFromString(stringValue);
		} else if (Float.class == c || Float.TYPE == c) {
			targetTypeValue = DataTypeUtil.toFloatFromString(stringValue);
		} else if (Double.class == c || Double.TYPE == c) {
			targetTypeValue = DataTypeUtil.toDoubleFromString(stringValue);
		} else if (BigDecimal.class == c) {
			targetTypeValue = DataTypeUtil.toBigDecimalFromString(stringValue);
		} else if (Date.class == c || java.sql.Date.class == c) {
			targetTypeValue = DataTypeUtil.toDateFromString(stringValue);
		} else if (Time.class == c) {
			targetTypeValue = DataTypeUtil.toTimeFromString(stringValue);
		} else if (java.sql.Timestamp.class == c) {
			targetTypeValue = DataTypeUtil.toTimestampFromString(stringValue);
		} else {
			targetTypeValue = stringValue;
		}
	}

	public void setDataType(String type) {
		this.dataType = type;
	}

	public void setStringValue(String value) {
		this.stringValue = value;
	}

}
