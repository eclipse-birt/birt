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

package org.eclipse.birt.report.engine.css.engine;

import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IStyle;

/**
 * This interface must be implemented by the DOM elements which needs CSS
 * support.
 *
 */
public interface CSSStylableElement extends IElement {

	/**
	 * Return the CSS engine used by this element
	 *
	 * @return Return the CSS engine used by this element
	 */
	CSSEngine getCSSEngine();

	/**
	 * Returns the computed style of this element/pseudo-element.
	 *
	 * @return Returns the computed style of this element/pseudo-element.
	 */
	IStyle getComputedStyle();

	/**
	 * Return the style of the element
	 *
	 * @return Return the style of the element
	 */
	IStyle getStyle();
}
