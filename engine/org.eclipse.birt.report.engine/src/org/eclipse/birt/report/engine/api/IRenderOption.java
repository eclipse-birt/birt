/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
 * Defines render options for emitters
 */
public interface IRenderOption extends ITaskOption {

	/**
	 * Property of format
	 */
	String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$

	/**
	 * Property of output format HTML
	 */
	String OUTPUT_FORMAT_HTML = "html"; //$NON-NLS-1$

	/**
	 * Property of output format PDF
	 */
	String OUTPUT_FORMAT_PDF = "pdf"; //$NON-NLS-1$

	/**
	 * Property of output format FO
	 */
	String OUTPUT_FORMAT_FO = "fo"; //$NON-NLS-1$

	/**
	 * Property of emitter id HTML
	 */
	String OUTPUT_EMITTERID_HTML = "org.eclipse.birt.report.engine.emitter.html";

	/**
	 * Property of emitter id PDF
	 */
	String OUTPUT_EMITTERID_PDF = "org.eclipse.birt.report.engine.emitter.pdf";

	/**
	 * Property of emitter id
	 */
	String EMITTER_ID = "emitterID"; //$NON-NLS-1$

	/**
	 * Property of output file
	 */
	String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$
	/**
	 * Property of output stream
	 */
	String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	/**
	 * Property of image handler
	 */
	String IMAGE_HANDLER = "imageHandler"; //$NON-NLS-1$

	/**
	 * Property of action handler
	 */
	String ACTION_HANDLER = "actionHandler"; //$NON-NLS-1$

	/**
	 * Property for local
	 */
	String LOCALE = "locale"; //$NON-NLS-1$

	/** The DPI which layout engine uses to convert pixel to an abstract length */
	String RENDER_DPI = "RenderDpi";

	/** The DPI which chart engine uses to generate charts */
	String CHART_DPI = "ChartDpi";

	/**
	 * Supported image formats
	 */
	String SUPPORTED_IMAGE_FORMATS = "supportedImageFormats";

	/**
	 * Property of base URL
	 */
	String BASE_URL = "baseUrl";

	/**
	 * bidi_hcg: Should we output report as RTL. The value is a Boolean Object, the
	 * default is <code>Boolean.FALSE</code>.
	 */
	String RTL_FLAG = "RTLFlag"; //$NON-NLS-1$

	/**
	 * APP_BASE_URL is the same as BASE_URL
	 */
	String APP_BASE_URL = BASE_URL; // $NON-NLS-1$

	/**
	 * Property to set output display to none
	 */
	String OUTPUT_DISPLAY_NONE = "org.eclipse.birt.report.engine.api.IRenderOption.outputDisplayNone";

	/**
	 * Should the report contains paginations The value is a Boolean object, default
	 * is <code>Boolean.FALSE</code>.
	 */
	String HTML_PAGINATION = "htmlPagination"; //$NON-NLS-1$

	/**
	 * Should the output stream be closed when render task exits.The value is a
	 * Boolean object, default is <code>Boolean.FALSE</code>
	 */
	String CLOSE_OUTPUTSTREAM_ON_EXIT = "closeOutputStreamOnExit";

	/**
	 * Property for report size
	 */
	String REPORTLET_SIZE = "reportletSize";

	/**
	 * Get the output settings
	 *
	 * @deprecated use getOptions instead
	 * @return Return the output settings
	 */
	@Deprecated
	Map<String, Object> getOutputSetting();

	/**
	 * Set output format.
	 *
	 * @param format
	 */
	void setOutputFormat(String format);

	/**
	 * Get output format.
	 *
	 * @return Return the output format
	 */
	String getOutputFormat();

	/**
	 * Set name of the output file.
	 *
	 * @param outputFileName name of the output file
	 */
	void setOutputFileName(String outputFileName);

	/**
	 * Get name of the output file.
	 *
	 * @return name
	 */
	String getOutputFileName();

	/**
	 * Set output stream.
	 *
	 * @param ostream
	 */
	void setOutputStream(OutputStream ostream);

	/**
	 * Get output stream.
	 *
	 * @return output stream
	 */
	OutputStream getOutputStream();

	/**
	 * @param formats - the image format supported by the browser
	 */
	void setSupportedImageFormats(String formats);

	/**
	 * @return the image format supported by the browser
	 */
	String getSupportedImageFormats();

	/**
	 * Returns the base URL for creating an Action URL
	 *
	 * @return the baseURL.
	 */
	String getBaseURL();

	/**
	 * sets the base url for action handling
	 *
	 * @param baseURL sets the base URL used for action handling
	 */
	void setBaseURL(String baseURL);

	/**
	 * Returns the app base url for URL images
	 *
	 * @return appBaseUrl
	 */
	String getAppBaseURL();

	/**
	 * Set app base url
	 *
	 * @param appBaseURL the app base url
	 */
	void setAppBaseURL(String appBaseURL);

	/**
	 * Set the action handler
	 *
	 * @deprecated
	 * @param handler
	 */
	@Deprecated
	void setActionHandle(IHTMLActionHandler handler);

	/**
	 * Set the action handler
	 *
	 * @param handler
	 */
	void setActionHandler(IHTMLActionHandler handler);

	/**
	 * Get the action handler
	 *
	 * @deprecated
	 * @return Return the action handler
	 */
	@Deprecated
	IHTMLActionHandler getActionHandle();

	/**
	 * Get the action handler
	 *
	 * @return Return the action handler
	 */
	IHTMLActionHandler getActionHandler();

	/**
	 * Set the image handler
	 *
	 * @deprecated
	 * @param handler
	 */
	@Deprecated
	void setImageHandle(IHTMLImageHandler handler);

	/**
	 * Set the image handler
	 *
	 * @param handler
	 */
	void setImageHandler(IHTMLImageHandler handler);

	/**
	 * Get the image handler
	 *
	 * @deprecated
	 * @return Return the image handler
	 */
	@Deprecated
	IHTMLImageHandler getImageHandle();

	/**
	 * Get the image handler
	 *
	 * @return Return the image handler
	 */
	IHTMLImageHandler getImageHandler();

	/**
	 * returns the emitter id
	 *
	 * @return Returns the emitter id
	 */
	String getEmitterID();

	/**
	 * sets the emitter id
	 *
	 * @param emitterID
	 */
	void setEmitterID(String emitterID);

}
