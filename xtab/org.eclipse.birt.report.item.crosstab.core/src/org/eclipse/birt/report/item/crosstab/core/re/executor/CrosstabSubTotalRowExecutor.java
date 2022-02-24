/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * CrosstabSubTotalRowExecutor
 */
public class CrosstabSubTotalRowExecutor extends BaseRowExecutor {

	private static Logger logger = Logger.getLogger(CrosstabSubTotalRowExecutor.class.getName());

	private int dimensionIndex, levelIndex;

	private boolean isLayoutDownThenOver;

	private int startTotalDimensionIndex;
	private int startTotalLevelIndex;

	private boolean rowEdgeStarted;
	private boolean rowSubTotalStarted;

	private int totalRowSpan;
	private boolean isFirstTotalRow;
	private boolean isSubTotalBefore;

	public CrosstabSubTotalRowExecutor(BaseCrosstabExecutor parent, int rowIndex, int dimensionIndex, int levelIndex) {
		super(parent, rowIndex);

		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
	}

	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		processRowHeight(findSubTotalRowCell(dimensionIndex, levelIndex, rowIndex));

		processRowLevelPageBreak(content, false);

		prepareChildren();

		return content;
	}

	protected void prepareChildren() {
		super.prepareChildren();

		initMeasureCache();

		isLayoutDownThenOver = PAGE_LAYOUT_DOWN_THEN_OVER.equals(crosstabItem.getPageLayout());

		if (isLayoutDownThenOver) {
			startTotalDimensionIndex = dimensionIndex;
			startTotalLevelIndex = levelIndex;
		} else {
			EdgeGroup nextGroup = GroupUtil.getNextGroup(rowGroups, dimensionIndex, levelIndex);
			startTotalDimensionIndex = nextGroup.dimensionIndex;
			startTotalLevelIndex = nextGroup.levelIndex;
		}

		DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, dimensionIndex);
		LevelViewHandle lv = dv.getLevel(levelIndex);

		isSubTotalBefore = lv.getAggregationHeader() != null
				&& AGGREGATION_HEADER_LOCATION_BEFORE.equals(lv.getAggregationHeaderLocation());

		isFirstTotalRow = rowIndex == GroupUtil.getFirstTotalRowIndex(crosstabItem, dimensionIndex, levelIndex,
				isVerticalMeasure);
		totalRowSpan = GroupUtil.getTotalRowSpan(crosstabItem, dimensionIndex, levelIndex, isVerticalMeasure);

		walker.reload();
	}

	private CrosstabCellHandle getSubTotalMeasureHeaderCell(int axis, int dimensionIndex, int levelIndex,
			int measureIndex) {
		if (measureIndex >= 0 && measureIndex < totalMeasureCount && dimensionIndex >= 0 && levelIndex >= 0) {
			DimensionViewHandle dv = crosstabItem.getDimension(axis, dimensionIndex);
			LevelViewHandle lv = dv.getLevel(levelIndex);

			return crosstabItem.getMeasure(measureIndex).getHeader(lv);
		}

		return null;
	}

	private AggregationCellHandle getRowSubTotalCell(int colDimensionIndex, int colLevelIndex, int measureIndex) {
		return getAggregationCell(dimensionIndex, levelIndex, colDimensionIndex, colLevelIndex, measureIndex);
	}

	private boolean isRowEdgeNeedStart(ColumnEvent ev) {
		if (rowEdgeStarted || ev.type != ColumnEvent.ROW_EDGE_CHANGE || !isSubTotalBefore) {
			return false;
		}

		if (ev.dimensionIndex > dimensionIndex || (ev.dimensionIndex == dimensionIndex
				&& (isLayoutDownThenOver ? (ev.levelIndex >= levelIndex) : (ev.levelIndex > levelIndex)))) {
			return false;
		}

		// check previous subtotal
		if (ev.dimensionIndex != dimensionIndex || ev.levelIndex != levelIndex) {
			DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, ev.dimensionIndex);
			LevelViewHandle lv = dv.getLevel(ev.levelIndex);

			if (!isLayoutDownThenOver && lv.getAggregationHeader() != null
					&& AGGREGATION_HEADER_LOCATION_BEFORE.equals(lv.getAggregationHeaderLocation())) {
				return false;
			}

			// check start edge position
			int gdx = GroupUtil.getGroupIndex(rowGroups, ev.dimensionIndex, ev.levelIndex);

			if (gdx != -1) {
				try {
					EdgeCursor rowEdgeCursor = getRowEdgeCursor();

					if (rowEdgeCursor != null) {
						DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor().get(gdx);

						if (rowEdgeCursor.getPosition() != dc.getEdgeStart()) {
							return false;
						}
					}
				} catch (OLAPException e) {
					logger.log(Level.SEVERE, Messages.getString("CrosstabSubTotalRowExecutor.error.check.edge.start"), //$NON-NLS-1$
							e);
				}
			}

		}

		return isFirstTotalRow;
	}

	protected boolean checkMeasureVerticalSpanOverlapped(ColumnEvent ev) {
		if (ev.measureIndex == -1 && totalMeasureCount != 1) {
			// TODO vertical multi meausures, not support span now
			return false;
		}

		int mx = ev.measureIndex;

		if (mx == -1) {
			// for verical measure, always use first one
			mx = 0;
		}

		LevelHandle spanLevel = null;

		switch (ev.type) {
		case ColumnEvent.MEASURE_CHANGE:
		case ColumnEvent.COLUMN_EDGE_CHANGE:

			spanLevel = getMeasureCell(mx).getSpanOverOnRow();
			break;

		case ColumnEvent.COLUMN_TOTAL_CHANGE:
		case ColumnEvent.GRAND_TOTAL_CHANGE:

			int dimCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
			DimensionViewHandle rdv = crosstabItem.getDimension(ROW_AXIS_TYPE, dimCount - 1);

			spanLevel = getAggregationCell(dimCount - 1, rdv.getLevelCount() - 1, ev.dimensionIndex, ev.levelIndex, mx)
					.getSpanOverOnRow();
			break;
		}

		if (spanLevel != null) {
			int targetRowSpanGroupIndex = GroupUtil.getGroupIndex(rowGroups, spanLevel);

			int currentGroupIndex = GroupUtil.getGroupIndex(rowGroups, dimensionIndex, levelIndex);

			if (targetRowSpanGroupIndex != -1) {
				return targetRowSpanGroupIndex <= currentGroupIndex;
			}
		}

		return false;
	}

	protected void advance() {
		int mx;

		try {
			while (walker.hasNext()) {
				ColumnEvent ev = walker.next();

				switch (currentChangeType) {
				case ColumnEvent.ROW_EDGE_CHANGE:

					if (rowEdgeStarted) {
						nextExecutor = new CrosstabCellExecutor(
								this, crosstabItem.getDimension(ROW_AXIS_TYPE, lastDimensionIndex)
										.getLevel(lastLevelIndex).getCell(),
								rowSpan, colSpan, currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						rowEdgeStarted = false;
						hasLast = false;
					} else if (rowSubTotalStarted && ev.type != ColumnEvent.ROW_EDGE_CHANGE) {
						nextExecutor = new CrosstabCellExecutor(this,
								crosstabItem.getDimension(ROW_AXIS_TYPE, dimensionIndex).getLevel(levelIndex)
										.getAggregationHeader(),
								rowSpan, colSpan, currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						rowSubTotalStarted = false;
						hasLast = false;
					}
					break;
				case ColumnEvent.MEASURE_HEADER_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this,
							getSubTotalMeasureHeaderCell(ROW_AXIS_TYPE, dimensionIndex, levelIndex, rowIndex), rowSpan,
							colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				case ColumnEvent.MEASURE_CHANGE:
				case ColumnEvent.COLUMN_EDGE_CHANGE:
				case ColumnEvent.COLUMN_TOTAL_CHANGE:
				case ColumnEvent.GRAND_TOTAL_CHANGE:

					mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

					if (measureDetailStarted
							&& isMeetMeasureDetailEnd(ev, getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx))) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureDetailStarted = false;
						hasLast = false;
					} else if (measureSubTotalStarted) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureSubTotalStarted = false;
						hasLast = false;
					} else if (measureGrandTotalStarted) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureGrandTotalStarted = false;
						hasLast = false;
					}

					break;
				}

				if (isRowEdgeNeedStart(ev)) {
					rowEdgeStarted = true;
					rowSpan = GroupUtil.computeRowSpan(crosstabItem, rowGroups, ev.dimensionIndex, ev.levelIndex,
							getRowEdgeCursor(), isLayoutDownThenOver);
					colSpan = 0;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (!rowSubTotalStarted && ev.type == ColumnEvent.ROW_EDGE_CHANGE
						&& ev.dimensionIndex == startTotalDimensionIndex && ev.levelIndex == startTotalLevelIndex
						&& isFirstTotalRow) {
					rowSubTotalStarted = true;

					rowSpan = totalRowSpan;
					colSpan = 0;
					hasLast = true;
				} else if (isMeasureDetailNeedStart(ev)) {
					measureDetailStarted = true;
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					if (columnGroups != null && columnGroups.size() > 0) {
						EdgeGroup gp = (EdgeGroup) columnGroups.get(columnGroups.size() - 1);
						lastDimensionIndex = gp.dimensionIndex;
						lastLevelIndex = gp.levelIndex;
					} else {
						lastDimensionIndex = ev.dimensionIndex;
						lastLevelIndex = ev.levelIndex;
					}
					hasLast = true;
				} else if (isMeasureSubTotalNeedStart(ev)) {
					measureSubTotalStarted = true;
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (isMeasureGrandTotalNeedStart(ev)) {
					measureGrandTotalStarted = true;
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (ev.type == ColumnEvent.MEASURE_HEADER_CHANGE) {
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				}

				currentChangeType = ev.type;
				currentEdgePosition = ev.dataPosition;
				colSpan++;
				currentColIndex++;

				if (nextExecutor != null) {
					return;
				}
			}

		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabSubTotalRowExecutor.error.retrieve.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			switch (currentChangeType) {
			case ColumnEvent.ROW_EDGE_CHANGE:

				if (rowEdgeStarted) {
					nextExecutor = new CrosstabCellExecutor(this, crosstabItem
							.getDimension(ROW_AXIS_TYPE, lastDimensionIndex).getLevel(lastLevelIndex).getCell(),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					rowEdgeStarted = false;
				} else if (rowSubTotalStarted) {
					nextExecutor = new CrosstabCellExecutor(this, crosstabItem
							.getDimension(ROW_AXIS_TYPE, dimensionIndex).getLevel(levelIndex).getAggregationHeader(),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);
				}
				break;
			case ColumnEvent.MEASURE_HEADER_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this,
						getSubTotalMeasureHeaderCell(ROW_AXIS_TYPE, dimensionIndex, levelIndex, rowIndex), rowSpan,
						colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				break;
			case ColumnEvent.MEASURE_CHANGE:
			case ColumnEvent.COLUMN_EDGE_CHANGE:
			case ColumnEvent.COLUMN_TOTAL_CHANGE:
			case ColumnEvent.GRAND_TOTAL_CHANGE:
				break;
			}

			if (measureDetailStarted) {
				mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

				nextExecutor = new CrosstabCellExecutor(this,
						getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureDetailStarted = false;
			} else if (measureSubTotalStarted) {
				mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

				nextExecutor = new CrosstabCellExecutor(this,
						getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureSubTotalStarted = false;
			} else if (measureGrandTotalStarted) {
				mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

				nextExecutor = new CrosstabCellExecutor(this,
						getRowSubTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureGrandTotalStarted = false;
			}
		}
	}
}
