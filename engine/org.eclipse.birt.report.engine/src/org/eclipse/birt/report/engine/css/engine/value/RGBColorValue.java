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
 *  Actuate Corporation  - modification of Batik's RGBColorValue.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

/**
 * This class represents RGB colors.
 *
 */
public class RGBColorValue extends Value implements RGBColor {

	/**
	 * The red component.
	 */
	protected CSSPrimitiveValue red;

	/**
	 * The green component.
	 */
	protected CSSPrimitiveValue green;

	/**
	 * The blue component.
	 */
	protected CSSPrimitiveValue blue;

	/**
	 * Creates a new RGBColorValue.
	 */
	public RGBColorValue(CSSPrimitiveValue r, CSSPrimitiveValue g, CSSPrimitiveValue b) {
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_RGBCOLOR;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return "rgb(" + red.getCssText() + ", " + green.getCssText() + ", " + blue.getCssText() + ")";
	}

	/**
	 * Implements {@link Value#getRed()}.
	 */
	@Override
	public CSSPrimitiveValue getRed() throws DOMException {
		return red;
	}

	/**
	 * Implements {@link Value#getGreen()}.
	 */
	@Override
	public CSSPrimitiveValue getGreen() throws DOMException {
		return green;
	}

	/**
	 * Implements {@link Value#getBlue()}.
	 */
	@Override
	public CSSPrimitiveValue getBlue() throws DOMException {
		return blue;
	}

	@Override
	public RGBColor getRGBColorValue() throws DOMException {
		return this;
	}

	/**
	 * Returns a printable representation of the color.
	 */
	@Override
	public String toString() {
		return getCssText();
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof RGBColorValue) {
			RGBColorValue color = (RGBColorValue) value;
			if (red.equals(color.red) && blue.equals(color.blue) && green.equals(color.green)) {
				return true;
			}
		}
		return false;

	}
}
