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

/**
 * a factory used to create the Report Engine.
 *
 * @see the ReportRunner for the usage.
 */
public interface IReportEngineFactory {

	/**
	 * the extension point used to create the factory object.
	 *
	 * @see org.eclipse.birt.core.framework.Platform#createFactoryObject(String)
	 */
	String EXTENSION_REPORT_ENGINE_FACTORY = "org.eclipse.birt.report.engine.ReportEngineFactory";

	/**
	 * create a new report engine object.
	 *
	 * @param config config used to create the report engine.
	 * @return the report engine object
	 */
	IReportEngine createReportEngine(EngineConfig config);
}
