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

import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class represents one column in the result set.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each result set column
 * has the following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a result set column has an optional name.</dd>
 *
 * <dt><strong>Position </strong></dt>
 * <dd>a result set column has an optional position for it.</dd>
 *
 * <dt><strong>Data Type </strong></dt>
 * <dd>a result set column has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * </dl>
 *
 */

public class ResultSetColumn extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String RESULT_SET_COLUMN_STRUCT = "ResultSetColumn"; //$NON-NLS-1$

	/**
	 * Name of the position member.
	 */

	public static final String POSITION_MEMBER = "position"; //$NON-NLS-1$

	/**
	 * Name of the column name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the data type member.
	 */

	public static final String DATA_TYPE_MEMBER = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the native (database) data type code.
	 */

	public static final String NATIVE_DATA_TYPE_MEMBER = "nativeDataType"; //$NON-NLS-1$

	/**
	 * The parameter position.
	 */

	private Integer position = null;

	/**
	 * The column name.
	 */

	private String columnName = null;

	/**
	 * The data type of this column.
	 */

	private String dataType = null;

	/**
	 * The native (database) data type.
	 */

	private Integer nativeDataType;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return RESULT_SET_COLUMN_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (POSITION_MEMBER.equals(propName)) {
			return position;
		}
		if (NAME_MEMBER.equals(propName)) {
			return columnName;
		}
		if (DATA_TYPE_MEMBER.equals(propName)) {
			return dataType;
		}
		if (NATIVE_DATA_TYPE_MEMBER.equals(propName)) {
			return nativeDataType;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (POSITION_MEMBER.equals(propName)) {
			position = (Integer) value;
		} else if (NAME_MEMBER.equals(propName)) {
			columnName = (String) value;
		} else if (DATA_TYPE_MEMBER.equals(propName)) {
			dataType = (String) value;
		} else if (NATIVE_DATA_TYPE_MEMBER.equals(propName)) {
			nativeDataType = (Integer) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the column name.
	 *
	 * @return the column name.
	 */

	public String getColumnName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the column name.
	 *
	 * @param columnName the column name to set
	 */

	public void setColumnName(String columnName) {
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

	/**
	 * Returns the position of this parameter.
	 *
	 * @return the position of this parameter
	 */

	public Integer getPosition() {
		return (Integer) getProperty(null, POSITION_MEMBER);
	}

	/**
	 * Sets the position of this column.
	 *
	 * @param position the position to set
	 */

	public void setPosition(Integer position) {
		setProperty(POSITION_MEMBER, position);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ResultSetColumnHandle(valueHandle, index);
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

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The column name is required.
	 * </ul>
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public List validate(Module module, DesignElement element) {
		List list = super.validate(module, element);

		if (StringUtil.isBlank(columnName)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), columnName,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}
}
