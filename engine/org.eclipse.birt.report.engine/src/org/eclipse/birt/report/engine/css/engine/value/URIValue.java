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
