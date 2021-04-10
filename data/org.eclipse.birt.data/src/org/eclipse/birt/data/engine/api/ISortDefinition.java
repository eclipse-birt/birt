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
package org.eclipse.birt.data.engine.api;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * Describes one sort (key, direction) pair in a sort sequence. The sort key can
 * be a single column name or a Javascript expression.<br>
 * NOTE: Presently only sorting on actual columns are supported. If the sort key
 * is specified as an expression, it must be in the form
 * <code>row.column_name</code>, or <code>row["column_name"]</code>
 */
public interface ISortDefinition {
	/**
	 * When the sort strength is set to -1, it indicate we do an ASCII sort rather
	 * than Collator sort.
	 */
	public static final int ASCII_SORT_STRENGTH = -1;

	/**
	 * 
	 */
	public static final int DEFAULT_SORT_STRENGTH = Collator.TERTIARY;

	// Enumeration constants for sort direction
	/**
	 * Sorts in ascending order of sort key values
	 */
	public static final int SORT_ASC = 0;

	/**
	 * Sorts in descending order of sort key values
	 */
	public static final int SORT_DESC = 1;

	/**
	 * Returns the name of the column to sort on. Either the KeyColumn or KeyExpr
	 * can be used to define the sort key.
	 */
	public abstract String getColumn();

	/**
	 * Returns the JavaScript expression that defines the group key. <br>
	 */
	public abstract IScriptExpression getExpression();

	/**
	 * Returns the sort direction.
	 * 
	 * @return the sort direction: one of SORT_ASC or SORT_DESC
	 */
	public abstract int getSortDirection();

	/**
	 * Returns the Strength of sort.
	 * 
	 * @return
	 */
	public abstract int getSortStrength();

	/**
	 * Return the locale based on which the sort should be done.
	 */
	public abstract ULocale getSortLocale();

	/**
	 * Set the locale of the sort.
	 * 
	 * @param locale
	 */
	public abstract void setSortLocale(ULocale locale);

	/**
	 * Set the strength of the sort
	 * 
	 * @param Strength
	 */
	public abstract void setSortStrength(int Strength);
}