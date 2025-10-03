/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * Data expression evaluator for cube query.
 *
 */

public class BIRTCubeResultSetEvaluator extends DataRowExpressionEvaluatorAdapter
		implements IGroupedDataRowExpressionEvaluator {

	protected static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	protected ICubeResultSet rs;

	protected ICubeQueryResults qr;

	protected ICubeCursor cubeCursor;

	protected long lSizeLimit = Long.MAX_VALUE;

	/**
	 * If there is Y optional expression, the cursor is related to Y optional
	 * expression. Otherwise, it is related to category expression.
	 */
	protected EdgeCursor mainEdgeCursor;

	/**
	 * The there is Y optional expression, the cursor is related to category
	 * expression. Otherwise it should be null.
	 */
	protected EdgeCursor subEdgeCursor;

	protected List<Integer> lstBreaks = new ArrayList<>();

	protected int iIndex = 0;

	protected boolean bWithoutSub = false;

	public BIRTCubeResultSetEvaluator(ICubeResultSet rs) {
		this.rs = rs;
		this.qr = null;
	}

	public BIRTCubeResultSetEvaluator(ICubeQueryResults qr) throws BirtException {
		this.rs = null;
		this.qr = qr;
		try {
			initCubeCursor();
		} catch (OLAPException e) {
			logger.log(e);
		}
	}

	@Override
	public int[] getGroupBreaks(int groupLevel) {
		if (lstBreaks.size() <= 1) {
			if (bWithoutSub && iIndex > 0) {
				// If no sub edge cursor, break every data
				int[] breaks = new int[iIndex - 1];
				for (int i = 0; i < breaks.length; i++) {
					breaks[i] = i + 1;
				}
				return breaks;
			}
			return new int[0];
		}
		// Remove the last index as requirement
		int[] breaks = new int[lstBreaks.size() - 1];
		for (int i = 0; i < breaks.length; i++) {
			breaks[i] = lstBreaks.get(i);
		}
		return breaks;
	}

	@Override
	public Object evaluate(String expression) {
		Object result = null;
		try {
			exprCodec.decode(expression);
			if (exprCodec.isConstant()) {
				return exprCodec.getExpression();
			}
			if (rs != null) {
				// If not binding name, evaluate it via report engine
				result = rs.evaluate(exprCodec.getType(), exprCodec.getExpression());
			} else {
				// DTE only supports evaluating data binding name, so chart
				// engine must check if it's binding name.
				final String bindingName;
				if (exprCodec.isCubeBinding(false)) {
					bindingName = exprCodec.getCubeBindingName(false);
					result = cubeCursor.getObject(bindingName);
				} else {
					// First try the expression as binding name to get result.
					try {
						result = cubeCursor.getObject(exprCodec.getExpression());
					} catch (OLAPException e) {
						// Try to escape the expression as binding name again, the escaped
						// expression is used for old approach to evaluate
						// binding. But now it seems the old approach isn't
						// used? I am not sure, so I just remain this code here.
						bindingName = ChartUtil.escapeSpecialCharacters(exprCodec.getExpression());
						result = cubeCursor.getObject(bindingName);
					}
				}
			}
		} catch (BirtException e) {
			result = e;
		} catch (RuntimeException e) {
			// Bugzilla#284528 During axis chart's evaluation, the cube cursor
			// may be after the last. However we don't need the actual value.
			// Shared scale can be used to draw an axis. Runtime exception
			// should be caught here to avoid stopping later rendering.
			logger.log(e);
			result = e;
		}
		return result;
	}

	@Override
	public Object evaluateGlobal(String expression) {
		return evaluate(expression);
	}

	@Override
	public boolean next() {
		iIndex++;
		try {
			if (subEdgeCursor != null) {
				// Break if sub cursor reaches end
				if (hasNext(subEdgeCursor)) {
					return true;
				}

				// Add break index for each start point
				lstBreaks.add(iIndex);

				subEdgeCursor.first();
				return hasNext(mainEdgeCursor);
			}
			return hasNext(mainEdgeCursor);
		} catch (OLAPException e) {
			logger.log(e);
		}
		return false;
	}

	/**
	 * Checks if current cursor can move to next.
	 *
	 * @param cursor
	 * @return
	 * @throws OLAPException
	 */
	protected boolean hasNext(EdgeCursor cursor) throws OLAPException {
		if (cursor.next()) {
			if (cursor.getPosition() < lSizeLimit) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void close() {
		if (rs != null) {
			rs.close();
		}
		if (qr != null) {
			try {
				qr.close();
			} catch (BirtException e) {
				logger.log(e);
			}
		}
	}

	@Override
	public boolean first() {
		try {
			initCubeCursor();

			if (mainEdgeCursor.first()) {
				if (subEdgeCursor != null) {
					subEdgeCursor.first();
				} else {
					bWithoutSub = true;
				}
				return true;
			}
		} catch (BirtException e) {
			logger.log(e);
		}
		return false;
	}

	protected void initCubeCursor() throws OLAPException, BirtException {
		if (cubeCursor == null) {
			cubeCursor = getCubeCursor();
			initCursors();
		}
	}

	/**
	 * @throws OLAPException
	 * @throws ChartException
	 */
	@SuppressWarnings("unchecked")
	protected void initCursors() throws OLAPException, ChartException {
		List<EdgeCursor> edges = cubeCursor.getOrdinateEdge();
		if (edges.size() == 0) {
			throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING,
					Messages.getString("exception.no.cube.edge")); //$NON-NLS-1$
		} else if (edges.size() == 1) {
			this.mainEdgeCursor = edges.get(0);
			this.subEdgeCursor = null;
		} else {
			this.mainEdgeCursor = edges.get(0);
			this.subEdgeCursor = edges.get(1);
		}
	}

	/**
	 * Returns cube cursor.
	 *
	 * @throws DataException
	 */
	protected ICubeCursor getCubeCursor() throws BirtException {
		if (rs != null) {
			return (ICubeCursor) rs.getCubeCursor();
		}
		return qr.getCubeCursor();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator#
	 * needCategoryGrouping()
	 */
	@Override
	public boolean needCategoryGrouping() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator#
	 * needOptionalGrouping()
	 */
	@Override
	public boolean needOptionalGrouping() {
		return false;
	}

	/**
	 * Sets size limit of row and column.
	 *
	 * @param dataSize
	 */
	public void setSizeLimit(long dataSize) {
		lSizeLimit = dataSize;
	}

	/*
	 * Returns if group is enabled in each group-level.
	 */
	@Override
	public boolean[] getGroupStatus() {
		return new boolean[] { true };
	}
}
