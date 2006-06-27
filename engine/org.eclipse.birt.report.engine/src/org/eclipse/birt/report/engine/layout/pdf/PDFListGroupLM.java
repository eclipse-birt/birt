
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.content.ListContainerExecutor;

public class PDFListGroupLM extends PDFGroupLM
		implements
			IBlockStackingLayoutManager
{

	public PDFListGroupLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
	}

	protected IListBandContent getHeader( )
	{
		return (IListBandContent) ( (IGroupContent) content ).getHeader( );
	}

	protected IReportItemExecutor createExecutor( )
	{
		return new ListContainerExecutor( content, executor );
	}

	protected void repeatHeader( )
	{
		if ( isFirst )
		{
			isFirst = false;
			return;
		}
		if ( !isRepeatHeader( ) )
		{
			return;
		}
		IListBandContent band = getHeader( );
		if ( band == null )
		{
			return;
		}
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor( band );
		headerExecutor.execute( );
		ContainerArea headerArea = (ContainerArea) AreaFactory
				.createLogicContainer( );
		headerArea.setAllocatedWidth( parent.getMaxAvaWidth( ) );
		PDFRegionLM regionLM = new PDFRegionLM( context, headerArea, band,
				emitter, headerExecutor );
		boolean allowPB = context.allowPageBreak( );
		context.setAllowPageBreak( false );
		regionLM.layout( );
		context.setAllowPageBreak( allowPB );
		if ( headerArea.getAllocatedHeight( ) + currentBP < parent
				.getMaxAvaHeight( ) )
		{
			addArea( headerArea );
			repeatCount++;
		}

	}

	protected void createRoot( )
	{
		root = (ContainerArea) AreaFactory.createBlockContainer( content );
	}
	
	protected void newContext()
	{
		super.newContext( );
		repeatCount = 0;
	}

}
