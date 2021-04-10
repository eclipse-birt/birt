/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.odt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.odt.writer.BodyWriter;
import org.eclipse.birt.report.engine.emitter.odt.writer.MasterPageWriter;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.odf.AbstractOdfEmitter;
import org.eclipse.birt.report.engine.odf.AbstractOdfEmitterContext;
import org.eclipse.birt.report.engine.odf.DiagonalLineInfo;
import org.eclipse.birt.report.engine.odf.MasterPageManager;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.SpanInfo;
import org.eclipse.birt.report.engine.odf.pkg.ImageEntry;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.w3c.dom.css.CSSValue;

public class OdtEmitter extends AbstractOdfEmitter {
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.text"; //$NON-NLS-1$

	public final static int NORMAL = -1;

	public static enum InlineFlag {
		FIRST_INLINE, MIDDLE_INLINE, BLOCK
	};

	public static enum TextFlag {
		START, MIDDLE, END, WHOLE
	};

	public final static int MAX_COLUMN = 63;

	protected AbstractOdfEmitter emitterImplement = null;

	private int omitCellLayer = 0;

	private boolean isClipped = false;

	public EmitterContext context;

	protected static Logger logger = Logger.getLogger(OdtEmitter.class.getName());

	private static final String OUTPUT_FORMAT = "odt"; //$NON-NLS-1$

	private Stack<IStyle> inlineStyles = new Stack<IStyle>();

	protected IOdtWriter bodyWriter = null;

	private boolean inForeign = false;

	private boolean hasPInside = false;

	protected IPageContent previousPage = null;

	protected Stack<IStyle> styles = new Stack<IStyle>();

	public double pageWidth = 0;

	public double contentWidth = 0;

	public double leftMargin = 0;

	public double rightMargin = 0;

	private StyleEntry pageLayout = null;

	private HashSet<String> bookmarks = new HashSet<String>();

	private boolean rowFilledFlag = false;

	private ArrayList<InstanceID> groupIdList = new ArrayList<InstanceID>();

	private int tocLevel = 1;

	private List<TocInfo> tableTocs = new ArrayList<TocInfo>();

	private String messageFlashObjectNotSupported;

	private String layoutPreference = null;

	private boolean fixedLayout;

	/**
	 * Deferred bookmarks names that need to be output in the first paragraph met.
	 * This applies to table/row or cell bookmark.
	 */
	private List<String> containerBookmarks;

	private MasterPageWriter masterPageWriter;

	private boolean pageBreakBefore = false;
	private String masterPage = null;

	public OdtEmitter() {
		super();
		containerBookmarks = new ArrayList<String>(3);
		pageBreakBefore = false;
		masterPage = null;
		tableCount = 0;
	}

	public String getOutputFormat() {
		return OUTPUT_FORMAT;
	}

	public void endContainer(IContainerContent container) {
		if (isClipped) {
			return;
		}

		boolean flag = hasForeignParent(container);

		if (flag) {
			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				adjustInline();
			}
			if (!styles.isEmpty()) {
				styles.pop();
			}
			if (!inlineStyles.isEmpty()) {
				inlineStyles.pop();
			}

			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				if (inForeign && hasPInside) {
					context.addContainer(false);
					hasPInside = false;
				} else if (!inForeign) {
					context.addContainer(true);
				}
				context.setLastIsTable(true);
			}
		}
	}

	public void startContainer(IContainerContent container) {
		if (isClipped) {
			return;
		}

		boolean flag = hasForeignParent(container);

		if (flag) {
			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				adjustInline();
			}

			if (!CSSConstants.CSS_INLINE_VALUE.equalsIgnoreCase(container.getComputedStyle().getDisplay())) {
				styles.push(container.getComputedStyle());
			} else {
				inlineStyles.push(container.getComputedStyle());
			}
		}
	}

	private boolean hasForeignParent(IContainerContent container) {
		IContainerContent con = container;
		while (con != null) {
			if (con.getParent() instanceof IForeignContent) {
				return true;
			}
			con = (IContainerContent) con.getParent();
		}
		return false;
	}

	public void endTable(ITableContent table) {
		if (isClipped) {
			return;
		}

		hasPInside = false;
		endTable();
		decreaseTOCLevel(table);
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		if (isClipped) {
			return;
		}

		if (IForeignContent.HTML_TYPE.equalsIgnoreCase(foreign.getRawType())) {
			inForeign = true;
			// store the inline state before the HTML foreign.
			boolean inlineBrother = !context.isFirstInline();
			// the inline state needs be recalculated in the HTML foreign.

			// if the foreign itself is not inline
			if (!"inline".equalsIgnoreCase(foreign.getComputedStyle() //$NON-NLS-1$
					.getDisplay())) {
				// stop the inline mode completely
				adjustInline();
				inlineBrother = false;
			} else {
				// only store the state to restore it later
				context.endInline();
			}

			HTML2Content.html2Content(foreign);

			context.startCell();

			tableCount++;
			StyleEntry foreignStyle = StyleBuilder.createStyleEntry(foreign.getComputedStyle(), StyleEntry.TYPE_TABLE);
			foreignStyle.setProperty(StyleConstant.WIDTH, context.getCurrentWidth());
			StyleEntry cellStyle = StyleBuilder.createStyleEntry(foreign.getComputedStyle(),
					StyleEntry.TYPE_TABLE_CELL);
			cellStyle.setProperty(StyleConstant.WIDTH, context.getCurrentWidth());

			context.addStyle(getTableStylePrefix(), foreignStyle);
			context.addStyle(getTableStylePrefix(), cellStyle);

			bodyWriter.startTable(null, foreignStyle);
			bodyWriter.writeColumn(new StyleEntry[] { null }); // only one column without style
			bodyWriter.startTableRow(null);
			bodyWriter.startTableCell(cellStyle, null);
			writeToc(foreign);
			contentVisitor.visitChildren(foreign, null);

			adjustInline();

			bodyWriter.endTableCell();

			context.endCell();
			bodyWriter.endTableRow();
			bodyWriter.endTable();
			context.setLastIsTable(true);
			context.addContainer(true);
			hasPInside = false;
			// restore the inline state after the HTML foreign.
			if (inlineBrother) {
				context.startInline();
			}
			inForeign = false;
		} else {
			Object rawValue = foreign.getRawValue();
			String text = rawValue == null ? "" : rawValue.toString(); //$NON-NLS-1$
			writeContent(OdtEmitter.NORMAL, text, foreign);
		}
	}

	protected void writeContent(int type, String txt, IContent content) {
		if (inForeign) {
			hasPInside = true;
		}
		context.addContainer(false);

		InlineFlag inlineFlag = InlineFlag.BLOCK;
		IStyle computedStyle = content.getComputedStyle();
		IStyle inlineStyle = null;

		boolean isInline = false;
		if ("inline".equalsIgnoreCase(content.getComputedStyle() //$NON-NLS-1$
				.getDisplay())) {
			isInline = true;
			if (context.isFirstInline()) {
				context.startInline();
				inlineFlag = InlineFlag.FIRST_INLINE;
				if (!styles.isEmpty()) {
					computedStyle = styles.peek();
				}
			} else
				inlineFlag = InlineFlag.MIDDLE_INLINE;
			if (!inlineStyles.isEmpty()) {
				inlineStyle = mergeStyles(inlineStyles);
			}
		} else {
			adjustInline();
		}

		StyleEntry computedStyleEntry = StyleBuilder.createStyleEntry(computedStyle, StyleEntry.TYPE_PARAGRAPH);
		StyleEntry inlineStyleEntry = null;
		if (inlineStyle != null) {
			inlineStyleEntry = StyleBuilder.createStyleEntry(inlineStyle, StyleEntry.TYPE_TEXT);
		} else {
			// use the text part of the computed style
			inlineStyleEntry = StyleBuilder.createStyleEntry(computedStyle, StyleEntry.TYPE_TEXT);
		}

		processBackgroundImageStyle(computedStyleEntry);

		context.addStyle(computedStyleEntry);
		context.addStyle(inlineStyleEntry);

		if (pageBreakBefore || masterPage != null) {
			computedStyleEntry = (StyleEntry) computedStyleEntry.clone();
			processMasterPage(computedStyleEntry);
			context.addStyle(computedStyleEntry);
		}

		writeText(type, txt, content, inlineFlag, computedStyleEntry, inlineStyleEntry);
		context.setLastIsTable(false);
	}

	private IStyle mergeStyles(Stack<IStyle> inlineStyles) {
		IStyle style = inlineStyles.peek();

		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			if (isNullValue(style.getProperty(i))) {
				style.setProperty(i, null);

				for (int p = inlineStyles.size() - 1; p >= 0; p--) {
					IStyle pstyle = (IStyle) inlineStyles.get(p);

					if (!isNullValue(pstyle.getProperty(i))) {
						style.setProperty(i, pstyle.getProperty(i));
						break;
					}
				}
			}
		}
		return style;
	}

	public void initialize(IEmitterServices service) throws EngineException {
		super.initialize(service);
		if (service != null) {
			EngineResourceHandle resourceHandle = new EngineResourceHandle(context.getLocale());
			messageFlashObjectNotSupported = resourceHandle
					.getMessage(MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT);
			IRenderOption renderOption = service.getRenderOption();
			if (renderOption != null) {
				HTMLRenderOption htmlOption = new HTMLRenderOption(renderOption);
				layoutPreference = htmlOption.getLayoutPreference();
			}
		}

		try {
			bodyWriter = new BodyWriter(bodyOut);
			masterPageWriter = new MasterPageWriter(masterPageOut);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	protected AbstractOdfEmitterContext createContext() {
		this.context = new EmitterContext();
		return this.context;
	}

	public void start(IReportContent report) throws BirtException {
		super.start(report);
		if (null == layoutPreference) {
			ReportDesignHandle designHandle = report.getDesign().getReportDesign();
			if (designHandle != null) {
				String reportLayoutPreference = designHandle.getLayoutPreference();
				if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(reportLayoutPreference)) {
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED;
				} else if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT.equals(reportLayoutPreference)) {
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_AUTO;
				}
			}
			fixedLayout = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals(layoutPreference);
		}

		masterPageWriter.start(false);
	}

	public void startPage(IPageContent page) {
		MasterPageManager masterPageManager = context.getMasterPageManager();
		if (previousPage != null) {
			try {
				outputPrePageProperties(page);
				// set flag to true, will add a page break before flag
				// to the next paragraph/table
				pageBreakBefore = true;
				masterPage = masterPageManager.getCurrentMasterPage();
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}
			previousPage = page;
			context.resetWidth();
		} else {
			previousPage = page;
			boolean isRtl = false;
			if (reportContent != null) {
				IContent rootContent = reportContent.getRoot();
				isRtl = rootContent != null && rootContent.isRTL();
			}

			try {
				writeMetaProperties(reportContent);
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			} catch (BirtException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}

			try {
				bodyWriter.start(isRtl);
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}

			SimpleMasterPageDesign master = (SimpleMasterPageDesign) page.getGenerateBy();
			masterPageManager.newPage(master.getName());
			masterPage = masterPageManager.getCurrentMasterPage();
		}
		processPageLayout(page);
		context.addWidth(contentWidth);
		bodyWriter.startPage();
	}

	private void outputPrePageProperties(IPageContent page) throws IOException, BirtException {
		adjustInline();
		writeHeaderFooter(page, false);
		bodyWriter.endPage();
	}

	public void end(IReportContent report) throws BirtException {
		adjustInline();
		try {
			writeHeaderFooter(null, true);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}
		bodyWriter.endPage();
		try {
			bodyWriter.end();
			masterPageWriter.end();
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}

		super.end(report);
	}

	public void processPageLayout(IPageContent page) {

		int reportDpi = context.getReportDpi();

		pageWidth = OdfUtil.convertTo(page.getPageWidth(), 0.0, reportDpi);
		// 11 inch * 1440
		/*
		 * double pageHeight = OdfUtil.convertTo( page.getPageHeight( ), 0.0, reportDpi
		 * ); double footerHeight = OdfUtil.convertTo( page.getFooterHeight( ), 0.0,
		 * reportDpi ); double headerHeight = OdfUtil.convertTo( page.getHeaderHeight(
		 * ), 0.0, reportDpi ); double topMargin = OdfUtil.convertTo( page.getMarginTop(
		 * ), 0.0, reportDpi ); double bottomMargin = OdfUtil.convertTo(
		 * page.getMarginBottom( ), 0.0, reportDpi );
		 */

		leftMargin = OdfUtil.convertTo(page.getMarginLeft(), 0.0, reportDpi);
		rightMargin = OdfUtil.convertTo(page.getMarginRight(), 0.0, reportDpi);

		contentWidth = pageWidth - leftMargin - rightMargin;

		pageLayout = makePageLayoutStyle(page);
	}

	public void startAutoText(IAutoTextContent autoText) {
		if (isClipped) {
			return;
		}
		writeContent(autoText.getType(), autoText.getText(), autoText);
	}

	public void startData(IDataContent data) {
		if (isClipped) {
			return;
		}

		writeContent(NORMAL, data.getText(), data);
	}

	public void startLabel(ILabelContent label) {
		if (isClipped) {
			return;
		}

		String txt = label.getText() == null ? label.getLabelText() : label.getText();
		txt = txt == null ? "" : txt;
		writeContent(NORMAL, txt, label);
	}

	public void startText(ITextContent text) {
		if (isClipped) {
			return;
		}

		writeContent(NORMAL, text.getText(), text);
	}

	public void startList(IListContent list) {
		if (isClipped) {
			return;
		}

		adjustInline();

		styles.push(list.getComputedStyle());
		addTableToc(list);
		increaseTOCLevel(list);

		double width = OdfUtil.convertTo(list.getWidth(), context.getCurrentWidth(), context.getReportDpi());
		width = Math.min(width, context.getCurrentWidth());
		StyleEntry tableStyle = StyleBuilder.createStyleEntry(list.getComputedStyle(), StyleEntry.TYPE_TABLE);
		tableStyle.setProperty(StyleConstant.WIDTH, width);
		processMasterPage(tableStyle);
		context.addStyle(tableStyle);
		bodyWriter.startTable(null, tableStyle);
		addContainerBookmark(list);
	}

	public void startListBand(IListBandContent listBand) {
		if (isClipped) {
			return;
		}

		context.startCell();
		bodyWriter.startTableRow(null);

		StyleEntry cellStyle = StyleBuilder.createStyleEntry(computeStyle(listBand.getComputedStyle()),
				StyleEntry.TYPE_TABLE_CELL);
		cellStyle.setProperty(StyleConstant.WIDTH, context.getCurrentWidth());
		context.addStyle(cellStyle);
		bodyWriter.startTableCell(cellStyle, null);
	}

	public void startListGroup(IListGroupContent group) {
		if (isClipped) {
			return;
		}

		setGroupToc(group);
	}

	public void startRow(IRowContent row) {
		if (isClipped) {
			return;
		}

		if (!isHidden(row)) {
			rowFilledFlag = false;
			styles.push(row.getComputedStyle());

			// TODO: fixedLayout

			StyleEntry rowStyle = context.getRowHeightStyle(row.getHeight());
			context.addStyle(getTableStylePrefix(), rowStyle);

			bodyWriter.startTableRow(rowStyle);
			addContainerBookmark(row);
			context.newRow();
		}
	}

	public void startContent(IContent content) {
		if (isClipped) {
			return;
		}
	}

	public void startGroup(IGroupContent group) {
		if (isClipped) {
			return;
		}

		setGroupToc(group);
	}

	public void startCell(ICellContent cell) {
		if (isClipped) {
			omitCellLayer++;
			return;
		}
		int colCount = cell.getColumn();
		if (colCount >= MAX_COLUMN) {
			omitCellLayer++;
			isClipped = true;
			return;
		}

		rowFilledFlag = true;
		context.startCell();
		int columnId = cell.getColumn();
		double cellWidth = context.getCellWidth(columnId, cell.getColSpan());

		IStyle style = computeStyle(cell.getComputedStyle());

		StyleEntry cellStyle = StyleBuilder.createStyleEntry(style, StyleEntry.TYPE_TABLE_CELL);
		cellStyle.setProperty(StyleConstant.WIDTH, cellWidth);
		processBackgroundImageStyle(cellStyle);
		context.addStyle(getTableStylePrefix(), cellStyle);

		SpanInfo info = processSpan(cell, cellStyle);

		bodyWriter.startTableCell(cellStyle, info);
		addContainerBookmark(cell);
		context.addWidth(getCellWidth(cellWidth, style));
		if (cell.getDiagonalNumber() != 0 && cell.getDiagonalStyle() != null
				&& !"none".equalsIgnoreCase(cell.getDiagonalStyle())) {
			drawDiagonalLine(cell, OdfUtil.twipToPt(cellWidth));
		}
	}

	/**
	 * @param columnId
	 * @param cell
	 * @param cellStyle
	 * @return
	 */
	private SpanInfo processSpan(ICellContent cell, StyleEntry cellStyle) {
		int columnId = cell.getColumn();
		List<SpanInfo> spans = context.getSpans(columnId);

		if (spans != null) {
			for (int i = 0; i < spans.size(); i++) {
				bodyWriter.writeSpanCell(spans.get(i));
			}
		}
		int columnSpan = cell.getColSpan();
		int rowSpan = cell.getRowSpan();

		if (rowSpan > 1) {
			context.addSpan(columnId, columnSpan, rowSpan, cellStyle);
		}

		SpanInfo info = null;
		if (columnSpan > 1 || rowSpan > 1) {
			info = new SpanInfo(columnId, columnSpan, rowSpan, true, cellStyle);
		}
		return info;
	}

	private void drawDiagonalLine(ICellContent cell, double cellWidth) {
		if (cellWidth == 0)
			return;
		int cellHeight = OdfUtil.convertTo(getCellHeight(cell), 0, context.getReportDpi()) / 20;
		if (cellHeight == 0)
			return;

		DiagonalLineInfo diagonalLineInfo = new DiagonalLineInfo();
		int diagonalWidth = PropertyUtil.getDimensionValue(cell, cell.getDiagonalWidth(), (int) cellWidth) / 1000;
		diagonalLineInfo.setDiagonalLine(cell.getDiagonalNumber(), cell.getDiagonalStyle(), diagonalWidth);
		diagonalLineInfo.setAntidiagonalLine(0, null, 0);
		diagonalLineInfo.setCoordinateSize(cellWidth, cellHeight);
		String lineColor = null;
		if (cell.getDiagonalColor() != null) {
			lineColor = OdfUtil.parseColor(cell.getDiagonalColor());
		} else {
			lineColor = OdfUtil.parseColor(cell.getComputedStyle().getColor());
		}
		diagonalLineInfo.setColor(lineColor);
		bodyWriter.drawDiagonalLine(diagonalLineInfo);
	}

	protected DimensionType getCellHeight(ICellContent cell) {
		IElement parent = cell.getParent();
		while (!(parent instanceof IRowContent)) {
			parent = parent.getParent();
		}
		return ((IRowContent) parent).getHeight();
	}

	public void startTable(ITableContent table) {
		if (isClipped) {
			return;
		}

		tableCount++;

		adjustInline();
		styles.push(table.getComputedStyle());

		// always create a new style for the tables
		StyleEntry tableStyle = StyleBuilder.createStyleEntry(table.getComputedStyle(), StyleEntry.TYPE_TABLE);
		processBackgroundImageStyle(tableStyle);
		context.addStyle(getTableStylePrefix(), tableStyle);

		addTableToc(table);
		increaseTOCLevel(table);

		String caption = table.getCaption();
		if (caption != null) {
			StyleEntry captionStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_PARAGRAPH);
			captionStyle.setProperty(StyleConstant.H_ALIGN_PROP, "center"); //$NON-NLS-1$

			context.addStyle(captionStyle);
			processMasterPage(captionStyle);

			bodyWriter.writeCaption(caption, captionStyle);
		}

		double width = OdfUtil.convertTo(table.getWidth(), context.getCurrentWidth(), context.getReportDpi());
		width = Math.min(width, context.getCurrentWidth());
		double[] cols = computeTblColumnWidths(table, width);
		tableStyle.setProperty(StyleConstant.WIDTH, getTableWidth(cols));

		processMasterPage(tableStyle);

		bodyWriter.startTable(null, tableStyle);

		addContainerBookmark(table);

		bodyWriter.writeColumn(getColStyles(cols));
		context.addTable(cols, table.getComputedStyle());
	}

	private void processMasterPage(StyleEntry style) {
		if (pageBreakBefore) {
			pageBreakBefore = false;
			style.setProperty(StyleConstant.PAGE_BREAK_BEFORE, "page"); //$NON-NLS-1$
		}

		if (masterPage != null) {
			style.setProperty(StyleConstant.MASTER_PAGE, masterPage);
			masterPage = null;
		}
	}

	/**
	 * @param table
	 */
	private void addTableToc(IContent table) {
		Object tableToc = table.getTOC();
		if (tableToc != null) {
			tableTocs.add(new TocInfo(tableToc.toString(), tocLevel));
		}
	}

	private double getTableWidth(double[] cols) {
		double tableWidth = 0;
		for (int i = 0; i < cols.length; i++) {
			tableWidth += cols[i];
		}
		return tableWidth;
	}

	public void startTableBand(ITableBandContent band) {
		if (isClipped) {
			return;
		}

		if (band.getBandType() == IBandContent.BAND_HEADER && ((ITableContent) band.getParent()).isHeaderRepeat()) {
			bodyWriter.startTableHeader();
		}
	}

	public void startTableGroup(ITableGroupContent group) {
		if (isClipped) {
			return;
		}

		setGroupToc(group);
	}

	private void setGroupToc(IGroupContent group) {
		if (group != null) {
			InstanceID groupId = group.getInstanceID();
			if (!groupIdList.contains(groupId)) {
				groupIdList.add(groupId);
				addTableToc(group);
			}
			increaseTOCLevel(group);
		}
	}

	public void endCell(ICellContent cell) {
		if (omitCellLayer != 0) {
			omitCellLayer--;
			if (omitCellLayer == 0) {
				isClipped = false;
			}
			return;
		}

		adjustInline();
		context.removeWidth();

		if (!containerBookmarks.isEmpty() || !tableTocs.isEmpty()) {
			// the table bookmarks and tocs have still not be output in
			// a cell, then assume that this cell is the first one and
			// didn't contain any paragraph, so output an empty paragraph

			bodyWriter.writeMarkersParagraph(containerBookmarks, tableTocs);
		}

		bodyWriter.endTableCell();
		context.endCell();
	}

	public void endContent(IContent content) {

	}

	public void endGroup(IGroupContent group) {
		if (isClipped) {
			return;
		}

		decreaseTOCLevel(group);
	}

	public void endList(IListContent list) {
		if (isClipped) {
			return;
		}

		if (!styles.isEmpty()) {
			styles.pop();
		}

		context.addContainer(true);
		bodyWriter.endTable();
		context.setLastIsTable(true);
		decreaseTOCLevel(list);
	}

	public void endListBand(IListBandContent listBand) {
		if (isClipped) {
			return;
		}

		adjustInline();
		bodyWriter.endTableCell();
		context.endCell();
		bodyWriter.endTableRow();
	}

	public void endListGroup(IListGroupContent group) {
		if (isClipped) {
			return;
		}

		decreaseTOCLevel(group);
	}

	public void endRow(IRowContent row) {
		if (isClipped) {
			return;
		}

		if (!isHidden(row)) {
			if (!styles.isEmpty()) {
				styles.pop();
			}

			int col = context.getCurrentTableColmns().length - 1;

			List<SpanInfo> spans = context.getSpans(col);

			if (spans != null) {
				int spanSize = spans.size();
				if (spanSize > 0) {
					rowFilledFlag = true;
				}
				for (int i = 0; i < spanSize; i++) {
					bodyWriter.writeSpanCell(spans.get(i));
				}
			}
			if (!rowFilledFlag) {
				bodyWriter.writeEmptyCell();
				rowFilledFlag = true;
			}

			bodyWriter.endTableRow();
		}
	}

	public void endTableBand(ITableBandContent band) {
		if (isClipped) {
			return;
		}

		if (band.getBandType() == IBandContent.BAND_HEADER && ((ITableContent) band.getParent()).isHeaderRepeat()) {
			bodyWriter.endTableHeader();
		}
	}

	public void endTableGroup(ITableGroupContent group) {
		if (isClipped) {
			return;
		}

		decreaseTOCLevel(group);
	}

	public void endPage(IPageContent page) {
		if (isClipped) {
			return;
		}
	}

	public void startImage(IImageContent image) {
		if (isClipped) {
			return;
		}

		StyleEntry style = StyleBuilder.createStyleEntry(image.getComputedStyle(), StyleEntry.TYPE_DRAW);
		context.addStyle(style);

		InlineFlag inlineFlag = getInlineFlag(style.getStyle());
		String uri = image.getURI();
		String mimeType = image.getMIMEType();
		String extension = image.getExtension();
		String altText = image.getAltText();
		double height = OdfUtil.convertImageSize(image.getHeight(), 0, context.getReportDpi());
		int parentWidth = (int) (OdfUtil.twipToPt(context.getCurrentWidth()) * context.getReportDpi()
				/ OdfUtil.INCH_PT);
		double width = OdfUtil.convertImageSize(image.getWidth(), parentWidth, context.getReportDpi());
		context.addContainer(false);

		String bookmark = getBookmark(image);
		TocInfo tocInfo = null;
		if (image.getTOC() != null) {
			tocInfo = new TocInfo(image.getTOC().toString(), tocLevel);
		}

		StyleEntry pStyle = null;
		if (pageBreakBefore || masterPage != null) {
			pStyle = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_PARAGRAPH);
			processMasterPage(pStyle);
			context.addStyle(pStyle);
		}

		if (FlashFile.isFlash(mimeType, uri, extension)) {
			if (altText == null) {
				altText = messageFlashObjectNotSupported;
			}
			bodyWriter.drawImage(null, height, width, null, style, pStyle, inlineFlag, altText, bookmark, tocInfo);
			return;
		}

		try {
			ImageEntry entry = context.getImageManager().addImage(image);
			Image imageInfo = entry.getImage();
			byte[] data = imageInfo.getData();
			if (data == null || data.length == 0) {
				bodyWriter.drawImage(null, 0.0, 0.0, null, style, pStyle, inlineFlag, altText, bookmark, tocInfo);
				return;
			}

			int imageFileWidthDpi = imageInfo.getPhysicalWidthDpi() == -1 ? 0 : imageInfo.getPhysicalWidthDpi();
			int imageFileHeightDpi = imageInfo.getPhysicalHeightDpi() == -1 ? 0 : imageInfo.getPhysicalHeightDpi();
			if (image.getHeight() == null && image.getWidth() == null) {
				height = OdfUtil.convertImageSize(image.getHeight(), imageInfo.getHeight(),
						PropertyUtil.getImageDpi(image, imageFileHeightDpi, 0));
				width = OdfUtil.convertImageSize(image.getWidth(), imageInfo.getWidth(),
						PropertyUtil.getImageDpi(image, imageFileWidthDpi, 0));
			} else if (image.getWidth() == null) {
				float scale = ((float) imageInfo.getHeight()) / ((float) imageInfo.getWidth());
				width = height / scale;
			} else if (image.getHeight() == null) {
				float scale = ((float) imageInfo.getHeight()) / ((float) imageInfo.getWidth());
				height = width * scale;
			}

			HyperlinkInfo hyper = getHyperlink(image);
			bodyWriter.drawImage(entry.getUri(), height, width, hyper, style, pStyle, inlineFlag, altText, bookmark,
					tocInfo);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
			bodyWriter.drawImage(null, height, width, null, style, pStyle, inlineFlag, altText, bookmark, tocInfo);
		}
	}

	protected void endTable() {
		if (isClipped) {
			return;
		}

		context.addContainer(true);
		if (!styles.isEmpty()) {
			styles.pop();
		}

		bodyWriter.endTable();
		context.setLastIsTable(true);
		context.removeTable();
	}

	protected void increaseTOCLevel(IContent content) {
		if (content != null && content.getTOC() != null) {
			tocLevel += 1;
		}
	}

	protected void decreaseTOCLevel(IContent content) {
		if (content != null && content.getTOC() != null) {
			tocLevel -= 1;
		}
	}

	protected void adjustInline() {
		if (!context.isFirstInline()) {
			bodyWriter.endParagraph();
			context.endInline();
		}
	}

	protected void writeToc(IContent content) {
		if (content != null) {
			Object tocObj = content.getTOC();
			if (tocObj != null) {
				String toc = tocObj.toString();
				toc = toc.trim();

				if (!"".equals(toc)) {
					bodyWriter.writeTOC(new TocInfo(toc, tocLevel));
				}
			}
		}
	}

	private InlineFlag getInlineFlag(IStyle style) {
		InlineFlag inlineFlag = InlineFlag.BLOCK;
		if ("inline".equalsIgnoreCase(style.getDisplay())) //$NON-NLS-1$
		{
			if (context.isFirstInline()) {
				context.startInline();
				inlineFlag = InlineFlag.FIRST_INLINE;
			} else
				inlineFlag = InlineFlag.MIDDLE_INLINE;
		} else {
			adjustInline();
		}
		return inlineFlag;
	}

	protected String getBookmark(IContent content) {
		String bookmark = content.getBookmark();
		// birt use __TOC_X_X as bookmark for toc and thus it is not a
		// really bookmark
		if (bookmark == null || bookmark.startsWith("_TOC")) //$NON-NLS-1$
		{
			return null;
		}

		bookmark = OdfUtil.validBookmarkName(bookmark);

		// only write the end if the bookmark exists
		// only write the start if the bookmark didn't exist before
		if (bookmarks.contains(bookmark)) {
			return null;
		}

		return bookmark;
	}

	protected HyperlinkInfo getHyperlink(IContent content) {
		HyperlinkInfo hyperlink = null;
		IHyperlinkAction linkAction = content.getHyperlinkAction();
		if (linkAction != null) {
			String tooltip = linkAction.getTooltip();
			String bookmark = linkAction.getBookmark();
			switch (linkAction.getType()) {
			case IHyperlinkAction.ACTION_BOOKMARK:
				bookmark = bookmark.replaceAll(" ", "_"); //$NON-NLS-1$//$NON-NLS-2$
				hyperlink = new HyperlinkInfo(HyperlinkInfo.BOOKMARK, bookmark, tooltip);
				break;
			case IHyperlinkAction.ACTION_HYPERLINK:
			case IHyperlinkAction.ACTION_DRILLTHROUGH:
				String url = EmitterUtil.getHyperlinkUrl(linkAction, reportRunnable, actionHandler, reportContext);
				hyperlink = new HyperlinkInfo(HyperlinkInfo.HYPERLINK, url, tooltip);
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

	protected void writeText(int type, String txt, IContent content, InlineFlag inlineFlag, StyleEntry computedStyle,
			StyleEntry inlineStyle) {
		addContainerBookmark(content);
		addTableToc(content);

		HyperlinkInfo hyper = getHyperlink(content);
		// FIXME: should convert to inches, not points
		int paragraphWidth = (int) OdfUtil.twipToPt(context.getCurrentWidth());
		boolean rtl = content.isDirectionRTL();
		if (content instanceof TextContent) {
			TextFlag textFlag = TextFlag.START;
			String fontFamily = null;

			bodyWriter.writeContent(type, txt, computedStyle, inlineStyle, fontFamily, hyper, inlineFlag, textFlag,
					paragraphWidth, rtl, containerBookmarks, tableTocs);
			if (inlineFlag == InlineFlag.BLOCK) {
				bodyWriter.writeContent(type, null, computedStyle, inlineStyle, fontFamily, hyper, inlineFlag,
						TextFlag.END, paragraphWidth, rtl, containerBookmarks, tableTocs);
			}
		} else {
			bodyWriter.writeContent(type, txt, computedStyle, inlineStyle, computedStyle.getStyle().getFontFamily(),
					hyper, inlineFlag, TextFlag.WHOLE, paragraphWidth, rtl, containerBookmarks, tableTocs);
		}

		containerBookmarks.clear();
		tableTocs.clear();
	}

	private String getFontFamily(IStyle c_style, Chunk ch) {
		String fontFamily = null;
		FontInfo info = ch.getFontInfo();
		if (info != null) {
			fontFamily = info.getFontName();
		} else {
			fontFamily = c_style.getFontFamily();
		}
		return fontFamily;
	}

	private boolean isHidden(IContent content) {
		if (content != null) {
			IStyle style = content.getStyle();
			if (!IStyle.NONE_VALUE.equals(style.getProperty(IStyle.STYLE_DISPLAY))) {
				return isHiddenByVisibility(content);
			}
			return true;
		}
		return false;
	}

	/**
	 * if the content is hidden
	 * 
	 * @return
	 */
	private boolean isHiddenByVisibility(IContent content) {
		assert content != null;
		IStyle style = content.getStyle();
		String formats = style.getVisibleFormat();
		return contains(formats, getOutputFormat());
	}

	private boolean contains(String formats, String format) {
		if (formats != null && (formats.indexOf(EngineIRConstants.FORMAT_TYPE_VIEWER) >= 0
				|| formats.indexOf(BIRTConstants.BIRT_ALL_VALUE) >= 0 || formats.indexOf(format) >= 0)) {
			return true;
		}
		return false;
	}

	protected IStyle computeStyle(IStyle style) {
		if (styles.size() == 0) {
			return style;
		}

		for (int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++) {
			if (isInherityProperty(i)) {
				if (isNullValue(style.getProperty(i))) {
					style.setProperty(i, null);

					for (int p = styles.size() - 1; p >= 0; p--) {
						IStyle parent = styles.get(p);

						if (!isNullValue(parent.getProperty(i))) {
							style.setProperty(i, parent.getProperty(i));
							break;
						}
					}
				}
			}
		}
		return style;
	}

	protected boolean isNullValue(CSSValue value) {
		if (value == null) {
			return true;
		}

		if (value instanceof DataFormatValue) {
			return true;
		}

		if (value instanceof FloatValue) {
			return false;
		}
		String cssText = value.getCssText();
		return "none".equalsIgnoreCase(cssText) //$NON-NLS-1$
				|| "transparent".equalsIgnoreCase(cssText); //$NON-NLS-1$
	}

	private void writeHeaderFooter(IPageContent currentPage, boolean isLastPage) throws IOException, BirtException {
		MasterPageManager masterPageManager = context.getMasterPageManager();
		context.startMasterPage();
		// save the body writer
		IOdtWriter savedBodyWriter = bodyWriter;
		bodyWriter = masterPageWriter;

		SimpleMasterPageDesign master = (SimpleMasterPageDesign) previousPage.getGenerateBy();
		SimpleMasterPageDesign nextMaster = null;
		if (currentPage != null && !isLastPage) {
			nextMaster = (SimpleMasterPageDesign) currentPage.getGenerateBy();
		}

		String displayName = master.getName();
		if (masterPageManager.getInstanceNumber() > 1) {
			displayName += "-" + masterPageManager.getInstanceNumber(); //$NON-NLS-1$
		}

		masterPageWriter.startMasterPage(pageLayout, masterPageManager.getCurrentMasterPage(), displayName);

		if (previousPage.getPageHeader() != null
				&& (previousPage.getPageNumber() > 1l || master.isShowHeaderOnFirst())) {
			masterPageWriter.startHeader();

			contentVisitor.visitChildren(previousPage.getPageHeader(), null);
			masterPageWriter.endHeader();
		}
		if (previousPage.getPageFooter() != null && (!isLastPage || master.isShowFooterOnLast())) {
			masterPageWriter.startFooter();
			contentVisitor.visitChildren(previousPage.getPageFooter(), null);
			masterPageWriter.endFooter();
		}

		masterPageWriter.endMasterPage();

		// restore the regular body writer
		bodyWriter = savedBodyWriter;
		context.endMasterPage();

		if (nextMaster != null) {
			masterPageManager.newPage(nextMaster.getName());
		}
	}

	private boolean isInherityProperty(int propertyIndex) {
		return !NON_INHERITY_STYLES.contains(propertyIndex);
	}

	private double getCellWidth(double cellWidth, IStyle style) {
		double leftPadding = getPadding(style.getProperty(IStyle.STYLE_PADDING_LEFT));
		double rightPadding = getPadding(style.getProperty(IStyle.STYLE_PADDING_RIGHT));

		if (leftPadding > cellWidth) {
			leftPadding = 0;
		}

		if (rightPadding > cellWidth) {
			rightPadding = 0;
		}

		if ((leftPadding + rightPadding) > cellWidth) {
			rightPadding = 0;
		}

		return (cellWidth - leftPadding - rightPadding);
	}

	private double getPadding(CSSValue padding) {
		return OdfUtil.getDimensionValue(padding, context.getReportDpi());
	}

	private double[] computeTblColumnWidths(ITableContent table, double tblWidth) {
		final double coef = 1000.0;
		int colCount = table.getColumnCount();
		int[] tblColumns = new int[colCount];
		double[] doubleCols = new double[tblColumns.length];
		int count = 0;
		int total = 0;
		for (int i = 0; i < colCount; i++) {
			IColumn col = table.getColumn(i);
			if (col.getWidth() == null) {
				tblColumns[i] = -1;
				doubleCols[i] = -1.0;
				count++;
			} else {
				double val = OdfUtil.convertTo(col.getWidth(), tblWidth, context.getReportDpi());
				doubleCols[i] = val;
				tblColumns[i] = (int) (val * coef);
				total += tblColumns[i];
			}
		}

		if (table.getWidth() == null && count == 0) {
			return doubleCols;
		}

		// since resizeTableColumn only supports int, then convert the double
		// to an int containing a thousand of inches (to keep some precision)
		tblColumns = EmitterUtil.resizeTableColumn((int) (tblWidth * coef), tblColumns, count, total);
		for (int i = 0; i < tblColumns.length; i++) {
			doubleCols[i] = tblColumns[i] / coef;
		}
		return doubleCols;
	}

	private void addContainerBookmark(IContent content) {
		String bookmark = getBookmark(content);
		if (content != null) {
			containerBookmarks.add(bookmark);
		}
	}

	protected String getRootMime() {
		return MIME_TYPE;
	}
}
