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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 */
public class CellContentWrapper extends AbstractContentWrapper implements ICellContent {

	protected ICellContent cell;
	/**
	 * row span
	 */
	protected int rowSpan = -1;

	/**
	 * col span, if equals to 1, then get it from the design.
	 */
	protected int colSpan = -1;

	/**
	 * column id, if equals to 0, get it from the design
	 */
	protected int column = -1;

	protected int row = -1;

	/**
	 * constructor
	 * 
	 * @param item cell design item
	 */
	public CellContentWrapper(ICellContent cell) {
		super(cell);
		this.cell = cell;
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan() {
		if (rowSpan != -1) {
			return rowSpan;
		}
		return cell.getRowSpan();
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan() {
		if (colSpan != -1) {
			return colSpan;
		}
		return cell.getColSpan();
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn() {
		if (column != -1) {
			return column;
		}
		return cell.getColumn();
	}

	public int getRow() {
		if (row != -1) {
			return row;
		}
		return cell.getRow();
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitCell(this, value);
	}

	/**
	 * @param rowSpan The rowSpan to set.
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean getDisplayGroupIcon() {
		return cell.getDisplayGroupIcon();
	}

	public void setDisplayGroupIcon(boolean isStartOfGroup) {
		cell.setDisplayGroupIcon(isStartOfGroup);
	}

	public IColumn getColumnInstance() {
		return cell.getColumnInstance();
	}

	public IContent cloneContent(boolean isDeep) {
		if (isDeep) {
			throw new UnsupportedOperationException();
		} else {
			return new CellContentWrapper(this);
		}
	}

	public boolean hasDiagonalLine() {
		return cell.hasDiagonalLine();
	}

	public void setDiagonalNumber(int diagonalNumber) {
		cell.setDiagonalNumber(diagonalNumber);
	}

	public int getDiagonalNumber() {
		return cell.getDiagonalNumber();
	}

	public void setDiagonalStyle(String diagonalStyle) {
		cell.setDiagonalStyle(diagonalStyle);
	}

	public String getDiagonalStyle() {
		return cell.getDiagonalStyle();
	}

	public void setDiagonalWidth(DimensionType diagonalWidth) {
		cell.setDiagonalWidth(diagonalWidth);
	}

	public DimensionType getDiagonalWidth() {
		return cell.getDiagonalWidth();
	}

	public void setDiagonalColor(String diagonalColor) {
		cell.setDiagonalColor(diagonalColor);
	}

	public String getDiagonalColor() {
		return cell.getDiagonalColor();
	}

	public void setAntidiagonalNumber(int antidiagonalNumber) {
		cell.setAntidiagonalNumber(antidiagonalNumber);
	}

	public int getAntidiagonalNumber() {
		return cell.getAntidiagonalNumber();
	}

	public void setAntidiagonalStyle(String antidiagonalStyle) {
		cell.setAntidiagonalStyle(antidiagonalStyle);
	}

	public String getAntidiagonalStyle() {
		return cell.getAntidiagonalStyle();
	}

	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		cell.setAntidiagonalWidth(antidiagonalWidth);
	}

	public DimensionType getAntidiagonalWidth() {
		return cell.getAntidiagonalWidth();
	}

	public void setAntidiagonalColor(String antidiagonalColor) {
		cell.setAntidiagonalColor(antidiagonalColor);
	}

	public String getAntidiagonalColor() {
		return cell.getAntidiagonalColor();
	}

	public String getHeaders() {
		return cell.getHeaders();
	}

	public String getScope() {
		return cell.getScope();
	}

	public void setHeaders(String headers) {
		cell.setHeaders(headers);
	}

	public String getDrop() {
		return cell.getScope();
	}

	public void setScope(String scope) {
		cell.setScope(scope);
	}

	public boolean repeatContent() {
		return cell.repeatContent();
	}

	public void setRepeatContent(boolean repeatContent) {
		cell.setRepeatContent(repeatContent);
	}

}
