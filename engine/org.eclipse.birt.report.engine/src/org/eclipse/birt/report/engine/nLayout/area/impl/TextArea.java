/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;

import com.ibm.icu.text.Bidi;
import com.lowagie.text.Font;

public class TextArea extends AbstractArea implements ITextArea
{

	protected String text;

	protected int runLevel;

	protected TextStyle style;

	/**
	 * the character numbers in the TextArea.
	 */
	protected int textLength;

	/**
	 * the offset relative to the TextContent, which indicates from where the
	 * TextArea starts.
	 */
	protected int offset;

	/**
	 * checks if line break happens
	 */
	protected boolean lineBreak;
	
	/**
	 * flag to show if the line is blank
	 */
	protected boolean blankLine = false;

	/**
	 * the max width of the TextArea( in 1/1000 points )
	 */
	protected int maxWidth;

	TextArea( TextArea area )
	{
		super( area );
		this.text = area.text;
		this.runLevel = area.runLevel;
		this.style = area.style;
		this.textLength = area.textLength;
		this.offset = area.offset;
	}
	
	public int getBaseLine()
	{
		if(style!=null)
		{
			return style.getFontInfo( ).getBaseline( );
		}
		return super.getBaseLine( );
	}

	public TextArea( String text, TextStyle style )
	{
		this.text = text;
		this.style = style;
		this.height = (int) ( style.getFontInfo( ).getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}
	
	public TextArea( TextStyle style )
	{
		this.style = style;
		this.height = (int) ( style.getFontInfo( ).getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
	}

	public void setRunLevel( int runLevel )
	{
		this.runLevel = runLevel;
	}

	public void setOffset( int offset )
	{
		this.offset = offset;
	}

	public boolean isLineBreak( )
	{
		return lineBreak;
	}

	public void setLineBreak( boolean lineBreak )
	{
		this.lineBreak = lineBreak;
	}

	public int getTextLength( )
	{
		return textLength;
	}

	private String calculateText( )
	{
		if( blankLine || text == null )
		{
			return "";
		}
		else
		{
			return text.substring( offset, offset + textLength );
		}
	}

	public void addWord( int textLength, float wordWidth )
	{
		this.textLength += textLength;
		this.width += wordWidth;
	}

	public void addWordSpacing( int wordSpacing )
	{
		this.width += wordSpacing;
	}

	public boolean hasSpace( int width )
	{
		return maxWidth - this.width > width;
	}

	public boolean isEmpty( )
	{
		return textLength == 0;
	}

	public int getMaxWidth( )
	{
		return maxWidth;
	}

	public void setMaxWidth( int maxWidth )
	{
		this.maxWidth = maxWidth;
	}

	public void setStyle( TextStyle style )
	{
		this.style = style;
	}

	public TextStyle getStyle( )
	{
		return style;
	}

	public String getLogicalOrderText( )
	{
		return calculateText( );
	}

	/**
	 * Gets the text in visual order.
	 * 
	 * @param text
	 *            the original text.
	 * @return the text in visual order.
	 */
	public String getText( )
	{

		if ( ( runLevel & 1 ) == 0 )
		{
			return calculateText( );
		}
		else
		{
			return flip( calculateText( ) );
		}
	}

	public void setTextLength( int textLength )
	{
		this.textLength = textLength;
	}

	private String flip( String text )
	{
		return Bidi
				.writeReverse( text, Bidi.OUTPUT_REVERSE | Bidi.DO_MIRRORING );
	}

	public int getRunLevel( )
	{
		return runLevel;
	}

	public TextStyle getTextStyle( )
	{
		return style;
	}

	public void accept( IAreaVisitor visitor )
	{
		visitor.visitText( this );
	}

	public int getWidth( )
	{
		int fontStyle = style.getFontInfo( ).getFontStyle( );
		// get width for text with simulated italic font.
		if ( style.getFontInfo( ).getSimulation( )
				&& ( Font.ITALIC == fontStyle || Font.BOLDITALIC == fontStyle ) )
		{
			width = (int) ( width + height
					* EmitterUtil.getItalicHorizontalCoefficient( ) );
		}
		return width;
	}

	public int getTextWidth( String text )
	{
		FontInfo fontInfo = style.getFontInfo( );
		if ( null != fontInfo )
		{
			return (int) ( style.getFontInfo( ).getWordWidth( text ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
		}
		else
		{
			return 0;
		}
	}

	public TextArea cloneArea( )
	{
		return new TextArea( this );
	}

}
