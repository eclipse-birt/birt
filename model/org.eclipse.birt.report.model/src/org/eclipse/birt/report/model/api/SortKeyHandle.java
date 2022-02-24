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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

import com.ibm.icu.util.ULocale;

/**
 * Represents the handle of sort key structure. The sort key is the sort entry
 * for a table or list item, it defines the column and sort direction pair. Each
 * sort key has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Column Name </strong></dt>
 * <dd>the name of the column that is sorted.</dd>
 * 
 * <dt><strong>Direction </strong></dt>
 * <dd>the sort direction:asc or desc.</dd>
 * </dl>
 * 
 */
public class SortKeyHandle extends StructureHandle {

	/**
	 * Constructs the handle of sort key.
	 * 
	 * @param valueHandle the value handle for sort key list of one property
	 * @param index       the position of this sort key in the list
	 */

	public SortKeyHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns an expression that gives the sort key on which to sort. The simplest
	 * case is the name of a column. The expression can also be an expression that
	 * includes columns. When used for a group, the expression can contain an
	 * aggregate computed over the group.
	 * 
	 * @return the key to sort
	 * 
	 * @see #setKey(String)
	 */

	public String getKey() {
		return getStringProperty(SortKey.KEY_MEMBER);
	}

	/**
	 * Sets an expression that gives the sort key on which to sort.
	 * 
	 * @param key the key to sort
	 * @throws SemanticException value required exception
	 * @see #getKey()
	 */

	public void setKey(String key) throws SemanticException {
		setProperty(SortKey.KEY_MEMBER, key);
	}

	/**
	 * Returns the sort direction. The possible values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @return the direction to sort
	 */

	public String getDirection() {
		return getStringProperty(SortKey.DIRECTION_MEMBER);
	}

	/**
	 * Sets the sort direction. The allowed values are define in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>SORT_DIRECTION_ASC
	 * <li>SORT_DIRECTION_DESC
	 * </ul>
	 * 
	 * @param direction the direction to set
	 * @throws SemanticException if the direction is not in choice list.
	 */

	public void setDirection(String direction) throws SemanticException {
		setProperty(SortKey.DIRECTION_MEMBER, direction);
	}

	/**
	 * Returns the name of the column that needs sort.
	 * 
	 * @return the column name
	 * 
	 * @deprecated This property has been removed. See the method {@link #getKey()}.
	 */

	public String getColumnName() {
		return getKey();
	}

	/**
	 * Sets the name of the column that needs sort.
	 * 
	 * @param columnName the column name to set
	 * @throws SemanticException value required exception
	 * @deprecated This property has been removed. See the method
	 *             {@link #setKey(String)}.
	 */

	public void setColumnName(String columnName) throws SemanticException {
		setKey(columnName);
	}

	/**
	 * Gets the strength of this sort collation. By default, it is -1.
	 * 
	 * @return the strength of this sort
	 * 
	 * @see #setStrength(int)
	 */

	public int getStrength() {
		return getIntProperty(SortKey.STRENGTH_MEMBER);
	}

	/**
	 * Sets the strength for this sort.
	 * 
	 * @param strength the strength to sort
	 * @throws SemanticException
	 * 
	 * @see #getStrength()
	 */

	public void setStrength(int strength) throws SemanticException {
		setProperty(SortKey.STRENGTH_MEMBER, strength);
	}

	/**
	 * Gets the locale of this sort collation.
	 * 
	 * @return the locale of this sort
	 * 
	 * @see #setLocale(ULocale)
	 */

	public ULocale getLocale() {
		return (ULocale) getProperty(SortKey.LOCALE_MEMBER);
	}

	/**
	 * Sets the locale for this sort.
	 * 
	 * @param locale the locale to sort
	 * @throws SemanticException
	 * 
	 * @see #getLocale()
	 */

	public void setLocale(ULocale locale) throws SemanticException {
		setProperty(SortKey.LOCALE_MEMBER, locale);
	}
}
