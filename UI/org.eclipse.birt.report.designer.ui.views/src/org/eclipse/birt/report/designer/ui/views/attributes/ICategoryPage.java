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

package org.eclipse.birt.report.designer.ui.views.attributes;

/**
 * Represents for the page for each category
 */
public interface ICategoryPage {

	/**
	 * Returns the display label for the category
	 * 
	 * @return the display label
	 */
	public String getDisplayLabel();

	/**
	 * Creates the page control with the given parent and style
	 * 
	 * @param parent the parent of the page control
	 * @param style  the style of the page control
	 * 
	 * @return the page control created
	 */

	public TabPage createPage();

	/**
	 * @return Returns the category key name
	 */
	public String getCategoryKey();

}
