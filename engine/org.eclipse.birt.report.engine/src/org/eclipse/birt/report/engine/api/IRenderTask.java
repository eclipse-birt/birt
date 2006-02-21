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

/**
 * An engine task that renders a Report Document to one of the output formats
 * supported by the engine.
 */
public interface IRenderTask extends IEngineTask
{
	/**
	 * set the rendering options
	 * 
	 * @param settings
	 *            the rendering options
	 */
	public abstract void setRenderOption( IRenderOption options );

	/**
	 * @return the render option
	 */
	public abstract IRenderOption getRenderOption( );

	/**
	 * sets a specific emitter to use when generate output. Used when there 
	 * are more than one emitters that support a single format. One example 
	 * is the FO-based PDF emitter and the new PDF emitter added in BIRT2.0.
	 * If this function is not called when there are more than 1 emitters 
	 * that support a format, engine may arbitrarily pick one.
	 * @param id the identifier for the emitter
	 */
	public abstract void setEmitterID(String id);

	/**
	 * render the whole report document or an output format
	 * 
	 * @throws EngineException if rendering fails
	 */
	public abstract void render( ) throws EngineException;

	/**
	 * @param pageNumber
	 * @throws EngineException
	 */
	public abstract void render( long pageNumber ) throws EngineException;

	/**
	 * Render the page from startPageNumber to endPageNumber in the Report
	 * Doucment to an output format.
	 * 
	 * @throws EngineException
	 */
	public abstract void render(String pageRange) throws EngineException;
	
	/**
	 * Render the Reportlet whose container is identified by iid. Useful for
	 * Reportlet support
	 * 
	 * @param itemInstanceID
	 *            the report iteminstance to be rendered
	 * @throws EngineException
	 */
	public abstract void render( InstanceID iid ) throws EngineException;
}
