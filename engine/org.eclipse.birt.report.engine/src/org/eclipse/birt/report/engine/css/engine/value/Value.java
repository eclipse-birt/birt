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

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

/**
 * This class provides an abstract implementation of the Value interface.
 *
 */
public abstract class Value implements CSSValue, CSSPrimitiveValue {

	/**
	 * Implements {@link Value#getCssValueType()}.
	 */
	@Override
	public short getCssValueType() {
		return CSSValue.CSS_PRIMITIVE_VALUE;
	}

	@Override
	public void setCssText(String cssText) throws DOMException {
		throw createDOMException();
	}

	@Override
	public short getPrimitiveType() {
		throw createDOMException();
	}

	@Override
	public void setFloatValue(short unitType, float floatValue) throws DOMException {
		throw createDOMException();
	}

	@Override
	public float getFloatValue(short unitType) throws DOMException {
		throw createDOMException();
	}

	public float getFloatValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public void setStringValue(short stringType, String stringValue) throws DOMException {
		throw createDOMException();
	}

	@Override
	public String getStringValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public Counter getCounterValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public Rect getRectValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public RGBColor getRGBColorValue() throws DOMException {
		throw createDOMException();
	}

	@Override
	public String getCssText() {
		throw createDOMException();
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 */
	protected DOMException createDOMException() {
		Object[] p = { Integer.valueOf(getCssValueType()) };
		String s = Messages.formatMessage("invalid.value.access", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

}
