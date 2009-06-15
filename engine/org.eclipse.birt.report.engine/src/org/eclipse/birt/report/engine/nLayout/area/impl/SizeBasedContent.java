package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.content.IContent;

public class SizeBasedContent
{
	public IContent content;
	public int floatPos;
	public int offsetInContent;
	public int dimension;
	
	public SizeBasedContent( )
	{
		
	}

	public SizeBasedContent( IContent content, int floatPos, int offset, int dimension )
	{
		this.content = content;
		this.floatPos = floatPos;
		this.offsetInContent = offset;
		this.dimension = dimension;
	}
}
