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
 * Defines the context for rendering report in PDF emitter. Objects stored in
 * the context object is mainly used for font, image, action handling, but can
 * be used for other purposes too.
 * 
 * @deprecated set the option to RenderOption directly.
 */
public class PDFRenderContext {

	/**
	 * base URL used for action handler
	 */
	protected String baseURL;

	protected boolean isEmbededFont = true;

	/**
	 * the image formats supported by the browser
	 */
	protected String supportedImageFormats;

	/**
	 * user-defined font directory
	 */
	protected String fontDirectory;

	/**
	 * dummy constructor
	 */
	public PDFRenderContext() {
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
	 * sets the base URL for action handling
	 * 
	 * @param baseURL sets the base URL used for action handling
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
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
	 * Set flag indicates if the font needs to be embedded.
	 * 
	 * @param isEmbededFont
	 */
	public void setEmbededFont(boolean isEmbededFont) {
		this.isEmbededFont = isEmbededFont;
	}

	/**
	 * 
	 * @return true if font is embedded
	 */
	public boolean isEmbededFont() {
		return isEmbededFont;
	}

	/**
	 * 
	 * @return the user-defined font directory
	 */
	public String getFontDirectory() {
		return fontDirectory;
	}

	/**
	 * 
	 * @param fontDirectory the user-defined font directory
	 */
	public void setFontDirectory(String fontDirectory) {
		this.fontDirectory = fontDirectory;
	}

}
