
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
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;

public class PDFTableGroupLM extends PDFGroupLM
		implements
			IBlockStackingLayoutManager
{

	protected PDFTableLM tableLM = null;

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
				if ( !isFinished( ) && needPageBreakBefore( ) )
				{
					itsTableLM.setTableCloseStateAsForced( );
				}
				else if ( isFinished( ) && needPageBreakAfter( ) )
				{
					itsTableLM.setTableCloseStateAsForced( );
				}
			}
		}
		return childBreak;
	}

	protected void repeatHeader( )
	{
		if ( isFirst )
		{
			isFirst = false;
			IArea tocAnchor = AreaFactory.createTableGroupArea( (IGroupContent) content );
			tableLM.addArea( tocAnchor );
			tableLM.setRepeatCount( tableLM.getRepeatCount( ) + 1 );
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
				&& tableRegion.getHeight( ) < this.getMaxAvaHeight( )
						- currentBP )
		{
			// add to root
			Iterator iter = tableRegion.getChildren( );
			RowArea row = null;
			int count = 0;
			while ( iter.hasNext( ) )
			{
				row = (RowArea) iter.next( );
				addArea( row );
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

	public int getCurrentBP( )
	{
		return parent.getCurrentBP( );
	}

	protected boolean submitRoot( boolean childBreak )
	{
		return true;
	}

	public int getCurrentIP( )
	{
		return parent.getCurrentIP( );
	}

	public int getMaxAvaHeight( )
	{
		return parent.getMaxAvaHeight( );
	}

	public int getMaxAvaWidth( )
	{
		return parent.getMaxAvaWidth( );
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

	public void setMaxAvaHeight( int height )
	{
		parent.setMaxAvaHeight( height );
	}

	public void setMaxAvaWidth( int width )
	{
		parent.setMaxAvaWidth( width );
	}

	public void setOffsetX( int x )
	{
		parent.setOffsetX( x );
	}

	public void setOffsetY( int y )
	{
		parent.setOffsetY( y );
	}

	public boolean addArea( IArea area )
	{
		return parent.addArea( area );
	}

	protected void createRoot( )
	{
		// do nothing
	}

	protected void newContext( )
	{

	}

	protected IReportItemExecutor createExecutor( )
	{
		return executor;
	}

}
