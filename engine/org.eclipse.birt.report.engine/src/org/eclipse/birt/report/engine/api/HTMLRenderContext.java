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
 * Defines the context for rendering report in HTML emitter. Objects stored in
 * the context object is mainly used for image and action handling, but can be
 * used for other purposes too.
 * 
 * @deprecated set the property to RenderOption directly.
 */
public class HTMLRenderContext {

	/**
	 * base URL used for action handler
	 */
	protected String baseURL;

	/**
	 * base URL used for image
	 */
	protected String baseImageURL;

	/**
	 * the image formats supported by the browser
	 */
	protected String supportedImageFormats;

	/**
	 * directory to store image
	 */
	protected String imageDirectory;

	/**
	 * HTML render option
	 */
	protected IRenderOption renderOption;

	/**
	 * dummy constrictor
	 */
	public HTMLRenderContext() {
	}

	/**
	 * Returns the base URL for creating an Action URL
	 * 
	 * @return the baseURL.
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * sets the base url for action handling
	 * 
	 * @param baseURL sets the base URL used for action handling
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * returns the image directory that engine stores images and charts into
	 * 
	 * @return the image directory.
	 */
	public String getImageDirectory() {
		return imageDirectory;
	}

	/**
	 * sets the image directory that engine stores images and charts into
	 * 
	 * @param imageDirectory the image directory that engine stores images and
	 *                       charts into
	 */
	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	/**
	 * returns the base url for creating image URL
	 * 
	 * @return Rreturn the abse image url
	 */
	public String getBaseImageURL() {
		return baseImageURL;
	}

	/**
	 * sets the base image URL for image handling
	 * 
	 * @param baseImageURL the base image URL
	 */
	public void setBaseImageURL(String baseImageURL) {
		this.baseImageURL = baseImageURL;
	}

	/**
	 * @param formats - the image format supported by the browser
	 */
	public void setSupportedImageFormats(String formats) {
		supportedImageFormats = formats;
	}

	/**
	 * @return the image format supported by the browser
	 */
	public String getSupportedImageFormats() {
		return supportedImageFormats;
	}

	/**
	 * @return render options
	 */
	public void setRenderOption(IRenderOption option) {
		renderOption = option;
	}

	/**
	 * @return render options
	 * @deprecated it is deprecated and use the setRenderOption instead.
	 */
	public void SetRenderOption(IRenderOption option) {
		this.setRenderOption(option);
	}

	/**
	 * @return render options
	 */
	public IRenderOption getRenderOption() {
		return renderOption;
	}

}
