/*******************************************************************************
 * Copyright (c) 2023 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;

/**
 * class to handle the CSS properties of background image type
 *
 * @since 4.13
 *
 */
public class BackgroundImageType extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		// CSSConstants.CSS_BACKGROUND_IMAGE_TYPE_PROPERTY
		values.put(CSSConstants.CSS_URL_VALUE, CSSValueConstants.URL_VALUE);
		values.put(CSSConstants.CSS_EMBED_VALUE, CSSValueConstants.EMBED_VALUE);
	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	/**
	 * constructor of the new identifier manager of the backround image type
	 */
	public BackgroundImageType() {
	}

	@Override
	public String getPropertyName() {
		return BIRTConstants.BIRT_BACKGROUND_IMAGE_TYPE;
	}

	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.URL_VALUE;
	}
}
