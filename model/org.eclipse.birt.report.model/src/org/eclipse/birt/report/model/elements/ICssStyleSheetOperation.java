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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.css.CssStyleSheet;

/**
 * Interface of operating css style sheet.
 * 
 */
public interface ICssStyleSheetOperation {

	/**
	 * Drops the given css from css list.
	 * 
	 * @param css the css to drop
	 * @return the position of the css to drop
	 */

	public int dropCss(CssStyleSheet css);

	/**
	 * Adds the given css to css style sheets list.
	 * 
	 * @param css the css to insert
	 */

	public void addCss(CssStyleSheet css);

	/**
	 * Returns only csses this module includes directly.
	 * 
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	public List<CssStyleSheet> getCsses();

	/**
	 * Insert the given css to the given position
	 * 
	 * @param css
	 * @param index
	 */

	public void insertCss(CssStyleSheet css, int index);
}
