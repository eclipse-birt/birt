/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	 * Creates a new Rect value.
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
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_RECT;
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText() {
		return "rect(" + top.getCssText() + ", " + right.getCssText() + ", " + bottom.getCssText() + ", "
				+ left.getCssText() + ")";
	}

	/**
	 * Implements {@link Value#getTop()}.
	 */
	public CSSPrimitiveValue getTop() throws DOMException {
		return top;
	}

	/**
	 * Implements {@link Value#getRight()}.
	 */
	public CSSPrimitiveValue getRight() throws DOMException {
		return right;
	}

	/**
	 * Implements {@link Value#getBottom()}.
	 */
	public CSSPrimitiveValue getBottom() throws DOMException {
		return bottom;
	}

	/**
	 * Implements {@link Value#getLeft()}.
	 */
	public CSSPrimitiveValue getLeft() throws DOMException {
		return left;
	}

	/**
	 * Returns a printable representation of this value.
	 */
	public String toString() {
		return getCssText();
	}
}
