/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

/**
 * Interface of styled element
 *
 * @since 3.3
 *
 */
public interface IStyledElement {

	/**
	 * Get the computed style of the element
	 *
	 * @return computed style
	 */
	IStyle getComputedStyle();

	/**
	 * Get the style of the element
	 *
	 * @return the style
	 */
	IStyle getStyle();

	/**
	 * Get the style class
	 *
	 * @return the style class
	 */
	String getStyleClass();

	/**
	 * Set the style class
	 *
	 * @param styleClass style class
	 */
	void setStyleClass(String styleClass);

}
