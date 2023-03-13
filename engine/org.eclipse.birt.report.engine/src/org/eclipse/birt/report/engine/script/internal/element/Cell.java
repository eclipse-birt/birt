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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.ICell;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

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

	@Override
	public int getColumnSpan() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getColumnSpan();
	}

	/**
	 * Returns the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 *
	 * @return the row span
	 */

	@Override
	public int getRowSpan() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getRowSpan();
	}

	/**
	 * Returns the cell's drop property. This is how the cell should expand to fill
	 * the entire table or group. This property is valid only for cells within a
	 * table; but not for cells within a grid.
	 *
	 * @return the string value of the drop property
	 * @see #setDrop(String)
	 */

	@Override
	public String getDrop() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getDrop();
	}

	/**
	 * Sets the cell's drop property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li>DROP_TYPE_NONE</li>
	 * <li>DROP_TYPE_DETAIL</li>
	 * <li>DROP_TYPE_ALL</li>
	 * </ul>
	 * <p>
	 * Note that This property is valid only for cells within a table; but not for
	 * cells within a grid.
	 *
	 * @param drop the string value of the drop property
	 * @throws ScriptException if the property is locked or the input value is not
	 *                         one of the above.
	 * @see #getDrop()
	 */

	@Override
	public void setDrop(String drop) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setDrop(drop);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the cell's column property. The return value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 *
	 * @return the column index, starting from 1.
	 */

	@Override
	public int getColumn() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getColumn();
	}

	/**
	 * Sets the cell's column property. The input value gives the column in which
	 * the cell starts. Columns are numbered from 1.
	 *
	 * @param column the column index, starting from 1.
	 * @throws ScriptException if this property is locked.
	 */

	@Override
	public void setColumn(int column) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setColumn(column);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the cell's height.
	 *
	 * @return the cell's height
	 */

	@Override
	public String getHeight() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getHeight();
	}

	/**
	 * Returns the cell's width.
	 *
	 * @return the cell's width
	 */

	@Override
	public String getWidth() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getWidth();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getAntidiagonalNumber()
	 */
	@Override
	public int getAntidiagonalNumber() {

		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getAntidiagonalNumber();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getAntidiagonalStyle()
	 */
	@Override
	public String getAntidiagonalStyle() {

		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getAntidiagonalStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getDiagonalNumber()
	 */
	@Override
	public int getDiagonalNumber() {

		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getDiagonalNumber();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getDiagonalStyle()
	 */
	@Override
	public String getDiagonalStyle() {

		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getDiagonalStyle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setAntidiagonalNumber(int)
	 */
	@Override
	public void setAntidiagonalNumber(int antidiagonalNumber) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl)
					.setAntidiagonalNumber(antidiagonalNumber);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setAntidiagonalStyle(java.lang.String)
	 */
	@Override
	public void setAntidiagonalStyle(String antidiagonalStyle) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl)
					.setAntidiagonalStyle(antidiagonalStyle);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setDiagonalNumber(int)
	 */
	@Override
	public void setDiagonalNumber(int diagonalNumber) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setDiagonalNumber(diagonalNumber);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setDiagonalStyle(java.lang.String)
	 */
	@Override
	public void setDiagonalStyle(String lineStyle) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setDiagonalStyle(lineStyle);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getAntidiagonalThickness()
	 */
	@Override
	public String getAntidiagonalThickness() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getAntidiagonalThickness();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * getDiagonalThickness()
	 */
	@Override
	public String getDiagonalThickness() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).getDiagonalThickness();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setAntidiagonalThickness(java.lang.String)
	 */
	@Override
	public void setAntidiagonalThickness(String thickness) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setAntidiagonalThickness(thickness);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign#
	 * setDiagonalThickness(java.lang.String)
	 */
	@Override
	public void setDiagonalThickness(String thickness) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ICell) designElementImpl).setDiagonalThickness(thickness);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
