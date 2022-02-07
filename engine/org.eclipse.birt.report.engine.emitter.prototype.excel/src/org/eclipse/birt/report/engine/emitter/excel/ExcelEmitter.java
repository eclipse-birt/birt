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

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.excel.layout.ColumnsInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.LayoutUtil;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

import com.ibm.icu.util.TimeZone;

public class ExcelEmitter extends ContentEmitterAdapter {

	protected static Logger logger = Logger.getLogger(ExcelEmitter.class.getName());

	protected IEmitterServices service = null;

	protected ExcelLayoutEngine engine;

	protected ExcelContext context;

	public String getOutputFormat() {
		return "xls";
	}

	public void initialize(IEmitterServices service) throws EngineException {
		this.context = createContext();
		this.service = service;
		context.initialize(service);
	}

	protected ExcelContext createContext() {
		return new ExcelContext();
	}

	public void start(IReportContent report) {
		context.setReport(report);
		IStyle style = report.getRoot().getComputedStyle();
		engine = createLayoutEngine(context, new ContentEmitterVisitor(this));
		engine.initalize(style);
	}

	protected ExcelLayoutEngine createLayoutEngine(ExcelContext context, ContentEmitterVisitor contentVisitor) {
		return new ExcelLayoutEngine(context, contentVisitor);
	}

	public void startPage(IPageContent page) throws BirtException {
		engine.startPage(page);
	}

	public void endPage(IPageContent page) throws BirtException {
		engine.endPage(page);
	}

	public void startTable(ITableContent table) {
		engine.startTable(table);
	}

	public void startRow(IRowContent row) {
		engine.addRow(row.getComputedStyle(), row.getBookmark());
	}

	public void endRow(IRowContent row) {
		DimensionType height = row.getHeight();
		float rowHeight = ExcelUtil.convertDimensionType(height, 0, context.getDpi()) / 1000f;
		engine.endRow(rowHeight);
	}

	public void startCell(ICellContent cell) {
		IStyle style = cell.getComputedStyle();
		engine.addCell(cell, cell.getColumn(), cell.getColSpan(), cell.getRowSpan(), style);
	}

	public void endCell(ICellContent cell) {
		engine.endCell(cell);
	}

	public void endTable(ITableContent table) {
		engine.endTable(table);
	}

	public void startList(IListContent list) {
		ContainerSizeInfo size = engine.getCurrentContainer().getSizeInfo();
		ColumnsInfo table = LayoutUtil.createTable(list, size.getWidth(), context.getDpi());
		engine.addTable(list, table, size);

		if (list.getChildren() == null) {
			HyperlinkDef link = parseHyperLink(list);
			float height = getContentHeight(list);
			engine.addData(null, list.getComputedStyle(), link, list.getBookmark(), height);
		}
	}

	public void startListBand(IListBandContent listBand) {
		engine.addCell(0, 1, 1, listBand.getComputedStyle());
	}

	public void endListBand(IListBandContent listBand) {
		engine.endListBandContainer();
	}

	public void endList(IListContent list) {
		engine.endTable(list);
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			HTML2Content.html2Content(foreign);
			HyperlinkDef link = parseHyperLink(foreign);
			engine.processForeign(foreign, link);
		}
	}

	public void startText(ITextContent text) {
		HyperlinkDef url = parseHyperLink(text);
		float height = getContentHeight(text);
		engine.addData(text.getText(), text.getComputedStyle(), url, text.getBookmark(), height);
	}

	public void startData(IDataContent data) {
		addDataContent(data);
	}

	protected Data addDataContent(IDataContent data) {
		float height = getContentHeight(data);
		HyperlinkDef url = parseHyperLink(data);
		String bookmark = data.getBookmark();
		Data excelData = null;
		Object generateBy = data.getGenerateBy();
		IStyle style = data.getComputedStyle();
		DataFormatValue dataformat = style.getDataFormat();
		MapDesign map = null;
		if (generateBy instanceof DataItemDesign) {
			DataItemDesign design = (DataItemDesign) generateBy;
			map = design.getMap();
		}
		if (map != null && map.getRuleCount() > 0 && data.getLabelText() != null) {
			excelData = engine.addData(data.getText(), style, url, bookmark, height);
		} else {
			String locale = null;
			int type = ExcelUtil.getType(data.getValue());
			if (type == SheetData.STRING) {
				if (dataformat != null) {
					locale = dataformat.getStringLocale();
				}
				String text = data.getValue() == null ? null : data.getText();
				excelData = engine.addData(text, style, url, bookmark, locale, height);
			} else if (type == Data.NUMBER) {
				if (dataformat != null) {
					locale = dataformat.getNumberLocale();
				}
				excelData = engine.addData(data.getValue(), style, url, bookmark, locale, height);
			} else {
				if (dataformat != null) {
					locale = dataformat.getDateTimeLocale();
				}
				excelData = engine.addDateTime(data, style, url, bookmark, locale, height);
			}
		}
		return excelData;
	}

	private float getContentHeight(IContent content) {
		return ExcelUtil.convertDimensionType(content.getHeight(), 0, context.getDpi()) / 1000f;
	}

	public void startImage(IImageContent image) {
		if (context.isIgnoreImage()) {
			return;
		}
		IStyle style = image.getComputedStyle();
		HyperlinkDef url = parseHyperLink(image);
		engine.addImageData(image, style, url, image.getBookmark());
	}

	public void startLabel(ILabelContent label) {
		Object design = label.getGenerateBy();
		IContent container = label;

		while (design == null) {
			container = (IContent) container.getParent();
			design = ((IContent) container).getGenerateBy();
		}

		HyperlinkDef url = parseHyperLink(label);

		// If the text is BR and it generated by foreign,
		// ignore it
		if (!("\n".equalsIgnoreCase(label.getText()) && container instanceof IForeignContent)) {
			float height = getContentHeight(label);
			engine.addData(label.getText(), label.getComputedStyle(), url, label.getBookmark(), height);
		}
	}

	public void startAutoText(IAutoTextContent autoText) {
		HyperlinkDef link = parseHyperLink(autoText);
		float height = getContentHeight(autoText);
		engine.addData(autoText.getText(), autoText.getComputedStyle(), link, autoText.getBookmark(), height);
	}

	public void end(IReportContent report) {
		engine.end(report);
		engine.endWriter();
	}

	public HyperlinkDef parseHyperLink(IContent content) {
		HyperlinkDef hyperlink = null;
		IHyperlinkAction linkAction = content.getHyperlinkAction();

		if (linkAction != null) {
			String tooltip = linkAction.getTooltip();
			String bookmark = linkAction.getBookmark();
			IReportRunnable reportRunnable = service.getReportRunnable();
			IReportContext reportContext = service.getReportContext();
			IHTMLActionHandler actionHandler = (IHTMLActionHandler) service.getOption(RenderOption.ACTION_HANDLER);
			switch (linkAction.getType()) {
			case IHyperlinkAction.ACTION_BOOKMARK:
				hyperlink = new HyperlinkDef(bookmark, IHyperlinkAction.ACTION_BOOKMARK, tooltip);

				break;
			case IHyperlinkAction.ACTION_HYPERLINK:
				String url = EmitterUtil.getHyperlinkUrl(linkAction, reportRunnable, actionHandler, reportContext);
				hyperlink = new HyperlinkDef(url, IHyperlinkAction.ACTION_HYPERLINK, tooltip);
				break;
			case IHyperlinkAction.ACTION_DRILLTHROUGH:
				url = EmitterUtil.getHyperlinkUrl(linkAction, reportRunnable, actionHandler, reportContext);
				hyperlink = new HyperlinkDef(url, IHyperlinkAction.ACTION_DRILLTHROUGH, tooltip);
				break;
			}
		}
		if (hyperlink != null) {
			Color color = PropertyUtil.getColor(content.getStyle().getProperty(IStyle.STYLE_COLOR));
			hyperlink.setColor(color);
		}
		return hyperlink;
	}

	public TimeZone getTimeZone() {
		if (service != null) {
			IReportContext reportContext = service.getReportContext();
			if (reportContext != null) {
				return reportContext.getTimeZone();
			}
		}
		return TimeZone.getDefault();
	}

	public void endContainer(IContainerContent container) {
		engine.removeContainerStyle();
	}

	public void startContainer(IContainerContent container) {
		engine.addContainerStyle(container.getComputedStyle());
	}
}
