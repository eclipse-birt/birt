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
