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
	 * return the css engine used by this elemetn
	 * 
	 * @return
	 */
	CSSEngine getCSSEngine();

	/**
	 * Returns the computed style of this element/pseudo-element.
	 */
	IStyle getComputedStyle();

	/**
	 * return the style of the element
	 * 
	 * @return
	 */
	IStyle getStyle();
}
