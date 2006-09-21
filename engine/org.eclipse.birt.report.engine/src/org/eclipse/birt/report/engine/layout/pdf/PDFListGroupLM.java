
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
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
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
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
		if(!isCurrentDetailBand())
		{
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
				.createLogicContainer( content.getReportContent( ) );
		headerArea.setAllocatedWidth( parent.getMaxAvaWidth( ) );
		PDFRegionLM regionLM = new PDFRegionLM( context, headerArea, band,
				headerExecutor );
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

	protected boolean isCurrentDetailBand()
	{
		if(child!=null)
		{
			IContent c = child.getContent( );
			if(c!=null)
			{
				if(c instanceof IGroupContent)
				{
					return true;
				}
				IElement p = c.getParent( );
				if(p instanceof IBandContent)
				{
					IBandContent band = (IBandContent)p;
					if(band.getBandType( )==IBandContent.BAND_DETAIL)
					{
						return true;
					}
				}
				
					
			}
		}
		else
		{
			return true;
		}
		return false;
	}
}
