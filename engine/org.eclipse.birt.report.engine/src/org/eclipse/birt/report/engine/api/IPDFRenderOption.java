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
 * Defines render options for PDF emitter.
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
		
	/**
	 * Properties to control how to output the content 
	 * if the content exceeds the page-size.
  	 *  CLIP_CONTENT:             clip the content
  	 *	FIT_TO_PAGE_SIZE:         scale the content to fit into the page
  	 *	OUTPUT_TO_MULTIPLE_PAGES: divided the content into multiple pages
  	 *	ENLARGE_PAGE_SIZE:        enlarge the page size to contain all the content.
	 */
	public static final String PAGE_OVERFLOW = "pdfRenderOption.pageOverfow";
	
	public static final int CLIP_CONTENT = 1;
	public static final int FIT_TO_PAGE_SIZE = 2;
	public static final int OUTPUT_TO_MULTIPLE_PAGES = 4;
	public static final int ENLARGE_PAGE_SIZE = 8;
	
	public static final String FIT_TO_PAGE = "pdfRenderOption.fitToPage";
	public static final String PAGEBREAK_PAGINATION_ONLY = "pdfRenderOption.pagebreakPaginationOnly";

	/**
	 * PDF_TEXT_WRAPPING, if it is set to false, all the text should be displayed into one line,
	 * so there is no need to do the wrapping.
	 */
	public static final String PDF_TEXT_WRAPPING = "pdfRenderOption.textWrapping";
	
	/**
	 * If it is set to false, we needn't check if the character exists in the selected font.
	 */
	public static final String PDF_FONT_SUBSTITUTION = "pdfRenderOption.fontSubstitution";
	
	/**
	 * If it is set to false, no BIDI processing is used.
	 */
	public static final String PDF_BIDI_PROCESSING = "pdfRenderOption.bidiProcessing";
	
	/**
	 * If it is set to false, no hyphenation is used. 
	 * Any words longer than the line width will be clipped at the line boundary.
	 */
	public static final String PDF_HYPHENATION = "pdfRenderOption.hyphenation";
	
	/**
	 * 
	 * @param isEmbededFont
	 */
	public void setEmbededFont( boolean isEmbededFont );
	/**
	 * 
	 * @return if font is embedded
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
