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

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabHeaderExecutor
 */
public class CrosstabHeaderExecutor extends BaseCrosstabExecutor {

	private static final Logger logger = Logger.getLogger(CrosstabHeaderExecutor.class.getName());

	private boolean hasMeasureHeader;
	private boolean useCornerHeader;
	private boolean hasGrandTotal;
	private int currentGroupIndex;

	private int currentGrandTotalRow;
	private int totalGrandTotalRow;

	private long resetRowCursorPosition = -1;

	public CrosstabHeaderExecutor(BaseCrosstabExecutor parent) {
		super(parent);
	}

	@Override
	public IContent execute() {
		ITableBandContent content = context.getReportContent().createTableBandContent();
		content.setBandType(ITableBandContent.BAND_HEADER);

		initializeContent(content, null);

		prepareChildren();

		return content;
	}

	@Override
	public void close() {
		try {
			EdgeCursor rowCursor = getRowEdgeCursor();

			if (rowCursor != null && resetRowCursorPosition != -1) {
				// restore cursor state
				rowCursor.setPosition(resetRowCursorPosition);
			}
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabHeaderExecutor.error.reset.row.position"), //$NON-NLS-1$
					e);
		}

		resetRowCursorPosition = -1;

		super.close();
	}

	private void prepareChildren() {
		try {
			EdgeCursor rowCursor = getRowEdgeCursor();

			if (rowCursor != null) {
				// reset cursor position to initial state
				resetRowCursorPosition = rowCursor.getPosition();
				rowCursor.setPosition(-1);
			}

			hasGrandTotal = needRowGrandTotal(GRAND_TOTAL_LOCATION_BEFORE);
		} catch (OLAPException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabHeaderExecutor.error.reset.row.position"), //$NON-NLS-1$
					e);
		}

		currentGroupIndex = 0;
		hasMeasureHeader = GroupUtil.hasMeasureHeader(crosstabItem, COLUMN_AXIS_TYPE);
		useCornerHeader = columnGroups.size() == 0 && !hasMeasureHeader && crosstabItem.getHeader() != null;

		if (hasGrandTotal) {
			currentGrandTotalRow = 0;

			int count = crosstabItem.getMeasureCount();
			totalGrandTotalRow = (count > 1 && MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection()))
					? count
					: 1;
		}
	}

	@Override
	public IReportItemExecutor getNextChild() {
		IReportItemExecutor nextExecutor = null;

		if (currentGroupIndex < columnGroups.size()) {
			EdgeGroup eg = (EdgeGroup) columnGroups.get(currentGroupIndex++);

			DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, eg.dimensionIndex);
			LevelViewHandle lv = dv.getLevel(eg.levelIndex);

			nextExecutor = new CrosstabHeaderRowExecutor(this, lv);
		} else if (hasMeasureHeader) {
			nextExecutor = new CrosstabMeasureHeaderRowExecutor(this);
			hasMeasureHeader = false;
		} else if (useCornerHeader) {
			nextExecutor = new CrosstabCornerHeaderRowExecutor(this);
			useCornerHeader = false;
		} else if (hasGrandTotal) {
			return new CrosstabGrandTotalRowExecutor(this, currentGrandTotalRow++);
		}

		return nextExecutor;
	}

	@Override
	public boolean hasNextChild() {
		if ((currentGroupIndex < columnGroups.size()) || hasMeasureHeader || useCornerHeader) {
			return true;
		}

		if (hasGrandTotal && currentGrandTotalRow < totalGrandTotalRow) {
			if (GroupUtil.hasTotalContent(crosstabItem, ROW_AXIS_TYPE, -1, -1,
					MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection()) ? currentGrandTotalRow
							: -1)) {
				return true;
			} else {
				currentGrandTotalRow++;
				return hasNextChild();
			}
		}
		return false;
	}
}
