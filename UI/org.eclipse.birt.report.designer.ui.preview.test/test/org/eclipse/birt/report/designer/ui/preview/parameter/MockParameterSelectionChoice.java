/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;

/**
 * Mock parameter selection choice
 */
public class MockParameterSelectionChoice implements IParameterSelectionChoice {
	private String value;
	private String label;

	/**
	 * @param value
	 * @param label
	 */
	public MockParameterSelectionChoice(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Object getValue() {
		return value;
	}

}
