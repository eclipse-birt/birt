/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

/**
 * The strategy to recover the table from an invalid layout to a valid one. It
 * may includes solve problems in the layout such as the overlapped areas, the
 * empty areas.
 * 
 */

public class BasicLayoutStrategies {

	/**
	 * Applies different strategies to the layout table and table element with the
	 * given options.
	 * 
	 * @param layoutTable     the layout table to apply strategies
	 * @param fillsEmptyCells <code>true</code> if cell elements are filled in empty
	 *                        areas. Otherwise <code>false</code>.
	 */

	public static void appliesStrategies(LayoutTable layoutTable, boolean fillsEmptyCells) {
		new FillCellsStrategy(layoutTable, fillsEmptyCells).applyStrategy();
		new DropStrategy(layoutTable).applyStrategy();
	}
}
