/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.style.StyleManager;

@SuppressWarnings("nls")
public class MasterPageWriter extends org.eclipse.birt.report.engine.odf.writer.MasterPageWriter {

	private StyleManager styleManager;

	public MasterPageWriter(OutputStream out, StyleManager styleManager) {
		super(out);
		this.styleManager = styleManager;
	}

	protected void writeTableCell(ICellContent cell, StyleEntry rowStyle) {
		Collection list = cell.getChildren();

		Iterator iter = list.iterator();

		if (iter.hasNext()) {
			StyleEntry cellStyle = StyleBuilder.createStyleEntry(cell.getComputedStyle(),
					StyleConstant.TYPE_TABLE_CELL);
			if (cellStyle != null && rowStyle != null) {
				StyleBuilder.mergeInheritableProp(cellStyle, rowStyle);
			}

			StyleEntry textStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TEXT);
			if (cellStyle != null) {
				StyleBuilder.mergeInheritableProp(textStyle, cellStyle);
			}

			writer.openTag("text:p");
			while (iter.hasNext()) {
				writeTextContent(iter.next(), textStyle);
			}
			writer.closeTag("text:p");
		}
	}

	protected void writeTableRow(IRowContent row, StyleEntry tableStyle) {
		StyleEntry rowStyle = StyleBuilder.createStyleEntry(row.getComputedStyle(), StyleConstant.TYPE_TABLE_ROW);
		if (rowStyle != null && tableStyle != null) {
			StyleBuilder.mergeInheritableProp(tableStyle, rowStyle);
		}

		Collection list = row.getChildren();
		Iterator iter = list.iterator();
		int currentCellCount = 0;
		while (iter.hasNext()) {
			currentCellCount++;
			ICellContent child = (ICellContent) iter.next();

			String tag = "";
			switch (currentCellCount) {
			case 1:
				tag = "style:region-left"; //$NON-NLS-1$
				break;
			case 2:
				tag = "style:region-center"; //$NON-NLS-1$
				break;
			case 3:
				tag = "style:region-right"; //$NON-NLS-1$
				break;
			default:
				break;
			}

			writer.openTag(tag);
			writeTableCell(child, rowStyle);
			writer.closeTag(tag);
		}
	}

	protected void writeTable(ITableContent table) {
		Collection list = table.getChildren();
		Iterator iter = list.iterator();

		if (iter.hasNext()) {
			StyleEntry tableStyle = StyleBuilder.createStyleEntry(table.getComputedStyle(), StyleConstant.TYPE_TABLE);
			while (iter.hasNext()) {
				Object child = iter.next();
				writeTableRow((IRowContent) child, tableStyle);
			}
		}
	}

	protected void writeTextContent(Object child, StyleEntry blockStyle) {
		StyleEntry entry = StyleBuilder.createStyleEntry(((IContent) child).getComputedStyle(),
				StyleConstant.TYPE_TEXT);
		if (blockStyle != null) {
			if (entry == null) {
				entry = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TEXT);
			}
			StyleBuilder.mergeInheritableProp(blockStyle, entry);
		}

		writer.openTag("text:span");
		if (entry != null) {
			styleManager.addStyle(entry);
			writer.attribute("text:style-name", entry.getName());
		}

		if (child instanceof IAutoTextContent) {
			writeAutoText(((IAutoTextContent) child).getType());
		} else if (child instanceof ITextContent) {
			writeString(((ITextContent) child).getText());
		} else if (child instanceof IForeignContent) {
			writeString(((IForeignContent) child).getRawValue().toString());
		}
		writer.closeTag("text:span");
	}

	public void writeHeaderFooter(IContent headerFooter) {
		if (headerFooter != null) {
			Collection list = headerFooter.getChildren();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Object child = iter.next();
				if (child instanceof ITableContent) {
					writeTable((ITableContent) child);
				} else {
					writer.openTag("text:p");
					StyleEntry blockEntry = StyleBuilder.createStyleEntry(headerFooter.getComputedStyle(),
							StyleConstant.TYPE_TEXT);
					writeTextContent(child, blockEntry);
					writer.closeTag("text:p");
				}
			}
		}
	}
}
