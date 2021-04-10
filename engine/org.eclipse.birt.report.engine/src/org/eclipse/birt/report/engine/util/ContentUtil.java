/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.w3c.dom.css.CSSValue;

public class ContentUtil {

	public static long getDesignID(IContent content) {
		if (content == null)
			return -1L;

		Object design = content.getGenerateBy();
		if (design instanceof ReportElementDesign) {
			return ((ReportElementDesign) design).getID();
		}
		return -1L;
	}

	/**
	 * to check whether there are horizontal page breaks in the table.
	 * 
	 * @param table
	 * @return
	 */
	public static boolean hasHorzPageBreak(ITableContent table) {
		int count = table.getColumnCount();
		for (int i = 0; i < count; i++) {
			IColumn column = table.getColumn(i);
			IStyle style = column.getStyle();
			CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
			if (i > 0 && IStyle.ALWAYS_VALUE == pageBreak) {
				return true;
			}
			pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_AFTER);
			if (i < count - 1 && IStyle.ALWAYS_VALUE == pageBreak) {
				return true;
			}
		}
		return false;
	}
}
