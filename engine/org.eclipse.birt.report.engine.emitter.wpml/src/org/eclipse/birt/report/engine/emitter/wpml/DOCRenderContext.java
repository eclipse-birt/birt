/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.wpml;

public class DOCRenderContext {
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
	 * user-defined font dirctory
	 */
	protected String fontDirectory;

	/**
	 * dummy constrictor
	 */
	public DOCRenderContext() {
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

	public void setEmbededFont(boolean isEmbededFont) {
		this.isEmbededFont = isEmbededFont;
	}

	/**
	 * 
	 * @return if font is embeded
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
