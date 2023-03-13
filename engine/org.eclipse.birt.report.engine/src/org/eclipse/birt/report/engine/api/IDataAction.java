/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

/**
 * Data Action is used to generate a URL used to reterive the data from the data
 * base.
 *
 * It has following files:
 * <li>getDataType</li> the output data type, such as csv, xml etc.
 * <li>getReportName</li> the report document name, which is the data soruce.
 * <li>getBookmark</li> the bookmark which define the result set to be exported.
 *
 */
public interface IDataAction extends IAction {

	/**
	 * data action, the user can safely type cast this object to IDataAction
	 */
	int ACTION_DATA = 4;

	/**
	 * the output data type, such as csv, xml. the type should be registered by a
	 * IDataExtractionExtension.
	 *
	 * @return the data type.
	 */
	String getDataType();

	/**
	 * Returns the instance ID of associated report item instance.
	 *
	 * @return The instance ID
	 */
	InstanceID getInstanceID();

	/**
	 * Returns true if current action is to get cube data.
	 *
	 * @return
	 * @since 2.5.1
	 */
	boolean isCube();
}
