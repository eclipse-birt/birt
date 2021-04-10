/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.dom.CellComputedStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Expression;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 */
public class CellContent extends AbstractContent implements ICellContent {

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

	/**
	 * Flag indicading if this cell is the start of a group.
	 */
	protected Boolean displayGroupIcon;

	/**
	 * Flag identify if need repeat content in cell after page-break
	 */
	protected boolean repeatContent = false;

	/**
	 * The cell design, which generate this cell content.
	 */
	CellDesign cellDesign = null;

	private String headers;

	private String scope;

	private String drop;

	/**
	 * Does the cell has diagonal line or antidiagonal line.
	 */
	private Boolean hasDiagonalLine = null;
	/**
	 * The number of the diagonal line.
	 */
	private int diagonalNumber = -1;
	/**
	 * The style of the diagonal line.
	 */
	private String diagonalStyle = null;
	/**
	 * The width of the diagonal line.
	 */
	private DimensionType diagonalWidth = null;
	/**
	 * The color of the diagonal line.
	 */
	private String diagonalColor = null;
	/**
	 * The number of the antidiagonal line.
	 */
	private int antidiagonalNumber = -1;
	/**
	 * The style of the antidiagonal line.
	 */
	private String antidiagonalStyle = null;
	/**
	 * The width of the antidiagonal line.
	 */
	private DimensionType antidiagonalWidth = null;
	/**
	 * The color of the antidiagonal line.
	 */
	private String antidiagonalColor = null;

	public int getContentType() {
		return CELL_CONTENT;
	}

	/**
	 * constructor
	 * 
	 * @param item cell design item
	 */
	CellContent(IReportContent report) {
		super(report);
	}

	CellContent(ICellContent cell) {
		super(cell);
		this.colSpan = cell.getColSpan();
		this.rowSpan = cell.getRowSpan();
		this.column = cell.getColumn();
		this.displayGroupIcon = Boolean.valueOf(cell.getDisplayGroupIcon());
		this.columnInstance = cell.getColumnInstance();
		if (generateBy instanceof CellDesign) {
			cellDesign = (CellDesign) generateBy;
		}
	}

	/**
	 * @param generateBy The generateBy to set.
	 */
	public void setGenerateBy(Object generateBy) {
		super.setGenerateBy(generateBy);

		if (generateBy instanceof CellDesign) {
			cellDesign = (CellDesign) generateBy;
		}
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan() {
		if (rowSpan == -1 && cellDesign != null) {
			rowSpan = cellDesign.getRowSpan();
		}
		return rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan() {
		if (colSpan == -1 && cellDesign != null) {
			colSpan = cellDesign.getColSpan();
		}
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn() {
		if (column == -1 && cellDesign != null) {
			column = cellDesign.getColumn();
		}
		return column;
	}

	public int getRow() {
		if (parent != null && parent instanceof IRowContent) {
			return ((IRowContent) parent).getRowID();
		}
		return 0;
	}

	public void setDrop(String drop) {
		this.drop = drop;
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

	public IStyle getComputedStyle() {
		if (computedStyle == null) {
			if (inlineStyle == null || inlineStyle.isEmpty()) {
				String cacheKey = getStyleClass();
				ITableContent table = ((IRowContent) parent).getTable();
				int column = getColumn();
				if (column >= 0 && column < table.getColumnCount()) {
					IColumn tblColumn = table.getColumn(column);
					if (tblColumn != null) {
						String columnStyleClass = tblColumn.getStyleClass();
						if (columnStyleClass != null) {
							cacheKey = cacheKey + columnStyleClass;
						}
					}
				}

				ComputedStyle pcs = (ComputedStyle) ((IContent) parent).getComputedStyle();
				ComputedStyle cs = pcs.getCachedStyle(cacheKey);
				if (cs == null) {
					cs = new CellComputedStyle(this);
					pcs.addCachedStyle(cacheKey, cs);
				}
				computedStyle = cs;
			} else {
				computedStyle = new CellComputedStyle(this);
			}
		}
		return computedStyle;
	}

	static final protected short FIELD_ROW_SPAN = 100;
	static final protected short FIELD_COL_SPAN = 101;
	static final protected short FIELD_COLUMN = 102;
	static final protected short FIELD_START_OF_GROUP = 103;
	static final protected short FIELD_DISPLAY_GROUP_ICON = 104;
	static final protected short FIELD_DROP = 111;
	static final protected short FIELD_HEADERS = 112;
	static final protected short FIELD_SCOPE = 113;
	static final protected short FIELD_REPEAT_CONTENT = 114;
	static final protected short FIELD_DIAGONAL_NUMBER = 115;
	static final protected short FIELD_DIAGONAL_STYLE = 116;
	static final protected short FIELD_DIAGONAL_WIDTH = 117;
	static final protected short FIELD_DIAGONAL_COLOR = 118;
	static final protected short FIELD_ANTIDIAGONAL_NUMBER = 119;
	static final protected short FIELD_ANTIDIAGONAL_STYLE = 120;
	static final protected short FIELD_ANTIDIAGONAL_WIDTH = 121;
	static final protected short FIELD_ANTIDIAGONAL_COLOR = 122;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (rowSpan != -1) {
			IOUtil.writeShort(out, FIELD_ROW_SPAN);
			IOUtil.writeInt(out, rowSpan);
		}
		if (colSpan != -1) {
			IOUtil.writeShort(out, FIELD_COL_SPAN);
			IOUtil.writeInt(out, colSpan);
		}
		if (column != -1) {
			IOUtil.writeShort(out, FIELD_COLUMN);
			IOUtil.writeInt(out, column);
		}
		if (displayGroupIcon != null) {
			IOUtil.writeShort(out, FIELD_DISPLAY_GROUP_ICON);
			IOUtil.writeBool(out, displayGroupIcon.booleanValue());
		}
		if (drop != null) {
			IOUtil.writeShort(out, FIELD_DROP);
			IOUtil.writeString(out, drop);
		}
		if (headers != null) {
			IOUtil.writeShort(out, FIELD_HEADERS);
			IOUtil.writeString(out, headers);
		}
		if (scope != null) {
			IOUtil.writeShort(out, FIELD_SCOPE);
			IOUtil.writeString(out, scope);
		}
		if (repeatContent) {
			IOUtil.writeShort(out, FIELD_REPEAT_CONTENT);
			IOUtil.writeBool(out, true);
		}
		if (diagonalNumber > 0) {
			IOUtil.writeShort(out, FIELD_DIAGONAL_NUMBER);
			IOUtil.writeInt(out, diagonalNumber);
			if (diagonalStyle != null) {
				IOUtil.writeShort(out, FIELD_DIAGONAL_STYLE);
				IOUtil.writeString(out, diagonalStyle);
			}
			if (diagonalWidth != null) {
				IOUtil.writeShort(out, FIELD_DIAGONAL_WIDTH);
				diagonalWidth.writeObject(out);
			}
			if (diagonalColor != null) {
				IOUtil.writeShort(out, FIELD_DIAGONAL_COLOR);
				IOUtil.writeString(out, diagonalColor);
			}
		}
		if (antidiagonalNumber > 0) {
			IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_NUMBER);
			IOUtil.writeInt(out, antidiagonalNumber);
			if (antidiagonalStyle != null) {
				IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_STYLE);
				IOUtil.writeString(out, antidiagonalStyle);
			}
			if (antidiagonalWidth != null) {
				IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_WIDTH);
				antidiagonalWidth.writeObject(out);
			}
			if (antidiagonalColor != null) {
				IOUtil.writeShort(out, FIELD_ANTIDIAGONAL_COLOR);
				IOUtil.writeString(out, antidiagonalColor);
			}
		}
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_ROW_SPAN:
			rowSpan = IOUtil.readInt(in);
			break;
		case FIELD_COL_SPAN:
			colSpan = IOUtil.readInt(in);
			break;
		case FIELD_COLUMN:
			column = IOUtil.readInt(in);
			break;
		case FIELD_START_OF_GROUP:
			IOUtil.readBool(in);
			break;
		case FIELD_DISPLAY_GROUP_ICON:
			displayGroupIcon = Boolean.valueOf(IOUtil.readBool(in));
			break;
		case FIELD_DROP:
			drop = IOUtil.readString(in);
			break;
		case FIELD_HEADERS:
			headers = IOUtil.readString(in);
			break;
		case FIELD_SCOPE:
			scope = IOUtil.readString(in);
			break;
		case FIELD_REPEAT_CONTENT:
			repeatContent = IOUtil.readBool(in);
			break;
		case FIELD_DIAGONAL_NUMBER:
			diagonalNumber = IOUtil.readInt(in);
			break;
		case FIELD_DIAGONAL_STYLE:
			diagonalStyle = IOUtil.readString(in);
			break;
		case FIELD_DIAGONAL_WIDTH:
			diagonalWidth = new DimensionType();
			diagonalWidth.readObject(in);
			break;
		case FIELD_DIAGONAL_COLOR:
			diagonalColor = IOUtil.readString(in);
			break;
		case FIELD_ANTIDIAGONAL_NUMBER:
			antidiagonalNumber = IOUtil.readInt(in);
			break;
		case FIELD_ANTIDIAGONAL_STYLE:
			antidiagonalStyle = IOUtil.readString(in);
			break;
		case FIELD_ANTIDIAGONAL_WIDTH:
			antidiagonalWidth = new DimensionType();
			antidiagonalWidth.readObject(in);
			break;
		case FIELD_ANTIDIAGONAL_COLOR:
			antidiagonalColor = IOUtil.readString(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
			break;
		}
	}

	public boolean needSave() {
		if (rowSpan != -1 || colSpan != -1 || column != -1) {
			return true;
		}
		if (displayGroupIcon != null || headers != null || scope != null) {
			return true;
		}
		if (this.diagonalNumber > 0 || this.antidiagonalNumber > 0) {
			return true;
		}
		return super.needSave();
	}

	public boolean getDisplayGroupIcon() {
		if (displayGroupIcon == null) {
			if (cellDesign != null) {
				return cellDesign.getDisplayGroupIcon();
			}
			return false;
		}
		return displayGroupIcon.booleanValue();
	}

	public void setDisplayGroupIcon(boolean displayGroupIcon) {
		this.displayGroupIcon = Boolean.valueOf(displayGroupIcon);
	}

	private IColumn columnInstance;

	public IColumn getColumnInstance() {
		if (columnInstance != null) {
			return columnInstance;
		}
		if (parent instanceof IRowContent) {
			IRowContent row = (IRowContent) parent;
			ITableContent table = row.getTable();
			if (table != null) {
				int columnId = getColumn();
				if (columnId >= 0 && columnId < table.getColumnCount()) {
					columnInstance = table.getColumn(columnId);
				}
			}
		}
		return columnInstance;
	}

	protected IContent cloneContent() {
		return new CellContent(this);
	}

	public boolean hasDiagonalLine() {
		if (hasDiagonalLine != null) {
			return hasDiagonalLine.booleanValue();
		} else if (cellDesign != null) {
			return cellDesign.hasDiagonalLine();
		}
		return false;
	}

	public void setDiagonalNumber(int diagonalNumber) {
		this.diagonalNumber = diagonalNumber;
		if (getDiagonalNumber() > 0 || getAntidiagonalNumber() > 0) {
			hasDiagonalLine = Boolean.TRUE;
		} else {
			hasDiagonalLine = Boolean.FALSE;
		}
	}

	public int getDiagonalNumber() {
		if (diagonalNumber >= 0) {
			return diagonalNumber;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalNumber();
		}
		return 0;
	}

	public void setDiagonalStyle(String diagonalStyle) {
		this.diagonalStyle = diagonalStyle;
	}

	public String getDiagonalStyle() {
		if (diagonalStyle != null) {
			return diagonalStyle;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalStyle();
		}
		return null;
	}

	public void setDiagonalWidth(DimensionType diagonalWidth) {
		this.diagonalWidth = diagonalWidth;
	}

	public DimensionType getDiagonalWidth() {
		if (diagonalWidth != null) {
			return diagonalWidth;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalWidth();
		}
		return null;
	}

	public void setDiagonalColor(String diagonalColor) {
		this.diagonalColor = diagonalColor;
	}

	public String getDiagonalColor() {
		if (diagonalColor != null) {
			return diagonalColor;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalColor();
		}
		return null;
	}

	public void setAntidiagonalNumber(int antidiagonalNumber) {
		this.antidiagonalNumber = antidiagonalNumber;
		if (getDiagonalNumber() > 0 || getAntidiagonalNumber() > 0) {
			hasDiagonalLine = Boolean.TRUE;
		} else {
			hasDiagonalLine = Boolean.FALSE;
		}
	}

	public int getAntidiagonalNumber() {
		if (antidiagonalNumber >= 0) {
			return antidiagonalNumber;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalNumber();
		}
		return 0;
	}

	public void setAntidiagonalStyle(String antidiagonalStyle) {
		this.antidiagonalStyle = antidiagonalStyle;
	}

	public String getAntidiagonalStyle() {
		if (antidiagonalStyle != null) {
			return antidiagonalStyle;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalStyle();
		}
		return null;
	}

	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		this.antidiagonalWidth = antidiagonalWidth;
	}

	public DimensionType getAntidiagonalWidth() {
		if (antidiagonalWidth != null) {
			return antidiagonalWidth;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalWidth();
		}
		return null;
	}

	public void setAntidiagonalColor(String antidiagonalColor) {
		this.antidiagonalColor = antidiagonalColor;
	}

	public String getAntidiagonalColor() {
		if (antidiagonalColor != null) {
			return antidiagonalColor;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalColor();
		}
		return null;
	}

	public String getHeaders() {
		if (headers != null) {
			return headers;
		} else if (cellDesign != null) {
			Expression expr = cellDesign.getHeaders();
			if (expr != null && expr.getType() == Expression.CONSTANT) {
				return expr.getScriptText();
			}
		}
		return null;
	}

	public String getScope() {
		if (scope != null) {
			return scope;
		} else if (cellDesign != null) {
			return cellDesign.getScope();
		}
		return null;
	}

	public void setHeaders(String headers) {
		if (cellDesign != null) {
			Expression expr = cellDesign.getHeaders();
			if (expr != null && expr.getType() == Expression.CONSTANT) {
				if (headers.equals(expr.getScriptText())) {
					this.headers = null;
					return;
				}
			}
		}
		this.headers = headers;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public boolean repeatContent() {
		return repeatContent;
	}

	public void setRepeatContent(boolean repeatContent) {
		this.repeatContent = repeatContent;
	}

}