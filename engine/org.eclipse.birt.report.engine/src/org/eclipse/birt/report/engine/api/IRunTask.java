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

import org.eclipse.birt.core.archive.FolderArchive;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;

/**
 * An engine task that runs a report and generates a report document.
 */
public interface IRunTask extends IEngineTask {

	/**
	 * set up event handler to be called after each page is generated
	 * 
	 * @param callback a callback function that is called after each checkpoint
	 */
	public void setPageHandler(IPageHandler callback);

	/**
	 * runs the task to generate report document
	 * 
	 * @param reportDocName the name for the report document file
	 * @throws EngineException throws exception when running report fails
	 */
	public abstract void run(String reportDocName) throws EngineException;

	/**
	 * @param archive a document archive object that supports various doc-related
	 *                functionalities
	 * @throws EngineException throws exception when running report fails
	 */
	public abstract void run(IDocArchiveWriter archive) throws EngineException;

	/**
	 * @deprecated
	 * @param fArchive a folder archive that is used both as the data source, and as
	 *                 output The engine WILL overwrite the archive that is passed
	 *                 in!
	 * @throws EngineException throws exception when running report fails
	 */
	public abstract void run(FolderArchive fArchive) throws EngineException;

	/**
	 * set the max rows per query
	 * 
	 * @param maxRows: max rows
	 */
	public void setMaxRowsPerQuery(int maxRows);

	/**
	 * need the run task support progressive viewing.
	 * 
	 * the default value is TRUE.
	 * 
	 * @param enable true the render task can render the document while the run task
	 *               is generating.
	 * 
	 *               false the render task must wait for the run task.
	 */
	public void enableProgressiveViewing(boolean enable);

	/**
	 * set report document. This archive takes precedence over a report document
	 * name
	 * 
	 * @param archive the archive file
	 */
	public void setReportDocument(IArchiveFile archive);

	/**
	 * set report document name
	 * 
	 * @param name report document name
	 */
	public void setReportDocument(String name);

	/**
	 * run this task.
	 * 
	 * @throws EngineException
	 */
	public void run() throws EngineException;
}
