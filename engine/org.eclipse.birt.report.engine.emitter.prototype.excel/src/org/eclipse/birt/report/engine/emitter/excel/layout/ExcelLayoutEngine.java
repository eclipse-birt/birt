/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.awt.Color;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.excel.BookmarkDef;
import org.eclipse.birt.report.engine.emitter.excel.Data;
import org.eclipse.birt.report.engine.emitter.excel.DataCache.DataFilter;
import org.eclipse.birt.report.engine.emitter.excel.DataCache.RowIndexAdjuster;
import org.eclipse.birt.report.engine.emitter.excel.DateTimeUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelWriter;
import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.IExcelWriter;
import org.eclipse.birt.report.engine.emitter.excel.ImageData;
import org.eclipse.birt.report.engine.emitter.excel.NumberFormatValue;
import org.eclipse.birt.report.engine.emitter.excel.RowData;
import org.eclipse.birt.report.engine.emitter.excel.SheetData;
import org.eclipse.birt.report.engine.emitter.excel.StyleBuilder;
import org.eclipse.birt.report.engine.emitter.excel.StyleConstant;
import org.eclipse.birt.report.engine.emitter.excel.StyleEngine;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.engine.util.FlashFile;

import com.ibm.icu.util.ULocale;

public class ExcelLayoutEngine {

	protected static Logger logger = Logger.getLogger(ExcelLayoutEngine.class.getName());

	public static final float DEFAULT_ROW_HEIGHT = 15;

	// Excel 2007 can support 1048576 rows and 16384 columns.
	private int autoBookmarkIndex = 0;

	public static final String AUTO_GENERATED_BOOKMARK = "auto_generated_bookmark_";

	public final static int MAX_ROW_OFFICE2007 = 1048576;

	public final static int MAX_COL_OFFICE2007 = 16384;

	public final static int MAX_ROW_OFFICE2003 = 65535;

	public final static int MAX_COLUMN_OFFICE2003 = 256;

	protected int maxRow = MAX_ROW_OFFICE2003;

	protected int maxCol = MAX_COLUMN_OFFICE2003;

	/**
	 * Bookmarks linked by existing element but not defined on any elements yet.
	 */
	private HashMap<String, String> cachedBookmarks = new HashMap<String, String>();

	protected StyleEngine engine;

	private Stack<XlsContainer> containers = new Stack<XlsContainer>();

	private Stack<XlsTable> tables = new Stack<XlsTable>();

	protected ExcelContext context = null;

	private String messageFlashObjectNotSupported;
	private String messageReportItemNotSupported;

	private ULocale locale;
	private HashMap<String, BookmarkDef> bookmarks = new HashMap<String, BookmarkDef>();
	protected Stack<Boolean> rowVisibilities = new Stack<Boolean>();
	protected Page page;
	protected IExcelWriter writer;
	protected ContentEmitterVisitor contentVisitor;

	private HashMap<String, Image> imageCache = new HashMap<String, Image>();
	// We only needs to apply page width when first non-auto-extend element is
	// output.
	protected boolean pageWidthApplied = false;

	public ExcelLayoutEngine(ExcelContext context, ContentEmitterVisitor contentVisitor) {
		this.context = context;
		this.locale = context.getLocale();
		EngineResourceHandle resourceHandle = new EngineResourceHandle(locale);
		messageFlashObjectNotSupported = resourceHandle.getMessage(MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT);
		messageReportItemNotSupported = resourceHandle.getMessage(MessageConstants.REPORT_ITEM_NOT_SUPPORTED_PROMPT);
		this.contentVisitor = contentVisitor;
	}

	protected void createWriter() {
		writer = new ExcelWriter(context);
	}

	public void initalize(IStyle style) {
		setCacheSize();
		engine = new StyleEngine(this);
		createWriter();
	}

	private void initializePage(IPageContent pageContent) {
		context.parsePageSize(pageContent);
		ContainerSizeInfo rule = new ContainerSizeInfo(0, context.getContentWidth());
		IStyle pageStyle = pageContent.getComputedStyle();
		containers.push(createContainer(rule, pageStyle, null));
	}

	protected void createPage(XlsContainer pageContainer) {
		page = new Page(context.getContentWidth(), engine, maxCol, context.getSheetName(), pageContainer);
		page.initalize();
		context.setPage(page);
	}

	private void setCacheSize() {
		if (context.getOfficeVersion().equals("office2007")) {
			maxCol = MAX_COL_OFFICE2007;
		}
	}

	public void processForeign(IForeignContent foreign, HyperlinkDef link) throws BirtException {
		addForeignContainer(foreign.getComputedStyle(), link);
		contentVisitor.visitChildren(foreign, null);
		endContainer();
	}

	public XlsContainer getCurrentContainer() {
		return (XlsContainer) containers.peek();
	}

	public Stack<XlsContainer> getContainers() {
		return containers;
	}

	public void startPage(IPageContent pageContent) throws BirtException {
		if (page == null || context.isEnableMultipleSheet()) {
			// intializePage method recalculate page style and page size. So it
			// only invoked when a new page is started, and not invoked in
			// outputDataIfBufferIsFull().
			initializePage(pageContent);
			newPage();
		}
		page.startPage(pageContent);
		XlsContainer topContainer = containers.peek();
		topContainer.setStyle(StyleBuilder.createStyleEntry(pageContent.getComputedStyle()));
		if (!page.isOutputInMasterPage() && pageContent.getPageHeader() != null) {
			contentVisitor.visitChildren(pageContent.getPageHeader(), null);
		}
	}

	private void newPage() {
		// The entrance to invoke Page constructor which will construct new axis
		// in turn. Needs to reset the pageWidthApplied flag here
		pageWidthApplied = false;
		createPage(containers.get(0));
		for (XlsTable table : tables) {
			splitColumns(table.getColumnsInfo(), table.getParent().getSizeInfo());
		}
		resetContainers();
	}

	public void endPage(IPageContent pageContent) throws BirtException {
		IContent footer = pageContent.getPageFooter();
		if (!page.isOutputInMasterPage() && footer != null) {
			contentVisitor.visitChildren(footer, null);
		}

		// update sheet name to page label, if necessary
		Object pageLabelObj = context.getReportContext().getPageVariable(IReportContext.PAGE_VAR_PAGE_LABEL);
		if (pageLabelObj instanceof String) {
			String pageLabel = (String) pageLabelObj;
			pageLabel = ExcelUtil.getValidSheetName(pageLabel);
			page.setSheetName(pageLabel);
		}

		outputSheet(page);
		containers.clear();
		page = null;
	}

	public void startTable(ITableContent table) {
		XlsContainer currentContainer = getCurrentContainer();
		if (currentContainer == null) {
			addContainer(null);
			tables.push(null);
			return;
		}
		ContainerSizeInfo sizeInfo = currentContainer.getSizeInfo();
		int width = sizeInfo.getWidth();
		ColumnsInfo info = null;
		int dpi = context.getDpi();
		if (context.isAutoLayout()) {
			info = LayoutUtil.createTable(table, width, dpi, autoExtend());
		} else {
			int[] columns = LayoutUtil.createFixedTable(table, LayoutUtil.getElementWidth(table, width, dpi), dpi);
			info = new ColumnsInfo(columns);
		}
		String caption = table.getCaption();
		if (caption != null) {
			addCaption(caption, table.getComputedStyle());
		}
		addTable(table, info, sizeInfo);
	}

	private boolean autoExtend() {
		return isTop() && context.isAutoLayout();
	}

	protected boolean isTop() {
		return containers.size() == 1;
	}

	public void addTable(IContainerContent content, ColumnsInfo columns, ContainerSizeInfo size) {
		IStyle style = content.getComputedStyle();
		XlsContainer currentContainer = getCurrentContainer();
		if (currentContainer == null) {
			addContainer(null);
			tables.push(null);
			return;
		}
		if (!autoExtend()) {
			applyPageWidth();
		}
		ContainerSizeInfo parentSizeInfo = currentContainer.getSizeInfo();
		int[] columnStartCoordinates = splitColumns(columns, parentSizeInfo, autoExtend());
		createTable(columns, style, currentContainer, columnStartCoordinates, content.getBookmark());
	}

	protected int[] splitColumns(ColumnsInfo columnsInfo, ContainerSizeInfo parentSizeInfo) {
		return splitColumns(columnsInfo, parentSizeInfo, false);
	}

	protected int[] splitColumns(ColumnsInfo columnsInfo, ContainerSizeInfo parentSizeInfo, boolean autoExtend) {
		int startCoordinate = parentSizeInfo.getStartCoordinate();
		int endCoordinate = parentSizeInfo.getEndCoordinate();

		int[] columnStartCoordinates = calculateColumnCoordinates(columnsInfo, startCoordinate, endCoordinate,
				autoExtend);

		page.splitColumns(startCoordinate, endCoordinate, columnStartCoordinates, autoExtend);
		return columnStartCoordinates;
	}

	private boolean isRightAligned(XlsContainer currentContainer) {
		boolean isRightAligned = false;
		String align = (String) currentContainer.getStyle().getProperty(StyleConstant.H_ALIGN_PROP);
		isRightAligned = "Right".equalsIgnoreCase(align);
		return isRightAligned;
	}

	private void createTable(ColumnsInfo tableInfo, IStyle style, XlsContainer currentContainer,
			int[] columnStartCoordinates, String bookmark) {
		int leftCordinate = columnStartCoordinates[0];
		int width = columnStartCoordinates[columnStartCoordinates.length - 1] - leftCordinate;
		ContainerSizeInfo sizeInfo = new ContainerSizeInfo(leftCordinate, width);
		StyleEntry styleEntry = engine.createEntry(sizeInfo, style, getParentStyle());
		XlsTable table = new XlsTable(tableInfo, styleEntry, sizeInfo, currentContainer);
		table.setBookmark(bookmark);
		tables.push(table);
		addContainer(table);
	}

	protected StyleEntry getParentStyle() {
		return getParentStyle(getCurrentContainer());
	}

	private int[] calculateColumnCoordinates(ColumnsInfo table, int startCoordinate, int endCoordinate,
			boolean autoExtend) {
		XlsContainer currentContainer = getCurrentContainer();
		if (table == null) {
			return new int[] { startCoordinate };
		}
		int columnCount = table.getColumnCount();
		int totalWidth = 0;
		for (int i = 0; i < columnCount; i++) {
			totalWidth += table.getColumnWidth(i);
		}
		boolean overflow = totalWidth > endCoordinate - startCoordinate;

		int[] columnStartCoordinates = new int[columnCount + 1];

		// If right aligned, need to extend and the total column width exceeds
		// the width of parent, the coordinates can be calculated same as left
		// aligned.
		if (isRightAligned(currentContainer) && !(overflow && autoExtend)) {
			columnStartCoordinates[columnCount] = endCoordinate;
			for (int i = columnCount - 1; i >= 0; i--) {
				columnStartCoordinates[i] = Math.max(startCoordinate,
						columnStartCoordinates[i + 1] - table.getColumnWidth(i));
			}
		} else {
			columnStartCoordinates[0] = startCoordinate;
			for (int i = 1; i <= columnCount; i++) {
				if (!autoExtend && (columnStartCoordinates[i - 1] + table.getColumnWidth(i - 1)) > endCoordinate) {
					columnStartCoordinates[i] = endCoordinate;
				} else
					columnStartCoordinates[i] = columnStartCoordinates[i - 1] + table.getColumnWidth(i - 1);
			}
		}
		return columnStartCoordinates;
	}

	public void addCell(int col, int colSpan, int rowSpan, IStyle style) {
		XlsTable table = tables.peek();
		ContainerSizeInfo cellSizeInfo = table.getColumnSizeInfo(col, colSpan);
		if (cellSizeInfo == null) {
			addContainer(null);
			return;
		}
		XlsCell cell = new XlsCell(engine.createEntry(cellSizeInfo, style, getParentStyle()), cellSizeInfo,
				getCurrentContainer(), rowSpan);
		addContainer(cell);
	}

	private boolean isHidden(IContent content) {
		if (content != null) {
			IStyle style = content.getStyle();
			if (IStyle.NONE_VALUE.equals(style.getProperty(IStyle.STYLE_DISPLAY))) {
				return true;
			}
		}
		return false;
	}

	public void addCell(ICellContent cellcontent, int col, int colSpan, int rowSpan, IStyle style) {
		if (!isHidden(cellcontent)) {
			rowVisibilities.pop();
			rowVisibilities.push(true);
			if (getCurrentContainer() == null) {
				addContainer(null);
				return;
			}
			XlsTable table = tables.peek();
			ContainerSizeInfo cellSizeInfo = table.getColumnSizeInfo(col, colSpan);
			if (cellSizeInfo == null) {
				addContainer(null);
				return;
			}
			int diagonalNumber = cellcontent.getDiagonalNumber();
			StyleEntry cellStyleEntry = null;
			if (diagonalNumber != 0) {
				String diagonalColor = cellcontent.getDiagonalColor();
				String diagonalStyle = cellcontent.getDiagonalStyle();
				int diagonalWidth = PropertyUtil.getDimensionValue(cellcontent, cellcontent.getDiagonalWidth(),
						cellSizeInfo.getWidth());
				cellStyleEntry = engine.createCellEntry(cellSizeInfo, style, diagonalColor, diagonalStyle,
						diagonalWidth, getParentStyle());
			} else {
				cellStyleEntry = engine.createEntry(cellSizeInfo, style, getParentStyle());
			}
			XlsCell cell = new XlsCell(cellStyleEntry, cellSizeInfo, getCurrentContainer(), rowSpan);
			addContainer(cell);
		}
	}

	public void endCell(ICellContent cell) {
		if (!isHidden(cell)) {
			endNormalContainer();
		}
	}

	public void addRow(IStyle style, String bookmark) {
		rowVisibilities.push(false);
		XlsContainer parent = getCurrentContainer();
		if (parent == null) {
			addContainer(null);
			return;
		}
		ContainerSizeInfo sizeInfo = parent.getSizeInfo();
		XlsContainer container = createContainer(sizeInfo, style, parent);
		container.setEmpty(false);
		container.setBookmark(bookmark);
		addContainer(container);
	}

	public void endRow(float rowHeight) {
		if (rowVisibilities.pop()) {
			XlsContainer rowContainer = getCurrentContainer();
			page.synchronize(rowHeight, rowContainer);
		}
		endContainer();
	}

	public void endTable(IContent content) {
		if (!tables.isEmpty()) {
			tables.pop();
			endContainer();
		}
	}

	public void addForeignContainer(IStyle style, HyperlinkDef link) {
		XlsContainer parent = getCurrentContainer();
		if (parent == null) {
			addContainer(null);
			return;
		}
		if (isTop()) {
			applyPageWidth();
		}
		ContainerSizeInfo sizeInfo = parent.getSizeInfo();
		StyleEntry entry = engine.createEntry(sizeInfo, style, getParentStyle(parent));
		addContainer(new XlsContainer(entry, sizeInfo, parent));
	}

	private StyleEntry getParentStyle(XlsContainer parent) {
		return parent == null ? null : parent.getStyle();
	}

	private void addContainer(XlsContainer child) {
		if (child != null) {
			XlsContainer parent = child.getParent();
			if (parent instanceof XlsCell) {
				page.addEmptyDataToContainer(child, parent);
			}
			if (parent != null) {
				parent.setEmpty(false);
			}
		}
		containers.push(child);
	}

	public void endListBandContainer() {
		XlsContainer container = getCurrentContainer();
		if (container != null) {
			if (!container.isEmpty()) {
				engine.applyContainerBottomStyle(container, page);
			}
			setParentContainerIndex();
		}
		containers.pop();
	}

	public void endContainer() {
		if (getCurrentContainer() == null) {
			containers.pop();
			return;
		}
		setParentContainerIndex();
		endNormalContainer();
	}

	private void setParentContainerIndex() {
		XlsContainer container = getCurrentContainer();
		XlsContainer parent = container.getParent();
		if (parent != null)
			parent.setEndRow(container.getEndRow());
	}

	public void endNormalContainer() {
		XlsContainer container = getCurrentContainer();
		if (container != null) {
			if (container.isEmpty()) {
				Data data = page.createEmptyData(container.getStyle());
				ContainerSizeInfo containerSize = container.getSizeInfo();
				data.setStartX(containerSize.getStartCoordinate());
				data.setEndX(containerSize.getEndCoordinate());
				addData(data, container);

				// If an empty data is added into container, the index of parent
				// should be updated. For cell, the index will be updated when
				// row is ended.
				if (!(container instanceof XlsCell)) {
					setParentContainerIndex();
				}
			}
			engine.applyContainerBottomStyle(container, page);
			createBookmark(container);
		}
		containers.pop();
	}

	private void createBookmark(XlsContainer container) {
		BookmarkDef bookmark = getBookmark(container.getBookmark());
		if (bookmark != null) {
			bookmark.setSheetName(context.getSheetName());
			bookmark.setStartRow(container.getStartRow() + 1);
			bookmark.setEndRow(container.getEndRow());
			ContainerSizeInfo size = container.getSizeInfo();
			bookmark.setStartColumn(size.getStartCoordinate());
			bookmark.setEndColumn(size.getEndCoordinate());
			page.addBookmark(bookmark);
		}
	}

	public Data addData(Object value, IStyle style, HyperlinkDef link, String bookmark, float height) {
		return addData(value, style, link, bookmark, null, height);
	}

	public Data addData(Object value, IStyle style, HyperlinkDef link, String bookmark, String locale, float height) {
		XlsContainer container = getCurrentContainer();
		if (container == null) {
			return null;
		}
		ContainerSizeInfo containerSize = container.getSizeInfo();
		StyleEntry entry = engine.getStyle(style, containerSize, getParentStyle(container));
		setDataType(entry, value, locale);
		setlinkStyle(entry, link);
		Data data = page.createData(value, entry);
		data.setHeight(height);
		data.setHyperlinkDef(link);
		data.setBookmark(getBookmark(bookmark));
		data.setStartX(containerSize.getStartCoordinate());
		data.setEndX(containerSize.getEndCoordinate());
		addData(data, container);
		return data;
	}

	protected void setlinkStyle(StyleEntry entry, HyperlinkDef link) {
		if (link != null) {
			Color color = link.getColor();
			if (color != null) {
				entry.setProperty(StyleConstant.COLOR_PROP, color);
			} else {
				entry.setProperty(StyleConstant.COLOR_PROP, StyleConstant.HYPERLINK_COLOR);
			}
			entry.setProperty(StyleConstant.TEXT_UNDERLINE_PROP, true);
			entry.setIsHyperlink(true);
		}
	}

	public void addImageData(IImageContent image, IStyle style, HyperlinkDef link, String bookmark) {
		XlsContainer container = getCurrentContainer();
		if (container == null) {
			return;
		}
		ContainerSizeInfo parentSizeInfo = container.getSizeInfo();
		int imageWidthDpi = context.getDpi();
		int imageHeightDpi = context.getDpi();
		int imageHeight;
		int imageWidth;
		byte[] imageData = null;
		try {
			Image imageInfo = null;
			if (image.getURI() != null && image.getURI().length() != 0) {
				imageInfo = imageCache.get(image.getURI());
				if (imageInfo == null) {
					// cache the image with URI
					imageInfo = EmitterUtil.parseImage(image, image.getImageSource(), image.getURI(),
							image.getMIMEType(), image.getExtension());
					imageCache.put(image.getURI(), imageInfo);
				}
			} else {
				imageInfo = EmitterUtil.parseImage(image, image.getImageSource(), image.getURI(), image.getMIMEType(),
						image.getExtension());
			}
			imageData = imageInfo.getData();
			int[] imageSize = getImageSize(image, imageInfo, parentSizeInfo, imageWidthDpi, imageHeightDpi);
			imageHeight = imageSize[0];
			imageWidth = imageSize[1];
		} catch (IOException ex) {
			imageHeight = LayoutUtil.getImageHeight(image.getHeight(), 0, imageHeightDpi);
			imageWidth = LayoutUtil.getImageWidth(image.getWidth(), parentSizeInfo.getWidth(), 0, imageWidthDpi);
		}

		StyleEntry parentStyle = getParentStyle(container);
		boolean isCenterAligned = false;
		if (parentStyle != null
				&& "center".equalsIgnoreCase((String) parentStyle.getProperty(StyleConstant.H_ALIGN_PROP))) {
			isCenterAligned = true;
		}
		ColumnsInfo imageColumnsInfo = LayoutUtil.createImage(imageWidth, parentSizeInfo.getWidth(), isCenterAligned);
		int[] imageCoordinates = splitColumns(imageColumnsInfo, parentSizeInfo);
		ContainerSizeInfo imageSize = new ContainerSizeInfo(imageCoordinates[imageCoordinates.length > 2 ? 1 : 0],
				imageWidth);
		StyleEntry entry = engine.getStyle(style, imageSize, parentSizeInfo, getParentStyle(container));
		setlinkStyle(entry, link);
		SheetData data = createImageData(image, imageData, imageSize.getWidth(), imageHeight, entry, container);
		data.setHyperlinkDef(link);
		data.setBookmark(getBookmark(bookmark));
		data.setStartX(imageSize.getStartCoordinate());
		data.setEndX(imageSize.getEndCoordinate());
		addData(data, container);
	}

	private int[] getImageSize(IImageContent image, Image imageInfo, ContainerSizeInfo parentSizeInfo,
			int imageWidthDpi, int imageHeightDpi) {
		int imageHeight;
		int imageWidth;
		if (image.getWidth() == null && image.getHeight() == null) {
			int imageFileWidthDpi = imageInfo.getPhysicalWidthDpi() == -1 ? 0 : imageInfo.getPhysicalWidthDpi();
			int imageFileHeightDpi = imageInfo.getPhysicalHeightDpi() == -1 ? 0 : imageInfo.getPhysicalHeightDpi();
			imageWidthDpi = PropertyUtil.getImageDpi(image, imageFileWidthDpi, 0);
			imageHeightDpi = PropertyUtil.getImageDpi(image, imageFileHeightDpi, 0);
		}

		int imageInfoHeight = (int) (imageInfo.getHeight() * 1000 * ExcelUtil.INCH_PT / imageHeightDpi);
		int imageInfoWidth = (int) (imageInfo.getWidth() * 1000 * ExcelUtil.INCH_PT / imageWidthDpi);
		if (image.getWidth() == null && image.getHeight() != null) {
			imageHeight = LayoutUtil.getImageHeight(image.getHeight(), imageInfoHeight, imageHeightDpi);
			float scale = ((float) imageInfoHeight) / ((float) imageInfoWidth);
			imageWidth = (int) (imageHeight / scale);
		} else if (image.getHeight() == null && image.getWidth() != null) {
			imageWidth = LayoutUtil.getImageWidth(image.getWidth(), parentSizeInfo.getWidth(), imageInfoWidth,
					imageWidthDpi);
			float scale = ((float) imageInfoHeight) / ((float) imageInfoWidth);
			imageHeight = (int) (imageWidth * scale);
		} else {
			imageHeight = LayoutUtil.getImageHeight(image.getHeight(), imageInfoHeight, imageHeightDpi);
			imageWidth = LayoutUtil.getImageWidth(image.getWidth(), parentSizeInfo.getWidth(), imageInfoWidth,
					imageWidthDpi);
		}
		int[] imageSize = { imageHeight, imageWidth };
		return imageSize;
	}

	private SheetData createImageData(IImageContent image, byte[] imageData, int imageWidth, int imageHeight,
			StyleEntry entry, XlsContainer container) {
		int type = SheetData.IMAGE;
		entry.setProperty(StyleConstant.DATA_TYPE_PROP, type);
		String uri = image.getURI();
		String mimeType = image.getMIMEType();
		String extension = image.getExtension();
		String altText = image.getAltText();
		if (FlashFile.isFlash(mimeType, uri, extension)) {
			if (null == altText) {
				altText = messageFlashObjectNotSupported;
			}
			entry.setProperty(StyleConstant.DATA_TYPE_PROP, SheetData.STRING);
			return page.createData(altText, entry);
		}

		if (imageData != null) {
			return createData(image, imageData, imageWidth, imageHeight, entry, container, type);
		} else {
			if (null == altText) {
				altText = messageReportItemNotSupported;
			}
			entry.setProperty(StyleConstant.DATA_TYPE_PROP, SheetData.STRING);
			return page.createData(altText, entry);
		}
	}

	protected SheetData createData(IImageContent image, byte[] data, int imageWidth, int imageHeight, StyleEntry entry,
			XlsContainer container, int type) {
		int styleId = engine.getStyleId(entry);
		SheetData imageData = new ImageData(image, data, imageWidth, imageHeight, styleId, type, container);
		return imageData;
	}

	public Data addDateTime(Object txt, IStyle style, HyperlinkDef link, String bookmark, String dateTimeLocale,
			float height) {
		XlsContainer currentContainer = getCurrentContainer();
		if (currentContainer == null) {
			return null;
		}
		ContainerSizeInfo containerSize = currentContainer.getSizeInfo();
		StyleEntry entry = engine.getStyle(style, containerSize, getParentStyle(currentContainer));
		setlinkStyle(entry, link);
		Data data = null;

		IDataContent dataContent = (IDataContent) txt;
		Object value = dataContent.getValue();
		Date date = ExcelUtil.getDate(value);
		// If date time is before 1900, it must be output as string, otherwise,
		// excel can't format the date.
		if (date != null && ((date instanceof Time) || date.getYear() >= 0)) {
			data = createDateData(value, entry, style.getDateTimeFormat(), dateTimeLocale);
			data.setHeight(height);
			data.setBookmark(getBookmark(bookmark));
			data.setHyperlinkDef(link);
			data.setStartX(containerSize.getStartCoordinate());
			data.setEndX(containerSize.getEndCoordinate());
			addData(data, currentContainer);
			return data;
		} else {
			entry.setProperty(StyleConstant.DATA_TYPE_PROP, SheetData.STRING);
			return addData(dataContent.getText(), style, link, bookmark, dateTimeLocale, height);
		}
	}

	public void addCaption(String text, IStyle style) {
		XlsContainer container = getCurrentContainer();
		if (container == null) {
			return;
		}
		ContainerSizeInfo containerSize = container.getSizeInfo();
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry();
		entry.setProperty(StyleEntry.H_ALIGN_PROP, "Center");
		entry.setProperty(StyleEntry.FONT_SIZE_PROP,
				StyleBuilder.convertFontSize(style.getProperty(IStyle.STYLE_FONT_SIZE)));
		entry.setProperty(StyleEntry.DATA_TYPE_PROP, SheetData.STRING);
		Data data = page.createData(text, entry);
		data.setStartX(containerSize.getStartCoordinate());
		data.setEndX(containerSize.getEndCoordinate());

		addData(data, container);
	}

	public void addData(SheetData data, XlsContainer container) {
		if (getCurrentContainer() == null) {
			return;
		} else if (isTop()) {
			applyPageWidth();
		}
		if (page.isValid(data)) {
			// FIXME: there is a bug when this data is in middle of a row.
			outputDataIfBufferIsFull();
			page.addData(data, container);
		}
	}

	protected void applyPageWidth() {
		if (!pageWidthApplied) {
			page.addPageCoordinate();
			pageWidthApplied = true;
		}
	}

	private void setDataType(StyleEntry entry, Object value, String dataLocale) {
		ULocale locale = getLocale(dataLocale);
		setDataType(entry, value, locale);
	}

	private void setDataType(StyleEntry entry, Object value, ULocale locale) {
		int type = SheetData.STRING;
		if (SheetData.NUMBER == ExcelUtil.getType(value)) {
			NumberFormatValue numberFormat = (NumberFormatValue) entry.getProperty(StyleConstant.NUMBER_FORMAT_PROP);
			if (numberFormat != null) {
				String format = ExcelUtil.formatNumberPattern(numberFormat.getFormat(), locale);
				NumberFormatValue localizedFormat = null;
				if (format != null && !format.equals(numberFormat.getFormat())) {
					localizedFormat = NumberFormatValue.getInstance(format);
				} else {
					localizedFormat = numberFormat;
				}
				entry.setProperty(StyleConstant.NUMBER_FORMAT_PROP, localizedFormat);
			}
			type = SheetData.NUMBER;

		} else if (SheetData.DATE == ExcelUtil.getType(value)) {
			String format = ExcelUtil.getPattern(value, (String) entry.getProperty(StyleConstant.DATE_FORMAT_PROP));
			entry.setProperty(StyleConstant.DATE_FORMAT_PROP, format);
			type = Data.DATE;
		}

		entry.setProperty(StyleConstant.DATA_TYPE_PROP, type);
	}

	private Data createDateData(Object txt, StyleEntry entry, String timeFormat, String dlocale) {
		ULocale dateLocale = getLocale(dlocale);
		timeFormat = ExcelUtil.parse(txt, timeFormat, dateLocale);
		timeFormat = DateTimeUtil.formatDateTime(timeFormat, dateLocale);
		entry.setProperty(StyleConstant.DATE_FORMAT_PROP, timeFormat);
		entry.setProperty(StyleConstant.DATA_TYPE_PROP, SheetData.DATE);
		return page.createData(txt, entry);
	}

	private ULocale getLocale(String dlocale) {
		return dlocale == null ? locale : new ULocale(dlocale);
	}

	private void outputDataIfBufferIsFull() {
		if (getCurrentContainer().getEndRow() >= maxRow) {
			Page lastPage = page;
			outputSheet(page);
			newPage();
			page.startPage(lastPage);
			page.setHeader(null);
		}
	}

	public void outputSheet(Page page) {
		page.finish();
		try {
			outputCacheData(page);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		context.setSheetIndex(context.getSheetIndex() + 1);
	}

	private XlsContainer createContainer(ContainerSizeInfo sizeInfo, IStyle style, XlsContainer parent) {
		return new XlsContainer(engine.createEntry(sizeInfo, style, getParentStyle(parent)), sizeInfo, parent);
	}

	public Map<StyleEntry, Integer> getStyleMap() {
		return engine.getStyleIDMap();
	}

	public StyleEntry getStyle(int styleId) {
		return engine.getStyle(styleId);
	}

	public Page getPage() {
		return page;
	}

	public void end(IReportContent report) {
		// Make sure the engine already calculates all data in cache.
		// cacheBookmarks( );
		// complete( );
		try {
			// TODO: style ranges.
			// writer.start( report, engine.getStyleMap( ), engine
			// .getStyleRanges( ), engine.getAllBookmarks( ) );
			writer.start(report, getStyleMap(), getAllBookmarks());
			// outputCacheData( true );
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public void endWriter() {
		try {
			writer.end();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	protected void outputRowData(Page page, RowData rowData) throws IOException {
		writer.startRow(rowData.getHeight());
		SheetData[] datas = rowData.getRowdata();
		for (int i = 0; i < datas.length; i++) {
			SheetData data = datas[i];
			int start = page.getStartColumn(data);
			int end = page.getEndColumn(data);
			int span = Math.max(0, end - start - 1);
			outputData(page, data, start, span);
		}
		writer.endRow();
	}

	protected void outputData(Page page, SheetData data, int start, int span) throws IOException {
		writer.outputData(data, engine.getStyle(data.getStyleId()), start, span);
	}

	public void complete(Page page) {
		XlsContainer pageContainer = page.getPageContainer();
		engine.applyContainerBottomStyle(pageContainer, page);
		Iterator<SheetData[]> iterator = page.getRowIterator();
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				SheetData[] rowData = iterator.next();

				for (int j = 0; j < rowData.length; j++) {
					SheetData data = rowData[j];
					if (data == null || data.isBlank()) {
						continue;
					}

					HyperlinkDef hyperLink = data.getHyperlinkDef();
					if (hyperLink != null) {
						if (hyperLink.getType() == IHyperlinkAction.ACTION_BOOKMARK) {
							setLinkedBookmark(data, hyperLink);
						}
					}
				}
				page.calculateRowHeight(rowData, context.isRTL());
			}
		} else {
			ContainerSizeInfo containerSize = pageContainer.getSizeInfo();
			page.addEmptyDataToContainer(pageContainer.getStyle(), pageContainer, containerSize.getStartCoordinate(),
					containerSize.getWidth());
		}
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void outputCacheData(Page page) throws IOException {
		complete(page);
		Iterator<RowData> it = getIterator(page);
		if (it.hasNext()) {
			double[] coordinates = page.getCoordinates();
			writer.startSheet(coordinates, page.getHeader(), page.getFooter(), page.getSheetName());
			while (it.hasNext()) {
				outputRowData(page, it.next());
			}
			writer.endSheet(coordinates, page.getOrientation(), context.getPageWidth(), context.getPageHeight(),
					context.getLeftMargin(), context.getRightMargin(), context.getTopMargin(),
					context.getBottomMargin());
		}
	}

	/**
	 * @param data
	 * @param hyperLink
	 */
	private void setLinkedBookmark(SheetData data, HyperlinkDef hyperLink) {
		String bookmarkName = hyperLink.getUrl();
		BookmarkDef linkedBookmark = bookmarks.get(bookmarkName);
		if (linkedBookmark != null) {
			data.setLinkedBookmark(linkedBookmark);
		} else {
			BookmarkDef newBookmark;
			if (ExcelUtil.isValidBookmarkName(bookmarkName))
				newBookmark = new BookmarkDef(bookmarkName);
			else {
				String generateBookmarkName = getGenerateBookmark(bookmarkName);
				newBookmark = new BookmarkDef(generateBookmarkName);
				cachedBookmarks.put(bookmarkName, generateBookmarkName);
			}
			data.setLinkedBookmark(newBookmark);
		}
	}

	public Stack<XlsTable> getTable() {
		return tables;
	}

	public void addContainerStyle(IStyle computedStyle) {
		engine.addContainderStyle(computedStyle, getParentStyle());
	}

	public void removeContainerStyle() {
		engine.removeForeignContainerStyle();
	}

	public void resetContainers() {
		for (XlsContainer container : containers) {
			container.setEndRow(0);
			container.setStartRow(0);
		}
		for (XlsTable table : tables) {
			table.setEndRow(0);
		}
	}

	public ExcelLayoutEngineIterator getIterator(Page page) {
		return getIterator(page, null, null);
	}

	public ExcelLayoutEngineIterator getIterator(Page page, DataFilter filter, RowIndexAdjuster rowIndexAdjuster) {
		return new ExcelLayoutEngineIterator(page, filter, rowIndexAdjuster);
	}

	private class ExcelLayoutEngineIterator implements Iterator<RowData> {

		private Iterator<SheetData[]> rowIterator;
		private Page page;

		public ExcelLayoutEngineIterator(Page page, DataFilter filter, RowIndexAdjuster rowIndexAdjuster) {
			this.page = page;
			rowIterator = page.getRowIterator(filter, rowIndexAdjuster);
		}

		public boolean hasNext() {
			return rowIterator.hasNext();
		}

		public RowData next() {
			SheetData[] row = rowIterator.next();
			List<SheetData> data = new ArrayList<SheetData>();
			int width = Math.min(row.length, maxCol);
			int rowIndex = 0;
			for (int i = 0; i < width; i++) {
				SheetData d = row[i];
				if (d == null || d.isBlank()) {
					continue;
				}
				rowIndex = d.getRowIndex();
				data.add(row[i]);
			}
			SheetData[] rowdata = new SheetData[data.size()];
			double rowHeight = page.getRowHeight(rowIndex - 1);
			data.toArray(rowdata);
			return new RowData(page, rowdata, rowHeight);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public HashMap<String, BookmarkDef> getAllBookmarks() {
		return bookmarks;
	}

	/**
	 * @param bookmarkName
	 * @return generatedBookmarkName
	 */
	public String getGenerateBookmark(String bookmarkName) {
		String generatedName = cachedBookmarks.get(bookmarkName);
		return generatedName != null ? generatedName : AUTO_GENERATED_BOOKMARK + autoBookmarkIndex++;
	}

	public boolean isContainerVisible() {
		return getCurrentContainer() != null;
	}

	protected BookmarkDef getBookmark(String bookmarkName) {
		if (bookmarkName == null)
			return null;

		// if bookmark was already found before, skip it
		if (bookmarks.containsKey(bookmarkName)) {
			return null;
		}

		BookmarkDef bookmark = new BookmarkDef(bookmarkName);
		if (!ExcelUtil.isValidBookmarkName(bookmarkName)) {
			bookmark.setGeneratedName(getGenerateBookmark(bookmarkName));
		}
		bookmarks.put(bookmarkName, bookmark);

		// !( content.getBookmark( ).startsWith( "__TOC" ) ) )
		// bookmark starting with "__TOC" is not OK?
		return bookmark;
	}

}
