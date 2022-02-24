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

import java.io.OutputStream;
import java.util.Map;

/**
 * Settings for rendering a report to an output format. Currently supported
 * options include format of the request, whether to render HTML with style
 * sheet, etc. Potential future options includes image formats in PDF (vector or
 * bitmap), font embedding options, etc.
 * <p>
 * The predefined options strings are what BIRT supports. Other options are
 * available depending on custom extensions.
 */
public class RenderOption extends TaskOption implements IRenderOption {

	/**
	 * constructor
	 */
	public RenderOption() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param options
	 */
	public RenderOption(Map options) {
		super(options);
	}

	/**
	 * Constructor.
	 * 
	 * @param options
	 */
	public RenderOption(IRenderOption options) {
		this(options.getOptions());
	}

	/**
	 * returns the output settings
	 * 
	 * @return the output settings
	 * @deprecated user should always use the get/set to change the setting.
	 */
	public Map getOutputSetting() {
		return options;
	}

	/**
	 * returns the output format, i.e., html, pdf, etc.
	 * 
	 * @return Returns the output format
	 */
	public String getOutputFormat() {
		return getStringOption(OUTPUT_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFormat(java.lang.
	 * String)
	 */
	public void setOutputFormat(String format) {
		setOption(OUTPUT_FORMAT, format);
	}

	/**
	 * Get emitter id.
	 */
	public String getEmitterID() {
		return getStringOption(EMITTER_ID);
	}

	/**
	 * Set emitter id.
	 * 
	 * @param emitterId emitter id
	 */
	public void setEmitterID(String emitterId) {
		setOption(EMITTER_ID, emitterId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputStream(java.io.
	 * OutputStream)
	 */
	public void setOutputStream(OutputStream ostream) {
		setOption(OUTPUT_STREAM, ostream);
	}

	/**
	 * Get output stream
	 * 
	 * @return output stream
	 */
	public OutputStream getOutputStream() {
		Object out = getOption(OUTPUT_STREAM);
		if (out instanceof OutputStream) {
			return (OutputStream) out;
		}
		return null;
	}

	/**
	 * Set name of the output file.
	 * 
	 * @param outputFileName name of the output file
	 */
	public void setOutputFileName(String outputFileName) {
		setOption(OUTPUT_FILE_NAME, outputFileName);
	}

	/**
	 * Get name of the output file.
	 * 
	 * @return output file name
	 */
	public String getOutputFileName() {
		return getStringOption(OUTPUT_FILE_NAME);
	}

	/**
	 * @param formats - the image format supported by the browser
	 */
	public void setSupportedImageFormats(String formats) {
		setOption(SUPPORTED_IMAGE_FORMATS, formats);
	}

	/**
	 * @return the image format supported by the browser
	 */
	public String getSupportedImageFormats() {
		return getStringOption(SUPPORTED_IMAGE_FORMATS);
	}

	/**
	 * Returns the base URL for creating an Action URL
	 * 
	 * @return the baseURL.
	 */
	public String getBaseURL() {
		return getStringOption(BASE_URL);
	}

	/**
	 * sets the base url for action handling
	 * 
	 * @param baseURL sets the base URL used for action handling
	 */
	public void setBaseURL(String baseURL) {
		setOption(BASE_URL, baseURL);
	}

	/**
	 * Returns the app base url for URL images
	 * 
	 * @return appBaseUrl
	 */
	public String getAppBaseURL() {
		return getStringOption(APP_BASE_URL);
	}

	/**
	 * Set app base url
	 * 
	 * @param appBaseURL the app base url
	 */
	public void setAppBaseURL(String appBaseUrl) {
		setOption(APP_BASE_URL, appBaseUrl);
	}

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setActionHandle(IHTMLActionHandler handler) {
		setActionHandler(handler);
	}

	public void setActionHandler(IHTMLActionHandler handler) {
		setOption(ACTION_HANDLER, handler);
	}

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLActionHandler getActionHandle() {
		return getActionHandler();
	}

	public IHTMLActionHandler getActionHandler() {
		Object handler = getOption(ACTION_HANDLER);
		if (handler instanceof IHTMLActionHandler) {
			return (IHTMLActionHandler) handler;
		}
		return null;
	}

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setImageHandle(IHTMLImageHandler handler) {
		setImageHandler(handler);
	}

	public void setImageHandler(IHTMLImageHandler handler) {
		setOption(IMAGE_HANDLER, handler);
	}

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLImageHandler getImageHandle() {
		return getImageHandler();
	}

	public IHTMLImageHandler getImageHandler() {
		Object handler = getOption(IMAGE_HANDLER);
		if (handler instanceof IHTMLImageHandler) {
			return (IHTMLImageHandler) handler;
		}
		return null;
	}

	/**
	 * If the output stream needs to be closed on exit.
	 */
	public boolean needCloseOutputStreamOnExit() {
		return getBooleanOption(CLOSE_OUTPUTSTREAM_ON_EXIT, true);
	}

	/**
	 * Get flag indicates if the output stream needs to be closed on exit.
	 * 
	 * @param closeOnExit
	 */
	public void closeOutputStreamOnExit(boolean closeOnExit) {
		setOption(CLOSE_OUTPUTSTREAM_ON_EXIT, Boolean.valueOf(closeOnExit));
	}
}
