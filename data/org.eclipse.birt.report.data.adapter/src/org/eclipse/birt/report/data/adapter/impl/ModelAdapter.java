/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.impl;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.LinkedDataSetUtil;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IBuildInBaseTimeFunction;
import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.internal.adapter.ColumnAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ComputedColumnAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ConditionAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ExpressionAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.FilterAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.GroupAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.InputParamBindingAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.JointDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.OdaDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.OdaDataSourceAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ParameterAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ScriptDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ScriptDataSourceAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.SortAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.SortHintAdapter;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.CalculationArgumentHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SortHintHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.mozilla.javascript.Scriptable;

/**
 * An adaptor to create Data Engine request objects based on Model element
 * definitions
 */
public class ModelAdapter implements IModelAdapter {
	protected static Logger logger = Logger.getLogger(ModelAdapter.class.getName());
	DataSessionContext context;

	public ModelAdapter(DataSessionContext context) {
		this.context = context;
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSource(org.eclipse.birt.report.model.api.DataSourceHandle)
	 */
	public BaseDataSourceDesign adaptDataSource(DataSourceHandle handle) throws BirtException {
		if (handle instanceof OdaDataSourceHandle) {
			// If an external top level scope is available (i.e., our
			// consumer
			// is the report engine), use it to resolve property bindings.
			// Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope() ? context.getTopScope() : null;
			return new OdaDataSourceAdapter((OdaDataSourceHandle) handle, propBindingScope,
					context.getDataEngineContext(), this);
		}

		if (handle instanceof ScriptDataSourceHandle) {
			return new ScriptDataSourceAdapter((ScriptDataSourceHandle) handle, context);
		}

		logger.fine("handle type: " + (handle == null ? "" : handle.getClass().getName())); //$NON-NLS-1$
		return null;

	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSet(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public BaseDataSetDesign adaptDataSet(DataSetHandle handle) throws BirtException {
		BaseDataSetDesign design = null;
		if (handle instanceof OdaDataSetHandle) {
			// If an external top level scope is available (i.e., our
			// consumer
			// is the report engine), use it to resolve property bindings.
			// Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope() ? context.getTopScope() : null;
			design = new OdaDataSetAdapter((OdaDataSetHandle) handle, propBindingScope, this,
					context.getDataEngineContext());
		}

		if (handle instanceof ScriptDataSetHandle)
			design = new ScriptDataSetAdapter((ScriptDataSetHandle) handle, this);

		if (handle instanceof JointDataSetHandle)
			design = new JointDataSetAdapter((JointDataSetHandle) handle, this);

		if (design != null) {
			if (handle.getACLExpression() != null)
				design.setDataSetACL(this.adaptExpression((Expression) handle.getACLExpression().getValue()));
			if (handle.getRowACLExpression() != null)
				design.setRowACL(this.adaptExpression((Expression) handle.getRowACLExpression().getValue()));

			Iterator columnHintIterator = handle.columnHintsIterator();
			while (columnHintIterator.hasNext()) {
				ColumnHintHandle ch = (ColumnHintHandle) columnHintIterator.next();
				design.setDataSetColumnACL(ch.getColumnName(),
						this.adaptExpression((Expression) ch.getACLExpression().getValue()));
			}

		}

		if (design != null) {
			design.setCompareLocale(handle.getLocale());
			design.setNullsOrdering(handle.getNullsOrdering());
			design.setNeedCache(handle.needsCache());
		}
		logger.fine("handle type: " + (handle == null ? "" : handle.getClass().getName())); //$NON-NLS-1$
		return design;

	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptConditionalExpression(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public ConditionalExpression adaptConditionalExpression(String mainExpr, String operator, String operand1,
			String operand2) {
		return new ConditionAdapter(mainExpr, operator, operand1, operand2);
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptExpression(java.lang.String,
	 *      java.lang.String)
	 */
	public ScriptExpression adaptExpression(Expression expr, String dataType) {
		if (expr == null || expr.getStringExpression() == null)
			return null;
		ScriptExpression jsExpr = new ExpressionAdapter(expr, dataType);
		if (ExpressionType.CONSTANT.equals(expr.getType())) {
			jsExpr = new ScriptExpression(expr.getStringExpression());
			jsExpr.setScriptId(BaseExpression.constantId);
			jsExpr.setHandle(expr.getExpression());
		}
		return jsExpr;
	}

	/*	*//**
			 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptExpression(org.eclipse.birt.report.model.api.ComputedColumnHandle)
			 *//*
				 * public ScriptExpression adaptExpression( ComputedColumnHandle ccHandle ) {
				 * return new ExpressionAdapter( ccHandle ); }
				 */

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptFilter(org.eclipse.birt.report.model.api.FilterConditionHandle)
	 */
	public FilterDefinition adaptFilter(FilterConditionHandle modelFilter) {
		try {
			return new FilterAdapter(this, modelFilter);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	public SortDefinition adaptSortHint(SortHintHandle sortHint) {
		try {
			return new SortHintAdapter(this, sortHint);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptGroup(org.eclipse.birt.report.model.api.GroupHandle)
	 */
	public GroupDefinition adaptGroup(GroupHandle groupHandle) {
		try {
			return new GroupAdapter(this, groupHandle);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(org.eclipse.birt.report.model.api.SortKeyHandle)
	 */
	public SortDefinition adaptSort(SortKeyHandle sortHandle) {
		try {
			return new SortAdapter(this, sortHandle);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(java.lang.String,
	 *      java.lang.String)
	 */
	public SortDefinition adaptSort(Expression expr, String direction) {
		try {
			return new SortAdapter(this, expr, direction);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptParameter(org.eclipse.birt.report.model.api.DataSetParameterHandle)
	 */
	public ParameterDefinition adaptParameter(DataSetParameterHandle paramHandle) {
		return new ParameterAdapter(paramHandle);
	}

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptInputParamBinding(org.eclipse.birt.report.model.api.ParamBindingHandle)
	 */
	public InputParameterBinding adaptInputParamBinding(ParamBindingHandle modelHandle) {
		try {
			return new InputParamBindingAdapter(this, modelHandle);
		} catch (AdapterException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#ColumnAdaptor(org.eclipse.birt.report.model.api.ResultSetColumnHandle)
	 */
	public ColumnDefinition ColumnAdaptor(ResultSetColumnHandle modelColumn) {
		return new ColumnAdapter(modelColumn);
	}

	/**
	 * @throws AdapterException
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptComputedColumn(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public ComputedColumn adaptComputedColumn(ComputedColumnHandle modelHandle) throws AdapterException {
		return new ComputedColumnAdapter(this, modelHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptBinding(org.
	 * eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public IBinding adaptBinding(ComputedColumnHandle handle) throws AdapterException {
		if (handle == null)
			return null;
		Binding result = new Binding(handle.getName());
		try {

			if (handle.getExpression() != null) {
				ScriptExpression expr = this.adaptExpression((Expression) handle
						.getExpressionProperty(
								org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.EXPRESSION_MEMBER)
						.getValue(), handle.getDataType());
				expr.setGroupName(handle.getAggregateOn());
				result.setExpression(expr);
			}
			result.setDisplayName(handle.getExternalizedValue(
					org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_ID_MEMBER,
					org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_MEMBER,
					this.context.getDataEngineContext().getLocale()));
			result.setTimeFunction(adaptTimeFunction(handle));
			result.setDataType(
					org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(handle.getDataType()));
			result.setAggrFunction(org.eclipse.birt.report.data.adapter.api.DataAdapterUtil
					.adaptModelAggregationType(handle.getAggregateFunction()));
			result.setFilter(
					handle.getFilterExpression() == null ? null
							: this.adaptExpression((Expression) handle.getExpressionProperty(
									org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.FILTER_MEMBER)
									.getValue(), DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN));
			result.setExportable(handle.allowExport());

			populateAggregateOns(result, handle);

		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return null;
		}

		populateArgument(result, handle);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptBinding(org.
	 * eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public IBinding adaptBinding(ComputedColumnHandle handle, ExpressionLocation el) throws AdapterException {
		if (el.equals(ExpressionLocation.TABLE)) {
			return this.adaptBinding(handle);
		} else {
			Binding binding = null;
			try {
				binding = new Binding(handle.getName());
				binding.setAggrFunction(handle.getAggregateFunction() == null ? null
						: DataAdapterUtil.adaptModelAggregationType(handle.getAggregateFunction()));
				binding.setDisplayName(handle.getExternalizedValue(
						org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_ID_MEMBER,
						org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.DISPLAY_NAME_MEMBER,
						this.context.getDataEngineContext().getLocale()));
				binding.setExportable(handle.allowExport());

				ExpressionLocation location = el;
				if (handle.getElementHandle() instanceof ReportItemHandle
						&& LinkedDataSetUtil.bindToLinkedDataSet(((ReportItemHandle) handle.getElementHandle()))) {
					location = ExpressionLocation.TABLE;
				}

				binding.setExpression(adaptExpression((Expression) handle
						.getExpressionProperty(
								org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.EXPRESSION_MEMBER)
						.getValue(), location));
				if (handle.getFilterExpression() != null) {
					binding.setFilter(adaptExpression((Expression) handle
							.getExpressionProperty(
									org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.FILTER_MEMBER)
							.getValue(), location));
				}

				binding.setDataType(DataAdapterUtil.adaptModelDataType(handle.getDataType()));

				for (Iterator argItr = handle.argumentsIterator(); argItr.hasNext();) {
					AggregationArgumentHandle aah = (AggregationArgumentHandle) argItr.next();

					binding.addArgument(aah.getName(),
							adaptExpression(
									(Expression) aah.getExpressionProperty(AggregationArgument.VALUE_MEMBER).getValue(),
									location));
				}

			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				binding.setTimeFunction(adaptTimeFunction(handle));
			} catch (BirtException ex) {
				throw new AdapterException(ex.getLocalizedMessage());
			}
			return binding;
		}
	}

	public ITimeFunction adaptTimeFunction(ComputedColumnHandle handle) throws DataException, BirtException {
		if (handle.getCalculationType() == null || handle.getCalculationType().trim().length() == 0)
			return null;
		TimeFunction timeFunction = new TimeFunction();
		Object referenceDate = null;
		if (DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY.equals(handle.getReferenceDateType())) {
			referenceDate = ScriptEvalUtil.evalExpr(new ScriptExpression("new java.util.Date()"),
					this.context.getDataEngineContext().getScriptContext(),
					org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
		} else if (DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE.equals(handle.getReferenceDateType())) {
			IBaseExpression sciptExpr = this.adaptExpression((Expression) (handle.getReferenceDateValue().getValue()));
			referenceDate = ScriptEvalUtil.evalExpr(sciptExpr, this.context.getDataEngineContext().getScriptContext(),
					org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
		}
		timeFunction.setReferenceDate(new ReferenceDate(DataTypeUtil.toDate(referenceDate)));
		timeFunction.setTimeDimension(handle.getTimeDimension());
		timeFunction.setBaseTimePeriod(populateBaseTimePeriod(handle));
		timeFunction.setRelativeTimePeriod(populateRelativeTimePeriod(handle));
		return timeFunction;
	}

	private ITimePeriod populateBaseTimePeriod(ComputedColumnHandle periodHandle) throws DataException, BirtException {
		String calculateType = periodHandle.getCalculationType();
		TimePeriod baseTimePeriod = null;
		Object expression = null;
		if (IBuildInBaseTimeFunction.CURRENT_QUARTER.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.QUARTER, true);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.YEAR, true);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_QUARTER.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.QUARTER, true);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_MONTH.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.MONTH, true);
		} else if (IBuildInBaseTimeFunction.CURRENT_MONTH.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.MONTH, true);
		} else if (IBuildInBaseTimeFunction.TRAILING_30_DAYS.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(-30, TimePeriodType.DAY);
		} else if (IBuildInBaseTimeFunction.TRAILING_60_DAYS.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(-60, TimePeriodType.DAY);
		} else if (IBuildInBaseTimeFunction.TRAILING_90_DAYS.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(-90, TimePeriodType.DAY);
		} else if (IBuildInBaseTimeFunction.TRAILING_120_DAYS.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(-120, TimePeriodType.DAY);
		} else if (IBuildInBaseTimeFunction.TRAILING_12_MONTHS.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(-12, TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.YEAR_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		} else if (IBuildInBaseTimeFunction.QUARTER_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.QUARTER);
		} else if (IBuildInBaseTimeFunction.MONTH_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.CURRENT_YEAR.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.YEAR, true);
		} else if (IBuildInBaseTimeFunction.WEEK_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.WEEK);
		} else if (IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.WEEK);
		} else if (IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.QUARTER);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.WEEK);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.QUARTER);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE.equals(calculateType)) {
			baseTimePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		} else if (IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals(calculateType)
				|| IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String period1 = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.PERIOD_1.equals(argument.getName())) {
					period1 = argument.getValue().getStringExpression();
					break;
				}
			}
			if (IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals(calculateType)) {
				baseTimePeriod = new TimePeriod(0, DataAdapterUtil.toTimePeriodType(period1), true);
			} else {
				baseTimePeriod = new TimePeriod(0, DataAdapterUtil.toTimePeriodType(period1));
			}
		} else if (IBuildInBaseTimeFunction.TRAILING_N_MONTHS.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
				}
			}
			baseTimePeriod = new TimePeriod(0 - evaluatePeriodsNForTrailingFunction(n, expression),
					TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.TRAILING_N_DAYS.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
				}
			}
			baseTimePeriod = new TimePeriod(0 - evaluatePeriodsNForTrailingFunction(n, expression), TimePeriodType.DAY);
		} else if (IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String period1 = null, n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.PERIOD_1.equals(argument.getName())) {
					period1 = argument.getValue().getStringExpression();
				}
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
				}
			}
			baseTimePeriod = new TimePeriod(0 - evaluatePeriodsNForTrailingFunction(n, expression),
					DataAdapterUtil.toTimePeriodType(period1));
		} else if (IBuildInBaseTimeFunction.NEXT_N_PERIODS.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null, period1 = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.PERIOD_1.equals(argument.getName())) {
					period1 = argument.getValue().getStringExpression();
				}
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
				}
			}
			baseTimePeriod = new TimePeriod(evaluatePeriodsNForTrailingFunction(n, expression),
					DataAdapterUtil.toTimePeriodType(period1));
		}
		return baseTimePeriod;
	}

	/**
	 * 
	 * @param periodHandle
	 * @return
	 * @throws BirtException
	 * @throws DataException
	 */
	private ITimePeriod populateRelativeTimePeriod(ComputedColumnHandle periodHandle)
			throws DataException, BirtException {
		String calculateType = periodHandle.getCalculationType();
		TimePeriod relativeTimePeriod = null;
		Object expression = null;
		if (IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR.equals(calculateType)
				|| IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR.equals(calculateType)
				|| IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
					break;
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression), TimePeriodType.YEAR);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
					break;
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression), TimePeriodType.WEEK);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE.equals(calculateType)
				|| IBuildInBaseTimeFunction.PREVIOUS_MONTH.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
					break;
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression), TimePeriodType.MONTH);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE.equals(calculateType)
				|| IBuildInBaseTimeFunction.PREVIOUS_QUARTER.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
					break;
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression), TimePeriodType.QUARTER);
		} else if (IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE.equals(calculateType)
				|| IBuildInBaseTimeFunction.PREVIOUS_YEAR.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.N_PERIOD1.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
					break;
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression), TimePeriodType.YEAR);
		} else if (IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.equals(calculateType)
				|| IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.equals(calculateType)
				|| IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.equals(calculateType)) {
			Iterator iter = periodHandle.calculationArgumentsIterator();
			String period2 = null, n = null;
			while (iter.hasNext()) {
				CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next();
				if (IArgumentInfo.PERIOD_2.equals(argument.getName())) {
					period2 = argument.getValue().getStringExpression();
				}
				if (IArgumentInfo.N_PERIOD2.equals(argument.getName())) {
					n = argument.getValue().getStringExpression();
					expression = argument.getValue().getValue();
				}
			}
			relativeTimePeriod = new TimePeriod(0 - evaluatePeriodsN(n, expression),
					DataAdapterUtil.toTimePeriodType(period2));
		}
		return relativeTimePeriod;
	}

	private int evaluatePeriodsN(String n, Object expression) throws DataException, BirtException {
		int num = 0;
		if (n == null || n.trim().equals("")) {
			n = "0";
		}
		try {
			num = Integer.valueOf(n);
		} catch (Exception e) {
			ScriptExpression scriptExpression = null;
			if (expression instanceof Expression) {
				scriptExpression = this.adaptExpression((Expression) expression);
				;
			} else {
				scriptExpression = adaptExpression(n, DesignChoiceConstants.PARAM_TYPE_ANY);
			}
			num = (Integer) ScriptEvalUtil.evalExpr(scriptExpression,
					this.context.getDataEngineContext().getScriptContext(),
					org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
		}
		return num;
	}

	private int evaluatePeriodsNForTrailingFunction(String n, Object expression) throws DataException, BirtException {
		if (n == null || n.trim().equals("")) {
			return 1;
		}
		return evaluatePeriodsN(n, expression);
	}

	/**
	 * 
	 * @param handle
	 * @param result
	 * @throws AdapterException
	 */
	private void populateAggregateOns(IBinding result, ComputedColumnHandle handle) throws AdapterException {
		List aggrOns = handle.getAggregateOnList();
		if (aggrOns == null)
			return;
		for (int i = 0; i < aggrOns.size(); i++) {
			try {
				result.addAggregateOn(aggrOns.get(i).toString());
			} catch (DataException e) {
				throw new AdapterException(e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @param binding
	 * @param modelCmptdColumn
	 * @throws AdapterException
	 */
	private void populateArgument(IBinding binding, ComputedColumnHandle modelCmptdColumn) throws AdapterException {

		Iterator it = modelCmptdColumn.argumentsIterator();
		while (it != null && it.hasNext()) {
			AggregationArgumentHandle arg = (AggregationArgumentHandle) it.next();
			try {
				Expression expr = (Expression) arg.getExpressionProperty(AggregationArgument.VALUE_MEMBER).getValue();
				ScriptExpression scriptExpression = this.adaptExpression(expr);
				if (expr != null && !ExpressionType.JAVASCRIPT.equals(expr.getType()) && scriptExpression == null)
					throw new AdapterException(Message.formatMessage(ResourceConstants.INVALID_BINDING_EXPRESSION,
							new Object[] { expr.getStringExpression() }));
				else
					binding.addArgument(scriptExpression);
			} catch (DataException e) {
				throw new AdapterException(e.getLocalizedMessage(), e);
			}
		}

	}

	public ConditionalExpression adaptConditionalExpression(Expression mainExpr, String operator, Expression operand1,
			Expression operand2) {
		return new ConditionAdapter(this.adaptExpression(mainExpr), operator, this.adaptExpression(operand1),
				this.adaptExpression(operand2));
	}

	public ScriptExpression adaptExpression(Expression expr) {
		return adaptExpression(expr, IModelAdapter.ExpressionLocation.TABLE);
	}

	public ScriptExpression adaptExpression(String jsExpr, String dataType) {
		if (jsExpr == null)
			return null;
		return new ExpressionAdapter(jsExpr, dataType);
	}

	public ScriptExpression adaptJSExpression(String jsExpr, String dataType) {
		if (jsExpr == null)
			return null;
		return new ExpressionAdapter(jsExpr, dataType);
	}

	public ScriptExpression adaptExpression(Expression expr, ExpressionLocation el) {
		if (expr == null)
			return null;

		ScriptExpression jsExpr = null;
		if (ExpressionType.CONSTANT.equals(expr.getType())) {
			jsExpr = new ScriptExpression(expr.getStringExpression());
			jsExpr.setScriptId(BaseExpression.constantId);
			jsExpr.setHandle(expr.getExpression());
			return jsExpr;
		} else if (!ExpressionType.JAVASCRIPT.equals(expr.getType())) {
			return null;
		} else {
			if (expr.getStringExpression() == null)
				return null;
			jsExpr = new ExpressionAdapter(expr, el);
		}

		return jsExpr;
	}
}
