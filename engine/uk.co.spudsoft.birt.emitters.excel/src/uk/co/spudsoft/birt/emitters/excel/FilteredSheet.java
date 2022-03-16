/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 *     Steve Schafer - Upgrade to poi 4.1.1
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// import org.apache.poi.hssf.util.PaneInformation;
import org.apache.poi.ss.usermodel.AutoFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;

public class FilteredSheet implements Sheet {

	private Sheet sheet;
	private int minRow;
	private int maxRow;

	public FilteredSheet(Sheet sheet, int minRow, int maxRow) {
		this.sheet = sheet;
		this.minRow = minRow;
		this.maxRow = maxRow;
	}

	private class FilteredIterator implements Iterator<Row> {

		private Iterator<Row> iter;
		private int minRow;
		private int maxRow;
		private Row cur;

		public FilteredIterator(Iterator<Row> iter, int minRow, int maxRow) {
			this.iter = iter;
			this.minRow = minRow;
			this.maxRow = maxRow;
		}

		@Override
		public boolean hasNext() {
			if (iter.hasNext()) {
				cur = iter.next();
				while (cur.getRowNum() < minRow) {
					if (!iter.hasNext()) {
						return false;
					}
					cur = iter.next();
				}
				if (cur.getRowNum() > maxRow) {
					return false;
				}
				return true;
			}
			return false;
		}

		@Override
		public Row next() {
			return cur;
		}

		@Override
		public void remove() {
			iter.remove();
		}

	}

	@Override
	public CellRangeAddress getRepeatingColumns() {
		return sheet.getRepeatingColumns();
	}

	@Override
	public CellRangeAddress getRepeatingRows() {
		return sheet.getRepeatingRows();
	}

	@Override
	public void setRepeatingColumns(CellRangeAddress arg0) {
		sheet.setRepeatingColumns(arg0);
	}

	@Override
	public void setRepeatingRows(CellRangeAddress arg0) {
		sheet.setRepeatingRows(arg0);
	}

	@Override
	public Iterator<Row> iterator() {
		return rowIterator();
	}

	@Override
	public Row createRow(int rownum) {
		return sheet.createRow(rownum);
	}

	@Override
	public void removeRow(Row row) {
		sheet.removeRow(row);
	}

	@Override
	public Row getRow(int rownum) {
		return sheet.getRow(rownum);
	}

	@Override
	public int getPhysicalNumberOfRows() {
		return sheet.getPhysicalNumberOfRows();
	}

	@Override
	public int getFirstRowNum() {
		return Math.max(minRow, sheet.getFirstRowNum());
	}

	@Override
	public int getLastRowNum() {
		return Math.min(maxRow, sheet.getLastRowNum());
	}

	@Override
	public void setColumnHidden(int columnIndex, boolean hidden) {
		sheet.setColumnHidden(columnIndex, hidden);
	}

	@Override
	public boolean isColumnHidden(int columnIndex) {
		return sheet.isColumnHidden(columnIndex);
	}

	@Override
	public void setRightToLeft(boolean value) {
		sheet.setRightToLeft(value);
	}

	@Override
	public boolean isRightToLeft() {
		return sheet.isRightToLeft();
	}

	@Override
	public void setColumnWidth(int columnIndex, int width) {
		sheet.setColumnWidth(columnIndex, width);
	}

	@Override
	public int getColumnWidth(int columnIndex) {
		return sheet.getColumnWidth(columnIndex);
	}

	@Override
	public void setDefaultColumnWidth(int width) {
		sheet.setDefaultColumnWidth(width);
	}

	@Override
	public int getDefaultColumnWidth() {
		return sheet.getDefaultColumnWidth();
	}

	@Override
	public short getDefaultRowHeight() {
		return sheet.getDefaultRowHeight();
	}

	@Override
	public float getDefaultRowHeightInPoints() {
		return sheet.getDefaultRowHeightInPoints();
	}

	@Override
	public void setDefaultRowHeight(short height) {
		sheet.setDefaultRowHeight(height);
	}

	@Override
	public void setDefaultRowHeightInPoints(float height) {
		sheet.setDefaultRowHeightInPoints(height);
	}

	@Override
	public CellStyle getColumnStyle(int column) {
		return sheet.getColumnStyle(column);
	}

	@Override
	public int addMergedRegion(CellRangeAddress region) {
		return sheet.addMergedRegion(region);
	}

	@Override
	public void setVerticallyCenter(boolean value) {
		sheet.setVerticallyCenter(value);
	}

	@Override
	public void setHorizontallyCenter(boolean value) {
		sheet.setHorizontallyCenter(value);
	}

	@Override
	public boolean getHorizontallyCenter() {
		return sheet.getHorizontallyCenter();
	}

	@Override
	public boolean getVerticallyCenter() {
		return sheet.getVerticallyCenter();
	}

	@Override
	public void removeMergedRegion(int index) {
		sheet.removeMergedRegion(index);
	}

	@Override
	public int getNumMergedRegions() {
		return sheet.getNumMergedRegions();
	}

	@Override
	public CellRangeAddress getMergedRegion(int index) {
		return sheet.getMergedRegion(index);
	}

	@Override
	public Iterator<Row> rowIterator() {
		return new FilteredIterator(sheet.rowIterator(), minRow, maxRow);
	}

	@Override
	public void setForceFormulaRecalculation(boolean value) {
		sheet.setForceFormulaRecalculation(value);
	}

	@Override
	public boolean getForceFormulaRecalculation() {
		return sheet.getForceFormulaRecalculation();
	}

	@Override
	public void setAutobreaks(boolean value) {
		sheet.setAutobreaks(value);
	}

	@Override
	public void setDisplayGuts(boolean value) {
		sheet.setDisplayGuts(value);
	}

	@Override
	public void setDisplayZeros(boolean value) {
		sheet.setDisplayZeros(value);
	}

	@Override
	public boolean isDisplayZeros() {
		return sheet.isDisplayZeros();
	}

	@Override
	public void setFitToPage(boolean value) {
		sheet.setFitToPage(value);
	}

	@Override
	public void setRowSumsBelow(boolean value) {
		sheet.setRowSumsBelow(value);
	}

	@Override
	public void setRowSumsRight(boolean value) {
		sheet.setRowSumsRight(value);
	}

	@Override
	public boolean getAutobreaks() {
		return sheet.getAutobreaks();
	}

	@Override
	public boolean getDisplayGuts() {
		return sheet.getDisplayGuts();
	}

	@Override
	public boolean getFitToPage() {
		return sheet.getFitToPage();
	}

	@Override
	public boolean getRowSumsBelow() {
		return sheet.getRowSumsBelow();
	}

	@Override
	public boolean getRowSumsRight() {
		return sheet.getRowSumsRight();
	}

	@Override
	public boolean isPrintGridlines() {
		return sheet.isPrintGridlines();
	}

	@Override
	public void setPrintGridlines(boolean show) {
		sheet.setPrintGridlines(show);
	}

	@Override
	public PrintSetup getPrintSetup() {
		return sheet.getPrintSetup();
	}

	@Override
	public Header getHeader() {
		return sheet.getHeader();
	}

	@Override
	public Footer getFooter() {
		return sheet.getFooter();
	}

	@Override
	public void setSelected(boolean value) {
		sheet.setSelected(value);
	}

	@Override
	public double getMargin(short margin) {
		return sheet.getMargin(margin);
	}

	@Override
	public void setMargin(short margin, double size) {
		sheet.setMargin(margin, size);
	}

	@Override
	public boolean getProtect() {
		return sheet.getProtect();
	}

	@Override
	public void protectSheet(String password) {
		sheet.protectSheet(password);
	}

	@Override
	public boolean getScenarioProtect() {
		return sheet.getScenarioProtect();
	}

	public void setZoom(int numerator, int denominator) {
		// sheet.setZoom(numerator, denominator);
		double dnum = Integer.valueOf(numerator).doubleValue();
		double dden = Integer.valueOf(denominator).doubleValue();
		double pct = dnum / dden * 100.0;
		sheet.setZoom(Double.valueOf(pct).intValue());
	}

	@Override
	public short getTopRow() {
		return sheet.getTopRow();
	}

	@Override
	public short getLeftCol() {
		return sheet.getLeftCol();
	}

	public void showInPane(short toprow, short leftcol) {
		sheet.showInPane(toprow, leftcol);
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n) {
		sheet.shiftRows(startRow, endRow, n);
	}

	@Override
	public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight) {
		sheet.shiftRows(startRow, endRow, n, copyRowHeight, resetOriginalRowHeight);
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
		sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
	}

	@Override
	public void createFreezePane(int colSplit, int rowSplit) {
		sheet.createFreezePane(colSplit, rowSplit);
	}

	@Override
	public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane) {
		sheet.createSplitPane(xSplitPos, ySplitPos, leftmostColumn, topRow, activePane);
	}

	@Override
	public PaneInformation getPaneInformation() {
		return sheet.getPaneInformation();
	}

	@Override
	public void setDisplayGridlines(boolean show) {
		sheet.setDisplayGridlines(show);
	}

	@Override
	public boolean isDisplayGridlines() {
		return sheet.isDisplayGridlines();
	}

	@Override
	public void setDisplayFormulas(boolean show) {
		sheet.setDisplayFormulas(show);
	}

	@Override
	public boolean isDisplayFormulas() {
		return sheet.isDisplayFormulas();
	}

	@Override
	public void setDisplayRowColHeadings(boolean show) {
		sheet.setDisplayRowColHeadings(show);
	}

	@Override
	public boolean isDisplayRowColHeadings() {
		return sheet.isDisplayRowColHeadings();
	}

	@Override
	public void setRowBreak(int row) {
		sheet.setRowBreak(row);
	}

	@Override
	public boolean isRowBroken(int row) {
		return sheet.isRowBroken(row);
	}

	@Override
	public void removeRowBreak(int row) {
		sheet.removeRowBreak(row);
	}

	@Override
	public int[] getRowBreaks() {
		return sheet.getRowBreaks();
	}

	@Override
	public int[] getColumnBreaks() {
		return sheet.getColumnBreaks();
	}

	@Override
	public void setColumnBreak(int column) {
		sheet.setColumnBreak(column);
	}

	@Override
	public boolean isColumnBroken(int column) {
		return sheet.isColumnBroken(column);
	}

	@Override
	public void removeColumnBreak(int column) {
		sheet.removeColumnBreak(column);
	}

	@Override
	public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
		sheet.setColumnGroupCollapsed(columnNumber, collapsed);
	}

	@Override
	public void groupColumn(int fromColumn, int toColumn) {
		sheet.groupColumn(fromColumn, toColumn);
	}

	@Override
	public void ungroupColumn(int fromColumn, int toColumn) {
		sheet.ungroupColumn(fromColumn, toColumn);
	}

	@Override
	public void groupRow(int fromRow, int toRow) {
		sheet.groupRow(fromRow, toRow);
	}

	@Override
	public void ungroupRow(int fromRow, int toRow) {
		sheet.ungroupRow(fromRow, toRow);
	}

	@Override
	public void setRowGroupCollapsed(int row, boolean collapse) {
		sheet.setRowGroupCollapsed(row, collapse);
	}

	@Override
	public void setDefaultColumnStyle(int column, CellStyle style) {
		sheet.setDefaultColumnStyle(column, style);
	}

	@Override
	public void autoSizeColumn(int column) {
		sheet.autoSizeColumn(column);
	}

	@Override
	public void autoSizeColumn(int column, boolean useMergedCells) {
		sheet.autoSizeColumn(column, useMergedCells);
	}

	public Comment getCellComment(int row, int column) {
		return sheet.getCellComment(new CellAddress(row, column));
	}

	@Override
	public Drawing<?> createDrawingPatriarch() {
		return sheet.createDrawingPatriarch();
	}

	@Override
	public Workbook getWorkbook() {
		return sheet.getWorkbook();
	}

	@Override
	public String getSheetName() {
		return sheet.getSheetName();
	}

	@Override
	public boolean isSelected() {
		return sheet.isSelected();
	}

	@Override
	public CellRange<? extends Cell> setArrayFormula(String formula, CellRangeAddress range) {
		return sheet.setArrayFormula(formula, range);
	}

	@Override
	public CellRange<? extends Cell> removeArrayFormula(Cell cell) {
		return sheet.removeArrayFormula(cell);
	}

	@Override
	public DataValidationHelper getDataValidationHelper() {
		return sheet.getDataValidationHelper();
	}

	@Override
	public void addValidationData(DataValidation dataValidation) {
		sheet.addValidationData(dataValidation);
	}

	@Override
	public AutoFilter setAutoFilter(CellRangeAddress range) {
		return sheet.setAutoFilter(range);
	}

	@Override
	public SheetConditionalFormatting getSheetConditionalFormatting() {
		return sheet.getSheetConditionalFormatting();
	}

	@Override
	public float getColumnWidthInPixels(int columnIndex) {
		return sheet.getColumnWidthInPixels(columnIndex);
	}

	@Override
	public int addMergedRegionUnsafe(CellRangeAddress region) {
		return sheet.addMergedRegionUnsafe(region);
	}

	@Override
	public void validateMergedRegions() {
		sheet.validateMergedRegions();
	}

	@Override
	public void removeMergedRegions(Collection<Integer> indices) {
		sheet.removeMergedRegions(indices);
	}

	@Override
	public List<CellRangeAddress> getMergedRegions() {
		return sheet.getMergedRegions();
	}

	@Override
	public boolean isPrintRowAndColumnHeadings() {
		return sheet.isPrintRowAndColumnHeadings();
	}

	@Override
	public void setPrintRowAndColumnHeadings(boolean show) {
		sheet.setPrintRowAndColumnHeadings(show);
	}

	@Override
	public void setZoom(int scale) {
		sheet.setZoom(scale);
	}

	@Override
	public void showInPane(int toprow, int leftcol) {
		sheet.showInPane(toprow, leftcol);
	}

	@Override
	public void shiftColumns(int startColumn, int endColumn, int n) {
		sheet.shiftColumns(startColumn, endColumn, n);
	}

	@Override
	public Comment getCellComment(CellAddress ref) {
		return sheet.getCellComment(ref);
	}

	@Override
	public Map<CellAddress, ? extends Comment> getCellComments() {
		return sheet.getCellComments();
	}

	@Override
	public Drawing<?> getDrawingPatriarch() {
		return sheet.getDrawingPatriarch();
	}

	@Override
	public List<? extends DataValidation> getDataValidations() {
		return sheet.getDataValidations();
	}

	@Override
	public int getColumnOutlineLevel(int columnIndex) {
		return sheet.getColumnOutlineLevel(columnIndex);
	}

	@Override
	public Hyperlink getHyperlink(int row, int column) {
		return sheet.getHyperlink(row, column);
	}

	@Override
	public Hyperlink getHyperlink(CellAddress addr) {
		return sheet.getHyperlink(addr);
	}

	@Override
	public List<? extends Hyperlink> getHyperlinkList() {
		return sheet.getHyperlinkList();
	}

	@Override
	public CellAddress getActiveCell() {
		return sheet.getActiveCell();
	}

	@Override
	public void setActiveCell(CellAddress address) {
		sheet.setActiveCell(address);
	}
}
