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

import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * Defines the image handler interface for use in HTML format 
 */
public interface IHTMLImageHandler {
	/**
	 * handles a design image. The implementation supplies a URL and optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onDesignImage(IImage image, Object context);
	public abstract String onDesignImage(IImage image, IReportContext context);
	
	/**
	 * handles a database image. The implementation supplies a URL and optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onDocImage(IImage image, Object context);
	public abstract String onDocImage(IImage image, IReportContext context);
	
	
	/**
	 * handles a image specified as a on-disk URI. The implementation supplies a URL and 
	 * optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onFileImage(IImage image, Object context);
	public abstract String onFileImage(IImage image, IReportContext context);
	
	/**
	 * handles an image specified as an external URL. The implementation supplies a URL and 
	 * optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onURLImage(IImage image, Object context);
	public abstract String onURLImage(IImage image, IReportContext context);

	/**
	 * handles a custom image created for example, by chart extension. The implementation 
	 * supplies a URL and optionally stores the image.   
	 * 
	 * @param image the image definition object
	 * @param context the context for generating the URL 
	 * @return the URL for the image
	 */
	public abstract String onCustomImage(IImage image, Object context);
	public abstract String onCustomImage(IImage image, IReportContext context);

}