/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.engine.adapter;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.data.Constants;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.ICombinedOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.CombinedOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSourceEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.script.internal.DataSetScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DataSourceScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ScriptDataSetScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ScriptDataSourceScriptExecutor;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.mozilla.javascript.Scriptable;

/**
 * An adapter class that creates data engine API interface objects from the
 * model.api objects for data set and data source definition.
 * 
 * The user of this adaptor can optionally provide an associated
 * ExecutionContext object and a Javascript scope. ExecutionContext is used to
 * provide a context for executing data source and data set event handlers. The
 * Javascript scope is used to evaluate data source and data set binding
 * expressions. If a scope is not provided, data source and data set bindings
 * will not take effect.
 */
public class ModelDteApiAdapter {
	private ExecutionContext context;

	private Scriptable jsScope;

	private DataRequestSession dteSession;

	/**
	 * @deprecated Construct an instance of this class directly
	 */
	public static ModelDteApiAdapter getInstance() {
		return new ModelDteApiAdapter();
	}

	/**
	 * @deprecated use createDataSourceDesign( dataSource )
	 */
	public IBaseDataSourceDesign createDataSourceDesign(DataSourceHandle dataSource, ExecutionContext context)
			throws EngineException {
		try {
			ModelDteApiAdapter tmpAdaptor = new ModelDteApiAdapter(context, null);
			return tmpAdaptor.createDataSourceDesign(dataSource);
		} catch (BirtException e) {
			throw new EngineException(e);
		}
	}

	/**
	 * Default constructor. Constructs an adaptor which uses no associated
	 * Javascript scope and no report context.
	 */
	public ModelDteApiAdapter() {
	}

	/**
	 * Constructs an instance with the given report context and scope
	 * 
	 * @param context Context for event handlers. May be null
	 * @param jsScope Scope for evaluting property binding expressions. If null,
	 *                property bindings have no effect
	 */
	public ModelDteApiAdapter(ExecutionContext context, Scriptable jsScope) {
		this.context = context;
		this.jsScope = jsScope;
	}

	public ModelDteApiAdapter(ExecutionContext context) throws BirtException {
		this.context = context;
		this.jsScope = ((IDataScriptEngine) context.getScriptContext().getScriptEngine(IDataScriptEngine.ENGINE_NAME))
				.getJSScope(context.getScriptContext());
	}

	/**
	 * Adapts the specified Model Data Source to a Data Engine API data source
	 * design object
	 */
	public IBaseDataSourceDesign createDataSourceDesign(DataSourceHandle dataSource) throws BirtException {
		BaseDataSourceDesign datasourceDesign = this.dteSession.getModelAdaptor().adaptDataSource(dataSource);
		IBaseDataSourceEventHandler eventHandler = null;
		if (dataSource instanceof OdaDataSourceHandle)
			eventHandler = new DataSourceScriptExecutor(dataSource, context);
		else if (dataSource instanceof ScriptDataSourceHandle)
			eventHandler = new ScriptDataSourceScriptExecutor((ScriptDataSourceHandle) dataSource, context);
		datasourceDesign.setEventHandler(eventHandler);
		return datasourceDesign;
	}

	/**
	 * Adapts the specified Model Data Set to a Data Engine API data set design
	 * object
	 */
	public IBaseDataSetDesign appendRuntimeInfoToDataSet(DataSetHandle handle, BaseDataSetDesign dataSet)
			throws BirtException {
		if (dataSet instanceof OdaDataSetDesign)
			return newOdaDataSet((OdaDataSetHandle) handle, (OdaDataSetDesign) dataSet, context);

		if (dataSet instanceof ScriptDataSetDesign)
			return newScriptDataSet((ScriptDataSetHandle) handle, (ScriptDataSetDesign) dataSet, context);

		return newGeneralDataSet(handle, dataSet);

	}

	/**
	 * Define data set and data source in DataEngine
	 * 
	 * @param dataSet
	 * @param dteEngine
	 * @throws BirtException
	 */
	public void defineDataSet(DataSetHandle dataSet, DataRequestSession dteSession) throws BirtException {
		if (dataSet == null || dteSession == null)
			return;
		this.dteSession = dteSession;
		DataSourceHandle dataSource = dataSet.getDataSource();
		if (dataSource != null) {
			doDefineDataSource(dataSource);
		}
		IBaseDataSetDesign design = createDataSetDesign(dataSet);
		dteSession.defineDataSet(design);
	}

	public void defineCombinedDataSet(DataSetHandle[] dataSets, DataRequestSession dteSession) throws BirtException {
		if (dataSets == null || dataSets.length < 2 || dteSession == null)
			return;
		this.dteSession = dteSession;
		DataSourceHandle dataSource = dataSets[0].getDataSource();
		if (dataSource != null) {
			doDefineDataSource(dataSource);
		}
		IBaseDataSetDesign masterDesign = createDataSetDesign(dataSets[0]);
		ICombinedOdaDataSetDesign finalDesign = new CombinedOdaDataSetDesign((IOdaDataSetDesign) masterDesign);
		for (int i = 1; i < dataSets.length; i++) {
			IBaseDataSetDesign design = createDataSetDesign(dataSets[i]);
			finalDesign.addDataSetDesign((IOdaDataSetDesign) design);
		}
		dteSession.defineDataSet(finalDesign);
	}

	/**
	 * 
	 * @param dataSource
	 * @throws BirtException
	 */
	private void doDefineDataSource(DataSourceHandle dataSource) throws BirtException {
		dteSession.defineDataSource(createDataSourceDesign(dataSource));
	}

	/**
	 * 
	 * @param dataSet
	 * @throws BirtException
	 */
	private IBaseDataSetDesign createDataSetDesign(DataSetHandle dataSet) throws BirtException {
		Iterator iter = null;
		if (dataSet instanceof JointDataSetHandle) {
			JointDataSetHandle jointDataSet = (JointDataSetHandle) dataSet;
			iter = ((JointDataSetHandle) jointDataSet).dataSetsIterator();
		} else if (dataSet instanceof DerivedDataSetHandle) {
			DerivedDataSetHandle handle = (DerivedDataSetHandle) dataSet;
			iter = handle.getInputDataSets().iterator();
		}
		if (iter != null) {
			while (iter.hasNext()) {
				DataSetHandle childDataSet = (DataSetHandle) iter.next();
				if (childDataSet != null) {
					defineDataSet(childDataSet, dteSession);
				}
			}
		}
		return this.appendRuntimeInfoToDataSet(dataSet, dteSession.getModelAdaptor().adaptDataSet(dataSet));
	}

	/**
	 * Create an IJointDataSetDesign instance.
	 * 
	 * @param handle
	 * @param context2
	 * @return
	 * @throws BirtException
	 */
	private IBaseDataSetDesign newGeneralDataSet(DataSetHandle handle, BaseDataSetDesign dteDataSet)
			throws BirtException {

		IBaseDataSetEventHandler eventHandler = new DataSetScriptExecutor(handle, context);

		dteDataSet.setEventHandler(eventHandler);
		return dteDataSet;
	}

	/**
	 * Evaluates a property binding Javascript expression
	 */
	String evaluatePropertyBindingExpr(String expr) throws BirtException {
		Object result = JavascriptEvalUtil.evaluateScript(null, jsScope, expr,
				org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
		return result == null ? null : result.toString();
	}

	IOdaDataSourceDesign newOdaDataSource(OdaDataSourceHandle source) throws BirtException {
		setResourceIDtoDataSourceHandle(source);
		OdaDataSourceDesign dteSource = new OdaDataSourceDesign(source.getQualifiedName());
		IBaseDataSourceEventHandler eventHandler = new DataSourceScriptExecutor(source, context);

		dteSource.setEventHandler(eventHandler);

		// Adapt base class properties
		adaptBaseDataSource(source, dteSource);

		// Adapt extended data source elements

		// validate that a required attribute is specified
		String driverName = source.getExtensionID();
		if (driverName == null || driverName.length() == 0) {
			throw new EngineException(MessageConstants.EXTENTION_ID_MISSING_ERROR, source.getName()); // $NON-NLS-1$
		}
		dteSource.setExtensionID(driverName);

		// static ROM properties defined by the ODA driver extension
		Map staticProps = getExtensionProperties(source, source.getExtensionPropertyDefinitionList());
		if (staticProps != null && !staticProps.isEmpty()) {
			Iterator entries = staticProps.entrySet().iterator();
			while (entries.hasNext()) {
				Entry entry = (Entry) entries.next();
				String propName = (String) entry.getKey();
				assert (propName != null);

				// If property binding expression exists, use its evaluation
				// result
				Expression expr = source.getPropertyBindingExpression(propName);
				String bindingExpr = getExpressionValue(expr);

				String propValue;
				if (needPropertyBinding() && bindingExpr != null && bindingExpr.length() > 0
						&& context.getDataEngine() instanceof DteDataEngine) {
					propValue = evaluatePropertyBindingExpr(bindingExpr);
				} else {
					propValue = (String) entry.getValue();
				}

				dteSource.addPublicProperty(propName, propValue);
			}
		}

		// private driver properties / private runtime data
		Iterator elmtIter = source.privateDriverPropertiesIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				ExtendedPropertyHandle modelProp = (ExtendedPropertyHandle) elmtIter.next();
				dteSource.addPrivateProperty(modelProp.getName(), modelProp.getValue());
			}
		}

		addPropertyConfigurationId(dteSource, source);

		return dteSource;
	}

	private String getExpressionValue(Expression expr) {
		if (expr == null) {
			return null;
		}
		if (ExpressionType.CONSTANT.equals(expr.getType())) {
			return JavascriptEvalUtil.transformToJsExpression(expr.getStringExpression());
		}
		return expr.getStringExpression();
	}

	/**
	 * Set the ResourceIdentifiers instance to the data source handle
	 * 
	 * @param source
	 */
	private void setResourceIDtoDataSourceHandle(OdaDataSourceHandle source) {
		if (dteSession.getDataSessionContext().getAppContext() == null) {
			dteSession.getDataSessionContext().setAppContext(new HashMap());
		}

		if (!dteSession.getDataSessionContext().getAppContext()
				.containsKey(ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS))
			dteSession.getDataSessionContext().getAppContext().put(
					ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
					createResourceIdentifiers(source.getModuleHandle()));
	}

	/**
	 * This method create a ResourceIdentifiers instance which is in turn being
	 * passed to appContext.
	 * 
	 * The consumer of appContext, especially those Oda drivers, can then use it for
	 * acquire Resource info.
	 * 
	 * @param handle
	 * @return
	 */
	private static ResourceIdentifiers createResourceIdentifiers(ModuleHandle handle) {
		if (handle == null)
			return null;
		try {
			ResourceIdentifiers identifiers = new ResourceIdentifiers();
			if (handle.getSystemId() != null) {
				identifiers.setDesignResourceBaseURI(handle.getSystemId().toURI());
			}
			if (handle.getResourceFolder() != null) {
				identifiers.setApplResourceBaseURI(new File(handle.getResourceFolder()).toURI());
			}
			return identifiers;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Adds the externalized property configuration id for use by a BIRT consumer
	 * application's propertyProvider extension. Use the name not qualified name as
	 * configurationId.
	 */
	private void addPropertyConfigurationId(OdaDataSourceDesign dteSource, OdaDataSourceHandle sourceHandle)
			throws BirtException {
		String configIdValue = dteSource.getExtensionID() + Constants.ODA_PROP_CONFIG_KEY_SEPARATOR
				+ sourceHandle.getName();
		dteSource.addPublicProperty(Constants.ODA_PROP_CONFIGURATION_ID, configIdValue);
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	IScriptDataSourceDesign newScriptDataSource(ScriptDataSourceHandle source) throws BirtException {
		ScriptDataSourceDesign dteSource = new ScriptDataSourceDesign(source.getQualifiedName());
		IScriptDataSourceEventHandler eventHandler = new ScriptDataSourceScriptExecutor(source, context);

		dteSource.setEventHandler(eventHandler);
		// Adapt base class properties
		adaptBaseDataSource(source, dteSource);

		// Adapt script data source elements
		dteSource.setOpenScript(source.getOpen());
		dteSource.setCloseScript(source.getClose());
		return dteSource;
	}

	/**
	 * 
	 * @param source
	 * @param dest
	 */
	void adaptBaseDataSource(DataSourceHandle source, BaseDataSourceDesign dest) {
		dest.setBeforeOpenScript(source.getBeforeOpen());
		dest.setAfterOpenScript(source.getAfterOpen());
		dest.setBeforeCloseScript(source.getBeforeClose());
		dest.setAfterCloseScript(source.getAfterClose());
	}

	IOdaDataSetDesign newOdaDataSet(OdaDataSetHandle modelDataSet, OdaDataSetDesign dteDataSet,
			ExecutionContext context) throws BirtException {
		IBaseDataSetEventHandler eventHandler = new DataSetScriptExecutor(modelDataSet, context);

		dteDataSet.setEventHandler(eventHandler);

		// Set query text; if binding exists, use its result; otherwise
		// use static design
		Expression expression = modelDataSet.getPropertyBindingExpression(OdaDataSet.QUERY_TEXT_PROP);
		String queryTextBinding = getExpressionValue(expression);

		if (needPropertyBinding() && queryTextBinding != null && queryTextBinding.length() > 0
				&& context.getDataEngine() instanceof DteDataEngine) {
			dteDataSet.setQueryText(evaluatePropertyBindingExpr(queryTextBinding));
		}

		// static ROM properties defined by the ODA driver extension
		Map staticProps = getExtensionProperties(modelDataSet, modelDataSet.getExtensionPropertyDefinitionList());
		if (staticProps != null && !staticProps.isEmpty()) {
			Iterator propNamesItr = staticProps.keySet().iterator();
			while (propNamesItr.hasNext()) {
				String propName = (String) propNamesItr.next();
				assert (propName != null);

				Expression expr = modelDataSet.getPropertyBindingExpression(propName);
				String bindingExpr = getExpressionValue(expr);

				if (needPropertyBinding() && bindingExpr != null && bindingExpr.length() > 0) {
					String propValue = this.evaluatePropertyBindingExpr(bindingExpr);
					dteDataSet.addPublicProperty((String) propName, propValue);
				}

			}
		}
		return dteDataSet;
	}

	IScriptDataSetDesign newScriptDataSet(ScriptDataSetHandle modelDataSet, ScriptDataSetDesign dteDataSet,
			ExecutionContext context) throws BirtException {
		IScriptDataSetEventHandler eventHandler = new ScriptDataSetScriptExecutor(modelDataSet, context);

		dteDataSet.setEventHandler(eventHandler);

		return dteDataSet;
	}

	/*
	 * void adaptBaseDataSet( DataSetHandle modelDataSet, BaseDataSetDesign
	 * dteDataSet ) throws BirtException { if ( (!(modelDataSet instanceof
	 * JointDataSetHandle)) && modelDataSet.getDataSource( ) == null ) throw new
	 * EngineException( MessageConstants.DATA_SOURCE_ERROR );
	 * 
	 * if ( !( modelDataSet instanceof JointDataSetHandle ) ) {
	 * dteDataSet.setDataSource( modelDataSet.getDataSource( ) .getQualifiedName( )
	 * ); dteDataSet.setBeforeOpenScript( modelDataSet.getBeforeOpen( ) );
	 * dteDataSet.setAfterOpenScript( modelDataSet.getAfterOpen( ) );
	 * dteDataSet.setOnFetchScript( modelDataSet.getOnFetch( ) );
	 * dteDataSet.setBeforeCloseScript( modelDataSet.getBeforeClose( ) );
	 * dteDataSet.setAfterCloseScript( modelDataSet.getAfterClose( ) ); //The cache
	 * row count setting is no longer valid. //dteDataSet.setCacheRowCount(
	 * modelDataSet.getCachedRowCount( ) );
	 * 
	 * } populateParameter( modelDataSet, dteDataSet );
	 * 
	 * populateComputedColumn( modelDataSet, dteDataSet );
	 * 
	 * populateFilter( modelDataSet, dteDataSet );
	 * 
	 * dteDataSet.setRowFetchLimit( modelDataSet.getRowFetchLimit( ) );
	 * 
	 * mergeHints( modelDataSet, dteDataSet );
	 * 
	 * }
	 */

	/**
	 * 
	 * @param modelDataSet
	 * @param dteDataSet
	 * @return
	 */
	private Iterator populateParameter(DataSetHandle modelDataSet, BaseDataSetDesign dteDataSet) throws BirtException {
		// dataset parameters definition
		HashMap paramBindingCandidates = new HashMap();

		Iterator elmtIter = modelDataSet.parametersIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				DataSetParameterHandle modelParam = (DataSetParameterHandle) elmtIter.next();
				// collect input parameter default values as
				// potential parameter binding if no explicit ones are
				// defined for a parameter
				if (modelParam.isInput()) {
					String defaultValueExpr = null;
					if (modelParam instanceof OdaDataSetParameterHandle) {
						String linkedReportParam = ((OdaDataSetParameterHandle) modelParam).getParamName();
						if (linkedReportParam != null) {
							ParameterHandle ph = modelDataSet.getModuleHandle().findParameter(linkedReportParam);
							if (ph instanceof ScalarParameterHandle) {
								if (((ScalarParameterHandle) ph).getParamType()
										.equals(DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE)) {
									throw new DataException(ResourceConstants.Linked_REPORT_PARAM_ALLOW_MULTI_VALUES,
											new String[] { linkedReportParam, modelParam.getName() });
								}
							}
							defaultValueExpr = ExpressionUtil.createJSParameterExpression(
									((OdaDataSetParameterHandle) modelParam).getParamName());
						} else {
							defaultValueExpr = getExpressionDefaultValue(modelParam);
						}
					} else {
						defaultValueExpr = getExpressionDefaultValue(modelParam);
					}
					dteDataSet.addParameter(newParam(modelParam));
					paramBindingCandidates.put(modelParam.getName(), new ScriptExpression(defaultValueExpr,
							DataAdapterUtil.modelDataTypeToCoreDataType(modelParam.getDataType())));
				} else {
					dteDataSet.addParameter(newParam(modelParam));
				}
			}
		}

		// input parameter bindings
		elmtIter = modelDataSet.paramBindingsIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				ParamBindingHandle modelParamBinding = (ParamBindingHandle) elmtIter.next();
				// replace default value of the same parameter, if defined
				paramBindingCandidates.put(modelParamBinding.getParamName(),
						new ScriptExpression(modelParamBinding.getExpression()));
			}
		}

		// assign merged parameter bindings to the data set
		if (paramBindingCandidates.size() > 0) {
			elmtIter = paramBindingCandidates.keySet().iterator();
			while (elmtIter.hasNext()) {
				Object paramName = elmtIter.next();
				assert (paramName != null && paramName instanceof String);
				ScriptExpression expression = (ScriptExpression) paramBindingCandidates.get(paramName);
				dteDataSet.addInputParamBinding(newInputParamBinding((String) paramName, expression));
			}
		}
		return elmtIter;
	}

	private String getExpressionDefaultValue(DataSetParameterHandle modelParam) {
		if (ExpressionType.CONSTANT
				.equals(modelParam.getExpressionProperty(DataSetParameter.DEFAULT_VALUE_MEMBER).getType())) {
			return JavascriptEvalUtil.transformToJsExpression(modelParam.getDefaultValue());
		}

		return modelParam.getDefaultValue();
	}

	/**
	 * 
	 * @param modelDataSet
	 * @param dteDataSet
	 * @throws EngineException
	 */
	private void populateComputedColumn(DataSetHandle modelDataSet, BaseDataSetDesign dteDataSet)
			throws EngineException {
		// computed columns
		Iterator elmtIter = modelDataSet.computedColumnsIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				ComputedColumnHandle modelCmptdColumn = (ComputedColumnHandle) elmtIter.next();
				IComputedColumn dteCmptdColumn = newComputedColumn(modelCmptdColumn);
				dteDataSet.addComputedColumn(dteCmptdColumn);
			}
		}
	}

	/**
	 * 
	 * @param modelDataSet
	 * @param dteDataSet
	 */
	private void populateFilter(DataSetHandle modelDataSet, BaseDataSetDesign dteDataSet) {
		// filter conditions
		Iterator elmtIter = modelDataSet.filtersIterator();
		if (elmtIter != null) {
			while (elmtIter.hasNext()) {
				FilterConditionHandle modelFilter = (FilterConditionHandle) elmtIter.next();
				IFilterDefinition dteFilter = newFilter(modelFilter);
				dteDataSet.addFilter(dteFilter);
			}
		}
	}

	/**
	 */
	/*
	 * private void mergeHints( DataSetHandle modelDataSet, BaseDataSetDesign
	 * dteDataSet ) { // merge ResultSetHints and ColumnHints, the order is
	 * important. // ResultSetHints will give each column a unique name, and //
	 * column hints should base on the result of ResultSet hint. // So in
	 * ResultSetHint list, the order of items should be // ResultSetColumn and then
	 * ColumnHint.
	 * 
	 * // now merge model's result set column info into existing columnDefn // with
	 * same column name, otherwise create new columnDefn // based on the model's
	 * result set column Iterator elmtIter = null; if ( modelDataSet instanceof
	 * OdaDataSetHandle ) { elmtIter = modelDataSet.resultSetIterator( ); if (
	 * elmtIter != null ) { while ( elmtIter.hasNext( ) ) { OdaResultSetColumnHandle
	 * modelColumn = (OdaResultSetColumnHandle) elmtIter.next( ); if (
	 * !modelColumn.getColumnName( ) .equals( modelColumn.getNativeName( ) ) )
	 * dteDataSet.addResultSetHint( newColumnDefn( (ResultSetColumnHandle)
	 * modelColumn ) ); } } }
	 * 
	 * elmtIter = modelDataSet.resultSetHintsIterator( ); if ( elmtIter != null ) {
	 * while ( elmtIter.hasNext( ) ) { ResultSetColumnHandle modelColumn =
	 * (ResultSetColumnHandle) elmtIter.next( ); dteDataSet.addResultSetHint(
	 * newColumnDefn( modelColumn ) ); } }
	 * 
	 * // merging result set column and column hints into DtE columnDefn; // first
	 * create new columnDefn based on model's column hints elmtIter =
	 * modelDataSet.columnHintsIterator( ); if ( elmtIter != null ) { List
	 * columnDefns = dteDataSet.getResultSetHints( ); while ( elmtIter.hasNext( ) )
	 * { ColumnHintHandle modelColumnHint = ( ColumnHintHandle ) elmtIter .next( );
	 * ColumnDefinition existDefn = findColumnDefn( columnDefns,
	 * modelColumnHint.getColumnName( ) ); if ( existDefn != null )
	 * updateColumnDefn( existDefn, modelColumnHint ); else dteDataSet
	 * .addResultSetHint( newColumnDefn( modelColumnHint ) ); } } }
	 */
	/**
	 * Creates a new DtE API IParameterDefinition from a model's
	 * DataSetParameterHandle.
	 */
	IParameterDefinition newParam(DataSetParameterHandle modelParam) {
		ParameterDefinition dteParam = new ParameterDefinition();

		dteParam.setName(modelParam.getName());
		if (modelParam.getPosition() != null)
			dteParam.setPosition(modelParam.getPosition().intValue());
		if (modelParam.getNativeDataType() != null)
			dteParam.setNativeType(modelParam.getNativeDataType().intValue());

		if (modelParam instanceof OdaDataSetParameterHandle) {
			dteParam.setNativeName(((OdaDataSetParameterHandle) modelParam).getNativeName());
		}

		dteParam.setType(DataAdapterUtil.adaptModelDataType(modelParam.getDataType()));
		dteParam.setInputMode(modelParam.isInput());
		dteParam.setOutputMode(modelParam.isOutput());
		dteParam.setNullable(modelParam.allowNull());
		dteParam.setInputOptional(modelParam.isOptional());
		dteParam.setDefaultInputValue(modelParam.getDefaultValue());

		return dteParam;
	}

	/**
	 * Creates a new DtE API InputParamBinding from a model's binding. Could return
	 * null if no expression is bound.
	 */
	IInputParameterBinding newInputParamBinding(ParamBindingHandle modelInputParamBndg) {
		// model provides binding by name only
		return newInputParamBinding(modelInputParamBndg.getParamName(),
				new ScriptExpression(modelInputParamBndg.getExpression()));
	}

	private IInputParameterBinding newInputParamBinding(String paramName, ScriptExpression paramValueExpr) {
		if (paramValueExpr == null || paramValueExpr.getText() == null)
			return null;
		return new InputParameterBinding(paramName, paramValueExpr);
	}

	/**
	 * Creates a new DtE API Computed Column from a model computed column. Could
	 * return null if no expression is defined.
	 * 
	 * @throws EngineException
	 */
	IComputedColumn newComputedColumn(ComputedColumnHandle modelCmptdColumn) throws EngineException {
		// no expression to define a computed column
		if (modelCmptdColumn.getExpression() == null && modelCmptdColumn.getAggregateFunction() == null) {
			throw new EngineException(MessageConstants.MISSING_COMPUTED_COLUMN_EXPRESSION_EXCEPTION,
					modelCmptdColumn.getName());
		}

		Map argumentList = new HashMap();
		Iterator argumentIter = modelCmptdColumn.argumentsIterator();
		while (argumentIter.hasNext()) {
			AggregationArgumentHandle handle = (AggregationArgumentHandle) argumentIter.next();
			argumentList.put(handle.getName(), new ScriptExpression(handle.getValue()));
		}

		List orderedArgument = new ArrayList();
		try {
			if (modelCmptdColumn.getAggregateFunction() != null) {
				IAggrFunction info = AggregationManager.getInstance()
						.getAggregation(modelCmptdColumn.getAggregateFunction());
				if (info != null) {
					IParameterDefn[] parameters = info.getParameterDefn();

					if (parameters != null) {
						for (int i = 0; i < parameters.length; i++) {
							IParameterDefn pInfo = parameters[i];
							if (argumentList.get(pInfo.getName()) != null) {
								orderedArgument.add(argumentList.get(pInfo.getName()));
							}
						}
					}
				}
			}
		} catch (DataException e) {
			throw new EngineException(e.getLocalizedMessage(), e);
		}

		return new ComputedColumn(modelCmptdColumn.getName(), modelCmptdColumn.getExpression(),
				toDteDataType(modelCmptdColumn.getDataType()), modelCmptdColumn.getAggregateFunction(),
				modelCmptdColumn.getFilterExpression() == null ? null
						: new ScriptExpression(modelCmptdColumn.getFilterExpression()),
				orderedArgument);
	}

	/**
	 * Creates a new DtE API IJSExprFilter or IColumnFilter from a model's filter
	 * condition. Could return null if no expression nor column operator is defined.
	 */
	IFilterDefinition newFilter(FilterConditionHandle modelFilter) {
		String filterExpr = modelFilter.getExpr();
		if (filterExpr == null || filterExpr.length() == 0)
			return null; // no filter defined

		// converts to DtE exprFilter if there is no operator
		String filterOpr = modelFilter.getOperator();
		if (filterOpr == null || filterOpr.length() == 0)
			return new FilterDefinition(new ScriptExpression(filterExpr));

		/*
		 * has operator defined, try to convert filter condition to operator/operand
		 * style column filter with 0 to 2 operands
		 */

		String column = filterExpr;
		int dteOpr = toDteFilterOperator(filterOpr);
		if (dteOpr == IConditionalExpression.OP_IN || dteOpr == IConditionalExpression.OP_NOT_IN) {
			List operands = modelFilter.getValue1List();
			return new FilterDefinition(new ConditionalExpression(column, dteOpr, operands));
		} else {
			String operand1 = modelFilter.getValue1();
			String operand2 = modelFilter.getValue2();

			return new FilterDefinition(new ConditionalExpression(column, dteOpr, operand1, operand2));
		}
	}

	private IColumnDefinition newColumnDefn(ResultSetColumnHandle modelColumn) {
		ColumnDefinition newColumn = new ColumnDefinition(modelColumn.getColumnName());
		if (modelColumn.getPosition() != null)
			newColumn.setColumnPosition(modelColumn.getPosition().intValue());
		if (modelColumn.getNativeDataType() != null)
			newColumn.setNativeDataType(modelColumn.getNativeDataType().intValue());
		newColumn.setDataType(toDteDataType(modelColumn.getDataType()));
		return newColumn;
	}

	private void updateColumnDefn(ColumnDefinition dteColumn, ColumnHintHandle modelColumnHint) {
		assert dteColumn.getColumnName().equals(modelColumnHint.getColumnName());
		dteColumn.setAlias(modelColumnHint.getAlias());

		String exportConstant = modelColumnHint.getExport();
		if (exportConstant != null) {
			int exportHint = IColumnDefinition.DONOT_EXPORT; // default value
			if (exportConstant.equals(DesignChoiceConstants.EXPORT_TYPE_IF_REALIZED))
				exportHint = IColumnDefinition.EXPORT_IF_REALIZED;
			else if (exportConstant.equals(DesignChoiceConstants.EXPORT_TYPE_ALWAYS))
				exportHint = IColumnDefinition.ALWAYS_EXPORT;
			else
				assert exportConstant.equals(DesignChoiceConstants.EXPORT_TYPE_NONE);

			dteColumn.setExportHint(exportHint);
		}

		String searchConstant = modelColumnHint.getSearching();
		if (searchConstant != null) {
			int searchHint = IColumnDefinition.NOT_SEARCHABLE;
			if (searchConstant.equals(DesignChoiceConstants.SEARCH_TYPE_INDEXED))
				searchHint = IColumnDefinition.SEARCHABLE_IF_INDEXED;
			else if (searchConstant.equals(DesignChoiceConstants.SEARCH_TYPE_ANY))
				searchHint = IColumnDefinition.ALWAYS_SEARCHABLE;
			else
				assert searchConstant.equals(DesignChoiceConstants.SEARCH_TYPE_NONE);

			dteColumn.setSearchHint(searchHint);
		}

	}

	private IColumnDefinition newColumnDefn(ColumnHintHandle modelColumnHint) {
		ColumnDefinition newColumn = new ColumnDefinition(modelColumnHint.getColumnName());
		updateColumnDefn(newColumn, modelColumnHint);
		return newColumn;
	}

	/**
	 * Find the DtE columnDefn from the given list of columnDefns that matches the
	 * given columnName.
	 */
	private ColumnDefinition findColumnDefn(List columnDefns, String columnName) {
		assert columnName != null;
		if (columnDefns == null)
			return null; // no list to find from
		Iterator iter = columnDefns.iterator();
		if (iter == null)
			return null;

		// iterate thru each columnDefn, and looks for a match of
		// specified column name
		while (iter.hasNext()) {
			ColumnDefinition column = (ColumnDefinition) iter.next();
			if (columnName.equals(column.getColumnName()))
				return column;
		}
		return null;
	}

	public static int toDteDataType(String modelDataType) {
		return DataAdapterUtil.adaptModelDataType(modelDataType);
	}

	// Convert model operator value to DtE IColumnFilter enum value
	public static int toDteFilterOperator(String modelOpr) {
		return DataAdapterUtil.adaptModelFilterOperator(modelOpr);
	}

	/*
	 * Gets the data handle's static ROM extension properties name and value pairs
	 * in String values and returns them in a Map
	 */
	private Map getExtensionProperties(ReportElementHandle dataHandle, List driverPropList) {
		if (driverPropList == null || driverPropList.isEmpty())
			return null; // nothing to add

		Map properties = new HashMap();
		Iterator elmtIter = driverPropList.iterator();
		while (elmtIter.hasNext()) {
			IPropertyDefn modelExtProp = (IPropertyDefn) elmtIter.next();

			// First get extension property's name
			String propName = modelExtProp.getName();
			assert (propName != null && propName.length() > 0);

			// Use property name to get property value
			Object propValueObj = dataHandle.getProperty(modelExtProp.getName());

			/*
			 * An ODA consumer does not distinguish whether a property value is not set or
			 * explicitly set to null. Its handling is pushed down to the underlying data
			 * provider.
			 */
			String propValue = (propValueObj == null) ? null : propValueObj.toString();
			properties.put(propName, propValue);
		}

		return properties;
	}

	/**
	 * temp method to decide whether need property binding
	 * 
	 * @return
	 */
	private boolean needPropertyBinding() {
		if (this.context == null || this.jsScope == null)
			return false;
		else
			return true;
	}
}
