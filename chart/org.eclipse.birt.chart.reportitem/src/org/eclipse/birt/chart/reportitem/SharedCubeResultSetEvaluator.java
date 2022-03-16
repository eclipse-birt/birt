/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;
import javax.olap.cursor.RowDataNavigation;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * The class implements evaluating for sharing xtab or chart view for xtab.
 */

public class SharedCubeResultSetEvaluator extends BIRTCubeResultSetEvaluator {

	protected int fCategoryInnerLevelIndex;
	protected int fYOptionalInnerLevelIndex;

	protected CursorPositionNode fMainPositionNodes;
	protected CursorPositionNode fSubPositionNodes;
	protected boolean fIsColEdgeAsCategoryCursor;

	/**
	 * Constructor.
	 *
	 * @param rs
	 * @param cm
	 */
	public SharedCubeResultSetEvaluator(ICubeResultSet rs, Chart cm) {
		super(rs);
		init(rs.getCubeQuery(), cm);
	}

	protected void init(IBaseCubeQueryDefinition queryDefinition, Chart cm) {
		parseLevelIndex(queryDefinition, cm);
		try {
			initCubeCursor();
		} catch (BirtException e) {
			logger.log(e);
		}
	}

	/**
	 * Constructor.
	 *
	 * @param qr
	 * @param queryDefinition
	 * @param cm
	 */
	public SharedCubeResultSetEvaluator(ICubeQueryResults qr, IBaseCubeQueryDefinition queryDefinition, Chart cm)
			throws BirtException {
		super((ICubeResultSet) null);
		this.qr = qr;
		init(queryDefinition, cm);
	}

	/**
	 * Parse the dimension levels on row edge and column edge to find out the level
	 * index used by category series and Y optional.
	 *
	 * @param queryDefintion
	 * @param cm
	 */
	protected void parseLevelIndex(IBaseCubeQueryDefinition queryDefintion, Chart cm) {
		fCategoryInnerLevelIndex = -1;
		fYOptionalInnerLevelIndex = -1;
		if (queryDefintion instanceof ICubeQueryDefinition) {
			String[] categoryExprs = ChartUtil.getCategoryExpressions(cm);

			ICubeQueryDefinition cqd = (ICubeQueryDefinition) queryDefintion;
			IEdgeDefinition rowED = cqd.getEdge(ICubeQueryDefinition.ROW_EDGE);
			IEdgeDefinition colED = cqd.getEdge(ICubeQueryDefinition.COLUMN_EDGE);

			// Swap the row edge and col edge if row edge is null.
			if (rowED == null && colED != null) {
				rowED = colED;
				colED = null;
			}

			// Gets cube binding expressions map.
			Map<String, String> cubeBindingMap = new HashMap<>();
			List bindingList = cqd.getBindings();
			for (int i = 0; i < bindingList.size(); i++) {
				Binding b = (Binding) bindingList.get(i);
				if (b.getExpression() instanceof IScriptExpression) {
					cubeBindingMap.put(b.getBindingName(), ((IScriptExpression) b.getExpression()).getText());
				}
			}

			if (categoryExprs != null && categoryExprs.length > 0) {
				if (rowED != null) {
					fCategoryInnerLevelIndex = findInnerLevelIndex(categoryExprs[0], rowED, cubeBindingMap);
				}

				if (fCategoryInnerLevelIndex < 0 && colED != null) {
					// Row level isn't find on row edge, find it on column
					// edge.
					fCategoryInnerLevelIndex = findInnerLevelIndex(categoryExprs[0], colED, cubeBindingMap);
					fIsColEdgeAsCategoryCursor = true;
				}
			}

			// If category level index is less than zero, it means no valid
			// edges for this chart.
			if (fCategoryInnerLevelIndex < 0) {
				return;
			}

			String[] yOptionalExprs = ChartUtil.getYOptoinalExpressions(cm);
			if (yOptionalExprs != null && yOptionalExprs.length > 0) {
				if (fIsColEdgeAsCategoryCursor && rowED != null) {
					fYOptionalInnerLevelIndex = findInnerLevelIndex(yOptionalExprs[0], rowED, cubeBindingMap);
				} else if (colED != null) {
					fYOptionalInnerLevelIndex = findInnerLevelIndex(yOptionalExprs[0], colED, cubeBindingMap);
				}
			}
		}
	}

	/**
	 * Find the inner level index from specified expression.
	 *
	 * @param expr
	 * @param levelNames
	 * @param cubeBindingMap
	 * @return
	 */
	protected int findInnerLevelIndex(String expr, IEdgeDefinition edge, Map<String, String> cubeBindingMap) {
		int index = -1;
		if (ChartUtil.isEmpty(expr)) {
			return index;
		}
		Map<String, List<String>> dimLevelMaps = getDimLevelsNames(edge);

		ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();
		Collection<String> bindingNames = exprCodec.getBindingNames(expr);

		for (String bindName : bindingNames) {
			String cubeBindingExpr = cubeBindingMap.get(bindName);
			if (cubeBindingExpr == null) {
				continue;
			}
			String[] lNames = exprCodec.getLevelNames(cubeBindingExpr);

			for (java.util.Iterator<Map.Entry<String, List<String>>> iter = dimLevelMaps.entrySet().iterator(); iter
					.hasNext();) {
				Map.Entry<String, List<String>> dimLevels = iter.next();
				// If dimension isn't equal, ignore.
				if (!lNames[0].equals(dimLevels.getKey())) {
					continue;
				}

				List<String> levelNames = dimLevels.getValue();

				for (int i = 1; i < lNames.length; i++) {
					int levelIndex = levelNames.indexOf(lNames[i]);
					if (levelIndex > index) {
						index = levelIndex;
					}
				}
			}
		}
		return index;
	}

	protected Map<String, List<String>> getDimLevelsNames(IEdgeDefinition ed) {
		Map<String, List<String>> map = new LinkedHashMap<>();
		List<IDimensionDefinition> dimensions = ed.getDimensions();
		for (IDimensionDefinition d : dimensions) {
			List<String> levelNames = new ArrayList<>();
			map.put(d.getName(), levelNames);
			List<IHierarchyDefinition> hieDefs = d.getHierarchy();
			for (IHierarchyDefinition hd : hieDefs) {
				List<ILevelDefinition> levels = hd.getLevels();
				for (ILevelDefinition ld : levels) {
					levelNames.add(ld.getName());
				}
			}

		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#initCubeCursor()
	 */
	@Override
	protected void initCubeCursor() throws OLAPException, BirtException {
		// Find row and column edge cursor.
		if (cubeCursor == null) {
			cubeCursor = getCubeCursor();

			List<EdgeCursor> edges = cubeCursor.getOrdinateEdge();
			if (edges.size() == 0) {
				throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING,
						Messages.getString("exception.no.cube.edge")); //$NON-NLS-1$
			} else if (edges.size() == 1) {
				this.mainEdgeCursor = (EdgeCursor) edges.get(0);
				this.subEdgeCursor = null;
			} else {
				this.mainEdgeCursor = (EdgeCursor) edges.get(0); // It returns column edge cursor.
				this.subEdgeCursor = (EdgeCursor) edges.get(1); // It returns row edge cursor.
			}
		}

		// It means the shared xtab has defined row and column edges, but chart
		// just select row or column edge. The edge cursor should be adjusted
		// for chart to evaluate expressions.
		if (fCategoryInnerLevelIndex >= 0 && fYOptionalInnerLevelIndex < 0 && subEdgeCursor != null) {
			if (!fIsColEdgeAsCategoryCursor) {
				// Row edge is used by chart, set subEdgeCursor(row edge)
				// to mainEdgeCursor.
				mainEdgeCursor = subEdgeCursor;
			}

			subEdgeCursor = null;
		} else if (fCategoryInnerLevelIndex >= 0 && fYOptionalInnerLevelIndex >= 0 && fIsColEdgeAsCategoryCursor) {
			// It should use row edge as main edge cursor.
			EdgeCursor tmp = mainEdgeCursor;
			mainEdgeCursor = subEdgeCursor;
			subEdgeCursor = tmp;
		}

		// Map dimension cursor, find out the right row dimension cursor and
		// column dimension cursor which is selected by chart.
		if (subEdgeCursor == null) {
			List dimCursors = mainEdgeCursor.getDimensionCursor();
			if (fCategoryInnerLevelIndex >= 0) {
				fMainPositionNodes = initCursorPositionsNodes(dimCursors, fCategoryInnerLevelIndex);
			} else if (fYOptionalInnerLevelIndex >= 0) {
				fMainPositionNodes = initCursorPositionsNodes(dimCursors, fYOptionalInnerLevelIndex);
			}
		} else {
			if (fCategoryInnerLevelIndex >= 0) {
				List dimCursors = subEdgeCursor.getDimensionCursor();
				fSubPositionNodes = initCursorPositionsNodes(dimCursors, fCategoryInnerLevelIndex);
			}
			if (fYOptionalInnerLevelIndex >= 0) {
				List dimCursors = mainEdgeCursor.getDimensionCursor();
				fMainPositionNodes = initCursorPositionsNodes(dimCursors, fYOptionalInnerLevelIndex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#getCubeCursor()
	 */
	@Override
	protected ICubeCursor getCubeCursor() throws BirtException {
		if (rs != null) {
			return (ICubeCursor) rs.getCubeCursor();
		} else {
			return qr.getCubeCursor();
		}
	}

	protected CursorPositionNode initCursorPositionsNodes(List dimCursorList, int innerLevelIndex) {
		CursorPositionNode pn = null;
		CursorPositionNode rootPN = null;
		for (int i = innerLevelIndex; i >= 0; i--) {
			if (pn == null) {
				pn = new CursorPositionNode((RowDataNavigation) dimCursorList.get(i));
				rootPN = pn;
			} else {
				pn.setParentNode(new CursorPositionNode((RowDataNavigation) dimCursorList.get(i)));
				pn = pn.getParentNode();
			}
		}
		return rootPN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	@Override
	public boolean next() {
		// In here, we use position to check if current edge cursor is moved on
		// right position. If the previous position equals current position, it
		// means the edge cursor is still in one data set on related dimension
		// cursor. If the position is changed, it means the edge cursor is moved
		// on the next data set of related dimension cursor.
		iIndex++;
		try {
			if (subEdgeCursor != null) {
				// Break if sub cursor reaches end
				boolean hasNext = false;
				while (hasNext = hasNext(subEdgeCursor)) {
					if (fSubPositionNodes.positionIsChanged()) {
						break;
					}
				}

				fSubPositionNodes.updatePosition();

				if (hasNext) {
					return true;
				}

				// Add break index for each start point
				lstBreaks.add(iIndex);
				subEdgeCursor.first();
				fSubPositionNodes.updatePosition();

				hasNext = false;
				while (hasNext = hasNext(mainEdgeCursor)) {
					if (fMainPositionNodes.positionIsChanged()) {
						break;
					}
				}
				fMainPositionNodes.updatePosition();

				if (hasNext) {
					return true;
				}
			} else {
				boolean hasNext = false;
				while (hasNext = hasNext(mainEdgeCursor)) {
					// if ( fColPosition != fMainCursor.getPosition( ) )
					// {
					// break;
					// }
					if (fMainPositionNodes.positionIsChanged()) {
						break;
					}
				}

				// fColPosition = fMainCursor.getPosition( );
				fMainPositionNodes.updatePosition();

				if (hasNext) {
					return true;
				}
			}
		} catch (OLAPException e) {
			logger.log(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	@Override
	public boolean first() {
		try {
			if (mainEdgeCursor.first()) {
				fMainPositionNodes.updatePosition();
				if (subEdgeCursor != null) {
					subEdgeCursor.first();
					fSubPositionNodes.updatePosition();
				} else {
					bWithoutSub = true;
				}
				return true;
			}
		} catch (OLAPException e) {
			logger.log(e);
		}
		return false;
	}

	/**
	 * The class records the position of dimension cursor and parent dimension
	 * cursor.
	 */
	static class CursorPositionNode {

		private RowDataNavigation fCursor;

		private CursorPositionNode fParentNode;

		private long fPosition = -1;

		public CursorPositionNode getParentNode() {
			return fParentNode;
		}

		void setParentNode(CursorPositionNode parentNode) {
			fParentNode = parentNode;
		}

		CursorPositionNode(RowDataNavigation cursor) {
			fCursor = cursor;
		}

		long getPosition() {
			return fPosition;
		}

		void updatePosition() throws OLAPException {
			fPosition = fCursor.getPosition();
			if (fParentNode != null) {
				fParentNode.updatePosition();
			}
		}

		boolean positionIsChanged() throws OLAPException {
			if (fPosition != fCursor.getPosition()) {
				return true;
			} else if (fCursor.getPosition() == 0 && fParentNode != null) {
				return fParentNode.positionIsChanged();
			}
			return false;
		}
	}
}
