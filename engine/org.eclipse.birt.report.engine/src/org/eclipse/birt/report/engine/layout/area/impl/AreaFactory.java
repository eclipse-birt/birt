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

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.IImageArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

/**
 *
 * area factory. create area object by content or area FIXME support
 * backgroud-image offset
 *
 */
public class AreaFactory {

	public static IImageArea createImageArea(IImageContent image) {
		return new ImageArea(image);
	}

	public static IArea createTableGroupArea(IContent group) {
		return new BlockContainerArea(group);
	}

	public static IArea createTemplateArea(IAutoTextContent autoText) {
		return new TemplateArea(autoText);
	}

	/**
	 * @deprecated
	 * @param content
	 * @param text
	 * @param fi
	 * @return
	 */
	@Deprecated
	public static IArea createTextArea(ITextContent content, String text, FontInfo fi) {
		return new TextArea(content, text, fi);
	}

	public static IArea createTextArea(ITextContent textContent, FontInfo fi, boolean blankLine) {
		return new TextArea(textContent, fi, blankLine);
	}

	public static IArea createTextArea(ITextContent textContent, int offset, int baseLevel, int runLevel,
			FontInfo fontInfo) {
		return new TextArea(textContent, offset, baseLevel, runLevel, fontInfo);
	}

	public static IArea createTextArea(ITextContent textContent, IStyle areaStyle, int offset, int baseLevel,
			int runLevel, FontInfo fontInfo) {
		return new TextArea(textContent, areaStyle, offset, baseLevel, runLevel, fontInfo);
	}

	/**
	 * create block container area by content
	 *
	 * @param content
	 * @return
	 */
	public static IContainerArea createBlockContainer(IContent content) {
		return new BlockContainerArea(content);
	}

	/**
	 * create lobic container area by content
	 *
	 * @param content
	 * @return
	 */
	public static IContainerArea createLogicContainer(IReportContent report) {
		return new LogicContainerArea(report);
	}

	/**
	 * create cell area by cell content
	 *
	 * @param cell
	 * @return
	 */
	public static CellArea createCellArea(ICellContent cell) {
		return new CellArea(cell);
	}

	/**
	 * create page area by page content
	 *
	 * @param pageContent
	 * @return
	 */
	public static IContainerArea createPageArea(IPageContent pageContent) {
		return new PageArea(pageContent);
	}

	/**
	 * create table area by table content
	 *
	 * @param table
	 * @return
	 */
	public static TableArea createTableArea(ITableContent table) {
		return new TableArea(table);
	}

	/**
	 * create row area by row content
	 *
	 * @param row
	 * @return
	 */
	public static RowArea createRowArea(IRowContent row) {
		return new RowArea(row);
	}

	public static LineArea createLineArea(IReportContent report) {
		return new LineArea(report);
	}

	public static IContainerArea createInlineContainer(IContent content) {
		return new InlineContainerArea(content);
	}

	/**
	 * create inline container area by content
	 *
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast  if this area is the last area of the content
	 * @return
	 */
	public static IContainerArea createInlineContainer(IContent content, boolean isFirst, boolean isLast) {
		IContainerArea containerArea = AreaFactory.createInlineContainer(content);
		IStyle style = containerArea.getStyle();
		// remove left padding, border and margin if it is not the first child
		if (!isFirst) {
			style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
		}
		// remove right padding, border and margin if it is not the last child
		if (!isLast) {
			style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
		}
		return containerArea;
	}

}
