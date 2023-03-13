/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.AbstractDataEngine;
import org.eclipse.birt.report.engine.data.dte.CubeResultSet;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

public class QueryUtil {

	/*
	 * Fetch all the result sets that the instanceID refers to.
	 */
	public static List<IBaseResultSet> getResultSet(ReportContent report, InstanceID instanceID) {
		Report design = report.getDesign();
		ExecutionContext context = report.getExecutionContext();
		try {
			ArrayList<QueryTask> plan = createPlan(design, instanceID);

			return executePlan(context, plan);
		} catch (EngineException ex) {
			context.addException(ex);
		}
		return null;
	}

	/*
	 * Return the result set id from which this instanceID is generated
	 */
	public static String getResultSetId(ReportContent report, InstanceID instanceID) {
		Report design = report.getDesign();
		ArrayList<QueryTask> plan = createPlan(design, instanceID);
		if (plan == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		ExecutionContext executionContext = report.getExecutionContext();
		try {
			for (int current = plan.size() - 1; current >= 0; current--) {
				QueryTask task = plan.get(current);
				IDataQueryDefinition query = task.getQuery();
				if (task.getParent() == null) {
					String rset = getResultSetID(executionContext, null, "-1", query);
					if (rset == null) {
						return null;
					}
					sb.append(rset);
				} else {
					if (sb.length() == 0) {
						throw new EngineException(MessageConstants.INVALID_INSTANCE_ID_ERROR, instanceID);
					}

					long rowid = task.getRowID();
					if (query instanceof ISubqueryDefinition) {
						String queryName = query.getName();
						sb.insert(0, "{");
						sb.append("}.").append(rowid).append(".").append(queryName);
					} else {
						String id = task.getCellID();
						if (id == null) {
							id = String.valueOf(rowid);
						}
						String rset = getResultSetID(executionContext, sb.toString(), id, query);
						sb.setLength(0);
						if (rset != null) {
							sb.append(rset);
						} else {
							// TODO: if possible to come here
						}
					}
				}
			}
		} catch (EngineException ex) {
			executionContext.addException(ex);
		}

		return sb.toString();
	}

	/*
	 * create a plan which contains only table queries.
	 */
	static public ArrayList<QueryTask> createTablePlan(Report report, InstanceID instanceId) throws EngineException {
		InstanceID iid = instanceId;
		IBaseQueryDefinition query = null;
		while (iid != null) {
			long id = iid.getComponentID();
			ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);
			IDataQueryDefinition dataQuery = design.getQuery();

			if (dataQuery != null) {
				ReportItemHandle handle = (ReportItemHandle) design.getHandle();
				if (!handle.allowExport()) {
					throw new EngineException(MessageConstants.RESULTSET_EXTRACT_ERROR);
				}
				if (!(dataQuery instanceof IBaseQueryDefinition)) {
					// it is a cube query, as we don't support it now, exit.
					return null;
				}
				query = (IBaseQueryDefinition) dataQuery;
				break;
			}
			iid = iid.getParentID();
		}
		// At this point, query refers to the last query in the query chain.
		ArrayList<DataSetID> dsIDs = new ArrayList<>();
		ArrayList plan = new ArrayList();
		while (query != null) {
			while (iid != null) {
				if (iid.getDataID() != null) {
					DataID dataId = iid.getDataID();
					DataSetID dsId = dataId.getDataSetID();
					boolean found = false;
					Iterator<DataSetID> itr = dsIDs.iterator();
					while (itr.hasNext()) {
						if (itr.next().equals(dsId)) {
							found = true;
							break;
						}
					}
					if (!found) {
						dsIDs.add(dsId);
						QueryTask task;
						if (dataId.getCellID() != null) {
							task = new QueryTask(query, dsId, dataId.getCellID(), iid);
						} else {
							task = new QueryTask(query, dsId, (int) dataId.getRowID(), iid);
						}

						plan.add(task);
						break;
					}
				}
				iid = iid.getParentID();
			}
			if (iid == null) {
				break;
			}
			query = query.getParentQuery();
		}
		// At this point, query refers to the top most query
		QueryTask task = new QueryTask(query, null, -1, null);
		plan.add(task);
		return plan;
	}

	/*
	 * create a plan; this plan gets all data queries.
	 */
	static public ArrayList<QueryTask> createPlan(Report report, InstanceID instanceId) {
		ArrayList<IDataQueryDefinition> queries = new ArrayList<>();
		InstanceID iid = instanceId;
		InstanceID dsIID = null;
		while (iid != null) {
			long id = iid.getComponentID();
			ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);
			if (design != null) {
				IDataQueryDefinition query = design.getQuery();
				if (query != null) {
					queries.add(query);
					if (dsIID == null) {
						/*
						 * From BIRT 2.3.0, there is no chance that an instance id has both design id
						 * and its own data id. But in older BIRT exists that an instance id is composed
						 * of its own data id; at least, Chart generates such instance id. So, dsIID
						 * jumps one layer up from the lowest instance id.
						 */
						dsIID = iid.getParentID();
					}
				}
			}
			iid = iid.getParentID();
		}
		if (queries.size() == 0) {
			return null;
		}
		ArrayList datasets = new ArrayList();
		ArrayList plan = new ArrayList();
		for (IDataQueryDefinition query : queries) {
			while (dsIID != null) {
				if (dsIID.getDataID() != null) {
					DataID dataId = dsIID.getDataID();
					DataSetID dsId = dataId.getDataSetID();
					if (!datasets.contains(dsId)) {
						datasets.add(dsId);
						QueryTask task = null;
						if (dataId.getCellID() != null) {
							task = new QueryTask(query, dsId, dataId.getCellID(), dsIID);
						} else {
							task = new QueryTask(query, dsId, (int) dataId.getRowID(), dsIID);
						}
						plan.add(task);
						break;
					}

				}
				dsIID = dsIID.getParentID();
			}
			if (dsIID == null) {
				break;
			}
		}
		QueryTask task = new QueryTask(queries.get(queries.size() - 1), null, -1, null);
		plan.add(task);
		return plan;
	}

	/*
	 *
	 */
	static public List executePlan(final ExecutionContext executionContext, ArrayList<QueryTask> plan)
			throws EngineException {
		List results = new ArrayList();
		IBaseResultSet parent = executePlan(plan, 0, executionContext, new IResultSetIDProvider() {

			@Override
			public String getResultsID(String parent, String rawId, IDataQueryDefinition query) {
				return getResultSetID(executionContext, parent, rawId, query);
			}
		});
		if (parent != null && !results.contains(parent)) {
			results.add(parent);
		}
		return results;
	}

	public static IBaseResultSet executePlan(List<QueryTask> plan, int index, ExecutionContext executionContext,
			IResultSetIDProvider resultsIDProvider) throws EngineException {
		if (plan == null || plan.size() == 0) {
			return null;
		}
		IBaseResultSet parent = null;
		try {
			for (int current = plan.size() - 1; current >= index; current--) {
				QueryTask task = plan.get(current);
				IDataQueryDefinition query = task.getQuery();
				if (task.getParent() == null) {
					// this is a top query
					String rsID = resultsIDProvider.getResultsID(null, "-1", query);
					IBaseQueryResults baseResults = QueryUtil.executeQuery(null, query, rsID, executionContext);
					if (baseResults == null) {
						return null;
					}
					if (baseResults instanceof IQueryResults) {
						parent = new QueryResultSet(executionContext.getDataEngine(), executionContext,
								(IQueryDefinition) query, (IQueryResults) baseResults);
					} else if (baseResults instanceof ICubeQueryResults) {
						parent = new CubeResultSet(executionContext.getDataEngine(), executionContext,
								(ICubeQueryDefinition) query, (ICubeQueryResults) baseResults);
					} else {
						// should not go here
						return null;
					}
				} else {
					assert parent != null;

					// skip parent to the proper position
					String parentID = null;
					if (parent instanceof IQueryResultSet) {
						IResultIterator parentItr = ((IQueryResultSet) parent).getResultIterator();
						parentItr.moveTo(task.getRowID());
						parentID = ((QueryResultSet) parent).getQueryResultsID();
					} else if (parent instanceof ICubeResultSet) {
						((ICubeResultSet) parent).skipTo(task.getCellID());
						parentID = ((CubeResultSet) parent).getQueryResultsID();
					}

					if (query instanceof ISubqueryDefinition) {
						IResultIterator parentItr = ((QueryResultSet) parent).getResultIterator();
						String queryName = query.getName();
						ScriptContext scriptContext = executionContext.getScriptContext();
						IResultIterator itr = parentItr.getSecondaryIterator(scriptContext, queryName);
						parent = new QueryResultSet((QueryResultSet) parent, (ISubqueryDefinition) query, itr);
					} else {
						String rsID = resultsIDProvider.getResultsID(parentID, parent.getRawID(), query);
						// TODO: if rsID is null, return?
						IBaseQueryResults baseResults = QueryUtil.executeQuery(parent.getQueryResults(), query, rsID,
								executionContext);
						if (baseResults instanceof IQueryResults) {
							parent = new QueryResultSet(executionContext.getDataEngine(), executionContext, parent,
									(IQueryDefinition) query, (IQueryResults) baseResults);
						} else if (baseResults instanceof ICubeQueryResults) {
							if (query instanceof ICubeQueryDefinition) {
								parent = new CubeResultSet(executionContext.getDataEngine(), executionContext, parent,
										(ICubeQueryDefinition) query, (ICubeQueryResults) baseResults);
							} else if (query instanceof ISubCubeQueryDefinition) {
								parent = new CubeResultSet(executionContext.getDataEngine(), executionContext, parent,
										(ISubCubeQueryDefinition) query, (ICubeQueryResults) baseResults);
							}
						} else {
							// should not go here
							return null;
						}
					}
				}
			}
		} catch (EngineException ex) {
			throw ex;
		} catch (BirtException ex) {
			throw new EngineException(ex);
		}
		return parent;
	}

	private static String getResultSetID(ExecutionContext context, String parent, String rowId,
			IDataQueryDefinition query) {
		IDataEngine engine = null;
		try {
			engine = context.getDataEngine();
		} catch (EngineException e) {
			context.addException(e);
		}
		if (engine instanceof AbstractDataEngine) {
			AbstractDataEngine dataEngine = (AbstractDataEngine) engine;
			String queryId = dataEngine.getQueryID(query);
			String result = dataEngine.getResultID(parent, rowId, queryId);
			if (result == null) {
				result = dataEngine.getResultIDByRowID(parent, rowId, queryId);
			}
			return result;
		}
		return null;
	}

	static public void processQueryExtensions(IDataQueryDefinition query, ExecutionContext executionContext)
			throws EngineException {
		String[] extensions = executionContext.getEngineExtensions();
		if (extensions != null) {
			EngineExtensionManager manager = executionContext.getEngineExtensionManager();
			for (String extensionName : extensions) {
				IDataExtension extension = manager.getDataExtension(extensionName);
				if (extension != null) {
					extension.prepareQuery(query);
				}
			}
		}
	}

	/**
	 * This method executes IQueryDefinition, ICubeQueryDefinition and
	 * ISubCubeQueryDefinition; ISubqueryDefinition is not included here.
	 */
	static public IBaseQueryResults executeQuery(IBaseQueryResults parent, IDataQueryDefinition query, String rset,
			ExecutionContext executionContext) throws EngineException {
		try {
			DataRequestSession dataSession = executionContext.getDataEngine().getDTESession();
			if (dataSession == null) {
				return null;
			}
			Map appContext = executionContext.getAppContext();
			dataSession.getDataSessionContext().setAppContext(appContext);
			ScriptContext scriptContext = executionContext.getScriptContext();
			if (query instanceof QueryDefinition) {
				QueryDefinition tmpQuery = (QueryDefinition) query;
				tmpQuery.setQueryResultsID(rset);
				processQueryExtensions(query, executionContext);
				IPreparedQuery pQuery = dataSession.prepare(tmpQuery);
				if (pQuery == null) {
					return null;
				}
				return dataSession.execute(pQuery, parent, scriptContext);
			} else if (query instanceof ICubeQueryDefinition) {
				ICubeQueryDefinition cubeQuery = (ICubeQueryDefinition) query;
				cubeQuery.setQueryResultsID(rset);
				processQueryExtensions(query, executionContext);
				IPreparedCubeQuery pQuery = dataSession.prepare(cubeQuery);
				if (pQuery == null) {
					return null;
				}
				return dataSession.execute(pQuery, parent, scriptContext);
			} else if (query instanceof ISubCubeQueryDefinition) {
				ISubCubeQueryDefinition cubeQuery = (ISubCubeQueryDefinition) query;
				IPreparedCubeQuery pQuery = dataSession.prepare(cubeQuery);
				if (pQuery == null) {
					return null;
				}
				return dataSession.execute(pQuery, parent, scriptContext);
			}
		} catch (BirtException ex) {
			throw new EngineException(ex);
		}
		return null;
	}

	public static QueryDefinition cloneQuery(QueryDefinition query) {
		if (query == null) {
			return null;
		}

		IBaseQueryDefinition parent = query.getParentQuery();
		QueryDefinition newQuery = null;
		if (parent instanceof BaseQueryDefinition) {
			newQuery = new QueryDefinition((BaseQueryDefinition) parent, query.needAutoBinding());
		} else {
			newQuery = new QueryDefinition(query.needAutoBinding());
		}
		newQuery.getBindings().putAll(query.getBindings());
		newQuery.getFilters().addAll(query.getFilters());
		newQuery.getSorts().addAll(query.getSorts());
		newQuery.getSubqueries().addAll(query.getSubqueries());
		newQuery.getGroups().addAll(query.getGroups());
		newQuery.setUsesDetails(query.usesDetails());
		newQuery.setMaxRows(query.getMaxRows());

		newQuery.setDataSetName(query.getDataSetName());
		newQuery.setColumnProjection(query.getColumnProjection());

		newQuery.setName(query.getName());
		newQuery.setIsSummaryQuery(query.isSummaryQuery());

		newQuery.setQueryExecutionHints(query.getQueryExecutionHints());
		newQuery.setLinks(query.getLinks());
		return newQuery;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getColumnNames(DataSetHandle dataset) {
		CachedMetaDataHandle cachedMetaDataHandle = dataset.getCachedMetaDataHandle();
		if (cachedMetaDataHandle != null) {
			List<ResultSetColumn> resultSetColumns = (List<ResultSetColumn>) cachedMetaDataHandle
					.getProperty(CachedMetaData.RESULT_SET_MEMBER);
			if (resultSetColumns == null) {
				return Collections.EMPTY_LIST;
			}
			ArrayList<String> columnNames = new ArrayList<>();
			for (ResultSetColumn column : resultSetColumns) {
				String columnName = column.getColumnName();
				columnNames.add(columnName);
			}
			return columnNames;
		}
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, ResultSetColumn> getResultSetColumns(DataSetHandle dataset) {
		CachedMetaDataHandle cachedMetaDataHandle = dataset.getCachedMetaDataHandle();
		if (cachedMetaDataHandle != null) {
			List<ResultSetColumn> resultSetColumns = (List<ResultSetColumn>) cachedMetaDataHandle
					.getProperty(CachedMetaData.RESULT_SET_MEMBER);
			if (resultSetColumns == null) {
				return Collections.EMPTY_MAP;
			}
			Map<String, ResultSetColumn> retVal = new LinkedHashMap<>();
			for (ResultSetColumn col : resultSetColumns) {
				retVal.put(col.getColumnName(), col);
			}
			return retVal;
		}
		return Collections.EMPTY_MAP;
	}

	public static void addBinding(IQueryDefinition query, String column) throws DataException {
		ScriptExpression expr = new ScriptExpression(ExpressionUtil.createDataSetRowExpression(column));
		IBinding binding = new Binding(column, expr);
		query.addBinding(binding);
	}

	public static void addBinding(IQueryDefinition query, ResultSetColumn column) throws DataException {
		ScriptExpression expr = new ScriptExpression(ExpressionUtil.createDataSetRowExpression(column.getColumnName()));
		IBinding binding = new Binding(column.getColumnName(), expr);
		binding.setDataType(DataAdapterUtil.adaptModelDataType(column.getDataType()));
		query.addBinding(binding);
	}

	public interface IResultSetIDProvider {
		String getResultsID(String parent, String rawId, IDataQueryDefinition query);
	}
}
