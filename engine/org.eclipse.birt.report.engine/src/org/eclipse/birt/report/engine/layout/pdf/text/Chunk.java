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

import java.text.Bidi;

import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;

public class Chunk 
{
	private String text;
	private int offset;
	private FontInfo fontInfo;
	private int baseLevel;
	private int runDirection;
	
	public static final Chunk HARD_LINE_BREAK = new Chunk("\n"); //$NON-NLS-1$
	
	public Chunk(String text)
	{
		this.text = text;
		this.fontInfo = null;
		this.baseLevel = Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT;
		this.runDirection = Bidi.DIRECTION_LEFT_TO_RIGHT;
	}
	
	public Chunk(String text, int offset, FontInfo fi)
	{
		this.text = text;
		this.offset = offset;
		this.fontInfo = fi;
		this.baseLevel = Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT;
		this.runDirection = Bidi.DIRECTION_LEFT_TO_RIGHT;
	}
	public Chunk(Chunk chunk)	
	{
		this.text = chunk.text;
		this.fontInfo = chunk.fontInfo;
		this.baseLevel = chunk.baseLevel;
		this.runDirection = chunk.runDirection;
	}
	
	public Chunk(String text, int offset, int baseLevel, int runDirection)
	{
		this.text = text;
		this.offset = offset;
		this.fontInfo = null;
		this.baseLevel = baseLevel;
		this.runDirection = runDirection;
	}
	
	public Chunk(String text, int offset, int baseLevel, int runDirection, FontInfo fi)
	{
		this.text = text;
		this.offset = offset;
		this.fontInfo = null;
		this.baseLevel = baseLevel;
		this.runDirection = runDirection;
		this.fontInfo = fi;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public void setFontInfo(FontInfo fi)
	{
		this.fontInfo = fi;
	}
	
	public FontInfo getFontInfo()
	{
		return this.fontInfo;
	}
	
	public int getOffset()
	{
		return this.offset;
	}
	
	public void setBaseLevel(int baseLevel)
	{
		this.baseLevel = baseLevel;
	}
	
	public int getBaseLevel()
	{
		return this.baseLevel;
	}
	
	public void setRunDirection(int runDirection)
	{
		this.runDirection = runDirection;
	}
	
	public int getRunDirection()
	{
		return this.runDirection;
	}
	
}
