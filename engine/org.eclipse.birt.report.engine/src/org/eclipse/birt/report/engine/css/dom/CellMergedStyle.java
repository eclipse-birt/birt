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

package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.w3c.dom.css.CSSValue;

//TODO: move it to HTMLReportEmitter, it is a util used to output the
//column style in cells.
/**
 * Represents style of cell with the style of column.
 *
 * FireFox/IE handles the column style differntly, so we can only ouptut all the
 * column style to cell to get the unique display.
 *
 * it only returns the property which defined in the column style but should be
 * outputed in the cell.
 *
 */
public class CellMergedStyle extends AbstractStyle {
	IStyle cellStyle;
	IStyle rowStyle;
	IStyle columnStyle;

	/**
	 * Constructor.
	 *
	 * @param cell the cell.
	 */
	public CellMergedStyle(ICellContent cell) {
		super(cell.getCSSEngine());
		this.cellStyle = cell.getStyle();
		IElement parent = cell.getParent();
		if (parent instanceof IRowContent) {
			IRowContent row = (IRowContent) parent;
			rowStyle = row.getStyle();
			ITableContent table = row.getTable();
			if (table != null) {
				int columnId = cell.getColumn();
				if (columnId >= 0 && columnId < table.getColumnCount()) {
					IColumn column = table.getColumn(columnId);
					columnStyle = column.getStyle();
				}
			}
		}
	}

	/**
	 *
	 * <li>if the property is not defined in the column, return null.
	 *
	 * <li>the property has been defined in the cell style, return null.
	 *
	 * <li>property which has been defined in the column but not defined in the
	 * cell.
	 * <li>if it is not inheritable attributes
	 * <ul>
	 * <li>background: return NULL if it has been defined in row.
	 * <li>vertical-align: return NULL if has been defined in row.
	 * <li>otherwise: return the value defined in the column.
	 * </ul>
	 * <li>if it is inheritable attribute
	 * <ul>
	 * <li>if it is defined in the row, return NULL
	 * <li>otherwise, return the value defined in the column
	 * </ul>
	 * </ul>
	 */
	@Override
	public CSSValue getProperty(int index) {
		if ((cellStyle != null && cellStyle.getProperty(index) != null) || (columnStyle == null)) {
			return null;
		}

		CSSValue value = columnStyle.getProperty(index);
		if (value == null) {
			return null;
		}
		// value != null
		if (!engine.isInheritedProperty(index)) {
			if (isBackgroundProperties(index)) {
				if (rowStyle != null) {
					CSSValue rowValue = rowStyle.getProperty(index);
					if (rowValue != null) {
						return null;
					}
				}
			}
			if (index == STYLE_VERTICAL_ALIGN) {
				if (rowStyle != null) {
					CSSValue rowValue = rowStyle.getProperty(index);
					if (rowValue != null) {
						return null;
					}
				}
			}
		} else if (rowStyle != null) {
			CSSValue rowValue = rowStyle.getProperty(index);
			if (rowValue != null) {
				return null;
			}
		}

		return value;
	}

	@Override
	public boolean isEmpty() {
		if ((cellStyle != null && !cellStyle.isEmpty()) || (rowStyle != null && !rowStyle.isEmpty())) {
			return false;
		}
		if (columnStyle != null && !columnStyle.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public void setProperty(int index, CSSValue value) {
	}

	private boolean isBackgroundProperties(int index) {
		if (StyleConstants.STYLE_BACKGROUND_COLOR == index || StyleConstants.STYLE_BACKGROUND_ATTACHMENT == index
				|| StyleConstants.STYLE_BACKGROUND_IMAGE == index || StyleConstants.STYLE_BACKGROUND_REPEAT == index) {
			return true;
		}
		return false;
	}
}
