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

package org.eclipse.birt.core.script;

import java.io.Serializable;

public class ParameterAttribute implements Serializable {

	private static final long serialVersionUID = 3172532636112306947L;
	private Object value;
	private Object displayText;

	public ParameterAttribute(Object[] values, String[] displayTexts) {
		this.value = values;
		this.displayText = displayTexts;
	}

	public ParameterAttribute(Object value, String displayText) {
		this.value = value;
		this.displayText = displayText;
	}

	public ParameterAttribute() {
	}

	public Object getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public void setDisplayText(String[] displayTexts) {
		this.displayText = displayTexts;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
