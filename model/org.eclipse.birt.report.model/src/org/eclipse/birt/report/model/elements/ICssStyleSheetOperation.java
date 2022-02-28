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

	int dropCss(CssStyleSheet css);

	/**
	 * Adds the given css to css style sheets list.
	 *
	 * @param css the css to insert
	 */

	void addCss(CssStyleSheet css);

	/**
	 * Returns only csses this module includes directly.
	 *
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	List<CssStyleSheet> getCsses();

	/**
	 * Insert the given css to the given position
	 *
	 * @param css
	 * @param index
	 */

	void insertCss(CssStyleSheet css, int index);
}
