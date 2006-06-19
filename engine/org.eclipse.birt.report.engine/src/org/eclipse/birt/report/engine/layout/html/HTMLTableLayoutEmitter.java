
package org.eclipse.birt.report.engine.layout.html;

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
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;

public class HTMLTableLayoutEmitter extends ContentEmitterAdapter
{

	/**
	 * the current table layout
	 */
	private TableContentLayout layout;

	/**
	 * the emitter used to output the table content
	 */
	private IContentEmitter emitter;

	/**
	 * emitter used to
	 */
	private BufferedReportEmitter cellEmitter;

	private Stack groupStack = new Stack( );

	public HTMLTableLayoutEmitter( IContentEmitter emitter )
	{
		this.emitter = emitter;
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
		}
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
			// ContentEmitterUtil.startContent( group, emitter );
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
			assert !groupStack.isEmpty( );
			groupStack.pop( );
			// ContentEmitterUtil.endContent( group, emitter );
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
			cellEmitter = new BufferedReportEmitter( emitter );
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
					cellEmitter ) );
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
		int rowCount = layout.getRowCount( );
		int colCount = layout.getColCount( );
		for ( int i = 0; i < rowCount; i++ )
		{
			Row row = layout.getRow( i );
			IRowContent rowContent = (IRowContent) row.getContent( );
			emitter.startRow( rowContent );
			for ( int j = 0; j < colCount; j++ )
			{
				Cell cell = row.getCell( j );
				if ( cell.getStatus( ) == Cell.CELL_USED )
				{
					CellContent content = (CellContent) cell.getContent( );
					int oColumn = content.cell.getColumn( );
					int oRowSpan = content.cell.getRowSpan( );
					int oColSpan = content.cell.getColSpan( );
					content.cell.setColumn( cell.getColId( ) );
					content.cell.setRowSpan( cell.getRowSpan( ) );
					content.cell.setColSpan( cell.getColSpan( ) );

					emitter.startCell( content.cell );
					if ( content.buffer != null )
					{
						content.buffer.flush( );
						// content.buffer.clear( );
					}
					emitter.endCell( content.cell );
					content.cell.setColumn( oColumn );
					content.cell.setRowSpan( oRowSpan );
					content.cell.setColSpan( oColSpan );

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
		layout.reset( );
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
