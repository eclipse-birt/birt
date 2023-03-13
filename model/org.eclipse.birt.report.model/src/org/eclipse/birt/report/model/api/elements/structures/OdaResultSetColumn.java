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

import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;

/**
 *
 *
 *
 */
public class OdaResultSetColumn extends ResultSetColumn {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "OdaResultSetColumn"; //$NON-NLS-1$

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
	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new OdaResultSetColumnHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {

		if (NATIVE_DATA_TYPE_MEMBER.equals(propName)) {
			return nativeDataType;
		}
		if (NATIVE_NAME_MEMBER.equals(propName)) {
			return nativeName;
		}
		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (NATIVE_DATA_TYPE_MEMBER.equals(propName)) {
			nativeDataType = (Integer) value;
		} else if (NATIVE_NAME_MEMBER.equals(propName)) {
			nativeName = (String) value;
		} else {
			super.setIntrinsicProperty(propName, value);
		}
	}

	/**
	 * Returns the native data type.
	 *
	 * @return the result set column native data type.
	 */

	@Override
	public Integer getNativeDataType() {
		return (Integer) getProperty(null, NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the result set column native data type.
	 *
	 * @param dataType the native data type to set.
	 */

	@Override
	public void setNativeDataType(Integer dataType) {
		setProperty(NATIVE_DATA_TYPE_MEMBER, dataType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
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
	 * Returns the report column name.
	 *
	 * @return report column name
	 */

	public String getParamName() {
		return (String) getProperty(null, NATIVE_NAME_MEMBER);
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
