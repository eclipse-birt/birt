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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;

/**
 * ColumnWalker
 */
class ColumnWalker3 implements ICrosstabConstants, IColumnWalker {

	private static final int STATE_INIT = 0;
	private static final int STATE_ROW_EDGE = 1;
	private static final int STATE_MEASURE_HEADER = 2;
	private static final int STATE_COLUMN_TOTAL_BEFORE = 3;
	private static final int STATE_COLUMN_TOTAL_AFTER = 4;
	private static final int STATE_COLUMN_EDGE = 5;
	private static final int STATE_GRAND_TOTAL = 6;
	private static final int STATE_MEASURE = 7;
	private static final int STATE_END = 10;

	private CrosstabReportItemHandle crosstabItem;
	private EdgeCursor columnEdgeCursor;

	private final int rdCount, cdCount, mCount;
	private final boolean isVerticalMeasure;

	private int currentState;
	private int dimensionIndex, levelIndex, measureIndex;

	private int tmpStartDimensionIndex, tmpStartLevelIndex, tmpEndDimensionIndex, tmpEndLevelIndex;
	private List columnDimensionCursors;
	private boolean hasNext, columnProcessed;

	// this is used to skip subtotal check for innerest column level
	private final int lastGroupIndex;

	ColumnWalker3(CrosstabReportItemHandle item, EdgeCursor columnEdgeCursor) {
		this.crosstabItem = item;
		this.columnEdgeCursor = columnEdgeCursor;

		rdCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
		cdCount = crosstabItem.getDimensionCount(COLUMN_AXIS_TYPE);
		mCount = crosstabItem.getMeasureCount();

		isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection());

		dimensionIndex = 0;
		levelIndex = -1;
		measureIndex = -1;

		if (cdCount > 0) {
			lastGroupIndex = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, cdCount - 1, -1);
		} else {
			lastGroupIndex = -1;
		}

		currentState = STATE_INIT;
	}

	public void reload() {
		dimensionIndex = 0;
		levelIndex = -1;
		measureIndex = -1;

		currentState = STATE_INIT;
	}

	public boolean hasNext() throws OLAPException {
		if (currentState == STATE_INIT) {
			advance();
		}
		return currentState != STATE_END;
	}

	private void advance() throws OLAPException {
		boolean first;

		switch (currentState) {
		case STATE_INIT:
		case STATE_ROW_EDGE:

			if (rdCount > 0) {
				// process row dimension header column if available
				first = true;

				for (int i = dimensionIndex; i < rdCount; i++) {
					DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, i);

					int start = 0;
					if (first) {
						start = levelIndex + 1;
						first = false;
					}

					for (int j = start; j < dv.getLevelCount(); j++) {
						dimensionIndex = i;
						levelIndex = j;
						currentState = STATE_ROW_EDGE;
						return;
					}
				}
			}

			// process vertical measure header column if available
			if (mCount > 0 && isVerticalMeasure) {
				for (int i = 0; i < mCount; i++) {
					MeasureViewHandle mv = crosstabItem.getMeasure(i);

					if (mv.getHeader() != null) {
						currentState = STATE_MEASURE_HEADER;
						return;
					}
				}
			}

		case STATE_MEASURE_HEADER:

			if (cdCount > 0) {
				columnDimensionCursors = columnEdgeCursor.getDimensionCursor();

				columnEdgeCursor.beforeFirst();
				hasNext = columnEdgeCursor.next();
				columnProcessed = false;

				dimensionIndex = 0;
				levelIndex = -1;
				measureIndex = -1;

				tmpStartDimensionIndex = 0;
				tmpStartLevelIndex = -1;

				tmpEndDimensionIndex = cdCount - 1;
				tmpEndLevelIndex = 0;
			}

		case STATE_COLUMN_TOTAL_BEFORE:
		case STATE_COLUMN_TOTAL_AFTER:
		case STATE_COLUMN_EDGE:

			if (cdCount > 0) {
				while (hasNext) {
					if (mCount > 0 || !IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE) {
						// check header
						first = true;

						for (int i = tmpStartDimensionIndex; i < cdCount; i++) {
							DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, i);

							int start = 0;
							if (first) {
								start = tmpStartLevelIndex + 1;
								first = false;
							}

							for (int j = start; j < dv.getLevelCount(); j++) {
								int positionIndex = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, i, j);

								if (positionIndex < lastGroupIndex) {
									DimensionCursor dc = (DimensionCursor) columnDimensionCursors
											.get(positionIndex + 1);

									if (dc.getEdgeStart() == columnEdgeCursor.getPosition()) {
										// process leading subtoal column if
										// available
										LevelViewHandle lv = dv.getLevel(j);
										if (lv.getAggregationHeader() != null && AGGREGATION_HEADER_LOCATION_BEFORE
												.equals(lv.getAggregationHeaderLocation())) {
											if (mCount > 0 && !isVerticalMeasure) {
												for (int m = measureIndex + 1; m < mCount; m++) {
													if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, i, j,
															m)) {
														tmpStartDimensionIndex = i;
														tmpStartLevelIndex = j - 1;
														dimensionIndex = i;
														levelIndex = j;
														measureIndex = m;
														currentState = STATE_COLUMN_TOTAL_BEFORE;
														return;
													}
												}

												// reset measure index
												measureIndex = -1;
											} else if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, i, j,
													-1)) {
												tmpStartDimensionIndex = i;
												tmpStartLevelIndex = j;
												dimensionIndex = i;
												levelIndex = j;
												currentState = STATE_COLUMN_TOTAL_BEFORE;
												return;
											}
										}
									}
								}
							}
						}

						// set to skip later header check
						tmpStartDimensionIndex = cdCount;
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
						first = true;

						for (int i = tmpEndDimensionIndex; i >= 0; i--) {
							DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, i);

							int end = dv.getLevelCount() - 1;
							if (first) {
								end = dv.getLevelCount() + tmpEndLevelIndex - 1;
								first = false;
							}

							for (int j = end; j >= 0; j--) {
								int positionIndex = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, i, j);

								if (positionIndex < lastGroupIndex) {
									DimensionCursor dc = (DimensionCursor) columnDimensionCursors
											.get(positionIndex + 1);

									if (dc.getEdgeEnd() == columnEdgeCursor.getPosition()) {
										// process trailing subtoal column
										// if
										// available
										LevelViewHandle lv = dv.getLevel(j);
										if (lv.getAggregationHeader() != null && AGGREGATION_HEADER_LOCATION_AFTER
												.equals(lv.getAggregationHeaderLocation())) {
											if (mCount > 0 && !isVerticalMeasure) {
												for (int m = measureIndex + 1; m < mCount; m++) {
													if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, i, j,
															m)) {
														tmpEndDimensionIndex = i;
														tmpEndLevelIndex = j - dv.getLevelCount() + 1;
														dimensionIndex = i;
														levelIndex = j;
														measureIndex = m;
														currentState = STATE_COLUMN_TOTAL_AFTER;
														return;
													}
												}

												// reset measure index
												measureIndex = -1;
											} else if (GroupUtil.hasTotalContent(crosstabItem, COLUMN_AXIS_TYPE, i, j,
													-1)) {
												tmpEndDimensionIndex = i;
												tmpEndLevelIndex = j - dv.getLevelCount();
												dimensionIndex = i;
												levelIndex = j;
												currentState = STATE_COLUMN_TOTAL_AFTER;
												return;
											}
										}
									}
								}
							}
						}
					}

					hasNext = columnEdgeCursor.next();

					// reset temp index
					columnProcessed = false;
					tmpStartDimensionIndex = 0;
					tmpStartLevelIndex = -1;
					tmpEndDimensionIndex = cdCount - 1;
					tmpEndLevelIndex = 0;
					measureIndex = -1;
				}
			}

			// reset measure index
			measureIndex = -1;

		case STATE_GRAND_TOTAL:

			if (cdCount > 0) {
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
			} else if (measureIndex == -1) {
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

		advance();

		return evt;
	}

}
