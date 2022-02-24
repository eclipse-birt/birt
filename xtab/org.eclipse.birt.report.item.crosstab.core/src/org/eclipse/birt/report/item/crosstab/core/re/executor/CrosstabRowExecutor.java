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
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * CrosstabRowExecutor
 */
public class CrosstabRowExecutor extends BaseRowExecutor {

	private static Logger logger = Logger.getLogger(CrosstabRowExecutor.class.getName());

	private int dimensionIndex;
	private int levelIndex;

	private boolean rowEdgeStarted;

	private boolean isLayoutDownThenOver;

	public CrosstabRowExecutor(BaseCrosstabExecutor parent, int rowIndex, int dimensionIndex, int levelIndex) {
		super(parent, rowIndex);

		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
	}

	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		processRowHeight(findDetailRowCell(rowIndex));

		processRowLevelPageBreak(content, false);

		prepareChildren();

		return content;
	}

	protected void prepareChildren() {
		super.prepareChildren();

		initMeasureCache();

		rowEdgeStarted = false;
		isLayoutDownThenOver = PAGE_LAYOUT_DOWN_THEN_OVER.equals(crosstabItem.getPageLayout());

		walker.reload();
	}

	private AggregationCellHandle getRowSubTotalCell(int colDimensionIndex, int colLevelIndex, int measureIndex) {
		return getAggregationCell(dimensionIndex, levelIndex, colDimensionIndex, colLevelIndex, measureIndex);
	}

	private boolean isForceEmpty() {
		try {
			EdgeCursor rowEdgeCursor = getRowEdgeCursor();

			if (rowEdgeCursor != null) {
				int groupIndex = GroupUtil.getGroupIndex(rowGroups, lastDimensionIndex, lastLevelIndex);

				DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor().get(groupIndex);

				return GroupUtil.isDummyGroup(dc);
			}
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabRowExecutor.error.check.force.empty"), //$NON-NLS-1$
					e);
		}

		return false;
	}

	private boolean isRowEdgeNeedStart(ColumnEvent ev) throws OLAPException {
		if (rowEdgeStarted || ev.type != ColumnEvent.ROW_EDGE_CHANGE) {
			return false;
		}

		EdgeCursor rowEdgeCursor = getRowEdgeCursor();

		if (rowEdgeCursor == null) {
			return false;
		}

		// check when previous subtotal already processed
		boolean groupFound = false;

		for (int i = 0; i < rowGroups.size() - 1; i++) {
			EdgeGroup gp = (EdgeGroup) rowGroups.get(i);

			if (!groupFound && gp.dimensionIndex == ev.dimensionIndex && gp.levelIndex == ev.levelIndex) {
				groupFound = true;
			}

			if (isLayoutDownThenOver && groupFound && gp.dimensionIndex == ev.dimensionIndex
					&& gp.levelIndex == ev.levelIndex) {
				// skip self
				continue;
			}

			// only check with non-leaf groups
			if (groupFound && !GroupUtil.isLeafGroup(rowEdgeCursor.getDimensionCursor(), i)) {
				DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, gp.dimensionIndex);
				LevelViewHandle lv = dv.getLevel(gp.levelIndex);

				if (lv.getAggregationHeader() != null
						&& AGGREGATION_HEADER_LOCATION_BEFORE.equals(lv.getAggregationHeaderLocation())) {
					return false;
				}
			}
		}

		// check start edge position
		int gdx = GroupUtil.getGroupIndex(rowGroups, ev.dimensionIndex, ev.levelIndex);

		if (gdx != -1) {
			try {
				DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor().get(gdx);

				if (!GroupUtil.isDummyGroup(dc) && rowEdgeCursor.getPosition() != dc.getEdgeStart()) {
					return false;
				}
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabRowExecutor.error.check.edge.start"), //$NON-NLS-1$
						e);
			}
		}

		return rowIndex == 0;
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

			AggregationCellHandle cellHandle = getAggregationCell(dimCount - 1, rdv.getLevelCount() - 1,
					ev.dimensionIndex, ev.levelIndex, mx);
			spanLevel = (cellHandle != null) ? cellHandle.getSpanOverOnRow() : null;
			break;
		}

		if (spanLevel != null) {
			int targetSpanGroupIndex = GroupUtil.getGroupIndex(rowGroups, spanLevel);

			if (targetSpanGroupIndex != -1) {
				try {
					EdgeCursor rowEdgeCursor = getRowEdgeCursor();

					if (rowEdgeCursor != null) {
						// use preview group cursor to check edge start.
						targetSpanGroupIndex--;

						if (targetSpanGroupIndex == -1) {
							// this is the outer-most level, it never ends until
							// whole
							// edge ends.
							return !rowEdgeCursor.isFirst();
						} else {
							DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor()
									.get(targetSpanGroupIndex);

							if (!GroupUtil.isDummyGroup(dc)) {
								return rowEdgeCursor.getPosition() > dc.getEdgeStart();
							}
						}
					}
				} catch (OLAPException e) {
					logger.log(Level.SEVERE, Messages.getString("CrosstabRowExecutor.error.check.edge.start"), //$NON-NLS-1$
							e);
				}
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

						((CrosstabCellExecutor) nextExecutor).setForceEmpty(isForceEmpty());

						rowEdgeStarted = false;
						hasLast = false;
					}
					break;
				case ColumnEvent.MEASURE_HEADER_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(rowIndex).getHeader(),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				case ColumnEvent.MEASURE_CHANGE:
				case ColumnEvent.COLUMN_EDGE_CHANGE:
				case ColumnEvent.COLUMN_TOTAL_CHANGE:
				case ColumnEvent.GRAND_TOTAL_CHANGE:

					mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

					if (measureDetailStarted
							&& isMeetMeasureDetailEnd(ev, totalMeasureCount > 0 ? getMeasureCell(mx) : null)) {
						nextExecutor = new CrosstabCellExecutor(this, totalMeasureCount > 0 ? getMeasureCell(mx) : null,
								rowSpan, colSpan, currentColIndex - colSpan + 1);

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
				} else if (isMeasureDetailNeedStart(ev)) {
					measureDetailStarted = true;

					if (ev.measureIndex == -1 && totalMeasureCount != 1) {
						// TODO vertical multi meausures, not support span now
						rowSpan = 1;
					} else {
						mx = ev.measureIndex;

						if (mx == -1) {
							// for verical measures, always use first measure
							mx = 0;
						}

						rowSpan = GroupUtil.computeAggregationCellRowOverSpan(crosstabItem, rowGroups,
								getMeasureCell(mx).getSpanOverOnRow(), getRowEdgeCursor());
					}
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (isMeasureSubTotalNeedStart(ev)) {
					measureSubTotalStarted = true;

					if (ev.measureIndex == -1 && totalMeasureCount != 1) {
						// TODO vertical multi meausures, not support span now
						rowSpan = 1;
					} else {
						mx = ev.measureIndex;

						if (mx == -1) {
							// for verical measures, always use first measure
							mx = 0;
						}

						int dimCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
						DimensionViewHandle rdv = crosstabItem.getDimension(ROW_AXIS_TYPE, dimCount - 1);

						AggregationCellHandle cellHandle = getAggregationCell(dimCount - 1, rdv.getLevelCount() - 1,
								ev.dimensionIndex, ev.levelIndex, mx);
						rowSpan = GroupUtil.computeAggregationCellRowOverSpan(crosstabItem, rowGroups,
								(cellHandle != null) ? cellHandle.getSpanOverOnRow() : null, getRowEdgeCursor());
					}
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (isMeasureGrandTotalNeedStart(ev)) {
					measureGrandTotalStarted = true;

					if (ev.measureIndex == -1 && totalMeasureCount != 1) {
						// TODO vertical multi meausures, not support span now
						rowSpan = 1;
					} else {
						mx = ev.measureIndex;

						if (mx == -1) {
							// for verical measures, always use first measure
							mx = 0;
						}

						int dimCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
						DimensionViewHandle rdv = crosstabItem.getDimension(ROW_AXIS_TYPE, dimCount - 1);

						AggregationCellHandle cellHandle = getAggregationCell(dimCount - 1, rdv.getLevelCount() - 1,
								ev.dimensionIndex, ev.levelIndex, mx);
						rowSpan = GroupUtil.computeAggregationCellRowOverSpan(crosstabItem, rowGroups,
								(cellHandle != null) ? cellHandle.getSpanOverOnRow() : null, getRowEdgeCursor());
					}
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
			logger.log(Level.SEVERE, Messages.getString("CrosstabRowExecutor.error.retrieve.child.executor"), //$NON-NLS-1$
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

					((CrosstabCellExecutor) nextExecutor).setForceEmpty(isForceEmpty());

					rowEdgeStarted = false;
				}
				break;
			case ColumnEvent.MEASURE_HEADER_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(rowIndex).getHeader(), rowSpan,
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

				nextExecutor = new CrosstabCellExecutor(this, totalMeasureCount > 0 ? getMeasureCell(mx) : null,
						rowSpan, colSpan, currentColIndex - colSpan + 1);

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
