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

package org.eclipse.birt.report.engine.layout.pdf.text;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.ISplitter;
import org.eclipse.birt.report.engine.layout.pdf.font.FontSplitter;

public class ChunkGenerator
{	
	private ITextContent textContent;
	private boolean bidiProcessing;
	private boolean fontSubstitution;
	private String text;
	
	private ISplitter bidiSplitter = null;
	private ISplitter fontSplitter = null;
	private String format = null;
	
	public ChunkGenerator(ITextContent textContent, 
			boolean bidiProcessing, boolean fontSubstitution, String format)
	{
		this.textContent = textContent;
		this.text = textContent.getText();
		this.bidiProcessing = bidiProcessing;
		this.fontSubstitution = fontSubstitution;
		this.format = format;
		
		if (text == null || text.length()==0)
			return;
		if (bidiProcessing)
		{
			bidiSplitter = new BidiSplitter(new Chunk(text));
		}
		
		if (null==bidiSplitter)
		{
			fontSplitter = new FontSplitter(new Chunk(text), 
					textContent, fontSubstitution, format);
		}
		else
		{
			if (bidiSplitter.hasMore())
			{
				fontSplitter = new FontSplitter(bidiSplitter.getNext(), 
						textContent, fontSubstitution, format);
			}	
		}		
				
	}
	
	public boolean hasMore()
	{
		if (text == null || text.length()==0)
			return false;
		if (bidiProcessing)
		{
			if (null == bidiSplitter)
				return false;
			if (bidiSplitter.hasMore())
				return true;
		}
		if (null == fontSplitter)
			return false;
		if (fontSplitter.hasMore())
			return true;
		else
			return false;	
	}
	
	public Chunk getNext()
	{		
		while ( null != fontSplitter )
		{
			if (fontSplitter.hasMore())
			{
				return fontSplitter.getNext();
			}else
			{
				fontSplitter = null;
			}
			if ( null != bidiSplitter && bidiSplitter.hasMore())
			{
				fontSplitter = new FontSplitter(bidiSplitter.getNext(), 
						textContent, fontSubstitution, format);
			}else
			{
				return null;
			}	
		}
		return null;
	}
}