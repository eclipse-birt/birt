/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.cache.TableAreaLayout;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.w3c.dom.css.CSSPrimitiveValue;

public class PDFTableLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	/**
	 * table content
	 */
	private ITableContent tableContent;

	/**
	 * identify if repeat header
	 */
	protected boolean repeatHeader;

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

	protected ITableBandContent currentBand = null;

	protected int repeatRowCount = 0;

	protected Stack groupStack = new Stack();

	protected ColumnWidthResolver columnWidthResolver;

	protected int rowCount = 0;

	protected boolean isNewArea = true;

	protected TableAreaLayout layout;

	protected TableAreaLayout regionLayout = null;

	public PDFTableLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		tableContent = (ITableContent) content;
		columnWidthResolver = new ColumnWidthResolver(tableContent);
		repeatHeader = tableContent.isHeaderRepeat();
		columnNumber = tableContent.getColumnCount();
	}

	@Override
	protected boolean traverseChildren() throws BirtException {
		if (isNewArea) {
			repeat();
			isNewArea = false;
		}
		skipCachedRow();
		return super.traverseChildren();
	}

	public boolean isCellVisible(ICellContent cell) {
		return true;
	}

	protected void repeat() throws BirtException {
		addCaption(tableContent.getCaption());
		repeatHeader();
	}

	public void startGroup(IGroupContent groupContent) {
		int groupLevel = groupContent.getGroupLevel();
		groupStack.push(Integer.valueOf(groupLevel));
	}

	public int endGroup(IGroupContent groupContent) {
		// if there is no group footer, we still need to do with the drop.
		int groupLevel = groupContent.getGroupLevel();
		int height;
		height = updateUnresolvedCell(groupLevel, false);
		height += updateUnresolvedCell(groupLevel, true);

		assert (!groupStack.isEmpty());
		groupStack.pop();
		return height;
	}

	protected int getGroupLevel() {
		if (!groupStack.isEmpty()) {
			return ((Integer) groupStack.peek()).intValue();
		}
		return -1;
	}

	private int createDropID(int groupIndex, String dropType) {
		int dropId = -10 * (groupIndex + 1);
		if ("all".equals(dropType)) //$NON-NLS-1$
		{
			dropId--;
		}
		return dropId;
	}

	/**
	 * start cell update content cache
	 *
	 * @param cell
	 */
	public int getRowSpan(ICellContent cell) {
		int groupLevel = getGroupLevel();
		int rowSpan = cell.getRowSpan();
		if (groupLevel >= 0) {
			Object generateBy = cell.getGenerateBy();
			if (generateBy instanceof CellDesign) {
				CellDesign cellDesign = (CellDesign) generateBy;
				if (cellDesign != null) {
					String dropType = cellDesign.getDrop();
					if (dropType != null && !"none".equals(dropType)) //$NON-NLS-1$
					{
						return createDropID(groupLevel, dropType);
					}
				}
			}
		}
		return rowSpan;
	}

	@Override
	protected void createRoot() {
		root = AreaFactory.createTableArea((ITableContent) content);
		root.setWidth(tableWidth);
		if (!isFirst) {
			root.getStyle().setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		}
	}

	public TableLayoutInfo getLayoutInfo() {
		return layoutInfo;
	}

	protected void buildTableLayoutInfo() {
		// this.layoutInfo = new TableLayoutInfo( resolveColumnWidth( ) );
		// this.layoutInfo = resolveTableLayoutInfo( (TableArea)root );
		this.layoutInfo = resolveTableFixedLayout((TableArea) root);

	}

	@Override
	protected void initialize() {
		if (root == null) {
			isNewArea = true;
			createRoot();
			buildTableLayoutInfo();
			root.setWidth(layoutInfo.getTableWidth());

			maxAvaWidth = layoutInfo.getTableWidth();
			setCurrentIP(0);
			setCurrentBP(0);
			repeatRowCount = 0;
			rowCount = 0;
			// lastRowArea = null;
		}

		if (layout == null) {
			layout = new TableAreaLayout(tableContent, layoutInfo, startCol, endCol);
			layout.initTableLayout(context.getUnresolvedRowHint(tableContent));
		}
		if (parent != null) {
			root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		} else {
			root.setAllocatedHeight(context.getMaxHeight());
		}
		maxAvaHeight = root.getContentHeight() - getBottomBorderWidth();

	}

	@Override
	protected void closeLayout() {
		regionLayout = null;
		// FIXME
		if (root == null) {
			return;
		}
		/*
		 * 1. resolve all unresolved cell 2. resolve table bottom border 3. update
		 * height of Root area 4. update the status of TableAreaLayout
		 */
		int borderHeight = 0;
		if (layout != null) {
			int height = layout.resolveAll();
			if (0 != height) {
				currentBP = currentBP + height;
			}
			borderHeight = layout.resolveBottomBorder();
			layout.remove((TableArea) root);
		}
		// update dimension of table area
		if (isLast) {
			root.setHeight(getCurrentBP() + getOffsetY() + borderHeight);
		} else {
			root.setHeight(getCurrentBP() + getOffsetY());
		}

	}

	private int getBottomBorderWidth() {
		IStyle style = root.getContent().getComputedStyle();
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
		if (layout != null) {
			layout.resolveBorderConflict(cellArea, isFirst);
		}
	}

	private class ColumnWidthResolver {

		ITableContent table;
		// a temp solution support horizontal page break in render task;
		int start;
		int end;

		public ColumnWidthResolver(ITableContent table) {
			this.table = table;
			TableColumnHint hint = null;
			InstanceID id = table.getInstanceID();
			if (id != null) {
				String tableId = id.toUniqueString();
				hint = PDFTableLM.this.context.getTableColumnHint(tableId);
			}
			if (hint != null) {
				start = hint.getStart();
				end = hint.getColumnCount() + start;
			} else {
				start = 0;
				end = table.getColumnCount();
			}
		}

		protected void formalize(DimensionType[] columns, int tableWidth) {
			ArrayList percentageList = new ArrayList();
			ArrayList unsetList = new ArrayList();
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
					int len = PDFTableLM.this.getDimensionValue(columns[i], PropertyUtil
							.getDimensionValue(table.getComputedStyle().getProperty(StyleConstants.STYLE_FONT_SIZE)));
					fixedLength += len;
				} else {
					int len = PDFTableLM.this.getDimensionValue(columns[i], tableWidth);
					fixedLength += len;
				}
			}

			if (fixedLength >= tableWidth) {
				for (int i = 0; i < unsetList.size(); i++) {
					Integer index = (Integer) unsetList.get(i);
					columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				}
				for (int i = 0; i < percentageList.size(); i++) {
					Integer index = (Integer) percentageList.get(i);
					columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				}
			} else {
				float leftPercentage = (((float) (tableWidth - fixedLength)) / tableWidth) * 100.0f;
				if (unsetList.isEmpty()) {
					double ratio = leftPercentage / total;
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = (Integer) percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
					}
				} else if (total < leftPercentage) {
					double delta = leftPercentage - total;
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = (Integer) unsetList.get(i);
						columns[index.intValue()] = new DimensionType(delta / (double) unsetList.size(),
								EngineIRConstants.UNITS_PERCENTAGE);
					}
				} else {
					double ratio = leftPercentage / total;
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = (Integer) unsetList.get(i);
						columns[index.intValue()] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
					}
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = (Integer) percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
					}
				}
			}
		}

		protected int[] resolve(int tableWidth, DimensionType[] columns) {
			int[] cols = new int[columns.length];
			int total = 0;
			for (int i = 0; i < columns.length; i++) {
				if (!EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits())) {
					if (EngineIRConstants.UNITS_EM.equals(columns[i].getUnits())
							|| EngineIRConstants.UNITS_EX.equals(columns[i].getUnits())) {
						cols[i] = PDFTableLM.this.getDimensionValue(columns[i], PropertyUtil.getDimensionValue(
								table.getComputedStyle().getProperty(StyleConstants.STYLE_FONT_SIZE)));
					} else {
						cols[i] = PDFTableLM.this.getDimensionValue(columns[i], tableWidth);
					}
					total += cols[i];
				}
			}

			if (total > tableWidth) {
				for (int i = 0; i < columns.length; i++) {
					if (EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits())) {
						cols[i] = 0;
					}
				}
			} else {
				int delta = tableWidth - total;
				boolean hasPercentage = false;
				for (int i = 0; i < columns.length; i++) {
					if (EngineIRConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits())) {
						cols[i] = (int) (tableWidth * columns[i].getMeasure() / 100.0d);
						hasPercentage = true;
					}
				}
				if (!hasPercentage) {
					int size = 0;
					for (int i = 0; i < columns.length; i++) {
						if (cols[i] > 0) {
							size++;
						}
					}
					for (int i = 0; i < columns.length; i++) {
						if (cols[i] > 0) {
							cols[i] += delta / size;
						}
					}
				}
			}
			return cols;
		}

		public int[] resolveFixedLayout(int maxWidth) {

			int columnNumber = table.getColumnCount();
			DimensionType[] columns = new DimensionType[columnNumber];

			// handle visibility
			for (int i = 0; i < columnNumber; i++) {
				IColumn column = table.getColumn(i);
				DimensionType w = column.getWidth();
				if (PDFTableLM.this.isColumnHidden(column) || i < start || i >= end) {
					columns[i] = new DimensionType(0d, EngineIRConstants.UNITS_PT);
				} else {
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
			}
			if (startCol < 0) {
				startCol = 0;
			}
			if (endCol < 0) {
				endCol = 0;
			}

			int specifiedWidth = getDimensionValue(tableContent.getWidth(), maxWidth);
			int tableWidth;
			if (specifiedWidth > 0) {
				tableWidth = specifiedWidth;
			} else {
				tableWidth = maxWidth;
			}
			formalize(columns, tableWidth);
			return resolve(tableWidth, columns);
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

	private boolean isColumnHidden(IColumn column) {
		String format = context.getFormat();
		return LayoutUtil.isHiddenByVisibility(column, format, false);
	}

	public int updateUnresolvedCell(int groupLevel, boolean dropAll) {
		String dropType = dropAll ? "all" : "detail"; //$NON-NLS-1$ //$NON-NLS-2$
		int dropValue = this.createDropID(groupLevel, dropType);
		if (layout != null) {
			return layout.resolveDropCells(dropValue);
		}
		return 0;
	}

	public void skipRow(RowArea row) {
		if (layout != null) {
			layout.skipRow(row);
		}
	}

	protected void validateBoxProperty(IStyle style) {
		int maxWidth = 0;
		if (parent != null) {
			maxWidth = parent.getCurrentMaxContentWidth();
		}
		// support negative margin
		int leftMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_LEFT), maxWidth);
		int rightMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_RIGHT), maxWidth);
		int topMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_TOP), maxWidth);
		int bottomMargin = getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_BOTTOM), maxWidth);

		int[] vs = { rightMargin, leftMargin };
		resolveBoxConflict(vs, maxWidth);

		int[] hs = { bottomMargin, topMargin };
		resolveBoxConflict(hs, context.getMaxHeight());

		style.setProperty(IStyle.STYLE_MARGIN_LEFT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[1]));
		style.setProperty(IStyle.STYLE_MARGIN_RIGHT, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, vs[0]));
		style.setProperty(IStyle.STYLE_MARGIN_TOP, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[1]));
		style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, new FloatValue(CSSPrimitiveValue.CSS_NUMBER, hs[0]));
	}

	private TableLayoutInfo resolveTableFixedLayout(TableArea area) {
		assert (parent != null);
		int parentMaxWidth = parent.maxAvaWidth;
		IStyle style = area.getStyle();
		validateBoxProperty(style);
		int marginWidth = getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT))
				+ getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT));

		return new TableLayoutInfo(columnWidthResolver.resolveFixedLayout(parentMaxWidth - marginWidth));
	}

	private int[] handleColumnVisibility(int[] columns) {
		// enable visibility
		int colWidth[] = new int[columnNumber];
		for (int i = 0; i < columnNumber; i++) {
			IColumn column = tableContent.getColumn(i);
			if (isColumnHidden(column)) {
				colWidth[i] = 0;
			} else {
				colWidth[i] = columns[i];
			}
		}
		return colWidth;
	}

	/**
	 * update row height
	 *
	 * @param row
	 */
	public void updateRow(RowArea row, int specifiedHeight, boolean finished) {

		if (layout != null) {
			layout.updateRow(row, specifiedHeight, finished);
		}
	}

	public void addRow(RowArea row, boolean finished, boolean repeated) {
		if (layout != null) {
			layout.addRow(row, finished, repeated);
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

	public PDFTableRegionLM getTableRegionLayout() {
		PDFReportLayoutEngine engine = context.getLayoutEngine();
		PDFLayoutEngineContext con = new PDFLayoutEngineContext(engine);
		con.setFactory(new PDFLayoutManagerFactory(con));
		con.setFormat(context.getFormat());
		con.setLocale(context.getLocale());
		con.setReport(context.getReport());
		con.setMaxHeight(context.getMaxHeight());
		con.setMaxWidth(context.getMaxWidth());
		con.setAllowPageBreak(false);
		if (regionLayout == null) {
			regionLayout = new TableAreaLayout(tableContent, layoutInfo, startCol, endCol);
		}
		return new PDFTableRegionLM(con, tableContent, layoutInfo, regionLayout);

	}

	protected void repeatHeader() throws BirtException {
		if (isFirst) {
			return;
		}
		ITableBandContent header = (ITableBandContent) tableContent.getHeader();
		if (!repeatHeader || header == null || header.getChildren().isEmpty()) {
			return;
		}
		if (child != null) {
			IContent content = child.getContent();
			if (content instanceof ITableBandContent) {
				if (((ITableBandContent) content).getBandType() == IBandContent.BAND_HEADER) {
					return;
				}

			}
		}
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor(header);
		headerExecutor.execute();
		PDFTableRegionLM regionLM = getTableRegionLayout();
		regionLM.initialize(header);
		regionLM.layout();
		TableArea tableRegion = (TableArea) tableContent.getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null && tableRegion.getHeight() < getCurrentMaxContentHeight()) {
			// add to root
			Iterator iter = tableRegion.getChildren();
			RowArea row = null;
			while (iter.hasNext()) {
				row = (RowArea) iter.next();
				addArea(row, false, pageBreakAvoid);
				addRow(row, true, true);
				repeatRowCount++;
			}
			if (row != null) {
				removeBottomBorder(row);
			}
		}
		tableContent.setExtension(IContent.LAYOUT_EXTENSION, null);
	}

	protected void addCaption(String caption) throws BirtException {
		if (caption == null || "".equals(caption)) //$NON-NLS-1$
		{
			return;
		}
		IReportContent report = tableContent.getReportContent();
		ILabelContent captionLabel = report.createLabelContent();
		captionLabel.setText(caption);
		captionLabel.getStyle().setProperty(IStyle.STYLE_TEXT_ALIGN, IStyle.CENTER_VALUE);
		ICellContent cell = report.createCellContent();
		cell.setColSpan(tableContent.getColumnCount());
		cell.setRowSpan(1);
		cell.setColumn(0);
		captionLabel.setParent(cell);
		cell.getChildren().add(captionLabel);
		IRowContent row = report.createRowContent();
		row.getChildren().add(cell);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_TOP_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_BOTTOM_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_LEFT_STYLE, IStyle.HIDDEN_VALUE);
		cell.getStyle().setProperty(IStyle.STYLE_BORDER_RIGHT_STYLE, IStyle.HIDDEN_VALUE);
		cell.setParent(row);
		ITableBandContent band = report.createTableBandContent();
		band.getChildren().add(row);
		row.setParent(band);
		band.setParent(tableContent);
		PDFTableRegionLM regionLM = getTableRegionLayout();
		regionLM.initialize(band);
		regionLM.layout();
		TableArea tableRegion = (TableArea) content.getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null && tableRegion.getHeight() < getCurrentMaxContentHeight()) {
			// add to root
			Iterator iter = tableRegion.getChildren();
			while (iter.hasNext()) {
				RowArea rowArea = (RowArea) iter.next();
				addArea(rowArea, false, false);
				repeatRowCount++;
			}
		}
		content.setExtension(IContent.LAYOUT_EXTENSION, null);
	}

	@Override
	protected IReportItemExecutor createExecutor() {
		return executor;
	}

	@Override
	protected boolean isRootEmpty() {
		return !((root != null && root.getChildrenCount() > repeatRowCount) || isLast);
	}

	protected void skipCachedRow() {
		if (keepWithCache.isEmpty()) {
			return;
		}
		Iterator iter = keepWithCache.getChildren();
		while (iter.hasNext()) {
			ContainerArea container = (ContainerArea) iter.next();
			skip(container);
		}
	}

	protected void skip(ContainerArea area) {
		if (area instanceof RowArea) {
			skipRow((RowArea) area);
		} else {
			Iterator iter = area.getChildren();
			while (iter.hasNext()) {
				ContainerArea container = (ContainerArea) iter.next();
				skip(container);
			}
		}
	}

	public class TableLayoutInfo {

		public TableLayoutInfo(int[] colWidth) {
			this.colWidth = colWidth;
			this.columnNumber = colWidth.length;
			this.xPositions = new int[columnNumber];
			this.tableWidth = 0;

			if (tableContent.isRTL()) {
				for (int i = 0; i < columnNumber; i++) {
					xPositions[i] = parent.getCurrentMaxContentWidth() - tableWidth - colWidth[i];
					tableWidth += colWidth[i];
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

	}

}
