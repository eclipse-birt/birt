package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;

import org.eclipse.birt.report.engine.content.IStyle;
import org.w3c.dom.css.CSSValue;


public class BorderInfo
{
	public static final int TOP_BORDER = 0;
	public static final int RIGHT_BORDER = 1;
	public static final int BOTTOM_BORDER = 2;
	public static final int LEFT_BORDER = 3;
	
	public int startX, startY, endX, endY;
	public int borderWidth;
	public Color borderColor;
	public String borderStyle;
	public int borderType;

	public BorderInfo( int startX, int startY, int endX, int endY,
			int borderWidth, Color borderColor, CSSValue borderStyle,
			int borderType )
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		if ( IStyle.DOUBLE_VALUE.equals( borderStyle ) )
		{
			this.borderStyle = "double";
		}
		else if ( IStyle.DASHED_VALUE.equals( borderStyle ) )
		{
			this.borderStyle = "dashed";
		}
		else if ( IStyle.DOTTED_VALUE.equals( borderStyle ) )
		{
			this.borderStyle = "dotted";
		}
		else
		{
			this.borderStyle = "solid";
		}
		this.borderType = borderType;
	}
	
	public BorderInfo( int startX, int startY, int endX, int endY,
			int borderWidth, Color borderColor, String borderStyle,
			int borderType )
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		this.borderStyle = borderStyle;
		this.borderType = borderType;
	}
}
