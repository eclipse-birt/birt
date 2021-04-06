
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api.script;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * 
 */

public class ReportContextObject {

	ModuleHandle module;

	/**
	 * 
	 * @param module
	 */
	public ReportContextObject(ModuleHandle module) {
		assert module != null;
		this.module = module;
	}

	/**
	 * Returns the default value for the report parameter
	 * 
	 * @param name
	 * @return
	 */
	public Object getParameterValue(String name) {
		ParameterHandle param = module.findParameter(name);
		if (param != null)
			return getParamDefaultValue(module.findParameter(name));
		else
			return null;
	}

	/**
	 * Returns the display text for the report parameter
	 * 
	 * @param name
	 * @return
	 */
	public Object getParameterDisplayText(String name) {
		ParameterHandle param = module.findParameter(name);
		if (param != null)
			return module.findParameter(name).getDisplayName();
		else
			return null;
	}

	/**
	 * Returns the application default locale
	 * 
	 * @return
	 */
	public Object getLocale() {
		return AccessController.doPrivileged(new PrivilegedAction<Locale>() {
			public Locale run() {
				return Locale.getDefault();
			}
		});
	}

	/**
	 * Gets the default value of a parameter. If a usable default value is defined,
	 * use it; otherwise use a default value appropriate for the data type
	 */
	private Object getParamDefaultValue(ParameterHandle param) {
		if (!(param instanceof ScalarParameterHandle))
			return null;

		ScalarParameterHandle sp = (ScalarParameterHandle) param;
		String defaultValue = sp.getDefaultValue();
		String type = sp.getDataType();
		if (defaultValue == null) {
			// No default value; if param allows null value, null is used
			if (sp.allowNull())
				return null;

			// Return a fixed default value appropriate for the data type
			if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
				if (sp.allowBlank())
					return "";
				else
					return "null";
			}
			if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type))
				return new Double(0);
			if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type))
				return new BigDecimal((double) 0);
			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type))
				return new Date(0);
			if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type))
				return new java.sql.Time(0);
			if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type))
				return new java.sql.Date(0);
			if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type))
				return Boolean.FALSE;
			if (DesignChoiceConstants.PARAM_TYPE_JAVA_OBJECT.equals(type))
				return null;

			// unknown parameter type; unexpected
			assert false;
			return null;
		}

		// Convert default value to the correct data type
		int typeNum = DataType.ANY_TYPE;
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type))
			typeNum = DataType.STRING_TYPE;
		else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type))
			typeNum = DataType.DOUBLE_TYPE;
		else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type))
			typeNum = DataType.DECIMAL_TYPE;
		else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type))
			typeNum = DataType.DATE_TYPE;
		else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type))
			typeNum = DataType.BOOLEAN_TYPE;
		else if (DesignChoiceConstants.PARAM_TYPE_JAVA_OBJECT.equals(type))
			typeNum = DataType.JAVA_OBJECT_TYPE;

		try {
			return DataTypeUtil.convert(defaultValue, typeNum);
		} catch (BirtException e) {
			return null;
		}
	}

	/**
	 * return the report runnable used to create/render this report
	 * 
	 * @return
	 * @throws AdapterException
	 */
	public void getReportRunnable() throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getReportRunnable");
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @throws AdapterException
	 */
	public void setParameterValue(String name, Object value) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "setParameterValue");
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @throws AdapterException
	 */
	public void setParameterDisplayText(String name, String value) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "setParameterDisplayText");
	}

	/**
	 * 
	 * @return
	 * @throws AdapterException
	 */
	public String getOutputFormat() throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getOutputFormat");
	}

	/**
	 * Get the application context
	 * 
	 * @throws AdapterException
	 */
	public Map getAppContext() throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getAppContext");
	}

	/**
	 * Get the http servlet request object
	 * 
	 * @throws AdapterException
	 * 
	 */
	public Object getHttpServletRequest() throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getHttpServletRequest");
	}

	/**
	 * Add the object to runtime scope. This object can only be retrieved in the
	 * same phase, i.e. it is not persisted between generation and presentation.
	 * 
	 * @throws AdapterException
	 */
	public void setGlobalVariable(String name, Object obj) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "setGlobalVariable");
	}

	/**
	 * Remove an object from runtime scope.
	 * 
	 * @throws AdapterException
	 */
	public void deleteGlobalVariable(String name) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "deleteGlobalVariable");
	}

	/**
	 * Retireve an object from runtime scope.
	 * 
	 * @throws AdapterException
	 */
	public Object getGlobalVariable(String name) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getGlobalVariable");
	}

	/**
	 * Add the object to report document scope. This object can be retrieved later.
	 * It is persisted between phases, i.e. between generation and presentation.
	 * 
	 * @throws AdapterException
	 */
	public void setPersistentGlobalVariable(String name, Serializable obj) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "setPersistentGlobalVariable");
	}

	/**
	 * Remove an object from report document scope.
	 * 
	 * @throws AdapterException
	 */
	public void deletePersistentGlobalVariable(String name) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "deletePersistentGlobalVariable");
	}

	/**
	 * Retireve an object from report document scope.
	 * 
	 * @throws AdapterException
	 */
	public Object getPersistentGlobalVariable(String name) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getPersistentGlobalVariable");
	}

	/**
	 * Finds user-defined messages for the current thread's locale.
	 * 
	 * @throws AdapterException
	 */
	public String getMessage(String key) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getMessage");
	}

	/**
	 * Finds user-defined messages for the given locale.
	 * 
	 * @throws AdapterException
	 */
	public String getMessage(String key, Locale locale) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getMessage");
	}

	/**
	 * Finds user-defined messages for the current thread's locale
	 * 
	 * @throws AdapterException
	 */
	public String getMessage(String key, Object[] params) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getMessage");
	}

	/**
	 * Finds user-defined messages for the given locale using parameters
	 * 
	 * @throws AdapterException
	 */
	public String getMessage(String key, Locale locale, Object[] params) throws AdapterException {
		throw new AdapterException(ResourceConstants.INVALID_DESIGNTIME_METHOD, "getMessage");
	}
}
