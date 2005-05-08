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
 * 
 */
public interface IHTMLImageHandler {
	/**
	 * handles a specific image. The implementation supplies a URL and optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onDesignImage(IImage image, Object context);
	
	public abstract String onDocImage(IImage image, Object context);
	
	public abstract String onFileImage(IImage image, Object context);
	
	public abstract String onURLImage(IImage image, Object context);

	public abstract String onCustomImage(IImage image, Object context);

}