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

import org.eclipse.birt.report.engine.api.script.IDataSetRow;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Script event handler interface for a scripted data set
 */
public interface IScriptedDataSetEventHandler extends IDataSetEventHandler
{
	/**
	 * Handle the open event
	 */
	void open( IDataSetInstance dataSet );

	/**
	 * Handle the fetch event
	 */
	boolean fetch( IDataSetInstance dataSet, IDataSetRow row );

	/**
	 * Handle the close event
	 */
	void close( IDataSetInstance dataSet );

}
