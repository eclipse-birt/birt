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
 * Defines render options for PDF emitter
 */
public interface IPDFRenderOption extends IRenderOption
{
	/**
	 * define the font directory used by PDF.
	 */
	public static final String FONT_DIRECTORY = "pdfRenderOption.fontDirectory";

	/**
	 * should the true-type font be embedded into the generated PDF file. 
	 */
	public static final String IS_EMBEDDED_FONT = "pdfRenderOption.embeddedFonts";
	
	public static final String FIT_TO_PAGE = "pdfRenderOption.fitToPage";
	public static final String PAGEBREAK_PAGINATION_ONLY = "pdfRenderOption.pagebreakPaginationOnly";

	/**
	 * PDF_TEXT_WRAPPING, if it is set to false, all the text should be displayed into one line,
	 * so there is no need to do the wrapping.
	 */
	public static final String PDF_TEXT_WRAPPING = "pdfRenderOption.textWrapping";
	
	/**
	 * If it is set to false, we needn¡¯t check if the character exits in the selected font.
	 */
	public static final String PDF_FONT_SUBSTITUTION = "pdfRenderOption.fontSubstitution";
	
	/**
	 * If it is set to false, no BIDI processing is used.
	 */
	public static final String PDF_BIDI_PROCESSING = "pdfRenderOption.bidiProcessing";
	
	/**
	 * 
	 * @param isEmbededFont
	 */
	public void setEmbededFont( boolean isEmbededFont );
	/**
	 * 
	 * @return if font is embeded
	 */
	public boolean isEmbededFont( );

	/**
	 * 
	 * @return the user-defined font directory
	 */
	public String getFontDirectory( );

	/**
	 * 
	 * @param fontDirectory
	 *            the user-defined font directory
	 */
	public void setFontDirectory( String fontDirectory );
}
