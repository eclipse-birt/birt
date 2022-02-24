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

import org.w3c.dom.css.CSSValue;

/**
 * This interface allows the user of a CSSEngine to provide contextual
 * informations.
 *
 */
public interface CSSContext {

	/**
	 * Returns the Value corresponding to the given system color.
	 */
	CSSValue getSystemColor(String ident);

	/**
	 * Returns the value corresponding to the default font-family.
	 */
	CSSValue getDefaultFontFamily();

	/**
	 * Returns a lighter font-weight.
	 */
	float getLighterFontWeight(float f);

	/**
	 * Returns a bolder font-weight.
	 */
	float getBolderFontWeight(float f);

	/**
	 * Returns the size of a px CSS unit in millimeters.
	 */
	float getPixelUnitToMillimeter();

	/**
	 * Returns the medium font size.
	 */
	float getMediumFontSize();
}
