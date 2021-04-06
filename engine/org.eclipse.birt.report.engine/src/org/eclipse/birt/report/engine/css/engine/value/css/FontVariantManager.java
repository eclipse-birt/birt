/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - modification of Batik's FontVariantManager.java to support BIRT's CSS rules
 *******************************************************************************/
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

/**
 * This class provides a manager for the 'font-variant' property values.
 *
 */
public class FontVariantManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NORMAL_VALUE, CSSValueConstants.NORMAL_VALUE);
		values.put(CSSConstants.CSS_SMALL_CAPS_VALUE, CSSValueConstants.SMALL_CAPS_VALUE);
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	public boolean isInheritedProperty() {
		return true;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getPropertyName()}.
	 */
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_VARIANT_PROPERTY;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	public Value getDefaultValue() {
		return CSSValueConstants.NORMAL_VALUE;
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap getIdentifiers() {
		return values;
	}
}
