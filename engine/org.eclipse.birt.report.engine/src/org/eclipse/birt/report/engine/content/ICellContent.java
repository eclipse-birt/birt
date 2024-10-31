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
	 * Get the column span
	 *
	 * @return the column span
	 */
	int getColSpan();

	/**
	 * Get the row span
	 *
	 * @return the row span
	 */
	int getRowSpan();

	/**
	 * Get the column number
	 *
	 * @return the column number
	 */
	int getColumn();

	/**
	 * Get the column content
	 *
	 * @return the column content
	 */
	IColumn getColumnInstance();

	/**
	 * Get the row
	 *
	 * @return the row
	 */
	int getRow();

	/**
	 * Set the column
	 *
	 * @param column
	 */
	void setColumn(int column);

	/**
	 * Set the row span
	 *
	 * @param rowSpan row span
	 */
	void setRowSpan(int rowSpan);

	/**
	 * Set the column span
	 *
	 * @param colSpan column span
	 */
	void setColSpan(int colSpan);

	/**
	 * Set display group icon
	 *
	 * @param displayGroupIcon display the group icon
	 */
	void setDisplayGroupIcon(boolean displayGroupIcon);

	/**
	 * Get the display group icon
	 *
	 * @return the display group icon
	 */
	boolean getDisplayGroupIcon();

	/**
	 * Has diagonal line
	 *
	 * @return check of diagonal line
	 */
	boolean hasDiagonalLine();

	/**
	 * Get diagonal number
	 *
	 * @return the diagonal number
	 */
	int getDiagonalNumber();

	/**
	 * Set diagonal number
	 *
	 * @param diagonalNumber diagonal number
	 */
	void setDiagonalNumber(int diagonalNumber);

	/**
	 * Get diagonal style
	 *
	 * @return the diagonal style
	 */
	String getDiagonalStyle();

	/**
	 * Set diagonal style
	 *
	 * @param diagonalStyle diagonal style
	 */
	void setDiagonalStyle(String diagonalStyle);

	/**
	 * Get the diagonal width
	 *
	 * @return the diagonal width
	 */
	DimensionType getDiagonalWidth();

	/**
	 * Set the diagonal width
	 *
	 * @param diagonalWidth diagonal width
	 */
	void setDiagonalWidth(DimensionType diagonalWidth);

	/**
	 * Get the diagonal color
	 *
	 * @return the diagonal color
	 */
	String getDiagonalColor();

	/**
	 * Set the diagonal color
	 *
	 * @param diagonalColor diagonal color
	 */
	void setDiagonalColor(String diagonalColor);

	/**
	 * Get the anti-diagonal number
	 *
	 * @return the anti-diagonal number
	 */
	int getAntidiagonalNumber();

	/**
	 * Set the anti-diagonal number
	 *
	 * @param antidiagonalNumber anti-diagonal number
	 */
	void setAntidiagonalNumber(int antidiagonalNumber);

	/**
	 * Get the anti-diagonal style
	 *
	 * @return the anti-diagonal style
	 */
	String getAntidiagonalStyle();

	/**
	 * Set the anti-diagonal style
	 *
	 * @param antidiagonalStyle anti-diagonal style
	 */
	void setAntidiagonalStyle(String antidiagonalStyle);

	/**
	 * Get the anti-diagonal width
	 *
	 * @return the anti-diagonal width
	 */
	DimensionType getAntidiagonalWidth();

	/**
	 * Set the anti-diagonal width
	 *
	 * @param antidiagonalWidth anti-diagonal width
	 */
	void setAntidiagonalWidth(DimensionType antidiagonalWidth);

	/**
	 * Get the anti-diagonal color
	 *
	 * @return the anti-diagonal color
	 */
	String getAntidiagonalColor();

	/**
	 * Set the anti-diagonal color
	 *
	 * @param antidiagonalColor anti-diagonal color
	 */
	void setAntidiagonalColor(String antidiagonalColor);

	/**
	 * Get the headers
	 *
	 * @return the headers
	 */
	String getHeaders();

	/**
	 * Set the headers
	 *
	 * @param headers
	 */
	void setHeaders(String headers);

	/**
	 * Get the scope
	 *
	 * @return the scope
	 */
	String getScope();

	/**
	 * Set the scope
	 *
	 * @param scope scope
	 */
	void setScope(String scope);

	/**
	 * Check if the content is repeatable
	 *
	 * @return the check result if content repeatable
	 */
	boolean repeatContent();

	/**
	 * Set the repeat content
	 *
	 * @param repeatContent repeat content
	 */
	void setRepeatContent(boolean repeatContent);

	/**
	 * @return Returns the tagType.
	 */
	public String getTagType();

	/**
	 * @param tagType The tagType to set.
	 */
	public void setTagType(String tagType);

}
