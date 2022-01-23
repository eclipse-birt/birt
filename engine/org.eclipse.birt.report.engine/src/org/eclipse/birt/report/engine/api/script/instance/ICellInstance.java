/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

public interface ICellInstance extends IReportElementInstance {

	/**
	 * Get the column span
	 * 
	 * @return the column span
	 */
	int getColSpan();

	/**
	 * Set the column span
	 * 
	 * @param colSpan, the column span
	 */
	void setColSpan(int colSpan);

	/**
	 * @return Returns the rowSpan.
	 */
	int getRowSpan();

	/**
	 * Set the rowspan
	 * 
	 * @param rowSpan, the row span
	 */
	void setRowSpan(int rowSpan);

	/**
	 * Get the column number
	 * 
	 * @return the column number
	 */
	int getColumn();

	/**
	 * Get the evaluated expression for this cell
	 * 
	 * @throws ScriptException
	 */
	/*
	 * Object getData( ) throws ScriptException;
	 */

}
