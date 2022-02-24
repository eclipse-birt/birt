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
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabMeasureHeaderRowExecutor
 */
public class CrosstabMeasureHeaderRowExecutor extends BaseCrosstabExecutor {

	private static Logger logger = Logger.getLogger(CrosstabMeasureHeaderRowExecutor.class.getName());

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;
	private int lastDimensionIndex;
	private int lastLevelIndex;

	private int totalMeasureCount;

	private long currentEdgePosition;

	private boolean hasColumnGroups;
	private boolean blankStarted;
	private boolean hasLast;

	private boolean isFirst;
	private IReportItemExecutor nextExecutor;

	public CrosstabMeasureHeaderRowExecutor(BaseCrosstabExecutor parent) {
		super(parent);
	}

	public void close() {
		super.close();

		nextExecutor = null;
	}

	public IContent execute() {
		IRowContent content = context.getReportContent().createRowContent();

		initializeContent(content, null);

		processRowHeight(findMeasureHeaderCell());

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		isFirst = true;

		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;
		currentColIndex = -1;

		currentEdgePosition = -1;

		blankStarted = false;
		hasColumnGroups = columnGroups != null && columnGroups.size() > 0;

		totalMeasureCount = crosstabItem.getMeasureCount();

		rowSpan = 1;
		colSpan = 0;
		lastMeasureIndex = -1;
		lastDimensionIndex = -1;
		lastLevelIndex = -1;

		hasLast = false;

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

	private boolean needCornerHeaderCell() {
		if (!hasColumnGroups) {
			return true;
		}

		int cgCount = columnGroups.size();
		int rgCount = rowGroups == null ? 0 : rowGroups.size();

		int offset = cgCount * rgCount;

		// assuming all header cell span == 1 for now
		if (offset < crosstabItem.getHeaderCount()) {
			return true;
		}

		return false;
	}

	private CrosstabCellHandle getCornerHeaderCell(int colIndex) {
		int offset = 0;

		if (hasColumnGroups) {
			int cgCount = columnGroups.size();
			int rgCount = rowGroups == null ? 0 : rowGroups.size();

			// even when no row groups, we consider there's always one dummy row
			// group.
			if (rgCount == 0) {
				rgCount++;
			}

			offset = cgCount * rgCount;
		}

		// assuming all header cell span == 1 for now
		if ((colIndex + offset) < crosstabItem.getHeaderCount()) {
			return crosstabItem.getHeader(colIndex + offset);
		}

		return null;
	}

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
				case ColumnEvent.MEASURE_CHANGE:
				case ColumnEvent.COLUMN_EDGE_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(lastMeasureIndex).getHeader(),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				case ColumnEvent.COLUMN_TOTAL_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this, getSubTotalMeasureHeaderCell(COLUMN_AXIS_TYPE,
							lastDimensionIndex, lastLevelIndex, lastMeasureIndex), rowSpan, colSpan,
							currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				case ColumnEvent.GRAND_TOTAL_CHANGE:

					nextExecutor = new CrosstabCellExecutor(this,
							crosstabItem.getMeasure(lastMeasureIndex).getHeader(null), rowSpan, colSpan,
							currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

					hasLast = false;
					break;
				}

				if (ev.type == ColumnEvent.MEASURE_CHANGE || ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
						|| ev.type == ColumnEvent.COLUMN_EDGE_CHANGE || ev.type == ColumnEvent.GRAND_TOTAL_CHANGE) {
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				} else if (!blankStarted
						&& (ev.type == ColumnEvent.ROW_EDGE_CHANGE || ev.type == ColumnEvent.MEASURE_HEADER_CHANGE)
						&& needCornerHeaderCell()) {
					blankStarted = true;
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
			logger.log(Level.SEVERE,
					Messages.getString("CrosstabMeasureHeaderRowExecutor.error.generate.child.executor"), //$NON-NLS-1$
					e);
		}

		if (hasLast) {
			hasLast = false;

			// handle last column
			switch (currentChangeType) {
			case ColumnEvent.ROW_EDGE_CHANGE:
			case ColumnEvent.MEASURE_HEADER_CHANGE:

				if (blankStarted) {
					nextExecutor = new CrosstabCellExecutor(this, getCornerHeaderCell(currentColIndex - colSpan + 1),
							rowSpan, colSpan, currentColIndex - colSpan + 1);

					((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);
				}
				break;
			case ColumnEvent.MEASURE_CHANGE:
			case ColumnEvent.COLUMN_EDGE_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(lastMeasureIndex).getHeader(),
						rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				break;
			case ColumnEvent.COLUMN_TOTAL_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this, getSubTotalMeasureHeaderCell(COLUMN_AXIS_TYPE,
						lastDimensionIndex, lastLevelIndex, lastMeasureIndex), rowSpan, colSpan,
						currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				break;
			case ColumnEvent.GRAND_TOTAL_CHANGE:

				nextExecutor = new CrosstabCellExecutor(this, crosstabItem.getMeasure(lastMeasureIndex).getHeader(null),
						rowSpan, colSpan, currentColIndex - colSpan + 1);

				((CrosstabCellExecutor) nextExecutor).setPosition(currentEdgePosition);

				break;
			}
		}
	}

	public IReportItemExecutor getNextChild() {
		IReportItemExecutor childExecutor = nextExecutor;

		nextExecutor = null;

		advance();

		return childExecutor;
	}

	public boolean hasNextChild() {
		if (isFirst) {
			isFirst = false;

			advance();
		}

		return nextExecutor != null;
	}

}
