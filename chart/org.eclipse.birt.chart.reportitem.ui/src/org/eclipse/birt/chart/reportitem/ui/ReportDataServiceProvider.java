/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator;
import org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.BaseGroupedQueryResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.ChartBaseQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartCubeQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.SharedCubeResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionSet;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider.BindingInfo;
import org.eclipse.birt.report.designer.ui.IReportClasspathResolver;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CrosstabQueryUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * Data service provider of chart builder for BIRT integration.
 */

public class ReportDataServiceProvider implements IDataServiceProvider {

	/**
	 * DataSetInfo
	 */
	public static class DataSetInfo extends BindingInfo {

		public DataSetInfo(int type, String value) {
			super(type, value);
		}

		public DataSetInfo(int type, String value, boolean isDataSet) {
			super(type, value, isDataSet);
		}

		public String getDisplayName() {
			return this.isDataSet() ? this.getBindingValue()
					: this.getBindingValue() + Messages.getString("ReportDataServiceProvider.Label.DataModel"); //$NON-NLS-1$
		}
	}

	protected ExtendedItemHandle itemHandle;

	protected ChartWizardContext context;

	/** The helper handle is used to do things for share binding case. */
	private final ShareBindingQueryHelper fShareBindingQueryHelper = new ShareBindingQueryHelper();

	static final String OPTION_NONE = Messages.getString("ReportDataServiceProvider.Option.None"); //$NON-NLS-1$

	ChartReportItemUIFactory uiFactory = ChartReportItemUIFactory.instance();

	protected DteAdapter dteAdapter = uiFactory.createDteAdapter();

	protected Object sessionLock = new Object();
	/**
	 * This flag indicates whether the error is found when fetching data. This is to
	 * help reduce invalid query.
	 */
	private boolean isErrorFound = false;
	private IProject project = null;

	// These fields are used to execute query for live preview.
	protected DataRequestSession session = null;
	protected ReportEngine engine = null;
	protected ChartDummyEngineTask engineTask = null;
	private Object dataSetReference = null;
	private Map<String, ReportItemHandle> referMap = new HashMap<String, ReportItemHandle>();

	protected ExpressionCodec exprCodec = null;

	protected ILogger logger = Logger.getLogger("com.actuate.birt.chart.reportitem.ui/trace"); //$NON-NLS-1$

	public ReportDataServiceProvider(ExtendedItemHandle itemHandle) {
		super();
		this.itemHandle = itemHandle;
		project = UIUtil.getCurrentProject();
		exprCodec = ChartReportItemHelper.instance().createExpressionCodec(itemHandle);
	}

	protected String[] getDesignWorkspaceClasspath() {
		IReportClasspathResolver provider = ReportPlugin.getDefault().getReportClasspathResolverService();
		if (provider != null) {
			String designPath = null;
			if (isLibraryHandle()) {
				designPath = ((LibraryHandle) itemHandle.getModuleHandle()).getFileName();
			} else {
				designPath = ((ReportDesignHandle) itemHandle.getModuleHandle()).getFileName();
			}
			return provider.resolveClasspath(designPath);
		}

		return null;
	}

	/**
	 * Initializes some instance handles for query execution.
	 * 
	 * @throws ChartException
	 */
	@SuppressWarnings("unchecked")
	public void initialize() throws ChartException {
		try {
			EngineConfig config = new EngineConfig();
			String[] paths = getDesignWorkspaceClasspath();
			if (paths != null && paths.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (String path : paths) {
					if (buffer.length() > 0) {
						buffer.append(';');
					}
					buffer.append(path);
				}
				if (buffer.length() != 0) {
					HashMap<Object, Object> appContext = config.getAppContext();
					appContext.put(EngineConstants.PROJECT_CLASSPATH_KEY, buffer.toString());
				}
			}
			engine = (ReportEngine) new ReportEngineFactory().createReportEngine(config);

			IReportRunnable rr = null;
			if (isReportDesignHandle()) {
				rr = new ReportEngineHelper(engine).openReportDesign((ReportDesignHandle) itemHandle.getModuleHandle());
			} else {
				rr = new ReportRunnable(engine, itemHandle.getModuleHandle());
			}

			engineTask = new ChartDummyEngineTask(engine, rr, itemHandle.getModuleHandle());

			session = engineTask.getDataSession();
			engineTask.run();
			dteAdapter.setExecutionContext(engineTask.getExecutionContext());
		} catch (BirtException e) {
			if (engine == null && session != null) {
				session.shutdown();
			}
			if (engineTask != null) {
				engineTask.close();
			}

			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
		}

	}

	/**
	 * Disposes instance handles.
	 */
	public void dispose() {
		if (session != null) {
			try {
				dteAdapter.unregisterSession(session);
			} catch (BirtException e) {
				logger.log(e);
			}
		}

		if (engineTask != null) {
			engineTask.close();
		} else if (session != null) {
			session.shutdown();
		}
	}

	public void setWizardContext(ChartWizardContext context) {
		this.context = context;
	}

	protected ModuleHandle getReportDesignHandle() {
		return itemHandle.getModuleHandle();
	}

	@SuppressWarnings("unchecked")
	protected DataSetInfo[] getAllDataSets() {
		List<DataSetHandle> list = getReportDesignHandle().getVisibleDataSets();
		DataSetInfo[] dataInfos = new DataSetInfo[list.size()];
		for (int i = 0; i < list.size(); i++) {
			dataInfos[i] = new DataSetInfo(ReportItemHandle.DATABINDING_TYPE_DATA, list.get(i).getQualifiedName(),
					true);
		}
		return dataInfos;
	}

	@SuppressWarnings("unchecked")
	protected String[] getAllDataCubes() {
		List<CubeHandle> list = getReportDesignHandle().getVisibleCubes();
		String[] names = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			names[i] = list.get(i).getQualifiedName();
		}
		return names;
	}

	/**
	 * Gets data cube from chart itself
	 * 
	 * @return data cube name
	 */
	public String getDataCube() {
		CubeHandle cube = itemHandle.getCube();
		if (cube == null) {
			return null;
		}
		return cube.getQualifiedName();
	}

	/**
	 * Gets data cube from chart container
	 * 
	 * @return data cube name
	 */
	protected String getInheritedCube() {
		CubeHandle cube = null;
		DesignElementHandle container = itemHandle.getContainer();
		while (container != null) {
			if (container instanceof ReportItemHandle) {
				cube = ((ReportItemHandle) container).getCube();
				if (cube != null) {
					break;
				}
			}
			container = container.getContainer();
		}
		if (cube == null) {
			return null;
		}
		if (getBindingCubeHandle() == null) {
			return null;
		}

		return cube.getQualifiedName();
	}

	public void setDataCube(String cubeName) {
		try {
			// Clean references if it's set
			boolean isPreviousDataBindingReference = false;
			if (itemHandle.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				isPreviousDataBindingReference = true;
				itemHandle.setDataBindingReference(null);
			}
			itemHandle.setDataSet(null);

			if (cubeName == null) {
				itemHandle.setCube(null);
				// Clear parameters and filters, binding if dataset changed
				clearBindings();
			} else {
				if (!cubeName.equals(getDataCube()) || isPreviousDataBindingReference) {
					CubeHandle cubeHandle = getReportDesignHandle().findCube(cubeName);
					itemHandle.setCube(cubeHandle);
					// Clear parameters and filters, binding if dataset changed
					clearBindings();
					generateBindings(ChartXTabUIUtil.generateComputedColumns(itemHandle, cubeHandle));
				}
			}
			ChartWizard.removeException(ChartWizard.RepDSProvider_Cube_ID);
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.RepDSProvider_Cube_ID, e.getLocalizedMessage());
		}
	}

	public final String[] getPreviewHeader() {
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings(itemHandle);
		List<String> list = new ArrayList<String>();
		boolean bInheritColumnsOnly = isInheritColumnsOnly();
		while (iterator.hasNext()) {
			ComputedColumnHandle binding = iterator.next();
			// Remove the aggregation bindings if "inherit columns" used.
			if (bInheritColumnsOnly && binding.getAggregateFunction() != null) {
				continue;
			}
			list.add((binding).getName());
		}
		return list.toArray(new String[list.size()]);
	}

	public final List<Object[]> getPreviewData() throws ChartException {
		synchronized (sessionLock) {
			if (engineTask != null) {
				try {
					engineTask.run();
				} catch (EngineException e) {
					throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
				}
			}

			List<Object[]> values = null;
			if (isSharedBinding() || isInheritColumnsGroups()) {
				values = fShareBindingQueryHelper.getPreviewRowData(getPreviewHeadersInfo(), -1, true);
			} else {
				values = getPreviewRowData(getPreviewHeader(false), -1, true);
			}

			dataSetReference = getBindingDataSetHandle();
			return values;
		}
	}

	private void removeIndirectRefOfAggregates(List<ComputedColumnHandle> list,
			Map<String, ComputedColumnHandle> bindingMap) throws BirtException {
		for (Iterator<ComputedColumnHandle> iter = list.iterator(); iter.hasNext();) {
			ComputedColumnHandle cch = iter.next();
			if (!isCommonBinding(cch, bindingMap)) {
				iter.remove();
				continue;
			}
		}
	}

	/**
	 * Checks if specified column binding is a common binding,excluding aggregation
	 * binding.
	 * 
	 * @param cch
	 * @param bindingMap
	 * @return
	 * @throws BirtException
	 */
	@SuppressWarnings("unchecked")
	private boolean isCommonBinding(ComputedColumnHandle cch, Map<String, ComputedColumnHandle> bindingMap)
			throws BirtException {
		ScriptExpression se = ChartReportItemUtil.newExpression(session.getModelAdaptor(), cch);
		List<IColumnBinding> bindings = ExpressionUtil.extractColumnExpressions(se.getText(),
				ExpressionUtil.ROW_INDICATOR);
		for (int i = 0; i < bindings.size(); i++) {
			ComputedColumnHandle refCch = bindingMap.get(bindings.get(i).getResultSetColumnName());
			if (refCch.getAggregateFunction() != null) {
				return false;
			} else if (refCch.getExpression() != null && !isCommonBinding(refCch, bindingMap)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns columns header info.
	 * 
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	public final ColumnBindingInfo[] getPreviewHeadersInfo() throws ChartException {
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings(itemHandle);
		if (iterator == null) {
			return new ColumnBindingInfo[] {};
		}
		Map<String, ComputedColumnHandle> bindingMap = new HashMap<String, ComputedColumnHandle>();
		List<ComputedColumnHandle> columnList = new ArrayList<ComputedColumnHandle>();
		boolean bInheritColumnsOnly = isInheritColumnsOnly();
		while (iterator.hasNext()) {
			ComputedColumnHandle binding = iterator.next();
			bindingMap.put(binding.getName(), binding);
			// Remove the aggregation bindings if "inherit columns" used.
			if (bInheritColumnsOnly && binding.getAggregateFunction() != null) {
				continue;
			}

			columnList.add(binding);
		}

		// Remove indirect reference of aggregation bindings.
		try {
			if (bInheritColumnsOnly) {
				removeIndirectRefOfAggregates(columnList, bindingMap);
			}
		} catch (BirtException e) {
			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
		}

		if (columnList.size() == 0) {
			return new ColumnBindingInfo[] {};
		}

		ColumnBindingInfo[] columnHeaders = null;
		if (isTableSharedBinding() || isInheritColumnsGroups()) {
			columnHeaders = fShareBindingQueryHelper.getPreviewHeadersInfo(columnList);
		} else {
			columnHeaders = new ColumnBindingInfo[columnList.size()];
			for (int i = 0; i < columnHeaders.length; i++) {
				ComputedColumnHandle cch = columnList.get(i);
				columnHeaders[i] = new ColumnBindingInfo(cch.getName(),
						ExpressionUtil.createJSRowExpression(cch.getName()), null, null, cch);
			}
		}

		// Sort columns.
		List<ColumnBindingInfo> columnsSet = new ArrayList<ColumnBindingInfo>();
		for (int i = columnHeaders.length - 1; i >= 0; i--)
			columnsSet.add(columnHeaders[i]);
		Collections.sort(columnsSet, new ColumnNameComprator());
		columnHeaders = columnsSet.toArray(new ColumnBindingInfo[] {});
		return columnHeaders;
	}

	static class ColumnNameComprator implements Comparator<ColumnBindingInfo>, Serializable {

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		// use JDK String's compare method to map the same sorting logic as
		// column binding dialog(270079)
		// private com.ibm.icu.text.Collator collator =
		// com.ibm.icu.text.Collator.getInstance( );
		public int compare(ColumnBindingInfo src, ColumnBindingInfo target) {
			return src.getName().compareTo(target.getName());
		}
	}

	private String[] getPreviewHeader(boolean isExpression) throws ChartException {
		ColumnBindingInfo[] cbis = getPreviewHeadersInfo();
		String[] exps = new String[cbis.length];
		int i = 0;
		for (ColumnBindingInfo cbi : cbis) {
			if (isExpression) {
				exps[i] = ExpressionUtil.createJSRowExpression(cbi.getName());
			} else {
				exps[i] = cbi.getName();
			}
			i++;
		}

		return exps;
	}

	protected final List<Object[]> getPreviewRowData(String[] bindingNames, int rowCount, boolean isStringType)
			throws ChartException {
		List<Object[]> dataList = null;

		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader parentLoader = oldContextLoader;
		if (parentLoader == null)
			parentLoader = this.getClass().getClassLoader();
		ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader(parentLoader,
				itemHandle.getModuleHandle());
		Thread.currentThread().setContextClassLoader(newContextLoader);

		try {
			DataSetHandle datasetHandle = getDataSetFromHandle();
			QueryDefinition queryDefn = new QueryDefinition();
			queryDefn.setMaxRows(getMaxRow());
			queryDefn.setDataSetName(datasetHandle.getQualifiedName());

			setQueryDefinitionWithDataSet(null, itemHandle, queryDefn);

			IQueryResults actualResultSet = executeDataSetQuery(queryDefn);
			if (actualResultSet != null) {
				IResultIterator iter = actualResultSet.getResultIterator();
				dataList = iterateDataSetResults(iter, bindingNames, isStringType);
				actualResultSet.close();
			}
		} catch (BirtException e) {
			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
		} finally {
			// Restore old thread context class loader
			Thread.currentThread().setContextClassLoader(oldContextLoader);
		}
		return (dataList == null) ? new ArrayList<Object[]>() : dataList;
	}

	private List<Object[]> iterateDataSetResults(IResultIterator iter, String[] bindingNames, boolean isStringType)
			throws BirtException {
		List<Object[]> dataList = new ArrayList<Object[]>();

		String[] expressions = bindingNames;
		int columnCount = expressions.length;
		while (iter.next()) {
			if (isStringType) {
				String[] record = new String[columnCount];
				for (int n = 0; n < columnCount; n++) {
					// Bugzilla#190229, to get string with localized
					// format
					record[n] = valueAsString(iter.getValue(expressions[n]));
				}
				dataList.add(record);
			} else {
				Object[] record = new Object[columnCount];
				for (int n = 0; n < columnCount; n++) {
					record[n] = iter.getValue(expressions[n]);
				}
				dataList.add(record);
			}
		}
		return dataList;
	}

	/**
	 * @param source
	 * @return
	 * @throws NumberFormatException
	 * @throws BirtException
	 */
	private String valueAsString(Object source) throws NumberFormatException, BirtException {
		if (source == null)
			return null;
		if (source instanceof Number) {
			ULocale locale = ULocale.getDefault();
			String value = source.toString();
			int index = value.indexOf('E');
			// It means the value is very larger, using E marked.
			if (index >= 0) {
				String returnValue = DataTypeUtil.toString(Double.valueOf(value.substring(0, index)), locale) + "E"; //$NON-NLS-1$
				String exponent = value.substring(index + 1);
				if (exponent.matches("[\\+-]+[1-9]{1}[0-9]*")) //$NON-NLS-1$
				{
					returnValue += exponent.substring(0, 1)
							+ DataTypeUtil.toString(Integer.valueOf(exponent.substring(1)), locale);
				} else {
					returnValue += DataTypeUtil.toString(Integer.valueOf(exponent), locale);
				}
				return returnValue;
			}
		}
		return DataTypeUtil.toString(source);
	}

	protected boolean isReportDesignHandle() {
		return itemHandle.getModuleHandle() instanceof ReportDesignHandle;
	}

	protected boolean isLibraryHandle() {
		return itemHandle.getModuleHandle() instanceof LibraryHandle;
	}

	/**
	 * Add group definitions into query definition.
	 * 
	 * @param queryDefn        query definition.
	 * @param reportItemHandle the item handle contains groups.
	 */
	@SuppressWarnings({ "unchecked" })
	private void handleGroup(QueryDefinition queryDefn, ExtendedItemHandle reportItemHandle,
			IModelAdapter modelAdapter) {
		ReportItemHandle handle = ChartReportItemUtil.getBindingHolder(reportItemHandle);
		if (handle instanceof ListingHandle) {
			SlotHandle groups = ((ListingHandle) handle).getGroups();
			for (Iterator<DesignElementHandle> iter = groups.iterator(); iter.hasNext();) {
				ChartBaseQueryHelper.handleGroup((GroupHandle) iter.next(), queryDefn, modelAdapter);
			}
		}
	}

	/**
	 * Check if current parameters is explicit value in chart builder, otherwise
	 * default value of parameters will be used to get preview data.
	 * 
	 * @param dataSetHandle current dataset handle.
	 * @param queryDefn     query definition.
	 * @return query definition.
	 */
	@SuppressWarnings("unchecked")
	private QueryDefinition resetParametersForDataPreview(DataSetHandle dataSetHandle, QueryDefinition queryDefn) {
		// 1. Get parameters description from dataset.
		Map<String, DataSetParameterHandle> dsphMap = new LinkedHashMap<String, DataSetParameterHandle>();
		if (dataSetHandle != null) {
			Iterator<DataSetParameterHandle> iterParams = dataSetHandle.parametersIterator();
			while (iterParams.hasNext()) {
				DataSetParameterHandle dsph = iterParams.next();
				dsphMap.put(dsph.getName(), dsph);
			}
		}

		// 2. Get parameters setting from current handle.
		Iterator<ParamBindingHandle> iterParams = itemHandle.getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP)
				.iterator();
		Map<String, ParamBindingHandle> pbhMap = new LinkedHashMap<String, ParamBindingHandle>();
		while (iterParams.hasNext()) {
			ParamBindingHandle paramBindingHandle = iterParams.next();
			pbhMap.put(paramBindingHandle.getParamName(), paramBindingHandle);
		}

		// 3. Check and reset parameters expression.
		Object[] names = pbhMap.keySet().toArray();
		for (int i = 0; i < names.length; i++) {
			DataSetParameterHandle dsph = dsphMap.get(names[i]);
			ScriptExpression se = null;
			String expr = pbhMap.get(names[i]).getExpression();
			// The parameters must be a explicit value, the 'row[]' and
			// 'row.' expressions only be converted to a explicit value
			// under runtime case. So use default value of parameter to get
			// data in Chart Builder.
			if (dsph.getDefaultValue() != null
					&& (expr == null || expr.indexOf("row[") >= 0 || expr.indexOf("row.") >= 0)) //$NON-NLS-1$ //$NON-NLS-2$
			{
				se = new ScriptExpression(dsph.getDefaultValue());
			} else {
				se = new ScriptExpression(expr);
			}

			InputParameterBinding ipb = new InputParameterBinding((String) names[i], se);
			queryDefn.addInputParamBinding(ipb);
		}

		return queryDefn;
	}

	/**
	 * Gets data set from chart itself
	 * 
	 * @return data set name
	 */
	public String getDataSet() {
		if (itemHandle.getDataSet() == null) {
			return null;
		}
		return itemHandle.getDataSet().getQualifiedName();
	}

	public DataSetInfo getDataSetInfo() {
		if (itemHandle.getDataSet() == null) {
			return null;
		}
		return new DataSetInfo(ReportItemHandle.DATABINDING_TYPE_DATA, itemHandle.getDataSet().getQualifiedName(),
				true);
	}

	/**
	 * Checks if only inheritance allowed.
	 * 
	 */
	boolean isInheritanceOnly() {
		if (isInXTabCell() || isInMultiView() || isInXTabMeasureCell()) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether an inherited data cube is used.
	 * 
	 * @return
	 * @since 2.5.3
	 */
	protected boolean isInheritCube() {
		return getDataSet() == null && getDataCube() == null && checkState(IDataServiceProvider.INHERIT_CUBE);
	}

	/**
	 * Check if chart is in multiple view.
	 * 
	 * @return
	 * @since 2.3
	 */
	protected boolean isInMultiView() {
		return itemHandle.getContainer() instanceof MultiViewsHandle;
	}

	/**
	 * Gets data set from chart container
	 * 
	 * @return data set name
	 */
	@SuppressWarnings("unchecked")
	String getInheritedDataSet() {
		List<DataSetHandle> list = DEUtil.getDataSetList(itemHandle.getContainer());
		if (list.size() > 0) {
			if (getBindingDataSetHandle() == null) {
				return null;
			}

			return list.get(0).getQualifiedName();
		}
		return null;
	}

	public void setDataSet(DataSetInfo dataSetInfo) {
		try {
			// Clean references if it's set
			boolean isPreviousDataBindingReference = false;
			if (itemHandle.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				isPreviousDataBindingReference = true;
				itemHandle.setDataBindingReference(null);
			}

			itemHandle.setCube(null);

			if (dataSetInfo == null) {

				if (getDataSet() != null) {
					// Clean old bindings and use container's binding
					clearBindings();
				}

				itemHandle.setDataSet(null);
			} else {
				DataSetHandle dataset = getReportDesignHandle().findDataSet(dataSetInfo.getBindingValue());
				if (isPreviousDataBindingReference || itemHandle.getDataSet() != dataset) {
					itemHandle.setDataSet(dataset);

					// Clean old bindings and add new bindings
					clearBindings();
					generateBindings(generateComputedColumns(dataset));

					enableCategoryGroup();
				}
			}
			ChartWizard.removeException(ChartWizard.RepDSProvider_Set_ID);
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.RepDSProvider_Set_ID, e.getLocalizedMessage());
		}
	}

	protected void enableCategoryGroup() {
		// enable default category grouping except Gantt and Stock
		if (!ChartUIConstants.TYPE_GANTT.equals(context.getModel().getType())
				&& !ChartUIConstants.TYPE_STOCK.equals(context.getModel().getType())) {
			List<SeriesDefinition> sds = ChartUIUtil.getBaseSeriesDefinitions(context.getModel());
			if (sds != null && sds.size() > 0) {
				SeriesDefinition base = sds.get(0);
				if (base.getGrouping() == null) {
					base.setGrouping(SeriesGroupingImpl.create());
				}
				base.getGrouping().setEnabled(true);
			}
		}
	}

	protected void clearBindings() throws SemanticException {
		clearProperty(itemHandle.getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP));
		clearProperty(itemHandle.getPropertyHandle(ExtendedItemHandle.FILTER_PROP));
		clearProperty(itemHandle.getColumnBindings());
	}

	private void clearProperty(PropertyHandle property) throws SemanticException {
		if (property != null) {
			property.clearValue();
		}
	}

	private Iterator<?> getPropertyIterator(PropertyHandle property) {
		if (property != null) {
			return property.iterator();
		}
		return null;
	}

	protected void generateBindings(List<ComputedColumn> columnList) throws SemanticException {
		if (columnList.size() > 0) {
			for (Iterator<ComputedColumn> iter = columnList.iterator(); iter.hasNext();) {
				DEUtil.addColumn(itemHandle, iter.next(), false);
			}
		}
	}

	/**
	 * Generate computed columns for the given report item with the closest data set
	 * available.
	 * 
	 * @param dataSetHandle Data Set. No aggregation created.
	 * 
	 * @return true if succeed,or fail if no column generated.
	 * @see DataUtil#generateComputedColumns(ReportItemHandle)
	 * 
	 */
	protected List<ComputedColumn> generateComputedColumns(DataSetHandle dataSetHandle) throws SemanticException {
		return ChartReportItemUIUtil.generateComputedColumns(itemHandle, dataSetHandle);
	}

	/**
	 * Gets dataset from ReportItemHandle at first. If null, get dataset from its
	 * container.
	 * 
	 * @return direct dataset
	 */
	@SuppressWarnings("unchecked")
	protected DataSetHandle getDataSetFromHandle() {
		DataSetHandle handle = ChartReportItemHelper.instance().getBindingDataSetHandle(itemHandle);
		if (handle != null) {
			return handle;
		}
		// TODO HENG 2013.03.14
//		else if ( itemHandle.getDataBindingReference( ) != null
//				&& itemHandle.getDataBindingReference( ).getDataSet( ) != null )
//		{
//			return itemHandle.getDataBindingReference( ).getDataSet( );
//		}
//		else if ( itemHandle.getContainer( ) instanceof MultiViewsHandle )
//		{
//			DesignElementHandle handle = ((MultiViewsHandle)itemHandle.getContainer( )).getContainer( );
//			if ( handle instanceof TableHandle && ((TableHandle)handle).getDataSet( ) != null )
//			{
//				return ((TableHandle)handle).getDataSet( );
//			}
//		}
//		// End of TODO
		List<DataSetHandle> datasetList = DEUtil.getDataSetList(itemHandle.getContainer());
		if (datasetList.size() > 0) {
			return datasetList.get(0);
		}
		return null;
	}

	/**
	 * The method is designed for overriding purpose. Subclasses can do some
	 * additional processing on the QueryDefinition here (e.g. add sorts or
	 * filters), before that it is going to be executed.
	 * 
	 * @param queryDefn
	 */
	protected void processQueryDefinition(QueryDefinition queryDefn) {
		// nothing to do here.
	}

	@SuppressWarnings("unchecked")
	private StyleHandle[] getAllStyleHandles() {
		List<StyleHandle> sLst = getReportDesignHandle().getAllStyles();
		StyleHandle[] list = sLst.toArray(new StyleHandle[sLst.size()]);
		Arrays.sort(list, new Comparator<StyleHandle>() {

			Collator collator = Collator.getInstance(ULocale.getDefault());

			public int compare(StyleHandle s1, StyleHandle s2) {
				return collator.compare(s1.getDisplayLabel(), s2.getDisplayLabel());
			}

		});
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	public String[] getAllStyles() {
		StyleHandle[] handles = getAllStyleHandles();
		String[] names = new String[handles.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = handles[i].getQualifiedName();
		}

		// Filter predefined styles to make its logic same with report design
		// side.
		names = filterPreStyles(names);
		return names;
	}

	/**
	 * Filter predefined styles.
	 * 
	 * @param items all available styles
	 * @return filtered styles.
	 */
	private String[] filterPreStyles(String items[]) {
		String[] newItems = items;
		if (items == null) {
			newItems = new String[] {};
		}

		List<IPredefinedStyle> preStyles = new DesignEngine(new DesignConfig()).getMetaData().getPredefinedStyles();
		List<String> preStyleNames = new ArrayList<String>();

		for (int i = 0; i < preStyles.size(); i++) {
			preStyleNames.add(preStyles.get(i).getName());
		}

		List<String> sytleNames = new ArrayList<String>();
		for (int i = 0; i < newItems.length; i++) {
			if (preStyleNames.indexOf(newItems[i]) == -1) {
				sytleNames.add(newItems[i]);
			}
		}

		return sytleNames.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#
	 * getAllStyleDisplayNames()
	 */
	public String[] getAllStyleDisplayNames() {
		List<String> styles = Arrays.asList(getAllStyles());
		StyleHandle[] handles = getAllStyleHandles();
		String[] names = new String[styles.size()];
		for (int i = 0, j = 0; i < handles.length; i++) {
			// Remove predefined styles to make its logic same with report
			// design side.
			if (styles.contains(handles[i].getQualifiedName())) {
				names[j++] = handles[i].getDisplayLabel();
			}
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentStyle ()
	 */
	public String getCurrentStyle() {
		if (itemHandle.getStyle() == null) {
			return null;
		}
		return itemHandle.getStyle().getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setStyle(java
	 * .lang.String)
	 */
	public void setStyle(String styleName) {
		try {
			if (styleName == null) {
				itemHandle.setStyle(null);
			} else {
				itemHandle.setStyle(getStyle(styleName));
			}
			ChartWizard.removeException(ChartWizard.RepDSProvider_Style_ID);
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.RepDSProvider_Style_ID, e.getLocalizedMessage());
		}
	}

	private SharedStyleHandle getStyle(String styleName) {
		return getReportDesignHandle().findStyle(styleName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataForColumns(java.lang.String[],
	 *      int, boolean)
	 * @deprecated since BIRT 2.3, the method is not used for chart live preview,
	 *             use
	 *             {@link #prepareRowExpressionEvaluator(Chart, List, int, boolean)}
	 *             instead.
	 * 
	 */
	@SuppressWarnings("dep-ann")
	public final Object[] getDataForColumns(String[] sExpressions, int iMaxRecords, boolean byRow)
			throws ChartException {
		return null;
	}

	protected int getMaxRow() {
		return PreferenceFactory.getInstance().getPreferences(ChartReportItemUIActivator.getDefault(), project)
				.getInt(ChartReportItemUIActivator.PREFERENCE_MAX_ROW);
	}

	public boolean isLivePreviewEnabled() {
		return !isErrorFound
				&& PreferenceFactory.getInstance().getPreferences(ChartReportItemUIActivator.getDefault(), project)
						.getBoolean(ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE)
				&& ChartReportItemUtil.getBindingHolder(itemHandle) != null;
	}

	boolean isInvokingSupported() {
		// If report item reference is set, all operations, including filters,
		// parameter bindings and column bindings are not supported
		if (isSharedBinding() || isInMultiView()) {
			return false;
		}
		ReportItemHandle container = DEUtil.getBindingHolder(itemHandle);
		if (container != null) {
			// To check container's report item reference
			return container.getDataBindingReference() == null;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataType
	 * (java.lang.String)
	 */
	public DataType getDataType(String expression) {
		return ChartItemUtil.getExpressionDataType(expression, itemHandle);
	}

	@SuppressWarnings("unchecked")
	protected String[] getAllReportItemReferences() {
		List<ReportItemHandle> availableList = itemHandle.getAvailableDataBindingReferenceList();
		List<ReportItemHandle> referenceList = new ArrayList<ReportItemHandle>();
		for (ReportItemHandle handle : availableList) {
			if (handle instanceof ExtendedItemHandle) {
				String extensionName = ((ExtendedItemHandle) handle).getExtensionName();
				if (!isAvailableExtensionToReferenceDataBinding(extensionName)) {
					continue;
				}
			}
			referenceList.add(handle);
		}
		return getAllReportItemReferences(referenceList);
	}

	/**
	 * Returns if reference binding can accept this type of extended item
	 * 
	 * @param extensionName extension name of item
	 * @return true means reference binding can accept this type of extended item
	 * @since 3.7
	 */
	protected boolean isAvailableExtensionToReferenceDataBinding(String extensionName) {
		return ChartReportItemUtil.isAvailableExtensionToReferenceDataBinding(extensionName);
	}

	private String[] getAllReportItemReferences(List<ReportItemHandle> referenceList) {
		referMap.clear();
		if (referenceList.isEmpty()) {
			return new String[0];
		}
		String[] references = new String[referenceList.size()];
		int i = 0;
		for (ReportItemHandle item : referenceList) {
			if (item.getName() != null) {
				references[i] = item.getQualifiedName();
				referMap.put(references[i], item);
				i++;
			}
		}
		int tmp = i;
		Arrays.sort(references, 0, tmp);
		for (ReportItemHandle item : referenceList) {
			if (item.getName() == null) {
				references[i++] = item.getElement().getDefn().getDisplayName() + " (ID " //$NON-NLS-1$
						+ item.getID() + ") - " //$NON-NLS-1$
						+ org.eclipse.birt.report.designer.nls.Messages.getString("BindingPage.ReportItem.NoName"); //$NON-NLS-1$
			}
		}
		Arrays.sort(references, tmp, referenceList.size());
		return references;
	}

	protected boolean isNoNameItem(String name) {
		ReportItemHandle item = referMap.get(name);
		return item == null || item.getName() == null;
	}

	String getReportItemReference() {
		ReportItemHandle ref = itemHandle.getDataBindingReference();
		if (ref == null) {
			return null;
		}
		return ref.getQualifiedName();
	}

	void setReportItemReference(String referenceName) {
		try {

			if (referenceName == null) {
				itemHandle.setDataBindingReference(null);
				// Select the corresponding data set, no need to reset bindings
			} else {
				itemHandle.setDataSet(null);
				itemHandle.setCube(null);

				if (!referenceName.equals(getReportItemReference())) {
					// Change reference and reset all bindings
					itemHandle.setDataBindingReference(
							(ReportItemHandle) getReportDesignHandle().findElement(referenceName));
					// //Model will reset bindings
					// clearBindings( );
					// generateBindings( );
				}
			}
			ChartWizard.removeException(ChartWizard.RepDSProvider_Ref_ID);
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.RepDSProvider_Ref_ID, e.getLocalizedMessage());
		}
	}

	/**
	 * Sets row limit to specified dte's session.
	 * 
	 * @param session
	 * @param rowLimit
	 * @param isCubeMode
	 */
	protected void setRowLimit(DataRequestSession session, int rowLimit, boolean isCubeMode) {
		// Bugzilla #210225.
		// If filter is set on report item handle of chart, here should not use
		// data cache mode and get all valid data firstly, then set row limit on
		// query(QueryDefinition.setMaxRows) to get required rows.
		PropertyHandle filterProperty = itemHandle.getPropertyHandle(IExtendedItemModel.FILTER_PROP);
		if (filterProperty == null || filterProperty.getListValue() == null
				|| filterProperty.getListValue().size() == 0) {
			dteAdapter.setRowLimit(session, rowLimit, isCubeMode);
		} else {
			// Needs to clear previous settings if filter is set.
			dteAdapter.setRowLimit(session, -1, isCubeMode);
		}
	}

	/**
	 * Check it should set cube into query session.
	 * 
	 * @param cube
	 * @return
	 */
	protected boolean needDefineCube(CubeHandle cube) {
		return dataSetReference != cube;
	}

	private boolean needDefineDataSet(DataSetHandle dataSetHandle) {
		return dataSetReference != dataSetHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#
	 * prepareRowExpressionEvaluator(org.eclipse.birt.chart.model.Chart,
	 * java.lang.String[], int, boolean)
	 */
	public IDataRowExpressionEvaluator prepareRowExpressionEvaluator(Chart cm, List<String> columnExpression,
			int rowCount, boolean isStringType) throws ChartException {
		synchronized (sessionLock) {
			if (engineTask != null) {
				try {
					engineTask.run();
				} catch (EngineException e) {
					throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
				}
			}

			// Set thread context class loader so Rhino can find POJOs in
			// workspace
			// projects
			ClassLoader oldContextLoader = Thread.currentThread().getContextClassLoader();
			ClassLoader parentLoader = oldContextLoader;
			if (parentLoader == null)
				parentLoader = this.getClass().getClassLoader();
			ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader(parentLoader,
					itemHandle.getModuleHandle());
			Thread.currentThread().setContextClassLoader(newContextLoader);

			IDataRowExpressionEvaluator evaluator = null;

			try {
				CubeHandle cube = getBindingCubeHandle();
				if (cube != null) {
					// Create evaluator for data cube, even if in multiple view
					evaluator = createCubeEvaluator(cube, cm, columnExpression);
					dataSetReference = cube;
				} else {
					// Create evaluator for data set
					if (isSharedBinding() && !ChartReportItemUtil.isOldChartUsingInternalGroup(itemHandle, cm)
							|| isInheritColumnsGroups()) {
						if (isSharingChart(true)) {
							evaluator = createBaseEvaluator(
									(ExtendedItemHandle) ChartReportItemUtil.getReportItemReference(itemHandle), cm,
									columnExpression);
						} else {
							evaluator = fShareBindingQueryHelper.createShareBindingEvaluator(cm, columnExpression);
						}
					} else {
						evaluator = createBaseEvaluator(itemHandle, cm, columnExpression);

					}

					dataSetReference = getBindingDataSetHandle();
				}
				return evaluator;
			} catch (BirtException e) {
				logger.log(e);
				throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
			} catch (RuntimeException e) {
				throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
			} finally {
				// Restore old thread context class loader
				Thread.currentThread().setContextClassLoader(oldContextLoader);

			}
		}
	}

	/**
	 * Create base evaluator for chart using data set.
	 * 
	 * @param handle
	 * @param cm
	 * @param columnExpression
	 * @return
	 * @throws ChartException
	 */
	protected IDataRowExpressionEvaluator createBaseEvaluator(ExtendedItemHandle handle, Chart cm,
			List<String> columnExpression) throws ChartException {
		IQueryResults actualResultSet;
		BaseQueryHelper cbqh = new BaseQueryHelper(handle, cm);
		QueryDefinition queryDefn = (QueryDefinition) cbqh.createBaseQuery(columnExpression);

		try {
			setQueryDefinitionWithDataSet(cm, handle, queryDefn);

			processQueryDefinition(queryDefn);

			actualResultSet = executeDataSetQuery(queryDefn);

			if (actualResultSet != null) {
				if (ChartReportItemUtil.isOldChartUsingInternalGroup(itemHandle, cm)) {
					return createSimpleExpressionEvaluator(actualResultSet);
				} else {
					return new BaseGroupedQueryResultSetEvaluator(actualResultSet.getResultIterator(),
							ChartReportItemUtil.isSetSummaryAggregation(cm), cm, itemHandle);
				}
			}
		} catch (BirtException e) {
			throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
		}

		return null;
	}

	/**
	 * @param queryDefn
	 * @return
	 * @throws AdapterException
	 * @throws BirtException
	 */
	protected IQueryResults executeDataSetQuery(QueryDefinition queryDefn) throws AdapterException, BirtException {
		IQueryResults actualResultSet;
		DataSetHandle dataSetHandle = getBindingDataSetHandle();
		if (needDefineDataSet(dataSetHandle)) {
			dteAdapter.registerSession(dataSetHandle, session);
			dteAdapter.defineDataSet(dataSetHandle, session, true, false);
		}

		setRowLimit(session, getMaxRow(), false);
		actualResultSet = dteAdapter.executeQuery(session, queryDefn);
		return actualResultSet;
	}

	/**
	 * @param handle
	 * @param queryDefn
	 * @throws AdapterException
	 * @throws DataException
	 */
	protected void setQueryDefinitionWithDataSet(Chart cm, ExtendedItemHandle handle, QueryDefinition queryDefn)
			throws AdapterException, DataException {
		// Iterate parameter bindings to check if its expression is a
		// explicit
		// value, otherwise use default value of parameter as its
		// expression.
		resetParametersForDataPreview(getDataSetFromHandle(), queryDefn);

		// Add bindings and filters from report handle.
		Iterator<?> bindingIt = getUsedDataSetBindings(cm, handle);
		while (bindingIt != null && bindingIt.hasNext()) {
			Object computedBinding = bindingIt.next();
			IBinding binding = session.getModelAdaptor().adaptBinding((ComputedColumnHandle) computedBinding);
			if (binding == null || queryDefn.getBindings().containsKey(binding.getBindingName())) {
				continue;
			}

			queryDefn.addBinding(binding);
		}
		Iterator<FilterConditionHandle> filtersIterator = getFiltersIterator();
		if (filtersIterator != null) {
			while (filtersIterator.hasNext()) {
				IFilterDefinition filter = session.getModelAdaptor().adaptFilter(filtersIterator.next());
				queryDefn.addFilter(filter);
			}
		}

		handleGroup(queryDefn, handle, session.getModelAdaptor());
	}

	protected Iterator<ComputedColumnHandle> getUsedDataSetBindings(Chart cm, ExtendedItemHandle handle) {
		return ChartReportItemUtil.getColumnDataBindings(handle, true);
	}

	public List<ComputedColumnHandle> getAvailableBindingsList(ExtendedItemHandle handle) {
		Iterator<ComputedColumnHandle> itr = getUsedDataSetBindings(null, handle);
		List<ComputedColumnHandle> columnsList = new ArrayList<ComputedColumnHandle>();
		while (itr.hasNext()) {
			ComputedColumnHandle curColumn = itr.next();
			columnsList.add(curColumn);
		}
		return columnsList;
	}

	/**
	 * Create a simple expression evaluator, it will causes chart engine using
	 * default grouping instead of DTE's grouping.
	 * 
	 * @param actualResultSet
	 * @return
	 * @throws BirtException
	 */
	protected IDataRowExpressionEvaluator createSimpleExpressionEvaluator(IQueryResults actualResultSet)
			throws BirtException {
		final IResultIterator resultIterator = actualResultSet.getResultIterator();
		return new DataRowExpressionEvaluatorAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory. IDataRowExpressionEvaluator
			 * #evaluate(java.lang.String)
			 */
			public Object evaluate(String expression) {
				try {
					return resultIterator.getValue(expression);
				} catch (BirtException e) {
					return null;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory. IDataRowExpressionEvaluator
			 * #evaluateGlobal(java.lang.String)
			 */
			public Object evaluateGlobal(String expression) {
				return evaluate(expression);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory. IDataRowExpressionEvaluator#next()
			 */
			public boolean next() {
				try {
					return resultIterator.next();
				} catch (BirtException e) {
					return false;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory. IDataRowExpressionEvaluator#close()
			 */
			public void close() {
				try {
					resultIterator.close();
				} catch (BirtException e) {
					return;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory. IDataRowExpressionEvaluator#first()
			 */
			public boolean first() {
				try {
					return resultIterator.next();
				} catch (BirtException e) {
					return false;
				}
			}
		};
	}

	/**
	 * Returns iterator of all filters.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Iterator<FilterConditionHandle> getFiltersIterator() {
		List<FilterConditionHandle> filterList = new ArrayList<FilterConditionHandle>();
		PropertyHandle ph = null;
		// Inherit case.
		if (getDataSet() == null && getReportItemReference() == null) {
			// Get filters on container.
			ReportItemHandle bindingHolder = ChartReportItemUtil.getBindingHolder(itemHandle);
			ph = bindingHolder == null ? null : bindingHolder.getPropertyHandle(IExtendedItemModel.FILTER_PROP);
			if (ph != null) {
				Iterator<FilterConditionHandle> filterIterator = ph.iterator();
				if (filterIterator != null) {
					while (filterIterator.hasNext()) {
						// Design time doesn't support parent query expressions
						FilterConditionHandle filter = filterIterator.next();
						if ((filter.getValue1() == null || filter.getValue1().indexOf("._outer") < 0) //$NON-NLS-1$
								&& (filter.getValue2() == null || filter.getValue2().indexOf("._outer") < 0)) //$NON-NLS-1$
						{
							filterList.add(filter);
						}
					}
				}
			}
		}

		// Get filters on current item hanlde.
		ph = itemHandle.getPropertyHandle(ExtendedItemHandle.FILTER_PROP);
		if (ph != null) {
			Iterator<FilterConditionHandle> filterIterator = ph.iterator();
			if (filterIterator != null) {
				while (filterIterator.hasNext()) {
					// Design time doesn't support parent query expressions
					FilterConditionHandle filter = filterIterator.next();
					if ((filter.getValue1() == null || filter.getValue1().indexOf("._outer") < 0)//$NON-NLS-1$
							&& (filter.getValue2() == null || filter.getValue2().indexOf("._outer") < 0)) //$NON-NLS-1$
					{
						filterList.add(filter);
					}
				}
			}
		}

		return filterList.isEmpty() ? null : filterList.iterator();
	}

	/**
	 * Creates the evaluator for Cube Live preview.
	 * 
	 * @param cube
	 * @param cm
	 * @param columnExpression
	 * @return
	 * @throws BirtException
	 */
	@SuppressWarnings("unchecked")
	protected IDataRowExpressionEvaluator createCubeEvaluator(CubeHandle cube, final Chart cm,
			List<String> columnExpression) throws BirtException {
		// Use the chart model in context, because this model will be updated
		// once UI changes it. On the contrary, the model in handle may be old.
		IBaseCubeQueryDefinition qd = null;

		ReportItemHandle referredHandle = ChartReportItemUtil.getReportItemReference(itemHandle);
		boolean isCrosstabReference = referredHandle != null && ICrosstabConstants.CROSSTAB_EXTENSION_NAME
				.equals(((ExtendedItemHandle) referredHandle).getExtensionName());
		boolean isSharingXtab = (referredHandle != null && isCrosstabReference);
		if (isSharingXtab) {
			// If it is 'sharing' case, include sharing crosstab and multiple
			// view, we just invokes referred crosstab handle to create
			// query.
			ExtendedItemHandle bindingHandle = (ExtendedItemHandle) referredHandle;
			qd = CrosstabQueryUtil.createCubeQuery((CrosstabReportItemHandle) bindingHandle.getReportItem(), null,
					session.getModelAdaptor(), true, true, true, true, true, true);
		} else if (isPartChart()) {
			qd = createPartChartCubeQuery(cm);
		} else {
			qd = createCubeQuery(cm);

		}

		// Add additional bindings, those bindings might be defined in chart's triggers.
		if (qd instanceof ICubeQueryDefinition && columnExpression != null && columnExpression.size() > 0) {
			ICubeQueryDefinition queryDef = (ICubeQueryDefinition) qd;

			// Generates a set of available binding names.
			Set<String> bindingNames = new HashSet<String>();
			List<Object> bindings = queryDef.getBindings();
			for (int i = 0; i < bindings.size(); i++) {
				bindingNames.add(((Binding) bindings.get(i)).getBindingName());
			}

			ExpressionSet exprSet = new ExpressionSet();
			exprSet.addAll(columnExpression);
			for (String expr : exprSet) {
				exprCodec.decode(expr);
				String bindingName = null;
				if (exprCodec.isCubeBinding(false)) {
					bindingName = exprCodec.getCubeBindingName(false);
				} else {
					bindingName = exprCodec.getExpression();
				}

				// Don't add duplicate binding.
				if (bindingNames.contains(bindingName)) {
					continue;
				}
				bindingNames.add(bindingName);

				// Create new binding
				Binding colBinding = new Binding(bindingName);
				colBinding.setDataType(org.eclipse.birt.core.data.DataType.ANY_TYPE);
				colBinding
						.setExpression(ChartReportItemUtil.adaptExpression(exprCodec, session.getModelAdaptor(), true));
				queryDef.addBinding(colBinding);
			}
		}

		resetCubeQuery(qd);

		registerCubeHandleToDataSession(cube, isSharingXtab);

		BIRTCubeResultSetEvaluator bcrse = executeCubeQuery(qd, isSharingXtab, cm);
		return bcrse;
	}

	protected IBaseCubeQueryDefinition createCubeQuery(final Chart cm) throws BirtException {
		return new ChartCubeQueryHelper(itemHandle, cm, session.getModelAdaptor()).createCubeQuery(null);
	}

	protected IBaseCubeQueryDefinition createPartChartCubeQuery(final Chart cm) throws BirtException {
		IBaseCubeQueryDefinition qd;
		qd = new ChartCubeQueryHelper(itemHandle, cm, session.getModelAdaptor()).createCubeQuery(null);
		return qd;
	}

	protected BIRTCubeResultSetEvaluator executeCubeQuery(IBaseCubeQueryDefinition qd, boolean isSharingXtab,
			final Chart cm) throws BirtException {
		// Cube doesn't support row limit, clear row limit for cube in case the
		// row limit is set for data set query.
		dteAdapter.unsetRowLimit(session);

		// Always cube query returned
		ICubeQueryResults cqr = dteAdapter.executeQuery(session, (ICubeQueryDefinition) qd);

		// Sharing case
		BIRTCubeResultSetEvaluator bcrse = null;
		if (isSharingXtab) {
			bcrse = new SharedCubeResultSetEvaluator(cqr, qd, cm);
		} else {
			bcrse = new BIRTCubeResultSetEvaluator(cqr);
		}
		bcrse.setSizeLimit(getMaxRow());
		return bcrse;
	}

	protected void registerCubeHandleToDataSession(CubeHandle cube, boolean isCrosstabReference)
			throws BirtException, AdapterException {
		if (needDefineCube(cube)) {
			dteAdapter.registerSession(cube, session);
			session.defineCube(cube);
		}
	}

	/**
	 * @param qd
	 */
	@SuppressWarnings("unchecked")
	protected void resetCubeQuery(IBaseCubeQueryDefinition qd) {
		// Remove special filters that contain _outer expression, live
		// preview doesn't support it.
		ICubeQueryDefinition cqd = (ICubeQueryDefinition) qd;
		for (Iterator<ICubeFilterDefinition> iter = cqd.getFilters().iterator(); iter.hasNext();) {
			ICubeFilterDefinition cfd = iter.next();
			ConditionalExpression ce = (ConditionalExpression) cfd.getExpression();
			if (hasOuterExpr(ce)) {
				iter.remove();
			}
		}
	}

	/**
	 * Check if specified base expression contains .outer expression.
	 * 
	 * @param be
	 * @return
	 */
	private boolean hasOuterExpr(IBaseExpression be) {
		if (be == null) {
			return false;
		}
		if (be instanceof IScriptExpression) {
			IScriptExpression se = (IScriptExpression) be;
			return (se.getText() != null && se.getText().indexOf("._outer") >= 0); //$NON-NLS-1$
		} else if (be instanceof IExpressionCollection) {
			IExpressionCollection ec = (IExpressionCollection) be;
			for (Object expr : ec.getExpressions()) {
				if (expr instanceof IBaseExpression) {
					if (hasOuterExpr((IBaseExpression) expr)) {
						return true;
					}
				}
			}
		} else if (be instanceof IConditionalExpression) {
			IConditionalExpression ce = (IConditionalExpression) be;
			return hasOuterExpr(ce.getExpression()) || hasOuterExpr(ce.getOperand1()) || hasOuterExpr(ce.getOperand2());
		}
		return false;
	}

	/**
	 * The class is responsible to create query definition.
	 * 
	 * @since BIRT 2.3
	 */
	protected class BaseQueryHelper extends AbstractChartBaseQueryGenerator {

		/**
		 * Constructor of the class.
		 * 
		 * @param chart
		 * @param handle
		 * @param query
		 */
		public BaseQueryHelper(ExtendedItemHandle handle, Chart chart) {
			// Used query expressions have been wrapped as bindings, so do not
			// wrap them again.
			super(handle, chart, false, session.getModelAdaptor());
		}

		/**
		 * Create query definition.
		 * 
		 * @param columnExpression
		 * @throws DataException
		 */
		public IDataQueryDefinition createBaseQuery(List<String> columnExpression) throws ChartException {
			QueryDefinition queryDefn = new QueryDefinition();
			int maxRow = getMaxRow();
			queryDefn.setMaxRows(maxRow);

			DataSetHandle dsHandle = getDataSetFromHandle();

			queryDefn.setDataSetName(dsHandle == null ? null : dsHandle.getQualifiedName());

			for (int i = 0; i < columnExpression.size(); i++) {
				String expr = columnExpression.get(i);
				exprCodec.decode(expr);
				String name = exprCodec.getExpression();
				if (!fNameSet.contains(name)) {
					Binding colBinding = new Binding(name);
					colBinding.setExpression(ChartReportItemUtil.adaptExpression(exprCodec, modelAdapter, false));

					try {
						queryDefn.addBinding(colBinding);
					} catch (DataException e) {
						throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, e);
					}
					fNameSet.add(name);
				}
			}

			generateExtraBindings(queryDefn);

			return queryDefn;
		}

		/**
		 * Add aggregate bindings of value series for grouping case.
		 * 
		 * @param query
		 * @param seriesDefinitions
		 * @param innerMostGroupDef
		 * @param valueExprMap
		 * @param baseSD
		 * @throws DataException
		 * @throws DataException
		 */
		protected void addValueSeriesAggregateBindingForGrouping(BaseQueryDefinition query,
				EList<SeriesDefinition> seriesDefinitions, GroupDefinition innerMostGroupDef,
				Map<String, String[]> valueExprMap, SeriesDefinition baseSD) throws ChartException {
			for (SeriesDefinition orthSD : seriesDefinitions) {
				Series series = orthSD.getDesignTimeSeries();

				// The qlist contains available expressions which have
				// aggregation.
				List<Query> qlist = ChartEngine.instance().getDataSetProcessor(series.getClass())
						.getDataDefinitionsForGrouping(series);

				for (Query qry : series.getDataDefinition()) {
					String expr = qry.getDefinition();

					if (expr == null || "".equals(expr) || isDataBinding(expr)) //$NON-NLS-1$
					{
						continue;
					}

					String aggName = ChartUtil.getAggregateFuncExpr(orthSD, baseSD, qry);
					if (aggName == null || "".equals(aggName)) //$NON-NLS-1$
					{
						continue;
					}

					// Get a unique name.
					String name = ChartUtil.generateBindingNameOfValueSeries(qry, orthSD, baseSD);
					if (fNameSet.contains(name)) {
						query.getBindings().remove(name);
					}

					fNameSet.add(name);

					Binding colBinding = new Binding(name);

					colBinding.setDataType(org.eclipse.birt.core.data.DataType.ANY_TYPE);

					if (qlist.contains(qry)) // Has aggregation.
					{
						// Set binding expression by different aggregation, some
						// aggregations can't set expression, like Count and so
						// on.
						try {
							setBindingExpressionDueToAggregation(colBinding, expr, aggName);
						} catch (DataException e1) {
							throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, e1);
						}

						if (innerMostGroupDef != null) {
							try {
								colBinding.addAggregateOn(innerMostGroupDef.getName());
							} catch (DataException e) {
								throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, e);
							}
						}

						// Set aggregate parameters.
						colBinding.setAggrFunction(ChartReportItemUtil.convertToDtEAggFunction(aggName));

						IAggregateFunction aFunc = PluginSettings.instance().getAggregateFunction(aggName);
						if (aFunc.getParametersCount() > 0) {
							String[] parameters = ChartUtil.getAggFunParameters(orthSD, baseSD, qry);

							for (int i = 0; i < parameters.length && i < aFunc.getParametersCount(); i++) {
								String param = parameters[i];
								colBinding.addArgument(new ScriptExpression(param));
							}
						}
					} else {
						// Direct setting expression for non-aggregation case.
						exprCodec.decode(expr);
						colBinding.setExpression(ChartReportItemUtil.adaptExpression(exprCodec, modelAdapter, false));
					}

					String newExpr = getExpressionForEvaluator(name);

					try {
						query.addBinding(colBinding);
					} catch (DataException e) {
						throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, e);
					}

					valueExprMap.put(expr, new String[] { expr, newExpr });
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator
		 * #createBaseQuery (org.eclipse.birt.data.engine.api.IDataQueryDefinition)
		 */
		public IDataQueryDefinition createBaseQuery(IDataQueryDefinition parent) {
			throw new UnsupportedOperationException("Don't be implemented in the class."); //$NON-NLS-1$
		}
	} // End of class BaseQueryHelper.

	boolean isInXTabMeasureCell() {
		return ChartCubeUtil.isInXTabMeasureCell(itemHandle);
	}

	boolean isInXTabCell() {
		try {
			return ChartCubeUtil.getXtabContainerCell(itemHandle, false) != null;
		} catch (BirtException e) {
			// do nothing
		}
		return false;
	}

	boolean isInXTabNonAggrCell() {
		return isInXTabCell() && !isInXTabAggrCell();
	}

	public boolean isPartChart() {
		return ChartCubeUtil.isPlotChart(itemHandle) || ChartCubeUtil.isAxisChart(itemHandle);
	}

	/**
	 * Check if chart is sharing query from other report item.
	 * 
	 * @return true if chart is sharing query from other report item.
	 */
	public boolean isSharedBinding() {
		return (itemHandle.getDataBindingReference() != null || isInMultiView());
	}

	/**
	 * Check if current item handle is table shared binding reference.
	 * 
	 * @return
	 * @since 2.3
	 */
	boolean isTableSharedBinding() {
		return isSharedBinding() && getBindingCubeHandle() == null;
	}

	/**
	 * Set predefined expressions for table shared binding, in table shared binding
	 * case, the binding/expression is read only, so predefined them.
	 * 
	 * @param headers
	 * @since 2.3
	 */
	public void setPredefinedExpressions(ColumnBindingInfo[] headers) {
		if (isSharedBinding() || isInheritColumnsGroups()) {
			fShareBindingQueryHelper.setPredefinedExpressions(headers);
		} else {
			// Add expressions into predefined query for the content assist
			// function when user set category/value series/Y optional
			// expressions on UI.
			context.addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, headers);
			context.addPredefinedQuery(ChartUIConstants.QUERY_VALUE, headers);
			context.addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, headers);
		}
	}

	/**
	 * Returns report item handle.
	 * 
	 * @since 2.3
	 */
	public ReportItemHandle getReportItemHandle() {
		if (isSharedBinding()) {
			if (isInMultiView()) {
				return (ReportItemHandle) itemHandle.getContainer().getContainer();
			}

			return itemHandle.getDataBindingReference();
		} else if (isInXTabNonAggrCell() && isInheritCube()) {
			DesignElementHandle handle = itemHandle;
			while (handle != null) {
				handle = handle.getContainer();

				if (handle instanceof ReportItemHandle && ((ReportItemHandle) handle).getColumnBindings() != null) {
					return (ReportItemHandle) handle;
				}
			}
			return null;
		}

		return itemHandle;
	}

	/**
	 * The class declares some methods for processing query share with table.
	 * 
	 * @since 2.3
	 */
	class ShareBindingQueryHelper {

		protected final ExpressionCodec sbqhExprCodec = ChartModelHelper.instance().createExpressionCodec();

		/**
		 * Set predefined expressions for UI selections.
		 * 
		 * @param headers
		 */
		private void setPredefinedExpressions(ColumnBindingInfo[] headers) {
			Object[] expressionsArray = getPredefinedExpressionsForSharing(headers);

			context.addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, (Object[]) expressionsArray[0]);
			context.addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, (Object[]) expressionsArray[1]);
			context.addPredefinedQuery(ChartUIConstants.QUERY_VALUE, (Object[]) expressionsArray[2]);
		}

		/**
		 * Returns predefined expressions for sharing case.
		 * 
		 * @param headers
		 * @return
		 */
		private Object[] getPredefinedExpressionsForSharing(ColumnBindingInfo[] headers) {
			List<ColumnBindingInfo> commons = new LinkedList<ColumnBindingInfo>();
			List<ColumnBindingInfo> aggs = new LinkedList<ColumnBindingInfo>();
			List<ColumnBindingInfo> groups = new LinkedList<ColumnBindingInfo>();
			for (int i = 0; i < headers.length; i++) {
				int type = headers[i].getColumnType();
				switch (type) {
				case ColumnBindingInfo.COMMON_COLUMN:
					commons.add(headers[i]);
					break;
				case ColumnBindingInfo.AGGREGATE_COLUMN:
					aggs.add(headers[i]);
					break;
				case ColumnBindingInfo.GROUP_COLUMN:
					groups.add(headers[i]);
					break;
				}
			}

			List<ColumnBindingInfo> groupsWithAgg = new LinkedList<ColumnBindingInfo>();
			List<ColumnBindingInfo> groupsWithoutAgg = new LinkedList<ColumnBindingInfo>(groups);

			for (Iterator<ColumnBindingInfo> iter = groupsWithoutAgg.iterator(); iter.hasNext();) {
				ColumnBindingInfo cbiGroup = iter.next();
				String groupName = cbiGroup.getName();

				// Remove some groups defined aggregate.
				for (ColumnBindingInfo cbiAggr : aggs) {
					if (groupName.equals(((ComputedColumnHandle) cbiAggr.getObjectHandle()).getAggregateOn())) {
						iter.remove();
						groupsWithAgg.add(cbiGroup);
						break;
					}
				}
			}

			// Prepare category and Y optional items.
			// TODO
			// ? Now(2008/02/18), for share binding case, we use following
			// rules:
			// 1. Y optional grouping just allow to use first grouping
			// definition in table.
			// 2. Category series allow to use all grouping definitions and
			// binding.
			ColumnBindingInfo[] categorys = new ColumnBindingInfo[groups.size() + commons.size()];

			int index = 0;
			for (ColumnBindingInfo cbi : groups) {
				categorys[index++] = cbi;
			}
			for (ColumnBindingInfo cbi : commons) {
				categorys[index++] = cbi;
			}

			// Prepare Y optional items.
			// (2009/10/27) Rules of inherit colum group: for inheriting column
			// groups case, the Y optional grouping should allow to use all the
			// outer groups than current group handle level(include self group).
			ColumnBindingInfo[] optionals = null;
			if (isInheritColumnsGroups() && groups.size() > 0) {
				List<ColumnBindingInfo> g = new LinkedList<ColumnBindingInfo>();
				DesignElementHandle reh = itemHandle;
				while (reh != null) {
					if (reh.getContainer() instanceof GroupHandle) {
						reh = reh.getContainer();

						for (ColumnBindingInfo cbi : groups) {
							g.add(cbi);
							if (reh.getName().equals(cbi.getName())) {
								break;
							}
						}
						break;
					} else if (reh.getContainer() instanceof RowHandle && reh.getContainer() != null
							&& !(reh.getContainer().getContainer() instanceof GroupHandle)) {
						DesignElementHandle deh = reh;
						while (deh != null) {
							if (deh.getContainer() instanceof ListingHandle) {
								deh = deh.getContainer();
								break;
							}

							deh = deh.getContainer();
						}
						if (deh != null && deh instanceof ListingHandle) {
							if (((ListingHandle) deh).getDetail().findPosn(reh.getContainer()) >= 0) {
								g = groups;
							} else if (((ListingHandle) deh).getHeader().findPosn(reh.getContainer()) >= 0) {
								g.add(groups.get(0));
							} else if (((ListingHandle) deh).getFooter().findPosn(reh.getContainer()) >= 0) {
								g.add(groups.get(0));
							}
						}
						break;
					} else if (reh.getContainer() instanceof ListingHandle) {

						reh = null;
						break;
					}

					reh = reh.getContainer();
				}

				optionals = new ColumnBindingInfo[g.size()];
				int i = 0;
				for (ColumnBindingInfo cbi : g) {
					optionals[i++] = cbi;
				}
			} else {
				int size = (groups.size() > 0) ? 1 : 0;
				optionals = new ColumnBindingInfo[size];
				if (groups.size() > 0) {
					optionals[0] = groups.get(0);
				}
			}

			// Prepare value items.
			ColumnBindingInfo[] values = new ColumnBindingInfo[aggs.size() + commons.size()];
			index = 0;
			for (ColumnBindingInfo cbi : aggs) {
				values[index++] = cbi;
			}
			for (ColumnBindingInfo cbi : commons) {
				values[index++] = cbi;
			}

			return new Object[] { categorys, optionals, values };
		}

		/**
		 * Prepare data expression evaluator for query share with table.
		 * 
		 * @param cm
		 * @param columnExpression
		 * @return
		 * @throws BirtException
		 * @throws AdapterException
		 * @throws DataException
		 * @throws ChartException
		 */
		private IDataRowExpressionEvaluator createShareBindingEvaluator(Chart cm, List<String> columnExpression)
				throws BirtException, AdapterException, DataException, ChartException {
			IQueryResults actualResultSet;
			// Now only create query for table shared binding.
			// Create query definition.
			QueryDefinition queryDefn = new QueryDefinition();
			int maxRow = getMaxRow();
			queryDefn.setMaxRows(maxRow);

			// Binding columns, aggregates, filters and sorts.
			final Map<String, String> bindingExprsMap = new HashMap<String, String>();

			Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings(itemHandle);
			List<ComputedColumnHandle> columnList = new ArrayList<ComputedColumnHandle>();
			while (iterator.hasNext()) {
				columnList.add(iterator.next());
			}

			generateShareBindingsWithTable(getPreviewHeadersInfo(columnList), queryDefn, session, bindingExprsMap);

			// Add custom expression of chart.
			addCustomExpressions(queryDefn, cm, columnExpression, bindingExprsMap);

			// Add filters from report handle.
			Iterator<?> filtersIterator = getPropertyIterator(
					itemHandle.getPropertyHandle(ExtendedItemHandle.FILTER_PROP));
			if (filtersIterator != null) {
				while (filtersIterator.hasNext()) {
					IFilterDefinition filter = session.getModelAdaptor()
							.adaptFilter((FilterConditionHandle) filtersIterator.next());
					queryDefn.addFilter(filter);
				}
			}

			actualResultSet = executeSharedQuery(queryDefn);

			if (actualResultSet != null) {
				return new BaseGroupedQueryResultSetEvaluator(actualResultSet.getResultIterator(),
						ChartReportItemUtil.isSetSummaryAggregation(cm), cm, itemHandle) {

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator
					 * #evaluate(java.lang.String)
					 */
					public Object evaluate(String expression) {
						try {
							String newExpr = bindingExprsMap.get(expression);
							if (newExpr != null) {
								return fResultIterator.getValue(newExpr);
							} else {
								return fResultIterator.getValue(expression);
							}
						} catch (BirtException e) {
							sLogger.log(e);
						}
						return null;
					}
				};
			}
			return null;
		}

		/**
		 * @param queryDefn
		 * @return
		 * @throws AdapterException
		 * @throws BirtException
		 */
		private IQueryResults executeSharedQuery(QueryDefinition queryDefn) throws AdapterException, BirtException {
			IQueryResults actualResultSet;
			DataSetHandle dataSetHandle = getBindingDataSetHandle();
			if (needDefineDataSet(dataSetHandle)) {
				dteAdapter.registerSession(dataSetHandle, session);
				dteAdapter.defineDataSet(dataSetHandle, session, true, false);
			}

			setRowLimit(session, getMaxRow(), false);
			actualResultSet = dteAdapter.executeQuery(session, queryDefn);
			return actualResultSet;
		}

		/**
		 * Add custom expressions of chart to query.
		 * 
		 * @param queryDefn
		 * @param cm
		 * @param bindingExprsMap
		 * @throws DataException
		 */
		private void addCustomExpressions(QueryDefinition queryDefn, Chart cm, List<String> columnExpression,
				final Map<String, String> bindingExprsMap) throws DataException {
			List<Query> queryList = ChartBaseQueryHelper.getAllQueryExpressionDefinitions(cm);

			Set<String> exprSet = new HashSet<String>();
			for (Query query : queryList) {
				String expr = query.getDefinition();
				if (expr != null) {
					exprSet.add(expr);
				}
			}

			exprSet.addAll(columnExpression);

			for (String expr : exprSet) {
				if (expr.length() > 0 && !bindingExprsMap.containsKey(expr)) {
					sbqhExprCodec.decode(expr);
					String name = StructureFactory.newComputedColumn(itemHandle,
							ChartUtil.escapeSpecialCharacters(sbqhExprCodec.getExpression())).getName();
					queryDefn.addBinding(new Binding(name,
							ChartReportItemUtil.adaptExpression(sbqhExprCodec, session.getModelAdaptor(), false)));

					bindingExprsMap.put(expr, name);
				}
			}
		}

		/**
		 * Returns columns header info.
		 * 
		 * @throws ChartException
		 * @since BIRT 2.3
		 */
		private final ColumnBindingInfo[] getPreviewHeadersInfo(List<ComputedColumnHandle> columnList)
				throws ChartException {
			if (columnList == null || columnList.size() == 0) {
				return new ColumnBindingInfo[0];
			}

			ColumnBindingInfo[] columnHeaders = null;

			// Process grouping and aggregate on group case.
			// Get groups.
			List<GroupHandle> groupList = getGroupsOfSharedBinding();

			columnHeaders = new ColumnBindingInfo[columnList.size() + groupList.size()];
			int index = 0;
			for (int i = 0; i < groupList.size(); i++) {
				GroupHandle gh = groupList.get(i);
				String groupName = gh.getName();
				Expression expr = (Expression) gh.getExpressionProperty(IGroupElementModel.KEY_EXPR_PROP).getValue();
				exprCodec.setExpression(expr.getStringExpression());
				exprCodec.setType(expr.getType());
				String groupKeyExpr = exprCodec.encode();
				String tooltip = Messages.getString("ReportDataServiceProvider.Tooltip.GroupExpression") //$NON-NLS-1$
						+ exprCodec.getExpression();
				columnHeaders[index++] = new ColumnBindingInfo(groupName, groupKeyExpr, // Use expression for group.
						ColumnBindingInfo.GROUP_COLUMN, "icons/obj16/group.gif", //$NON-NLS-1$
						tooltip, gh);

				for (Iterator<ComputedColumnHandle> iter = columnList.iterator(); iter.hasNext();) {
					ComputedColumnHandle cch = iter.next();

					String aggOn = cch.getAggregateOn();
					if (groupName.equals(aggOn)) {
						// Remove the column binding from list.
						iter.remove();

						tooltip = Messages.getString("ReportDataServiceProvider.Tooltip.Aggregate") //$NON-NLS-1$
								+ cch.getAggregateFunction() + "\n" //$NON-NLS-1$
								+ Messages.getString("ReportDataServiceProvider.Tooltip.OnGroup") + groupName; //$NON-NLS-1$
						columnHeaders[index] = new ColumnBindingInfo(cch.getName(),
								ExpressionUtil.createJSRowExpression(cch.getName()), // Use
								// binding
								// expression
								// for
								// aggregate.
								ColumnBindingInfo.AGGREGATE_COLUMN, ChartUIConstants.IMAGE_SIGMA, tooltip, cch);
						columnHeaders[index].setChartAggExpression(
								ChartReportItemUtil.convertToChartAggExpression(cch.getAggregateFunction()));
						index++;
					}
				}
			}

			// Process aggregate on whole table case.
			for (Iterator<ComputedColumnHandle> iter = columnList.iterator(); iter.hasNext();) {
				ComputedColumnHandle cch = iter.next();

				if (cch.getAggregateFunction() != null) {
					// Remove the column binding form list.
					iter.remove();

					String tooltip = Messages.getString("ReportDataServiceProvider.Tooltip.Aggregate") //$NON-NLS-1$
							+ cch.getAggregateFunction() + "\n" //$NON-NLS-1$
							+ Messages.getString("ReportDataServiceProvider.Tooltip.OnGroup"); //$NON-NLS-1$
					columnHeaders[index] = new ColumnBindingInfo(cch.getName(),
							ExpressionUtil.createJSRowExpression(cch.getName()), // Use
							// binding
							// expression
							// for
							// aggregate.
							ColumnBindingInfo.AGGREGATE_COLUMN, ChartUIConstants.IMAGE_SIGMA, tooltip, cch);
					columnHeaders[index].setChartAggExpression(
							ChartReportItemUtil.convertToChartAggExpression(cch.getAggregateFunction()));
					index++;
				}
			}

			// Process common bindings.
			for (Iterator<ComputedColumnHandle> iter = columnList.iterator(); iter.hasNext();) {
				ComputedColumnHandle cch = iter.next();
				columnHeaders[index++] = new ColumnBindingInfo(cch.getName(),
						ExpressionUtil.createJSRowExpression(cch.getName()), // Use
						// binding
						// expression
						// for
						// common
						// binding.
						ColumnBindingInfo.COMMON_COLUMN, null, null, cch);
			}

			return columnHeaders;
		}

		/**
		 * Returns groups in shared binding.
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private List<GroupHandle> getGroupsOfSharedBinding() {
			List<GroupHandle> groupList = new ArrayList<GroupHandle>();
			ListingHandle table = null;
			if (isInheritColumnsGroups()) {
				table = findListingInheritance();
			} else {
				ReportItemHandle handle = getReportItemHandle();
				table = getSharedListingHandle(handle);
			}
			if (table != null) {
				SlotHandle groups = table.getGroups();
				for (Iterator<DesignElementHandle> iter = groups.iterator(); iter.hasNext();) {
					groupList.add((GroupHandle) iter.next());
				}
			}
			return groupList;
		}

		/**
		 * Returns correct shared table handle when chart is sharing table's query.
		 * 
		 * @param aItemHandle
		 * @return
		 */
		private ListingHandle getSharedListingHandle(ReportItemHandle aItemHandle) {
			if (aItemHandle instanceof ListingHandle) {
				return (ListingHandle) aItemHandle;
			}

			ReportItemHandle handle = aItemHandle.getDataBindingReference();
			if (handle != null) {
				return getSharedListingHandle(handle);
			}

			if (aItemHandle.getContainer() instanceof MultiViewsHandle) {
				return getSharedListingHandle((ReportItemHandle) aItemHandle.getContainer().getContainer());
			}

			return null;
		}

		/**
		 * Returns preview row data for table shared binding, it will share table's
		 * bindings and get them data.
		 * 
		 * @param headers
		 * @param rowCount
		 * @param isStringType
		 * @return
		 * @throws ChartException
		 * @since 2.3
		 */
		private List<Object[]> getPreviewRowData(ColumnBindingInfo[] headers, int rowCount, boolean isStringType)
				throws ChartException {
			List<Object[]> dataList = new ArrayList<Object[]>();
			// Set thread context class loader so Rhino can find POJOs in
			// workspace
			// projects
			ClassLoader oldContextLoader = Thread.currentThread().getContextClassLoader();
			ClassLoader parentLoader = oldContextLoader;
			if (parentLoader == null)
				parentLoader = this.getClass().getClassLoader();
			ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader(parentLoader,
					itemHandle.getModuleHandle());
			Thread.currentThread().setContextClassLoader(newContextLoader);

			try {
				// Create query definition.
				QueryDefinition queryDefn = new QueryDefinition();
				queryDefn.setMaxRows(getMaxRow());

				// Binding columns, aggregates, filters and sorts.
				List<String> columns = generateShareBindingsWithTable(headers, queryDefn, session,
						new HashMap<String, String>());

				// Add filters from report handle.
				Iterator<?> filtersIterator = getPropertyIterator(
						itemHandle.getPropertyHandle(IExtendedItemModel.FILTER_PROP));
				if (filtersIterator != null) {
					while (filtersIterator.hasNext()) {
						IFilterDefinition filter = session.getModelAdaptor()
								.adaptFilter((FilterConditionHandle) filtersIterator.next());
						queryDefn.addFilter(filter);
					}
				}

				IQueryResults actualResultSet = executeSharedQuery(queryDefn);

				if (actualResultSet != null) {
					int columnCount = columns.size();
					IResultIterator iter = actualResultSet.getResultIterator();
					while (iter.next()) {
						if (isStringType) {
							String[] record = new String[columnCount];
							for (int n = 0; n < columnCount; n++) {
								// Bugzilla#190229, to get string with localized
								// format
								record[n] = valueAsString(iter.getValue(columns.get(n)));
							}
							dataList.add(record);
						} else {
							Object[] record = new Object[columnCount];
							for (int n = 0; n < columnCount; n++) {
								record[n] = iter.getValue(columns.get(n));
							}
							dataList.add(record);
						}
					}

					actualResultSet.close();
				}
			} catch (BirtException e) {
				throw new ChartException(ChartReportItemUIActivator.ID, ChartException.DATA_BINDING, e);
			} finally {
				// Restore old thread context class loader
				Thread.currentThread().setContextClassLoader(oldContextLoader);
			}
			return dataList;
		}

		/**
		 * Generate share bindings with table into query.
		 * 
		 * @param headers
		 * @param queryDefn
		 * @param aSession
		 * @param bindingExprsMap
		 * @return
		 * @throws AdapterException
		 * @throws DataException
		 */
		@SuppressWarnings("unchecked")
		private List<String> generateShareBindingsWithTable(ColumnBindingInfo[] headers, QueryDefinition queryDefn,
				DataRequestSession aSession, Map<String, String> bindingExprsMap)
				throws AdapterException, DataException {
			List<String> columns = new ArrayList<String>();
			ReportItemHandle reportItemHandle = getReportItemHandle();
			if (isInheritColumnsGroups()) {
				reportItemHandle = findListingInheritance();
			}

			queryDefn.setDataSetName(reportItemHandle.getDataSet().getQualifiedName());
			IModelAdapter modelAdapter = aSession.getModelAdaptor();

			for (int i = 0; i < headers.length; i++) {
				ColumnBindingInfo chi = headers[i];
				int type = chi.getColumnType();
				switch (type) {
				case ColumnBindingInfo.COMMON_COLUMN:
				case ColumnBindingInfo.AGGREGATE_COLUMN:
					IBinding binding = modelAdapter.adaptBinding((ComputedColumnHandle) chi.getObjectHandle());
					queryDefn.addBinding(binding);

					columns.add(binding.getBindingName());
					// Use original binding expression as map key for
					// aggregate
					// and common binding.
					bindingExprsMap.put(chi.getExpression(), binding.getBindingName());

					break;
				case ColumnBindingInfo.GROUP_COLUMN:
					GroupDefinition gd = modelAdapter.adaptGroup((GroupHandle) chi.getObjectHandle());
					queryDefn.addGroup(gd);

					String name = StructureFactory.newComputedColumn(reportItemHandle, gd.getName()).getName();
					binding = new Binding(name);
					binding.setExpression(modelAdapter
							.adaptExpression(ChartReportItemUtil.getExpression((GroupHandle) chi.getObjectHandle())));
					queryDefn.addBinding(binding);

					columns.add(name);

					// Use created binding expression as map key for group.
					bindingExprsMap.put(((ScriptExpression) binding.getExpression()).getText(),
							binding.getBindingName());
					break;
				}
			}

			// Add sorts.
			if (reportItemHandle instanceof ListingHandle) {
				queryDefn.getSorts().addAll(ChartBaseQueryHelper
						.createSorts(((ListingHandle) reportItemHandle).sortsIterator(), modelAdapter));
			}

			return columns;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#update(
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public boolean update(String type, Object value) {
		boolean isUpdated = false;
		if (ChartUIConstants.COPY_SERIES_DEFINITION.equals(type)) {
			copySeriesDefinition(value);
		} else if (ChartUIConstants.QUERY_VALUE.equals(type)
				&& (getDataCube() != null && isSharedBinding() || isInXTabNonAggrCell() && isInheritCube())) {
			// Need to automated set category/Y optional bindings by value
			// series
			// binding.
			// 1. Get all bindings.
			Map<String, ComputedColumnHandle> bindingMap = new LinkedHashMap<String, ComputedColumnHandle>();
			for (Iterator<ComputedColumnHandle> bindings = ChartReportItemUtil
					.getAllColumnBindingsIterator(itemHandle); bindings.hasNext();) {
				ComputedColumnHandle column = bindings.next();
				bindingMap.put(column.getName(), column);
			}

			// 2. Get value series bindings.
			String bindingName = exprCodec.getBindingName((String) value);
			ComputedColumnHandle computedBinding = bindingMap.get(bindingName);

			// 3. Get all levels which value series binding aggregate on and set
			// correct binding to category/ Y optional.
			ChartAdapter.beginIgnoreNotifications();
			String exprType = UIUtil.getDefaultScriptType();
			List<String> aggOnList = computedBinding.getAggregateOnList();
			if (aggOnList.size() > 0) {
				String[] levelNames = CubeUtil.splitLevelName(aggOnList.get(0));
				ComputedColumnHandle cch = ChartReportItemHelper.instance().findDimensionBinding(exprCodec,
						levelNames[0], levelNames[1], bindingMap.values(), itemHandle);
				// Set category.
				if (cch != null) {
					setCategoryExpression(exprType, cch);
				}
			}

			if (aggOnList.size() > 1) {
				String[] levelNames = CubeUtil.splitLevelName(aggOnList.get(1));
				ComputedColumnHandle cch = ChartReportItemHelper.instance().findDimensionBinding(exprCodec,
						levelNames[0], levelNames[1], bindingMap.values(), itemHandle);
				// Set optional Y.
				if (cch != null) {
					isUpdated = setOptionalYExpression(exprType, cch);
				}
			} else {
				if (aggOnList.size() == 0) {
					// If it consumes xtab, including multiple view, sharing
					// xtab, and inheriting from xtab. And the selected series
					// expression is a computed/derived expression, we need
					// update category and optional Y expressions list for user
					// to select.
					CrosstabReportItemHandle xtabHandle = null;
					try {
						if (checkState(IN_MULTI_VIEWS)) {
							// Chart view of xtab
							xtabHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) this.itemHandle.getContainer()
									.getContainer()).getReportItem();

						} else if (isSharedBinding()) // Sharing xtab.
						{
							ReportItemHandle rih = ChartReportItemUtil.getReportItemReference(this.itemHandle);
							if (rih != null && rih instanceof ExtendedItemHandle) {
								xtabHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) rih).getReportItem();
							}
						} else if (isInXTabNonAggrCell() && isInheritCube()) {
							// Inheriting from xtab.
							xtabHandle = ChartCubeUtil.getXtabContainerCell(itemHandle, false).getCrosstab();
						}
					} catch (ExtendedElementException e) {
						WizardBase.displayException(e);
					} catch (BirtException e) {
						WizardBase.displayException(e);
					}
					if (xtabHandle != null) {
						isUpdated = updateExpressionForConsumingXTab(xtabHandle, exprType, bindingMap);
					}
				} else {
					// When chart share cube with other chart, the measure
					// expression(value series expressions) doesn't contain
					// level
					// information, so here only update Y optional expression
					// for
					// sharing with crosstab case.
					if (!isChartReportItemHandle(ChartItemUtil.getReportItemReference(itemHandle))) {
						for (Iterator<SeriesDefinition> iter = ChartUIUtil
								.getAllOrthogonalSeriesDefinitions(context.getModel()).iterator(); iter.hasNext();) {
							SeriesDefinition sd = iter.next();
							sd.getQuery().setDefinition(""); //$NON-NLS-1$
							isUpdated = true;
						}
					}
				}
			}

			ChartAdapter.endIgnoreNotifications();
		} else if (ChartUIConstants.QUERY_CATEGORY.equals(type) && value instanceof String) {
			EList<SeriesDefinition> baseSDs = ChartUtil.getBaseSeriesDefinitions(context.getModel());
			for (SeriesDefinition sd : baseSDs) {
				EList<Query> dds = sd.getDesignTimeSeries().getDataDefinition();
				Query q = dds.get(0);
				if (q.getDefinition() == null || "".equals(q.getDefinition().trim())) //$NON-NLS-1$
				{
					q.setDefinition((String) value);
				}
			}
		} else if (ChartUIConstants.QUERY_OPTIONAL.equals(type) && value instanceof String) {
			List<SeriesDefinition> orthSDs = ChartUtil.getAllOrthogonalSeriesDefinitions(context.getModel());
			for (SeriesDefinition sd : orthSDs) {
				Query q = sd.getQuery();

				if (q == null) {
					sd.setQuery(QueryImpl.create((String) value));
					continue;
				}

				if (q.getDefinition() == null || "".equals(q.getDefinition().trim())) //$NON-NLS-1$
				{
					q.setDefinition((String) value);
				}
			}
		} else if (ChartUIConstants.UPDATE_CUBE_BINDINGS.equals(type)) {
			isUpdated = updateCubeBindings();
		}
		return isUpdated;
	}

	private boolean updateExpressionForConsumingXTab(CrosstabReportItemHandle xtabHandle, String exprType,
			Map<String, ComputedColumnHandle> bindingMap) {
		boolean isUpdated = false;
		List<ComputedColumnHandle> rowChildren = new ArrayList<ComputedColumnHandle>();
		for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE); i++) {
			DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i);
			ComputedColumnHandle cch = ChartReportItemHelper.instance().findDimensionBinding(exprCodec,
					dimensionHandle.getCubeDimensionName(),
					dimensionHandle.getLevel(dimensionHandle.getLevelCount() - 1).getCubeLevel().getName(),
					bindingMap.values(), itemHandle);
			rowChildren.add(cch);
		}
		if (rowChildren.size() > 0) {
			// If the existent category expression is legal,
			// then don't set category expression again.
			Query query = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0).getDesignTimeSeries()
					.getDataDefinition().get(0);
			String expr = query.getDefinition();
			boolean needUpdate = !isExistentBinding(expr, bindingMap, exprType);
			if (needUpdate) {
				setCategoryExpression(exprType, rowChildren.get(0));
			}
		}

		List<ComputedColumnHandle> colChildren = new ArrayList<ComputedColumnHandle>();
		for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE); i++) {
			DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE, i);
			ComputedColumnHandle cch = ChartReportItemHelper.instance().findDimensionBinding(exprCodec,
					dimensionHandle.getCubeDimensionName(),
					dimensionHandle.getLevel(dimensionHandle.getLevelCount() - 1).getCubeLevel().getName(),
					bindingMap.values(), itemHandle);
			colChildren.add(cch);
		}
		if (colChildren.size() > 0) {
			// If row children list is empty, it means related
			// crosstab has no row, just use column expression
			// to set category expression of chart.
			if (rowChildren.size() == 0) {
				// If the existent category expression is legal,
				// then don't set category expression again.
				Query query = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0).getDesignTimeSeries()
						.getDataDefinition().get(0);
				String expr = query.getDefinition();
				boolean needUpdate = !isExistentBinding(expr, bindingMap, exprType);
				if (needUpdate) {
					setCategoryExpression(exprType, colChildren.get(0));
				}
			} else {
				// If the existent optional Y expression is
				// legal, then don't set it again.
				Query query = ChartUIUtil.getAllOrthogonalSeriesDefinitions(context.getModel()).get(0).getQuery();
				String expr = query.getDefinition();
				boolean needUpdate = !isExistentBinding(expr, bindingMap, exprType);
				if (needUpdate) {
					isUpdated = setOptionalYExpression(exprType, colChildren.get(0));
				}
			}
		}
		return isUpdated;
	}

	private boolean isExistentBinding(String expr, Map<String, ComputedColumnHandle> bindingMap, String exprType) {
		if (expr == null || "".equals(expr)) //$NON-NLS-1$
		{
			return false;
		}
		boolean isExistent = false;
		for (ComputedColumnHandle cch : bindingMap.values()) {
			exprCodec.setBindingName(cch.getName(), true, exprType);
			if (expr.indexOf(exprCodec.encode()) >= 0) {
				isExistent = true;
				break;
			}
		}
		return isExistent;
	}

	private boolean setOptionalYExpression(String exprType, ComputedColumnHandle cch) {
		boolean isUpdated = false;
		for (Iterator<SeriesDefinition> iter = ChartUIUtil.getAllOrthogonalSeriesDefinitions(context.getModel())
				.iterator(); iter.hasNext();) {
			exprCodec.setBindingName(cch.getName(), true, exprType);
			Query query = iter.next().getQuery();
			query.setDefinition(exprCodec.encode());
			isUpdated = true;
		}
		return isUpdated;
	}

	private void setCategoryExpression(String exprType, ComputedColumnHandle cch) {
		Query query = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0).getDesignTimeSeries()
				.getDataDefinition().get(0);
		exprCodec.setBindingName(cch.getName(), true, exprType);
		query.setDefinition(exprCodec.encode());

		// Update X axis type in chart with axes
		if (context.getModel() instanceof ChartWithAxes) {
			Axis xAxis = ChartUIUtil.getAxisXForProcessing((ChartWithAxes) context.getModel());
			xAxis.setType(ChartItemUtil.convertToAxisType(cch.getDataType()));
		}
	}

	/**
	 * Updates cube bindings due to the change of cube set.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean updateCubeBindings() {
		boolean updated = false;
		try {
			CubeHandle cubeHandle = itemHandle.getCube();
			if (cubeHandle != null) {
				List<ComputedColumn> columnList = ChartXTabUIUtil.generateComputedColumns(itemHandle, cubeHandle);
				if (columnList.size() > 0) {
					List<String> bindingNameList = new ArrayList<String>();
					for (Iterator<ComputedColumnHandle> iter = itemHandle.columnBindingsIterator(); iter.hasNext();) {
						ComputedColumnHandle cch = iter.next();
						bindingNameList.add(cch.getName());
					}
					for (Iterator<ComputedColumn> iter = columnList.iterator(); iter.hasNext();) {
						ComputedColumn cc = iter.next();
						if (!bindingNameList.contains(cc.getName())) {
							DEUtil.addColumn(itemHandle, cc, false);
							updated = true;
						}
					}
				}
			}
		} catch (SemanticException e) {
			ChartWizard.showException(ChartWizard.RepDSProvider_Cube_ID, e.getLocalizedMessage());
		}
		return updated;
	}

	/**
	 * Check if the cube of current item handle has multiple dimensions.
	 * 
	 * @return
	 * @since 2.3
	 */
	boolean hasMultiCubeDimensions() {
		CubeHandle cube = getBindingCubeHandle();
		if (ChartCubeUtil.getDimensionCount(cube) > 1) {
			return true;
		}

		return false;
	}

	boolean isInXTabAggrCell() {
		try {
			AggregationCellHandle cell = ChartCubeUtil.getXtabContainerCell(itemHandle);
			if (cell != null) {
				return ChartCubeUtil.isAggregationCell(cell);
			}
		} catch (BirtException e) {
			// Silent exception
		}
		return false;
	}

	boolean isInheritColumnsSet() {
		PropertyHandle property = itemHandle.getPropertyHandle(ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS);
		return property != null && property.isSet();
	}

	protected boolean isInheritColumnsOnly() {
		return itemHandle.getDataSet() == null && ChartReportItemUtil.isContainerInheritable(itemHandle)
				&& context.isInheritColumnsOnly();
	}

	protected boolean isInheritColumnsGroups() {
		return itemHandle.getDataSet() == null && ChartReportItemUtil.isContainerInheritable(itemHandle)
				&& !ChartReportItemUtil.isContainerGridHandle(itemHandle) && !context.isInheritColumnsOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getStates()
	 */
	public int getState() {
		int states = 0;
		if (getDataSet() != null) {
			states |= HAS_DATA_SET;
		}
		if (getDataCube() != null) {
			states |= HAS_CUBE;
		}
		if (getInheritedDataSet() != null) {
			states |= INHERIT_DATA_SET;
		}
		if (getInheritedCube() != null) {
			states |= INHERIT_CUBE;
		}
		if (itemHandle.getDataBindingReference() != null) {
			states |= DATA_BINDING_REFERENCE;
		}
		if (isInMultiView()) {
			states |= IN_MULTI_VIEWS;
		}
		if (isSharedBinding()) {
			states |= SHARE_QUERY;
			if (isSharingChart(false)) {
				states |= SHARE_CHART_QUERY;
			}

			if (isSharingChart(true)) {
				states |= SHARE_CHART_QUERY_RECURSIVELY;
			}

			if (getBindingCubeHandle() != null) {
				states |= SHARE_CROSSTAB_QUERY;
			} else if (!isSharingChart(false)) {
				states |= SHARE_TABLE_QUERY;
			}
		}
		if (isPartChart()) {
			states |= PART_CHART;
		}
		if (hasMultiCubeDimensions()) {
			states |= MULTI_CUBE_DIMENSIONS;
		}
		if (isInheritColumnsOnly()) {
			states |= INHERIT_COLUMNS_ONLY;
		}
		if (isInheritColumnsGroups()) {
			states |= INHERIT_COLUMNS_GROUPS;
		}
		if (isKeepCubeHierarchyAndNotCubeTopLevelOnCategory()) {
			states |= IS_CUBE_AND_CATEGORY_NOT_TOP_LEVEL;
		}
		if (isKeepCubeHierarchyAndNotCubeTopLevelOnSeries()) {
			states |= IS_CUBE_AND_SERIES_NOT_TOP_LEVEL;
		}

		return states;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkState
	 * (int)
	 */
	public boolean checkState(int state) {
		return (getState() & state) == state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkData
	 * (String, java.lang.Object)
	 */
	public Object checkData(String checkType, Object data) {
		if (ChartUIConstants.QUERY_OPTIONAL.equals(checkType) || ChartUIConstants.QUERY_CATEGORY.equals(checkType)) {
			// Only check query for Cube/Crosstab sharing cases.
			if (checkState(IDataServiceProvider.INHERIT_CUBE) || checkState(IDataServiceProvider.HAS_CUBE)
					|| checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY)) {
				return Boolean.valueOf(
						ChartXTabUIUtil.checkQueryExpression(checkType, data, context.getModel(), itemHandle, this));
			}
		}

		return null;
	}

	private ListingHandle findListingInheritance() {
		DesignElementHandle container = itemHandle.getContainer();
		while (container != null) {
			if (container instanceof ListingHandle && ((ListingHandle) container).getDataSet() != null) {
				return (ListingHandle) container;
			}
			container = container.getContainer();
		}
		return null;
	}

	/**
	 * Calculate the number of expressions binded to category series, value series
	 * or y-optional grouping .
	 * 
	 * @param expr
	 * @return count number
	 */
	int getNumberOfSameDataDefinition(String expr) {
		if (expr == null) {
			return 0;
		}
		int count = 0;
		Chart chart = context.getModel();
		String[] expres = ChartUtil.getCategoryExpressions(chart);
		for (String s : expres) {
			if (expr.equals(s)) {
				count++;
			}
		}
		expres = ChartUtil.getValueSeriesExpressions(chart);
		for (String s : expres) {
			if (expr.equals(s)) {
				count++;
			}
		}
		expres = ChartUtil.getYOptoinalExpressions(chart);
		for (String s : expres) {
			if (expr.equals(s)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Check if current item handle is table shared binding reference. Check if
	 * current is sharing query with other chart.
	 * 
	 * @param isRecursive <code>true</code> means it will be recursive to find out
	 *                    the shared chart case, else just check if the direct
	 *                    reference is a chart item.
	 * @return
	 */
	boolean isSharingChart(boolean isRecursive) {
		boolean isShare = isSharedBinding();
		if (isRecursive) {
			return isShare && isChartReportItemHandle(ChartReportItemUtil.getReportItemReference(itemHandle));
		} else {
			return isShare && isChartReportItemHandle(itemHandle.getDataBindingReference());
		}
	}

	/**
	 * This method just calls a utility method to check if current handle holds a
	 * chart,and will be overridden for difference cases.
	 * 
	 * @param referredHandle
	 * @return if current handle holds a chart
	 * @since 2.5.3
	 */
	public boolean isChartReportItemHandle(ReportItemHandle referredHandle) {
		return ChartReportItemUtil.isChartReportItemHandle(referredHandle);
	}

	/**
	 * This method just calls a utility method to get reference handle.
	 * 
	 * @return reference handle
	 * @since 2.5.3
	 */
	public ExtendedItemHandle getChartReferenceItemHandle() {
		return ChartReportItemUtil.getChartReferenceItemHandle(itemHandle);
	}

	/**
	 * @param target
	 * @since 2.5.1
	 */
	protected void copySeriesDefinition(Object target) {
		Chart targetCM = context.getModel();
		if (target != null && target instanceof Chart) {
			targetCM = (Chart) target;
		}
		ExtendedItemHandle refHandle = getChartReferenceItemHandle();
		if (refHandle != null) {
			Chart srcChart = ChartReportItemUtil.getChartFromHandle(refHandle);
			ChartReportItemUtil.copyChartSeriesDefinition(srcChart, targetCM);
			ChartReportItemUtil.copyChartSampleData(srcChart, targetCM);
		}
	}

	public String[] getSeriesExpressionsFrom(Object obj, String type) {
		if (!(obj instanceof Chart)) {
			return new String[] {};
		}

		if (ChartUIConstants.QUERY_CATEGORY.equals(type)) {
			return ChartUtil.getCategoryExpressions((Chart) obj);
		} else if (ChartUIConstants.QUERY_OPTIONAL.equals(type)) {
			return ChartUtil.getYOptoinalExpressions((Chart) obj);
		} else if (ChartUIConstants.QUERY_VALUE.equals(type)) {
			return ChartUtil.getValueSeriesExpressions((Chart) obj);
		}

		return new String[] {};
	}

	/**
	 * DummyEngineTask implementation for chart live preview.
	 */
	protected static class ChartDummyEngineTask extends DummyEngineTask {

		public ChartDummyEngineTask(ReportEngine engine, IReportRunnable runnable, ModuleHandle moduleHandle) {
			super(engine, runnable, moduleHandle);
		}

		@Override
		public void run() throws EngineException {
			parameterChanged = true;
			usingParameterValues();
			// Initial report variables to make sure the report variables can be
			// access in chart builder.
			initReportVariable();
			loadDesign();
		}
	}

	protected boolean isKeepCubeHierarchyAndNotCubeTopLevelOnCategory() {
		return ChartReportItemUtil.isKeepCubeHierarchyAndNotCubeTopLevelOnCategory(context.getModel(),
				getBindingCubeHandle(), itemHandle);
	}

	protected boolean isKeepCubeHierarchyAndNotCubeTopLevelOnSeries() {
		return ChartReportItemUtil.isKeepCubeHierarchyAndNotCubeTopLevelOnSeries(context.getModel(),
				getBindingCubeHandle(), itemHandle);
	}

	public CubeHandle getBindingCubeHandle() {
		return ChartReportItemHelper.instance().getBindingCubeHandle(itemHandle);
	}

	public DataSetHandle getBindingDataSetHandle() {
		return ChartReportItemHelper.instance().getBindingDataSetHandle(itemHandle);
	}
}
