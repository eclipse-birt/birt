package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;


public abstract class ContainerLayout extends Layout
{
	
	protected ContainerArea root;
	
	protected int currentIP = 0;
	
	protected int currentBP = 0;
		
	protected int maxAvaHeight = 0;
	
	protected int maxAvaWidth = 0;
	
	protected int offsetX = 0;

	protected int offsetY = 0;

	
	public ContainerLayout( LayoutEngineContext context, ContainerLayout parentContext, IContent content )
	{
		super(context, parentContext, content);
	}
	
	public void layout()
	{
		
	}
	
	public abstract boolean addArea(AbstractArea area);
	
	public int getMaxAvaHeight()
	{
		return maxAvaHeight;
	}
	
	public int getCurrentMaxContentWidth( )
	{
		return maxAvaWidth - currentIP;
	}
	
	public int getCurrentMaxContentHeight()
	{
		return maxAvaHeight - currentBP;
	}


	public int getCurrentIP( )
	{
		return currentIP;
	}

	public int getCurrentBP( )
	{
		return this.currentBP;
	}


	public int getOffsetX( )
	{
		return offsetX;
	}


	public int getOffsetY( )
	{
		return offsetY;
	}



	protected boolean isRootEmpty( )
	{
		return !( root != null && root.getChildrenCount( ) > 0 );
	}



	protected abstract void createRoot( );
		
	
	
}
