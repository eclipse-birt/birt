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

package org.eclipse.birt.report.designer.ui.dialogs;

/**
 * The class used for operators in the expression builder
 */

public class Operator {

	/**
	 * The tooltip of the operator
	 */
	public String tooltip;
	/**
	 * The symbol of the operator
	 */
	public String symbol;

	/**
	 * The text to insert into the source viewer
	 */
	public String insertString;

	/**
	 * Construct a new operator used in the expression builder with the given symbol
	 * and tooltip.
	 * 
	 * @param symbol  the symbol of the operator.
	 * @param tooltip the tooltip for the operator
	 * 
	 */
	public Operator(String symbol, String tooltip) {
		this(symbol, null, tooltip);
	}

	/**
	 * Construct a new operator used in the expression builder with the given symbol
	 * ,insert string and tooltip.
	 * 
	 * @param symbol       the symbol of the operator
	 * @param insertString the string to insert into the source viewer for the
	 *                     operator
	 * @param tooltip      the tooltip for the operator
	 * 
	 */

	public Operator(String symbol, String insertString, String tooltip) {
		assert symbol != null;
		this.symbol = symbol;
		if (insertString == null) {
			this.insertString = symbol;
		} else {
			this.insertString = insertString;
		}
		this.tooltip = tooltip;
	}
}