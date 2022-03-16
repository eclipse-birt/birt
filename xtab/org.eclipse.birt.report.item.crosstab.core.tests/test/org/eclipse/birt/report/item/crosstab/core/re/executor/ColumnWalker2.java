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

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
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
class ColumnWalker2 implements ICrosstabConstants {

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
	private CubeCursor cubeCursor;

	private int currentState;
	private int rdCount, cdCount, mCount;
	private int dimensionIndex, levelIndex, measureIndex;
	private boolean isVerticalMeasure;

	private int tmpStartDimensionIndex, tmpStartLevelIndex, tmpEndDimensionIndex, tmpEndLevelIndex;
	private EdgeCursor columnEdgeCursor;
	private List columnDimensionCursors;
	private long[] columnDimensionPositions;
	private boolean hasNext, hasAny;

	// this is used to skip subtotal check for innerest column level
	private int lastGroupIndex;

	ColumnWalker2(CrosstabReportItemHandle item, CubeCursor cursor) {
		this.crosstabItem = item;
		this.cubeCursor = cursor;

		rdCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
		cdCount = crosstabItem.getDimensionCount(COLUMN_AXIS_TYPE);
		mCount = crosstabItem.getMeasureCount();

		isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection());

		dimensionIndex = 0;
		levelIndex = -1;
		measureIndex = -1;

		lastGroupIndex = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, cdCount - 1, -1);

		currentState = STATE_INIT;
	}

	boolean hasNext() throws OLAPException {
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
				// TODO ensure first is always column edge cursor?
				columnEdgeCursor = (EdgeCursor) cubeCursor.getOrdinateEdge().get(0);
				columnDimensionCursors = columnEdgeCursor.getDimensionCursor();
				columnDimensionPositions = new long[columnDimensionCursors.size()];

				assert columnDimensionPositions.length == lastGroupIndex + 1;

				columnEdgeCursor.beforeFirst();
				hasNext = columnEdgeCursor.next();
				hasAny = hasNext;

				dimensionIndex = 0;
				levelIndex = -1;
				measureIndex = -1;

				tmpStartDimensionIndex = 0;
				tmpStartLevelIndex = -1;

				tmpEndDimensionIndex = 0;
				tmpEndLevelIndex = -1;
			}

		case STATE_COLUMN_TOTAL_BEFORE:
		case STATE_COLUMN_TOTAL_AFTER:
		case STATE_COLUMN_EDGE:

			if (cdCount > 0) {
				while (hasNext) {
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

							DimensionCursor dc = (DimensionCursor) columnDimensionCursors.get(positionIndex);
							long p = dc.getPosition();

							if (columnDimensionPositions[positionIndex] == 0) {
								// process leading subtoal column if
								// available
								LevelViewHandle lv = dv.getLevel(j);
								if (positionIndex != lastGroupIndex && lv.getAggregationHeader() != null
										&& AGGREGATION_HEADER_LOCATION_BEFORE
												.equals(lv.getAggregationHeaderLocation())) {
									if (mCount > 0 && !isVerticalMeasure) {
										for (int m = measureIndex + 1; m < mCount; m++) {
											tmpStartDimensionIndex = i;
											tmpStartLevelIndex = j - 1;
											dimensionIndex = i;
											levelIndex = j;
											measureIndex = m;
											currentState = STATE_COLUMN_TOTAL_BEFORE;
											return;
										}

										// reset measure index
										measureIndex = -1;
									} else {
										columnDimensionPositions[positionIndex] = p;

										tmpStartDimensionIndex = i;
										tmpStartLevelIndex = j;
										dimensionIndex = i;
										levelIndex = j;
										currentState = STATE_COLUMN_TOTAL_BEFORE;
										return;
									}
								}

								columnDimensionPositions[positionIndex] = p;
							} else if (p != columnDimensionPositions[positionIndex]) {
								boolean subFirst = true;

								if (currentState != STATE_COLUMN_TOTAL_BEFORE) {
									// check trailing subtotal column for
									// each level
									for (int t = tmpEndDimensionIndex; t >= i; t--) {
										DimensionViewHandle sdv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, t);

										int subStart = 0;
										int subEnd = sdv.getLevelCount() - 1;

										if (subFirst) {
											subEnd = sdv.getLevelCount() + tmpEndLevelIndex - 1;
											subFirst = false;
										}

										if (t == i) {
											subStart = j;
										}

										for (int u = subEnd; u >= subStart; u--) {
											LevelViewHandle slv = sdv.getLevel(u);

											int cuPx = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, t, u);

											// process trailing subtotal
											// column if available
											if (cuPx != lastGroupIndex && slv.getAggregationHeader() != null
													&& AGGREGATION_HEADER_LOCATION_AFTER
															.equals(slv.getAggregationHeaderLocation())) {
												if (mCount > 0 && !isVerticalMeasure) {
													for (int m = measureIndex + 1; m < mCount; m++) {
														tmpEndDimensionIndex = t;
														tmpEndLevelIndex = u - sdv.getLevelCount() + 1;
														dimensionIndex = t;
														levelIndex = u;
														measureIndex = m;
														currentState = STATE_COLUMN_TOTAL_AFTER;
														return;
													}

													// reset measure index
													measureIndex = -1;
												} else {
													tmpEndDimensionIndex = t;
													tmpEndLevelIndex = u - sdv.getLevelCount();
													dimensionIndex = t;
													levelIndex = u;
													currentState = STATE_COLUMN_TOTAL_AFTER;
													return;
												}
											}
										}
									}

									subFirst = true;

									tmpEndDimensionIndex = i;
									tmpEndLevelIndex = j;
									measureIndex = -1;
								}

								// check leading subtotal column for each
								// level
								for (int t = tmpEndDimensionIndex; t < cdCount; t++) {
									DimensionViewHandle sdv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, t);

									int subStart = 0;
									if (subFirst) {
										subStart = tmpEndLevelIndex;
										subFirst = false;
									}

									for (int u = subStart; u < sdv.getLevelCount(); u++) {
										LevelViewHandle slv = sdv.getLevel(u);

										int cuPx = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, t, u);

										// process leading subtotal column
										// if available
										if (cuPx != lastGroupIndex && slv.getAggregationHeader() != null
												&& AGGREGATION_HEADER_LOCATION_BEFORE
														.equals(slv.getAggregationHeaderLocation())) {
											if (mCount > 0 && !isVerticalMeasure) {
												for (int m = measureIndex + 1; m < mCount; m++) {
													tmpEndDimensionIndex = t;
													tmpEndLevelIndex = u;
													dimensionIndex = t;
													levelIndex = u;
													measureIndex = m;
													currentState = STATE_COLUMN_TOTAL_BEFORE;
													return;
												}

												// reset measure index
												measureIndex = -1;
											} else {
												tmpEndDimensionIndex = t;
												tmpEndLevelIndex = u + 1;
												dimensionIndex = t;
												levelIndex = u;
												currentState = STATE_COLUMN_TOTAL_BEFORE;
												return;
											}
										}
									}
								}

								columnDimensionPositions[positionIndex] = p;

								// cursor stepped, reset all sublevel index
								for (int m = positionIndex + 1; m < columnDimensionPositions.length; m++) {
									columnDimensionPositions[m] = 1;
								}
							}

						}
					}

					if (currentState != STATE_COLUMN_EDGE) {
						measureIndex = -1;
					}

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

					hasNext = columnEdgeCursor.next();

					// reset temp index
					tmpStartDimensionIndex = 0;
					tmpStartLevelIndex = -1;
					tmpEndDimensionIndex = cdCount - 1;
					tmpEndLevelIndex = 0;
					measureIndex = -1;
				}

				if (hasAny) {
					if (currentState != STATE_COLUMN_TOTAL_AFTER) {
						// reset tmp index
						tmpStartDimensionIndex = 0;
						tmpStartLevelIndex = -1;
						tmpEndDimensionIndex = cdCount - 1;
						tmpEndLevelIndex = 0;
						measureIndex = -1;
					}

					first = true;

					// check trailing subtotal column for each level

					for (int t = tmpEndDimensionIndex; t >= 0; t--) {
						DimensionViewHandle sdv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, t);

						int subEnd = sdv.getLevelCount() - 1;

						if (first) {
							subEnd = sdv.getLevelCount() + tmpEndLevelIndex - 1;
							first = false;
						}

						for (int u = subEnd; u >= 0; u--) {
							LevelViewHandle slv = sdv.getLevel(u);

							int cuPx = GroupUtil.getGroupIndex(crosstabItem, COLUMN_AXIS_TYPE, t, u);

							// process trailing subtotal column
							// if available
							if (cuPx != lastGroupIndex && slv.getAggregationHeader() != null
									&& AGGREGATION_HEADER_LOCATION_AFTER.equals(slv.getAggregationHeaderLocation())) {
								if (mCount > 0 && !isVerticalMeasure) {
									for (int m = measureIndex + 1; m < mCount; m++) {
										tmpEndDimensionIndex = t;
										tmpEndLevelIndex = u - sdv.getLevelCount() + 1;
										dimensionIndex = t;
										levelIndex = u;
										measureIndex = m;
										currentState = STATE_COLUMN_TOTAL_AFTER;
										return;
									}

									// reset measure index
									measureIndex = -1;
								} else {
									tmpEndDimensionIndex = t;
									tmpEndLevelIndex = u - sdv.getLevelCount();
									dimensionIndex = t;
									levelIndex = u;
									currentState = STATE_COLUMN_TOTAL_AFTER;
									return;
								}
							}
						}
					}
				}
			}

			// reset measure index
			measureIndex = -1;

		case STATE_GRAND_TOTAL:

			if (cdCount > 0) {
				// process grand total column
				if (crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null) {
					if (mCount > 0 && !isVerticalMeasure) {
						for (int i = measureIndex + 1; i < mCount; i++) {
							measureIndex = i;
							currentState = STATE_GRAND_TOTAL;
							return;
						}
					} else if (currentState != STATE_GRAND_TOTAL) {
						currentState = STATE_GRAND_TOTAL;
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
			} else if (measureIndex == -1) {
				measureIndex--;
				currentState = STATE_MEASURE;
				return;
			}

			currentState = STATE_END;
		}
	}

	ColumnEvent next() throws OLAPException {
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

		advance();

		return evt;
	}

}
