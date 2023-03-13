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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * Provides the interfaces for Cell Content
 *
 */
public interface ICellContent extends IContainerContent {

	/**
	 *
	 * @return the column span
	 */
	int getColSpan();

	/**
	 * @return Returns the rowSpan.
	 */
	int getRowSpan();

	/**
	 *
	 * @return the column number
	 */
	int getColumn();

	/**
	 * @return the column content.
	 */
	IColumn getColumnInstance();

	/**
	 *
	 * @return
	 */
	int getRow();

	void setColumn(int column);

	void setRowSpan(int rowSpan);

	void setColSpan(int colSpan);

	void setDisplayGroupIcon(boolean displayGroupIcon);

	boolean getDisplayGroupIcon();

	boolean hasDiagonalLine();

	int getDiagonalNumber();

	void setDiagonalNumber(int diagonalNumber);

	String getDiagonalStyle();

	void setDiagonalStyle(String diagonalStyle);

	DimensionType getDiagonalWidth();

	void setDiagonalWidth(DimensionType diagonalWidth);

	String getDiagonalColor();

	void setDiagonalColor(String diagonalColor);

	int getAntidiagonalNumber();

	void setAntidiagonalNumber(int antidiagonalNumber);

	String getAntidiagonalStyle();

	void setAntidiagonalStyle(String antidiagonalStyle);

	DimensionType getAntidiagonalWidth();

	void setAntidiagonalWidth(DimensionType antidiagonalWidth);

	String getAntidiagonalColor();

	void setAntidiagonalColor(String antidiagonalColor);

	String getHeaders();

	void setHeaders(String headers);

	String getScope();

	void setScope(String scope);

	boolean repeatContent();

	void setRepeatContent(boolean repeatContent);

}
