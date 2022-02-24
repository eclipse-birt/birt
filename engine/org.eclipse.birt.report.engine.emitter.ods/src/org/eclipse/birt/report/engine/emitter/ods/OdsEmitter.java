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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
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
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ods.layout.ColumnsInfo;
import org.eclipse.birt.report.engine.emitter.ods.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.ods.layout.LayoutUtil;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsContext;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsLayoutEngine;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.AbstractOdfEmitter;
import org.eclipse.birt.report.engine.odf.AbstractOdfEmitterContext;
import org.eclipse.birt.report.engine.odf.MasterPageManager;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class OdsEmitter extends AbstractOdfEmitter {
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.spreadsheet"; //$NON-NLS-1$

	private boolean isAuto = true;

	protected static Logger logger = Logger.getLogger(OdsEmitter.class.getName());

	protected static final String DEFAULT_SHEET_NAME = "Report";

	protected OdsLayoutEngine engine;

	protected IOdsWriter writer;

	protected MasterPageWriter mpWriter;

	protected OdsContext context;

	private boolean outputInMasterPage = false;
	protected boolean isRTLSheet = false;
	protected int sheetIndex = 1;
	protected String sheetName;

	protected int pageWidth;

	protected int pageHeight;

	protected int contentwidth;

	protected int reportDpi;

	protected StyleEntry pageLayout;

	protected IReportContext reportContext;

	public String getOutputFormat() {
		return "ods";
	}

	public void initialize(IEmitterServices service) throws EngineException {
		super.initialize(service);
		IReportContext reportContext = service.getReportContext();
		if (reportContext != null) {
			Locale locale = reportContext.getLocale();
			if (locale != null) {
				context.setLocale(ULocale.forLocale(locale));
			} else
				context.setLocale(ULocale.getDefault());
		}
		this.reportContext = reportContext;
		tableCount = 0;
	}

	protected AbstractOdfEmitterContext createContext() {
		this.context = new OdsContext();
		return context;
	}

	public void start(IReportContent report) throws BirtException {
		super.start(report);
		setupRenderOptions();
		// We can the page size from the design, maybe there is a better way
		// to get the page definition.
		ReportDesignHandle designHandle = report.getDesign().getReportDesign();
		parseReportOrientation(designHandle);
		parseReportLayout(designHandle);
		parseSheetName(designHandle);
		parsePageSize(report);
		IStyle style = report.getRoot().getComputedStyle();
		engine = createLayoutEngine(context, this);
		engine.initalize(contentwidth, style, reportDpi, context.getStyleManager());
		createWriter();

		try {
			writeMetaProperties(reportContent);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		} catch (BirtException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}

	}

	private void parseSheetName(ReportDesignHandle designHandle) {
		String reportTitle = designHandle.getStringProperty(IModuleModel.TITLE_PROP);
		if (reportTitle != null) {
			sheetName = reportTitle;
		} else {
			sheetName = DEFAULT_SHEET_NAME;
		}
		sheetName = OdsUtil.getValidSheetName(sheetName);
	}

	private void parseReportLayout(ReportDesignHandle designHandle) {
		String reportLayoutPreference = designHandle.getLayoutPreference();
		if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(reportLayoutPreference)) {
			isAuto = false;
		}
	}

	private void parseReportOrientation(ReportDesignHandle designHandle) {
		String reportOrientation = designHandle.getBidiOrientation();
		if ("rtl".equalsIgnoreCase(reportOrientation)) {
			isRTLSheet = true;
		}
	}

	private void parsePageSize(IReportContent report) {
		Object dpi = report.getReportContext().getRenderOption().getOption(IRenderOption.RENDER_DPI);
		int renderDpi = 0;
		if (dpi != null && dpi instanceof Integer) {
			renderDpi = ((Integer) dpi).intValue();
		}
		reportDpi = PropertyUtil.getRenderDpi(report, renderDpi);
		SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) report.getDesign().getPageSetup().getMasterPage(0);
		this.pageWidth = OdfUtil.convertDimensionType(masterPage.getPageWidth(), 0, reportDpi);
		int leftmargin = OdfUtil.convertDimensionType(masterPage.getLeftMargin(), pageWidth, reportDpi);
		int rightmargin = OdfUtil.convertDimensionType(masterPage.getRightMargin(), pageWidth, reportDpi);
		this.contentwidth = pageWidth - leftmargin - rightmargin;
		this.pageHeight = OdfUtil.convertDimensionType(masterPage.getPageHeight(), 0, reportDpi);
	}

	protected OdsLayoutEngine createLayoutEngine(OdsContext context, OdsEmitter emitter) {
		return new OdsLayoutEngine(context, emitter);
	}

	private void setupRenderOptions() {
		IRenderOption renderOptions = service.getRenderOption();
		Object textWrapping = renderOptions.getOption(IExcelRenderOption.WRAPPING_TEXT);
		if (textWrapping != null && textWrapping instanceof Boolean) {
			context.setWrappingText((Boolean) textWrapping);
		} else {
			context.setWrappingText((Boolean) true);
		}

		Object hideGridlines = renderOptions.getOption(IExcelRenderOption.HIDE_GRIDLINES);
		if (hideGridlines != null && hideGridlines instanceof Boolean) {
			context.setHideGridlines((Boolean) hideGridlines);
		} else {
			context.setHideGridlines((Boolean) false);
		}
	}

	public void startPage(IPageContent page) throws BirtException {
		if (pageLayout == null) {
			pageLayout = makePageLayoutStyle(page);
		}

		MasterPageManager mpManager = context.getMasterPageManager();
		mpManager.newPage("Standard"); //$NON-NLS-1$
		mpWriter.startMasterPage(pageLayout, mpManager.getCurrentMasterPage(), null);

		if (needOutputInMasterPage(page.getPageHeader()) && needOutputInMasterPage(page.getPageFooter())) {
			outputInMasterPage = true;
			IContent pageHeader = page.getPageHeader();
			IContent pageFooter = page.getPageFooter();

			if (pageHeader != null) {
				mpWriter.startHeader();
				mpWriter.writeHeaderFooter(pageHeader);
				mpWriter.endHeader();
			}

			if (pageFooter != null) {
				mpWriter.startFooter();
				mpWriter.writeHeaderFooter(pageFooter);
				mpWriter.endFooter();
			}
		}
		mpWriter.endMasterPage();

		if (!outputInMasterPage && page.getPageHeader() != null) {
			contentVisitor.visitChildren(page.getPageHeader(), null);
		}
	}

	public void endPage(IPageContent page) throws BirtException {
		if (!outputInMasterPage && page.getPageFooter() != null) {
			contentVisitor.visitChildren(page.getPageFooter(), null);
		}
	}

	public void startTable(ITableContent table) {
		ContainerSizeInfo sizeInfo = engine.getCurrentContainer().getSizeInfo();
		int width = sizeInfo.getWidth();
		ColumnsInfo info = null;
		// now only use "auto" to deal with table's width.
		boolean isAutoTable = true;
		if (isAutoTable) {
			info = LayoutUtil.createTable(table, width, reportDpi);
		} else {
			int[] columns = LayoutUtil.createFixedTable(table, LayoutUtil.getElementWidth(table, width, reportDpi),
					reportDpi);
			info = new ColumnsInfo(columns);
		}
		String caption = table.getCaption();
		if (caption != null) {
			engine.addCaption(caption, table.getComputedStyle());
		}
		engine.addTable(table, info, sizeInfo);
	}

	public void startRow(IRowContent row) {
		engine.addRow(row.getComputedStyle());
	}

	public void endRow(IRowContent row) {
		DimensionType height = row.getHeight();
		float rowHeight = (float) OdfUtil.convertDimensionType(height, 0, reportDpi) / 1000f;
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
		ColumnsInfo table = LayoutUtil.createTable(list, size.getWidth(), reportDpi);
		engine.addTable(list, table, size);

		if (list.getChildren() == null) {
			HyperlinkInfo link = parseHyperLink(list);
			BookmarkDef bookmark = getBookmark(list);
			float height = getContentHeight(list);
			engine.addData(OdsLayoutEngine.EMPTY, list.getComputedStyle(), link, bookmark, height);
		}
	}

	public void startListBand(IListBandContent listBand) {
		engine.addCell(0, 1, 1, listBand.getComputedStyle());
	}

	public void endListBand(IListBandContent listBand) {
		engine.endContainer();
	}

	public void endList(IListContent list) {
		engine.endTable(list);
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			HTML2Content.html2Content(foreign);
			HyperlinkInfo link = parseHyperLink(foreign);
			engine.addContainer(foreign.getComputedStyle(), link);
			contentVisitor.visitChildren(foreign, null);
			engine.endContainer();
		}
	}

	public void startText(ITextContent text) {
		HyperlinkInfo url = parseHyperLink(text);
		BookmarkDef bookmark = getBookmark(text);
		float height = getContentHeight(text);
		engine.addData(text.getText(), text.getComputedStyle(), url, bookmark, height);
	}

	public void startData(IDataContent data) {
		addDataContent(data);
	}

	protected Data addDataContent(IDataContent data) {
		float height = getContentHeight(data);
		HyperlinkInfo url = parseHyperLink(data);
		BookmarkDef bookmark = getBookmark(data);
		Data outputData = null;
		Object generateBy = data.getGenerateBy();
		IStyle style = data.getComputedStyle();
		DataFormatValue dataformat = style.getDataFormat();
		MapDesign map = null;
		if (generateBy instanceof DataItemDesign) {
			DataItemDesign design = (DataItemDesign) generateBy;
			map = design.getMap();
		}
		if (map != null && map.getRuleCount() > 0 && data.getLabelText() != null) {
			outputData = engine.addData(data.getText(), style, url, bookmark, height);
		} else {
			String locale = null;
			int type = OdsUtil.getType(data.getValue());
			if (type == SheetData.STRING) {
				if (dataformat != null) {
					locale = dataformat.getStringLocale();
				}
				outputData = engine.addData(data.getText(), style, url, bookmark, locale, height);
			} else if (type == Data.NUMBER) {
				if (dataformat != null) {
					locale = dataformat.getNumberLocale();
				}
				outputData = engine.addData(data.getValue(), style, url, bookmark, locale, height);
			} else {
				if (dataformat != null) {
					locale = dataformat.getDateTimeLocale();
				}
				outputData = engine.addDateTime(data, style, url, bookmark, locale, height);
			}
		}
		return outputData;
	}

	private float getContentHeight(IContent content) {
		return OdfUtil.convertDimensionType(content.getHeight(), 0, reportDpi) / 1000f;
	}

	public void startImage(IImageContent image) {
		IStyle style = image.getComputedStyle();
		HyperlinkInfo url = parseHyperLink(image);
		BookmarkDef bookmark = getBookmark(image);

		engine.addImageData(image, style, url, bookmark);
	}

	public void startLabel(ILabelContent label) {
		Object design = label.getGenerateBy();
		IContent container = label;

		while (design == null) {
			container = (IContent) container.getParent();
			design = ((IContent) container).getGenerateBy();
		}

		HyperlinkInfo url = parseHyperLink(label);
		BookmarkDef bookmark = getBookmark(label);

		// If the text is BR and it generated by foreign,
		// ignore it
		if (!("\n".equalsIgnoreCase(label.getText()) && container instanceof IForeignContent)) {
			float height = getContentHeight(label);
			engine.addData(label.getText(), label.getComputedStyle(), url, bookmark, height);
		}
	}

	public void startAutoText(IAutoTextContent autoText) {
		HyperlinkInfo link = parseHyperLink(autoText);
		BookmarkDef bookmark = getBookmark(autoText);
		float height = getContentHeight(autoText);
		engine.addData(autoText.getText(), autoText.getComputedStyle(), link, bookmark, height);
	}

	public void outputSheet() {
		engine.cacheBookmarks(sheetName);
		engine.complete(isAuto);
		try {
			outputCacheData();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		sheetIndex++;
	}

	public void end(IReportContent report) throws BirtException {
		// Make sure the engine already calculates all data in cache.
		engine.cacheBookmarks(sheetName);
		engine.complete(isAuto);
		try {
			writer.start(report, engine.getAllBookmarks());
			outputCacheData();
			writer.end();
			mpWriter.end();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		super.end(report);
	}

	protected void createWriter() {
		writer = new OdsWriter(bodyOut, context, isRTLSheet);
		mpWriter = new MasterPageWriter(masterPageOut, context.getGlobalStyleManager());
		mpWriter.start();
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void outputCacheData() throws IOException {
		// update sheet name to page label, if necessary
		Object pageLabelObj = reportContext.getPageVariable("__page_label");
		if (pageLabelObj instanceof String) {
			String pageLabel = (String) pageLabelObj;
			pageLabel = OdsUtil.getValidSheetName(pageLabel);
			sheetName = pageLabel;
		}

		MasterPageManager mpManager = context.getMasterPageManager();

		tableCount++;
		StyleEntry tableStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_TABLE);
		tableStyle.setProperty(StyleConstant.MASTER_PAGE, mpManager.getCurrentMasterPage());
		context.addStyle(getTableStylePrefix(), tableStyle);

		// TODO: master page header + footer content

		int[] cols = engine.getCoordinates();
		double[] colSizes = new double[cols.length];
		for (int i = 0; i < cols.length; i++) {
			// convert to inches
			colSizes[i] = (cols[i] / PDFConstants.LAYOUT_TO_PDF_RATIO) / OdfUtil.INCH_PT;
		}

		StyleEntry[] colStyles = getColStyles(colSizes);

		writer.startSheet(tableStyle, colStyles, sheetName);
		sheetName = DEFAULT_SHEET_NAME + sheetIndex;
		Iterator<RowData> it = engine.getIterator();
		while (it.hasNext()) {
			outputRowData(it.next());
		}
		writer.endSheet();
	}

	protected void outputRowData(RowData rowData) throws IOException {
		// TODO: check unit
		StyleEntry rowStyle = StyleBuilder.createEmptyStyleEntry(StyleEntry.TYPE_TABLE_ROW);
		rowStyle.setProperty(StyleConstant.HEIGHT, rowData.getHeight() / OdfUtil.INCH_PT);
		context.addStyle(getTableStylePrefix(), rowStyle);

		writer.startRow(rowStyle);
		SheetData[] datas = rowData.getRowdata();
		for (int i = 0; i < datas.length; i++) {
			SheetData data = datas[i];
			int start = engine.getStartColumn(data);
			int end = engine.getEndColumn(data);
			int span = Math.max(0, end - start - 1);
			writer.outputData(data, data.getStyleId(), start, span);
		}
		writer.endRow();
	}

	public HyperlinkInfo parseHyperLink(IContent content) {
		HyperlinkInfo hyperlink = null;
		IHyperlinkAction linkAction = content.getHyperlinkAction();

		if (linkAction != null) {
			String tooltip = linkAction.getTooltip();
			String bookmark = linkAction.getBookmark();
			switch (linkAction.getType()) {
			case IHyperlinkAction.ACTION_BOOKMARK:
				hyperlink = new HyperlinkInfo(IHyperlinkAction.ACTION_BOOKMARK, bookmark, tooltip);

				break;
			case IHyperlinkAction.ACTION_HYPERLINK:
				String url = EmitterUtil.getHyperlinkUrl(linkAction, reportRunnable, actionHandler, reportContext);
				hyperlink = new HyperlinkInfo(IHyperlinkAction.ACTION_HYPERLINK, url, tooltip);
				break;
			case IHyperlinkAction.ACTION_DRILLTHROUGH:
				url = EmitterUtil.getHyperlinkUrl(linkAction, reportRunnable, actionHandler, reportContext);
				hyperlink = new HyperlinkInfo(IHyperlinkAction.ACTION_DRILLTHROUGH, url, tooltip);
				break;
			}
		}
		if (hyperlink != null) {
			StyleEntry style = StyleBuilder.createStyleEntry(content.getStyle(), StyleEntry.TYPE_TEXT);
			context.addStyle(style);
			hyperlink.setStyle(style);
		}
		return hyperlink;
	}

	protected BookmarkDef getBookmark(IContent content) {
		String bookmarkName = content.getBookmark();
		if (bookmarkName == null)
			return null;

		BookmarkDef bookmark = new BookmarkDef(content.getBookmark());
		if (!OdsUtil.isValidBookmarkName(bookmarkName)) {
			bookmark.setGeneratedName(engine.getGenerateBookmark(bookmarkName));
		}

		// !( content.getBookmark( ).startsWith( "__TOC" ) ) )
		// bookmark starting with "__TOC" is not OK?
		return bookmark;
	}

	public String capitalize(String orientation) {
		if (orientation != null) {
			if (orientation.equalsIgnoreCase("landscape")) {
				return "Landscape";
			}
			if (orientation.equalsIgnoreCase("portrait")) {
				return "Portrait";
			}
		}
		return null;
	}

	public boolean needOutputInMasterPage(IContent headerFooter) {
		if (headerFooter != null) {
			Collection list = headerFooter.getChildren();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Object child = iter.next();
				if (child instanceof ITableContent) {
					int columncount = ((ITableContent) child).getColumnCount();
					int rowcount = ((ITableContent) child).getChildren().size();
					if (columncount > 3 || rowcount > 1) {
						logger.log(Level.WARNING,
								"ODS page header or footer only accept a table no more than 1 row and 3 columns.");
						return false;
					}
					if (isEmbededTable((ITableContent) child)) {
						logger.log(Level.WARNING, "ODS page header and footer don't support embeded grid.");
						return false;
					}
				}
				if (isHtmlText(child)) {
					logger.log(Level.WARNING, "ODS page header and footer don't support html text.");
					return false;
				}
			}
		}
		return true;
	}

	private boolean isHtmlText(Object child) {
		return child instanceof IForeignContent
				&& IForeignContent.HTML_TYPE.equalsIgnoreCase(((IForeignContent) child).getRawType());
	}

	private boolean isEmbededTable(ITableContent table) {
		boolean isEmbeded = false;
		Collection list = table.getChildren();
		Iterator iterRow = list.iterator();
		while (iterRow.hasNext()) {
			Object child = iterRow.next();
			Collection listCell = ((IRowContent) child).getChildren();
			Iterator iterCell = listCell.iterator();
			while (iterCell.hasNext()) {
				Object cellChild = iterCell.next();
				Collection listCellChild = ((ICellContent) cellChild).getChildren();
				Iterator iterCellChild = listCellChild.iterator();
				while (iterCellChild.hasNext()) {
					Object cellchild = iterCellChild.next();
					if (cellchild instanceof ITableContent) {
						isEmbeded = true;
					}
				}
			}
		}
		return isEmbeded;
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

	protected String getRootMime() {
		return MIME_TYPE;
	}
}
