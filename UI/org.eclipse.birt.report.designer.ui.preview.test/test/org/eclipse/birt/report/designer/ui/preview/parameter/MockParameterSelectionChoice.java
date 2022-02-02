/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
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
