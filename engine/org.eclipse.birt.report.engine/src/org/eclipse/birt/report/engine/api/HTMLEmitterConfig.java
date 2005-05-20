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
 * Defines an emitter configuration class for HTML output format
 */
public class HTMLEmitterConfig {
	
	/**
	 * default image hanlder
	 */
	protected IHTMLImageHandler imageHandler;
	
	/**
	 * default action handler
	 */
	protected IHTMLActionHandler actionHandler;
	
	/**
	 * dummy constructor
	 */
	public HTMLEmitterConfig()
	{
	}
	
	/**
	 * sets the image handler for HTML format
	 * 
	 * @param handler image handler
	 */
	public void setImageHandler(IHTMLImageHandler handler){
		this.imageHandler = handler;
	}

	/**
	 * sets the action handler for HTML format
	 * 
	 * @param handler action handler
	 */
	public void setActionHandler(IHTMLActionHandler handler){
		this.actionHandler = handler;
		
	}
	
	/**
	 * returns the image handler for HTML output format
	 * 
	 * @return the image handler for HTML output format
	 */
	public IHTMLImageHandler getImageHandler()
	{
		return this.imageHandler;
	}
	
	/**
	 * returns the action handler for HTML format
	 * 
	 * @return the action handler for HTML format
	 */
	public IHTMLActionHandler getActionHandler()
	{
		return this.actionHandler;
	}
}
