/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.engine;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;

/**
 * the report engine extension is used to extend the feature of the report
 * engine.
 *
 * Each report engine create a extension instance, it is closed in engine's
 * shutdown.
 *
 */
public interface IReportEngineExtension {

	IReportEngine getReportEngine();

	/**
	 * the extension name. The extension name is the unique identifier of the
	 * extension. It will be saved into the report design and report document.
	 *
	 * @return extension name
	 */
	String getExtensionName();

	/**
	 * return if the extension is used by the report design
	 *
	 * @param runnable
	 * @return
	 */
	boolean needExtension(IReportRunnable runnable);

	/**
	 * create a extension to load the data saved in the report document.
	 *
	 * the extension will be closed by the report document's close().
	 *
	 * @param document the report document.
	 * @return the extension of the document
	 * @throws EngineException
	 */
	IReportDocumentExtension createDocumentExtension(IReportDocument document) throws EngineException;

	/**
	 * create the generate extension.
	 *
	 * @param context the run context.
	 * @return the generate extension.
	 * @throws EngineException
	 */
	IGenerateExtension createGenerateExtension(IRunContext context) throws EngineException;

	IDataExtension createDataExtension(IRunContext context) throws EngineException;

	/**
	 * create the extension to handle the extra document processing.
	 *
	 * @param context run context
	 * @return the document extension.
	 * @throws EngineException
	 */
	IDocumentExtension createDocumentExtension(IRunContext context) throws EngineException;

	/**
	 * create the render extension.
	 *
	 * @param context render context.
	 * @return the render extension.
	 * @throws EngineException
	 */
	IRenderExtension createRenderExtension(IRenderContext context) throws EngineException;

	/**
	 * create an engine task
	 *
	 * @return an engine task
	 */
	IEngineTask createEngineTask(String taskName) throws EngineException;

	/**
	 * release the extension.
	 *
	 * The user should release the shared resource allocated for the instance.
	 */
	void close();
}
