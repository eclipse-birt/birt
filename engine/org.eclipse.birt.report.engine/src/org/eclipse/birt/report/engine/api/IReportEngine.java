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

import java.util.Locale;

/**
 * Defines interface to the report engine. Classes implementing the
 * interface might include ReportEngineApp, EmbeddedReportEngine and 
 * ReportEngineServer
 */

public interface IReportEngine
{
	/**
	 * @param reportName the name of the report design file
	 * @param locale the locale used to render the report. Notice that the factory part of the
	 * 		  engine is locale-independent, but the presentation part of the engine is.
	 * @return A handle that the user can request for reporting services from
	 */
	public IReportHandle getReportHandle( String reportName, Locale locale );

	/**
	 * @param stream the input stream for the report design
	 * @param locale the locale used to render the report. Notice that the factory part of the
	 * 		  engine is locale-independent, but the presentation part of the engine is.
	 * @return A handle that the user can request for reporting services from
	 */
//	public IReportHandle getReportHandle( InputStream stream, Locale locale );
}