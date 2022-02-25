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
package org.eclipse.birt.report.engine.api.script.instance;

public interface ILabelInstance extends IReportItemInstance {

	/**
	 * Get the value
	 */
	String getText();

	/**
	 * Set the value
	 */
	void setText(String value);

	/**
	 * Get the text key
	 */
	String getTextKey();

	/**
	 * Set the text key
	 */
	void setTextKey(String key);

	/**
	 * Create a new action instance, witch can be bookmark, hyperlink or
	 * drillThrough. The default action instance type is NULL.
	 */
	IActionInstance createAction();

	/**
	 * Get the action instance.
	 */
	IActionInstance getAction();

	/**
	 * set the actionInstance
	 *
	 * @param actionInstance
	 */
	void setAction(IActionInstance actionInstance);
}
