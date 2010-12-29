package org.eclipse.birt.report.engine.emitter.excel.layout;


public class ContainerSizeInfo
{
	// start point and the width in points
	private int start, width;
	
	public ContainerSizeInfo(int start, int width)
	{
		this.start = start;
		this.width = width;
	}

	
	public int getStartCoordinate( )
	{
		return AxisProcessor.round( start );
	}
	
	public int getWidth( )
	{
		return AxisProcessor.round( width );
	}
	
	public int getEndCoordinate()
	{
		return AxisProcessor.round( start + width );
	}
}
