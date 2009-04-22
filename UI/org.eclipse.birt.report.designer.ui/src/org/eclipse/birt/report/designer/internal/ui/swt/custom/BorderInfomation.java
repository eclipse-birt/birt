package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.swt.graphics.RGB;


public class BorderInfomation
{
	public static final String BORDER_LEFT = "left"; //$NON-NLS-1$
	public static final String BORDER_TOP = "top"; //$NON-NLS-1$
	public static final String BORDER_RIGHT = "right"; //$NON-NLS-1$
	public static final String BORDER_BOTTOM= "bottom"; //$NON-NLS-1$
	
	
	String position;
	String style;
	RGB color;
	String width;
	
	public String getPosition( )
	{
		return position;
	}
	
	public void setPosition( String position )
	{
		this.position = position;
	}
	
	public String getStyle( )
	{
		return style;
	}
	
	public void setStyle( String style )
	{
		this.style = style;
	}
	
	public RGB getColor( )
	{
		return color;
	}
	
	public void setColor( RGB color )
	{
		this.color = color;
	}
	
	public String getWidth( )
	{
		return width;
	}
	
	public void setWidth( String width )
	{
		this.width = width;
	}
}
