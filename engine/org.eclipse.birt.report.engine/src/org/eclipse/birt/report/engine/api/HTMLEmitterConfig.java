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

package org.eclipse.birt.report.engine.api;

/**
 * 
 */
public class HTMLEmitterConfig {
	
	protected IHTMLImageHandler imageHandler;
	protected IHTMLActionHandler actionHandler;
	
	public HTMLEmitterConfig()
	{
		
	}
	
	public void setImageHandler(IHTMLImageHandler handler){
		this.imageHandler = handler;
	}

	public void setActionHandler(IHTMLActionHandler handler){
		this.actionHandler = handler;
		
	}
	
	public IHTMLImageHandler getImageHandler()
	{
		return this.imageHandler;
	}
	public IHTMLActionHandler getActionHandler()
	{
		return this.actionHandler;
	}

}
