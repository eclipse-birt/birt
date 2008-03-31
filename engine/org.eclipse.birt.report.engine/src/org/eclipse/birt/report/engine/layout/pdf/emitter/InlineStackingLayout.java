package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;


public class InlineStackingLayout extends ContainerLayout implements IInlineStackingLayout
{

	public InlineStackingLayout( LayoutEngineContext context, ContainerLayout parentContext,
			IContent content )
	{
		super(context, parentContext, content );
	}
	
	public boolean addArea(AbstractArea area)
	{
		root.addChild( area );
		area.setAllocatedPosition( currentIP + offsetX, currentBP + offsetY );
		currentIP += area.getAllocatedWidth( );
		if ( currentIP + area.getAllocatedWidth( ) > root.getContentWidth( ))
		{
			root.setNeedClip( true );
		}
		else if( currentBP > maxAvaHeight )
		{
			root.setNeedClip( true );
		}
		return true;
	}

	protected void closeLayout( )
	{
		// TODO Auto-generated method stub
		
	}

	protected void createRoot( )
	{
		// TODO Auto-generated method stub
		
	}

	protected void initialize( )
	{
		// TODO Auto-generated method stub
		
	}

	public boolean endLine( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int getMaxLineWidth( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmptyLine( )
	{
		// TODO Auto-generated method stub
		return false;
	}

}
