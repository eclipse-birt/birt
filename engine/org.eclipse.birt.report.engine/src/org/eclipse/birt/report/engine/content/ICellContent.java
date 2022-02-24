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
	public int getColSpan();

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan();

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn();

	/**
	 * @return the column content.
	 */
	public IColumn getColumnInstance();

	/**
	 * 
	 * @return
	 */
	public int getRow();

	public void setColumn(int column);

	public void setRowSpan(int rowSpan);

	public void setColSpan(int colSpan);

	public void setDisplayGroupIcon(boolean displayGroupIcon);

	public boolean getDisplayGroupIcon();

	public boolean hasDiagonalLine();

	public int getDiagonalNumber();

	public void setDiagonalNumber(int diagonalNumber);

	public String getDiagonalStyle();

	public void setDiagonalStyle(String diagonalStyle);

	public DimensionType getDiagonalWidth();

	public void setDiagonalWidth(DimensionType diagonalWidth);

	public String getDiagonalColor();

	public void setDiagonalColor(String diagonalColor);

	public int getAntidiagonalNumber();

	public void setAntidiagonalNumber(int antidiagonalNumber);

	public String getAntidiagonalStyle();

	public void setAntidiagonalStyle(String antidiagonalStyle);

	public DimensionType getAntidiagonalWidth();

	public void setAntidiagonalWidth(DimensionType antidiagonalWidth);

	public String getAntidiagonalColor();

	public void setAntidiagonalColor(String antidiagonalColor);

	public String getHeaders();

	public void setHeaders(String headers);

	public String getScope();

	public void setScope(String scope);

	public boolean repeatContent();

	public void setRepeatContent(boolean repeatContent);

}
