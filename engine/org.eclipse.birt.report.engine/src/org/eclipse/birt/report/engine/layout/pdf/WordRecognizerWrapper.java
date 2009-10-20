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
package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Locale;

import org.eclipse.birt.report.engine.layout.pdf.hyphen.ICUWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.IWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Word;

public class WordRecognizerWrapper implements IWordRecognizer
{
	private String text;
	private ICUWordRecognizer wr = null;

	private Word currentWord = null;

	private int start = 0;
	private int end = 0;

	private static final String KEEP_WITH_NEXT_CHARS = "(<{[（《«“$￥";
	private static final String KEEP_WITH_LAST_CHARS = ")>}]）》»”,.;:/!? ，。；：！？";
	
	//FIXME: for quotes across chunks, the algorithm has some problem.
	private static final String KEEP_WITH_DEPENDS = "\"'";
	private boolean leftQuote = true;
	
	public WordRecognizerWrapper( String text, Locale locale )
	{
		this.text = text;
		wr = new ICUWordRecognizer( text, locale );
	}

	public Word getNextWord( )
	{
		start = end;
		if ( start == text.length( ) )
			return null;
		keepWithNext( );
		keepWithLast( );
		return new Word( text, start, end );
	}

	public boolean hasWord( )
	{
		return end != text.length( );
	}
	
	public int getLastWordEnd( )
	{
		return end;
	}
	
	private void keepWithNext( )
	{
		if ( !genCurrentICUWord( ) )
		{
			return;
		}
		
		// current word is a char must keep with next.
		if (currentWord.getLength( ) == 1)
		{
			if ( KEEP_WITH_NEXT_CHARS.indexOf( currentWord.getValue( ) ) != -1 )
			{
				end = currentWord.getEnd( );
				keepWithNext( );
			}
			else if ( KEEP_WITH_DEPENDS.indexOf( currentWord.getValue( ) ) != -1 
					&& leftQuote )
			{
				end = currentWord.getEnd( );
				leftQuote = false;
				keepWithNext( );
			}
		}
		if ( null != currentWord )
		{
			end = currentWord.getEnd( );
		}
	}

	private void keepWithLast( )
	{
		if ( !genCurrentICUWord( ) )
		{
			return;
		}
		
		if ( currentWord.getLength( ) == 1 )
		{
			// current word is a char must keep with last
			if ( KEEP_WITH_LAST_CHARS.indexOf( currentWord.getValue( ) ) != -1 )
			{
				end = currentWord.getEnd( );
				keepWithLast( );
			}
			else if ( KEEP_WITH_DEPENDS.indexOf( currentWord.getValue( ) ) != -1 
					&& !leftQuote )
			{
				end = currentWord.getEnd( );
				leftQuote = true;
				keepWithLast( );
			}
		}
	}
	
	private boolean genCurrentICUWord( )
	{
		// the text has not be handled yet.
		if ( null == currentWord )
		{
			currentWord = wr.getNextWord( );
			if ( null == currentWord )
				return false;
			else
				return true;
		}
		// last word has been eaten up.
		if ( end == currentWord.getEnd( ) )
		{
			currentWord = wr.getNextWord( );
			if ( null == currentWord )
				return false;
			else
				return true;
		}
		// last word has already been generated, but has not been used.
		return true;
	}

}
