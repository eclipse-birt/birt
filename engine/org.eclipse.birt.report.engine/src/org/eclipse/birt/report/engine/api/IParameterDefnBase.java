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
package org.eclipse.birt.report.engine.api;

import java.util.Map;

import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * Captures properties shared by all types of parameters and parameter group,
 * i.e., name, display name, help text and custom-defined properties.
 * 
 * Note that even though display name and help text are locale-sensitive, the
 * API does not take a locale. The parameter returned to the user was obtained
 * from a report runnable, which has already had a locale.
 */
public interface IParameterDefnBase {
	public static final int SCALAR_PARAMETER = 0;
	public static final int FILTER_PARAMETER = 1;
	public static final int LIST_PARAMETER = 2;
	public static final int TABLE_PARAMETER = 3;
	public static final int PARAMETER_GROUP = 4;
	public static final int CASCADING_PARAMETER_GROUP = 5;

	/**
	 * @return the parameter type, i.e., scalar, filter, list, table or parameter
	 *         group
	 */
	public int getParameterType();

	/**
	 * 
	 * @return name of the parameter type.
	 */
	public String getTypeName();

	/**
	 * returns the name of the parameter
	 * 
	 * @return the name of the parameter
	 */
	public String getName();

	/**
	 * returns the locale-specific display name for the parameter. The locale used
	 * is the locale in the getParameterDefinition task
	 * 
	 * @return display name under the request or default locale
	 */
	public String getDisplayName();

	/**
	 * returns the locale-specific help text. The locale used is the locale in the
	 * getParameterDefinition task
	 * 
	 * @return help text for the parameter
	 */
	public String getHelpText();

	/**
	 * returns a collection of user-defined property name and value pairs
	 *
	 * @return a collection of user-defined property name ane value pairs
	 */
	public Map getUserPropertyValues();

	/**
	 * returns the value of a user-defined property
	 * 
	 * @return the value for a user-defined property
	 */
	public String getUserPropertyValue(String name);

	/**
	 * returns the report element handle which is wrapped by this object.
	 * 
	 * @return the report element handle
	 */
	public ReportElementHandle getHandle();

	/**
	 * @return prompt text
	 */
	public String getPromptText();

}
