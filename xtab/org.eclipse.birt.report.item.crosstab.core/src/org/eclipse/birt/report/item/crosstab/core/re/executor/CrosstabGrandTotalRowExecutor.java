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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabGrandTotalRowExecutor
 */
public class CrosstabGrandTotalRowExecutor extends BaseRowExecutor {

	private static final Logger logger = Logger.getLogger(CrosstabGrandTotalRowExecutor.class.getName());

	private int totalRowSpan;
	private boolean isFirstTotalRow;

	public CrosstabGrandTotalRowExecutor(BaseCrosstabExecutor parent, int rowIndex) {
		super(parent, rowIndex);
	}

	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();
		content.setRepeatable(false);

		initializeContent(content, null);

		processRowHeight(findGrandTotalRowCell(rowIndex));

		processRowLevelPageBreak(content, true);

		prepareChildren();

		return content;
	}

	protected void prepareChildren() {
		super.prepareChildren();

		isFirstTotalRow = rowIndex == GroupUtil.getFirstTotalRowIndex(crosstabItem, -1, -1, isVerticalMeasure);
		totalRowSpan = GroupUtil.getTotalRowSpan(crosstabItem, -1, -1, isVerticalMeasure);

		walker.reload();
	}

	private AggregationCellHandle getRowGrandTotalCell(int dimensionIndex, int levelIndex, int measureIndex) {
		return getAggregationCell(-1, -1, dimensionIndex, levelIndex, measureIndex);
	}

	protected void advance() {
		int mx;

		try {
			while (walker.hasNext()) {
				ColumnEvent ev = walker.next();

				switch (currentChangeType) {
				case ColumnEvent.ROW_EDGE_CHANGE:

					if (ev.type != ColumnEvent.ROW_EDGE_CHANGE && isFirstTotalRow) {
						nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getGrandTotal(ROW_AXIS_TYPE),
								rowSpan, colSpan, currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						hasLast = false;
					}
					break;
				case ColumnEvent.MEASURE_HEADER_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(rowIndex).getHeader(null),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				case ColumnEvent.MEASURE_CHANGE:
				case ColumnEvent.COLUMN_EDGE_CHANGE:
				case ColumnEvent.COLUMN_TOTAL_CHANGE:
				case ColumnEvent.GRAND_TOTAL_CHANGE:

					mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

					if (measureDetailStarted && isMeetMeasureDetailEnd(ev,
							getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx))) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureDetailStarted = false;
						hasLast = false;
					} else if (measureSubTotalStarted) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureSubTotalStarted = false;
						hasLast = false;
					} else if (measureGrandTotalStarted) {
						nextExecutor = new CrosstabCellExecutor(this,
								getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
								currentColIndex - colSpan + 1);

						((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

						measureGrandTotalStarted = false;
						hasLast = false;
					}

					break;
				}

				if (isMeasureDetailNeedStart(ev)) {
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
				} else if (ev.type == ColumnEvent.ROW_EDGE_CHANGE && isFirstTotalRow) {
					rowSpan = totalRowSpan;

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
			logger.log(Level.SEVERE, Messages.getString("CrosstabGrandTotalRowExecutor.error.generate.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			switch (currentChangeType) {
			case ColumnEvent.ROW_EDGE_CHANGE:

				if (isFirstTotalRow) {
					nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getGrandTotal(ROW_AXIS_TYPE), rowSpan,
							colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);
				}
				break;
			case ColumnEvent.MEASURE_HEADER_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(rowIndex).getHeader(null),
						rowSpan, colSpan, currentColIndex - colSpan + 1);

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
						getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureDetailStarted = false;
			} else if (measureSubTotalStarted) {
				mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

				nextExecutor = new CrosstabCellExecutor(this,
						getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureSubTotalStarted = false;
			} else if (measureGrandTotalStarted) {
				mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

				nextExecutor = new CrosstabCellExecutor(this,
						getRowGrandTotalCell(lastDimensionIndex, lastLevelIndex, mx), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				measureGrandTotalStarted = false;
			}
		}
	}

}
