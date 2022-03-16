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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a Cell in the scripting environment
 *
 */
public interface ICell extends IDesignElement {

	/**
	 * Returns the cell's column span. This is the number of table or grid columns
	 * occupied by this cell.
	 *
	 * @return the column span
	 */
	int getColumnSpan();

	/**
	 * Returns the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 *
	 * @return the row span
	 */
	int getRowSpan();

	/**
	 * Returns the cell's drop property. This is how the cell should expand to fill
	 * the entire table or group. This property is valid only for cells within a
	 * table; but not for cells within a grid.
	 *
	 * @return the string value of the drop property
	 * @see #setDrop(String)
	 */
	String getDrop();

	/**
	 * Sets the cell's drop property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li>DROP_TYPE_NONE</li>
	 * <li>DROP_TYPE_DETAIL</li>
	 * <li>DROP_TYPE_ALL</li>
	 * </ul>
	 *
	 * <p>
	 *
	 * Note that This property is valid only for cells within a table; but not for
	 * cells within a grid.
	 *
	 * @param drop the string value of the drop property
	 *
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 *
	 * @see #getDrop()
	 */
	void setDrop(String drop) throws SemanticException;

	/**
	 * Returns the cell's column property. The return value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 *
	 * @return the column index, starting from 1.
	 */
	int getColumn();

	/**
	 * Sets the cell's column property. The input value gives the column in which
	 * the cell starts. Columns are numbered from 1.
	 *
	 * @param column the column index, starting from 1.
	 *
	 * @throws SemanticException if this property is locked.
	 */
	void setColumn(int column) throws SemanticException;

	/**
	 * Returns the cell's height.
	 *
	 * @return the cell's height
	 */
	String getHeight();

	/**
	 * Returns the cell's width.
	 *
	 * @return the cell's width
	 */
	String getWidth();

	/**
	 * Sets the number of the diagonal lines that are from top-left to bottom-right
	 * corner.
	 *
	 * @param diagonalNumber the diagonal number.
	 * @throws SemanticException
	 */
	void setDiagonalNumber(int diagonalNumber) throws SemanticException;

	/**
	 * Gets the number of the diagonal lines that are from top-left to bottom-right
	 * corner.
	 *
	 * @return the diagonal number.
	 */
	int getDiagonalNumber();

	/**
	 * Sets the style of the diagonal line that is from top-left to bottom-right
	 * corner. The input value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 *
	 * @param lineStyle the line style.
	 * @throws SemanticException if the input value is not one of the above.
	 */
	void setDiagonalStyle(String lineStyle) throws SemanticException;

	/**
	 * Returns the style of the diagonal line that is from top-left to bottom-right
	 * corner. The return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 *
	 * @return the line style in string.
	 */
	String getDiagonalStyle();

	/**
	 * Sets the number of the anti-diagonal lines that are from the top-right to
	 * bottom-left.
	 *
	 * @param antidiagonalNumber the anti-diagonal number
	 * @throws SemanticException
	 */
	void setAntidiagonalNumber(int antidiagonalNumber) throws SemanticException;

	/**
	 * Gets the number of the anti-diagonal lines that are from the top-right to
	 * bottom-left.
	 *
	 * @return the anti-diagonal number.
	 */
	int getAntidiagonalNumber();

	/**
	 * Returns the style of the anti-diagonal lines that are from the top-right to
	 * bottom-left. The return value is one of constants defined in
	 * <code>DesignChoiceConstants</code>:
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 *
	 * @return the line style in string.
	 */
	String getAntidiagonalStyle();

	/**
	 * Sets the style of the anti-diagonal lines that are from the top-right to
	 * bottom-left. The input value is one of constants defined in <code>
	 * DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>LINE_STYLE_NONE</code>
	 * <li><code>LINE_STYLE_SOLID</code>
	 * <li><code>LINE_STYLE_DOTTED</code>
	 * <li><code>LINE_STYLE_DASHED</code>
	 * <li><code>LINE_STYLE_DOUBLE</code>
	 * <li><code>LINE_STYLE_GROOVE</code>
	 * <li><code>LINE_STYLE_RIDGE</code>
	 * <li><code>LINE_STYLE_INSET</code>
	 * <li><code>LINE_STYLE_OUTSET</code>
	 * </ul>
	 *
	 * @param antidiagonalStyle the new line style.
	 * @throws SemanticException if the input value is not one of the above.
	 */
	void setAntidiagonalStyle(String antidiagonalStyle) throws SemanticException;

	/**
	 * Gets the the thickness of the diagonal that are from top-left to bottom-right
	 * corner using a dimension string. Besides the dimension value,the value can be
	 * one of constants defined in <code>
	 * DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 *
	 * @return the thickness of the line.
	 */
	String getDiagonalThickness();

	/**
	 * Sets the the thickness of the diagonal that is from top-left to bottom-right
	 * corner using a dimension string with optional unit suffix such as "10" or
	 * "10pt". If no suffix is provided, then the units are assumed to be in the
	 * design's default units. Call this method to set a string typed in by the
	 * user. Besides the dimension value,the value maybe one of constants defined in
	 * <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 *
	 * @param thickness the thickness of the line.
	 * @throws SemanticException
	 */
	void setDiagonalThickness(String thickness) throws SemanticException;

	/**
	 * Gets the thickness of the anti-diagonal line that is from top-right to
	 * bottom-left corner using a dimension string. Besides the dimension value,the
	 * value can be one of constants defined in <code>
	 * DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 *
	 * @return the thickness of the line.
	 */
	String getAntidiagonalThickness();

	/**
	 * Sets the the thickness of the anti-diagonal that is from top-right to
	 * bottom-left corner using a dimension string with optional unit suffix such as
	 * "10" or "10pt". If no suffix is provided, then the units are assumed to be in
	 * the design's default units. Call this method to set a string typed in by the
	 * user. Besides the dimension value,the value maybe one of constants defined in
	 * <code>DesignChoiceConstatns</code>:
	 * <ul>
	 * <li><code>LINE_WIDTH_THIN</code>
	 * <li><code>LINE_WIDTH_MEDIUM</code>
	 * <li><code>LINE_WIDTH_THICK</code>
	 * </ul>
	 *
	 * @param thickness the thickness of the line.
	 * @throws SemanticException
	 */
	void setAntidiagonalThickness(String thickness) throws SemanticException;

}
