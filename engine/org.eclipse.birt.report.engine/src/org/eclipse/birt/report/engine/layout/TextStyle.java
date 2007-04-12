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

package org.eclipse.birt.report.engine.layout;

import java.awt.Color;

import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.w3c.dom.css.CSSValue;


public class TextStyle
{
	private FontInfo fontInfo;
	private float characterSpacing;
	private float wordSpacing;
	private Color color;
	private boolean linethrough;
	private boolean overline;
	private boolean underline;
	private CSSValue align;
	
	public TextStyle(FontInfo fontInfo, float characterSpacing,
			float wordSpacing, Color color, boolean linethrough,
			boolean overline, boolean underline, CSSValue align)
	{
		this.fontInfo = fontInfo;
		this.characterSpacing = characterSpacing;
		this.wordSpacing = wordSpacing;
		this.color = color;
		this.linethrough = linethrough;
		this.overline = overline;
		this.underline = underline;
		this.align = align;
	}

	
	public FontInfo getFontInfo( )
	{
		return fontInfo;
	}

	
	public void setFontInfo( FontInfo fontInfo )
	{
		this.fontInfo = fontInfo;
	}

	
	public float getCharacterSpacing( )
	{
		return characterSpacing;
	}

	
	public void setCharacterSpacing( float characterSpacing )
	{
		this.characterSpacing = characterSpacing;
	}

	
	public float getWordSpacing( )
	{
		return wordSpacing;
	}

	
	public void setWordSpacing( float wordSpacing )
	{
		this.wordSpacing = wordSpacing;
	}

	
	public Color getColor( )
	{
		return color;
	}

	
	public void setColor( Color color )
	{
		this.color = color;
	}

	
	public boolean isLinethrough( )
	{
		return linethrough;
	}

	
	public void setLinethrough( boolean linethrough )
	{
		this.linethrough = linethrough;
	}

	
	public boolean isOverline( )
	{
		return overline;
	}

	
	public void setOverline( boolean overline )
	{
		this.overline = overline;
	}

	
	public boolean isUnderline( )
	{
		return underline;
	}

	
	public void setUnderline( boolean underline )
	{
		this.underline = underline;
	}

	
	public CSSValue getAlign( )
	{
		return align;
	}

	
	public void setAlign( CSSValue align )
	{
		this.align = align;
	}
}
