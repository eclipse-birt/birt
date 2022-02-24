/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.cursor.CubeCursorImpl;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationFactory;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutorHints;
import org.eclipse.birt.data.engine.olap.impl.query.IPreparedCubeOperation;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.mozilla.javascript.Scriptable;

/**
 * A <code>BirtCubeView</code> represents a multi-dimensional selection of
 * values. A view contains a collection of <code>BirtEdgeView</code> that group
 * dimensions into a logical layout. This view has three types of
 * edges:rowEdgeView, coulumnEdgeView, measureEdgeView. A BirtCubeView has
 * association with a CubeCursor. This association will provide a user a way to
 * get data for the current intersection of the multi-dimensional selection.
 * 
 */
public class BirtCubeView {
	private BirtEdgeView columnEdgeView, rowEdgeView, pageEdgeView;
	private AggregationRegisterTable registerTable;
	private CubeQueryExecutor executor;
	private Map appContext;
	private Map measureMapping;
	private IQueryExecutor queryExecutor;
	private IResultSet parentResultSet;
	private IPreparedCubeOperation[] preparedCubeOperations;
	private CubeCursor cubeCursor;
	private ICube cube;
	private IBindingValueFetcher fetcher;
	private CubeQueryExecutorHints executorHints;

	private BirtCubeView() {
	}

	/**
	 * Constructor: construct the row/column/measure EdgeView.
	 * 
	 * @param queryExecutor
	 * @throws DataException
	 */
	public BirtCubeView(CubeQueryExecutor queryExecutor, ICube cube, Map appContext, IBindingValueFetcher fetcher)
			throws DataException {
		this.executor = queryExecutor;
		this.cube = cube;
		this.fetcher = fetcher;
		pageEdgeView = createBirtEdgeView(this.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.PAGE_EDGE),
				ICubeQueryDefinition.PAGE_EDGE);
		columnEdgeView = createBirtEdgeView(this.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.COLUMN_EDGE),
				ICubeQueryDefinition.COLUMN_EDGE);
		rowEdgeView = createBirtEdgeView(this.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.ROW_EDGE),
				ICubeQueryDefinition.ROW_EDGE);

		this.executor = queryExecutor;
		this.appContext = appContext;
		prepareCubeQuery(queryExecutor.getSession().getEngineContext().getScriptContext(), queryExecutor.getScope());
	}

	private void prepareCubeQuery(ScriptContext context, Scriptable scope) throws DataException {
		measureMapping = new HashMap();
		CalculatedMember[] members = CubeQueryDefinitionUtil.getCalculatedMembers(this.getCubeQueryDefinition(), scope,
				measureMapping, context);
		registerTable = new AggregationRegisterTable(members);
		prepareCubeOperations(registerTable);
	}

	private void prepareCubeOperations(AggregationRegisterTable manager) throws DataException {
		List<IBinding> bindings = new ArrayList<IBinding>(this.getCubeQueryDefinition().getBindings());
		Scriptable scope = executor.getSession().getSharedScope();
		ScriptContext cx = executor.getSession().getEngineContext().getScriptContext();
		preparedCubeOperations = new IPreparedCubeOperation[this.getCubeQueryDefinition().getCubeOperations().length];
		int i = 0;
		for (ICubeOperation co : this.getCubeQueryDefinition().getCubeOperations()) {
			IPreparedCubeOperation pco = CubeOperationFactory.createPreparedCubeOperation(co);
			preparedCubeOperations[i++] = pco;
			pco.prepare(scope, cx, manager, bindings.toArray(new IBinding[0]), this.getCubeQueryDefinition());
			bindings.addAll(Arrays.asList(co.getNewBindings()));
		}
	}

	private CubeAggrDefn[] getAggrDefnsFromOperations() {
		List<CubeAggrDefn> result = new ArrayList<CubeAggrDefn>();
		for (IPreparedCubeOperation pco : this.preparedCubeOperations) {
			result.addAll(Arrays.asList(pco.getNewCubeAggrDefns()));
		}
		return result.toArray(new CubeAggrDefn[0]);
	}

	public BirtCubeView createSubView() throws DataException {
		BirtCubeView subView = new BirtCubeView();
		subView.executor = executor;
		subView.pageEdgeView = createBirtEdgeView(
				subView.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.PAGE_EDGE),
				ICubeQueryDefinition.PAGE_EDGE);
		subView.columnEdgeView = createBirtEdgeView(
				subView.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.COLUMN_EDGE),
				ICubeQueryDefinition.COLUMN_EDGE);
		subView.rowEdgeView = createBirtEdgeView(
				subView.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.ROW_EDGE), ICubeQueryDefinition.ROW_EDGE);

		subView.measureMapping = measureMapping;
		subView.preparedCubeOperations = preparedCubeOperations;
		subView.registerTable = registerTable;
		return subView;
	}

	public AggregationRegisterTable getAggregationRegisterTable() {
		return registerTable;
	}

	/**
	 * Constructor: construct the row/column/measure EdgeView. for test usage
	 * 
	 * @param queryExecutor
	 * @throws DataException
	 */
	public BirtCubeView(CubeQueryExecutor queryExecutor) throws DataException {
		this(queryExecutor, null, null, null);
	}

	/**
	 * Get cubeCursor for current cubeView.
	 * 
	 * @param stopSign
	 * @return CubeCursor
	 * @throws OLAPException
	 * @throws DataException
	 */
	public CubeCursor getCubeCursor(StopSign stopSign, ICube cube) throws OLAPException, DataException {
		if (cubeCursor == null)
			cubeCursor = createCubeCursor(stopSign, this.getCubeQueryDefinition(), cube);
		return cubeCursor;
	}

	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws DataException
	 * @throws OLAPException
	 */
	private CubeCursor createCubeCursor(StopSign stopSign, ICubeQueryDefinition query, ICube cube)
			throws DataException, OLAPException {
		if (query.getQueryResultsID() != null) {
			int version = new VersionManager(executor.getContext()).getVersion(query.getQueryResultsID());
			if (version < VersionManager.VERSION_4_2_3)
				queryExecutor = new QueryExecutorV0();
		}
		if (queryExecutor == null)
			queryExecutor = new QueryExecutorV1();
		try {
			parentResultSet = queryExecutor.execute(this, stopSign, cube, this.fetcher);
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage(), e);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		CubeCursor cubeCursor;
		Map referedLevel = getReferencedLevels();
		if (this.appContext != null && this.executor.getContext().getMode() == DataEngineContext.DIRECT_PRESENTATION) {
			cubeCursor = new CubeCursorImpl(this, parentResultSet, referedLevel, appContext);
		} else {
			cubeCursor = new CubeCursorImpl(this, parentResultSet, referedLevel);
		}
		return cubeCursor;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public Map getReferencedLevels() throws DataException {
		Map relationMap = CubeQueryDefinitionUtil.getRelationWithMeasure(this.getCubeQueryDefinition(), measureMapping,
				this.getAggrDefnsFromOperations());
		return relationMap;
	}

	/**
	 * Get cubeCursor for current cubeView.
	 * 
	 * @param stopSign
	 * @return CubeCursor
	 * @throws OLAPException
	 * @throws DataException
	 */
	public CubeCursor getCubeCursor(StopSign stopSign, String startingColumnLevel, String startingRowLevel,
			BirtCubeView parentView) throws OLAPException, DataException {
		if (parentView == null || parentView.getCubeQueryExecutor() == null) {
			throw new DataException(ResourceConstants.NO_PARENT_RESULT_CURSOR);
		}
		Map relationMap = CubeQueryDefinitionUtil.getRelationWithMeasure(this.getCubeQueryDefinition(), measureMapping,
				this.getAggrDefnsFromOperations());

		int startingColumnLevelIndex = -1, startingRowLevelIndex = -1;
		if (startingColumnLevel != null)
			startingColumnLevelIndex = CubeQueryDefinitionUtil.getLevelIndex(this.getCubeQueryDefinition(),
					startingColumnLevel, ICubeQueryDefinition.COLUMN_EDGE);
		if (startingRowLevel != null)
			startingRowLevelIndex = CubeQueryDefinitionUtil.getLevelIndex(this.getCubeQueryDefinition(),
					startingRowLevel, ICubeQueryDefinition.ROW_EDGE);
		if (startingColumnLevelIndex == -1 && startingRowLevelIndex == -1) {
			startingColumnLevelIndex = CubeQueryDefinitionUtil
					.getLevelsOnEdge(this.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.COLUMN_EDGE)).length
					- 1;
			startingRowLevelIndex = CubeQueryDefinitionUtil
					.getLevelsOnEdge(this.getCubeQueryDefinition().getEdge(ICubeQueryDefinition.ROW_EDGE)).length - 1;
		}
		this.queryExecutor = parentView.getQueryExecutor();
		try {
			parentResultSet = queryExecutor.executeSubQuery(parentView.getResultSet(), this, startingColumnLevelIndex,
					startingRowLevelIndex);
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
		CubeCursor cubeCursor = new CubeCursorImpl(this, parentResultSet, relationMap);
		return cubeCursor;
	}

	/**
	 * 
	 * @return
	 */
	public IQueryExecutor getQueryExecutor() {
		return this.queryExecutor;
	}

	/**
	 * 
	 * @return
	 */
	public CubeQueryExecutor getCubeQueryExecutor() {
		return this.executor;
	}

	/**
	 * 
	 * @return
	 */
	public IResultSet getResultSet() {
		return this.parentResultSet;
	}

	/**
	 * @return
	 */
	public BirtEdgeView getRowEdgeView() {
		return this.rowEdgeView;
	}

	/**
	 * @return
	 */
	public BirtEdgeView getColumnEdgeView() {
		return this.columnEdgeView;
	}

	/**
	 * @return
	 */
	public BirtEdgeView getPageEdgeView() {
		return this.pageEdgeView;
	}

	public ICube getCube() {
		return this.cube;
	}

	public Map getAppContext() {
		return appContext;
	}

	/**
	 * 
	 * @return
	 */
	public BirtEdgeView[] getMeasureEdgeView() {
		BirtEdgeView[] calculatedMemberViews = null;
		CalculatedMember[] members = registerTable.getCalculatedMembers();
		if (members != null && members.length > 0) {
			Set rsIDSet = new HashSet();
			calculatedMemberViews = new BirtEdgeView[members.length];
			int index = 0;
			for (int i = 0; i < members.length; i++) {
				if (rsIDSet.contains(Integer.valueOf(members[i].getRsID())))
					continue;
				calculatedMemberViews[index] = this.createBirtEdgeView(members[i]);
				rsIDSet.add(Integer.valueOf(members[i].getRsID()));
				index++;
			}
		}
		return calculatedMemberViews;
	}

	/**
	 * 
	 * @return
	 */
	public Map getMeasureMapping() {
		return measureMapping;
	}

	/**
	 * 
	 * @param edgeDefn
	 * @return
	 */
	private BirtEdgeView createBirtEdgeView(IEdgeDefinition edgeDefn, int type) {
		if (edgeDefn == null)
			return null;
		return new BirtEdgeView(this, edgeDefn, type);
	}

	/**
	 * 
	 * @param calculatedMember
	 * @return
	 */
	private BirtEdgeView createBirtEdgeView(CalculatedMember calculatedMember) {
		return new BirtEdgeView(calculatedMember);
	}

	public ICubeQueryDefinition getCubeQueryDefinition() {
		return executor.getCubeQueryDefinition();
	}

	protected IPreparedCubeOperation[] getPreparedCubeOperations() {
		return preparedCubeOperations;
	}

	public CubeQueryExecutorHints getCubeQueryExecutionHints() {
		return this.executorHints;
	}

	public void setCubeQueryExecutionHints(CubeQueryExecutorHints hints) {
		this.executorHints = hints;
	}
}
