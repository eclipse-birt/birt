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

package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class CellComputedStyle extends ComputedStyle {

	private CSSStylableElement cell;
	private IStyle columnStyle;
	private IStyle rowStyle;

	public CellComputedStyle(ICellContent elt) {
		super(elt);
		IRowContent row = (IRowContent) elt.getParent();
		if (row != null) {
			rowStyle = row.getStyle();
			ITableContent table = row.getTable();
			if (table != null) {
				int columnId = elt.getColumn();
				if (columnId >= 0 && columnId < table.getColumnCount()) {
					IColumn column = table.getColumn(columnId);
					columnStyle = column.getStyle();
				}
			}
		}
		cell = elt;
	}

	protected Value resolveProperty(int index) {
		CSSStylableElement parent = (CSSStylableElement) cell.getParent();
		IStyle pcs = null;
		if (parent != null) {
			pcs = parent.getComputedStyle();
		}
		// get the specified style
		IStyle s = cell.getStyle();

		Value sv = s == null ? null : (Value) s.getProperty(index);

		// none inheritable properties
		// background color: if the property defined in the row is empty, use column's
		// property.
		// vertical-align: if the row property is not null, use it, otherwise, use the
		// column.
		// other properties, use the property of column directly.
		if (sv == null && columnStyle != null) {
			if (engine.isInheritedProperty(index) == false) {
				if (isBackgroundProperties(index)) {
					Value rowValue = null;
					if (rowStyle != null) {
						rowValue = (Value) rowStyle.getProperty(index);
					}
					if (rowValue == null) {
						sv = (Value) columnStyle.getProperty(index);
					}
				} else if (index == STYLE_VERTICAL_ALIGN) {
					if (rowStyle != null) {
						sv = (Value) rowStyle.getProperty(index);
					}
					if (sv == null) {
						sv = (Value) columnStyle.getProperty(index);
					}
				} else {
					sv = (Value) columnStyle.getProperty(index);
				}
			} else {
				sv = (Value) rowStyle.getProperty(index);
				if (sv == null) {
					sv = (Value) columnStyle.getProperty(index);
				}
			}
		}

		Value cv = engine.resolveStyle(elt, index, sv, pcs);
		return cv;
	}

	private boolean isBackgroundProperties(int index) {
		if (StyleConstants.STYLE_BACKGROUND_COLOR == index || StyleConstants.STYLE_BACKGROUND_ATTACHMENT == index
				|| StyleConstants.STYLE_BACKGROUND_IMAGE == index || StyleConstants.STYLE_BACKGROUND_REPEAT == index) {
			return true;
		} else {
			return false;
		}
	}

}
