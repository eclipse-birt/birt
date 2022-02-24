/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.ICell;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;

public class Cell extends DesignElement implements ICell {

	public Cell(CellHandle handle) {
		super(handle);
	}

	/**
	 * Returns the cell's column span. This is the number of table or grid columns
	 * occupied by this cell.
	 * 
	 * @return the column span
	 */

	public int getColumnSpan() {
		return ((CellHandle) handle).getColumnSpan();
	}

	/**
	 * Returns the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 * 
	 * @return the row span
	 */

	public int getRowSpan() {
		return ((CellHandle) handle).getRowSpan();
	}

	/**
	 * Returns the cell's drop property. This is how the cell should expand to fill
	 * the entire table or group. This property is valid only for cells within a
	 * table; but not for cells within a grid.
	 * 
	 * @return the string value of the drop property
	 * @see #setDrop(String)
	 */

	public String getDrop() {
		return ((CellHandle) handle).getDrop();
	}

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

	public void setDrop(String drop) throws SemanticException {
		setProperty(ICellModel.DROP_PROP, drop);

	}

	/**
	 * Returns the cell's column property. The return value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 * 
	 * @return the column index, starting from 1.
	 */

	public int getColumn() {
		return ((CellHandle) handle).getColumn();
	}

	/**
	 * Sets the cell's column property. The input value gives the column in which
	 * the cell starts. Columns are numbered from 1.
	 * 
	 * @param column the column index, starting from 1.
	 * 
	 * @throws SemanticException if this property is locked.
	 */

	public void setColumn(int column) throws SemanticException {
		setProperty(ICellModel.COLUMN_PROP, Integer.valueOf(column));

	}

	/**
	 * Returns the cell's height.
	 * 
	 * @return the cell's height
	 */

	public String getHeight() {
		return ((CellHandle) handle).getHeight().getStringValue();
	}

	/**
	 * Returns the cell's width.
	 * 
	 * @return the cell's width
	 */

	public String getWidth() {
		return ((CellHandle) handle).getWidth().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getAntidiagonalNumber
	 * ()
	 */
	public int getAntidiagonalNumber() {

		return ((CellHandle) handle).getAntidiagonalNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IStyle#getAntidiagonalStyle()
	 */
	public String getAntidiagonalStyle() {

		return ((CellHandle) handle).getAntidiagonalStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getDiagonalNumber()
	 */
	public int getDiagonalNumber() {

		return ((CellHandle) handle).getDiagonalNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#getDiagonalStyle()
	 */
	public String getDiagonalStyle() {

		return ((CellHandle) handle).getDiagonalStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setAntidiagonalNumber
	 * (int)
	 */
	public void setAntidiagonalNumber(int antidiagonalNumber) throws SemanticException {
		setProperty(ICellModel.ANTIDIAGONAL_NUMBER_PROP, Integer.valueOf(antidiagonalNumber));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setAntidiagonalStyle
	 * (java.lang.String)
	 */
	public void setAntidiagonalStyle(String antidiagonalStyle) throws SemanticException {
		setProperty(ICellModel.ANTIDIAGONAL_STYLE_PROP, antidiagonalStyle);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IStyle#setDiagonalNumber(int)
	 */
	public void setDiagonalNumber(int diagonalNumber) throws SemanticException {
		setProperty(ICellModel.DIAGONAL_NUMBER_PROP, Integer.valueOf(diagonalNumber));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setDiagonalStyle(java
	 * .lang.String)
	 */
	public void setDiagonalStyle(String lineStyle) throws SemanticException {
		setProperty(ICellModel.DIAGONAL_STYLE_PROP, lineStyle);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IStyle#getAntidiagonalThickness
	 * ()
	 */
	public String getAntidiagonalThickness() {
		return ((CellHandle) handle).getAntidiagonalThickness().getStringValue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IStyle#getDiagonalThickness()
	 */
	public String getDiagonalThickness() {
		return ((CellHandle) handle).getDiagonalThickness().getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IStyle#setAntidiagonalThickness
	 * (java.lang.String)
	 */
	public void setAntidiagonalThickness(String thickness) throws SemanticException {
		setProperty(ICellModel.ANTIDIAGONAL_THICKNESS_PROP, thickness);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IStyle#setDiagonalThickness
	 * (java.lang.String)
	 */
	public void setDiagonalThickness(String thickness) throws SemanticException {
		setProperty(ICellModel.DIAGONAL_THICKNESS_PROP, thickness);

	}
}
