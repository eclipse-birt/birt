/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_HTML = "html"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_PDF = "pdf"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_FO = "fo"; //$NON-NLS-1$
	public static final String OUTPUT_EMITTERID_HTML = "org.eclipse.birt.report.engine.emitter.html";
	public static final String OUTPUT_EMITTERID_PDF = "org.eclipse.birt.report.engine.emitter.pdf";
	public static final String EMITTER_ID = "emitterID"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$
	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	public static final String IMAGE_HANDLER = "imageHandler"; //$NON-NLS-1$
	public static final String ACTION_HANDLER = "actionHandler"; //$NON-NLS-1$

	public static final String LOCALE = "locale"; //$NON-NLS-1$

	/** The DPI which layout engine uses to convert pixel to an abstract length */
	public static final String RENDER_DPI = "RenderDpi";

	/** The DPI which chart engine uses to generate charts */
	public static final String CHART_DPI = "ChartDpi";

	public static final String SUPPORTED_IMAGE_FORMATS = "supportedImageFormats";

	public static final String BASE_URL = "baseUrl";

	/**
	 * bidi_hcg: Should we output report as RTL. The value is a Boolean Object, the
	 * default is <code>Boolean.FALSE</code>.
	 */
	public static final String RTL_FLAG = "RTLFlag"; //$NON-NLS-1$

	/**
	 * APP_BASE_URL is the same as BASE_URL
	 */
	public static final String APP_BASE_URL = BASE_URL; // $NON-NLS-1$

	public static final String OUTPUT_DISPLAY_NONE = "org.eclipse.birt.report.engine.api.IRenderOption.outputDisplayNone";

	/**
	 * Should the report contains paginations The value is a Boolean object, default
	 * is <code>Boolean.FALSE</code>.
	 */
	public static final String HTML_PAGINATION = "htmlPagination"; //$NON-NLS-1$

	/**
	 * Should the output stream be closed when render task exits.The value is a
	 * Boolean object, default is <code>Boolean.FALSE</code>
	 */
	public static final String CLOSE_OUTPUTSTREAM_ON_EXIT = "closeOutputStreamOnExit";

	public static final String REPORTLET_SIZE = "reportletSize";

	/**
	 * @return
	 * @deprecated use getOptions instead
	 */
	public Map getOutputSetting();

	/**
	 * Set output format.
	 * 
	 * @param format
	 */
	public void setOutputFormat(String format);

	/**
	 * Get output format.
	 * 
	 * @return
	 */
	public String getOutputFormat();

	/**
	 * Set name of the output file.
	 * 
	 * @param outputFileName name of the output file
	 */
	public void setOutputFileName(String outputFileName);

	/**
	 * Get name of the output file.
	 * 
	 * @return name
	 */
	public String getOutputFileName();

	/**
	 * Set output stream.
	 * 
	 * @param ostream
	 */
	public void setOutputStream(OutputStream ostream);

	/**
	 * Get output stream.
	 * 
	 * @return output stream
	 */
	public OutputStream getOutputStream();

	/**
	 * @param formats - the image format supported by the browser
	 */
	public void setSupportedImageFormats(String formats);

	/**
	 * @return the image format supported by the browser
	 */
	public String getSupportedImageFormats();

	/**
	 * Returns the base URL for creating an Action URL
	 * 
	 * @return the baseURL.
	 */
	public String getBaseURL();

	/**
	 * sets the base url for action handling
	 * 
	 * @param baseURL sets the base URL used for action handling
	 */
	public void setBaseURL(String baseURL);

	/**
	 * Returns the app base url for URL images
	 * 
	 * @return appBaseUrl
	 */
	public String getAppBaseURL();

	/**
	 * Set app base url
	 * 
	 * @param appBaseURL the app base url
	 */
	public void setAppBaseURL(String appBaseURL);

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setActionHandle(IHTMLActionHandler handler);

	public void setActionHandler(IHTMLActionHandler handler);

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLActionHandler getActionHandle();

	public IHTMLActionHandler getActionHandler();

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setImageHandle(IHTMLImageHandler handler);

	public void setImageHandler(IHTMLImageHandler handler);

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLImageHandler getImageHandle();

	public IHTMLImageHandler getImageHandler();

	/**
	 * returns the emitter id
	 * 
	 * @return Returns the emitter id
	 */
	public String getEmitterID();

	/**
	 * sets the emitter id
	 * 
	 * @param emitterID
	 */
	public void setEmitterID(String emitterID);

}