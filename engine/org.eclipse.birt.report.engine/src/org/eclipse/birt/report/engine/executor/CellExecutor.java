
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class CellExecutor extends QueryItemExecutor
{
	protected CellExecutor( ExecutorManager manager )
	{
		super( manager );
	}
	
	int cellId;
	
	public void reset( )
	{
		super.reset( );
		this.cellId = 0;
	}

	public IContent execute( )
	{
		CellDesign cellDesign = (CellDesign)getDesign();
		
		ICellContent cellContent = report.createCellContent( );
		setContent(cellContent);
		
		executeQuery( );
		initializeContent( cellDesign, cellContent );

		//cellContent.setColumn( cellDesign.getColumn( ) );
		//cellContent.setColSpan( cellDesign.getColSpan( ) );
		//cellContent.setRowSpan( cellDesign.getRowSpan( ) );

		processAction( cellDesign, cellContent );
		processBookmark( cellDesign, cellContent );
		processStyle( cellDesign, cellContent );
		processVisibility( cellDesign, cellContent );

		cellContent.setDisplayGroupIcon( cellDesign.getDisplayGroupIcon( ) );
		
		if ( context.isInFactory( ) )
		{
			handleOnCreate( cellContent );
		}

		startTOCEntry( cellContent );

		if (emitter != null)
		{
			emitter.startCell( cellContent );
		}
		//prepare to execute the children.
		currentItem = 0;
		
		return content;
	}

	public void close( )
	{
		ICellContent cellContent = (ICellContent)getContent();
		if (emitter != null)
		{
			emitter.endCell( cellContent);
		}
		finishTOCEntry( );
		closeQuery( );
		manager.releaseExecutor( ExecutorManager.CELLITEM, this );
	}
	
	private int currentItem = 0;

	public boolean hasNextChild()
	{
		CellDesign cellDesign = (CellDesign) getDesign();
		return currentItem < cellDesign.getContentCount( );
	}
	
	public IReportItemExecutor getNextChild( )
	{
		CellDesign cellDesign = (CellDesign) getDesign();
		if ( currentItem < cellDesign.getContentCount( ) )
		{
			ReportItemDesign itemDesign = cellDesign.getContent( currentItem++ );
			ReportItemExecutor executor = manager.createExecutor( this,  
					itemDesign);
			return executor;
		}
		return null;
	}
}
