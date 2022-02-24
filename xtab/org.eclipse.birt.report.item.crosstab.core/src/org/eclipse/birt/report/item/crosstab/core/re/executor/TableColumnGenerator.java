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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;

/**
 * TableColumnGenerator
 */
public class TableColumnGenerator implements ICrosstabConstants {

	private static Logger logger = Logger.getLogger(TableColumnGenerator.class.getName());

	private IColumnWalker walker;
	private CrosstabReportItemHandle crosstabItem;
	// private IBaseResultSet resultSet;

	private EdgeCursor columnCursor;
	private List<DimensionCursor> groupCursors;
	private List<EdgeGroup> columnGroups;

	private int[] pageBreakBeforeInts, pageBreakAfterInts;
	private boolean[] hasTotalBefore, hasTotalAfter;
	private int[] firstTotalMeasureIndex, lastTotalMeasureIndex;

	private int[] columnLevelPageBreakIntervals;
	private long[] lastColumnLevelState;
	private long[] checkedColumnLevelState;

	private final int forcedColumnLevelPageBreakInterval;

	private int lastMeasureIndex;
	private int firstGrandTotalMeasureIndex;

	private int notifyNextPageBreak;

	private String rowDimension, rowLevel;

	private boolean repeatRowHeader;

	TableColumnGenerator(CrosstabReportItemHandle item, IColumnWalker walker, IBaseResultSet resultSet,
			EdgeCursor columnCursor, List<EdgeGroup> columnGroups) throws OLAPException {
		this.crosstabItem = item;
		this.walker = walker;
		// this.resultSet = resultSet;

		this.repeatRowHeader = crosstabItem.isRepeatRowHeader();

		this.columnLevelPageBreakIntervals = GroupUtil.getLevelPageBreakIntervals(crosstabItem, columnGroups,
				COLUMN_AXIS_TYPE);

		this.forcedColumnLevelPageBreakInterval = crosstabItem.getColumnPageBreakInterval();

		this.columnCursor = columnCursor;
		this.columnGroups = columnGroups;
		if (columnCursor != null) {
			groupCursors = columnCursor.getDimensionCursor();
		}

		if (columnGroups.size() > 0) {
			// init page break info

			pageBreakBeforeInts = new int[columnGroups.size()];
			pageBreakAfterInts = new int[columnGroups.size()];

			hasTotalBefore = new boolean[columnGroups.size()];
			hasTotalAfter = new boolean[columnGroups.size()];

			Arrays.fill(hasTotalBefore, false);
			Arrays.fill(hasTotalAfter, false);

			firstTotalMeasureIndex = new int[columnGroups.size()];
			lastTotalMeasureIndex = new int[columnGroups.size()];

			Arrays.fill(firstTotalMeasureIndex, -1);
			Arrays.fill(lastTotalMeasureIndex, -1);

			int totalMeasureCount = crosstabItem.getMeasureCount();

			lastMeasureIndex = totalMeasureCount - 1;
			firstGrandTotalMeasureIndex = -1;

			String pageBreak;

			boolean allowTotal = totalMeasureCount > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE;

			for (int i = 0; i < columnGroups.size(); i++) {
				EdgeGroup eg = columnGroups.get(i);

				LevelViewHandle lv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, eg.dimensionIndex)
						.getLevel(eg.levelIndex);

				// init ints flag
				// 1. 1 == page_break_before_always;
				// 2. 2 == page_break_before_always_excluding_first;
				pageBreak = lv.getPageBreakBefore();
				pageBreakBeforeInts[i] = DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals(pageBreak) ? 1
						: (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals(pageBreak) ? 2 : 0);

				// init ints flag
				// 1. 1 == page_break_after_always;
				// 2. 2 == page_break_after_always_excluding_last;
				pageBreak = lv.getPageBreakAfter();
				pageBreakAfterInts[i] = DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals(pageBreak) ? 1
						: (DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals(pageBreak) ? 2 : 0);

				if (allowTotal && i != columnGroups.size() - 1 && lv.getAggregationHeader() != null) {
					if (AGGREGATION_HEADER_LOCATION_BEFORE.equals(lv.getAggregationHeaderLocation())) {
						hasTotalBefore[i] = true;
					} else if (AGGREGATION_HEADER_LOCATION_AFTER.equals(lv.getAggregationHeaderLocation())) {
						hasTotalAfter[i] = true;
					}

					List mvs = lv.getAggregationMeasures();

					if (mvs.size() > 0) {
						firstTotalMeasureIndex[i] = ((MeasureViewHandle) mvs.get(0)).getIndex();
						lastTotalMeasureIndex[i] = ((MeasureViewHandle) mvs.get(mvs.size() - 1)).getIndex();
					}
				}
			}

			if (allowTotal && crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null) {
				List mvs = crosstabItem.getAggregationMeasures(COLUMN_AXIS_TYPE);

				if (mvs.size() > 0) {
					firstGrandTotalMeasureIndex = ((MeasureViewHandle) mvs.get(0)).getIndex();
				}
			}
		}

		int rdCount = crosstabItem.getDimensionCount(ROW_AXIS_TYPE);
		if (rdCount > 0) {
			// TODO check visibility
			DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, rdCount - 1);

			if (dv.getLevelCount() > 0) {
				LevelViewHandle lv = dv.getLevel(dv.getLevelCount() - 1);

				rowDimension = dv.getCubeDimensionName();
				rowLevel = lv.getCubeLevelName();
			}
		}
	}

	void generateColumns(IReportContent report, ITableContent table) throws OLAPException {
		while (walker.hasNext()) {
			ColumnEvent ce = walker.next();

			addColumn(ce, report, table);

			logger.log(Level.INFO, ce.toString());
		}

		handlePageBreak(table);
	}

	private String getDisplay(CrosstabCellHandle cell) {
		Object value = cell.getProperty(Style.DISPLAY_PROP);
		if (value != null) {
			return value.toString();
		}
		return null;
	}

	private void addColumn(ColumnEvent event, IReportContent report, ITableContent table) {
		Column col = new Column(report);

		CrosstabCellHandle cell = null;
		switch (event.type) {
		case ColumnEvent.ROW_EDGE_CHANGE:

			col.setColumnHeaderState(true);
			col.setRepeated(repeatRowHeader);

			if (event.dimensionIndex == -1 && event.levelIndex == -1) {
				// this is a dummy row edge event, we use header cell
				cell = crosstabItem.getHeader();
			} else {
				// use row level cell
				cell = crosstabItem.getDimension(ROW_AXIS_TYPE, event.dimensionIndex).getLevel(event.levelIndex)
						.getCell();
			}
			break;
		case ColumnEvent.MEASURE_HEADER_CHANGE:

			col.setColumnHeaderState(true);
			col.setRepeated(repeatRowHeader);

			// use first measure header cell
			int totalMeasureCount = crosstabItem.getMeasureCount();
			for (int i = 0; i < totalMeasureCount; i++) {
				MeasureViewHandle mv = crosstabItem.getMeasure(i);
				if (mv.getHeader() != null) {
					cell = mv.getHeader();
					break;
				}
			}
			break;
		case ColumnEvent.COLUMN_EDGE_CHANGE:
			if (crosstabItem.getMeasureCount() > 0) {
				int mx = event.measureIndex >= 0 ? event.measureIndex : 0;

				// use measure cell
				cell = crosstabItem.getMeasure(mx).getCell();
			} else {
				// TODO check visibility
				// use innerest column level cell
				DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE,
						crosstabItem.getDimensionCount(COLUMN_AXIS_TYPE) - 1);
				cell = dv.getLevel(dv.getLevelCount() - 1).getCell();
			}
			break;
		case ColumnEvent.COLUMN_TOTAL_CHANGE:
			if (crosstabItem.getMeasureCount() > 0) {
				int mx = event.measureIndex >= 0 ? event.measureIndex : 0;

				// use selected aggregation cell
				DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, event.dimensionIndex);
				LevelViewHandle lv = dv.getLevel(event.levelIndex);

				cell = crosstabItem.getMeasure(mx).getAggregationCell(rowDimension, rowLevel, dv.getCubeDimensionName(),
						lv.getCubeLevelName());
			} else {
				// use column sub total cell
				DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, event.dimensionIndex);
				cell = dv.getLevel(event.levelIndex).getAggregationHeader();
			}
			break;
		case ColumnEvent.GRAND_TOTAL_CHANGE:
			if (crosstabItem.getMeasureCount() > 0) {
				int mx = event.measureIndex >= 0 ? event.measureIndex : 0;

				// use selected aggregation cell
				cell = crosstabItem.getMeasure(mx).getAggregationCell(rowDimension, rowLevel, null, null);
			} else {
				// use column grand total cell
				cell = crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE);
			}
			break;
		case ColumnEvent.MEASURE_CHANGE:
			// use measure cell
			int mx = event.measureIndex >= 0 ? event.measureIndex : 0;
			cell = crosstabItem.getMeasure(mx).getCell();
			break;
		}

		try {
			DimensionHandle handle = crosstabItem.getColumnWidth(cell);
			if (handle != null) {
				DimensionType width = ContentUtil.createDimension(handle);

				if (width != null) {
					col.setWidth(width);
				}
			}
		} catch (CrosstabException e) {
			logger.log(Level.SEVERE, Messages.getString("TableColumnGenerator.error.process.column.width"), //$NON-NLS-1$
					e);
		}

		// set display
		String display = getDisplay(cell);

		if ("none".equals(display)) {
			col.getStyle().setDisplay("none");
		}

		// TODO temprarily commented out
		// process style
		// IStyle style = ContentUtil.processStyle( report, handle );
		// if ( style != null )
		// {
		// col.setInlineStyle( style );
		// }
		//
		// // process visibility
		// try
		// {
		// String visibleFormat = ContentUtil.processVisibility( handle,
		// resultSet );
		// if ( visibleFormat != null )
		// {
		// col.setVisibleFormat( visibleFormat );
		// }
		// }
		// catch ( BirtException e )
		// {
		// logger.log( Level.SEVERE,
		// Messages.getString(
		// "TableColumnGenerator.error.process.visibility" ), //$NON-NLS-1$
		// e );
		// }
		table.addColumn(col);
	}

	private void handlePageBreak(ITableContent table) throws OLAPException {
		if (columnCursor == null || columnGroups.size() == 0) {
			return;
		}

		walker.reload();

		int i = 0;

		int[] checkPoint = new int[] { -1 };

		// TODO fix potential issue for only have one global notify flag. May
		// need a flag stack.
		notifyNextPageBreak = -1;

		while (walker.hasNext()) {
			ColumnEvent ce = walker.next();

			if (ce.type == ColumnEvent.COLUMN_EDGE_CHANGE || ce.type == ColumnEvent.COLUMN_TOTAL_CHANGE
					|| ce.type == ColumnEvent.GRAND_TOTAL_CHANGE) {
				IColumn col = table.getColumn(i);

				handleColumnPageBreak(ce, col);

				if (forcedColumnLevelPageBreakInterval > 0) {
					// TODO watch out here the events must be contineous to
					// ensure the positions are contineous after
					// skipped the row header events.
					handleForcedColumnPageBreak(checkPoint, i, col);
				}
			}

			i++;
		}
	}

	private void handleForcedColumnPageBreak(int[] checkPoint, int position, IColumn col) {
		// handle forced page break interval
		if (checkPoint[0] == -1) {
			// record the current position only
			checkPoint[0] = position;
		} else if (position - checkPoint[0] >= forcedColumnLevelPageBreakInterval) {
			col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);

			checkPoint[0] = position;
		}
	}

	private void handleColumnPageBreak(ColumnEvent event, IColumn col) throws OLAPException {
		columnCursor.setPosition(event.dataPosition);

		if (event.type == ColumnEvent.COLUMN_TOTAL_CHANGE) {
			int currentGroupIndex = GroupUtil.getGroupIndex(columnGroups, event.dimensionIndex, event.levelIndex);

			if (event.isLocationBefore) {
				// only handle vertical measure case and first measure column
				if (event.measureIndex == -1 || event.measureIndex == firstTotalMeasureIndex[currentGroupIndex]) {
					boolean isFirst = groupCursors.get(currentGroupIndex).isFirst();

					// process page_break_before and
					// page_break_before_excluding_first
					if (pageBreakBeforeInts[currentGroupIndex] == 1
							|| (pageBreakBeforeInts[currentGroupIndex] == 2 && !isFirst)) {
						col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
					}

					// process page_break_after_excluding_last
					if (notifyNextPageBreak != -1) {
						notifyNextPageBreak = -1;

						col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
					}
				}
			} else {
				// only handle vertical measure case and last measure column
				if (event.measureIndex == -1 || event.measureIndex == lastTotalMeasureIndex[currentGroupIndex]) {
					// process page_break_after and
					// page_break_after_excluding_last
					if (pageBreakAfterInts[currentGroupIndex] == 1) {
						col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_AFTER, IStyle.ALWAYS_VALUE);
					} else if (pageBreakAfterInts[currentGroupIndex] == 2) {
						boolean isLast = groupCursors.get(currentGroupIndex).isLast();

						if (!isLast) {
							notifyNextPageBreak = currentGroupIndex;
						}
					}
				}
			}

			if (columnLevelPageBreakIntervals != null) {
				processLevelPageBreakIntervals(col);
			}
		} else if (event.type == ColumnEvent.COLUMN_EDGE_CHANGE) {
			// only handle vertical measure case and first measure column
			if (event.measureIndex == -1 || event.measureIndex == 0) {
				int startingGrouplevel = GroupUtil.getStartingGroupLevel(columnCursor, groupCursors);

				int startBound = startingGrouplevel == 0 ? 0 : (startingGrouplevel - 1);

				for (int i = startBound; i < pageBreakBeforeInts.length; i++) {
					if (!hasTotalBefore[i]) {
						// process page_break_before and
						// page_break_before_excluding_first
						if (pageBreakBeforeInts[i] == 1) {
							col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
							break;
						} else if (pageBreakBeforeInts[i] == 2) {
							boolean isFirst = groupCursors.get(i).isFirst();

							if (!isFirst) {
								col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
								break;
							}
						}
					}
				}

				// process page_break_after_excluding_last
				if (notifyNextPageBreak != -1 && (startingGrouplevel <= notifyNextPageBreak + 1)) {
					notifyNextPageBreak = -1;

					col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
				}
			}

			// only handle vertical measure case and last measure column
			if (event.measureIndex == -1 || event.measureIndex == lastMeasureIndex) {
				int endingGroupLevel = GroupUtil.getEndingGroupLevel(columnCursor, groupCursors);

				int endBound = endingGroupLevel == 0 ? 0 : (endingGroupLevel - 1);

				for (int i = endBound; i < pageBreakAfterInts.length; i++) {
					if (!hasTotalAfter[i]) {
						// process page_break_after and
						// page_break_after_excluding_last
						if (pageBreakAfterInts[i] == 1) {
							col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_AFTER, IStyle.ALWAYS_VALUE);
						} else if (pageBreakAfterInts[i] == 2) {
							boolean isLast = groupCursors.get(i).isLast();

							if (!isLast) {
								notifyNextPageBreak = i;
							}
						}
					}
				}
			}

			if (columnLevelPageBreakIntervals != null) {
				processLevelPageBreakIntervals(col);
			}
		} else if (event.type == ColumnEvent.GRAND_TOTAL_CHANGE) {
			// only handle vertical measure case and first measure column
			if (event.measureIndex == -1 || event.measureIndex == firstGrandTotalMeasureIndex) {
				// only process page_break_after_excluding_last
				if (notifyNextPageBreak != -1) {
					notifyNextPageBreak = -1;

					col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
				}
			}
		}
	}

	private void processLevelPageBreakIntervals(IColumn col) throws OLAPException {
		// TODO merge the same code base in BaseCrosstabExecutor

		if (lastColumnLevelState == null) {
			// this is the first access, store the initial state only
			lastColumnLevelState = GroupUtil.getLevelCursorState(columnCursor);

			// need use diffrernt state instance for checked state and last
			// state, must not use
			// "checkedColumnLevelState = lastColumnLevelState;"
			checkedColumnLevelState = GroupUtil.getLevelCursorState(columnCursor);
			return;
		}

		long[] currentColumnLevelState = GroupUtil.getLevelCursorState(columnCursor);

		for (int i = 0; i < columnLevelPageBreakIntervals.length; i++) {
			long currentPos = currentColumnLevelState[i];
			long lastPos = lastColumnLevelState[i];

			if (currentPos == lastPos) {
				continue;
			}

			if (columnLevelPageBreakIntervals[i] > 0) {
				// TODO check dummy group?

				long lastCheckedPos = checkedColumnLevelState[i];

				if (currentPos - lastCheckedPos >= columnLevelPageBreakIntervals[i]) {
					// if step length larger than interval setting, then
					// break
					col.getStyle().setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);

					// after break, need reset checked level state to
					// current state
					System.arraycopy(currentColumnLevelState, 0, checkedColumnLevelState, 0,
							currentColumnLevelState.length);
				}
			}

			// also revalidate subsequent checked level state since
			// parent level position change will reset all sub level
			// positions
			for (int j = i + 1; j < columnLevelPageBreakIntervals.length; j++) {
				checkedColumnLevelState[j] = 0;
			}
		}

		lastColumnLevelState = currentColumnLevelState;
	}
}
