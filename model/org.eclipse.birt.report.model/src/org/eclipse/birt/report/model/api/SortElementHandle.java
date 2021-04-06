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

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ISortElementModel;

import com.ibm.icu.util.ULocale;

/**
 * 
 */
public class SortElementHandle extends ContentElementHandle implements ISortElementModel {

	/**
	 * Constructs a sort handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public SortElementHandle(Module module, DesignElement element) {
		super(module, element);
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
		return getStringProperty(KEY_PROP);
	}

	/**
	 * Sets an expression that gives the sort key on which to sort.
	 * 
	 * @param key the key to sort
	 * @throws SemanticException value required exception
	 * @see #getKey()
	 */

	public void setKey(String key) throws SemanticException {
		setStringProperty(KEY_PROP, key);
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
		return getStringProperty(DIRECTION_PROP);
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
		setStringProperty(DIRECTION_PROP, direction);
	}

	/**
	 * Gets the member value handle of this sort element if it sets. Otherwise
	 * return null.
	 * 
	 * @return the member value handle.
	 */
	public MemberValueHandle getMember() {
		List contents = getContents(MEMBER_PROP);
		if (contents != null && contents.size() > 0)
			return (MemberValueHandle) contents.get(0);
		return null;
	}

	/**
	 * Gets the strength of this sort collation. By default, it is -1.
	 * 
	 * @return the strength of this sort
	 * 
	 * @see #setStrength(int)
	 */

	public int getStrength() {
		return getIntProperty(STRENGTH_PROP);
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
		setIntProperty(STRENGTH_PROP, strength);
	}

	/**
	 * Gets the locale of this sort collation.
	 * 
	 * @return the locale of this sort
	 * 
	 * @see #setLocale(ULocale)
	 */

	public ULocale getLocale() {
		return (ULocale) getProperty(LOCALE_PROP);
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
		setProperty(LOCALE_PROP, locale);
	}
}
