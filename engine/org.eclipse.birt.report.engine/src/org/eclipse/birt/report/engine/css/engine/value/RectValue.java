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
 *  Actuate Corporation  - modification of Batik's RectValue.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

/**
 * This class represents CSS rect values.
 *
 */
public class RectValue extends Value implements Rect {

	/**
	 * The top value.
	 */
	protected Value top;

	/**
	 * The right value.
	 */
	protected Value right;

	/**
	 * The bottom value.
	 */
	protected Value bottom;

	/**
	 * The left value.
	 */
	protected Value left;

	/**
	 * Creates a new rectangle value.
	 *
	 * @param t top
	 * @param r right
	 * @param b bottom
	 * @param l left
	 */
	public RectValue(Value t, Value r, Value b, Value l) {
		top = t;
		right = r;
		bottom = b;
		left = l;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_RECT;
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return "rect(" + top.getCssText() + ", " + right.getCssText() + ", " + bottom.getCssText() + ", "
				+ left.getCssText() + ")";
	}

	@Override
	public CSSPrimitiveValue getTop() throws DOMException {
		return top;
	}

	@Override
	public CSSPrimitiveValue getRight() throws DOMException {
		return right;
	}

	@Override
	public CSSPrimitiveValue getBottom() throws DOMException {
		return bottom;
	}

	@Override
	public CSSPrimitiveValue getLeft() throws DOMException {
		return left;
	}

	/**
	 * Returns a printable representation of this value.
	 */
	@Override
	public String toString() {
		return getCssText();
	}
}
