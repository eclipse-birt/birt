/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.eventhandler;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
* Script event handler interface for a report
*/
public interface IReportEventHandler
{
	/**
	 * Handle the initialize event
	 */
	void initialize( IReportContext reportContext );

	/**
	 * Handle the beforeFactory event
	 */
	void beforeFactory( ReportDesignHandle report,
			IReportContext reportContext );

	/**
	 * Handle the afterFactory event
	 */
	void afterFactory( IReportContext reportContext );

	/**
	 * Handle the beforeOpenDoc event
	 */
	void beforeOpenDoc( IReportContext reportContext );

	/**
	 * Handle the afterOpenDoc event
	 */
	void afterOpenDoc( IReportContext reportContext );
	
	/**
	 * Handle the beforeCloseDoc event
	 */
	void beforeCloseDoc( IReportContext reportContext );

	/**
	 * Handle the afterCloseDoc event
	 */
	void afterCloseDoc( IReportContext reportContext );

	/**
	 * Handle the beforeRender event
	 */
	void beforeRender( IReportContext reportContext );

	/**
	 * Handle the afterRender event
	 */
	void afterRender( IReportContext reportContext );
}
