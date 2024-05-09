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

package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.URIValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * One line Class Desc
 *
 * Complete Class Desc
 *
 */
public class URIManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
	}

	protected String propertyName;
	protected Value defaultValue;
	protected boolean inherit;

	/**
	 * Constructor
	 *
	 * @param propertyName
	 * @param inherit
	 * @param defaultValue
	 */
	public URIManager(String propertyName, boolean inherit, Value defaultValue) {
		this.propertyName = propertyName;
		this.defaultValue = defaultValue;
		this.inherit = inherit;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return inherit;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Create URI value Implements
	 * {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 *
	 * @param value  URI string
	 * @param engine CSS engine
	 * @return Return
	 * @throws DOMException
	 */
	public Value createValue(String value, CSSEngine engine) throws DOMException {
		return new URIValue(value);
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_URI) {
			return new URIValue(lu.getStringValue());
		}
		return createStringValue(lu.getLexicalUnitType(), lu.getStringValue());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.css.engine.ValueManager#createStringValue(
	 * short, java.lang.String, org.eclipse.birt.report.engine.css.engine.CSSEngine)
	 */
	@Override
	public Value createStringValue(short type, String value) throws DOMException {
		return new StringValue(CSSPrimitiveValue.CSS_STRING, value);
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	@Override
	public StringMap getIdentifiers() {
		return values;
	}
}
