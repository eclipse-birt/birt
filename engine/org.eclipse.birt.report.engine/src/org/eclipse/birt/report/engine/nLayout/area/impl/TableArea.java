/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Definition of the table area
 *
 * @since 3.3
 *
 */
public class TableArea extends RepeatableArea {

	protected transient TableLayoutInfo layoutInfo;

	protected transient TableLayout layout;

	protected RowArea unresolvedRow;

	protected int startCol;
	protected int endCol;

	/**
	 * Constructor container based
	 *
	 * @param parent
	 * @param context
	 * @param content
	 */
	public TableArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
	}

	TableArea(TableArea table) {
		super(table);
		layout = table.layout;
		layoutInfo = table.layoutInfo;
	}

	/**
	 * Verify if the row exists at the table
	 *
	 * @param row
	 * @return true, row exists
	 */
	public boolean contains(RowArea row) {
		return children.contains(row);
	}

	/**
	 * Add row to the table
	 *
	 * @param row new row
	 */
	public void addRow(RowArea row) {
		if (layout != null) {
			layout.addRow(row, context.isFixedLayout());
		}
	}

	/**
	 * Get column count
	 *
	 * @return Return column count
	 */
	public int getColumnCount() {
		if (content != null) {
			return ((ITableContent) content).getColumnCount();
		}
		return 0;
	}

	@Override
	protected boolean needRepeat() {
		ITableContent table = (ITableContent) content;
		if (table != null && table.isHeaderRepeat()) {
			return true;
		}
		return false;
	}

	@Override
	public TableArea cloneArea() {
		return new TableArea(this);
	}

	/**
	 * Get the position X from the layout based on column id
	 *
	 * @param columnID
	 * @return Return the position X from the layout
	 */
	public int getXPos(int columnID) {
		if (layoutInfo != null) {
			return layoutInfo.getXPosition(columnID);
		}
		return 0;
	}

	/**
	 * Verify if the table use grid design (without data)
	 *
	 * @return true, table is grid design
	 */
	public boolean isGridDesign() {
		if (content != null) {
			Object gen = content.getGenerateBy();
			return gen instanceof GridItemDesign;
		}
		return false;
	}

	@Override
	protected void buildProperties(IContent content, LayoutContext context) {
		IStyle style = content.getStyle();
		if (style != null && !style.isEmpty()) {
			boxStyle = new BoxStyle();
			IStyle cs = content.getComputedStyle();
			Color color = PropertyUtil.getColor(cs.getProperty(StyleConstants.STYLE_BACKGROUND_COLOR));

			if (color != null) {
				boxStyle.setBackgroundColor(color);
			}

			String url = style.getBackgroundImage();
			if (url != null) {
				ResourceLocatorWrapper rl = null;
				ExecutionContext exeContext = ((ReportContent) content.getReportContent()).getExecutionContext();
				if (exeContext != null) {
					rl = exeContext.getResourceLocator();
				}
				BackgroundImageInfo backgroundImage = new BackgroundImageInfo(getImageUrl(url),
						style.getProperty(StyleConstants.STYLE_BACKGROUND_REPEAT),
						PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X)),
						PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y)),
						0, 0, rl, this.getCurrentModule(),
						style.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE_TYPE));
				backgroundImage.setImageSize(style);
				boxStyle.setBackgroundImage(backgroundImage);
			}
			localProperties = new LocalProperties();
			int maw = parent.getMaxAvaWidth();

			localProperties.setMarginBottom(getDimensionValue(cs.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM), maw));
			localProperties.setMarginLeft(getDimensionValue(cs.getProperty(StyleConstants.STYLE_MARGIN_LEFT), maw));
			localProperties.setMarginTop(getDimensionValue(cs.getProperty(StyleConstants.STYLE_MARGIN_TOP), maw));
			localProperties.setMarginRight(getDimensionValue(cs.getProperty(StyleConstants.STYLE_MARGIN_RIGHT), maw));
			if (!isInlineStacking) {
				pageBreakAfter = cs.getProperty(StyleConstants.STYLE_PAGE_BREAK_AFTER);
				pageBreakInside = cs.getProperty(StyleConstants.STYLE_PAGE_BREAK_INSIDE);
				pageBreakBefore = cs.getProperty(StyleConstants.STYLE_PAGE_BREAK_BEFORE);
			}
		} else {
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
		}
		bookmark = content.getBookmark();
		action = content.getHyperlinkAction();
	}

	@Override
	public void initialize() throws BirtException {
		calculateSpecifiedWidth(content);
		buildProperties(content, context);
		layoutInfo = resolveTableFixedLayout(content, context);
		width = layoutInfo.getTableWidth();
		maxAvaWidth = layoutInfo.getTableWidth();
		ITableContent tableContent = (ITableContent) content;
		int start = 0;
		int end = tableContent.getColumnCount() - 1;
		layout = new TableLayout(tableContent, layoutInfo, start, end);

		parent.add(this);
		// No longer using addDummyColumnForRTL
		// TODO addDummyColumnForRTL

		addCaption(((ITableContent) content).getCaption());
	}

	protected void addCaption(String caption) throws BirtException {
		if (caption == null || "".equals(caption)) //$NON-NLS-1$
		{
			return;
		}
		ReportContent report = (ReportContent) content.getReportContent();
		IRowContent row = report.createRowContent();
		row.setTagType("Caption");
		row.setParent(content);
		ICellContent cell = report.createCellContent();
		cell.setTagType("NonStruct");
		cell.setColSpan(getColumnCount());
		cell.setColumn(0);
		StyleDeclaration cstyle = new StyleDeclaration(report.getCSSEngine());
		cstyle.setProperty(StyleConstants.STYLE_BORDER_TOP_STYLE, CSSValueConstants.HIDDEN_VALUE);
		cstyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE, CSSValueConstants.HIDDEN_VALUE);
		cstyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE, CSSValueConstants.HIDDEN_VALUE);
		cell.setInlineStyle(cstyle);
		cell.setParent(row);
		ILabelContent captionLabel = report.createLabelContent();
		captionLabel.setTagType(null);
		captionLabel.setParent(cell);
		captionLabel.setText(caption);
		StyleDeclaration style = new StyleDeclaration(report.getCSSEngine());
		style.setProperty(StyleConstants.STYLE_TEXT_ALIGN, CSSValueConstants.CENTER_VALUE);
		captionLabel.setInlineStyle(style);
		captionLabel.setTagType("NonStruct");
		RowArea captionRow = new RowArea(this, context, row);
		captionRow.isDummy = true;
		captionRow.setParent(this);
		captionRow.setWidth(width);
		captionRow.initialize();
		CellArea captionCell = new CellArea(captionRow, context, cell);
		captionCell.setWidth(width);
		captionCell.setMaxAvaWidth(width);
		captionCell.initialize();
		captionCell.isDummy = true;
		captionCell.setRowSpan(1);
		BlockTextArea captionText = new BlockTextArea(captionCell, context, captionLabel);
		captionText.isDummy = true;
		captionText.layout();
		int h = captionText.getAllocatedHeight();
		captionCell.setContentHeight(h);
		captionRow.setHeight(captionCell.getAllocatedHeight());
		captionRow.finished = true;
		if (repeatList == null) {
			repeatList = new ArrayList<AbstractArea>();
		}
		repeatList.add(captionRow);
		update(captionRow);
	}

	protected boolean isInHeaderBand() {
		if (children.size() > 0) {
			ContainerArea child = (ContainerArea) children.get(children.size() - 1);
			IContent childContent = child.getContent();
			if (childContent != null) {
				if (childContent.getContentType() == IContent.TABLE_GROUP_CONTENT) {
					return false;
				}
				IContent band = (IContent) childContent.getParent();
				if (band instanceof IBandContent) {
					int type = ((IBandContent) band).getBandType();
					if (type != IBandContent.BAND_HEADER) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		SplitResult result = super.split(height, force);
		if (result.getResult() != null) {
			TableArea tableResult = (TableArea) result.getResult();
			unresolvedRow = tableResult.getLastRow();
			int h = tableResult.layout.resolveAll(unresolvedRow);
			if (h > 0) {
				tableResult.setHeight(tableResult.getHeight() + h);
			}
			tableResult.resolveBottomBorder();
			// layout.setUnresolvedRow( unresolvedRow );
			if (context.isFixedLayout()) {
				FixedLayoutPageHintGenerator pageHintGenerator = context.getPageHintGenerator();
				if (pageHintGenerator != null && unresolvedRow != null) {
					InstanceID unresolvedTableIID = unresolvedRow.getTableArea().getContent().getInstanceID();
					// this iid can be null, because the table may be generated
					// from HTML2Content.
					// in this case, they are ignored by unresolved row hint.
					// Currently, large HTML text is not supported to be split.
					if (unresolvedTableIID != null) {
						pageHintGenerator.addUnresolvedRowHint(unresolvedTableIID.toUniqueString(),
								convertRowToHint(unresolvedRow));
					}
				}
			}
			// when split result is not null, should re-layout the left rows
			relayoutChildren();
		}
		return result;
	}

	private UnresolvedRowHint convertRowToHint(RowArea row) {
		IRowContent rowContent = (IRowContent) row.getContent();
		ITableContent table = rowContent.getTable();
		InstanceID tableId = table.getInstanceID();
		InstanceID rowId = rowContent.getInstanceID();
		UnresolvedRowHint hint = new UnresolvedRowHint(tableId.toUniqueString(), rowId.toUniqueString());
		if (row.cells != null) {
			for (int i = 0; i < row.cells.length; i++) {
				AbstractArea area = row.cells[i];
				String style = null;
				if (area instanceof DummyCell) {
					CellArea cell = ((DummyCell) area).getCell();
					ICellContent cellContent = (ICellContent) cell.getContent();
					if (cellContent != null) {
						style = cellContent.getStyle().getCssText();
					}
					hint.addUnresolvedCell(style, cell.columnID, ((DummyCell) area).colSpan,
							((DummyCell) area).rowSpan);
				} else if (area instanceof CellArea) {
					CellArea cell = (CellArea) area;
					ICellContent cellContent = (ICellContent) cell.getContent();
					if (cellContent != null) {
						style = cellContent.getStyle().getCssText();
					}
					// hint.addUnresolvedCell( style, cell.columnID, cell.colSpan,
					// cell.rowSpan );
					hint.addUnresolvedCell(style, cellContent.getColumn(), cellContent.getColSpan(),
							cellContent.getRowSpan());
				}
			}
		}
		return hint;
	}

	protected RowArea getLastRow(ContainerArea container) {
		int count = container.getChildrenCount();
		for (int i = count - 1; i >= 0; i--) {
			IArea child = container.getChild(i);
			if (child instanceof RowArea) {
				return (RowArea) child;
			} else if (child instanceof ContainerArea) {
				RowArea lastRow = getLastRow((ContainerArea) child);
				if (lastRow != null) {
					return lastRow;
				}
			} else {
				return null;
			}
		}
		return null;
	}

	protected RowArea getLastRow() {
		return getLastRow(this);
	}

	/**
	 * Resolve the bottom border of the table
	 */
	public void resolveBottomBorder() {
		RowArea lastRow = getLastRow();
		if (lastRow != null) {
			if (lastRow.cells != null) {
				int bw = 0;
				for (int i = 0; i < lastRow.cells.length; i++) {
					if (lastRow.cells[i] != null) {
						bw = Math.max(bw, layout.resolveBottomBorder(lastRow.cells[i]));
						i = i + lastRow.cells[i].getColSpan() - 1;
					}
				}
				if (bw > 0) {
					lastRow.setHeight(bw + lastRow.getHeight());
					for (int i = 0; i < lastRow.cells.length; i++) {
						if (lastRow.cells[i] != null) {
							if (lastRow.cells[i] instanceof DummyCell) {
								// FIXME
								CellArea c = ((DummyCell) lastRow.cells[i]).getCell();
							} else {
								lastRow.cells[i].setHeight(lastRow.cells[i].getHeight() + bw);
							}
							i = i + lastRow.cells[i].getColSpan() - 1;
						}
					}
				}
			}
			// FIMXE update group/table height;
		}
	}

	protected String getNextRowId(RowArea row) {
		RowArea nextRow = layout.getNextRow(row);
		if (nextRow != null) {
			InstanceID id = nextRow.getContent().getInstanceID();
			if (id != null) {
				return id.toUniqueString();
			}
		}
		return null;
	}

	protected boolean setUnresolvedRow = false;

	protected void setUnresolvedRow() {
		if (!setUnresolvedRow) {
			layout.setUnresolvedRow(unresolvedRow);
			setUnresolvedRow = true;
		}
	}

	/**
	 * Relayout the children of the table area
	 *
	 * @throws BirtException
	 */
	public void relayoutChildren() throws BirtException {
		String nextRowId = null;
		if (unresolvedRow != null) {
			nextRowId = this.getNextRowId(unresolvedRow);
		}
		layout.clear();
		setUnresolvedRow = false;

		/***
		 * 1. collect all rows 2. use nextRowId to collect all rows little than the
		 * rowId 3. udpate the rowSpan in collection 2 4. add all rows to layout
		 */

		List<RowArea> rows = new ArrayList<>();
		collectRows(this, layout, rows);
		int rowCount = getRowCountNeedResolved(rows, nextRowId);
		boolean resolved = false;
		if (rowCount > 0 && unresolvedRow != null) {
			for (int i = 0; i < rowCount; i++) {
				resolved = resolveRowSpan(rows.get(i), unresolvedRow, rowCount - i) || resolved;
			}
		}

		addRows(this, layout, nextRowId);
		if (!resolved) {
			setUnresolvedRow();
		}
	}

	protected boolean resolveRowSpan(RowArea row, RowArea unresolvedRow, int rowCount) {
		boolean resolved = false;
		for (int i = startCol; i <= endCol;) {
			CellArea cell = row.getCell(i);
			CellArea uCell = unresolvedRow.getCell(i);
			if (cell != null && uCell != null) {
				IContent cellContent = cell.getContent();
				IContent uCellContent = cell.getContent();
				if (cellContent == uCellContent) {
					int rowSpan = 0;
					if (unresolvedRow.finished) {
						rowSpan = uCell.getRowSpan() + rowCount - 1;
					} else {
						rowSpan = uCell.getRowSpan() + rowCount;
					}
					if (rowSpan < cell.getRowSpan() && rowSpan >= 1) {
						cell.setRowSpan(rowSpan);
						resolved = true;
						setUnresolvedRow = true;
					}
				}
				i = i + cell.getColSpan();
			} else {
				i++;
			}
		}
		return resolved;
	}

	protected int getRowCountNeedResolved(List<RowArea> rows, String rowId) {
		for (int i = 0; i < rows.size(); i++) {
			RowArea row = rows.get(i);
			InstanceID id = row.getContent().getInstanceID();
			if (rowId != null && id != null && rowId.equals(id.toUniqueString())) {
				return i;
			}
		}
		return rows.size();

	}

	protected void collectRows(ContainerArea container, TableLayout layout, List<RowArea> rows) {
		if (container instanceof RowArea) {
			RowArea row = (RowArea) container;
			if (row.finished) {
				rows.add(row);
			}
		} else {
			int size = container.getChildrenCount();
			for (int i = 0; i < size; i++) {
				ContainerArea child = (ContainerArea) container.getChild(i);
				collectRows(child, layout, rows);
			}
		}
	}

	protected void addRows(ContainerArea container, TableLayout layout, String rowId) throws BirtException {
		if (container instanceof RowArea) {
			RowArea row = (RowArea) container;
			InstanceID id = row.getContent().getInstanceID();
			if (rowId != null && id != null && rowId.equals(id.toUniqueString())) {
				setUnresolvedRow();
			}
			if (row.needResolveBorder) {
				int size = row.getChildrenCount();
				for (int i = 0; i < size; i++) {
					CellArea cell = (CellArea) row.getChild(i);
					int ch = cell.getContentHeight();
					cell.boxStyle.clearBorder();
					layout.resolveBorderConflict(cell, true);
					cell.setContentHeight(ch);
				}
				row.needResolveBorder = false;
			}
			if (row.finished) {
				if (row.getChildrenCount() != row.cells.length) {
					for (int i = 0; i < row.cells.length; i++) {
						if (row.cells[i] instanceof DummyCell) {
							row.cells[i] = null;
						}
					}
				}
				layout.addRow(row, context.isFixedLayout());
			}
		} else {
			int size = container.getChildrenCount();
			for (int i = 0; i < size; i++) {
				ContainerArea child = (ContainerArea) container.getChild(i);
				addRows(child, layout, rowId);
				child.updateChildrenPosition();
			}
			container.updateChildrenPosition();
		}
	}

	@Override
	public void close() throws BirtException {
		/*
		 * 1. resolve all unresolved cell 2. resolve table bottom border 3. update
		 * height of Root area 4. update the status of TableAreaLayout
		 */

		int borderHeight = 0;
		if (layout != null) {
			int height = layout.resolveAll(getLastRow());
			if (0 != height) {
				currentBP = currentBP + height;
			}
			borderHeight = layout.resolveBottomBorder();
			layout.remove(this);
		}
		setHeight(currentBP + getOffsetY() + borderHeight);
		updateBackgroundImage();
		if (parent != null) {
			IContent parentContent = parent.getContent();
			if (parentContent != null && parentContent.isRTL()) // bidi_hcg
			{
				flipPositionForRtl();
			}
			boolean pb = checkPageBreak();
			if (pb) {
				int height = layout.resolveAll(getLastRow());
				if (0 != height) {
					currentBP = currentBP + height;
				}
				borderHeight = layout.resolveBottomBorder();
				layout.remove(this);
			}
			parent.update(this);
		}
		finished = true;
		checkDisplayNone();
	}

	/**
	 * Get the cell width
	 *
	 * @param startColumn
	 * @param endColumn
	 * @return Return the cell width
	 */
	public int getCellWidth(int startColumn, int endColumn) {
		if (layoutInfo != null) {
			return layoutInfo.getCellWidth(startColumn, endColumn);
		}
		return 0;
	}

	/**
	 * Resolve the border conflict
	 *
	 * @param cellArea
	 * @param isFirst
	 */
	public void resolveBorderConflict(CellArea cellArea, boolean isFirst) {
		if (layout != null) {
			layout.resolveBorderConflict(cellArea, isFirst);
		}
	}

	private TableLayoutInfo resolveTableFixedLayout(IContent content, LayoutContext context) {
		assert (parent != null);
		int parentMaxWidth = parent.getMaxAvaWidth();
		int marginWidth = localProperties.getMarginLeft() + localProperties.getMarginRight();
		return new TableLayoutInfo((ITableContent) content, context,
				new ColumnWidthResolver((ITableContent) content).resolveFixedLayout(parentMaxWidth - marginWidth));
	}

	@Override
	protected int getDimensionValue(IContent content, DimensionType d, int referenceLength) {
		// compatibility process. For old report document render(engine version<=2.3.2),
		// we use 72dpi.
		ReportContent report = (ReportContent) content.getReportContent();
		ExecutionContext executionContext = report.getExecutionContext();
		if (executionContext != null) {
			if (executionContext.getTaskType() == IEngineTask.TASK_RENDER) {
				IReportDocument doc = executionContext.getReportDocument();
				if (doc != null) {
					String version = doc.getProperty(ReportDocumentConstants.BIRT_ENGINE_VERSION_KEY);
					if (version != null && version.compareTo(ReportDocumentConstants.BIRT_ENGINE_VERSION_2_3_2) <= 0) {
						return getDimensionValue(content, d, 72, referenceLength);
					}
				}
			}
		}
		return getDimensionValue(content, d, 0, referenceLength);
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
			ArrayList<Integer> percentageList = new ArrayList<Integer>();
			ArrayList<Integer> unsetList = new ArrayList<Integer>();
			ArrayList<Integer> preFixedList = new ArrayList<Integer>();
			int[] resolvedColumnWidth = new int[columns.length];
			double total = 0.0f;
			int fixedLength = 0;
			for (int i = 0; i < columns.length; i++) {
				if (columns[i] == null) {
					unsetList.add(Integer.valueOf(i));
				} else if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(columns[i].getUnits())) {
					percentageList.add(Integer.valueOf(i));
					total += columns[i].getMeasure();
				} else if (DesignChoiceConstants.UNITS_EM.equals(columns[i].getUnits())
						|| DesignChoiceConstants.UNITS_EX.equals(columns[i].getUnits())) {
					int len = getDimensionValue(table, columns[i],
							getDimensionValue(table.getComputedStyle().getProperty(StyleConstants.STYLE_FONT_SIZE)));
					resolvedColumnWidth[i] = len;
					fixedLength += len;
				} else {
					int len = getDimensionValue(table, columns[i], tableWidth);
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
					Integer index = unsetList.get(i);
					resolvedColumnWidth[index.intValue()] = 0;
				}
				for (int i = 0; i < percentageList.size(); i++) {
					Integer index = percentageList.get(i);
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
							Integer index = preFixedList.get(i);
							resolvedColumnWidth[index.intValue()] += delta;
						}
					}
				} else {
					float leftPercentage = (((float) (tableWidth - fixedLength)) / tableWidth) * 100.0f;
					double ratio = leftPercentage / total;
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
						resolvedColumnWidth[index.intValue()] = getDimensionValue(table, columns[index.intValue()],
								tableWidth);
					}
				}
			} else if (percentageList.isEmpty()) {
				int left = tableWidth - fixedLength;
				int eachWidth = left / unsetList.size();
				for (int i = 0; i < unsetList.size(); i++) {
					Integer index = unsetList.get(i);
					resolvedColumnWidth[index.intValue()] = eachWidth;
				}
			} else {
				float leftPercentage = (((float) (tableWidth - fixedLength)) / tableWidth) * 100.0f;
				if (leftPercentage <= total) {
					double ratio = leftPercentage / total;
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = unsetList.get(i);
						resolvedColumnWidth[index.intValue()] = 0;
					}
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = percentageList.get(i);
						columns[index.intValue()] = new DimensionType(columns[index.intValue()].getMeasure() * ratio,
								columns[index.intValue()].getUnits());
						resolvedColumnWidth[index.intValue()] = getDimensionValue(table, columns[index.intValue()],
								tableWidth);
					}
				} else {
					int usedLength = fixedLength;
					for (int i = 0; i < percentageList.size(); i++) {
						Integer index = percentageList.get(i);
						int width = getDimensionValue(table, columns[index.intValue()], tableWidth);
						usedLength += width;
						resolvedColumnWidth[index.intValue()] = width;

					}
					int left = tableWidth - usedLength;
					int eachWidth = left / unsetList.size();
					for (int i = 0; i < unsetList.size(); i++) {
						Integer index = unsetList.get(i);
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
					columns[i] = LayoutUtil.getColWidthFromCellInFirstRow(table, i);
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
			int specifiedWidth = getDimensionValue(table, table.getWidth(), maxWidth);
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

	}

	/**
	 * Definition of table layout info
	 *
	 * @since 3.3
	 *
	 */
	public static class TableLayoutInfo {

		ITableContent tableContent;
		LayoutContext context;

		/**
		 * Constructor
		 *
		 * @param tableContent
		 * @param context
		 * @param colWidth
		 */
		public TableLayoutInfo(ITableContent tableContent, LayoutContext context, int[] colWidth) {
			this.tableContent = tableContent;
			this.context = context;
			this.colWidth = colWidth;
			this.columnNumber = colWidth.length;
			this.xPositions = new int[columnNumber];
			this.tableWidth = 0;

			for (int i = 0; i < columnNumber; i++) {
				xPositions[i] = tableWidth;
				tableWidth += colWidth[i];
			}
		}

		/**
		 * Get the table width
		 *
		 * @return Return the table width
		 */
		public int getTableWidth() {
			return this.tableWidth;
		}

		/**
		 * Get the position X
		 *
		 * @param index
		 * @return Return the position X
		 */
		public int getXPosition(int index) {
			return xPositions[index];
		}

		/**
		 * Get cell width
		 *
		 * @param startColumn
		 * @param endColumn
		 * @return Return the cell width
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
