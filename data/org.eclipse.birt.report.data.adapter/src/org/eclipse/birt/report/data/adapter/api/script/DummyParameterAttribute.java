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

package org.eclipse.birt.report.data.adapter.api.script;

import java.io.Serializable;

/**
 * The dummy object to hold tha report paramter attributes
 *
 */
public class DummyParameterAttribute implements Serializable {

	private static final long serialVersionUID = 29392938327489L;
	private Object value;
	private String displayText;

	/**
	 *
	 * @param value
	 * @param displayText
	 */
	public DummyParameterAttribute(Object value, String displayText) {
		this.value = value;
		this.displayText = displayText;
	}

	public DummyParameterAttribute() {
	}

	/**
	 *
	 * @return
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 *
	 * @param displayText
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	/**
	 *
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 *
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
