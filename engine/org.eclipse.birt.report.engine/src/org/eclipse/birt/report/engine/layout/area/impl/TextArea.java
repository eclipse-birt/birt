/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import java.text.Bidi;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.ITextArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

public class TextArea extends AbstractArea implements ITextArea
{
	protected String text;
	
	protected FontInfo fi;

	/**
	 * the offset relative to the TextContent, which indicates from where the TextArea starts.
	 */
	private int offset;
	
	private ITextContent textContent;
	
	private int runDirection;
	/** 
	 * checks if line break happens
	 */
	private boolean lineBreak;
	
	/**
	 * flag to show if the line is blank
	 */
	private boolean blankLine = false;
	
	/**
	 * the character numbers in the TextArea.
	 */
	private int textLength;
	
	/**
	 * the max width of the TextArea( in 1/1000 points ) 
	 */
	private int maxWidth;
	
	/**
	 * @deprecated
	 * @param textContent
	 * @param text
	 * @param fi
	 */
	public TextArea( ITextContent textContent, String text, FontInfo fi )
	{
		super(textContent);
		this.textContent = textContent;
		this.text = text;
		this.offset = 0;
		this.textLength = text.length( );
		this.fi = fi;
		height = (int)( fi.getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
		baseLine = this.fi.getBaseline( );
		removePadding( );
		removeBorder( );
		removeMargin( );
	}
	
	public TextArea( ITextContent textContent, FontInfo fi, boolean blankLine )
	{
		super(textContent);
		this.textContent = textContent;
		this.fi = fi;
		height = (int)( fi.getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
		baseLine = this.fi.getBaseline( );
		if( blankLine )
		{
			this.lineBreak = true;
			this.blankLine = true;	
		}
		else
		{
			this.offset = 0;
			this.textLength = textContent.getText( ).length( );
		}
		removePadding( );
		removeBorder( );
		removeMargin( );
	}

	public TextArea( ITextContent textContent, int offset, int baseLevel, int runDirection,
			FontInfo fontInfo )
	{
		super(textContent);
		this.textContent = textContent;
		this.fi = fontInfo;
		height = (int)( fi.getWordHeight( ) * PDFConstants.LAYOUT_TO_PDF_RATIO );
		baseLine = this.fi.getBaseline( );
		this.offset = offset;
		this.runDirection = runDirection;
		this.lineBreak = false;
		removePadding( );
		removeBorder( );
		removeMargin( );
	}

	public boolean lineBreak( )
	{
		return lineBreak;
	}

	public boolean isEmpty( )
	{
		return textLength == 0;
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
	
	private void calculateText( )
	{
		if( blankLine )
		{
			this.text = null;
		}
		else
		{
			this.text = textContent.getText( ).substring( offset,
					offset + textLength );	
		}
	}

	public String getLogicalOrderText( )
	{
		calculateText( );
		return text;
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
		calculateText( );
		if ( runDirection == Bidi.DIRECTION_LEFT_TO_RIGHT )
		{
			return text;
		}
		else
		{
			return flip( text );
		}
	}

	/**
	 * Reverse text
	 * 
	 * @param text
	 * @return
	 */
	private String flip( String text )
	{
		char[] indexChars = text.toCharArray( );
		int start = 0;
		int end = indexChars.length;
		int mid = ( start + end ) / 2;
		--end;
		for ( ; start < mid; ++start, --end )
		{
			char temp = indexChars[start];
			indexChars[start] = indexChars[end];
			indexChars[end] = temp;
		}
		return new String( indexChars );
	}

	public FontInfo getFontInfo( )
	{
		return this.fi;
	}
	
	public void accept(IAreaVisitor visitor)
	{
		visitor.visitText(this);
	}

	public int getTextLength( )
	{
		return textLength;
	}
	
	public void setTextLength( int textLength )
	{
		this.textLength = textLength;
	}

	public boolean isLineBreak( )
	{
		return lineBreak;
	}
	
	public void setLineBreak( boolean lineBreak )
	{
		this.lineBreak = lineBreak;
	}

	public int getMaxWidth( )
	{
		return maxWidth;
	}

	public void setMaxWidth( int maxWidth )
	{
		this.maxWidth = maxWidth;
	}

	
	public boolean isBlankLine( )
	{
		return blankLine || textLength == 0;
	}
	
}
