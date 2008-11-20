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

package org.eclipse.birt.report.engine.layout.pdf.font;

import org.eclipse.birt.report.engine.layout.PDFConstants;

import com.lowagie.text.pdf.BaseFont;

public class FontInfo
{
	private BaseFont bf;

	private float fontSize;

	private int fontStyle;

	private float fontPadding;

	private float lineWidth;

	private boolean simulation;

	public FontInfo( BaseFont bf, float fontSize, int fontStyle,
			boolean simulation )
	{
		this.bf = bf;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.fontPadding = fontSize / 5f;
		this.lineWidth = fontSize / 20f;
		this.simulation = simulation;
	}

	public FontInfo( FontInfo fontInfo )
	{
		this.bf = fontInfo.bf;
		this.fontSize = fontInfo.fontSize;
		this.fontStyle = fontInfo.fontStyle;
		this.fontPadding = fontInfo.fontSize / 5f;
		this.lineWidth = fontInfo.fontSize / 20f;
		this.simulation = fontInfo.simulation;
	}

	public void setBaseFont( BaseFont bf )
	{
		this.bf = bf;
	}

	public void setFontSize( float fontSize )
	{
		this.fontSize = fontSize;
		this.fontPadding = this.fontSize / 5f;
		this.lineWidth = this.fontSize / 20f;
	}

	public void setFontStyle( int fontStyle )
	{
		this.fontStyle = fontStyle;
	}

	public void setSimulation( boolean simulation )
	{
		this.simulation = simulation;
	}

	public BaseFont getBaseFont( )
	{
		return this.bf;
	}

	public float getFontSize( )
	{
		return this.fontSize;
	}

	public int getFontStyle( )
	{
		return this.fontStyle;
	}

	public boolean getSimulation( )
	{
		return this.simulation;
	}

	public float getLineWidth( )
	{
		return this.lineWidth;
	}

	public int getOverlinePosition( )
	{
		// float awtAscent = bf.getFontDescriptor(BaseFont.AWT_ASCENT,
		// fontSize);
		// float ascent = bf.getFontDescriptor(BaseFont.ASCENT, fontSize);
		return (int) ( ( fontPadding / 2f - lineWidth / 2f ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	public int getUnderlinePosition( )
	{
		float awtAscent = bf.getFontDescriptor( BaseFont.AWT_ASCENT, fontSize );
		float awtDescent = -bf.getFontDescriptor( BaseFont.AWT_DESCENT,
				fontSize );
		return (int) ( ( awtAscent + awtDescent + lineWidth / 2f ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	public int getLineThroughPosition( )
	{
		float awtAscent = bf.getFontDescriptor( BaseFont.AWT_ASCENT, fontSize );
		float ascent = bf.getFontDescriptor( BaseFont.ASCENT, fontSize );
		float descent = -bf.getFontDescriptor( BaseFont.DESCENT, fontSize );
		return (int) ( ( awtAscent + fontPadding / 2f - ascent + ( ascent + descent ) / 2.0f ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	public int getBaseline( )
	{
		return (int) ( ( bf.getFontDescriptor( BaseFont.AWT_ASCENT, fontSize ) + fontPadding / 2f ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	/**
	 * Gets the width of the specified word.
	 * 
	 * @param word
	 *            the word
	 * @return the points of the width
	 */
	public float getWordWidth( String word )
	{
		if ( bf == null || word == null )
			return 0;
		//FIXME the width should consider the italic/bold font style.
		return bf.getWidthPoint( word, fontSize );
	}

	/**
	 * Gets the height of the specified word.
	 * 
	 * @return the height of the font, it equals ascent+|descent|+leading
	 */
	public float getWordHeight( )
	{
		if ( bf == null )
			return fontSize;
		return bf.getFontDescriptor( BaseFont.AWT_ASCENT, fontSize )
				- bf.getFontDescriptor( BaseFont.AWT_DESCENT, fontSize )
				+ bf.getFontDescriptor( BaseFont.AWT_LEADING, fontSize )
				+ fontSize / 4f;
	}

	public String getFontName( )
	{
		assert bf != null;
		String[][] familyFontNames = bf.getFamilyFontName();
		String[] family = familyFontNames[familyFontNames.length - 1];
		return family[family.length - 1];
	}
}