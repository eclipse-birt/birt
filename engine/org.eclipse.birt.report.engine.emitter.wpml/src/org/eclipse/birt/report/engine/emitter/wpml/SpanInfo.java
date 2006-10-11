package org.eclipse.birt.report.engine.emitter.wpml;

import org.eclipse.birt.report.engine.content.IStyle;



public class SpanInfo
{
	public SpanInfo(int x, int cs, int width, boolean start, IStyle style)
	{
		this.x = x;
		this.cs = cs;
		this.width = width;
		this.start = start;
		this.style = style;		
	}
	
	int x =0 ;
	int cs = 0;
	int width = 0;
	boolean start = false;
	
	IStyle style = null;	
}
