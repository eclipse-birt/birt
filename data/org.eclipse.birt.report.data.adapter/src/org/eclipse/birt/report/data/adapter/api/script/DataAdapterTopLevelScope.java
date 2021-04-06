/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.api.script;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

/**
 * Top-level scope created by Data Adaptor. This scope provides implementation
 * of some report level script object (e.g., "params")
 */
public class DataAdapterTopLevelScope extends ImporterTopLevel {
	private static final long serialVersionUID = 4230948829384l;

	private static final String PROP_PARAMS = "params";
	private static final String PROP_REPORTCONTEXT = "reportContext";
	private ModuleHandle designModule;
	private Scriptable paramsProp;
	private Object reportContextProp;

	private Context cx;

	/**
	 * Constructor; initializes standard objects in scope
	 * 
	 * @param cx     Context used to initialze scope
	 * @param module Module associated with this scope; can be null
	 */
	public DataAdapterTopLevelScope(Context cx, ModuleHandle module) {
		super(cx);
		// init BIRT native objects
		new CoreJavaScriptInitializer().initialize(cx, this);
		this.cx = cx;
		designModule = module;
	}

	public boolean has(String name, Scriptable start) {
		if (super.has(name, start))
			return true;
		// "params" is available only if we have a module
		if (designModule != null && PROP_PARAMS.equals(name))
			return true;

		if (PROP_REPORTCONTEXT.equals(name))
			return true;
		return false;
	}

	public Object get(String name, Scriptable start) {
		Object result = super.get(name, start);
		if (result != NOT_FOUND)
			return result;

		if (designModule != null && PROP_PARAMS.equals(name)) {
			return getParamsScriptable();
		}

		if (PROP_REPORTCONTEXT.equals(name))
			return getReportContext();

		return NOT_FOUND;
	}

	/**
	 * Gets a object which implements the "reportContext" property
	 * 
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getReportContext() {
		if (reportContextProp != null)
			return reportContextProp;

		assert designModule != null;

		reportContextProp = new ReportContextObject(designModule);

		return reportContextProp;
	}

	/**
	 * Gets a scriptable object which implements the "params" property
	 */
	private Scriptable getParamsScriptable() {
		if (paramsProp != null)
			return paramsProp;

		assert designModule != null;

		Map parameters = new HashMap();
		paramsProp = new ReportParameters(parameters, this);
		List paramsList = designModule.getAllParameters();
		for (int i = 0; i < paramsList.size(); i++) {
			Object parameterObject = paramsList.get(i);
			if (parameterObject instanceof ScalarParameterHandle) {
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) parameterObject;
				Object value = DataAdapterUtil.getParamValueFromConfigFile(parameterHandle);
				if (value == null) {
					Object values = getParamDefaultValue(parameterHandle);
					if (values != null) {
						Object[] obj = (Object[]) values;
						if (obj.length == 1)
							parameters.put(((ScalarParameterHandle) parameterObject).getQualifiedName(),
									new DummyParameterAttribute(obj[0], ""));
						else
							parameters.put(((ScalarParameterHandle) parameterObject).getQualifiedName(),
									new DummyParameterAttribute(obj, ""));
					}
				} else {
					parameters.put(((ScalarParameterHandle) parameterObject).getQualifiedName(),
							new DummyParameterAttribute(value, ""));
				}
			} else if (parameterObject instanceof DynamicFilterParameterHandle) {
				List defaultValue = ((DynamicFilterParameterHandle) parameterObject).getDefaultValueList();
				if (defaultValue != null && defaultValue.size() > 0) {
					Expression expression = (Expression) defaultValue.get(0);
					String defaultValueString = expression.getStringExpression();
					parameters.put(((DynamicFilterParameterHandle) parameterObject).getQualifiedName(),
							new DummyParameterAttribute(defaultValueString, ""));
				} else {
					parameters.put(((DynamicFilterParameterHandle) parameterObject).getQualifiedName(),
							new DummyParameterAttribute("true", ""));
				}
			}
		}
		return paramsProp;
	}

	/**
	 * Get the parameter value from the static report parameter
	 * 
	 * @return Object[] the static parameter values
	 */
	/*
	 * private Object[] getStaticParamValue( ScalarParameterHandle handle ) {
	 * Iterator it = handle.choiceIterator( ); if ( it == null ) { return null; }
	 * 
	 * List values = new ArrayList( );
	 * 
	 * while ( it.hasNext( ) ) { SelectionChoiceHandle choice =
	 * (SelectionChoiceHandle) it.next( ); values.add( choice.getValue( ) ); }
	 * 
	 * return values.toArray( ); }
	 */

	/**
	 * Gets the default value of a parameter. If a usable default value is defined,
	 * use it; otherwise use a default value appropriate for the data type
	 */
	private Object getParamDefaultValue(Object params) {
		if (!(params instanceof ScalarParameterHandle))
			return null;

		ScalarParameterHandle sp = (ScalarParameterHandle) params;
		Object[] defaultValues = null;
		List defaultValueList = sp.getDefaultValueList();
		if (defaultValueList != null && defaultValueList.size() > 0) {
			defaultValues = new Object[defaultValueList.size()];

			for (int i = 0; i < defaultValueList.size(); i++) {
				Expression expression = (Expression) defaultValueList.get(i);
				String defaultValue = expression.getStringExpression();
				String expressionType = expression.getType();
				if (ExpressionType.JAVASCRIPT.equals(expressionType)) {
					try {
						Object evaluatedResult = JavascriptEvalUtil.evaluateScript(cx, this,
								expression.getStringExpression(), ScriptExpression.defaultID, 0);
						if (evaluatedResult != null) {
							defaultValue = evaluatedResult.toString();
						}
					} catch (BirtException e) {
						e.printStackTrace();
					}
				}

				String type = sp.getDataType();
				if (defaultValue == null) {
					// No default value; if param allows null value, null is
					// used
					if (sp.allowNull())
						defaultValues[i] = null;

					// Return a fixed default value appropriate for the data
					// type
					if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
						defaultValues[i] = "";
					}
					if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type))
						defaultValues[i] = new Double(0);
					if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type))
						defaultValues[i] = new BigDecimal((double) 0);
					if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type))
						defaultValues[i] = new Date(0);
					if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type))
						defaultValues[i] = new java.sql.Date(0);
					if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type))
						defaultValues[i] = new java.sql.Time(0);
					if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type))
						defaultValues[i] = Boolean.FALSE;
					if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type))
						defaultValues[i] = Integer.valueOf(0);

					// unknown parameter type; unexpected
					assert false;
					defaultValues[i] = null;
				}

				try {
					defaultValues[i] = DataTypeUtil.convert(defaultValue,
							DataAdapterUtil.modelDataTypeToCoreDataType(type));
				} catch (BirtException e) {
					defaultValues[i] = null;
				}
			}
		}

		return defaultValues;
	}

	/**
	 * To check whether the object with the specific type should be converted
	 * 
	 * @param type
	 * @return true if should be converted
	 */
	private static boolean isToBeConverted(String type) {
		return type.equals(DesignChoiceConstants.PARAM_TYPE_STRING)
				|| type.equals(DesignChoiceConstants.PARAM_TYPE_DATETIME)
				|| type.equals(DesignChoiceConstants.PARAM_TYPE_TIME)
				|| type.equals(DesignChoiceConstants.PARAM_TYPE_DATE);
	}

}
