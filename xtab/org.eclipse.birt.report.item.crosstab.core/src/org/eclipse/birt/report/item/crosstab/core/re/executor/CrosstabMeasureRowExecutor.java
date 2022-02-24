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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabMeasureRowExecutor
 */
public class CrosstabMeasureRowExecutor extends BaseRowExecutor {

	private static Logger logger = Logger.getLogger(CrosstabMeasureRowExecutor.class.getName());

	public CrosstabMeasureRowExecutor(BaseCrosstabExecutor parent, int rowIndex) {
		super(parent, rowIndex);
	}

	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		processRowHeight(findMeasureRowCell(rowIndex));

		prepareChildren();

		return content;
	}

	protected void prepareChildren() {
		super.prepareChildren();

		initMeasureCache();

		walker.reload();
	}

	private AggregationCellHandle getRowSubTotalCell(int colDimensionIndex, int colLevelIndex, int measureIndex) {
		return getAggregationCell(-1, -1, colDimensionIndex, colLevelIndex, measureIndex);
	}

	protected void advance() {
		int mx;

		try {
			while (walker.hasNext()) {
				ColumnEvent ev = walker.next();

				switch (currentChangeType) {
				case ColumnEvent.ROW_EDGE_CHANGE:

					// we generate a dummy empty cell
					nextExecutor = new CrosstabCellExecutor(this, null, rowSpan, colSpan,
							currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
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

				if (isMeasureDetailNeedStart(ev)) {
					measureDetailStarted = true;
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
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
				} else if (ev.type == ColumnEvent.ROW_EDGE_CHANGE) {
					// this is a dummy row edge event
					rowSpan = 1;
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
			logger.log(Level.SEVERE, Messages.getString("CrosstabMeasureRowExecutor.error.generate.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			switch (currentChangeType) {
			case ColumnEvent.ROW_EDGE_CHANGE:

				// we generate a dummy empty cell
				nextExecutor = new CrosstabCellExecutor(this, null, rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

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
