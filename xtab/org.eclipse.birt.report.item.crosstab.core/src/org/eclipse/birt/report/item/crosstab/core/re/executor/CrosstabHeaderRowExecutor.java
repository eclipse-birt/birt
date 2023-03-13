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
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabHeaderRowExecutor
 */
public class CrosstabHeaderRowExecutor extends BaseCrosstabExecutor {

	private static Logger logger = Logger.getLogger(CrosstabHeaderRowExecutor.class.getName());

	private LevelViewHandle levelView;

	private long currentEdgePosition;

	private int currentDimensionIndex, currentLevelIndex;
	private int subTotalDimensionIndex, subTotalLevelIndex;
	private int currentChangeType;
	private int currentColIndex;
	private int rowSpan, colSpan;
	private boolean edgeStarted, subTotalStarted, grandTotalStarted, blankStarted;
	private boolean hasLast;

	private boolean isLayoutDownThenOver;

	private int currentGroupIndex;
	private int nextGroupIndex;
	private boolean isFirst;
	private IReportItemExecutor nextExecutor;

	public CrosstabHeaderRowExecutor(BaseCrosstabExecutor parent, LevelViewHandle levelView) {
		super(parent);

		this.levelView = levelView;
	}

	@Override
	public void close() {
		super.close();

		nextExecutor = null;
	}

	@Override
	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		isFirst = true;

		subTotalStarted = false;
		grandTotalStarted = false;
		edgeStarted = false;
		blankStarted = false;
		hasLast = false;

		if (crosstabItem != null) {
			isLayoutDownThenOver = ICrosstabConstants.PAGE_LAYOUT_DOWN_THEN_OVER.equals(crosstabItem.getPageLayout());
		}

		currentLevelIndex = levelView.getIndex();
		currentDimensionIndex = ((DimensionViewHandle) levelView.getContainer()).getIndex();

		processRowHeight(findHeaderRowCell(currentDimensionIndex, currentLevelIndex));

		subTotalDimensionIndex = -1;
		subTotalLevelIndex = -1;

		if (isLayoutDownThenOver) {
			subTotalDimensionIndex = currentDimensionIndex;
			subTotalLevelIndex = currentLevelIndex;
		} else {
			EdgeGroup gp = GroupUtil.getPreviousGroup(columnGroups, currentDimensionIndex, currentLevelIndex);

			if (gp != null) {
				DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, gp.dimensionIndex);
				LevelViewHandle lv = dv.getLevel(gp.levelIndex);

				if (lv.getAggregationHeader() != null) {
					subTotalDimensionIndex = gp.dimensionIndex;
					subTotalLevelIndex = gp.levelIndex;
				}
			}
		}

		currentGroupIndex = GroupUtil.getGroupIndex(columnGroups, currentDimensionIndex, currentLevelIndex);

		nextGroupIndex = GroupUtil.getNextGroupIndex(columnGroups, currentDimensionIndex, currentLevelIndex);

		rowSpan = 1;
		colSpan = 0;

		currentColIndex = -1;
		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;

		currentEdgePosition = -1;

		walker.reload();
	}

	private boolean isForceEmpty() {
		try {
			EdgeCursor columnEdgeCursor = getColumnEdgeCursor();

			if (columnEdgeCursor != null) {
				columnEdgeCursor.setPosition(currentEdgePosition);

				DimensionCursor dc = (DimensionCursor) columnEdgeCursor.getDimensionCursor().get(currentGroupIndex);

				return GroupUtil.isDummyGroup(dc);
			}
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabHeaderRowExecutor.error.check.force.empty"), //$NON-NLS-1$
					e);
		}

		return false;
	}

	private boolean isMeetEdgeEnd(ColumnEvent ev) {
		if (ev.type == ColumnEvent.GRAND_TOTAL_CHANGE || isStartUnderSubTotal(ev)) {
			return true;
		}

		if (nextGroupIndex != -1) {
			try {
				EdgeCursor columnEdgeCursor = getColumnEdgeCursor();

				if (columnEdgeCursor != null) {
					columnEdgeCursor.setPosition(ev.dataPosition);

					DimensionCursor dc = (DimensionCursor) columnEdgeCursor.getDimensionCursor().get(currentGroupIndex);

					if (!GroupUtil.isDummyGroup(dc)) {
						return currentEdgePosition < dc.getEdgeStart();
					}
				}
			} catch (OLAPException e) {
				logger.log(Level.SEVERE, Messages.getString("CrosstabHeaderRowExecutor.error.check.edge.end"), //$NON-NLS-1$
						e);
			}
		}

		return ev.measureIndex <= 0;
	}

	private boolean isEdgeNeedStart(ColumnEvent ev) {
		return !edgeStarted && !subTotalStarted
				&& (ev.type == ColumnEvent.COLUMN_EDGE_CHANGE || isStartOverSubTotal(ev));
	}

	private boolean isStartOverSubTotal(ColumnEvent ev) {
		if (isLayoutDownThenOver) {
			return !subTotalStarted && ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
					&& (ev.dimensionIndex > currentDimensionIndex
							|| (ev.dimensionIndex == currentDimensionIndex && ev.levelIndex > currentLevelIndex));

		} else {
			return !subTotalStarted && ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
					&& (ev.dimensionIndex > currentDimensionIndex
							|| (ev.dimensionIndex == currentDimensionIndex && ev.levelIndex >= currentLevelIndex));
		}
	}

	private boolean isStartUnderSubTotal(ColumnEvent ev) {
		if (isLayoutDownThenOver) {
			return !subTotalStarted && ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
					&& (ev.dimensionIndex < currentDimensionIndex
							|| (ev.dimensionIndex == currentDimensionIndex && ev.levelIndex <= currentLevelIndex));
		} else {
			return !subTotalStarted && ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
					&& (ev.dimensionIndex < currentDimensionIndex
							|| (ev.dimensionIndex == currentDimensionIndex && ev.levelIndex < currentLevelIndex));
		}
	}

	private boolean isSubTotalNeedStart(ColumnEvent ev) {
		return !subTotalStarted && ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
				&& ev.dimensionIndex == subTotalDimensionIndex && ev.levelIndex == subTotalLevelIndex;
	}

	private boolean isGrandTotalNeedStart(ColumnEvent ev) {
		return !grandTotalStarted && ev.type == ColumnEvent.GRAND_TOTAL_CHANGE
				&& crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE) != null
				&& GroupUtil.isFirstGroup(columnGroups, currentDimensionIndex, currentLevelIndex);
	}

	private boolean isBlankNeedStart(ColumnEvent ev) {
		if (!blankStarted && (ev.type == ColumnEvent.ROW_EDGE_CHANGE || ev.type == ColumnEvent.MEASURE_HEADER_CHANGE)) {
			int headerCount = crosstabItem.getHeaderCount();

			// TODO to simplify, only check headerCount>1 for now (e.g. assume
			// all cell span == 1), later if header cell span supported, should
			// calculate based on real span.
			if (headerCount > 1 || GroupUtil.isFirstGroup(columnGroups, currentDimensionIndex, currentLevelIndex)) {
				return true;
			}
		}

		return false;
	}

	private int computeCornerHeaderCellRowSpan() {
		if (crosstabItem.getHeaderCount() > 1) {
			// assuming all cell span == 1 for now
			return 1;
		}

		int span = GroupUtil.computeGroupSpan(columnGroups, currentDimensionIndex, currentLevelIndex) + 1;

		if (GroupUtil.hasMeasureHeader(crosstabItem, COLUMN_AXIS_TYPE)) {
			span++;
		}

		return span;
	}

	private CrosstabCellHandle getCornerHeaderCell(int colIndex) {
		int headerCount = crosstabItem.getHeaderCount();

		if (headerCount <= 1) {
			return crosstabItem.getHeader();
		}

		int rgCount = rowGroups == null ? 0 : rowGroups.size();

		if (GroupUtil.hasMeasureHeader(crosstabItem, ROW_AXIS_TYPE)) {
			rgCount++;
		} else if (rgCount == 0) {
			// even when no row groups, we consider there's always one dummy row
			// group.
			rgCount++;
		}

		int offset = currentGroupIndex * rgCount;

		// assuming all header cell span == 1 for now
		if ((colIndex + offset) < headerCount) {
			return crosstabItem.getHeader(colIndex + offset);
		}

		return null;
	}

	/**
	 * Advance until find next executor or till the end.
	 */
	private void advance() {
		try {
			while (walker.hasNext()) {
				ColumnEvent ev = walker.next();

				switch (currentChangeType) {
				case ColumnEvent.ROW_EDGE_CHANGE:
				case ColumnEvent.MEASURE_HEADER_CHANGE:

					if (blankStarted) {
						int headerCount = crosstabItem.getHeaderCount();

						// TODO to simplify, only check headerCount>1 for
						// now (e.g. assume all cell span == 1), later if
						// header cell span supported, should calculate
						// based on real span.
						if (headerCount > 1 || (ev.type != ColumnEvent.ROW_EDGE_CHANGE
								&& ev.type != ColumnEvent.MEASURE_HEADER_CHANGE)) {
							nextExecutor = new CrosstabCellExecutor(this,
									getCornerHeaderCell(currentColIndex - colSpan + 1), rowSpan, colSpan,
									currentColIndex - colSpan + 1);

							((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

							blankStarted = false;
							hasLast = false;
						}
					}
					break;
				case ColumnEvent.COLUMN_EDGE_CHANGE:
				case ColumnEvent.COLUMN_TOTAL_CHANGE:

					if (edgeStarted && isMeetEdgeEnd(ev)) {
						nextExecutor = new CrosstabCellExecutor(this, levelView.getCell(), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						((CrosstabCellExecutor) nextExecutor).setForceEmpty(isForceEmpty());

						edgeStarted = false;
						hasLast = false;
					} else if (subTotalStarted && (ev.type != currentChangeType
							|| ev.dimensionIndex != subTotalDimensionIndex || ev.levelIndex != subTotalLevelIndex)) {
						nextExecutor = new CrosstabCellExecutor(this,
								crosstabItem.getDimension(COLUMN_AXIS_TYPE, subTotalDimensionIndex)
										.getLevel(subTotalLevelIndex).getAggregationHeader(),
								rowSpan, colSpan, currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						subTotalStarted = false;
						hasLast = false;
					}
					break;
				case ColumnEvent.GRAND_TOTAL_CHANGE:

					if (grandTotalStarted && ev.type != currentChangeType) {
						nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE),
								rowSpan, colSpan, currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						grandTotalStarted = false;
						hasLast = false;
					}
					break;
				}

				if (isSubTotalNeedStart(ev)) {
					subTotalStarted = true;

					rowSpan = GroupUtil.computeGroupSpan(columnGroups, subTotalDimensionIndex, subTotalLevelIndex);

					if (isLayoutDownThenOver) {
						rowSpan++;
					}

					colSpan = 0;
					hasLast = true;
				} else if (isGrandTotalNeedStart(ev)) {
					grandTotalStarted = true;

					rowSpan = GroupUtil.computeGroupSpan(columnGroups, currentDimensionIndex, currentLevelIndex) + 1;

					colSpan = 0;
					hasLast = true;
				} else if (isEdgeNeedStart(ev)) {
					edgeStarted = true;
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				} else if (isBlankNeedStart(ev)) {
					blankStarted = true;

					rowSpan = computeCornerHeaderCellRowSpan();
					colSpan = 0;

					hasLast = true;
				}

				currentEdgePosition = ev.dataPosition;

				currentChangeType = ev.type;
				colSpan++;
				currentColIndex++;

				if (nextExecutor != null) {
					return;
				}
			}

		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabHeaderRowExecutor.error.retrieve.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			if (blankStarted) {
				nextExecutor = new CrosstabCellExecutor(this, getCornerHeaderCell(currentColIndex - colSpan + 1),
						rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);
			} else if (edgeStarted) {
				nextExecutor = new CrosstabCellExecutor(this, levelView.getCell(), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				((CrosstabCellExecutor) nextExecutor).setForceEmpty(isForceEmpty());

				edgeStarted = false;
			} else if (subTotalStarted) {
				nextExecutor = new CrosstabCellExecutor(
						this, crosstabItem.getDimension(COLUMN_AXIS_TYPE, subTotalDimensionIndex)
								.getLevel(subTotalLevelIndex).getAggregationHeader(),
						rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				subTotalStarted = false;
			} else if (grandTotalStarted) {
				nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE), rowSpan,
						colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				grandTotalStarted = false;
			}
		}

	}

	@Override
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor childExecutor = nextExecutor;

		nextExecutor = null;

		advance();

		return childExecutor;
	}

	@Override
	public boolean hasNextChild() {
		if (isFirst) {
			isFirst = false;

			advance();
		}

		return nextExecutor != null;
	}

}
