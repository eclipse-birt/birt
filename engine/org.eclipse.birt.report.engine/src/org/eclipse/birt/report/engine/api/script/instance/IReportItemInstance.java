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

public interface IReportItemInstance extends IReportElementInstance {

	/**
	 * Get the hyperlink
	 *
	 * @return the hyperlink
	 */
	String getHyperlink();

	/**
	 * Get the name
	 */
	String getName();

	/**
	 * Set the name
	 */
	void setName(String name);

	/**
	 * Get the help text
	 */
	String getHelpText();

	/**
	 * Set the help text
	 */
	void setHelpText(String helpText);

}
