/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
