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

package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.excel.BlankData;
import org.eclipse.birt.report.engine.emitter.excel.BlankData.Type;
import org.eclipse.birt.report.engine.emitter.excel.BookmarkDef;
import org.eclipse.birt.report.engine.emitter.excel.Data;
import org.eclipse.birt.report.engine.emitter.excel.DataCache;
import org.eclipse.birt.report.engine.emitter.excel.DataCache.DataFilter;
import org.eclipse.birt.report.engine.emitter.excel.DataCache.RowIndexAdjuster;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.emitter.excel.SheetData;
import org.eclipse.birt.report.engine.emitter.excel.StyleBuilder;
import org.eclipse.birt.report.engine.emitter.excel.StyleConstant;
import org.eclipse.birt.report.engine.emitter.excel.StyleEngine;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;
import org.eclipse.birt.report.model.api.util.ColorUtil;

public class Page {

	public static final float DEFAULT_ROW_HEIGHT = 15;
	protected static Logger logger = Logger.getLogger(Page.class.getName());

	protected AxisProcessor axis;
	protected DataCache currentCache;
	protected List<DataCache> caches = new ArrayList<DataCache>();
	private int maxCol;
	private StyleEngine styleEngine;
	private boolean outputInMasterPage = false;
	private String header;
	private String footer;
	private String orientation;
	private List<BookmarkDef> bookmarks = new ArrayList<BookmarkDef>();
	private String sheetName;
	private XlsContainer pageContainer;
	private int pageWidth;
	private int lastInRangeCoordinateIndex = -1;

	public Page(int contentWidth, StyleEngine styleEngine, int maxCol, String sheetName, XlsContainer pageContainer) {
		axis = new AxisProcessor();
		this.styleEngine = styleEngine;
		pageWidth = contentWidth;
		this.maxCol = maxCol;
		this.sheetName = sheetName;
		this.pageContainer = pageContainer;
	}

	public void addPageCoordinate() {
		axis.addCoordinate(pageWidth);
	}

	public void startPage(IPageContent pageContent) {
		orientation = capitalize(pageContent.getOrientation());
		if (needOutputInMasterPage(pageContent.getPageHeader())
				&& needOutputInMasterPage(pageContent.getPageFooter())) {
			outputInMasterPage = true;
			header = formatHeaderFooter(pageContent.getPageHeader());
			footer = formatHeaderFooter(pageContent.getPageFooter());
		}
	}

	public void startPage(Page page) {
		orientation = page.orientation;
		outputInMasterPage = page.outputInMasterPage;
		header = page.header;
		footer = page.footer;
	}

	public void setOutputInMasterPage(boolean outputInMasterPage) {
		this.outputInMasterPage = outputInMasterPage;
	}

	public boolean isOutputInMasterPage() {
		return outputInMasterPage;
	}

	public void initalize() {
		this.currentCache = createDataCache(0, maxCol);
		caches.add(currentCache);
	}

	protected DataCache createDataCache(int offset, int maxColumn) {
		return new DataCache(offset, maxColumn);
	}

	public void splitColumns(int startCoordinate, int endCoordinate, int[] columnStartCoordinates, boolean autoExtend) {
		if (axis.getColumnsCount() == 1) {
			for (int columnCoordinate : columnStartCoordinates) {
				axis.addCoordinate(columnCoordinate);
			}
			currentCache.insertColumns(columnStartCoordinates.length - 1);
			return;
		}

		lastInRangeCoordinateIndex = -1;
		int[] scale = axis.getColumnCoordinatesInRange(startCoordinate, endCoordinate);

		for (int i = 0; i < scale.length - 1; i++) {
			int startPosition = scale[i];
			int endPosition = scale[i + 1];

			int[] range = inRange(startPosition, endPosition, columnStartCoordinates);

			if (range.length > 0) {
				int pos = axis.getColumnIndexByCoordinate(startPosition);
				currentCache.insertColumns(pos, range.length);

				for (int j = 0; j < range.length; j++) {
					axis.addCoordinate(range[j]);
				}
			}
		}
		if (autoExtend) {
			int currentColumnCount = columnStartCoordinates.length;
			// The condition
			// "AxisProcessor.round(
			// columnStartCoordinates[lastInRangeCoordinateIndex + 1] ) ==
			// endCoordinate"
			// is not correct
			// In auto layout mode, the axis for endCoordinate may not exist.
			// For example, the endCoordinate is page width and no tables has
			// the same width as page. In such cases scale[scale.length-1]
			// becomes the actual endCoordinates and autoExtend is based on it
			if (lastInRangeCoordinateIndex < currentColumnCount - 1 && AxisProcessor
					.round(columnStartCoordinates[lastInRangeCoordinateIndex + 1]) == scale[scale.length - 1]) {
				lastInRangeCoordinateIndex++;
			}

			for (int i = lastInRangeCoordinateIndex + 1; i < currentColumnCount; i++) {
				axis.addCoordinate(columnStartCoordinates[i]);
			}
			int newColumnCount = currentColumnCount - (lastInRangeCoordinateIndex + 1);
			currentCache.insertColumns(newColumnCount);
		}
	}

	public String formatHeaderFooter(IContent content) {
		StringBuffer headfoot = new StringBuffer();
		if (content != null) {
			Collection list = content.getChildren();
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Object child = iter.next();
				if (child instanceof ITableContent) {
					headfoot.append(getTableValue((ITableContent) child));
				} else
					processText(headfoot, child);
			}
			return headfoot.toString();
		}
		return null;
	}

	private void processText(StringBuffer buffer, Object child) {
		if (child instanceof IAutoTextContent) {
			processTextStyle(buffer, ((IAutoTextContent) child).getComputedStyle());
			buffer.append(getAutoText((IAutoTextContent) child));
		} else if (child instanceof ITextContent) {
			processTextStyle(buffer, ((ITextContent) child).getComputedStyle());
			buffer.append(((ITextContent) child).getText());
		} else if (child instanceof IForeignContent) {
			processTextStyle(buffer, ((IForeignContent) child).getComputedStyle());
			buffer.append(((IForeignContent) child).getRawValue());
		}
	}

	private void processTextStyle(StringBuffer buffer, IStyle style) {
		outputStyleInQuote(style.getFontFamily(), style.getFontWeight(), style.getFontStyle(), buffer);

		String underLine = style.getTextUnderline();
		if (underLine != null && underLine.equalsIgnoreCase("underline")) {
			buffer.append("&U");
		}
		String lineTrough = style.getTextLineThrough();
		if (lineTrough != null && "line-through".equalsIgnoreCase(lineTrough)) {
			buffer.append("&S");
		}

		if (style.getProperty(IStyle.STYLE_FONT_SIZE) != null) {
			buffer.append("&");
			// float font size is not supported in Excel page header/footer.
			buffer.append((int) StyleBuilder.convertFontSize(style.getProperty(IStyle.STYLE_FONT_SIZE)));
		}
		// 34276: solve the font color issue in master page header in XLS/XLSX
		String color = style.getColor();
		if (color != null) {
			int value = ColorUtil.parseColor(color);
			if (value >= 0) {
				buffer.append("&K");
				buffer.append(Integer.toHexString(0x1000000 | value).substring(1));
			}
		}
	}

	private void outputStyleInQuote(String fontFamily, String bold, String italic, StringBuffer buffer) {
		if (needOutputInQuote(fontFamily, bold, italic)) {
			buffer.append("&\"");
			if (fontFamily != null) {
				buffer.append(ExcelUtil.getValue(fontFamily));
			}
			if (bold != null && "bold".equalsIgnoreCase(bold)) {
				if (fontFamily != null)
					buffer.append(",");
				buffer.append("Bold");
			}
			if (italic != null && "italic".equalsIgnoreCase(italic)) {
				if (bold != null && "bold".equalsIgnoreCase(bold)) {
					buffer.append(" ");
				} else if (fontFamily != null) {
					buffer.append(",");
				}
				buffer.append("Italic");
			}
			buffer.append("\"");
		}
	}

	private boolean needOutputInQuote(String fontFamily, String bold, String italic) {
		return fontFamily != null || (bold != null && "bold".equalsIgnoreCase(bold))
				|| (italic != null && "italic".equalsIgnoreCase(italic));
	}

	public String getTableValue(ITableContent table) {
		StringBuffer tableValue = new StringBuffer();
		Collection list = table.getChildren();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Object child = iter.next();
			tableValue.append(getRowValue((IRowContent) child));
		}
		return tableValue.toString();

	}

	public String getRowValue(IRowContent row) {
		StringBuffer rowValue = new StringBuffer();
		Collection list = row.getChildren();
		Iterator iter = list.iterator();
		int currentCellCount = 0;
		while (iter.hasNext()) {
			currentCellCount++;
			Object child = iter.next();
			switch (currentCellCount) {
			case 1:
				rowValue.append("&L");
				break;
			case 2:
				rowValue.append("&C");
				break;
			case 3:
				rowValue.append("&R");
				break;
			default:
				break;
			}
			rowValue.append(getCellValue((ICellContent) child));
		}
		return rowValue.toString();
	}

	public String getCellValue(ICellContent cell) {
		StringBuffer cellValue = new StringBuffer();
		Collection list = cell.getChildren();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			processText(cellValue, iter.next());
		}
		return cellValue.toString();
	}

	public void createNewCache() {
		this.currentCache = new DataCache(currentCache);
	}

	private String getAutoText(IAutoTextContent autoText) {
		String result = null;
		int type = autoText.getType();
		if (type == IAutoTextContent.PAGE_NUMBER) {
			result = "&P";
		} else if (type == IAutoTextContent.TOTAL_PAGE) {
			result = "&N";
		} else {
			result = autoText.getText();
		}
		return result;
	}

	private String capitalize(String orientation) {
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

	private int[] inRange(int start, int end, int[] data) {
		int[] range = new int[data.length];
		int count = 0;
		for (int i = 0; i < range.length; i++) {
			int value = AxisProcessor.round(data[i]);
			if ((value > start) && (value < end)) {
				range[count++] = value;
				lastInRangeCoordinateIndex = i;
			}
		}
		int[] result = new int[count];
		System.arraycopy(range, 0, result, 0, count);
		return result;
	}

	public void addData(SheetData data, XlsContainer container) {
		container.setEmpty(false);
		int col = axis.getColumnIndexByCoordinate(data.getStartX());
		int span = axis.getColumnIndexByCoordinate(data.getEndX()) - col;
		updataRowIndex(data, container);
		addDatatoCache(col, data);
		for (int i = col + 1; i < col + span; i++) {
			BlankData blankData = new BlankData(data);
			blankData.setType(Type.HORIZONTAL);
			addDatatoCache(i, blankData);
		}
		if (data.getDataType() == SheetData.IMAGE) {
			addEmptyData(data, container);
		}

		while (container != null) {
			if (container instanceof XlsCell) {
				XlsCell cell = (XlsCell) container;
				data.setRowSpanInDesign(cell.getRowSpan() - 1);
				break;
			} else {
				container = container.getParent();
			}
		}
	}

	public boolean isValid(SheetData data) {
		if (data.getStartX() == data.getEndX())
			return false;
		int col = axis.getColumnIndexByCoordinate(data.getStartX());
		if (col == -1 || col >= currentCache.getColumnCount())
			return false;
		return true;
	}

	public void addEmptyData(SheetData data, XlsContainer container) {
		int parentStartCoordinate = container.getSizeInfo().getStartCoordinate();
		int parentEndCoordinate = container.getSizeInfo().getEndCoordinate();
		int childStartCoordinate = data.getStartX();
		int childEndCoordinate = data.getEndX();
		if (childEndCoordinate < parentEndCoordinate) {
			StyleEntry style = container.getStyle();
			removeLeftBorder(style);
			int column = axis.getColumnIndexByCoordinate(childEndCoordinate);
			int num = axis.getColumnIndexByCoordinate(parentEndCoordinate) - column - 1;
			Data empty = createEmptyData(style);
			empty.setStartX(childEndCoordinate);
			empty.setEndX(parentEndCoordinate);
			empty.setRowIndex(data.getRowIndex());
			addDatatoCache(column, empty);
			addBlankData(column, num, empty);
		}
		if (childStartCoordinate > parentStartCoordinate) {
			StyleEntry style = container.getStyle();
			removeRightBorder(style);
			int column = axis.getColumnIndexByCoordinate(parentStartCoordinate);
			int num = column - axis.getColumnIndexByCoordinate(childStartCoordinate) - 1;
			Data empty = createEmptyData(style);
			empty.setStartX(parentStartCoordinate);
			empty.setEndX(childStartCoordinate);
			empty.setRowIndex(data.getRowIndex());
			addDatatoCache(column, empty);
			addBlankData(column - num - 1, num, empty);
		}
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
								"Excel page header or footer only accept a table no more than 1 row and 3 columns.");
						return false;
					}
					if (isEmbededTable((ITableContent) child)) {
						logger.log(Level.WARNING, "Excel page header and footer don't support embeded grid.");
						return false;
					}
				}
				if (isHtmlText(child)) {
					logger.log(Level.WARNING, "Excel page header and footer don't support html text.");
					return false;
				}
				if (child instanceof IImageContent) {
					logger.log(Level.WARNING, "Excel page header and footer don't support image.");
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

	private void addBlankData(int column, int num, Data empty) {
		for (int i = 1; i <= num; i++) {
			BlankData blank = new BlankData(empty);
			blank.setRowIndex(empty.getRowIndex());
			addDatatoCache(column + i, blank);
		}
	}

	private void addDatatoCache(int col, SheetData data) {
		currentCache.addData(col, data);
		BookmarkDef bookmark = data.getBookmark();
		addBookmark(bookmark);
	}

	public void addBookmark(BookmarkDef bookmark) {
		if (bookmark != null) {
			bookmarks.add(bookmark);
		}
	}

	protected void updataRowIndex(SheetData data, XlsContainer container) {
		int rowIndex = container.getEndRow() + 1;
		data.setRowIndex(rowIndex);
		container.setEndRow(rowIndex);
	}

	private void removeRightBorder(StyleEntry style) {
		style.setProperty(StyleConstant.BORDER_RIGHT_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_RIGHT_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_RIGHT_WIDTH_PROP, null);
	}

	private void removeLeftBorder(StyleEntry style) {
		style.setProperty(StyleConstant.BORDER_LEFT_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_LEFT_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_LEFT_WIDTH_PROP, null);
	}

	private void removeTopBorder(StyleEntry style) {
		style.setProperty(StyleConstant.BORDER_TOP_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_TOP_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_TOP_WIDTH_PROP, null);
	}

	private void removeBottomBorder(StyleEntry style) {
		style.setProperty(StyleConstant.BORDER_BOTTOM_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_BOTTOM_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_BOTTOM_WIDTH_PROP, null);
	}

	private void removeDiagonalLine(StyleEntry style) {
		style.setProperty(StyleConstant.BORDER_DIAGONAL_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_DIAGONAL_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_DIAGONAL_WIDTH_PROP, null);
		style.setProperty(StyleConstant.BORDER_ANTIDIAGONAL_COLOR_PROP, null);
		style.setProperty(StyleConstant.BORDER_ANTIDIAGONAL_STYLE_PROP, null);
		style.setProperty(StyleConstant.BORDER_ANTIDIAGONAL_WIDTH_PROP, null);
	}

	protected Data createData() {
		return new Data();
	}

	public Data createData(Object value, StyleEntry style) {
		return createData(value, style, 0);
	}

	protected Data createData(Object value, StyleEntry style, int rowSpanOfDesign) {
		int dataType = SheetData.STRING;
		if (style != null) {
			Object property = style.getProperty(StyleConstant.DATA_TYPE_PROP);
			if (property instanceof Integer) {
				dataType = (Integer) property;
			}
		}
		int styleId = styleEngine.getStyleId(style);
		return createData(value, styleId, dataType, rowSpanOfDesign);
	}

	public Data createData(Object value, int styleId, int dataType, int rowSpanOfDesign) {
		Data data = createData();
		data.setDataType(dataType);
		data.setValue(value);
		if (styleId > 0) {
			data.setStyleId(styleId);
		}
		data.setRowSpanInDesign(rowSpanOfDesign);
		return data;
	}

	public void addEmptyDataToContainer(XlsContainer child, XlsContainer parent) {
		ContainerSizeInfo childSizeInfo = child.getSizeInfo();
		int childStartCoordinate = childSizeInfo.getStartCoordinate();
		int childEndCoordinate = childSizeInfo.getEndCoordinate();
		ContainerSizeInfo parentSizeInfo = parent.getSizeInfo();
		int parentStartCoordinate = parentSizeInfo.getStartCoordinate();
		int parentEndCoordinate = parent.getSizeInfo().getEndCoordinate();

		if (childEndCoordinate < parentEndCoordinate) {
			StyleEntry style = parent.getStyle();
			removeLeftBorder(style);
			removeDiagonalLine(style);
			addEmptyDataToContainer(style, parent, childEndCoordinate, parentEndCoordinate - childEndCoordinate);
		}
		if (childStartCoordinate > parentStartCoordinate) {
			StyleEntry style = parent.getStyle();
			removeRightBorder(style);
			removeDiagonalLine(style);
			addEmptyDataToContainer(style, parent, parentStartCoordinate, childStartCoordinate - parentStartCoordinate);
		}
	}

	public void addEmptyDataToContainer(StyleEntry style, XlsContainer parent, int startCoordinate, int width) {
		Data data = createEmptyData(style);
		data.setStartX(startCoordinate);
		data.setEndX(startCoordinate + width);
		addData(data, parent);
	}

	public double[] getCoordinates() {
		int[] columnWidths = axis.getColumnWidths();
		int count = Math.min(columnWidths.length, maxCol);
		double[] coord = new double[count];
		for (int i = 0; i < count; i++) {
			coord[i] = columnWidths[i];
		}
		return coord;
	}

	public AxisProcessor getAxis() {
		return axis;
	}

	public int getStartColumn(SheetData data) {
		// Excel row index Starts From 1
		int start = axis.getColumnIndexByCoordinate(data.getStartX()) + 1;
		return Math.min(start, maxCol);
	}

	public int getEndColumn(SheetData data) {
		// Excel column index Starts From 1
		int end = axis.getColumnIndexByCoordinate(data.getEndX()) + 1;
		return Math.min(end, maxCol);
	}

	public void synchronize(float height, XlsContainer rowContainer) {
		ContainerSizeInfo rowSizeInfo = rowContainer.getSizeInfo();
		int startCoordinate = rowSizeInfo.getStartCoordinate();
		int endCoordinate = rowSizeInfo.getEndCoordinate();
		int startColumnIndex = axis.getColumnIndexByCoordinate(startCoordinate);
		int endColumnIndex = axis.getColumnIndexByCoordinate(endCoordinate);

		int maxRowIndex = 0;
		// contains the index of the last row in each column
		// this is used to track how much the columns must be filled using
		// rowspan
		int rowIndexes[] = new int[endColumnIndex - startColumnIndex];

		// populate rowIndexes and maxRowIndex (the max of rowIndexes)
		for (int currentColumnIndex = startColumnIndex; currentColumnIndex < endColumnIndex; currentColumnIndex++) {
			int rowIndex = currentCache.getMaxRowIndex(currentColumnIndex);
			SheetData lastData = currentCache.getColumnLastData(currentColumnIndex);
			rowIndexes[currentColumnIndex - startColumnIndex] = rowIndex;
			int span = lastData != null ? lastData.getRowSpanInDesign() : 0;
			if (span == 0 || (span == 1 && !isInContainer(lastData, rowContainer))) {
				maxRowIndex = maxRowIndex > rowIndex ? maxRowIndex : rowIndex;
			}
		}
		int startRowIndex = rowContainer.getEndRow();
		if (maxRowIndex <= startRowIndex) {
			maxRowIndex = startRowIndex + 1;
		}
		rowContainer.setEndRow(maxRowIndex);
		float resize = height / (maxRowIndex - startRowIndex);
		if (resize == 0f || resize > ExcelLayoutEngine.DEFAULT_ROW_HEIGHT) {
			for (int i = startRowIndex; i < maxRowIndex; i++) {
				currentCache.setRowHeight(i, resize);
			}
		}

		// for each column, process the cells and update the rowspan according
		// to the situation
		for (int currentColumnIndex = startColumnIndex; currentColumnIndex < endColumnIndex; currentColumnIndex++) {
			int rowspan = maxRowIndex - rowIndexes[currentColumnIndex - startColumnIndex];
			SheetData upstair = currentCache.getColumnLastData(currentColumnIndex);
			if (rowspan > 0) {
				if (upstair != null && canSpan(upstair, rowContainer, currentColumnIndex, endColumnIndex)) {
					Type blankType = Type.VERTICAL;
					if (upstair.isBlank()) {
						BlankData blankData = (BlankData) upstair;
						if (blankData.getType() == Type.VERTICAL) {
							upstair.setRowSpan(upstair.getRowSpan() + rowspan);
							if (!isInContainer(blankData, rowContainer)) {
								upstair.decreasRowSpanInDesign();
							}
						}
						blankType = blankData.getType();
					} else {
						upstair.setRowSpan(upstair.getRowSpan() + rowspan);
						if (!isInContainer(upstair, rowContainer)) {
							upstair.decreasRowSpanInDesign();
						}
					}

					// blank data is added to pad up the rowspan
					int rowIndex = upstair.getRowIndex();
					for (int p = 1; p <= rowspan; p++) {
						BlankData blank = new BlankData(upstair);
						blank.setRowIndex(rowIndex + p);
						blank.setType(blankType);
						currentCache.addData(currentColumnIndex, blank);
					}
				}
				// if can't span the cell
				else if (upstair != null && isInContainer(upstair, rowContainer.getParent())
						&& (!upstair.isBlank() || ((BlankData) upstair).getType() == Type.VERTICAL)) {
					// pad up with empty data cells, with updated border
					spanWithEmptyData(upstair, currentColumnIndex, rowspan);
				}
			} else if (upstair != null && upstair.getRowSpanInDesign() > 0 && !isInContainer(upstair, rowContainer)) {
				upstair.decreasRowSpanInDesign();
			}
		}
	}

	/**
	 * Expand the cell of the passed sheet data. Creates empty data entries to
	 * cover the given rowspan and updates the border accordingly.
	 * 
	 * @param sheetData sheet data
	 * @param currentColumnIndex current column index
	 * @param rowspan number of cells to which to expand the cell
	 */
	private void spanWithEmptyData(SheetData sheetData, int currentColumnIndex, int rowspan) {
		SheetData ref = sheetData;
		while (ref.isBlank()) {
			ref = ((BlankData) ref).getData();
			if (ref == null) {
				// no real data available
				return;
			}
		}

		// fills the spanned cell location with styled empty cells
		// and update the border
		int rowIndex = sheetData.getRowIndex();
		for (int p = 1; p <= rowspan; p++) {

			Data blank = createData();
			blank.setRowIndex(rowIndex + p);
			blank.setValue(""); //$NON-NLS-1$
			blank.setStartX(ref.getStartX());
			blank.setEndX(ref.getEndX());

			int styleId = sheetData.getStyleId();
			if (styleId != -1) {
				// expand the border to the blank cell
				// move the bottom border of the upstair cell to the blank one
				StyleEntry refStyle = styleEngine.getStyle(styleId);
				StyleEntry blankCellStyle = new StyleEntry(refStyle);

				// the bottom border and side borders have been copied through
				// the style copy
				// remove the top border of the blank cell
				removeTopBorder(blankCellStyle);
				blank.setStyleId(styleEngine.getStyleId(blankCellStyle));

				// now remove the bottom border of the upstair cell
				if (refStyle.getProperty(StyleConstant.BORDER_BOTTOM_STYLE_PROP) != null) {
					StyleEntry refNewStyle = new StyleEntry(refStyle);
					removeBottomBorder(refNewStyle);
					sheetData.setStyleId(styleEngine.getStyleId(refNewStyle));
				}

			}

			currentCache.addData(currentColumnIndex, blank);
		}
	}

	public Data createEmptyData(StyleEntry style) {
		return createData(null, style);
	}

	private boolean isInContainer(SheetData data, XlsContainer rowContainer) {
		return data.getRowIndex() > rowContainer.getStartRow();
	}

	private boolean canSpan(SheetData data, XlsContainer rowContainer, int currentColumn, int lastColumn) {
		SheetData realData = ExcelUtil.getRealData(data);
		if (realData == null)
			return false;
		if (!isInContainer(realData, rowContainer) && realData.getRowSpanInDesign() <= 0) {
			return false;
		}

		// Data can only span if the span doesn't conflict with some other
		// items.
		for (int i = currentColumn + 1; i < lastColumn; i++) {
			SheetData lastData = getColumnLastData(i);
			SheetData lastRealData = ExcelUtil.getRealData(lastData);

			// If there is some item under current data and would be
			// overridden if current data span rows, then current data can't
			// span.
			if (lastRealData == null || lastRealData.getRowIndex() <= realData.getRowIndex()) {
				continue;
			}
			if (realData.getEndX() > lastRealData.getStartX()) {
				return false;
			}
		}
		return true;
	}

	public void calculateRowHeight(SheetData[] rowData, boolean isAuto) {
		float rowHeight = 0;
		int rowIndex = getRowIndex(rowData);
		float lastRowHeight = rowIndex > 0 ? currentCache.getRowHeight(rowIndex - 1) : 0;
		boolean hasCurrentRowHeight = currentCache.hasRowHeight(rowIndex);
		if (!hasCurrentRowHeight || isAuto) {
			for (int i = 0; i < rowData.length; i++) {
				SheetData data = rowData[i];
				if (data != null) {
					if (data.isBlank()) {
						// if the data spans last row,then recalculate data
						// height.
						// if current row is the last row of real data, then
						// adjust
						// row height.
						BlankData blankData = (BlankData) data;
						if (blankData.getType() == Type.VERTICAL) {
							data.setHeight(data.getHeight() - lastRowHeight);
						}
					}
					SheetData realData = ExcelUtil.getRealData(data);
					if (realData != null) {
						int realDataRowEnd = realData.getRowIndex() + realData.getRowSpan();
						if (realDataRowEnd == data.getRowIndex()) {
							rowHeight = data.getHeight() > rowHeight ? data.getHeight() : rowHeight;
						}
					}
				}
			}
			currentCache.setRowHeight(rowIndex, rowHeight);
		}
	}

	public SheetData getColumnLastData(int column) {
		return currentCache.getColumnLastData(column);
	}

	public Iterator<SheetData[]> getRowIterator() {
		return currentCache.getRowIterator();
	}

	public Iterator<SheetData[]> getRowIterator(DataFilter filter, RowIndexAdjuster rowIndexAdjuster) {
		return currentCache.getRowIterator(filter, rowIndexAdjuster);
	}

	public float getRowHeight(int rowIndex) {
		return currentCache.getRowHeight(rowIndex);
	}

	public List<BookmarkDef> getBookmarks() {
		return bookmarks;
	}

	private int getRowIndex(SheetData[] rowData) {
		for (int j = 0; j < rowData.length; j++) {
			SheetData data = rowData[j];
			if (data != null) {
				return data.getRowIndex() - 1;
			}
		}
		return 0;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getFooter() {
		return footer;
	}

	public String getOrientation() {
		return orientation;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public XlsContainer getPageContainer() {
		return pageContainer;
	}

	/**
	 * Clears the cache.
	 */
	public void clearCache() {
		currentCache = null;
		caches = null;
		bookmarks = null;
	}

	public void finish() {
		for (BookmarkDef bookmark : bookmarks) {
			bookmark.setSheetName(sheetName);

			// Transform column coordinate to column index.
			bookmark.setStartColumn(axis.getColumnIndexByCoordinate(bookmark.getStartColumn()) + 1);
			int endColumn = bookmark.getEndColumn();
			if (endColumn != -1) {
				bookmark.setEndColumn(axis.getColumnIndexByCoordinate(endColumn));
			}
		}
	}
}
