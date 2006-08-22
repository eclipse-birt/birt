
package org.eclipse.birt.report.engine.layout.html;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.emitter.BufferedReportEmitter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.TableContentLayout;
import org.eclipse.birt.report.engine.internal.content.wrap.CellContentWrapper;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;

public class HTMLTableLayoutEmitter extends ContentEmitterAdapter
{

	/**
	 * If the nested table need to be layouted.
	 */
	private boolean needNestedLayout;

	/**
	 * the current table layout
	 */
	private TableContentLayout layout;
	
	private Stack layoutEvents;

	/**
	 * the emitter used to output the table content
	 */
	private IContentEmitter emitter;

	/**
	 * emitter used to
	 */
	private IContentEmitter cellEmitter;

	private Stack groupStack = new Stack( );

	public HTMLTableLayoutEmitter( IContentEmitter emitter )
	{
		this.emitter = emitter;
		this.needNestedLayout = false;
	}

	public HTMLTableLayoutEmitter( IContentEmitter emitter,
			boolean needNestedLayout )
	{
		this.emitter = emitter;
		this.needNestedLayout = needNestedLayout;
	}

	public void end( IReportContent report )
	{
		emitter.end( report );
	}

	public String getOutputFormat( )
	{
		return emitter.getOutputFormat( );
	}

	public void initialize( IEmitterServices service )
	{
		emitter.initialize( service );
	}

	public void start( IReportContent report )
	{
		emitter.start( report );
	}

	
	public void startTable( ITableContent table )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startTable( table );
		}
		else
		{
			this.layout = new TableContentLayout( table,
					EngineIRConstants.FORMAT_TYPE_VIEWER );
			this.layoutEvents = new Stack();

			emitter.startTable( table );
		}
	}

	public void endTable( ITableContent table )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTable( table );
		}
		else
		{
			assert layout != null;
			resolveAll( );
			flush( );
			emitter.endTable( table );
			layout = null;
			layoutEvents = null;
		}
	}

	private static class LayoutEvent
	{
		final static int START_GROUP = 0;
		final static int START_BAND = 1;
		final static int END_GROUP = 2;
		final static int END_BAND = 3;
		final static int ON_ROW = 4;
		
		LayoutEvent(int type, Object value )
		{
			this.eventType= type;
			this.value = value;
		}
		int eventType;
		Object value;
	}
	
	public void startTableGroup( ITableGroupContent group )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startTableGroup( group );
		}
		else
		{
			int groupLevel = group.getGroupLevel( );
			groupStack.push( new Integer( groupLevel ) );
			layoutEvents
					.push( new LayoutEvent( LayoutEvent.START_GROUP, group ) );
		}
	}

	public void endTableGroup( ITableGroupContent group )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTableGroup( group );
		}
		else
		{
			// if there is no group footer, we still need to do with the drop.
			int groupLevel = getGroupLevel();
			resolveCellsOfDrop( groupLevel, false );
			resolveCellsOfDrop( groupLevel, true );
			assert !groupStack.isEmpty( );
			groupStack.pop( );
			// ContentEmitterUtil.endContent( group, emitter );
			layoutEvents.push( new LayoutEvent( LayoutEvent.END_GROUP, group ) );
		}
	}

	private int getGroupLevel( )
	{
		if ( !groupStack.isEmpty( ) )
		{
			return ( (Integer) groupStack.peek( ) ).intValue( );
		}
		return -1;
	}

	public void startTableBand( ITableBandContent band )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startTableBand( band );
		}
		else
		{
			// ContentEmitterUtil.startContent( band, emitter );
			if (band.getBandType( ) == IBandContent.BAND_GROUP_FOOTER )
			{
				int groupLevel = getGroupLevel();
				resolveCellsOfDrop( groupLevel, false );
			}
			layoutEvents.push( new LayoutEvent(LayoutEvent.START_BAND, band ));
		}
	}

	public void endTableBand( ITableBandContent band )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTableBand( band );
		}
		else
		{
			// ContentEmitterUtil.endContent( band, emitter );
			if ( band.getBandType() == IBandContent.BAND_GROUP_FOOTER )
			{
				int groupLevel = getGroupLevel();
				resolveCellsOfDrop( groupLevel, true );
			}
			if ( layout.hasDropCell( ) )
			{
				layoutEvents.push( new LayoutEvent(LayoutEvent.END_BAND, band ));
			}
			else
			{
				flush( );
				ContentEmitterUtil.endContent( band, emitter );
			}
		}
	}

	public void startRow( IRowContent row )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startRow( row );
		}
		else
		{
			layoutEvents.push( new LayoutEvent( LayoutEvent.ON_ROW,
					new Integer( layout.getRowCount( ) ) ) );
			layout.createRow( row );
		}
	}

	public void endRow( IRowContent row )
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endRow( row );
		}
		else
		{
			if ( !layout.hasDropCell( ) )
			{
				flush( );
			}
		}
	}

	int nestCellCount = 0;

	public void startCell( ICellContent cell )
	{
		nestCellCount++;
		if ( cellEmitter != null )
		{
			cellEmitter.startCell( cell );
		}
		else
		{
			BufferedReportEmitter buffer = new BufferedReportEmitter( emitter );
			if ( needNestedLayout )
			{
				cellEmitter = new HTMLTableLayoutEmitter( buffer, true );
			}
			else
			{
				cellEmitter = buffer;
			}
			int colId = cell.getColumn( );
			int colSpan = cell.getColSpan( );
			int rowSpan = cell.getRowSpan( );

			// the current executed cell is rowIndex, columnIndex
			// get the span value of that cell.
			CellDesign cellDesign = (CellDesign) cell.getGenerateBy( );
			if ( cellDesign != null )
			{
				String dropType = cellDesign.getDrop( );
				if ( dropType != null && !"none".equals( dropType ) )
				{
					rowSpan = createDropID( getGroupLevel( ), dropType );
				}
			}
			layout.createCell( colId, rowSpan, colSpan, new CellContent( cell,
					buffer ) );
		}
	}

	public void endCell( ICellContent cell )
	{
		assert cellEmitter != null;
		nestCellCount--;
		if ( nestCellCount == 0 )
		{
			cellEmitter = null;
		}
		else
		{
			cellEmitter.endCell( cell );
		}
	}

	public void startContent( IContent content )
	{
		if ( cellEmitter != null )
		{
			ContentEmitterUtil.startContent( content, cellEmitter );
		}
		else
		{
			ContentEmitterUtil.startContent( content, emitter );
		}
	}

	public void endContent( IContent content )
	{
		if ( cellEmitter != null )
		{
			ContentEmitterUtil.endContent( content, cellEmitter );
		}
		else
		{
			ContentEmitterUtil.endContent( content, emitter );
		}
	}

	private int createDropID( int groupIndex, String dropType )
	{
		int dropId = -10 * ( groupIndex + 1 );
		if ( "all".equals( dropType ) )
		{
			dropId--;
		}
		return dropId;
	}

	public void resolveAll( )
	{
		layout.resolveDropCells( );
	}

	public void resolveCellsOfDrop( int groupLevel, boolean dropAll )
	{
		if ( dropAll )
		{
			layout.resolveDropCells( createDropID( groupLevel, "all" ) );
		}
		else
		{
			layout.resolveDropCells( createDropID( groupLevel, "detail" ) );
		}
	}

	public void flush( )
	{
		Iterator iter = layoutEvents.iterator( );
		while ( iter.hasNext( ) )
		{
			LayoutEvent event = (LayoutEvent) iter.next( );
			switch ( event.eventType )
			{
				case LayoutEvent.START_GROUP :
				case LayoutEvent.START_BAND :
					ContentEmitterUtil.startContent( (IContent) event.value,
							emitter );
					break;
				case LayoutEvent.END_GROUP :
				case LayoutEvent.END_BAND :
					ContentEmitterUtil.endContent( (IContent) event.value,
							emitter );
					break;
				case LayoutEvent.ON_ROW :
					flushRow( ( (Integer) event.value ).intValue( ) );
					break;
			}
		}
		layoutEvents.clear( );
		layout.reset( );
	}
	
	protected void flushRow( int rowId)
	{
		int colCount = layout.getColCount( );
		Row row = layout.getRow( rowId );
		IRowContent rowContent = (IRowContent) row.getContent( );
		emitter.startRow( rowContent );
		for ( int j = 0; j < colCount; j++ )
		{
			Cell cell = row.getCell( j );
			if ( cell.getStatus( ) == Cell.CELL_USED )
			{
				CellContent content = (CellContent) cell.getContent( );
				CellContentWrapper tempCell = new CellContentWrapper(
						content.cell ); 
				tempCell.setColumn( cell.getColId( ) );
				tempCell.setRowSpan( cell.getRowSpan( ) );
				tempCell.setColSpan( cell.getColSpan( ) );

				emitter.startCell( tempCell );
				if ( content.buffer != null )
				{
					content.buffer.flush( );
				}
				emitter.endCell( tempCell );
			}
			if ( cell.getStatus( ) == Cell.CELL_EMPTY )
			{
				IReportContent report = rowContent.getReportContent( );
				ICellContent cellContent = report.createCellContent( );
				cellContent.setParent( rowContent );
				cellContent.setColumn( cell.getColId( ) + 1 );
				cellContent.setRowSpan( cell.getRowSpan( ) );
				cellContent.setColSpan( cell.getColSpan( ) );
				emitter.startCell( cellContent );
				emitter.endCell( cellContent );
			}
		}
		emitter.endRow( rowContent );
	}

	private class CellContent implements Cell.Content
	{

		protected ICellContent cell;

		protected BufferedReportEmitter buffer;

		protected CellContent( ICellContent cell, BufferedReportEmitter buffer )
		{
			this.cell = cell;
			this.buffer = buffer;

		}

		public boolean isEmpty( )
		{
			return buffer == null || buffer.isEmpty( );
		}

		public void reset( )
		{
			buffer = null;
		}
	}
}
