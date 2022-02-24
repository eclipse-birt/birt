/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.impl.DataModelAdapter;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IEngineConfig;
import org.eclipse.birt.report.engine.api.IExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunnable;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;

public class DatasetPreviewTask extends EngineTask implements IDatasetPreviewTask {

	protected IRunnable runnable;

	protected DataSetHandle dataset;

	protected QueryDefinition query;

	protected int maxRow = -1;

	/**
	 * Start row.
	 */
	protected int startRow = 0;

	protected IFilterDefinition[] filterExpressions = null;

	protected ISortDefinition[] sortExpressions = null;
	protected boolean overrideExistingSorts = false;

	protected String[] selectedColumns;

	/**
	 * Filter defined on data sets shall be ignored during evaluation of candidate
	 * values for report parameter. PARAM_EVALUATION_FLOW Data Engine flow mode
	 * shall be used in this scenario in addition to the Data Engine mode.
	 */
	private DataEngineFlowMode dataEngineFlowMode = DataEngineFlowMode.NORMAL;

	protected DatasetPreviewTask(ReportEngine engine) {
		super(engine, TASK_DATASETPREVIEW);
	}

	public IExtractionResults execute() throws EngineException {
		if (dataset == null) {
			throw new IllegalArgumentException("dataset can not be null");
		}
		return runDataset();
	}

	public IExtractionResults extract() throws EngineException {
		return execute();
	}

	public void extract(IExtractionOption options) throws BirtException {
		IDataExtractionOption option = null;
		if (options == null) {
			option = new DataExtractionOption();
		} else {
			option = new DataExtractionOption(options.getOptions());
		}
		IDataExtractionOption extractOption = setupExtractOption(option);
		IDataExtractionExtension dataExtraction = getDataExtractionExtension(extractOption);
		try {
			dataExtraction.initialize(executionContext.getReportContext(), extractOption);
			IExtractionResults results = execute();
			if (executionContext.isCanceled()) {
				return;
			} else {
				dataExtraction.output(results);
			}
		} finally {
			dataExtraction.release();
		}
	}

	private IDataExtractionOption setupExtractOption(IExtractionOption options) {
		// setup the data extraction options from:
		HashMap allOptions = new HashMap();

		// try to get the default render option from the engine config.
		HashMap configs = engine.getConfig().getEmitterConfigs();
		// get the default format of the emitters, the default format key is
		// IRenderOption.OUTPUT_FORMAT;
		IRenderOption defaultOptions = (IRenderOption) configs.get(IEngineConfig.DEFAULT_RENDER_OPTION);
		if (defaultOptions != null) {
			allOptions.putAll(defaultOptions.getOptions());
		}

		// try to get the render options by the format
		IRenderOption defaultHtmlOptions = (IRenderOption) configs.get(IRenderOption.OUTPUT_FORMAT_HTML);
		if (defaultHtmlOptions != null) {
			allOptions.putAll(defaultHtmlOptions.getOptions());
		}

		// merge the user's setting
		allOptions.putAll(options.getOptions());

		// copy the new setting to old APIs
		Map appContext = executionContext.getAppContext();
		Object renderContext = appContext.get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT);
		if (renderContext == null) {
			HTMLRenderContext htmlContext = new HTMLRenderContext();
			HTMLRenderOption htmlOptions = new HTMLRenderOption(allOptions);
			htmlContext.setBaseImageURL(htmlOptions.getBaseImageURL());
			htmlContext.setBaseURL(htmlOptions.getBaseURL());
			htmlContext.setImageDirectory(htmlOptions.getImageDirectory());
			htmlContext.setSupportedImageFormats(htmlOptions.getSupportedImageFormats());
			htmlContext.setRenderOption(htmlOptions);
			appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, htmlContext);
		}

		IDataExtractionOption extractOption = new DataExtractionOption(allOptions);

		return extractOption;
	}

	private IDataExtractionExtension getDataExtractionExtension(IDataExtractionOption option) throws EngineException {
		IDataExtractionExtension dataExtraction = null;
		String extension = option.getExtension();
		ExtensionManager extensionManager = ExtensionManager.getInstance();
		if (extension != null) {
			dataExtraction = extensionManager.createDataExtractionExtensionById(extension);
		}

		String format = null;
		if (dataExtraction == null) {
			format = option.getOutputFormat();
			if (format != null) {
				dataExtraction = extensionManager.createDataExtractionExtensionByFormat(format);
			}
		}
		if (dataExtraction == null) {
			throw new EngineException(MessageConstants.INVALID_EXTENSION_ERROR, new Object[] { extension, format });
		}
		return dataExtraction;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	protected void checkRequiredParamenter(String paramName, String value) throws ParameterValidationException {

	}

	public void setDataSet(DataSetHandle dataset) {
		if (dataset == null) {
			throw new IllegalArgumentException("dataset can not be null!");
		}
		this.dataset = dataset;
		ModuleHandle mh = dataset.getModuleHandle();
		runnable = new ReportRunnable(engine, mh);
		setReportRunnable((ReportRunnable) runnable);
	}

	public void setRunnable(IRunnable runnable) {
		this.runnable = runnable;
		setReportRunnable((ReportRunnable) runnable);
	}

	public void selectColumns(String[] columnNames) {
		selectedColumns = columnNames;
	}

	public void setFilters(IFilterDefinition[] simpleFilterExpression) {
		filterExpressions = simpleFilterExpression;
	}

	public void setSorts(ISortDefinition[] simpleSortExpression) {
		setSorts(simpleSortExpression, false);
	}

	protected ModuleHandle getHandle() {
		return ((ReportRunnable) runnable).getModuleHandle();
	}

	protected IExtractionResults runDataset() throws EngineException {
		IExtractionResults resultset = null;
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			if (runnable == null) {
				throw new EngineException(MessageConstants.REPORT_RUNNABLE_NOT_SET_EXCEPTION); // $NON-NLS-1$
			}
			resultset = doRun();
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
		}

		return resultset;
	}

	/**
	 * runs the report
	 * 
	 * @throws EngineException throws exception when there is a run error
	 */
	protected IExtractionResults doRun() throws EngineException {
		IExtractionResults result = null;
		usingParameterValues();
		initReportVariable();
		loadDesign();
		prepareDesign();
		startFactory();
		try {

			executionContext.openDataEngine();
			result = extractQuery(dataset);

			// executionContext.closeDataEngine( );
		} catch (Exception ex) {
			log.log(Level.SEVERE, "An error happened while extracting data the report. Cause:", ex); //$NON-NLS-1$
			throw new EngineException(MessageConstants.REPORT_RUN_ERROR, ex);
		} catch (OutOfMemoryError err) {
			log.log(Level.SEVERE, "There is insufficient memory to extract data from this report."); //$NON-NLS-1$
			throw err;
		} catch (Throwable t) {
			log.log(Level.SEVERE, "Error happened while running the report.", t); //$NON-NLS-1$
			throw new EngineException(MessageConstants.REPORT_RUN_ERROR, t); // $NON-NLS-1$
		} finally {
			closeFactory();
		}
		return result;
	}

	protected IExtractionResults extractQuery(DataSetHandle dataset) throws BirtException {
		DataRequestSession session = executionContext.getDataEngine().getDTESession();

		session.getDataSessionContext().getDataEngineContext().setFlowMode(this.dataEngineFlowMode);

		QueryDefinition newQuery = constructQuery(dataset, session);
		ModelDteApiAdapter apiAdapter = new ModelDteApiAdapter(executionContext);
		apiAdapter.defineDataSet(dataset, session);
		session.registerQueries(new IQueryDefinition[] { newQuery });
		IBasePreparedQuery preparedQuery = session.prepare(newQuery);
		IQueryResults result = (IQueryResults) session.execute(preparedQuery, null,
				executionContext.getScriptContext());
		ResultMetaData metadata = new ResultMetaData(result.getResultMetaData());
		if (null != selectedColumns) {
			metadata = new ResultMetaData(metadata, selectedColumns);
		}
		// apply the startRow and maxRows in query. So here we need not apply them to
		// the result.
		return new ExtractionResults(result, metadata, null, 0, -1, null);
	}

	protected ModuleHandle getModuleHandle() {
		return dataset.getModuleHandle();
	}

	protected QueryDefinition constructQuery(DataSetHandle dataset, DataRequestSession session) throws BirtException {
		if (this.query != null)
			return this.query;
		if (dataset.getCachedMetaDataHandle() == null) {
			session.refreshMetaData(dataset);
		}
		QueryDefinition query = new QueryDefinition();
		query.setDataSetName(dataset.getQualifiedName());
		Set<String> existBindings = new HashSet<String>();

		Map<String, ResultSetColumn> columns = QueryUtil.getResultSetColumns(dataset);
		if (this.selectedColumns == null) {
			for (ResultSetColumn column : columns.values()) {
				if (!existBindings.contains(column.getColumnName())) {
					QueryUtil.addBinding(query, column);
					existBindings.add(column.getColumnName());
				}
			}
		} else {
			for (String column : selectedColumns) {
				if (!existBindings.contains(column)) {
					ResultSetColumn col = columns.get(column);
					if (col != null) {
						QueryUtil.addBinding(query, col);
						existBindings.add(column);
					}
				}
			}
		}
		Set<String> referenced = new HashSet<String>();
		// set max rows
		if (maxRow >= 0) {
			query.setMaxRows(maxRow);
		}
		// set start row.
		if (startRow > 0) {
			query.setStartingRow(startRow);
		}
		// add filter
		if (filterExpressions != null) {
			for (int i = 0; i < filterExpressions.length; i++) {
				query.getFilters().add(filterExpressions[i]);
				findReferencedColumns(referenced, filterExpressions[i].getExpression());
			}
			filterExpressions = null;
		}
		// add sort
		if (sortExpressions != null) {
			if (this.overrideExistingSorts) {
				query.getSorts().clear();
			}
			for (int i = 0; i < sortExpressions.length; i++) {
				query.getSorts().add(sortExpressions[i]);
				findReferencedColumns(referenced, sortExpressions[i].getExpression());
			}
			sortExpressions = null;
		}
		if (!referenced.isEmpty()) {
			for (String col : referenced) {
				if (!existBindings.contains(col)) {
					QueryUtil.addBinding(query, col);
					existBindings.add(col);
				}
			}
		}
		return query;
	}

	private void findReferencedColumns(Set<String> referenced, IBaseExpression expr) {
		findReferencedColumns(referenced, null, expr);

	}

	@SuppressWarnings("unchecked")
	protected void findReferencedColumns(Set<String> referencedRows, Set<String> referencedDSRows,
			IBaseExpression expr) {
		if (expr instanceof IScriptExpression) {
			IScriptExpression script = (IScriptExpression) expr;
			try {
				IScriptExpression scriptExpr = script;
				String scriptId = script.getScriptId();
				if (BaseExpression.constantId.equals(scriptId)) {
					// Constant expression can't refer to columns
					return;
				}
				// convert non-JavaScript expression into JS expression
				if (!Expression.SCRIPT_JAVASCRIPT.equals(scriptId)) {
					IModelAdapter adapter = getModelAdapter();
					scriptExpr = adapter.adaptJSExpression(script.getText(), script.getScriptId());
				}

				// find referenced binding names
				// the script text may be null value, for example: a value
				// list which contains a none value.
				if (scriptExpr != null) {
					List<IColumnBinding> columns = ExpressionUtil.extractColumnExpressions(scriptExpr.getText());
					for (IColumnBinding col : columns) {
						referencedRows.add(col.getResultSetColumnName());
					}

					if (referencedDSRows != null) {
						columns = ExpressionUtil.extractColumnExpressions(scriptExpr.getText(),
								ExpressionUtil.DATASET_ROW_INDICATOR);
						for (IColumnBinding col : columns) {
							referencedDSRows.add(col.getResultSetColumnName());
						}
					}
				}
			} catch (BirtException e) {
				log.log(Level.WARNING, "Error processing script: " + script.getText(), e);
			}
		} else if (expr instanceof IConditionalExpression) {
			IConditionalExpression condition = (IConditionalExpression) expr;
			findReferencedColumns(referencedRows, referencedDSRows, condition.getExpression());
			findReferencedColumns(referencedRows, referencedDSRows, condition.getOperand1());
			findReferencedColumns(referencedRows, referencedDSRows, condition.getOperand2());
		} else if (expr instanceof IExpressionCollection) {
			IExpressionCollection exprs = (IExpressionCollection) expr;
			Collection<IBaseExpression> collection = exprs.getExpressions();
			for (Iterator<IBaseExpression> i = collection.iterator(); i.hasNext();) {
				findReferencedColumns(referencedRows, referencedDSRows, i.next());
			}
		}
	}

	protected IModelAdapter getModelAdapter() throws BirtException {
		IModelAdapter adapter = new DataModelAdapter(new DataSessionContext(DataEngineContext.DIRECT_PRESENTATION));
		return adapter;
	}

	protected void validateStringParameter(String paramName, Object paramValue,
			AbstractScalarParameterHandle paramHandle) throws ParameterValidationException {
		// do not check length of parameter value even when parameter value is required
	}

	protected void loadDesign() {

		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable != null) {
			ReportDesignHandle reportDesign = executionContext.getReportDesign();
			if (reportDesign != null) {
				// execute scripts defined in include-script element of the
				// libraries
				Iterator iter = reportDesign.includeLibraryScriptsIterator();
				loadScript(iter);
				// execute scripts defined in include-script element of this
				// report
				iter = reportDesign.includeScriptsIterator();
				loadScript(iter);

				// Intialize the report
				ReportScriptExecutor.handleInitialize(reportDesign, executionContext);
			} else {
				if (dataset != null) {
					ModuleHandle moduleHandle = dataset.getModuleHandle();
					Iterator iter = moduleHandle.includeScriptsIterator();
					loadScript(iter);

					// Intialize the report
					ReportScriptExecutor.handleInitialize(moduleHandle, executionContext);

				}
			}
		}

	}

	public void setQuery(QueryDefinition query) {
		this.query = query;
	}

	@Override
	public void setSorts(ISortDefinition[] simpleSortExpression, boolean overrideExistingSorts) {
		this.sortExpressions = simpleSortExpression;
		this.overrideExistingSorts = overrideExistingSorts;
	}

	public void setDataEngineFlowMode(DataEngineFlowMode dataEngineFlowMode) {
		this.dataEngineFlowMode = dataEngineFlowMode;
	}

}
