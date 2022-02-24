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

import org.eclipse.birt.report.model.api.OdaLevelAttributeHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;

/**
 * This class represents one attribute of the level element.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each attribute has the
 * following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a result set column has an optional name.</dd>
 * 
 * <dt><strong>Data Type </strong></dt>
 * <dd>a result set column has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * 
 * <dt><strong>Native Name </strong></dt>
 * <dd>ODA defined name, controlled by the driver.</dd>
 * 
 * <dt><strong>Native Data Type Code </strong></dt>
 * <dd>ODA defined data type, controlled by the driver. It is integer type.</dd>
 * 
 * </dl>
 * 
 */

public class OdaLevelAttribute extends LevelAttribute {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "OdaLevelAttribute"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the native (database) data type code.
	 */

	public static final String NATIVE_DATA_TYPE_MEMBER = "nativeDataType"; //$NON-NLS-1$

	/**
	 * The native name for the result set.
	 */

	public static final String NATIVE_NAME_MEMBER = "nativeName"; //$NON-NLS-1$

	/**
	 * The native (database) data type.
	 */

	private Integer nativeDataType;

	/**
	 * Column name for ODA.
	 */

	private String nativeName = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */

	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new OdaLevelAttributeHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NATIVE_DATA_TYPE_MEMBER.equals(propName))
			return nativeDataType;
		if (NATIVE_NAME_MEMBER.equals(propName))
			return nativeName;
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
		if (NATIVE_DATA_TYPE_MEMBER.equals(propName))
			nativeDataType = (Integer) value;
		else if (NATIVE_NAME_MEMBER.equals(propName))
			nativeName = (String) value;
		else
			super.setIntrinsicProperty(propName, value);
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the result set column native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(null, NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the result set column native data type.
	 * 
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setProperty(NATIVE_DATA_TYPE_MEMBER, dataType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/**
	 * Sets the native oda dataset parameter name.
	 * 
	 * @param name the native name
	 */
	public void setNativeName(String name) {
		setProperty(NATIVE_NAME_MEMBER, name);
	}

	/**
	 * Returns the native column name.
	 * 
	 * @return native column name
	 */

	public String getNativeName() {
		return (String) getProperty(null, NATIVE_NAME_MEMBER);
	}
}
