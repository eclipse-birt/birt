/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorderHelper;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayoutData.ColumnData;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayoutData.RowData;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.widgets.Display;

/**
 * Fit table layout
 */

public class FixTableLayout extends TableLayout {
	public static final int ALLOW_ROW_HEIGHT = 1;
	private static final int ALLOW_COLOUMN_WIDTH = 1;
	public static final int DEFAULT_ROW_HEIGHT = 16;

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public FixTableLayout(ITableLayoutOwner part) {
		super(part);
	}

	@Override
	public void layout(IFigure container) {
		if (!isDistroy()) {
			return;
		}
		ITableLayoutOwner owner = getOwner();
		helper = new TableBorderHelper(owner);

		helper.updateCellBorderInsets();

		data = new WorkingData();
		data.columnWidths = new TableLayoutData.ColumnData[getColumnCount()];
		data.rowHeights = new TableLayoutData.RowData[getRowCount()];

		List children = container.getChildren();

		init(data.columnWidths, data.rowHeights);

		// put the figure, info key
		initFigureInfo(children);

		// set the column
		caleColumnWidth();

		// debugColumn( );
		preCaleRow();

		layoutTable(container);

		resetRowMinSize(data.rowHeights);
		initRowMinSize(children);
		initRowMergeMinsize(children);

		caleRowData();

		// second pass, layout the container itself.
		layoutTable(container);

		setConstraint(container, data);
		needlayout = false;
		int containerWidth = getOwner().getFigure().getParent().getClientArea().getSize().width;

		if (containerWidth < 0) {
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					getOwner().reLayout();
				}
			});
			return;
		}
		reselect();
	}

	private void caleRowData() {
		if (data == null) {
			return;
		}

		int size = data.rowHeights.length;
		int dxRows[] = new int[size];

		for (int i = 0; i < size; i++) {
			dxRows[i] = data.rowHeights[i].height - data.rowHeights[i].trueMinRowHeight;

		}

		for (int i = 0; i < size; i++) {
			if (dxRows[i] < 0 && !data.rowHeights[i].isForce) {
				data.rowHeights[i].height = data.rowHeights[i].trueMinRowHeight;
			}
		}

		String dw = getOwner().getDefinedHeight();
		if (dw == null) {
			return;
		}

		// int containerHeight = getOwner( ).getFigure( ).getParent( )
		// .getClientArea( ).getSize( ).height;
		//
		// containerHeight -= getFigureMargin( getOwner( ).getFigure( ) )
		// .getHeight( );
		//
		// containerHeight = Math.max( 0, containerHeight );

		int rowHeight = getDefinedHeight(dw, 0);
		if (rowHeight <= 0) {
			return;
		}

		int forceTotal = 0;

		int forceCount = size;

		List<RowData> noSettingList = new ArrayList<RowData>();
		for (int i = 0; i < size; i++) {
			if (data.rowHeights[i].isForce) {
				forceCount--;
			} else if (!data.rowHeights[i].isSetting) {
				data.rowHeights[i].height = getOwner().getFixAllowMinRowHight();
				noSettingList.add(data.rowHeights[i]);
			}
			forceTotal = forceTotal + data.rowHeights[i].height;
		}

		if (forceTotal >= rowHeight) {
			for (int i = 0; i < size; i++) {
				RowData rData = data.rowHeights[i];
				if (!rData.isSetting) {
					rData.height = getOwner().getFixAllowMinRowHight();
					rData.trueMinRowHeight = ALLOW_COLOUMN_WIDTH;
				}
			}
		} else {
			int moreWith = rowHeight - forceTotal;
			int argaWith;
			int others;
			if (forceCount == 0) {
				argaWith = moreWith / size;
				others = moreWith % size;

				for (int i = 0; i < size; i++) {
					RowData rData = data.rowHeights[i];
					if (i <= others - 1) {
						rData.height = rData.height + argaWith + 1;
					} else {
						rData.height = rData.height + argaWith;
					}
				}
			} else {
				int noSettingSize = noSettingList.size();
				if (moreWith < noSettingSize * (DEFAULT_ROW_HEIGHT - getOwner().getFixAllowMinRowHight())) {
					argaWith = moreWith / noSettingSize;

					others = moreWith % noSettingSize;

					for (int i = 0; i < noSettingList.size(); i++) {
						RowData adjust = noSettingList.get(i);
						if (i <= others - 1) {
							adjust.height = adjust.height + argaWith + 1;
						} else {
							adjust.height = adjust.height + argaWith;
						}
					}
				} else {
					for (int i = 0; i < noSettingList.size(); i++) {
						RowData adjust = noSettingList.get(i);
						adjust.height = DEFAULT_ROW_HEIGHT;
					}
					moreWith = moreWith - noSettingSize * (DEFAULT_ROW_HEIGHT - getOwner().getFixAllowMinRowHight());

					argaWith = moreWith / forceCount;
					others = moreWith % forceCount;
					List<RowData> adjustList = new ArrayList<RowData>();
					for (int i = 0; i < size; i++) {
						RowData rData = data.rowHeights[i];

						if (!rData.isForce) {
							adjustList.add(rData);
						}
					}

					for (int i = 0; i < adjustList.size(); i++) {
						RowData adjust = adjustList.get(i);
						if (i <= others - 1) {
							adjust.height = adjust.height + argaWith + 1;
						} else {
							adjust.height = adjust.height + argaWith;
						}
					}
				}

			}
		}
	}

	private void getOtioseRow(List<RowData> rows, int forceCount, int width) {
		// do nothing now
	}

	private int getDefinedHeight(String dw, int cw) {
		if (dw == null || dw.length() == 0) {
			return 0;
		}

		try {
			if (dw.endsWith("%")) //$NON-NLS-1$
			{
				return (int) (Double.parseDouble(dw.substring(0, dw.length() - 1)) * cw / 100);
			}

			return (int) Double.parseDouble(dw);
		} catch (NumberFormatException e) {
			// ignore.
		}

		return 0;
	}

	private void initRowMergeMinsize(List children) {
		int size = children.size();
		// Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		List list = new ArrayList();
		List adjustRow = new ArrayList();

		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) children.get(i);
			// ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );
			FigureInfomation info = figureInfo.get(figure);
			int rowNumber = info.rowNumber;

			int rowSpan = info.rowSpan;

			if (rowSpan == 1) {
				continue;
			}
			if (figure.getChildren().size() == 0) {
				continue;
			}
			list.add(figure);

			if (rowSpan > 1) {
				for (int j = rowNumber; j < rowNumber + rowSpan; j++) {
					RowData rData = data.findRowData(j);
					if (!adjustRow.contains(Integer.valueOf(j))) {
						adjustRow.add(Integer.valueOf(j));
					}
				}
			}
		}

		List hasAdjust = new ArrayList();
		size = data.rowHeights.length;
		// int dxRows[] = new int[size];

		for (int i = 0; i < size; i++) {
			if (data.rowHeights[i].isForce) {
				hasAdjust.add(Integer.valueOf(data.rowHeights[i].rowNumber));
			}
		}

		caleRowMergeMinHeight(list, adjustRow, hasAdjust);

	}

	private void caleRowMergeMinHeight(List figures, List adjust, List hasAdjust) {
		if (adjust.isEmpty()) {
			return;
		}
		int size = figures.size();
		// Map map = getOwner( ).getViewer( ).getVisualPartMap( );
		int adjustMax = 0;
		int trueAdjustMax = 0;
		int adjustMaxNumber = 0;

		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) figures.get(i);
			// ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );
			FigureInfomation info = figureInfo.get(figure);
			int rowNumber = info.rowNumber;
			int rowSpan = info.rowSpan;

			int colWith = getColumnWith(info.columnNumber, info.columnSpan);
			Dimension minSize = figure.getMinimumSize(colWith, -1);

			int samMin = 0;
			int trueSamMin = 0;

			int[] adjustNumber = new int[0];
			for (int j = rowNumber; j < rowNumber + rowSpan; j++) {
				TableLayoutData.RowData rowData = data.findRowData(j);
				if (!hasAdjust.contains(Integer.valueOf(j))) {
					int len = adjustNumber.length;
					int temp[] = new int[len + 1];
					System.arraycopy(adjustNumber, 0, temp, 0, len);
					temp[len] = j;
					adjustNumber = temp;
				} else {
					samMin = samMin + rowData.trueMinRowHeight;
					trueSamMin = trueSamMin + rowData.trueMinRowHeight;
				}
				if (!rowData.isSetting) {
					rowData.height = getOwner().getFixAllowMinRowHight();
				}
				// rowData.height = ALLOW_ROW_HEIGHT;
			}
			int adjustCount = adjustNumber.length;
			if (adjustCount == 0) {
				continue;
			}
			int value = minSize.height - samMin;
			int trueValue = minSize.height - trueSamMin;
			for (int j = 0; j < adjustCount; j++) {
				int temp = 0;
				int trueTemp = 0;
				TableLayoutData.RowData rowData = data.findRowData(adjustNumber[j]);
				if (rowData.isForce) {
					temp = rowData.minRowHeight;
					trueTemp = rowData.trueMinRowHeight;
				} else {
					int otioseValue = value % adjustCount;
					int trueOtioseValue = trueValue % adjustCount;
					if (j <= otioseValue - 1) {
						temp = value / adjustCount + 1;

					} else {
						temp = value / adjustCount;
					}

					if (j <= trueOtioseValue - 1)

					{
						trueTemp = trueValue / adjustCount + 1;
					} else {
						trueTemp = trueValue / adjustCount;
					}
				}

				temp = Math.max(temp, rowData.minRowHeight);
				trueTemp = Math.max(trueTemp, rowData.trueMinRowHeight);

				if (trueTemp > trueAdjustMax) {
					adjustMax = temp;
					trueAdjustMax = trueTemp;
					adjustMaxNumber = adjustNumber[j];
				}
			}
		}

		if (adjustMaxNumber > 0) {
			TableLayoutData.RowData rowData = data.findRowData(adjustMaxNumber);
			// rowData.minRowHeight = adjustMax;
			rowData.trueMinRowHeight = trueAdjustMax;
			rowData.isSetting = true;
			adjust.remove(Integer.valueOf(adjustMaxNumber));
			hasAdjust.add(Integer.valueOf(adjustMaxNumber));
			caleRowMergeMinHeight(figures, adjust, hasAdjust);
		}
	}

	private void initRowMinSize(List children) {
		int size = children.size();
		// Map map = getOwner( ).getViewer( ).getVisualPartMap( );

		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) children.get(i);
			// ITableLayoutCell cellPart = (ITableLayoutCell) map.get( figure );
			FigureInfomation info = figureInfo.get(figure);
			int rowNumber = info.rowNumber;

			TableLayoutData.RowData rowData = data.findRowData(rowNumber);
			if (rowData.isForce) {
				continue;
			}
			int columnNumber = info.columnNumber;
			int columnSpan = info.columnSpan;
			int rowSpan = info.rowSpan;
			if (rowSpan != 1) {
				continue;
			}

			int colWidth = getColumnWith(columnNumber, columnSpan);

			Dimension dim = figure.getMinimumSize(colWidth, -1);

			if (rowData.isSetting) {
				if (dim.height > rowData.trueMinRowHeight) {
					rowData.trueMinRowHeight = dim.height;
					rowData.isSetting = true;
				}
			} else if (figure.getChildren().size() > 0) {
				if (dim.height > rowData.trueMinRowHeight) {
					rowData.trueMinRowHeight = dim.height;
				}

				rowData.height = getOwner().getFixAllowMinRowHight();
				rowData.isSetting = true;
			}
//			else if ( dim.height > rowData.trueMinRowHeight )
//			{
//				rowData.trueMinRowHeight = dim.height;
//			}
		}
	}

	private int getColumnWith(int columnNumber, int columnSpan) {
		TableLayoutData.ColumnData columnData = data.findColumnData(columnNumber);

		int colWidth = columnData.width;

		if (columnSpan > 1) {
			for (int k = 1; k < columnSpan; k++) {
				TableLayoutData.ColumnData cData = data.findColumnData(columnNumber + k);

				if (cData != null) {
					colWidth += cData.width;
				}
			}
		}

		return colWidth;
	}

	private void preCaleRow() {
		int size = data.rowHeights.length;
		int dxRows[] = new int[size];

		for (int i = 0; i < size; i++) {
			dxRows[i] = data.rowHeights[i].height - data.rowHeights[i].trueMinRowHeight;
		}

		for (int i = 0; i < size; i++) {
			if (dxRows[i] < 0) {
				data.rowHeights[i].height = data.rowHeights[i].trueMinRowHeight;
			}
		}
	}

	private void debugColumn() {
		System.out.println("//////////start//////////");
		int containerWidth = getLayoutWidth();
		System.out.println("container width===" + containerWidth);
		int size = data.columnWidths.length;
		for (int i = 0; i < size; i++) {
			ColumnData cData = data.columnWidths[i];
			System.out.println("column " + (i + 1) + "===" + cData.width);
		}
	}

	private void caleColumnWidth() {
		int size = data.columnWidths.length;

		int containerWidth = getLayoutWidth();

		containerWidth = Math.max(0, containerWidth);

		// Totol of the figure set width(include ercentage), should = percentageTotal +
		// forceTotal;
		int totalColumn = 0;

		// Total count of the figure set width(include ercentage)
		int forceCount = size;
		// there add the percentage adjust,total width of the figure set percentage
		int percentageTotal = 0;

		// Total of force count
		int forceTotal = 0;
		double percentageValueTotal = 0.0;
		for (int i = 0; i < size; i++) {
			ColumnData cData = data.columnWidths[i];
			if (cData.isPercentage) {
				cData.trueMinColumnWidth = (int) (containerWidth * cData.percentageWidth / 100);
				totalColumn = totalColumn + cData.trueMinColumnWidth;
				percentageTotal = percentageTotal + cData.trueMinColumnWidth;
				percentageValueTotal = percentageValueTotal + cData.percentageWidth;
				cData.width = cData.trueMinColumnWidth;
				forceCount--;
			} else if (cData.isForce) {
				totalColumn = totalColumn + cData.trueMinColumnWidth;
				forceTotal = forceTotal + cData.trueMinColumnWidth;
				cData.width = cData.trueMinColumnWidth;
				forceCount--;
			}
		}

		/*
		 * if the percentageTotal > containerWidth - forceTotal, the precentage reset on
		 * scale. for example, (All units is pix),if the containerWidth is 1000,has 4
		 * columns, one is 300(user set),one is 40% and one is 35%, the last column not
		 * set.So The 1000*40% + 1000*35 > 1000 - 300, the last result is 300,
		 * 700*40/(40+35), 700*35/(40+35), 1(ALLOW_COLOUMN_WIDTH).
		 */
		if (percentageTotal > 0 && containerWidth - forceTotal < percentageTotal) {
			percentageTotal = 0;

			int widthMore = containerWidth - forceTotal;
			for (int i = 0; i < size; i++) {
				ColumnData cData = data.columnWidths[i];
				if (cData.isPercentage) {
					if (widthMore < 0) {
						cData.trueMinColumnWidth = ALLOW_COLOUMN_WIDTH;
					} else {
						cData.trueMinColumnWidth = (int) (widthMore * cData.percentageWidth / percentageValueTotal);
					}
					percentageTotal = percentageTotal + cData.trueMinColumnWidth;
					cData.width = cData.trueMinColumnWidth;
				}
			}

			totalColumn = percentageTotal + forceTotal;
		}

		/*
		 * The forceWidth is the user set the size(include %),if the user don't set the
		 * size, the default size is 100%,but is not force width If the force count is
		 * 0, means all the column set width(include %). If force count is 0, and the
		 * table don't set width,don't change anything.
		 * 
		 */
		if ((!getOwner().isForceWidth()) && forceCount == 0) {
			return;
		}

		// if the set width total larger than container width, the others column (not
		// set width ) set the 1 pix.
		if (totalColumn >= containerWidth) {
			for (int i = 0; i < size; i++) {
				ColumnData cData = data.columnWidths[i];
				if (!cData.isForce) {
					cData.width = ALLOW_COLOUMN_WIDTH;
					cData.trueMinColumnWidth = ALLOW_COLOUMN_WIDTH;
				}
			}
		}
		/*
		 * If the container width larger than totalColumn, assign the more width to the
		 * column average.
		 */
		else {
			int moreWith = containerWidth - totalColumn;
			// moreWith / column count
			int argaWith;
			// Mode of the moreWith % column count
			int others;
			/*
			 * If all the column set width, the more with assign the all column average.
			 */
			if (forceCount == 0) {
				argaWith = moreWith / size;
				others = moreWith % size;
				for (int i = 0; i < size; i++) {
					ColumnData cData = data.columnWidths[i];
					if (i <= others - 1) {
						cData.width = cData.width + argaWith + 1;
					} else {
						cData.width = cData.width + argaWith;
					}
				}
			}
			/*
			 * If has other column don't set the column, the more width assign to the
			 * columns(not set the width) average
			 */
			else {
				if (moreWith < forceCount * ALLOW_COLOUMN_WIDTH) {
					for (int i = 0; i < size; i++) {
						ColumnData cData = data.columnWidths[i];
						if (!cData.isForce) {
							cData.width = ALLOW_COLOUMN_WIDTH;
						}
					}
				} else {
					argaWith = moreWith / forceCount;
					others = moreWith % forceCount;
					List<ColumnData> adjustList = new ArrayList<ColumnData>();
					for (int i = 0; i < size; i++) {
						ColumnData cData = data.columnWidths[i];
						if (!cData.isForce) {
							adjustList.add(cData);
						}
					}

					for (int i = 0; i < adjustList.size(); i++) {
						ColumnData adjust = adjustList.get(i);
						if (i <= others - 1) {
							adjust.width = argaWith + 1;
						} else {
							adjust.width = argaWith;
						}
					}
				}
			}
		}
	}

	private void init(TableLayoutData.ColumnData[] columnWidths, TableLayoutData.RowData[] rowHeights) {
		int size = rowHeights.length;
		for (int i = 1; i < size + 1; i++) {
			rowHeights[i - 1] = new TableLayoutData.RowData();
			rowHeights[i - 1].rowNumber = i;

			rowHeights[i - 1].height = getOwner().getRowHeightValue(i);
			rowHeights[i - 1].minRowHeight = getOwner().getFixAllowMinRowHight();

			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner().getRowHeight(i);

			rowHeights[i - 1].isForce = dim.getMeasure() > 0 || dim.isSet();

			if (dim.getUnits() == null || dim.getUnits().length() == 0
					|| DesignChoiceConstants.UNITS_PERCENTAGE.equals(dim.getUnits())) {
				rowHeights[i - 1].isAuto = true;
				rowHeights[i - 1].isForce = false;
			}

			rowHeights[i - 1].trueMinRowHeight = (rowHeights[i - 1].isForce) ? rowHeights[i - 1].height
					: DEFAULT_ROW_HEIGHT;

			if (rowHeights[i - 1].trueMinRowHeight < getOwner().getFixAllowMinRowHight()) {
				rowHeights[i - 1].trueMinRowHeight = getOwner().getFixAllowMinRowHight();
			}
		}

		size = columnWidths.length;
		for (int i = 1; i < size + 1; i++) {
			columnWidths[i - 1] = new TableLayoutData.ColumnData();
			columnWidths[i - 1].columnNumber = i;

			columnWidths[i - 1].width = getOwner().getColumnWidthValue(i);
			columnWidths[i - 1].minColumnWidth = getOwner().getFixAllowMinRowHight();
			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner().getColumnWidth(i);

			columnWidths[i - 1].isForce = dim.getMeasure() > 0 || dim.isSet();
			if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(dim.getUnits()) && dim.getMeasure() > 0) {
				columnWidths[i - 1].isPercentage = true;
				columnWidths[i - 1].percentageWidth = dim.getMeasure();
			}

			// add to handle auto case;
			if (dim.getUnits() == null || dim.getUnits().length() == 0) {
				columnWidths[i - 1].isAuto = true;
			}

			// added by gao 2004.11.22
			columnWidths[i - 1].trueMinColumnWidth = (columnWidths[i - 1].isForce && !columnWidths[i - 1].isPercentage)
					? columnWidths[i - 1].width
					: columnWidths[i - 1].minColumnWidth;
		}
	}

	private void resetRowMinSize(TableLayoutData.RowData[] rowHeights) {
		int size = rowHeights.length;

		for (int i = 1; i < size + 1; i++) {
			rowHeights[i - 1] = new TableLayoutData.RowData();
			rowHeights[i - 1].rowNumber = i;

			rowHeights[i - 1].height = getOwner().getRowHeightValue(i);
			rowHeights[i - 1].minRowHeight = getOwner().getFixAllowMinRowHight();

			// add to handle percentage case.
			ITableLayoutOwner.DimensionInfomation dim = getOwner().getRowHeight(i);

			rowHeights[i - 1].isForce = dim.getMeasure() > 0 || dim.isSet();
			rowHeights[i - 1].isSetting = dim.getMeasure() > 0 || dim.isSet();
			// add to handle auto case;
			if (dim.getUnits() == null || dim.getUnits().length() == 0
					|| DesignChoiceConstants.UNITS_PERCENTAGE.equals(dim.getUnits())) {
				rowHeights[i - 1].isAuto = true;
				rowHeights[i - 1].isForce = false;
				rowHeights[i - 1].isSetting = false;
				// rowHeights[i - 1].height = DEFAULT_ROW_HEIGHT;
			}

			rowHeights[i - 1].trueMinRowHeight = rowHeights[i - 1].isForce ? rowHeights[i - 1].height
					: getOwner().getFixAllowMinRowHight();// DEFAULT_ROW_HEIGHT;
		}
	}

	private void initFigureInfo(List children) {
		int size = children.size();
		Map map = getOwner().getViewer().getVisualPartMap();

		for (int i = 0; i < size; i++) {
			IFigure figure = (IFigure) children.get(i);
			ITableLayoutCell cellPart = (ITableLayoutCell) map.get(figure);

			int rowNumber = cellPart.getRowNumber();
			int columnNumber = cellPart.getColumnNumber();
			int rowSpan = cellPart.getRowSpan();
			int columnSpan = cellPart.getColSpan();

			FigureInfomation info = new FigureInfomation();
			info.rowNumber = rowNumber;
			info.columnNumber = columnNumber;
			info.rowSpan = rowSpan;
			info.columnSpan = columnSpan;

			figureInfo.put(figure, info);
		}
	}

}
