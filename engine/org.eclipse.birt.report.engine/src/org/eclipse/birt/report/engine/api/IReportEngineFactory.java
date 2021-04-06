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
	static final String EXTENSION_REPORT_ENGINE_FACTORY = "org.eclipse.birt.report.engine.ReportEngineFactory";

	/**
	 * create a new report engine object.
	 * 
	 * @param config config used to create the report engine.
	 * @return the report engine object
	 */
	IReportEngine createReportEngine(EngineConfig config);
}
