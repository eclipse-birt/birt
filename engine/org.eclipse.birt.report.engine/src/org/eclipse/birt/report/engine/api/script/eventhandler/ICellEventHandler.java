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
import org.eclipse.birt.report.engine.api.script.element.ICell;
import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;

/**
 * Script event handler interface for a cell
 */
public interface ICellEventHandler
{

	/**
	 * Handle the onPrepare event
	 */
	void onPrepare( ICell cell, IReportContext reportContext );

	/**
	 * Handle the onCreate event
	 */
	void onCreate( ICellInstance cellInstance, IReportContext reportContext );

	/**
	 * Handle the onRender event
	 */
	void onRender( ICellInstance cellInstance, IReportContext reportContext );

}
