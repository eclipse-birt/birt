/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * 
 * Utility class to get meta data and data from data set
 * 
 */
public final class DataSetProvider {

	private static final String BIRT_SCRIPTLIB = "/birt/scriptlib"; //$NON-NLS-1$
	private static final String BIRT_CLASSES = "/birt/WEB-INF/classes/"; //$NON-NLS-1$
	private static final String VIEWER_NAMESPACE = "org.eclipse.birt.report.viewer"; //$NON-NLS-1$
	private static final String DERIVED_SEPERATOR = "::"; //$NON-NLS-1$

	private static DataSetProvider instance = null;

	// column hash table
	private Map<DataSetHandle, DataSetViewData[]> htColumns = new LinkedHashMap<DataSetHandle, DataSetViewData[]>(10,
			(float) 0.75, true) {

		private static final long serialVersionUID = 4685315474104939633L;

		/*
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		protected boolean removeEldestEntry(final Map.Entry eldest) {
			return size() > 10;
		}
	};

	private static Hashtable<String, IConfigurationElement> htDataSourceExtensions = new Hashtable<String, IConfigurationElement>(
			10);

	private boolean needToFocusOnOutput = true;

	/**
	 * @return
	 */
	private static DataSetProvider newInstance() {
		return new DataSetProvider();
	}

	/**
	 * 
	 * @return
	 */
	public static DataSetProvider getCurrentInstance() {
		if (instance == null)
			instance = newInstance();
		return instance;
	}

	/**
	 * get columns data by data set name
	 * 
	 * @param dataSetName
	 * @param refresh
	 * @return
	 * @throws BirtException
	 */
	public DataSetViewData[] getColumns(String dataSetName, boolean refresh) throws BirtException {
		ModuleHandle handle = Utility.getReportModuleHandle();
		DataSetHandle dataSet = handle.findDataSet(dataSetName);
		if (dataSet == null) {
			return new DataSetViewData[] {};
		}
		return getColumns(dataSet, refresh);
	}

	/**
	 * get column data by data set handle
	 * 
	 * @param dataSet
	 * @param refresh
	 * @return
	 * @throws BirtException
	 */
	public DataSetViewData[] getColumns(DataSetHandle dataSet, boolean refresh) throws BirtException {
		return getColumns(dataSet, refresh, true);
	}

	/**
	 * 
	 * @param dataSet
	 * @param refresh
	 * @param useColumnHints Only applicable if the list is refreshed.
	 * @return
	 * @throws BirtException
	 */
	public DataSetViewData[] getColumns(DataSetHandle dataSet, boolean refresh, boolean useColumnHints)
			throws BirtException {
		if (dataSet == null) {
			return new DataSetViewData[0];
		}
		DataSetViewData[] columns = null;
		DataRequestSession session = null;
		try {
			// Find the data set in the hashtable
			columns = htColumns.get(dataSet);

			// If there are not cached get them from the column hints
			if (columns == null || refresh) {
				DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
						dataSet.getModuleHandle());
				session = DataRequestSession.newSession(context);

				columns = this.populateAllOutputColumns(dataSet, session);
				htColumns.put(dataSet, columns);
			}
		} catch (BirtException e) {
			columns = new DataSetViewData[] {};
			// updateModel( dataSet, columns );
			htColumns.put(dataSet, columns);
			throw e;
		} finally {
			if (session != null) {
				session.shutdown();
			}
		}

		// If the columns array is still null
		// just initialize it to an empty array
		if (columns == null) {
			columns = new DataSetViewData[] {};
			// updateModel( dataSet, columns );
			htColumns.put(dataSet, columns);
		}
		return columns;
	}

	/**
	 * populate all output columns in viewer display. The output columns is
	 * retrieved from oda dataset handles's RESULT_SET_PROP and
	 * COMPUTED_COLUMNS_PROP.
	 * 
	 * @throws BirtException
	 */
	public DataSetViewData[] populateAllOutputColumns(DataSetHandle dataSetHandle, DataRequestSession session)
			throws BirtException {
		try {
			DataService.getInstance().registerSession(dataSetHandle, session);
			IResultMetaData metaData = session.getDataSetMetaData(dataSetHandle, false);
			if (metaData == null)
				return new DataSetViewData[0];
			DataSetViewData[] items = new DataSetViewData[metaData.getColumnCount()];

			for (int i = 0; i < metaData.getColumnCount(); i++) {
				items[i] = new DataSetViewData();
				items[i].setName(metaData.getColumnName(i + 1));
				items[i].setDataTypeName(DataAdapterUtil.adapterToModelDataType(metaData.getColumnType(i + 1)));
				items[i].setAlias(metaData.getColumnAlias(i + 1));
				items[i].setComputedColumn(metaData.isComputedColumn(i + 1));
				items[i].setPosition(i + 1);
				items[i].setDataType(metaData.getColumnType(i + 1));
				ColumnHintHandle hint = findColumnHint(dataSetHandle, items[i].getName());
				if (hint != null) {
					if (!items[i].isComputedColumn()) {
						items[i].setAnalysis(hint.getAnalysis());
						if (DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(hint.getAnalysis())) {
							items[i].setAnalysisColumn(hint.getAnalysisColumn());
						} else {
							items[i].setAnalysisColumn(null);
						}
					} else {
						items[i].setAnalysis(hint.getAnalysis());
						items[i].setAnalysisColumn(hint.getAnalysisColumn());
					}
					items[i].setDisplayName(hint.getDisplayName());
					items[i].setDisplayNameKey(hint.getDisplayNameKey());
					items[i].setACLExpression(hint.getACLExpression());
					items[i].setFormat(hint.getFormat());
					items[i].setDisplayLength(hint.getDisplayLength());
					items[i].setHeading(hint.getHeading());
					items[i].setHelpText(hint.getHelpText());
					items[i].setFormatValue(hint.getValueFormat());
					items[i].setHorizontalAlign(hint.getHorizontalAlign());
					items[i].setTextFormat(hint.getTextFormat());
					items[i].setDescription(hint.getDescription());
					items[i].setWordWrap(hint.wordWrap());
					items[i].setIndexColumn(hint.isIndexColumn());
					items[i].setRemoveDuplicateValues(hint.isCompressed());
					items[i].setAlias(hint.getAlias());
					items[i].setActionHandle(hint.getActionHandle());
				} else {
					if (items[i].isComputedColumn()) {
						items[i].setAnalysis(null);
						items[i].setAnalysisColumn(null);
					} else {
						items[i].setAnalysisColumn(null);
					}
				}
			}
			return items;
		} finally {
			DataService.getInstance().unRegisterSession(session);
		}
	}

	/**
	 * get Cached metadata
	 * 
	 * @throws BirtException
	 */
	public DataSetViewData[] populateAllCachedMetaData(DataSetHandle dataSetHandle, DataRequestSession session)
			throws BirtException {
		IResultMetaData metaData = session.getDataSetMetaData(dataSetHandle, true);

		if (metaData == null) {
			return new DataSetViewData[0];
		}

		DataSetViewData[] items = new DataSetViewData[metaData.getColumnCount()];

		for (int i = 0; i < metaData.getColumnCount(); i++) {
			items[i] = new DataSetViewData();
			items[i].setName(metaData.getColumnName(i + 1));
			items[i].setDataTypeName(DataAdapterUtil.adapterToModelDataType(metaData.getColumnType(i + 1)));
			items[i].setAlias(metaData.getColumnAlias(i + 1));
			items[i].setComputedColumn(metaData.isComputedColumn(i + 1));
			items[i].setPosition(i + 1);
			items[i].setDataType(metaData.getColumnType(i + 1));
			ColumnHintHandle hint = findColumnHint(dataSetHandle, items[i].getName());
			if (hint != null) {
				if (!items[i].isComputedColumn()) {
					items[i].setAnalysis(hint.getAnalysis());
					if (DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(items[i].getAnalysis())) {
						items[i].setAnalysisColumn(hint.getAnalysisColumn());
					} else {
						items[i].setAnalysisColumn(null);
					}
				} else {
					items[i].setAnalysis(hint.getAnalysis());
					items[i].setAnalysisColumn(hint.getAnalysisColumn());
				}
				items[i].setDisplayName(hint.getDisplayName());
				items[i].setDisplayNameKey(hint.getDisplayNameKey());
				items[i].setACLExpression(hint.getACLExpression());
				items[i].setFormat(hint.getFormat());
				items[i].setDisplayLength(hint.getDisplayLength());
				items[i].setHeading(hint.getHeading());
				items[i].setHelpText(hint.getHelpText());
				items[i].setHorizontalAlign(hint.getHorizontalAlign());
				items[i].setFormatValue(hint.getValueFormat());
				items[i].setTextFormat(hint.getTextFormat());
				items[i].setDescription(hint.getDescription());
				items[i].setWordWrap(hint.wordWrap());
				items[i].setIndexColumn(hint.isIndexColumn());
				items[i].setRemoveDuplicateValues(hint.isCompressed());
				items[i].setActionHandle(hint.getActionHandle());
			} else {
				if (items[i].isComputedColumn()) {
					items[i].setAnalysis(null);
					items[i].setAnalysisColumn(null);
				} else {
					items[i].setAnalysis(null);
					items[i].setAnalysisColumn(null);
				}
			}
		}
		return items;
	}

	/**
	 * update the columns of the DataSetHandle and put the new DataSetViewData[]
	 * into htColumns
	 * 
	 * @param dataSet
	 * @param dsItemModel
	 */
	public void updateColumnsOfDataSetHandle(DataSetHandle dataSet, DataSetViewData[] dsItemModel) {
		if (dataSet == null || dsItemModel == null || dsItemModel.length == 0)
			return;
		htColumns.put(dataSet, dsItemModel);
	}

	/**
	 * This function should be called very carefully. Presently it is only called in
	 * DataSetEditorDialog#performCancel.
	 * 
	 * @param dataSet
	 * @param itemModel
	 */
	public void setModelOfDataSetHandle(DataSetHandle dataSet, DataSetViewData[] dsItemModel) {
		if (dataSet == null || dsItemModel == null)
			return;

		updateModel(dataSet, dsItemModel);
		cleanUnusedResultSetColumn(dataSet, dsItemModel);
		cleanUnusedComputedColumn(dataSet, dsItemModel);
		htColumns.put(dataSet, dsItemModel);
	}

	private ColumnHintHandle findColumnHint(DataSetHandle handle, String columnName) {
		if (columnName == null || columnName.trim().length() == 0)
			return null;

		ColumnHintHandle hint = null;
		if (handle instanceof DerivedDataSetHandle) {
			String[] splits = columnName.split(DERIVED_SEPERATOR);
			List<DataSetHandle> inputDataSets = ((DerivedDataSetHandle) handle).getInputDataSets();
			for (int i = 0; i < inputDataSets.size(); i++) {
				hint = findColumnHint(inputDataSets.get(i), columnName);
				if (hint != null) {
					return hint;
				}
				if (splits.length > 1) {
					if (splits[0].equals(inputDataSets.get(i).getName())) {
						columnName = columnName
								.substring(columnName.indexOf(DERIVED_SEPERATOR) + DERIVED_SEPERATOR.length());

						return findColumnHint(inputDataSets.get(i), columnName);
					}
				}
			}
		}

		Iterator iter = handle.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator();
		while (iter.hasNext()) {
			hint = (ColumnHintHandle) iter.next();
			if (columnName.equals(hint.getColumnName())) {
				return hint;
			}
		}
		return null;
	}

	/**
	 * To rollback original datasetHandle, clean unused resultset columm
	 * 
	 * @param dataSetHandle
	 * @param dsItemModel
	 */
	private void cleanUnusedResultSetColumn(DataSetHandle dataSetHandle, DataSetViewData[] dsItemModel) {
		PropertyHandle handle = dataSetHandle.getPropertyHandle(DataSetHandle.RESULT_SET_PROP);
		if (handle != null && handle.getListValue() != null) {
			ArrayList list = handle.getListValue();
			int count = list.size();
			for (int n = count - 1; n >= 0; n--) {
				ResultSetColumn rsColumn = (ResultSetColumn) list.get(n);
				String columnName = (String) rsColumn.getColumnName();
				boolean found = false;

				for (int m = 0; m < dsItemModel.length; m++) {
					if (columnName.equals(dsItemModel[m].getName())) {
						found = true;
						break;
					}
				}

				if (!found) {
					try {
						// remove the item
						handle.removeItem(rsColumn);
					} catch (PropertyValueException e) {
					}
				}
			}
		}
	}

	/**
	 * To rollback original datasetHandle, clean unused computed columm
	 * 
	 * @param dataSetHandle
	 * @param dsItemModel
	 */
	private void cleanUnusedComputedColumn(DataSetHandle dataSetHandle, DataSetViewData[] dsItemModel) {
		PropertyHandle handle = dataSetHandle.getPropertyHandle(DataSetHandle.COMPUTED_COLUMNS_PROP);
		if (handle != null && handle.getListValue() != null) {
			ArrayList list = handle.getListValue();
			int count = list.size();
			for (int n = count - 1; n >= 0; n--) {
				ComputedColumn rsColumn = (ComputedColumn) list.get(n);
				String columnName = (String) rsColumn.getName();
				boolean found = false;

				for (int m = 0; m < dsItemModel.length; m++) {
					if (columnName.equals(dsItemModel[m].getName())) {
						found = true;
						break;
					}
				}

				if (!found) {
					try {
						// remove the item
						handle.removeItem(rsColumn);
					} catch (PropertyValueException e) {
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param dataSetDesign
	 * @param rowsToReturn
	 * @return
	 */
	private final QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign, int rowsToReturn) {
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
	 * @param dataSetDesign
	 * @param bindingParams
	 * @return
	 */
	public final QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign,
			ParamBindingHandle[] bindingParams) {
		return getQueryDefinition(dataSetDesign, bindingParams, -1);
	}

	/**
	 * @param dataSetDesign
	 * @param bindingParams
	 * @param i
	 * @return
	 */
	private QueryDefinition getQueryDefinition(IBaseDataSetDesign dataSetDesign, ParamBindingHandle[] bindingParams,
			int rowsToReturn) {
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
	 * @param ds
	 * @param columns
	 */
	public void updateModel(DataSetHandle ds, DataSetViewData[] columns) {
		// get the column hints
		PropertyHandle handle = ds.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		PropertyHandle resultSetColumnHandle = ds.getPropertyHandle(DataSetHandle.RESULT_SET_HINTS_PROP);

		Iterator iter = handle.iterator();
		if (iter != null) {
			while (iter.hasNext()) {
				ColumnHintHandle hint = (ColumnHintHandle) iter.next();
				// Find this column in the list of columns passed and update the

				for (int n = 0; n < columns.length; n++) {
					// If the column name is not present then get the column
					// name from
					// the result set column definition if any
					String columnName = columns[n].getName();
					if (resultSetColumnHandle != null && (columnName == null || columnName.trim().length() == 0)) {
						Iterator resultIter = resultSetColumnHandle.iterator();
						if (resultIter != null) {
							while (resultIter.hasNext()) {
								ResultSetColumnHandle column = (ResultSetColumnHandle) resultIter.next();
								if (column.getPosition().intValue() == n + 1) {
									columnName = column.getColumnName();
									break;
								}
							}
						}
						if (columnName == null) {
							columnName = ""; //$NON-NLS-1$
						}
						columns[n].setName(columnName);
					}
					if (columns[n].getName().equals(hint.getColumnName())) {
						if (hint.getDisplayNameKey() != null) {
							columns[n].setExternalizedName(hint.getExternalizedValue(
									org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_ID_MEMBER,
									org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_MEMBER,
									hint.getModule().getLocale()));
						}
						columns[n].setDisplayName(hint.getDisplayName());
						columns[n].setDisplayNameKey(hint.getDisplayNameKey());
						columns[n].setAlias(hint.getAlias());
						columns[n].setHelpText(hint.getHelpText());
						columns[n].setAnalysis(hint.getAnalysis());
						columns[n].setActionHandle(hint.getActionHandle());

						if (DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(columns[n].getAnalysis())) {
							columns[n].setAnalysisColumn(hint.getAnalysis());
						} else {
							columns[n].setAnalysis(null);
							columns[n].setAnalysisColumn(null);
						}
						columns[n].setAnalysisColumn(hint.getAnalysisColumn());
						columns[n].setACLExpression(hint.getACLExpression());
						columns[n].setIndexColumn(hint.isIndexColumn());
						columns[n].setRemoveDuplicateValues(hint.isCompressed());
						break;
					}
				}
			}
		}
	}

	/**
	 * Get cached data set item model. If none is cached, return null;
	 * 
	 * @param ds
	 * @param columns
	 */
	public DataSetViewData[] getCachedDataSetItemModel(DataSetHandle ds, boolean needToFocusOnOutput) {
		this.needToFocusOnOutput = needToFocusOnOutput;
		DataSetViewData[] result = this.htColumns.get(ds);
		if (result == null) {

			DataRequestSession session = null;
			try {
				DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
						ds.getModuleHandle());
				session = DataRequestSession.newSession(context);
				result = this.populateAllOutputColumns(ds, session);
				return result;
			} catch (BirtException e) {
				result = new DataSetViewData[0];
			} finally {
				if (session != null) {
					session.shutdown();
				}
			}
		}
		return result;
	}

	/**
	 * @param dataSetType
	 * @param dataSourceType
	 * @return
	 */
	public static IConfigurationElement findDataSetElement(String dataSetType, String dataSourceType) {
		// NOTE: multiple data source types can support the same data set type
		IConfigurationElement dataSourceElem = findDataSourceElement(dataSourceType);
		if (dataSourceElem != null) {
			// Find data set declared in the same extension
			IExtension ext = dataSourceElem.getDeclaringExtension();
			IConfigurationElement[] elements = ext.getConfigurationElements();
			for (int n = 0; n < elements.length; n++) {
				if (elements[n].getAttribute("id").equals(dataSetType)) //$NON-NLS-1$
				{
					return elements[n];
				}
			}
		}
		return null;
	}

	/**
	 * @param dataSourceType
	 * @return
	 */
	public static IConfigurationElement findDataSourceElement(String dataSourceType) {
		assert (dataSourceType != null);

		// Find it in the hashtable
		IConfigurationElement element = htDataSourceExtensions.get(dataSourceType);
		if (element == null) {
			IConfigurationElement[] elements = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("org.eclipse.birt.report.designer.ui.odadatasource"); //$NON-NLS-1$
			for (int n = 0; n < elements.length; n++) {
				if (elements[n].getAttribute("id").equals(dataSourceType)) //$NON-NLS-1$
				{
					element = elements[n];
					htDataSourceExtensions.put(dataSourceType, element);
					break;
				}
			}
			elements = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("org.eclipse.datatools.connectivity.oda.design.ui.dataSource"); //$NON-NLS-1$
			for (int n = 0; n < elements.length; n++) {
				if (elements[n].getAttribute("id").equals(dataSourceType)) //$NON-NLS-1$
				{
					element = elements[n];
					htDataSourceExtensions.put(dataSourceType, element);
					break;
				}
			}
		}
		return element;
	}

	/**
	 * @param dataSet
	 * @param useColumnHints
	 * @param useFilters
	 * @param session
	 * @return
	 * @throws BirtException
	 */
	private IBaseDataSetDesign getDataSetDesign(DataSetHandle dataSet, boolean useColumnHints, boolean useFilters,
			DataRequestSession session) throws BirtException {
		IBaseDataSetDesign dataSetDesign = session.getModelAdaptor().adaptDataSet(dataSet);

		if (!useColumnHints) {
			dataSetDesign.getResultSetHints().clear();
		}
		if (!useFilters) {
			dataSetDesign.getFilters().clear();
		}
		if (!(dataSet instanceof JointDataSetHandle)) {
			IBaseDataSourceDesign dataSourceDesign = session.getModelAdaptor().adaptDataSource(dataSet.getDataSource());
			session.defineDataSource(dataSourceDesign);

		}
		if (dataSet instanceof JointDataSetHandle) {
			defineSourceDataSets(session, dataSet, dataSetDesign);
		}
		session.defineDataSet(dataSetDesign);
		return dataSetDesign;
	}

	/**
	 * @param dataSet
	 * @param dataSetDesign
	 * @throws BirtException
	 */
	private void defineSourceDataSets(DataRequestSession session, DataSetHandle dataSet,
			IBaseDataSetDesign dataSetDesign) throws BirtException {
		List dataSets = dataSet.getModuleHandle().getAllDataSets();
		for (int i = 0; i < dataSets.size(); i++) {
			DataSetHandle dsHandle = (DataSetHandle) dataSets.get(i);
			if (dsHandle.getName() != null) {
				if (dsHandle.getName().equals(((IJointDataSetDesign) dataSetDesign).getLeftDataSetDesignName())
						|| dsHandle.getName()
								.equals(((IJointDataSetDesign) dataSetDesign).getRightDataSetDesignName())) {
					getDataSetDesign(dsHandle, true, true, session);
				}
			}
		}
	}

	/**
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	public Collection getParametersFromDataSet(DataSetHandle dataSet) throws BirtException {
		DataRequestSession session = null;
		try {
			DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
					dataSet.getModuleHandle());
			session = DataRequestSession.newSession(context);
			IBaseDataSetDesign dataSetDesign = getDataSetDesign(dataSet, true, true, session);

			QueryDefinition queryDefn = getQueryDefinition(dataSetDesign, -1);

			IPreparedQuery query = session.prepare(queryDefn, null);
			return query.getParameterMetaData();
		} finally {
			if (session != null) {
				session.shutdown();
			}
		}
	}

	/**
	 * @param parent
	 * @return
	 */
	public static ClassLoader getCustomScriptClassLoader(ClassLoader parent, ModuleHandle handle) {
		List<URL> urls = getClassPathURLs(handle == null ? null : handle.getFileName());

		loadResourceFolderScriptLibs(handle, urls);

		if (urls.size() == 0)
			return parent;

		return new URLClassLoader(urls.toArray(new URL[0]), parent);
	}

	private static void loadResourceFolderScriptLibs(ModuleHandle handle, List<URL> urls) {
		Iterator it = handle.scriptLibsIterator();
		while (it.hasNext()) {
			ScriptLibHandle libHandle = (ScriptLibHandle) it.next();
			URL url = handle.findResource(libHandle.getName(), IResourceLocator.LIBRARY);
			if (url != null)
				urls.add(url);
		}
	}

	private static List<URL> getClassPathURLs(String reportFilePath) {
		List<URL> urls = new ArrayList<URL>();
		urls.addAll(getDefaultViewerScriptLibURLs());
		urls.addAll(getWorkspaceProjectURLs(reportFilePath));
		return urls;
	}

	/**
	 * Return the URLs of ScriptLib jars.
	 * 
	 * @return
	 */
	private static List<URL> getDefaultViewerScriptLibURLs() {
		List<URL> urls = new ArrayList<URL>();
		try {
			Bundle bundle = Platform.getBundle(VIEWER_NAMESPACE);

			// Prepare ScriptLib location
			Enumeration bundleFile = bundle.getEntryPaths(BIRT_SCRIPTLIB);
			while (bundleFile.hasMoreElements()) {
				String o = bundleFile.nextElement().toString();
				if (o.endsWith(".jar"))
					urls.add(bundle.getResource(o));
			}
			URL classes = bundle.getEntry(BIRT_CLASSES);
			if (classes != null) {
				urls.add(classes);
			}
		} catch (Exception e) {

		}
		return urls;
	}

	/**
	 * Return the URLs of Workspace projects.
	 * 
	 * @return
	 */
	private static List<URL> getWorkspaceProjectURLs(String reportFilePath) {
		return DatasetClassPathHelper.getWorkspaceClassPath(reportFilePath);
	}

	/**
	 * clear cached metadata for specified dataSetHandle
	 * 
	 * @param dataSetHandle
	 */
	public void clear(DataSetHandle dataSetHandle) {
		if (dataSetHandle != null) {
			htColumns.remove(dataSetHandle);
		}
	}

	/**
	 * clear all cached metadata
	 */
	public void clearAll() {
		htColumns.clear();
	}
}
