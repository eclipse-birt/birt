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

package org.eclipse.birt.report.engine.emitter;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;

/**
 * Defines interface to supply emitters with necessary information
 */
public interface IEmitterServices
{

	/**
	 * @return emitter configuration of engine
	 */
	public HashMap getEmitterConfig( );

	/**
	 * @return render options
	 */
	public IRenderOption getRenderOption( );

	/**
	 * @return the current report name
	 */
	public String getReportName( );
	
	/**
	 * 
	 * @return render context
	 */
	public Object getRenderContext();

	/**
	 * 
	 * @return the report runnable
	 */
	public IReportRunnable getReportRunnable();


	/**
	 * @param name
	 *            option name
	 * @return option value
	 */
	public Object getOption( String name );
	
	/**
	 * 
	 * @return the engine task
	 */
	public IEngineTask getTask();
}