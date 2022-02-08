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

package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.QueryPrepareUtil;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.document.CubeRADocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.BindingValueFetcher;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.script.JSLevelAccessor;
import org.eclipse.birt.data.engine.olap.script.JSMeasureAccessor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class CubeQueryResults implements ICubeQueryResults {

	protected ICubeQueryDefinition cubeQueryDefinition;
	private Scriptable scope;
	protected DataEngineContext context;
	protected DataEngineSession session;
	private String queryResultsId;
	protected Map appContext;
	private StopSign stopSign;
	private IBaseQueryResults outResults;
	protected ICubeCursor cubeCursor;
	private String name;
	private PreparedCubeQuery preparedQuery;
	protected CubeQueryExecutor executor;

	protected static Logger logger = Logger.getLogger(CubeQueryResults.class.getName());

	/**
	 * 
	 * @param preparedQuery
	 * @param scope
	 */
	public CubeQueryResults(IBaseQueryResults outResults, PreparedCubeQuery preparedQuery, DataEngineSession session,
			Scriptable scope, DataEngineContext context, Map appContext) {
		this.cubeQueryDefinition = (ICubeQueryDefinition) preparedQuery.getCubeQueryDefinition();
		this.preparedQuery = preparedQuery;
		this.scope = scope;
		this.context = context;
		this.session = session;
		this.appContext = appContext;
		this.queryResultsId = cubeQueryDefinition.getQueryResultsID();
		this.outResults = outResults;
		this.stopSign = session.getStopSign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.ICubeQueryResults#getCubeCursor()
	 */
	public ICubeCursor getCubeCursor() throws DataException {
		if (this.cubeCursor != null)
			return this.cubeCursor;
		try {
			if (this.session.getEngineContext().getMode() == DataEngineContext.MODE_PRESENTATION) {
				this.cubeCursor = createCursor(null, null);
				return this.cubeCursor;
			}

			stopSign.start();
			Set<String> involvedDerivedMeasure = new HashSet<String>();
			Set<String> derivedMeasureNames = OlapExpressionUtil
					.getDerivedMeasureNames(this.cubeQueryDefinition.getBindings());
			List<IBinding> bindingSet = new ArrayList<IBinding>();
			bindingSet.addAll(this.cubeQueryDefinition.getBindings());
			CubeQueryExecutorHints hints = new CubeQueryExecutorHints();

			if (this.cubeQueryDefinition instanceof PreparedCubeQueryDefinition) {
				Set<IBinding> binding4NestedAggr = ((PreparedCubeQueryDefinition) this.cubeQueryDefinition)
						.getBindingsForNestAggregation();
				binding4NestedAggr.addAll(org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil
						.getNewBindingsFromCubeOperations(this.cubeQueryDefinition));
				for (IBinding binding : binding4NestedAggr) {
					if (OlapExpressionUtil.isAggregationBinding(binding)) {
						IBaseExpression expr = binding.getExpression();
						involvedDerivedMeasure.addAll(this.getInvolvedDerivedMeasure(expr, derivedMeasureNames,
								this.cubeQueryDefinition.getBindings()));
						if (!involvedDerivedMeasure.isEmpty()) {
							hints.executeCubeOperation(false);
							hints.executeDrillOperation(false);
						}
					}
				}
			}

			if (this.cubeQueryDefinition instanceof PreparedCubeQueryDefinition) {
				Set<IBinding> binding4NestedAggr = ((PreparedCubeQueryDefinition) this.cubeQueryDefinition)
						.getBindingsForNestAggregation();
				for (IBinding binding : binding4NestedAggr) {
					derivedMeasureNames.add(binding.getBindingName());
				}
				bindingSet.addAll(binding4NestedAggr);

			}
			List<IFilterDefinition> derivedMeasureFilters = new ArrayList<IFilterDefinition>();
			if (!this.cubeQueryDefinition.getFilters().isEmpty()) {
				for (IFilterDefinition filter : (List<IFilterDefinition>) this.cubeQueryDefinition.getFilters()) {
					IBaseExpression expr = filter.getExpression();
					Set<String> temp = this.getInvolvedDerivedMeasure(expr, derivedMeasureNames,
							this.cubeQueryDefinition.getBindings());
					if (temp.size() > 0)
						derivedMeasureFilters.add(filter);
					involvedDerivedMeasure.addAll(temp);
				}
			}

			if (!this.cubeQueryDefinition.getSorts().isEmpty()) {
				for (ISortDefinition sort : (List<ISortDefinition>) this.cubeQueryDefinition.getSorts()) {
					IBaseExpression expr = sort.getExpression();
					involvedDerivedMeasure.addAll(this.getInvolvedDerivedMeasure(expr, derivedMeasureNames,
							this.cubeQueryDefinition.getBindings()));
				}
			}

			if (involvedDerivedMeasure.isEmpty()) {
				this.cubeCursor = createCursor(null, hints);
			} else {
				List<String> candidateBindingOfInteresting = new ArrayList<String>();
				candidateBindingOfInteresting.addAll(involvedDerivedMeasure);
				List<Set<String>> bindingDimLevels = new ArrayList<Set<String>>();

				for (String bindingName : candidateBindingOfInteresting) {
					Set<IDimLevel> dimLevels = OlapExpressionUtil.getAggregateOnLevel(bindingName, bindingSet,
							getMeasureDimLevel());
					Set<String> temp = new HashSet<String>();
					for (IDimLevel dl : dimLevels) {
						temp.add(OlapExpressionUtil.getAttrReference(dl.getDimensionName(), dl.getLevelName(),
								dl.getLevelName()));
					}
					bindingDimLevels.add(temp);
				}
				List<IFilterDefinition> filterTemp = new ArrayList<IFilterDefinition>();
				List<ISortDefinition> sortTemp = new ArrayList<ISortDefinition>();
				filterTemp.addAll(this.cubeQueryDefinition.getFilters());
				sortTemp.addAll(this.cubeQueryDefinition.getSorts());
				this.cubeQueryDefinition.getFilters().removeAll(derivedMeasureFilters);
				this.cubeQueryDefinition.getSorts().clear();
				hints.needSaveToDoc(false);
				this.cubeCursor = createCursor(null, hints);
				this.cubeQueryDefinition.getFilters().clear();
				this.cubeQueryDefinition.getFilters().addAll(filterTemp);
				this.cubeQueryDefinition.getSorts().addAll(sortTemp);
				BindingValueFetcher fetcher = new BindingValueFetcher(this.cubeCursor, this.cubeQueryDefinition,
						candidateBindingOfInteresting, bindingDimLevels);
				hints.executeCubeOperation(true);
				hints.needSaveToDoc(true);
				hints.executeDrillOperation(true);
				this.cubeCursor = createCursor(fetcher, hints);
			}
			return this.cubeCursor;

		} catch (OLAPException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		} catch (DataException e) {
			// if fail to load cube, return a NULL result cursor.
			if (e.getErrorCode() == ResourceConstants.FAIL_LOAD_CUBE) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
			throw e;
		}
	}

	private Set<IDimLevel> getMeasureDimLevel() {
		Set<IDimLevel> result = getDimLevels(ICubeQueryDefinition.COLUMN_EDGE);
		result.addAll(getDimLevels(ICubeQueryDefinition.ROW_EDGE));
		return result;
	}

	private Set<IDimLevel> getDimLevels(int edge) {
		Set<IDimLevel> result = new HashSet<IDimLevel>();
		IEdgeDefinition edgeDefn = this.cubeQueryDefinition.getEdge(edge);
		if (edgeDefn != null) {
			List<IDimensionDefinition> dimDefns = edgeDefn.getDimensions();
			for (IDimensionDefinition dim : dimDefns) {
				List<IHierarchyDefinition> hier = dim.getHierarchy();
				if (hier.size() == 1) {
					List<ILevelDefinition> levels = dim.getHierarchy().get(0).getLevels();
					for (ILevelDefinition level : levels) {
						result.add(new DimLevel(dim.getName(), level.getName()));
					}
				}
			}
		}
		return result;
	}

	private Set<String> getInvolvedDerivedMeasure(IBaseExpression expr, Set<String> derivedMeasureNames,
			List<IBinding> bindings) throws DataException {
		Set<String> result = new HashSet<String>();
		if (!OlapExpressionUtil.isDirectRerenrence(expr, bindings)) {
			List<String> involvedMeasureNames = ExpressionCompilerUtil.extractColumnExpression(expr,
					ExpressionUtil.DATA_INDICATOR);
			for (String candidate : involvedMeasureNames) {
				if (derivedMeasureNames.contains(candidate)) {
					result.add(candidate);
				}
			}
		}
		return result;
	}

	private ICubeCursor createCursor(IBindingValueFetcher fetcher, CubeQueryExecutorHints hints)
			throws DataException, IOException, OLAPException {
		ICubeCursor cursor;
		executor = new CubeQueryExecutor(this.outResults, cubeQueryDefinition, this.session, this.scope, this.context);
		executor.getdimensionSimpleFilter().addAll(this.preparedQuery.getInternalFilters());

		IDocumentManager documentManager = getDocumentManager(executor);
		ICube cube = null;

		try {
			// need not load cube in render task.
			if (!isStandAloneQuery(cubeQueryDefinition, session.getEngineContext()))
				cube = loadCube(documentManager, executor);
		} catch (Exception ex) {
			throw new DataException(ResourceConstants.FAIL_LOAD_CUBE, ex);
		}

		BirtCubeView bcv = new BirtCubeView(executor, cube, appContext, fetcher);
		bcv.setCubeQueryExecutionHints(hints);
		CubeCursor cubeCursor = bcv.getCubeCursor(stopSign, cube);
		if (cube != null)
			cube.close();

		String newResultSetId = executor.getQueryResultsId();
		if (newResultSetId != null) {
			this.queryResultsId = newResultSetId;
		}
		this.scope.put(ScriptConstants.MEASURE_SCRIPTABLE, this.scope,
				new JSMeasureAccessor(cubeCursor, bcv.getMeasureMapping()));
		this.scope.put(ScriptConstants.DIMENSION_SCRIPTABLE, this.scope,
				new JSLevelAccessor(this.cubeQueryDefinition, bcv));

		cursor = new CubeCursorImpl(outResults, cubeCursor, this.scope, session.getEngineContext().getScriptContext(),
				cubeQueryDefinition, bcv);
		return cursor;
	}

	/**
	 * Get the document manager.
	 * 
	 * @param executor
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDocumentManager getDocumentManager(CubeQueryExecutor executor) throws DataException, IOException {
		IDocumentManager manager = null;
		if (preparedQuery.isFromDataMart()) {
			// cube should be always loaded from data mart, nothing to do with MODE
			return CubeRADocumentManagerFactory.createRADocumentManager(executor.getCubeQueryDefinition().getName(),
					executor.getContext().getDocReader());
		}
		if (executor.getContext().getMode() == DataEngineContext.DIRECT_PRESENTATION
				|| executor.getContext().getMode() == DataEngineContext.MODE_GENERATION) {
			manager = DocManagerMap.getDocManagerMap().get(String.valueOf(executor.getSession().getEngine().hashCode()),
					executor.getSession().getTempDir() + executor.getSession().getEngine().hashCode());
		}
		if (manager != null) {
			if (manager.exist(NamingUtil.getCubeDocName(executor.getCubeQueryDefinition().getName())))
				return manager;
		}
		return CubeRADocumentManagerFactory.createRADocumentManager(executor.getCubeQueryDefinition().getName(),
				executor.getContext().getDocReader());
	}

	/**
	 * 
	 * @param cubeName
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private ICube loadCube(IDocumentManager documentManager, CubeQueryExecutor executor)
			throws DataException, IOException {
		ICube cube = null;

		if (this.preparedQuery.getInaccessibleDimLevels() == null)
			cube = CubeQueryExecutorHelper.loadCube(executor.getCubeQueryDefinition().getName(), documentManager,
					executor.getSession());
		else
			cube = CubeQueryExecutorHelper.loadCube(executor.getCubeQueryDefinition().getName(), documentManager,
					executor.getSession().getStopSign(), this.preparedQuery.getInaccessibleDimLevels());
		return cube;
	}

	private static boolean isStandAloneQuery(ICubeQueryDefinition cubeQuery, DataEngineContext context) {
		if (cubeQuery.getQueryResultsID() != null && context.getMode() == DataEngineContext.MODE_PRESENTATION) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#getID()
	 */
	public String getID() {
		return this.queryResultsId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#close()
	 */
	public void close() throws BirtException {
		QueryPrepareUtil.clear(session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.ICubeQueryResults#cancel()
	 */
	public void cancel() {
		stopSign.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
	 */
	public String getName() {
		return name;
	}

}
