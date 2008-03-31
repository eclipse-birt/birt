package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.report.engine.content.IContent;


public class InlineContainerLayout extends InlineStackingLayout
		implements
			IInlineStackingLayout
{

	public InlineContainerLayout( LayoutEngineContext context,
			ContainerLayout parentContext, IContent content )
	{
		super( context, parentContext, content );
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void closeLayout( )
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createRoot( )
	{
		// TODO Auto-generated method stub

	}

	@Override
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
