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

package org.eclipse.birt.report.engine.css.dom;

import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.css.CSSValue;

public class StyleDeclaration extends AbstractStyle {
	protected CSSValue[] values;
	protected boolean shared;
	protected int propertyCount = 0;

	/**
	 * set the property
	 */
	public void setProperty(int index, CSSValue value) {
		// assert index >= 0 && index < NUMBER_OF_STYLE;
		if (values[index] != value) {
			if (shared) {
				decouple();
			}
			if (values[index] == null) {
				propertyCount++;
			} else if (value == null) {
				propertyCount--;
			}
			values[index] = value;
		}
	}

	/**
	 * set the property
	 */
	public CSSValue getProperty(int index) {
		assert index >= 0 && index < NUMBER_OF_STYLE;
		return values[index];
	}

	public StyleDeclaration(StyleDeclaration style) {
		super(style.engine);
		this.values = style.values;
		this.propertyCount = style.propertyCount;
		this.shared = true;
	}

	public StyleDeclaration(CSSEngine engine) {
		super(engine);
		this.values = new CSSValue[NUMBER_OF_STYLE];
		this.shared = false;
	}

	// TODO: remoview, there is a problem for list value.
	protected void decouple() {
		CSSValue[] newValues = new CSSValue[NUMBER_OF_STYLE];
		System.arraycopy(values, 0, newValues, 0, NUMBER_OF_STYLE);
		values = newValues;
		shared = false;
	}

	public boolean isEmpty() {
		return propertyCount == 0;
	}

	public int getLength() {
		return propertyCount;
	}

	public boolean equals(Object aStyle) {
		if (aStyle instanceof StyleDeclaration) {
			StyleDeclaration style = (StyleDeclaration) aStyle;
			if (propertyCount == style.propertyCount) {
				for (int i = 0; i < NUMBER_OF_STYLE; i++) {
					CSSValue value1 = values[i];
					CSSValue value2 = style.values[i];
					if (value1 != value2 && (value1 == null || !value1.equals(value2))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public void write(DataOutputStream out) throws IOException {
		// count how many valid value in the style
		IOUtil.writeInt(out, propertyCount);

		// write the style's property
		for (int i = 0; i < values.length; i++) {
			CSSValue value = values[i];
			if (null != value) {
				String propertyName = engine.getPropertyName(i);
				IOUtil.writeString(out, propertyName);
				writeCSSValue(out, propertyName, value);
			}
		}
	}
}
