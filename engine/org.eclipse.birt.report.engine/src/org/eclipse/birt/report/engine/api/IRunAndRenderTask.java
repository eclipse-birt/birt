/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public interface IRunAndRenderTask extends IEngineTask {
	/**
	 * set the rendering options
	 * 
	 * @param settings the rendering options
	 */
	public abstract void setRenderOption(IRenderOption options);

	/**
	 * @return the render option
	 */
	public abstract IRenderOption getRenderOption();

	/**
	 * runs the task to generate report document or other output format
	 */
	public abstract void run() throws EngineException;

	/**
	 * sets a specific emitter to use when generate output. Used when there are more
	 * than one emitters that support a single format. One example is the FO-based
	 * PDF emitter and the new PDF emitter added in BIRT2.0. If this function is not
	 * called when there are more than 1 emitters that support a format, engine may
	 * arbitrarily pick one.
	 * 
	 * @param id the identifier for the emitter
	 */
	public abstract void setEmitterID(String id);

	/**
	 * set the max rows per query
	 * 
	 * @param maxRows: max rows
	 */
	public void setMaxRowsPerQuery(int maxRows);

	/**
	 * set user defined IPageHandler
	 * 
	 * @param callback user-defined IPageHandler
	 */
	public void setPageHandler(IPageHandler callback);
}
