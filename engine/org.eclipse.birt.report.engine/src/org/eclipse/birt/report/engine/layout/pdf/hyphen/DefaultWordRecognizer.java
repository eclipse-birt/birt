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

package org.eclipse.birt.report.engine.layout.pdf.hyphen;

import java.util.HashSet;

public class DefaultWordRecognizer implements IWordRecognizer
{

	final static HashSet excludedSplitChars = new HashSet( );
	final static HashSet includedSplitChars = new HashSet( );

	static
	{
		excludedSplitChars.add( new Character( ' ' ) );
		includedSplitChars.add( new Character( (char) 0x0A ) );
	}

	protected int start;

	protected String text;

	protected char splitChar;

	protected Word lastWord = null;

	protected Word currentWord = null;

	public DefaultWordRecognizer( String text )
	{
		this.text = text;

	}

	public int getLastWordEnd( )
	{
		return lastWord == null ? 0 : lastWord.getEnd( );
	}

	public Word getNextWord( )
	{
		lastWord = currentWord;
		if ( start > text.length( ) - 1 )
		{
			return null;
		}

		for ( int i = start; i < text.length( ); i++ )
		{
			Character c = new Character( text.charAt( i ) );
			if ( excludedSplitChars.contains( c ) )
			{
				currentWord = new Word( text, start, i + 1 );
				start = i + 1;
				return currentWord;
			}
			else if ( includedSplitChars.contains( c ) )
			{
				if(i==start)
				{
					currentWord = new Word(text, start, i + 1);
					start = i + 1;
					return currentWord;
				}
				else
				{
					currentWord = new Word( text, start, i );
					start = i;
					return currentWord;
				}
			}
		}
		currentWord = new Word( text, start, text.length( ) );
		start = text.length( );

		return currentWord;

	}

}
