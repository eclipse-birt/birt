/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * Cell area
 *
 * @since 3.3
 *
 */
public class CellArea extends ContainerArea {

	static Value DEFAULT_PADDING = new FloatValue(CSSPrimitiveValue.CSS_NUMBER, 1500);

	protected int rowSpan = 0;

	/**
	 * Constructor
	 */
	public CellArea() {
		super((IContent) null);
	}

	CellArea(ICellContent cell) {
		super(cell);
		// remove all border
		removeBorder();
		setDefaultPadding();
	}

	/**
	 * Get column id
	 *
	 * @return Return the column id
	 */
	public int getColumnID() {
		if (content != null) {
			return ((ICellContent) content).getColumn();
		}
		return 0;
	}

	/**
	 * Get row id
	 *
	 * @return Return the row id
	 */
	public int getRowID() {
		if (content != null) {
			return ((ICellContent) content).getRow();
		}
		return 0;
	}

	/**
	 * Get column span
	 *
	 * @return Return the column span value
	 */
	public int getColSpan() {
		if (content != null) {
			return ((ICellContent) content).getColSpan();
		}
		return 1;
	}

	/**
	 * Get row span
	 *
	 * @return Return the row span value
	 */
	public int getRowSpan() {
		if (rowSpan == 0 && content != null) {
			return ((ICellContent) content).getRowSpan();
		}
		return rowSpan;
	}

	/**
	 * Set row span
	 *
	 * @param rowSpan row span value
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	protected void setDefaultPadding() {

		if (content != null) {
			IStyle contentStyle = content.getStyle();
			CSSValue padding = contentStyle.getProperty(StyleConstants.STYLE_PADDING_TOP);
			if (padding == null) {
				style.setProperty(StyleConstants.STYLE_PADDING_TOP, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty(StyleConstants.STYLE_PADDING_BOTTOM);
			if (padding == null) {
				style.setProperty(StyleConstants.STYLE_PADDING_BOTTOM, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty(StyleConstants.STYLE_PADDING_LEFT);
			if (padding == null) {
				style.setProperty(StyleConstants.STYLE_PADDING_LEFT, DEFAULT_PADDING);
			}
			padding = contentStyle.getProperty(StyleConstants.STYLE_PADDING_RIGHT);
			if (padding == null) {
				style.setProperty(StyleConstants.STYLE_PADDING_RIGHT, DEFAULT_PADDING);
			}
		}
	}

}
