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

package org.eclipse.birt.report.engine.extension;

import java.io.InputStream;
import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Represents the extended item presentation time extension.
 * 
 * The calling sequence in presentation engine might work as follows:
 * <p>
 * <li> Design engine creates a new instance of the extended item.
 * <li> Presentation engine detects that the element is an extended item. It
 * dynamically creates an object with type IReportItemPresentation.
 * <li> The presentation engine calls various setXXX methods to pass
 * initialization parameters to the extension implementation.
 * <li> deserialize method restores the generation time state
 * <li> Render the extended item through onRowSets method, handled the returned
 * image
 * <li> Call finish() for cleanup.
 */

public interface IReportItemPresentation
{

	public static int OUTPUT_NONE = 0;
	public static int OUTPUT_AS_IMAGE = 1; // Only this format is supported for
											// now
	public static int OUTPUT_AS_TEXT = 2;
	public static int OUTPUT_AS_HTML_TEXT = 3;
	public static int OUTPUT_AS_DRAWING = 4;
	public static int OUTPUT_AS_CUSTOM = 5;
	public static int OUTPUT_AS_IMAGE_WITH_MAP = 6;

	/**
	 * passes a handle to the extended report item model to the extension
	 * 
	 * @param modelHandle
	 *            a handle to the extended item model object
	 */
	public abstract void setModelObject( ExtendedItemHandle modelHandle );

	/**
	 * pass the script context to the report item.
	 * 
	 * @param context
	 *            report context used by java-based script
	 */
	public abstract void setScriptContext( IReportContext context );

	/**
	 * pass the prepared query definition to extended item implementation, so
	 * that it can access data.
	 */
	public void setReportQueries( IBaseQueryDefinition[] queries );

	/**
	 * passes the locale used in the presentation.
	 * 
	 * @param locale
	 *            locale
	 */
	public void setLocale( Locale locale );

	/**
	 * passes the dpi (dot per inch) from the rendering environment to the
	 * extension. Mostly used for printing.
	 * 
	 * @param dpi
	 *            the dpi of the rendering environment
	 */
	public abstract void setResolution( int dpi );

	/**
	 * sets the output format, i.e., HTML, PDF, etc.
	 * 
	 * @param outputFormat
	 *            the output format, i.e., html, pdf, etc.
	 */
	public abstract void setOutputFormat( String outputFormat );

	/**
	 * @return the image MIME type (e.g. "image/svg+xml")
	 */
	public abstract String getImageMIMEType( );

	/**
	 * sets the image formats that are supported for this output format. Formats
	 * are separated by semi-colon. For example, the argument could be
	 * JPG;PNG;BMP;SVG
	 * 
	 * @param supportedImageFormats
	 *            the image formats that the presentation engine could support.
	 */
	public abstract void setSupportedImageFormats( String supportedImageFormats );

	/**
	 * deserializes generation time state information about the extended item
	 * 
	 * @param istream
	 *            the input stream to deserialize generation time state from
	 */
	public void deserialize( InputStream istream );

	/**
	 * returns the output type, which could be IMAGE, TEXT, HTML TEXT, DRAWING,
	 * etc. For now, only Image is supported.
	 * 
	 * @param mimeType
	 *            an out parameter that returns the MIME type of the output
	 * @return output type, for now OUTPUT_AS_IMAGE only
	 */
	public int getOutputType( );

	/**
	 * processes the extended item in report presentation environment.
	 * 
	 * @param rowSets
	 *            rowSets an array of row sets that is passed to the extension
	 * @return the returned value could be different depending on the type of
	 *         the output. For image, returns an input stream or byte array that
	 *         the engine could retrieve data from
	 * @throws BirtException
	 *             throws exception when there is a problem processing the
	 *             extended item
	 */
	public abstract Object onRowSets( IRowSet[] rowSets ) throws BirtException;

	/**
	 * Get the size of the extended item. The size is a Dimension object. The
	 * width and height can only be in absolute units (inch, mm, etc.) or pixel.
	 * It can not be a relative size such as 150% or 1.2em. Notice that an
	 * extended item can obtain its design-time size information by querying DE.
	 * This function is needed because the actual size may not be the same as
	 * the design-time size.
	 * 
	 * @return the size of the extended item. Return null if the size does not
	 *         matter or can not be determined.
	 */
	public Size getSize( );

	/**
	 * Performs clean up work
	 */
	public void finish( );
}
