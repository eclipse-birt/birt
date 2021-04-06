/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;

/**
 * ColumnWalker
 */
class ColumnWalker implements ICrosstabConstants, IColumnWalker {

	private static final int STATE_INIT = 0;
	private static final int STATE_ROW_EDGE = 1;
	private static final int STATE_MEASURE_HEADER = 2;
	private static final int STATE_COLUMN_TOTAL_BEFORE = 3;
	private static final int STATE_COLUMN_TOTAL_AFTER = 4;
	private static final int STATE_COLUMN_EDGE = 5;
	private static final int STATE_GRAND_TOTAL = 6;
	private static final int STATE_MEASURE = 7;
	private static final int STATE_END = 10;
	private static final int STATE_PENDING_CHECK_COLUMN_EDGE = 20;

	private CrosstabReportItemHandle crosstabItem;
	private EdgeCursor columnEdgeCursor;

	private final List rowGroups, columnGroups;

	private final int mCount;
	private final boolean isVerticalMeasure;
	private final boolean isHideMeasureHeader;

	private int currentState;
	private int dimensionIndex, levelIndex, measureIndex;
	private int groupIndex;

	private int tmpStartGroupIndex, tmpEndGroupIndex;
	private List columnDimensionCursors;
	private boolean hasNext, columnProcessed;
	private boolean inProcessingGrandTotalBefore;

	// this is used to skip subtotal check for innerest column level
	private final int lastColumnGroupIndex;

	ColumnWalker(CrosstabReportItemHandle item, EdgeCursor columnEdgeCursor) {
		this.crosstabItem = item;
		this.columnEdgeCursor = columnEdgeCursor;

		rowGroups = GroupUtil.getGroups(crosstabItem, ROW_AXIS_TYPE);
		columnGroups = GroupUtil.getGroups(crosstabItem, COLUMN_AXIS_TYPE);

		mCount = crosstabItem.getMeasureCount();
		isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection());
		isHideMeasureHeader = crosstabItem.isHideMeasureHeader();

		groupIndex = 0;
		measureIndex = -1;

		lastColumnGroupIndex = columnGroups.size() - 1;

		currentState = STATE_INIT;
	}

	public void reload() {
		groupIndex = 0;
		measureIndex = -1;

		currentState = STATE_INIT;
	}

	public boolean hasNext() throws OLAPException {
		if (currentState == STATE_INIT) {
			safeAdvance();
		}
		return currentState != STATE_END;
	}

	private void safeAdvance() throws OLAPException {
		advance();

		if (currentState == STATE_PENDING_CHECK_COLUMN_EDGE) {
			// need advance again to recheck the state
			advance();
		}
	}

	private void advance() throws OLAPException {
		switch (currentState) {
		case STATE_INIT:
		case STATE_ROW_EDGE:

			if (rowGroups.size() > 0) {
				// TODO how to skip dummy groups?

				// process row dimension header column if available
				for (int i = groupIndex; i < rowGroups.size(); i++) {
					EdgeGroup group = (EdgeGroup) rowGroups.get(i);

					groupIndex++;

					dimensionIndex = group.dimensionIndex;
					levelIndex = group.levelIndex;
					currentState = STATE_ROW_EDGE;
					return;
				}
			}

			// process vertical measure header column if available
			if (mCount > 0 && isVerticalMeasure && !isHideMeasureHeader) {
				for (int i = 0; i < mCount; i++) {
					MeasureViewHandle mv = crosstabItem.getMeasure(i);

					if (mv.getHeader() != null) {
						currentState = STATE_MEASURE_HEADER;
						return;
					}
				}
			} else if (rowGroups.size() == 0 && groupIndex == 0 && crosstabItem.getHeader() != null) {
				// mark the flag as we only check once.
				groupIndex++;

				boolean hasHeaderContent = false;
				for (int i = 0; i < crosstabItem.getHeaderCount(); i++) {
					CrosstabCellHandle cell = crosstabItem.getHeader(i);

					if (cell.getContents().size() > 0) {
						hasHeaderContent = true;
						break;
					}
				}

				if (hasHeaderContent) {
					// in case it has no row edge but has non-empty header
					// cell, we produce a dummy row edge event to output the
					// crosstab header.

					dimensionIndex = -1;
					levelIndex = -1;
					currentState = STATE_ROW_EDGE;
					return;
				}
			}

		case STATE_MEASURE_HEADER:

			// check if need processing grandtotal_before
			inProcessingGrandTotalBefore = columnGroups.size() > 0 && columnEdgeCursor != null
					&& crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null && GRAND_TOTAL_LOCATION_BEFORE
							.equals(crosstabItem.getCrosstabView(COLUMN_AXIS_TYPE).getGrandTotalLocation());

		case STATE_PENDING_CHECK_COLUMN_EDGE:

			if (!inProcessingGrandTotalBefore && columnGroups.size() > 0 && columnEdgeCursor != null) {
				columnDimensionCursors = columnEdgeCursor.getDimensionCursor();

				columnEdgeCursor.beforeFirst();
				hasNext = columnEdgeCursor.next();
				columnProcessed = false;

				groupIndex = 0;
				measureIndex = -1;

				tmpStartGroupIndex = 0;
				tmpEndGroupIndex = lastColumnGroupIndex;
			}

		case STATE_COLUMN_TOTAL_BEFORE:
		case STATE_COLUMN_TOTAL_AFTER:
		case STATE_COLUMN_EDGE:

			if (!inProcessingGrandTotalBefore && columnGroups.size() > 0 && columnEdgeCursor != null) {
				while (hasNext) {
					if (mCount > 0 || !IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
						// check header
						for (int i = tmpStartGroupIndex; i < columnGroups.size(); i++) {
							EdgeGroup group = (EdgeGroup) columnGroups.get(i);

							if (!GroupUtil.isLeafOrDummyGroup(columnDimensionCursors, i)) {
								DimensionCursor dc = (DimensionCursor) columnDimensionCursors.get(i);

								if (dc.getEdgeStart() == columnEdgeCursor.getPosition()) {
									// process leading subtoal column if
									// available
									LevelViewHandle lv = crosstabItem
											.getDimension(COLUMN_AXIS_TYPE, group.dimensionIndex)
											.getLevel(group.levelIndex);

									if (lv.getAggregationHeader() != null && AGGREGATION_HEADER_LOCATION_BEFORE
											.equals(lv.getAggregationHeaderLocation())) {
										if (mCount > 0 && !isVerticalMeasure) {
											for (int m = measureIndex + 1; m < mCount; m++) {
												if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE,
														group.dimensionIndex, group.levelIndex, m)) {
													tmpStartGroupIndex = i;

													dimensionIndex = group.dimensionIndex;
													levelIndex = group.levelIndex;
													measureIndex = m;
													currentState = STATE_COLUMN_TOTAL_BEFORE;
													return;
												}
											}

											// reset measure index
											measureIndex = -1;
										} else if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE,
												group.dimensionIndex, group.levelIndex, -1)) {
											tmpStartGroupIndex = i + 1;

											dimensionIndex = group.dimensionIndex;
											levelIndex = group.levelIndex;
											currentState = STATE_COLUMN_TOTAL_BEFORE;
											return;
										}
									}
								}
							}
						}

						// set to skip later header check
						tmpStartGroupIndex = columnGroups.size();
					}

					// reset measure index
					if (currentState != STATE_COLUMN_EDGE && currentState != STATE_COLUMN_TOTAL_AFTER) {
						measureIndex = -1;
					}

					// check column
					if (!columnProcessed) {
						// add data columns per edge tuple
						if (mCount > 0 && !isVerticalMeasure) {
							for (int m = measureIndex + 1; m < mCount; m++) {
								measureIndex = m;
								currentState = STATE_COLUMN_EDGE;
								return;
							}
						} else if (measureIndex == -1) {
							measureIndex--;
							currentState = STATE_COLUMN_EDGE;
							return;
						}

						columnProcessed = true;
					}

					if (mCount > 0 || !IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
						// reset measure index
						if (currentState != STATE_COLUMN_TOTAL_AFTER) {
							measureIndex = -1;
						}

						// check footer
						for (int i = tmpEndGroupIndex; i >= 0; i--) {
							EdgeGroup group = (EdgeGroup) columnGroups.get(i);

							if (!GroupUtil.isLeafOrDummyGroup(columnDimensionCursors, i)) {
								DimensionCursor dc = (DimensionCursor) columnDimensionCursors.get(i);

								if (dc.getEdgeEnd() == columnEdgeCursor.getPosition()) {
									// process trailing subtoal column
									// if
									// available
									LevelViewHandle lv = crosstabItem
											.getDimension(COLUMN_AXIS_TYPE, group.dimensionIndex)
											.getLevel(group.levelIndex);

									if (lv.getAggregationHeader() != null && AGGREGATION_HEADER_LOCATION_AFTER
											.equals(lv.getAggregationHeaderLocation())) {
										if (mCount > 0 && !isVerticalMeasure) {
											for (int m = measureIndex + 1; m < mCount; m++) {
												if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE,
														group.dimensionIndex, group.levelIndex, m)) {
													tmpEndGroupIndex = i;

													dimensionIndex = group.dimensionIndex;
													levelIndex = group.levelIndex;
													measureIndex = m;
													currentState = STATE_COLUMN_TOTAL_AFTER;
													return;
												}
											}

											// reset measure index
											measureIndex = -1;
										} else if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE,
												group.dimensionIndex, group.levelIndex, -1)) {
											tmpEndGroupIndex = i - 1;

											dimensionIndex = group.dimensionIndex;
											levelIndex = group.levelIndex;
											currentState = STATE_COLUMN_TOTAL_AFTER;
											return;
										}
									}
								}
							}
						}
					}

					hasNext = columnEdgeCursor.next();

					// reset temp index
					columnProcessed = false;

					tmpStartGroupIndex = 0;
					tmpEndGroupIndex = lastColumnGroupIndex;

					measureIndex = -1;
				}

				// check if grandtotal already processed, otherwise, this is
				// already the end of column edge
				if (crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null && GRAND_TOTAL_LOCATION_BEFORE
						.equals(crosstabItem.getCrosstabView(COLUMN_AXIS_TYPE).getGrandTotalLocation())) {
					currentState = STATE_END;
					return;
				}
			}

			// reset measure index
			measureIndex = -1;

		case STATE_GRAND_TOTAL:

			if (columnGroups.size() > 0 && columnEdgeCursor != null) {
				// process grand total column
				if (crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null) {
					if (mCount > 0 || !IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
						if (mCount > 0 && !isVerticalMeasure) {
							for (int i = measureIndex + 1; i < mCount; i++) {
								if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, -1, -1, i)) {
									measureIndex = i;
									currentState = STATE_GRAND_TOTAL;
									return;
								}
							}
						} else if (currentState != STATE_GRAND_TOTAL) {
							if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, -1, -1, -1)) {
								currentState = STATE_GRAND_TOTAL;
								return;
							}
						}
					}

					// check if this is grandtotal_before, then forward the
					// processing to column edge
					if (inProcessingGrandTotalBefore) {
						inProcessingGrandTotalBefore = false;

						currentState = STATE_PENDING_CHECK_COLUMN_EDGE;
						return;
					}
				}

				currentState = STATE_END;
				return;
			}

		case STATE_MEASURE:

			// process measure columns in case no column edge defined
			if (!isVerticalMeasure) {
				for (int i = measureIndex + 1; i < mCount; i++) {
					measureIndex = i;
					currentState = STATE_MEASURE;
					return;
				}
			} else if (measureIndex == -1 && mCount > 0) {
				measureIndex--;
				currentState = STATE_MEASURE;
				return;
			}

			currentState = STATE_END;
			return;
		}
	}

	public ColumnEvent next() throws OLAPException {
		ColumnEvent evt = null;

		int mx = measureIndex < 0 ? -1 : measureIndex;

		switch (currentState) {
		case STATE_INIT:
			break;
		case STATE_ROW_EDGE:
			evt = new RowEdgeColumnEvent(dimensionIndex, levelIndex);
			break;
		case STATE_MEASURE_HEADER:
			evt = new MeasureHeaderColumnEvent();
			break;
		case STATE_COLUMN_TOTAL_BEFORE:
			evt = new ColumnTotalColumnEvent(true, dimensionIndex, levelIndex, mx);
			break;
		case STATE_COLUMN_TOTAL_AFTER:
			evt = new ColumnTotalColumnEvent(false, dimensionIndex, levelIndex, mx);
			break;
		case STATE_COLUMN_EDGE:
			evt = new ColumnEdgeColumnEvent(mx);
			break;
		case STATE_GRAND_TOTAL:
			evt = new GrandTotalColumnEvent(mx);
			break;
		case STATE_MEASURE:
			evt = new MeasureColumnEvent(mx);
			break;
		case STATE_END:
			break;
		}

		if (columnEdgeCursor != null) {
			evt.dataPosition = columnEdgeCursor.getPosition();
		}

		safeAdvance();

		return evt;
	}

}
