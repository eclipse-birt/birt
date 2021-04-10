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

package org.eclipse.birt.report.designer.ui.parameters;

import java.io.Serializable;

/**
 * Parameter choice contains value or label.
 * 
 */

public class ParameterChoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5052627081310365919L;

	private Object value;

	private Object label;

	/**
	 * returns the value of the selection choice
	 * 
	 * @return the value of the selction choice
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * returns the locale-specific label for a selection choice. The locale used is
	 * the locale in the parameter definition request.
	 * 
	 * @return the localized label for the parameter
	 */

	public Object getLabel() {
		return label;
	}

	/**
	 * Sets value
	 * 
	 * @param value
	 */

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets label
	 * 
	 * @param label
	 */

	public void setLabel(Object label) {
		this.label = label;
	}

}
