/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;

/**
 *
 */

public class ParameterDefn implements IParameterDefn {

	private String name;
	private boolean isOptional = false;
	private boolean isDataField = false;
	private String displayName;
	private String description;
	private int[] supportedDataTypes;

	/**
	 *
	 * @param name
	 * @param displayName
	 * @param isOptional
	 * @param isDataField
	 * @param supportedDataTypes
	 * @param description
	 */
	public ParameterDefn(String name, String displayName, boolean isOptional, boolean isDataField,
			int[] supportedDataTypes, String description) {
		assert name != null;
		assert supportedDataTypes != null;

		this.name = name;
		this.isOptional = isOptional;
		this.isDataField = isDataField;
		this.displayName = displayName;
		this.supportedDataTypes = supportedDataTypes;
		this.description = description;
	}

	/**
	 * @param isOptional the isOptional to set
	 */
	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	/**
	 * @param isDataField the isDataField to set
	 */
	public void setDataField(boolean isDataField) {
		this.isDataField = isDataField;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#isDataField()
	 */
	@Override
	public boolean isDataField() {
		return isDataField;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#isOptional()
	 */
	@Override
	public boolean isOptional() {
		return isOptional;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#supportDataType(
	 * int)
	 */
	@Override
	public boolean supportDataType(int dataType) {
		if (dataType == DataType.UNKNOWN_TYPE) {
			return true;
		}

		for (int i = 0; i < supportedDataTypes.length; i++) {
			if (supportedDataTypes[i] == DataType.ANY_TYPE || supportedDataTypes[i] == dataType) {
				return true;
			}
		}
		return false;
	}

}
