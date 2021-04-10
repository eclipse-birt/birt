/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;

import com.ibm.icu.util.ULocale;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.ISortDefinition} interface.
 */

public class SortDefinition implements ISortDefinition {
	protected IScriptExpression keyExpr;
	protected String keyColumn;
	protected int direction;
	private int strength = ISortDefinition.ASCII_SORT_STRENGTH;
	private ULocale locale = null;

	public SortDefinition() {
	}

	/**
	 * Returns the name of the column to sort on. Either the KeyColumn or KeyExpr
	 * can be used to define the sort key.
	 */
	public String getColumn() {
		return keyColumn;
	}

	/**
	 * Returns the JavaScript expression that defines the group key. <br>
	 */
	public IScriptExpression getExpression() {
		return keyExpr;
	}

	/**
	 * @param keyColumn Name of key column to sort by
	 */
	public void setColumn(String keyColumn) {
		this.keyColumn = keyColumn;
		this.keyExpr = null;
	}

	/**
	 * @param keyExpr Key expression to sort by
	 */
	public void setExpression(String keyExpr) {
		this.keyExpr = new ScriptExpression(keyExpr);
		this.keyColumn = null;
	}

	public void setExpression(IScriptExpression keyExpr) {
		this.keyExpr = keyExpr;
		this.keyColumn = null;
	}

	/**
	 * Returns the sort direction.
	 * 
	 * @return the sort direction: one of SORT_ASC or SORT_DESC
	 */

	public int getSortDirection() {
		return direction;
	}

	/**
	 * @param sortDirection The sortDirection to set.
	 */
	public void setSortDirection(int sortDirection) {
		this.direction = sortDirection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.ISortDefinition#getSortStrength()
	 */
	public int getSortStrength() {
		return this.strength;
	}

	/**
	 * Set the collator sort strength of the destine sort.
	 * 
	 * @param strength
	 */
	public void setSortStrength(int strength) {
		this.strength = strength;
	}

	/**
	 * 
	 * @param locale
	 */
	public void setSortLocale(ULocale locale) {
		this.locale = locale;
	}

	public ULocale getSortLocale() {
		return this.locale;
	}
}
