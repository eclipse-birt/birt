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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;

/**
 * Represents the parameter for ODA drivers.
 * 
 */

public class OdaDataSetParameter extends DataSetParameter {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCT_NAME = "OdaDataSetParam"; //$NON-NLS-1$

	/**
	 * The parameter name used to refer to a report parameter.
	 */

	public static final String PARAM_NAME_MEMBER = "paramName";//$NON-NLS-1$

	/**
	 * The native name from oda parameter.
	 */

	public static final String NATIVE_NAME_MEMBER = "nativeName"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the native (database) data type code.
	 */

	public static final String NATIVE_DATA_TYPE_MEMBER = "nativeDataType"; //$NON-NLS-1$

	/**
	 * Report parameter name.
	 */

	private String paramName = null;

	/**
	 * Oda parameter name.
	 */

	private String nativeName = null;

	/**
	 * The native (database) data type.
	 */

	private Integer nativeDataType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new OdaDataSetParameterHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NATIVE_NAME_MEMBER.equals(propName))
			return nativeName;
		else if (PARAM_NAME_MEMBER.equals(propName))
			return paramName;
		else if (NATIVE_DATA_TYPE_MEMBER.equals(propName))
			return nativeDataType;
		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {

		if (NATIVE_NAME_MEMBER.equals(propName))
			nativeName = (String) value;
		else if (PARAM_NAME_MEMBER.equals(propName))
			paramName = (String) value;
		else if (NATIVE_DATA_TYPE_MEMBER.equals(propName))
			nativeDataType = (Integer) value;
		else
			super.setIntrinsicProperty(propName, value);

	}

	/**
	 * set the refered report parameter name.
	 * 
	 * @param name the parameter name
	 */

	public void setParamName(String name) {
		setProperty(PARAM_NAME_MEMBER, name);
	}

	/**
	 * set the native oda dataset parameter name.
	 * 
	 * @param name the native name
	 */
	public void setNativeName(String name) {
		setProperty(NATIVE_NAME_MEMBER, name);
	}

	/**
	 * returns the report parameter name.
	 * 
	 * @return report parameter name
	 */
	public String getParamName() {
		return (String) getProperty(null, PARAM_NAME_MEMBER);
	}

	/**
	 * returns the native parameter name.
	 * 
	 * @return native parameter name
	 */
	public String getNativeName() {
		return (String) getProperty(null, NATIVE_NAME_MEMBER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCT_NAME;
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the parameter native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(null, NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the parameter native data type.
	 * 
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setProperty(NATIVE_DATA_TYPE_MEMBER, dataType);
	}

}
