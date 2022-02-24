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

package org.eclipse.birt.report.engine.extension;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Represents the extended item presentation time extension.
 * 
 * The calling sequence in presentation engine might work as follows:
 * <p>
 * <li>Design engine creates a new instance of the extended item.
 * <li>Presentation engine detects that the element is an extended item. It
 * dynamically creates an object with type IReportItemPresentation.
 * <li>The presentation engine calls various setXXX methods to pass
 * initialization parameters to the extension implementation.
 * <li>deserialize method restores the generation time state
 * <li>Render the extended item through onRowSets method, handled the returned
 * image
 * <li>Call finish() for cleanup.
 */

public interface IReportItemPresentation {

	public static int OUTPUT_NONE = 0;
	public static int OUTPUT_AS_IMAGE = 1; // Only this format is supported for
											// now
	public static int OUTPUT_AS_TEXT = 2;
	public static int OUTPUT_AS_HTML_TEXT = 3;
	public static int OUTPUT_AS_DRAWING = 4;
	public static int OUTPUT_AS_CUSTOM = 5;
	public static int OUTPUT_AS_IMAGE_WITH_MAP = 6;
	public static int OUTPUT_AS_UNKNOWN = 7;

	/**
	 * @since BIRT 2.3
	 * @param info Presentation info of report item
	 */
	public abstract void init(IReportItemPresentationInfo info);

	/**
	 * passes a handle to the extended report item model to the extension
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead
	 * @param modelHandle a handle to the extended item model object
	 */
	public abstract void setModelObject(ExtendedItemHandle modelHandle);

	/**
	 * passes the class loader used to load user defined classes.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param loader class loader used to load the classes
	 */
	public abstract void setApplicationClassLoader(ClassLoader loader);

	/**
	 * pass the script context to the report item.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param context report context used by java-based script
	 */
	public abstract void setScriptContext(IReportContext context);

	/**
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * 
	 *             pass the prepared query definition to extended item
	 *             implementation, so that it can access data.
	 */
	public void setReportQueries(IDataQueryDefinition[] queries);

	/**
	 * passes the locale used in the presentation.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param locale locale
	 */
	public void setLocale(Locale locale);

	/**
	 * passes the dpi (dot per inch) from the rendering environment to the
	 * extension. Mostly used for printing.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param dpi the dpi of the rendering environment
	 */
	public abstract void setResolution(int dpi);

	/**
	 * sets the output format, i.e., HTML, PDF, etc.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param outputFormat the output format, i.e., html, pdf, etc.
	 */
	public abstract void setOutputFormat(String outputFormat);

	/**
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param ah the HTML action handler used to create a URL based on an action
	 */
	public abstract void setActionHandler(IHTMLActionHandler ah);

	/**
	 * @return the image MIME type (e.g. "image/svg+xml")
	 */
	public abstract String getImageMIMEType();

	/**
	 * sets the image formats that are supported for this output format. Formats are
	 * separated by semi-colon. For example, the argument could be JPG;PNG;BMP;SVG
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param supportedImageFormats the image formats that the presentation engine
	 *                              could support.
	 */
	public abstract void setSupportedImageFormats(String supportedImageFormats);

	/**
	 * deserializes generation time state information about the extended item
	 * 
	 * @param istream the input stream to deserialize generation time state from
	 */
	public void deserialize(InputStream istream);

	/**
	 * returns the output type, which could be IMAGE, TEXT, HTML TEXT, DRAWING, etc.
	 * For now, only Image is supported.
	 * 
	 * @param mimeType an out parameter that returns the MIME type of the output
	 * @return output type, for now OUTPUT_AS_IMAGE only
	 */
	public int getOutputType();

	/**
	 * processes the extended item in report presentation environment.
	 * 
	 * @deprecated since BIRT 2.3
	 * @param rowSets rowSets an array of row sets that is passed to the extension
	 * @return the returned value could be different depending on the type of the
	 *         output. For image, returns an input stream or byte array that the
	 *         engine could retrieve data from
	 * @throws BirtException throws exception when there is a problem processing the
	 *                       extended item
	 */
	public abstract Object onRowSets(IRowSet[] rowSets) throws BirtException;

	/**
	 * Process the extended item. It is called in render time.
	 * 
	 * @param results results is an array of query results which is passed to the
	 *                extended item. The extended item could retrieve data from
	 *                those results.
	 * @return The returned value could be different depending on the type of the
	 *         output. For image, returns an input stream or byte array.
	 * @throws BirtException Throws exception when there is a problem processing the
	 *                       extended item
	 */
	public abstract Object onRowSets(IBaseResultSet[] results) throws BirtException;

	/**
	 * Get the size of the extended item. The size is a Dimension object. The width
	 * and height can only be in absolute units (inch, mm, etc.) or pixel. It can
	 * not be a relative size such as 150% or 1.2em. Notice that an extended item
	 * can obtain its design-time size information by querying DE. This function is
	 * needed because the actual size may not be the same as the design-time size.
	 * 
	 * @return the size of the extended item. Return null if the size does not
	 *         matter or can not be determined.
	 */
	public Size getSize();

	/**
	 * Performs clean up work
	 */
	public void finish();

	/**
	 * @deprecated implement #init(IReportItemPresentationInfo) instead. Set dynamic
	 *             style.
	 */
	public void setDynamicStyle(IStyle style);

	/**
	 * Set the content which is transformed from extended item. Extended item can
	 * process some properties itself, such as bookmark, style etc.
	 * 
	 * @deprecated implement #init(IReportItemPresentationInfo) instead.
	 * @param content content which is transformed from extended item.
	 */
	public void setExtendedItemContent(IContent content);

	public IReportItemPresentationInfo getPresentationConfig();

	/**
	 * Check if can support cache in current situation. For example, the chart image
	 * is can't cached when export to some format and some property of chart is
	 * changed(e.g. by script), such as height, width etc.
	 * 
	 * @return true if can support cache, otherwise false
	 */
	public boolean isCacheable();
}
