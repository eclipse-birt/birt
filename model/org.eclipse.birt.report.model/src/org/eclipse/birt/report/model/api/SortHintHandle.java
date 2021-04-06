/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.SortHint;

/**
 * This class represents sort hint handle.
 * 
 */
public class SortHintHandle extends StructureHandle {

	/**
	 * @param valueHandle the value handle for computed column list of one property
	 * @param index       the position in the list.
	 */
	public SortHintHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Gets the column name.
	 * 
	 * @return the column name.
	 */
	public String getColumnName() {
		return getStringProperty(SortHint.COLUMN_NAME_MEMBER);
	}

	/**
	 * Sets the column Name.
	 * 
	 * @param columnName the column name.
	 * @throws SemanticException
	 * 
	 */
	public void setColumnName(String columnName) throws SemanticException {
		setProperty(SortHint.COLUMN_NAME_MEMBER, columnName);
	}

	/**
	 * Gets the index position of a result set column.
	 * 
	 * @return the index position of a result set column.
	 */
	public int getPosition() {
		return getIntProperty(SortHint.POSITION_MEMBER);
	}

	/**
	 * Sets the index position of a result set column.
	 * 
	 * @param position the index position of a result set column.
	 * @throws SemanticException
	 */
	public void setPosition(int position) throws SemanticException {
		setProperty(SortHint.POSITION_MEMBER, position);
	}

	/**
	 * Gets the sort direction of this result set column. The possible values are
	 * define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @return the sort direction of this result set column.
	 */
	public String getDirection() {
		return getStringProperty(SortHint.DIRECTION_MEMBER);
	}

	/**
	 * Sets the direction value. It indicates the sort direction of this result set
	 * column. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @param direction the sort direction of this result set column.
	 * @throws SemanticException
	 */
	public void setDirection(String direction) throws SemanticException {
		setProperty(SortHint.DIRECTION_MEMBER, direction);
	}

	/**
	 * Gets the null value ordering. It indicates the ordering of null vs. non-null
	 * values in the sort order. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>NULL_VALUE_ORDERING_TYPE_UNKNOWN
	 * <li>NULL_VALUE_ORDERING_TYPE_NULLISFIRST
	 * <li>NULL_VALUE_ORDERING_TYPE_NULLISLAST
	 * </ul>
	 * 
	 * @return the null value ordering.
	 */
	public String getNullValueOrdering() {
		return getStringProperty(SortHint.NULL_VALUE_ORDERING_MEMBER);
	}

	/**
	 * Sets the null value ordering. It indicates the ordering of null vs. non-null
	 * values in the sort order. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>NULL_VALUE_ORDERING_TYPE_UNKNOWN
	 * <li>NULL_VALUE_ORDERING_TYPE_NULLISFIRST
	 * <li>NULL_VALUE_ORDERING_TYPE_NULLISLAST
	 * </ul>
	 * 
	 * @param nullValueOrdering the null value ordering.
	 * @throws SemanticException
	 */
	public void setNullValueOrdering(String nullValueOrdering) throws SemanticException {
		setProperty(SortHint.NULL_VALUE_ORDERING_MEMBER, nullValueOrdering);
	}

	/**
	 * Indicates whether this sort key can be excluded at runtime.
	 * 
	 * @return <true> if this sort key can be excluded at runtime, otherwise return
	 *         <false>.
	 */
	public boolean isOptional() {
		Boolean isOptional = (Boolean) getProperty(SortHint.IS_OPTIONAL_MEMBER);
		if (isOptional == null)
			return false;
		return isOptional.booleanValue();

	}

	/**
	 * Sets the isOptional value.
	 * 
	 * @param isOptional <true> if this sort key can be excluded at runtime,
	 *                   otherwise return <false>.
	 */
	public void setOptional(boolean isOptional) {
		setPropertySilently(FilterCondition.IS_OPTIONAL_MEMBER, Boolean.valueOf(isOptional));
	}
}
