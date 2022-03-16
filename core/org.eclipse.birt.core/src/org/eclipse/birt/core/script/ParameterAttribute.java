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
