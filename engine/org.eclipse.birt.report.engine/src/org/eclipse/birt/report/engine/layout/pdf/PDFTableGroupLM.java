
package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.IPDFTableLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;

public class PDFTableGroupLM extends PDFGroupLM
		implements
			IBlockStackingLayoutManager
{

	protected PDFTableLM tableLM = null;
	protected boolean firstRow = true;

	public PDFTableGroupLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		IPDFTableLayoutManager lm = getTableLayoutManager( );
		assert(lm instanceof PDFTableLM);
		tableLM = (PDFTableLM) lm;
		tableLM.startGroup( (IGroupContent) content );
	}

	protected boolean traverseChildren( )
	{
		boolean childBreak = super.traverseChildren( );
		if ( !childBreak )
		{
			tableLM.endGroup( (IGroupContent) content );
		}
		return childBreak;
	}
	
	protected boolean checkAvailableSpace( )
	{
		boolean availableSpace = super.checkAvailableSpace( );
		if(availableSpace && tableLM != null)
		{
			tableLM.setTableCloseStateAsForced( );
		}
		return availableSpace;
	}
	
	public boolean layout()
	{
		boolean childBreak = super.layout( );
		if ( childBreak )
		{
			IPDFTableLayoutManager itsTableLM = getTableLayoutManager( );
			if ( itsTableLM != null )
			{
				if ( !isFinished( ) && needPageBreakBefore(null ) )
				{
					itsTableLM.setTableCloseStateAsForced( );
				}
				else if ( isFinished( ) && needPageBreakAfter(null ) )
				{
					itsTableLM.setTableCloseStateAsForced( );
				}
			}
		}
		return childBreak;
	}

	protected void repeatHeader( )
	{
		if ( isFirst || tableLM.isFirst )
		{
			isFirst = false;
			return;
			
		}
		if(!isCurrentDetailBand())
		{
			return;
		}
		ITableBandContent header = (ITableBandContent) groupContent.getHeader( );
		if ( !isRepeatHeader( ) || header == null )
		{
			return;
		}
		if ( header.getChildren( ).isEmpty( ) )
		{
			return;
		}
		if ( child != null )
		{
			IContent content = child.getContent( );
			if ( content instanceof ITableBandContent )
			{
				if ( ( (ITableBandContent) content ).getBandType( ) == IBandContent.BAND_GROUP_HEADER )
				{
					return;
				}

			}
		}
		PDFReportLayoutEngine engine = context.getLayoutEngine( );
		PDFLayoutEngineContext con = new PDFLayoutEngineContext( engine );
		con.setFactory( new PDFLayoutManagerFactory( con ) );
		con.setFormat( context.getFormat( ) );
		con.setReport( context.getReport( ) );
		con.setMaxHeight( context.getMaxHeight( ) );
		con.setAllowPageBreak( false );
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor( header );
		headerExecutor.execute( );
		PDFTableRegionLM regionLM = new PDFTableRegionLM( con, tableLM
				.getContent( ), tableLM.getLayoutInfo( ) );
		regionLM.initialize( header, tableLM.lastRowArea);
		regionLM.layout( );
		TableArea tableRegion = (TableArea) tableLM.getContent( ).getExtension(
				IContent.LAYOUT_EXTENSION );
		if ( tableRegion != null
				&& tableRegion.getHeight( ) < getCurrentMaxContentHeight( )	)
		{
			// add to root
			Iterator iter = tableRegion.getChildren( );
			RowArea row = null;
			int count = 0;
			while ( iter.hasNext( ) )
			{
				row = (RowArea) iter.next( );
				addArea( row, false, true );
				count++;
			}
			if ( row != null )
			{
				removeBottomBorder( row );
			}
			tableLM.setRepeatCount( tableLM.getRepeatCount( ) + count );
		}
		tableLM.getContent( ).setExtension( IContent.LAYOUT_EXTENSION, null );
	}

	public boolean addArea( IArea area, boolean keepWithPrevious, boolean keepWithNext )
	{
		if(firstRow)
		{
			firstRow = false;
			IArea tocAnchor = AreaFactory.createTableGroupArea( (IGroupContent) content );
			tableLM.addArea( tocAnchor, false, false );
			tableLM.setRepeatCount( tableLM.getRepeatCount( ) + 1 );
		}
		return parent.addArea(  area, false, false );
	}

	protected void createRoot( )
	{
		// do nothing
	}

	protected void initialize( )
	{

	}

	protected IReportItemExecutor createExecutor( )
	{
		return executor;
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
				else if(c instanceof IBandContent)
				{
					IBandContent band = (IBandContent)c;
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
	
	public void submit(AbstractArea area)
	{
		parent.submit( area );
	}
	
	protected boolean addToRoot(AbstractArea area)
	{
		return parent.addArea( area, false, false );
		/*if(getCurrentBP() + area.getAllocatedHeight( ) <= getMaxAvaHeight())
		{
			parent.addArea( area, false, false );
			return true;
		}
		else
		{
			return false;
		}*/
	}
	
	public int getCurrentBP( )
	{
		return parent.getCurrentBP( );
	}
	

	public int getCurrentIP( )
	{
		return parent.getCurrentIP( );
	}


	public int getCurrentMaxContentHeight()
	{
		return parent.getCurrentMaxContentHeight( );
	}
	public int getCurrentMaxContentWidth( )
	{
		return parent.getCurrentMaxContentWidth( );
	}

	public int getOffsetX( )
	{
		return parent.getOffsetX( );
	}

	public int getOffsetY( )
	{
		return parent.getOffsetY( );
	}

	public void setCurrentBP( int bp )
	{
		parent.setCurrentBP( bp );
	}

	public void setCurrentIP( int ip )
	{
		parent.setCurrentIP( ip );
	}

	public void setOffsetX( int x )
	{
		parent.setOffsetX( x );
	}
	
	public int getMaxAvaHeight()
	{
		return parent.getMaxAvaHeight();
	}

	public void setOffsetY( int y )
	{
		parent.setOffsetY( y );
	}
}
