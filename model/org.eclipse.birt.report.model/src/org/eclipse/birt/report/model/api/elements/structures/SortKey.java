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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

import com.ibm.icu.util.ULocale;

/**
 * This class represents a sort entry for a table or list item, it defines the
 * column and sort direction pair. Each sort key has the following properties:
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

public class SortKey extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String SORT_STRUCT = "SortKey"; //$NON-NLS-1$

	/**
	 * Name of the "key" member. An expression that gives the sort key on which to
	 * sort.
	 */

	public static final String KEY_MEMBER = "key"; //$NON-NLS-1$

	/**
	 * Name of the "direction" member.
	 */

	public static final String DIRECTION_MEMBER = "direction"; //$NON-NLS-1$

	/**
	 * Name of the member that defines the strength of the sort collation.
	 */
	public static final String STRENGTH_MEMBER = "strength"; //$NON-NLS-1$

	/**
	 * Name of the member that defines the locale of the sort collation.
	 */
	public static final String LOCALE_MEMBER = "locale"; //$NON-NLS-1$

	/**
	 * Value of the "key" member.
	 */

	private Expression key = null;

	/**
	 * Value of the "direction" member.
	 */

	private String direction = null;

	/**
	 * Value of sort strength.
	 */

	private Integer strength = null;

	/**
	 * Value of sort locale.
	 */

	private ULocale locale = null;

	/**
	 * Constructs the sort key with the key to sort and the direction.
	 *
	 * @param key       the key of the sort entry
	 * @param direction sort direction: Ascending or descending order
	 */

	public SortKey(String key, String direction) {
		this.key = new Expression(key, null);
		this.direction = direction;
	}

	/**
	 * Default constructor.
	 *
	 */

	public SortKey() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return SORT_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (KEY_MEMBER.equals(propName)) {
			return key;
		} else if (DIRECTION_MEMBER.equals(propName)) {
			return direction;
		} else if (STRENGTH_MEMBER.equals(propName)) {
			return strength;
		} else if (LOCALE_MEMBER.equals(propName)) {
			return locale;
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
		if (KEY_MEMBER.equals(propName)) {
			key = (Expression) value;
		} else if (DIRECTION_MEMBER.equals(propName)) {
			direction = (String) value;
		} else if (STRENGTH_MEMBER.equals(propName)) {
			strength = (Integer) value;
		} else if (LOCALE_MEMBER.equals(propName)) {
			locale = (ULocale) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the expression that gives the sort key on which to sort.
	 *
	 * @return the sort key on which to sort
	 */

	public String getKey() {
		return getStringProperty(null, KEY_MEMBER);
	}

	/**
	 * Sets the expression that gives the sort key on which to sort.
	 *
	 * @param key the sort key to set
	 */

	public void setKey(String key) {
		setProperty(KEY_MEMBER, key);
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
	 * @return the sort direction
	 */

	public String getDirection() {
		return (String) getProperty(null, DIRECTION_MEMBER);

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
	 */

	public void setDirection(String direction) {
		setProperty(DIRECTION_MEMBER, direction);
	}

	/**
	 * Gets the strength of the sort collation. By default it is -1.
	 *
	 * @return the strength of the sort
	 */

	public int getStrength() {
		PropertyDefn propDefn = (PropertyDefn) getMemberDefn(STRENGTH_MEMBER);
		assert propDefn != null;

		Object strength = getProperty(null, propDefn);
		return propDefn.getIntValue(null, strength);
	}

	/**
	 * Sets the strength of this sort collation.
	 *
	 * @param strength
	 */
	public void setStrength(int strength) {
		setProperty(STRENGTH_MEMBER, Integer.valueOf(strength));
	}

	/**
	 * Gets the locale of this sort.
	 *
	 * @return locale of this sort
	 */
	public ULocale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale of this sort.
	 *
	 * @param locale the locale to set
	 */
	public void setLocale(ULocale locale) {
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new SortKeyHandle(valueHandle, index);
	}

	/**
	 * Returns the name of the column that needs sort.
	 *
	 * @return the column name.
	 *
	 * @deprecated This property has been removed. See the method {@link #getKey()}.
	 */

	@Deprecated
	public String getColumnName() {
		return getKey();
	}

	/**
	 * Sets the name of the column that needs sort.
	 *
	 * @param columnName the column name to set
	 *
	 * @deprecated This property has been removed. See the method
	 *             {@link #setKey(String)}.
	 */

	@Deprecated
	public void setColumnName(String columnName) {
		setKey(columnName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	@Override
	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(getKey())) {
			list.add(new PropertyValueException(element, getDefn().getMember(KEY_MEMBER), getColumnName(),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}
		return list;
	}
}
