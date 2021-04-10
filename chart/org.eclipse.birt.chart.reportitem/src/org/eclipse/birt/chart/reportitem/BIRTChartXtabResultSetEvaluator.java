/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Data expression evaluator for chart in xtab.
 */

public class BIRTChartXtabResultSetEvaluator extends BIRTCubeResultSetEvaluator {

	private final ExtendedItemHandle handle;
	private boolean bSubCursor = false;
	private boolean bTransposed = false;

	public BIRTChartXtabResultSetEvaluator(ICubeResultSet rs, ExtendedItemHandle handle) {
		super(rs);
		this.handle = handle;
	}

	protected void initCubeCursor() throws OLAPException, BirtException {
		ICubeCursor parent = getCubeCursor();
		cubeCursor = parent;
		try {
			AggregationCellHandle cellHandle = ChartCubeUtil.getXtabContainerCell(handle);
			LevelHandle levelAggColumn = cellHandle.getAggregationOnColumn();
			LevelHandle levelAggRow = cellHandle.getAggregationOnRow();
			Chart cm = ChartItemUtil.getChartFromHandle(handle);
			bTransposed = ((ChartWithAxes) cm).isTransposed();
			if (!bTransposed) {
				// Horizontal span
				if (levelAggColumn != null && levelAggRow != null) {
					// row cursor is the main
					List edges = cubeCursor.getOrdinateEdge();
					this.mainEdgeCursor = (EdgeCursor) edges.get(1);
					this.subEdgeCursor = (EdgeCursor) edges.get(0);

					bSubCursor = true;
				} else {
					cubeCursor = parent;
				}

			} else if (cellHandle.getSpanOverOnRow() != null) {
				// Vertical span
				if (levelAggColumn != null && levelAggRow != null) {
					// column cursor is the main
					List edges = cubeCursor.getOrdinateEdge();
					this.mainEdgeCursor = (EdgeCursor) edges.get(0);
					this.subEdgeCursor = (EdgeCursor) edges.get(1);

					bSubCursor = true;
				} else {
					cubeCursor = parent;
				}
			} else {
				cubeCursor = parent;
			}
		} catch (BirtException e) {
			logger.log(e);
			cubeCursor = parent;
		}

		if (!bSubCursor) {
			List edges = cubeCursor.getOrdinateEdge();
			if (edges.size() == 1) {
				this.mainEdgeCursor = (EdgeCursor) edges.get(0);
			} else if (edges.size() > 1) {
				this.mainEdgeCursor = (EdgeCursor) edges.get(bTransposed ? 1 : 0);
			}
			this.subEdgeCursor = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#getCubeCursor()
	 */
	protected ICubeCursor getCubeCursor() throws BirtException {
		return (ICubeCursor) rs.getCubeCursor();
	}

	public boolean first() {
		try {
			initCubeCursor();

			if (!bSubCursor) {
				return mainEdgeCursor.first();
			}

			mainEdgeCursor.first();

			return subEdgeCursor.first();
		} catch (OLAPException e) {
			logger.log(e);
		} catch (BirtException e) {
			logger.log(e);
		}
		return false;
	}

	public boolean next() {
		try {
			if (!bSubCursor) {
				return hasNext(mainEdgeCursor);
			}
			return hasNext(subEdgeCursor);
		} catch (OLAPException e) {
			logger.log(e);
		}
		return false;
	}
}
