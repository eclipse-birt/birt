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

import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents uri values.
 * 
 */
public class URIValue extends StringValue {
	/**
	 * Creates a new StringValue.
	 */
	public URIValue(String uri) {
		super(CSSPrimitiveValue.CSS_URI, uri);
	}
}
