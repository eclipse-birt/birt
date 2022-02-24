/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class TableLayout extends RepeatableLayout {
	/**
	 * table content
	 */
	private ITableContent tableContent;

	/**
	 * number of table column
	 */
	protected int columnNumber;

	/**
	 * the first visible column id of the table.
	 */
	protected int startCol = -1;

	/**
	 * the last visible column id of the table.
	 */
	protected int endCol = -1;

	/**
	 * table width
	 */
	protected int tableWidth;

	protected TableLayoutInfo layoutInfo = null;

	protected TableContext tableContext = null;

	protected ColumnWidthResolver columnWidthResolver;

	protected TableAreaLayout regionLayout = null;

	public TableLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		tableContent = (ITableContent) content;
		columnWidthResolver = new ColumnWidthResolver(tableContent);
		columnNumber = tableContent.getColumnCount();
		boolean isBlock = !PropertyUtil.isInlineElement(content);
		isInBlockStacking &= isBlock;
	}

	@Override
	protected void createRoot() {
		currentContext.root = AreaFactory.createTableArea((ITableContent) content);
		currentContext.root.setWidth(tableWidth);
	}

	public TableLayoutInfo getLayoutInfo() {
		return layoutInfo;
	}

	protected void buildTableLayoutInfo() {
		this.layoutInfo = resolveTableFixedLayout((TableArea) currentContext.root);

	}

	public int getColumnCount() {
		if (tableContent != null) {
			return tableContent.getColumnCount();
		}
		return 0;
	}

	protected void checkInlineBlock() throws BirtException {
		if (PropertyUtil.isInlineElement(tableContent)) {
			if (parent instanceof IInlineStackingLayout) {
				int avaWidth = parent.getCurrentMaxContentWidth();
				calculateSpecifiedWidth();
				if (avaWidth < specifiedWidth && specifiedWidth > 0 && specifiedWidth < parent.getMaxAvaWidth()) {
					((IInlineStackingLayout) parent).endLine();
				}
			}
		}
	}

	@Override
	protected void initialize() throws BirtException {
		checkInlineBlock();
		currentContext = new TableContext();
		contextList.add(currentContext);
		tableContext = (TableContext) currentContext;
		createRoot();
		buildTableLayoutInfo();
		currentContext.root.setWidth(layoutInfo.getTableWidth());
		currentContext.maxAvaWidth = layoutInfo.getTableWidth();

		// bidi_hcg start
		if (this.columnNumber < layoutInfo.columnNumber) {
			addDummyColumnForRTL();
		}
		// bidi_hcg end

		if (parent != null) {
			currentContext.root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		} else {
			currentContext.root.setAllocatedHeight(context.getMaxHeight());
		}
		if (tableContext.layout == null) {
			int start = 0;
			int end = tableContent.getColumnCount() - 1;
			tableContext.layout = new TableAreaLayout(tableContent, layoutInfo, start, end);
			// layout.initTableLayout( context.getUnresolvedRowHint( tableContent ) );
		}
		currentContext.maxAvaHeight = currentContext.root.getContentHeight() - getBottomBorderWidth();
		addCaption(tableContent.getCaption());
		repeatHeader();

	}

	@Override
	protected void setCurrentContext(int index) {
		super.setCurrentContext(index);
		tableContext = (TableContext) currentContext;
	}

	@Override
	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		if (currentContext.root == null || currentContext.root.getChildrenCount() == 0) {
			return;
		}
		/*
		 * 1. resolve all unresolved cell 2. resolve table bottom border 3. update
		 * height of Root area 4. update the status of TableAreaLayout
		 */
		TableContext tableContext = (TableContext) currentContext;
		int borderHeight = 0;
		if (tableContext.layout != null) {
			int height = tableContext.layout.resolveAll();
			if (0 != height) {
				currentContext.currentBP = currentContext.currentBP + height;
			}
			borderHeight = tableContext.layout.resolveBottomBorder();
			tableContext.layout.remove((TableArea) currentContext.root);
		}
		currentContext.root.setHeight(currentContext.currentBP + getOffsetY() + borderHeight);
		parent.addToRoot(currentContext.root, index);
		regionLayout = null;
	}

	private int getBottomBorderWidth() {
		IStyle style = currentContext.root.getContent().getComputedStyle();
		int borderHeight = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		return borderHeight;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * resolve cell border conflict
	 *
	 * @param cellArea
	 */
	public void resolveBorderConflict(CellArea cellArea, boolean isFirst) {
		if (tableContext.layout != null) {
			tableContext.layout.resolveBorderConflict(cellArea, isFirst);
		}
	}

	/**
	 * Creates a hidden column at X position 0.
	 *
	 * @author bidi_hcg
	 */
	private void addDummyColumnForRTL() {
		// If the leftmost column X is not 0, the border is not drawn correctly
		// (FIXME??? - this may apparently happen in LTR as well, when xOffset
		// is not 0).
		// In RTL, that usually happens when table width is other than 100 %.
		// To work around, create a dummy column which will occupy the room
		// between |x = 0| and the leftmost meaningful table column.

		tableContent.addColumn(new Column(tableContent.getReportContent()));
		this.columnNumber++;
	}

	private class ColumnWidthResolver {

		ITableContent table;

		public ColumnWidthResolver(ITableContent table) {
			this.table = table;
		}

		/**
		 * Calculates the column width for the table. the return value should be each
		 * column width in point.
		 *
		 * @param columns             The column width specified in report design.
		 * @param tableWidth          The suggested table width. If isTableWidthDefined
		 *                            is true, this value is user defined table width;
		 *                            otherwise, it is the max possible width for the
		 *                            table.
		 * @param isTableWidthDefined The flag to indicate whether the table width has
		 *                            been defined explicitly.
		 * @return each column width in point.
		 */
		protected int[] formalize(DimensionType[] columns, int tableWidth, boolean isTableWidthDefined) {
			ArrayList percentageList = new ArrayList();
			ArrayList unsetList = new ArrayList();
			ArrayList preFixedList = new ArrayList();
			int[] resolvedColumnWidth = new int[columns.length];
			double total = 0.0f;
			int fixedLength = 0;
			for (int i = 0; i < columns.length; i++) {
				if (columns[i] == null) {
					unsetList.add(Integer.valueOf(i));
				} else if (EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits())) {
					percentageList.add(Integer.valueOf(i));
					total += columns[i].getMeasure();
				} else if (EngineIRConstants.UNITS_EM.equals(columns[i].getUnits())
						|| EngineIRConstants.UNITS_EX.equals(columns[i].getUnits())) {
					int len = TableLayout.this.getDimensionValue(columns[i], PropertyUtil
							.getDimensionValue(table.getComputedStyle().getProperty(StyleConstants.STYLE_FONT_SIZE)));
					resolvedColumnWidth[i] = len;
					fixedLength += len;
				} else {
					int len = TableLayout.this.getDimensionValue(columns[i], tableWidth);
					resolvedColumnWidth[i] = len;
					preFixedList.add(Integer.valueOf(i));
					fixedLength += len;
				}
			}

			// all the columns have fixed width.
			if (!isTableWidthDefined && unsetList.isEmpty() && percentageList.isEmpty()) {
				return resolvedColumnWidth;
			}

			if (fixedLength >= tableWidth) {
				for (int i = 0; i < unsetList.size(); i++) {
					Integer index = (Integer) unsetList.get(i);
					resolvedColumnWidth[index.intValue()] = 0;
				}
				for (int i = 0; i < percentageList.size(); i++) {
					Integer index = (Integer) percentageList.get(i);
					resolvedColumnWidth[index.intValue()] = 0;
				}
				return resolvedColumnWidth;
			}

			if (unsetList.isEmpty()) {
				if (percentageList.isEmpty()) {
					int left = tableWidth - fixedLength;
					if (!preFixedList.isEmpty()) {
						int delta = left / preFixedList.size();
						for (int i = 0; i < preFixedList.size(); i++) {
							Integer index = (Integer) preFixedList.get(i);
							resolvedColumnWidth[index.intValue()] += delta;
						}
					}
				} else {
					float leftPercentage = (((float) (tableWidth - fixedLength)) / tableWidth) * 100.0f;
					double ratio = leftPercentage / total;
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = (Integer) percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
						resolvedColumnWidth[index.intValue()] = TableLayout.this
								.getDimensionValue(columns[index.intValue()], tableWidth);
					}
				}
			} else if (percentageList.isEmpty()) {
				int left = tableWidth - fixedLength;
				int eachWidth = left / unsetList.size();
				for (int i = 0; i < unsetList.size(); i++) {
					Integer index = (Integer) unsetList.get(i);
					resolvedColumnWidth[index.intValue()] = eachWidth;
				}
			} else {
				float leftPercentage = (((float) (tableWidth - fixedLength)) / tableWidth) * 100.0f;
				if (leftPercentage <= total) {
					double ratio = leftPercentage / total;
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = (Integer) unsetList.get(i);
						resolvedColumnWidth[index.intValue()] = 0;
					}
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = (Integer) percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
						resolvedColumnWidth[index.intValue()] = TableLayout.this
								.getDimensionValue(columns[index.intValue()], tableWidth);
					}
				} else {
					int usedLength = fixedLength;
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = (Integer) percentageList.get(i);
						int width = TableLayout.this.getDimensionValue(columns[index.intValue()], tableWidth);
						usedLength += width;
						resolvedColumnWidth[index.intValue()] = width;

					}
					int left = tableWidth - usedLength;
					int eachWidth = left / unsetList.size();
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = (Integer) unsetList.get(i);
						resolvedColumnWidth[index.intValue()] = eachWidth;
					}
				}
			}
			return resolvedColumnWidth;
		}

		public int[] resolveFixedLayout(int maxWidth) {

			int columnNumber = table.getColumnCount();
			DimensionType[] columns = new DimensionType[columnNumber];

			// handle visibility
			for (int i = 0; i < columnNumber; i++) {
				IColumn column = table.getColumn(i);
				DimensionType w = column.getWidth();
				if (startCol < 0) {
					startCol = i;
				}
				endCol = i;
				if (w == null) {
					columns[i] = null;
				} else {
					columns[i] = new DimensionType(w.getMeasure(), w.getUnits());

				}
			}
			if (startCol < 0) {
				startCol = 0;
			}
			if (endCol < 0) {
				endCol = 0;
			}

			boolean isTableWidthDefined = false;
			int specifiedWidth = getDimensionValue(tableContent.getWidth(), maxWidth);
			int tableWidth;
			if (specifiedWidth > 0) {
				tableWidth = specifiedWidth;
				isTableWidthDefined = true;
			} else {
				tableWidth = maxWidth;
				isTableWidthDefined = false;
			}
			return formalize(columns, tableWidth, isTableWidthDefined);
		}

		public int[] resolve(int specifiedWidth, int maxWidth) {
			assert (specifiedWidth <= maxWidth);
			int columnNumber = table.getColumnCount();
			int[] columns = new int[columnNumber];
			int columnWithWidth = 0;
			int colSum = 0;

			for (int j = 0; j < table.getColumnCount(); j++) {
				IColumn column = table.getColumn(j);
				int columnWidth = getDimensionValue(column.getWidth(), tableWidth);
				if (columnWidth > 0) {
					columns[j] = columnWidth;
					colSum += columnWidth;
					columnWithWidth++;
				} else {
					columns[j] = -1;
				}
			}

			if (columnWithWidth == columnNumber) {
				if (colSum <= maxWidth) {
					return columns;
				} else {
					float delta = colSum - maxWidth;
					for (int i = 0; i < columnNumber; i++) {
						columns[i] -= (int) (delta * columns[i] / colSum);
					}
					return columns;
				}
			} else if (specifiedWidth == 0) {
				if (colSum < maxWidth) {
					distributeLeftWidth(columns, (maxWidth - colSum) / (columnNumber - columnWithWidth));
				} else {
					redistributeWidth(columns,
							colSum - maxWidth + (columnNumber - columnWithWidth) * maxWidth / columnNumber, maxWidth,
							colSum);
				}
			} else {
				if (colSum < specifiedWidth) {
					distributeLeftWidth(columns, (specifiedWidth - colSum) / (columnNumber - columnWithWidth));
				} else {
					if (colSum < maxWidth) {
						distributeLeftWidth(columns, (maxWidth - colSum) / (columnNumber - columnWithWidth));
					} else {
						redistributeWidth(columns,
								colSum - specifiedWidth
										+ (columnNumber - columnWithWidth) * specifiedWidth / columnNumber,
								specifiedWidth, colSum);
					}
				}

			}
			return columns;
		}

		private void redistributeWidth(int cols[], int delta, int sum, int currentSum) {
			int avaWidth = sum / cols.length;
			for (int i = 0; i < cols.length; i++) {
				if (cols[i] < 0) {
					cols[i] = avaWidth;
				} else {
					cols[i] -= (int) (((float) cols[i]) * delta / currentSum);
				}
			}

		}

		private void distributeLeftWidth(int cols[], int avaWidth) {
			for (int i = 0; i < cols.length; i++) {
				if (cols[i] < 0) {
					cols[i] = avaWidth;
				}
			}
		}
	}

	private TableLayoutInfo resolveTableFixedLayout(TableArea area) {
		assert (parent != null);
		int parentMaxWidth = parent.currentContext.maxAvaWidth;
		IStyle style = area.getStyle();
		int marginWidth = getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT))
				+ getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT));

		return new TableLayoutInfo(columnWidthResolver.resolveFixedLayout(parentMaxWidth - marginWidth));
	}

	public void addRow(RowArea row, int specifiedHeight, int index, int size) {
		if (isInBlockStacking) {
			tableContext = (TableContext) contextList.get(index);
			if (tableContext.layout != null) {
				tableContext.layout.addRow(row, specifiedHeight);
			}
		} else {
			int tableSize = contextList.size();
			tableContext = (TableContext) contextList.get(tableSize - size + index);
			if (tableContext.layout != null) {
				tableContext.layout.addRow(row, specifiedHeight);
			}
		}

	}

	public int getXPos(int columnID) {
		if (layoutInfo != null) {
			return layoutInfo.getXPosition(columnID);
		}
		return 0;
	}

	public int getCellWidth(int startColumn, int endColumn) {
		if (layoutInfo != null) {
			return layoutInfo.getCellWidth(startColumn, endColumn);
		}
		return 0;
	}

	public TableRegionLayout getTableRegionLayout() {
		if (regionLayout == null) {
			regionLayout = new TableAreaLayout(tableContent, layoutInfo, startCol, endCol);
		}
		return new TableRegionLayout(context, tableContent, layoutInfo, regionLayout);

	}

	protected IContent generateCaptionRow(String caption) {
		IReportContent report = tableContent.getReportContent();
		ILabelContent captionLabel = report.createLabelContent();
		captionLabel.setText(caption);
		captionLabel.getStyle().setProperty(IStyle.STYLE_TEXT_ALIGN, IStyle.CENTER_VALUE);
		ICellContent cell = report.createCellContent();
		cell.setColSpan(tableContent.getColumnCount());
		cell.setRowSpan(1);
		cell.setColumn(0);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_TOP_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_BOTTOM_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_LEFT_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_RIGHT_STYLE, IStyle.HIDDEN_VALUE);
		captionLabel.setParent(cell);
		cell.getChildren().add(captionLabel);
		IRowContent row = report.createRowContent();
		row.getChildren().add(cell);
		cell.setParent(row);
		row.setParent(tableContent);
		return row;
	}

	@Override
	protected void repeatHeader() throws BirtException {
		if (bandStatus == IBandContent.BAND_HEADER) {
			return;
		}
		ITableBandContent header = context.getWrappedTableHeader(content.getInstanceID());
		if (header == null || !tableContent.isHeaderRepeat() || header.getChildren().isEmpty()) {
			return;
		}

		TableRegionLayout rLayout = getTableRegionLayout();
		rLayout.initialize(header);

		rLayout.layout();
		TableArea tableRegion = (TableArea) header.getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null && tableRegion.getAllocatedHeight() < getCurrentMaxContentHeight()) {
			// add to layout
			TableContext tableContext = (TableContext) contextList.getLast();
			tableContext.layout.addRows(rLayout.getTableAreaLayout().getRows());

			// add to root
			Iterator iter = tableRegion.getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				addArea(area);
			}
		}
		content.setExtension(IContent.LAYOUT_EXTENSION, null);

	}

	protected void addCaption(String caption) throws BirtException {
		if (caption == null || "".equals(caption)) //$NON-NLS-1$
		{
			return;
		}
		TableRegionLayout rLayout = getTableRegionLayout();
		IContent row = generateCaptionRow(tableContent.getCaption());
		rLayout.initialize(row);

		rLayout.layout();
		TableArea tableRegion = (TableArea) row.getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null) {
			// add to root
			Iterator iter = tableRegion.getChildren();
			while (iter.hasNext()) {
				RowArea rowArea = (RowArea) iter.next();
				addArea(rowArea);
			}
		}
		row.setExtension(IContent.LAYOUT_EXTENSION, null);
		regionLayout = null;
	}

	public class TableLayoutInfo {

		public TableLayoutInfo(int[] colWidth) {
			this.colWidth = colWidth;
			this.columnNumber = colWidth.length;
			this.xPositions = new int[columnNumber];
			this.tableWidth = 0;

			if (tableContent.isRTL()) // bidi_hcg
			{
				int parentMaxWidth = parent != null ? parent.getCurrentMaxContentWidth() : context.getMaxWidth();
				for (int i = 0; i < columnNumber; i++) {
					xPositions[i] = parentMaxWidth - tableWidth - colWidth[i];
					tableWidth += colWidth[i];
				}
				if (xPositions[columnNumber - 1] != 0) {
					addDummyColumnForRTL(colWidth);
				}
			} else // ltr
			{
				for (int i = 0; i < columnNumber; i++) {
					xPositions[i] = tableWidth;
					tableWidth += colWidth[i];
				}
			}
		}

		public int getTableWidth() {
			return this.tableWidth;
		}

		public int getXPosition(int index) {
			return xPositions[index];
		}

		/**
		 * get cell width
		 *
		 * @param startColumn
		 * @param endColumn
		 * @return
		 */
		public int getCellWidth(int startColumn, int endColumn) {
			assert (startColumn < endColumn);
			assert (colWidth != null);
			int sum = 0;
			for (int i = startColumn; i < endColumn; i++) {
				sum += colWidth[i];
			}
			return sum;
		}

		protected int columnNumber;

		protected int tableWidth;
		/**
		 * Array of column width
		 */
		protected int[] colWidth = null;

		/**
		 * array of position for each column
		 */
		protected int[] xPositions = null;

		/**
		 * Creates a hidden column at X position 0.
		 *
		 * @author bidi_hcg
		 */
		private void addDummyColumnForRTL(int[] colWidth) {
			this.colWidth = new int[columnNumber + 1];
			System.arraycopy(colWidth, 0, this.colWidth, 0, columnNumber);
			this.colWidth[columnNumber] = xPositions[columnNumber - 1];

			int[] newXPositions = new int[columnNumber + 1];
			System.arraycopy(xPositions, 0, newXPositions, 0, columnNumber);
			xPositions = newXPositions;
			xPositions[columnNumber] = 0;

			tableWidth += this.colWidth[columnNumber - 1];
			++columnNumber;
		}
	}

	@Override
	public boolean addArea(AbstractArea area) {
		return super.addArea(area);
	}

	class TableContext extends ContainerContext {
		TableAreaLayout layout;

	}

}
