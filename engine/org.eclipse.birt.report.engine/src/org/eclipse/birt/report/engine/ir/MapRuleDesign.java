/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

/**
 * 
 */
public class MapRuleDesign extends RuleDesign {

	/**
	 * display text key
	 */
	protected String displayKey;
	/**
	 * display text
	 */
	protected String displayText;

	/**
	 * @param displayKey The displayKey to set.
	 */
	public void setDisplayText(String displayKey, String displayText) {
		this.displayKey = displayKey;
		this.displayText = displayText;
	}

	/**
	 * @return Returns the displayText.
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * @return Returns the displayKey.
	 */
	public String getDisplayKey() {
		return displayKey;
	}

}
