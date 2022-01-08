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
 *  Actuate Corporation  - modification of Batik's AbstractValueFactory.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;

/**
 * This class provides a base implementation for the value factories.
 * 
 */
public abstract class AbstractValueFactory {

	/**
	 * Returns the name of the property handled.
	 */
	public abstract String getPropertyName();

	/**
	 * Creates a DOM exception, given an invalid identifier.
	 */
	protected DOMException createInvalidIdentifierDOMException(String ident) {
		Object[] p = new Object[] { getPropertyName(), ident };
		String s = Messages.formatMessage("invalid.identifier", p);
		return new DOMException(DOMException.SYNTAX_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid lexical unit type.
	 */
	protected DOMException createInvalidLexicalUnitDOMException(short type) {
		Object[] p = new Object[] { getPropertyName(), Integer.valueOf(type) };
		String s = Messages.formatMessage("invalid.lexical.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float type.
	 */
	protected DOMException createInvalidFloatTypeDOMException(short t) {
		Object[] p = new Object[] { getPropertyName(), Integer.valueOf(t) };
		String s = Messages.formatMessage("invalid.float.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float value.
	 */
	protected DOMException createInvalidFloatValueDOMException(float f) {
		Object[] p = new Object[] { getPropertyName(), new Float(f) };
		String s = Messages.formatMessage("invalid.float.value", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid string type.
	 */
	protected DOMException createInvalidStringTypeDOMException(short t) {
		Object[] p = new Object[] { getPropertyName(), Integer.valueOf(t) };
		String s = Messages.formatMessage("invalid.string.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createMalformedLexicalUnitDOMException() {
		Object[] p = new Object[] { getPropertyName() };
		String s = Messages.formatMessage("malformed.lexical.unit", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createDOMException() {
		Object[] p = new Object[] { getPropertyName() };
		String s = Messages.formatMessage("invalid.access", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}
}
