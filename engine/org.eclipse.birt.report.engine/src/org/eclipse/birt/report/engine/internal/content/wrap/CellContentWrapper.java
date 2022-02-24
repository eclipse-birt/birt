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
	@Override
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
	@Override
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
	@Override
	public int getColumn() {
		if (column != -1) {
			return column;
		}
		return cell.getColumn();
	}

	@Override
	public int getRow() {
		if (row != -1) {
			return row;
		}
		return cell.getRow();
	}

	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitCell(this, value);
	}

	/**
	 * @param rowSpan The rowSpan to set.
	 */
	@Override
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	@Override
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	@Override
	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public boolean getDisplayGroupIcon() {
		return cell.getDisplayGroupIcon();
	}

	@Override
	public void setDisplayGroupIcon(boolean isStartOfGroup) {
		cell.setDisplayGroupIcon(isStartOfGroup);
	}

	@Override
	public IColumn getColumnInstance() {
		return cell.getColumnInstance();
	}

	@Override
	public IContent cloneContent(boolean isDeep) {
		if (isDeep) {
			throw new UnsupportedOperationException();
		} else {
			return new CellContentWrapper(this);
		}
	}

	@Override
	public boolean hasDiagonalLine() {
		return cell.hasDiagonalLine();
	}

	@Override
	public void setDiagonalNumber(int diagonalNumber) {
		cell.setDiagonalNumber(diagonalNumber);
	}

	@Override
	public int getDiagonalNumber() {
		return cell.getDiagonalNumber();
	}

	@Override
	public void setDiagonalStyle(String diagonalStyle) {
		cell.setDiagonalStyle(diagonalStyle);
	}

	@Override
	public String getDiagonalStyle() {
		return cell.getDiagonalStyle();
	}

	@Override
	public void setDiagonalWidth(DimensionType diagonalWidth) {
		cell.setDiagonalWidth(diagonalWidth);
	}

	@Override
	public DimensionType getDiagonalWidth() {
		return cell.getDiagonalWidth();
	}

	@Override
	public void setDiagonalColor(String diagonalColor) {
		cell.setDiagonalColor(diagonalColor);
	}

	@Override
	public String getDiagonalColor() {
		return cell.getDiagonalColor();
	}

	@Override
	public void setAntidiagonalNumber(int antidiagonalNumber) {
		cell.setAntidiagonalNumber(antidiagonalNumber);
	}

	@Override
	public int getAntidiagonalNumber() {
		return cell.getAntidiagonalNumber();
	}

	@Override
	public void setAntidiagonalStyle(String antidiagonalStyle) {
		cell.setAntidiagonalStyle(antidiagonalStyle);
	}

	@Override
	public String getAntidiagonalStyle() {
		return cell.getAntidiagonalStyle();
	}

	@Override
	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		cell.setAntidiagonalWidth(antidiagonalWidth);
	}

	@Override
	public DimensionType getAntidiagonalWidth() {
		return cell.getAntidiagonalWidth();
	}

	@Override
	public void setAntidiagonalColor(String antidiagonalColor) {
		cell.setAntidiagonalColor(antidiagonalColor);
	}

	@Override
	public String getAntidiagonalColor() {
		return cell.getAntidiagonalColor();
	}

	@Override
	public String getHeaders() {
		return cell.getHeaders();
	}

	@Override
	public String getScope() {
		return cell.getScope();
	}

	@Override
	public void setHeaders(String headers) {
		cell.setHeaders(headers);
	}

	public String getDrop() {
		return cell.getScope();
	}

	@Override
	public void setScope(String scope) {
		cell.setScope(scope);
	}

	@Override
	public boolean repeatContent() {
		return cell.repeatContent();
	}

	@Override
	public void setRepeatContent(boolean repeatContent) {
		cell.setRepeatContent(repeatContent);
	}

}
