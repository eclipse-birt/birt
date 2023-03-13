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

import java.util.ArrayList;

/**
 * base interface for a BIRT report parameter
 */
public interface IParameterDefn extends IParameterDefnBase {

	int TYPE_ANY = 0;
	int TYPE_STRING = 1;
	int TYPE_FLOAT = 2;
	int TYPE_DECIMAL = 3;
	int TYPE_DATE_TIME = 4;
	int TYPE_BOOLEAN = 5;
	int TYPE_INTEGER = 6;
	int TYPE_DATE = 7;
	int TYPE_TIME = 8;

	int SELECTION_LIST_NONE = 0;
	int SELECTION_LIST_DYNAMIC = 1;
	int SELECTION_LIST_STATIC = 2;

	/**
	 * returns whether the parameter is a hidden parameter
	 *
	 * @return whether the parameter is a hidden parameter
	 */
	boolean isHidden();

	/**
	 * @return whether the parameter is required.<br>
	 *         the rule for String type is:
	 *         <li>isRequired=true, allowNull and allowBlank are false</li>
	 *         <li>isRequired=false, allowNull and allowBlank are true</li> <br>
	 *         for other type like integer:
	 *         <li>isRequired=true, allowNull and allowBlank are false</li>
	 *         <li>isRequired=false, allowNull and allowBlank are true</li>
	 */
	boolean isRequired();

	/**
	 * returns the parameter data type. The valid data type could be
	 * <code>IParameterDefn.TYPE_ANY</code>,
	 * <code>IParameterDefn.TYPE_STRING</code>,
	 * <code>IParameterDefn.TYPE_FLOAT</code>,
	 * <code>IParameterDefn.TYPE_DECIMAL</code>,
	 * <code>IParameterDefn.TYPE_DATE_TIME</code>,
	 * <code>IParameterDefn.TYPE_BOOLEAN</code>,
	 * <code>IParameterDefn.TYPE_INTEGER</code>,
	 * <code>IParameterDefn.TYPE_DATE</code>, <code>IParameterDefn.TYPE_TIME</code>.
	 *
	 * @return the parameter data type
	 */

	int getDataType();

	/**
	 * @return get a parameter value selection object, from which a list of
	 *         parameter values and label values can be retrieved.
	 * @deprecated
	 */
	@Deprecated
	ArrayList getSelectionList();

	/**
	 * @return the type of the parameter selection list
	 */
	int getSelectionListType();
}
