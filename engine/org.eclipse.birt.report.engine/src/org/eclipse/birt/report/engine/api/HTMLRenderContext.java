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
 * Defines the context for rendering in HTML emitter. Information provided in this
 * context object is mainly used for image and action handling, but can be used for
 * other purposes too. 
 */
public class HTMLRenderContext {
	
	protected String baseURL;

    /**
     */
    protected String baseImageURL;

	
	protected String imageDirectory;
	
	/**
	 * @return Returns the baseURL.
	 */
	public String getBaseURL() {
		return baseURL;
	}
	/**
	 * @param baseURL The baseURL to set.
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	/**
	 * @return Returns the imageDirectory.
	 */
	public String getImageDirectory() {
		return imageDirectory;
	}
	
	/**
	 * @param imageDirectory The imageDirectory to set.
	 */
	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}
	
	/**
	 * constrictor 
	 */
	public HTMLRenderContext()
	{
		
	}

    /**
     * @return Returns the baseImageURL.
     */
    public String getBaseImageURL() {
        return baseImageURL;
    }

    /**
     * @param baseImageURL The baseImageURL to set.
     */
    public void setBaseImageURL(String baseImageURL) {
        this.baseImageURL = baseImageURL;
    }

}
