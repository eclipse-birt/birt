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

package org.eclipse.birt.report.engine.executor;

import java.util.Map;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * Captures the need for scripting at factory time.
 * 
 */
public interface IFactoryContext {

	/**
	 * @return the report design Java Object
	 */
	public ReportDesignHandle getDesign();

	/**
	 * @return A map of all parameter name/vaue pairs
	 */
	public Map getParams();

	/**
	 * @return configuration variable name/value pairs. Configuration variables are
	 *         defined externally to the report and are set in the engine
	 *         environment
	 */
	public Map getConfigs();

	/**
	 * @return the read-only report item handle
	 */
	public ReportElementHandle getItemDesign();
}
