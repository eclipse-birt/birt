
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

package org.eclipse.birt.data.oda.pojo.querymodel;

import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 */

public class VariableParameter implements IMethodParameter {
	private String name;
	private String dataType;
	private Object value;
	private String stringValue;

	public VariableParameter(String name, String dataType) {
		assert name != null && dataType != null;
		this.name = name;
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element createElement(Document doc) {
		Element ele = doc.createElement(Constants.ELEMENT_VARIABLEPARMETER);
		ele.setAttribute(Constants.ATTR_VARIABLEPARMETER_NAME, name);
		ele.setAttribute(Constants.ATTR_PARMETER_VALUE, stringValue);
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
		result = prime * result + name.hashCode();
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
		VariableParameter other = (VariableParameter) obj;
		if (!dataType.equals(other.dataType))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}

	public Object getTargetValue() {
		return value;
	}

	public void prepareValue(Map<String, Object> paramValues, ClassLoader pojoClassLoader) throws OdaException {
		if (!paramValues.containsKey(getName())) {
			throw new OdaException(Messages.getString("MethodSource.MissingPrameterValue", getName())); //$NON-NLS-1$
		}
		value = paramValues.get(getName());
	}

	public void setDataType(String type) {
		this.dataType = type;

	}

	public void setStringValue(String value) {
		this.stringValue = value;
	}

	public String getStringValue() {
		return this.stringValue;
	}

}
