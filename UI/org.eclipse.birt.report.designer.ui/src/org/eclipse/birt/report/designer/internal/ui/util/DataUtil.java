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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.core.runtime.Assert;

/**
 * Data utilities for UI
 */

public class DataUtil {

	/**
	 * Gets the columns list from the data set
	 * 
	 * @param handle the handle of the data set
	 * @return the list of the columns
	 * @throws SemanticException
	 */
	public static List getColumnList(DataSetHandle handle) throws SemanticException {
		return getColumnList(handle, false);
	}

	public static List getColumnList(DataSetHandle handle, boolean refresh) throws SemanticException {
		List result = new ArrayList();
		if (handle != null) {
			if (refresh) {
				DataSetUIUtil.updateColumnCache(handle);
			}

			CachedMetaDataHandle meta = handle.getCachedMetaDataHandle();
			if (meta == null) {
				DataSetUIUtil.updateColumnCache(handle);
				meta = handle.getCachedMetaDataHandle();
			}

			if (meta != null) {
				MemberHandle resultSet = meta.getResultSet();

				if (resultSet.getListValue() != null) {
					for (int i = 0; i < resultSet.getListValue().size(); i++) {
						result.add(resultSet.getAt(i));
					}
				}
			}

		}
		return result;
	}

	/**
	 * Generate computed columns for the given report item with the closest data set
	 * available.
	 * 
	 * @param handle the handle of the report item
	 * 
	 * @return true if succeed,or fail if no column generated.
	 */
	public static List generateComputedColumns(ReportItemHandle handle) throws SemanticException {
		Assert.isNotNull(handle);
		DataSetHandle dataSetHandle = handle.getDataSet();
		if (dataSetHandle == null) {
			dataSetHandle = DEUtil.getBindingHolder(handle).getDataSet();
		}
		if (dataSetHandle != null) {
			List resultSetColumnList = getColumnList(dataSetHandle);
			ArrayList columnList = new ArrayList();
			String groupType = DEUtil.getGroupControlType(handle);
			List groupList = DEUtil.getGroups(handle);
			for (Iterator iter = resultSetColumnList.iterator(); iter.hasNext();) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) iter.next();
				ComputedColumn column = StructureFactory.createComputedColumn();
				column.setName(resultSetColumn.getColumnName());
				column.setDataType(resultSetColumn.getDataType());
				ExpressionUtility.setBindingColumnExpression(resultSetColumn, column);
				if (ExpressionUtil.hasAggregation(column.getExpression())) {
					if (groupType.equals(DEUtil.TYPE_GROUP_GROUP))
						column.setAggregateOn(((GroupHandle) groupList.get(0)).getName());
					else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING))
						column.setAggregateOn(null);
				}
				columnList.add(column);
			}
			return columnList;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Creates a query for the given data set
	 * 
	 * @param dataSet the handle of the data set
	 * @return the query created
	 * @throws BirtException
	 */
	public static final IPreparedQuery getPreparedQuery(DataEngine engine, DataSetHandle dataSet) throws BirtException {
		return getPreparedQuery(engine, dataSet, true);
	}

	/**
	 * @param dataSet
	 * @param useColumnHints
	 * @return
	 * @throws BirtException
	 */
	public static final IPreparedQuery getPreparedQuery(DataEngine engine, DataSetHandle dataSet,
			boolean useColumnHints) throws BirtException {
		return getPreparedQuery(engine, dataSet, useColumnHints, true);
	}

	/**
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	public static final IPreparedQuery getPreparedQuery(DataEngine engine, DataSetHandle dataSet,
			boolean useColumnHints, boolean useFilters) throws BirtException {
		return getPreparedQuery(engine, dataSet, null, useColumnHints, useFilters);

	}

	/**
	 * Gets prepared query, given Data set, Parameter binding, and useColumnHints,
	 * useFilters information.
	 * 
	 * @param dataSet        Given DataSet providing SQL query and parameters.
	 * @param bindingParams  Given Parameter bindings providing binded parameters,
	 *                       null if no binded parameters.
	 * @param useColumnHints Using column hints flag.
	 * @param useFilters     Using filters flag.
	 * @return IPreparedQeury
	 * @throws BirtException
	 */
	public static final IPreparedQuery getPreparedQuery(DataEngine engine, DataSetHandle dataSet,
			ParamBindingHandle[] bindingParams, boolean useColumnHints, boolean useFilters) throws BirtException {
		IBaseDataSetDesign dataSetDesign = getDataSetDesign(engine, dataSet, useColumnHints, useFilters);
		return engine.prepare(getQueryDefinition(dataSetDesign, bindingParams));
	}

	/**
	 * 
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @return
	 * @throws BirtException
	 */
	public static final IBaseDataSetDesign getDataSetDesign(DataEngine engine, DataSetHandle dataSet,
			boolean useColumnHints, boolean useFilters) throws BirtException {
		if (dataSet != null) {
			DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSet.getModuleHandle());
			DataRequestSession session = DataRequestSession.newSession(context);
			IModelAdapter adaptor = session.getModelAdaptor();
			IBaseDataSetDesign dataSetDesign = adaptor.adaptDataSet(dataSet);

			if (!useColumnHints) {
				dataSetDesign.getResultSetHints().clear();
			}
			if (!useFilters) {
				dataSetDesign.getFilters().clear();
			}
			if (!(dataSet instanceof JointDataSetHandle)) {
				IBaseDataSourceDesign dataSourceDesign = adaptor.adaptDataSource(dataSet.getDataSource());
				engine.defineDataSource(dataSourceDesign);
			}
			if (dataSet instanceof JointDataSetHandle) {
				defineSourceDataSets(engine, dataSet, dataSetDesign);
			}
			engine.defineDataSet(dataSetDesign);
			session.shutdown();
			return dataSetDesign;
		}
		return null;
	}

	/**
	 * @param dataSet
	 * @param dataSetDesign
	 * @throws BirtException
	 */
	private static void defineSourceDataSets(DataEngine engine, DataSetHandle dataSet, IBaseDataSetDesign dataSetDesign)
			throws BirtException {
		List dataSets = dataSet.getModuleHandle().getAllDataSets();
		for (int i = 0; i < dataSets.size(); i++) {
			DataSetHandle dsHandle = (DataSetHandle) dataSets.get(i);
			if (dsHandle.getName() != null) {
				if (dsHandle.getName().equals(((IJointDataSetDesign) dataSetDesign).getLeftDataSetDesignName())
						|| dsHandle.getName()
								.equals(((IJointDataSetDesign) dataSetDesign).getRightDataSetDesignName())) {
					getDataSetDesign(engine, dsHandle, true, true);
				}
			}
		}
	}

	/**
	 * @param dataSetDesign
	 * @param bindingParams
	 * @return
	 */
	public static final QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign,
			ParamBindingHandle[] bindingParams) {
		return getQueryDefinition(dataSetDesign, bindingParams, -1);
	}

	/**
	 * @param dataSetDesign
	 * @param bindingParams
	 * @param i
	 * @return
	 */
	public static QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign,
			ParamBindingHandle[] bindingParams, int rowsToReturn) {
		if (bindingParams == null || bindingParams.length == 0) {
			return getQueryDefinition(dataSetDesign, rowsToReturn);
		}
		if (dataSetDesign != null) {
			QueryDefinition defn = new QueryDefinition(null);
			defn.setDataSetName(dataSetDesign.getName());
			if (rowsToReturn > 0) {
				defn.setMaxRows(rowsToReturn);
			}

			for (int i = 0; i < bindingParams.length; i++) {
				ParamBindingHandle param = bindingParams[i];
				InputParameterBinding binding = new InputParameterBinding(param.getParamName(),
						new ScriptExpression(param.getExpression()));
				defn.addInputParamBinding(binding);
			}

			return defn;
		}
		return null;
	}

	/**
	 * 
	 * @param dataSetDesign
	 * @param rowsToReturn
	 * @return
	 */
	public static final QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign, int rowsToReturn) {
		if (dataSetDesign != null) {
			QueryDefinition defn = new QueryDefinition(null);
			defn.setDataSetName(dataSetDesign.getName());
			if (rowsToReturn > 0) {
				defn.setMaxRows(rowsToReturn);
			}
			List parameters = dataSetDesign.getParameters();
			Iterator iter = parameters.iterator();
			while (iter.hasNext()) {
				ParameterDefinition paramDefn = (ParameterDefinition) iter.next();
				if (paramDefn.isInputMode()) {
					if (paramDefn.getDefaultInputValue() != null) {
						InputParameterBinding binding = new InputParameterBinding(paramDefn.getName(),
								new ScriptExpression(paramDefn.getDefaultInputValue()));
						defn.addInputParamBinding(binding);
					}
				}
			}
			return defn;
		}
		return null;
	}

	/**
	 * Finds the data set by the given name. If not found, try to find the extended
	 * data set.
	 * 
	 * @param name the data set name
	 * @return the data set handle
	 */
	public static DataSetHandle findDataSet(String name) {
		return findDataSet(name, null);
	}

	/**
	 * Finds the data set by the given name. If not found, try to find the extended
	 * data set.
	 * 
	 * @param module the module handle
	 * @param name   the data set name
	 * @return the data set handle
	 */
	public static DataSetHandle findDataSet(String name, ModuleHandle module) {
		DataSetHandle dataSet = null;

		if (module != null) {
			dataSet = module.findDataSet(name);
		} else {
			dataSet = SessionHandleAdapter.getInstance().getModule().findDataSet(name);

		}

		if (dataSet != null) {
			return dataSet;
		}

		return findExtendedDataSet(name);
	}

	/**
	 * Finds the extended data set by the given name.
	 * 
	 * @param name the data set name
	 * @return the extended data set handle, or null if not found
	 */
	public static DataSetHandle findExtendedDataSet(String name) {
		IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();
		if (adapter != null) {
			ReportElementHandle extData = adapter.findExtendedDataByName(name);

			if (extData != null) {
				return adapter.getDataSet(extData);
			}
		}

		return null;
	}

	/**
	 * Gets names of the available data sets and extended data sets
	 * 
	 * @param module
	 * @return
	 */
	public static List<String> getAvailableDataSetNames(ModuleHandle module) {
		List<String> dataSets = new ArrayList<String>();
		if (module == null) {
			return dataSets;
		}

		for (Iterator iterator = module.getVisibleDataSets().iterator(); iterator.hasNext();) {
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next();
			dataSets.add(dataSetHandle.getName());
		}
		for (Iterator itr = new LinkedDataSetAdapter().getVisibleLinkedDataSets().iterator(); itr.hasNext();) {
			dataSets.add((String) itr.next());
		}

		return dataSets;
	}

	public static List<ColumnHintHandle> getColumnHints(DataSetHandle dataSet) {
		java.util.List<ColumnHintHandle> columnHints = new ArrayList<ColumnHintHandle>();
		if (dataSet != null) {
			PropertyHandle hintHandle = dataSet.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
			if (hintHandle != null) {
				Iterator iter = hintHandle.iterator();
				while (iter.hasNext()) {
					ColumnHintHandle columnHint = (ColumnHintHandle) iter.next();
					columnHints.add(columnHint);
				}
			}
		}
		return columnHints;
	}
}
