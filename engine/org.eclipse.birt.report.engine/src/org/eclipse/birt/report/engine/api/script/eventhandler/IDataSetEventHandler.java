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

import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.script.element.DataSet;

/**
 * Script event handler interface for a data set
 */
public interface IDataSetEventHandler
{

	/**
	 * Handle the beforeOpen event
	 */
	void beforeOpen( DataSet dataSet, IReportContext reportContext );

	/**
	 * Handle the afterOpen event
	 */
	void afterOpen( IReportContext reportContext );

	/**
	 * Handle the onFetch event
	 */
	void onFetch( IRowData expressionResults,
			IReportContext reportContext );

	/**
	 * Handle the beforeClose event
	 */
	void beforeClose( IReportContext reportContext );

	/**
	 * Handle the afterClose event
	 */
	void afterClose( IReportContext reportContext );

}
