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

import org.eclipse.birt.core.archive.IDocArchiveWriter;

/**
 * An engine task that runs a report and generates a report document.
 */
public interface IRunTask extends IEngineTask
{

	/**
	 * set up event handler to be called after each page is generated
	 * 
	 * @param callback
	 *            a callback function that is called after each checkpoint
	 */
	public void setPageHandler( IPageHandler callback );

	/**
	 * runs the task to generate report document
	 * 
	 * @param reportDocName the name for the report document file
	 * @throws EngineException throws exception when running report fails
	 */
	public abstract void run( String reportDocName ) throws EngineException;

	/**
	 * @param archive	a document archive object that supports various doc-related functionalities
	 * @throws EngineException throws exception when running report fails
	 */
	public abstract void run( IDocArchiveWriter archive ) throws EngineException;
}
