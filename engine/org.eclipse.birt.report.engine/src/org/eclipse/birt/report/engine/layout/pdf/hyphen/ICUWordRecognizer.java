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

import java.util.Locale;

import com.ibm.icu.text.BreakIterator;


public class ICUWordRecognizer implements IWordRecognizer
{
	protected BreakIterator wordBreaker = null;
	protected Word lastWord = null;
	protected Word currentWord = null;
	protected String text;
	int end;
	
	
	public ICUWordRecognizer( String text, Locale locale )
	{
		if(locale!=null)
		{
			wordBreaker = BreakIterator.getWordInstance(locale);
		}
		else
		{
			wordBreaker = BreakIterator.getWordInstance(Locale.getDefault( ));
		}
		this.text = text;
		wordBreaker.setText( text);

	}
	public int getLastWordEnd( )
	{
		return wordBreaker.current( )==0? 0: wordBreaker.current( )-1;
	}

	public Word getNextWord( )
	{
		int start = wordBreaker.current( );
		end = wordBreaker.next( );
		if(end!=BreakIterator.DONE)
		{
			return new Word(text, start, end ); 
		}
		else
		{
			return null;
		}
	}
}
