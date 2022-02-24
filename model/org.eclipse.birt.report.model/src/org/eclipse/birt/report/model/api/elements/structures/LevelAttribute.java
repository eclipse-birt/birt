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

import java.util.List;

import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

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
 * </dl>
 * 
 */

public class LevelAttribute extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "LevelAttribute"; //$NON-NLS-1$

	/**
	 * Name of the column name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the data type member.
	 */

	public static final String DATA_TYPE_MEMBER = "dataType"; //$NON-NLS-1$

	/**
	 * Constant for the name of the 'DateTime' level attribute.
	 */
	public static final String DATE_TIME_ATTRIBUTE_NAME = "DateTime"; //$NON-NLS-1$

	/**
	 * The column name.
	 */

	private String name = null;

	/**
	 * The data type of this column.
	 */

	private String dataType = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NAME_MEMBER.equals(propName))
			return name;
		if (DATA_TYPE_MEMBER.equals(propName))
			return dataType;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (NAME_MEMBER.equals(propName))
			name = (String) value;
		else if (DATA_TYPE_MEMBER.equals(propName))
			dataType = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name.
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName the column name to set
	 */

	public void setName(String columnName) {
		setProperty(NAME_MEMBER, columnName);
	}

	/**
	 * Returns the data type of this column. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @return the data type of this column.
	 */

	public String getDataType() {
		return (String) getProperty(null, DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the data type of this column. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @param dataType the data type to set
	 */

	public void setDataType(String dataType) {
		setProperty(DATA_TYPE_MEMBER, dataType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new LevelAttributeHandle(valueHandle, index);
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The column name is required.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		List list = super.validate(module, element);

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}
}
