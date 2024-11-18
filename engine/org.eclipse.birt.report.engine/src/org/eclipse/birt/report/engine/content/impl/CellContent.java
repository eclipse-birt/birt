/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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
import org.eclipse.birt.report.engine.executor.ExecutionContext;
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
	 * Flag indicating if this cell is the start of a group.
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

	@Override
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
		this.displayGroupIcon = cell.getDisplayGroupIcon();
		this.columnInstance = cell.getColumnInstance();
		if (generateBy instanceof CellDesign) {
			cellDesign = (CellDesign) generateBy;
		}
	}

	/**
	 * @param generateBy The generateBy to set.
	 */
	@Override
	public void setGenerateBy(Object generateBy) {
		super.setGenerateBy(generateBy);

		if (generateBy instanceof CellDesign) {
			cellDesign = (CellDesign) generateBy;
		}
	}

	/**
	 * @return Returns the rowSpan.
	 */
	@Override
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
	@Override
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
	@Override
	public int getColumn() {
		if (column == -1 && cellDesign != null) {
			column = cellDesign.getColumn();
		}
		return column;
	}

	@Override
	public int getRow() {
		if (parent instanceof IRowContent) {
			return ((IRowContent) parent).getRowID();
		}
		return 0;
	}

	/**
	 * Set the drop property
	 *
	 * @param drop drop value
	 */
	public void setDrop(String drop) {
		this.drop = drop;
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

	@Override
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

	@Override
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
			displayGroupIcon = IOUtil.readBool(in);
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

	@Override
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

	@Override
	public boolean getDisplayGroupIcon() {
		if (displayGroupIcon == null) {
			if (cellDesign != null) {
				return cellDesign.getDisplayGroupIcon();
			}
			return false;
		}
		return displayGroupIcon.booleanValue();
	}

	@Override
	public void setDisplayGroupIcon(boolean displayGroupIcon) {
		this.displayGroupIcon = displayGroupIcon;
	}

	private IColumn columnInstance;

	@Override
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

	@Override
	protected IContent cloneContent() {
		return new CellContent(this);
	}

	@Override
	public boolean hasDiagonalLine() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getDiagonalNumber() > 0 && cellStyle.getDiagonalStyle() != null
				&& !"none".equals(cellStyle.getDiagonalStyle())
				|| cellStyle.getAntidiagonalNumber() > 0 && cellStyle.getAntidiagonalStyle() != null
						&& !"none".equals(cellStyle.getAntidiagonalStyle())) {
			hasDiagonalLine = Boolean.TRUE;

		} else if (getDiagonalNumber() > 0 && getDiagonalStyle() != null && !"none".equals(getDiagonalStyle())
				|| getAntidiagonalNumber() > 0 && getAntidiagonalStyle() != null
						&& !"none".equals(getAntidiagonalStyle())) {
			hasDiagonalLine = Boolean.TRUE;

		} else if (cellDesign != null) {
			hasDiagonalLine = cellDesign.hasDiagonalLine();

		}

		if (hasDiagonalLine != null) {
			return hasDiagonalLine.booleanValue();
		}
		return false;
	}

	@Override
	public void setDiagonalNumber(int diagonalNumber) {
		this.diagonalNumber = diagonalNumber;
	}

	@Override
	public int getDiagonalNumber() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getDiagonalNumber() >= 1) {
			return cellStyle.getDiagonalNumber();
		} else if (diagonalNumber > 1) {
			return diagonalNumber;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalNumber();
		}
		return 0;
	}

	@Override
	public void setDiagonalStyle(String diagonalStyle) {
		this.diagonalStyle = diagonalStyle;
	}

	@Override
	public String getDiagonalStyle() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getDiagonalStyle() != null && !"none".equals(cellStyle.getDiagonalStyle())) {
			return cellStyle.getDiagonalStyle();
		} else if (diagonalStyle != null) {
			return diagonalStyle;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalStyle();
		}
		return null;
	}

	@Override
	public void setDiagonalWidth(DimensionType diagonalWidth) {
		this.diagonalWidth = diagonalWidth;
	}

	@Override
	public DimensionType getDiagonalWidth() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getDiagonalWidth() != null && !"none".equals(cellStyle.getDiagonalStyle())) {
			return DimensionType.parserUnit(cellStyle.getDiagonalWidth());
		} else if (diagonalWidth != null) {
			return diagonalWidth;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalWidth();
		}
		return null;
	}

	@Override
	public void setDiagonalColor(String diagonalColor) {
		this.diagonalColor = diagonalColor;
	}

	@Override
	public String getDiagonalColor() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getDiagonalColor() != null && !"none".equals(cellStyle.getDiagonalStyle())) {
			return cellStyle.getDiagonalColor();
		} else if (diagonalColor != null) {
			return diagonalColor;
		} else if (cellDesign != null) {
			return cellDesign.getDiagonalColor();
		}
		return null;
	}

	@Override
	public void setAntidiagonalNumber(int antidiagonalNumber) {
		this.antidiagonalNumber = antidiagonalNumber;
	}

	@Override
	public int getAntidiagonalNumber() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getAntidiagonalNumber() >= 1) {
			return cellStyle.getAntidiagonalNumber();
		} else if (antidiagonalNumber >= 1) {
			return antidiagonalNumber;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalNumber();
		}
		return 0;
	}

	@Override
	public void setAntidiagonalStyle(String antidiagonalStyle) {
		this.antidiagonalStyle = antidiagonalStyle;
	}

	@Override
	public String getAntidiagonalStyle() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getAntidiagonalStyle() != null && !"none".equals(cellStyle.getAntidiagonalStyle())) {
			return cellStyle.getAntidiagonalStyle();
		} else if (antidiagonalStyle != null) {
			return antidiagonalStyle;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalStyle();
		}
		return null;
	}

	@Override
	public void setAntidiagonalWidth(DimensionType antidiagonalWidth) {
		this.antidiagonalWidth = antidiagonalWidth;
	}

	@Override
	public DimensionType getAntidiagonalWidth() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getAntidiagonalWidth() != null && !"none".equals(cellStyle.getAntidiagonalStyle())) {
			return DimensionType.parserUnit(cellStyle.getAntidiagonalWidth());
		} else if (antidiagonalWidth != null) {
			return antidiagonalWidth;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalWidth();
		}
		return null;
	}

	@Override
	public void setAntidiagonalColor(String antidiagonalColor) {
		this.antidiagonalColor = antidiagonalColor;
	}

	@Override
	public String getAntidiagonalColor() {
		IStyle cellStyle = this.getComputedStyle();
		if (cellStyle.getAntidiagonalColor() != null && !"none".equals(cellStyle.getAntidiagonalStyle())) {
			return cellStyle.getAntidiagonalColor();
		} else if (antidiagonalColor != null) {
			return antidiagonalColor;
		} else if (cellDesign != null) {
			return cellDesign.getAntidiagonalColor();
		}
		return null;
	}

	@Override
	public String getHeaders() {
		if (headers != null) {
			return headers;
		} else if (cellDesign != null) {
			Expression expr = cellDesign.getHeaders();
			if (expr != null) {
				ExecutionContext exeContext = ((ReportContent) getReportContent()).getExecutionContext();
				try {
					return (String) exeContext.evaluate(expr);
				} catch (BirtException be) {
					be.printStackTrace();
					exeContext.addException(be);
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String getScope() {
		if (scope != null) {
			return scope;
		} else if (cellDesign != null) {
			return cellDesign.getScope();
		}
		return null;
	}

	@Override
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

	@Override
	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public boolean repeatContent() {
		return repeatContent;
	}

	@Override
	public void setRepeatContent(boolean repeatContent) {
		this.repeatContent = repeatContent;
	}

}
