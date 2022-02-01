/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Base class for parameter definition.
 */
public class ParameterDefnBase implements IParameterDefnBase, Cloneable {

	private static final String SCALAR = "scalar"; //$NON-NLS-1$
	private static final String FILTER = "filter"; //$NON-NLS-1$
	private static final String LIST = "list"; //$NON-NLS-1$
	private static final String TABLE = "table"; //$NON-NLS-1$
	private static final String GROUP = "group"; //$NON-NLS-1$

	protected int parameterType;
	protected String displayName;
	protected String displayNameKey;
	protected String helpText;
	protected String helpTextKey;
	protected String name;
	protected String promptTextKey;

	protected String promptText;

	protected Map customProperties = new HashMap();

	protected ModuleHandle designHandle;
	protected Locale locale = null;

	protected ReportElementHandle handle = null;

	/**
	 * @param reportDesign The reportDesign to set.
	 */
	public void setDesign(ModuleHandle designHandle) {
		this.designHandle = designHandle;
	}

	/**
	 * @param locale the locale under which the parameter display name, help text
	 *               need to be returned
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IParameterDefnBase#getParameterType()
	 */
	public int getParameterType() {
		return parameterType;
	}

	/**
	 * @param parameterType The parameterType to set.
	 */
	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getDisplayName()
	 */
	public String getDisplayName() {
		if (displayNameKey == null)
			return displayName;

		String ret = designHandle.getMessage(displayNameKey, (locale == null) ? Locale.getDefault() : locale);
		if (ret == null || ret.length() == 0)
			return displayName;
		return ret;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IParameterDefnBase#getHelpText()
	 */
	public String getHelpText() {
		if (helpTextKey == null)
			return helpText;

		String ret = designHandle.getMessage(helpTextKey, (locale == null) ? Locale.getDefault() : locale);
		if (ret == null || ret.length() == 0)
			return helpText;
		return ret;
	}

	/**
	 * @param helpText The help text to set.
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/**
	 * @param helpTextKey the message key for help text
	 */
	public void setHelpTextKey(String helpTextKey) {
		this.helpTextKey = helpTextKey;
	}

	public void setPromptTextKey(String promptTextKey) {
		this.promptTextKey = promptTextKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IParameterDefnBase#getUserPropertyValues
	 * ()
	 */
	public Map getUserPropertyValues() {
		return Collections.unmodifiableMap(customProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IParameterDefnBase#getUserPropertyValue
	 * (java.lang.String)
	 */
	public String getUserPropertyValue(String name) {
		if (customProperties.containsKey(name)) {
			Object value = customProperties.get(name);
			if (value != null) {
				return value.toString();
			}
		}
		return null;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IParameterDefnBase#getTypeName()
	 */
	public String getTypeName() {
		String typeName;
		switch (parameterType) {
		case IParameterDefnBase.FILTER_PARAMETER:
			typeName = FILTER;
			break;
		case IParameterDefnBase.LIST_PARAMETER:
			typeName = LIST;
			break;
		case IParameterDefnBase.TABLE_PARAMETER:
			typeName = TABLE;
			break;
		case IParameterDefnBase.PARAMETER_GROUP:
			typeName = GROUP;
			break;
		case IParameterDefnBase.SCALAR_PARAMETER:
		default:
			typeName = SCALAR;
			break;
		}
		return typeName;
	}

	public void addUserProperty(String name, Object value) {
		customProperties.put(name, value);
	}

	/**
	 * @return Returns the handle.
	 */
	public ReportElementHandle getHandle() {
		return handle;
	}

	/**
	 * @param handle The handle to set.
	 */
	public void setHandle(ReportElementHandle handle) {
		this.handle = handle;
	}

	/**
	 * @return Returns the prompt text.
	 */
	public String getPromptText() {
		if (promptTextKey == null)
			return promptText;

		Locale theLocale = (locale == null) ? Locale.getDefault() : locale;
		return handle.getExternalizedValue(ScalarParameterHandle.PROMPT_TEXT_ID_PROP,
				ScalarParameterHandle.PROMPT_TEXT_PROP, theLocale);
	}

	/**
	 * @param promptText , The prompt text to set.
	 */
	public void setPromptText(String promptText) {
		this.promptText = promptText;
	}

}
