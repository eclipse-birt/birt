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

import java.util.HashMap;

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
	 * set all parameter valuess
	 * 
	 * @param params a hash map with all parameters
	 */
	public abstract void setParameterValues(HashMap params);
	
	/**
	 * sets one parameter value
	 * 
	 * @param name parameter name
	 * @param value parameter value
	 */
	public abstract void setParameterValue(String name, Object value);
	
	/**
	 * returns the parameter name/value collection
	 * 
	 * @return the parameter names/values in a hash map 
	 */
	public abstract HashMap getParameterValues();
	
	/**
	 * @return whether the parameter validation succeeds <br>
	 */
	public boolean validateParameters( );

	/**
	 * runs the task to generate report document or other output format
	 */
	public abstract void run() throws EngineException;

}