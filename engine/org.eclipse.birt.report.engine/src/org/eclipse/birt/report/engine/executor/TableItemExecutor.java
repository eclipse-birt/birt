/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.BirtTimer;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumnContent;
import org.eclipse.birt.report.engine.content.IReportItemContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ColumnContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.emitter.buffer.BufferedReportEmitter;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.IContent;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.Table;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * Defines execution logic for a List report item.
 * <p>
 * Currently table header and footer do not support data items
 * 
 * @version $Revision: 1.18 $ $Date: 2005/05/23 11:57:54 $
 */
public class TableItemExecutor extends ListingElementExecutor
{

	protected static Logger logger = Logger.getLogger( TableItemExecutor.class
			.getName( ) );

	/**
	 * the table design
	 */
	protected TableItemDesign table;
	/**
	 * current report emitter;
	 */
	protected IReportEmitter emitter;

	/**
	 * emitter used to output/layout table.
	 */
	protected ITableEmitter tableEmitter;

	/**
	 * layout emitter used to layout the cells if there exist drop cells.
	 */
	protected LayoutTableEmitter layoutTableEmitter;

	protected TABLEINFO tableInfo;
	/**
	 * current executed group index. used to indicate the level of drop cells
	 */
	protected int groupIndex;

	/**
	 * current executed row index. the row index is the order of row which shows
	 * on the screen.
	 */
	protected int rowIndex;

	/**
	 * current execute column index. the order of column shows on the screen.
	 */
	protected int columnIndex;

	/**
	 * identify if current row is end
	 */
	protected boolean isRowEnd = true;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected TableItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#execute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		BirtTimer timer = new BirtTimer( );
		timer.start( );

		super.execute( item, emitter );

		this.emitter = emitter;
		this.tableEmitter = emitter.getTableEmitter( );

		this.table = (TableItemDesign) item;
		logger.log( Level.FINE, "start table item" ); //$NON-NLS-1$
		//execute the on start script
		context.execute( table.getOnStart( ) );
		TableContent tableObj = (TableContent) ContentFactory
				.createTableContent( table, context.getContentObject( ) );
		context.pushContentObject( tableObj );

		tableObj.setCaption( getLocalizedString( table.getCaptionKey( ), table
				.getCaption( ) ) );
		try
		{
			logger.log( Level.FINE, "start get table data" ); //$NON-NLS-1$
			rs = openResultSet( table );
			logger.log( Level.FINE, "end get table data" ); //$NON-NLS-1$
			boolean isRowAvailable = false;
			if ( rs != null )
			{
				isRowAvailable = rs.next( );
			}

			setStyles( tableObj, item );
			setVisibility( item, tableObj );

			String bookmarkStr = evalBookmark( item );
			if ( bookmarkStr != null )
				tableObj.setBookmarkValue( bookmarkStr );

			tableEmitter.start( tableObj );

			accessColumns( );
			//access table header
			accessHeader( );

			//data driving report
			tableEmitter.startBody( );

			if ( isRowAvailable )
			{
				tableInfo = new TABLEINFO( );
				layoutTableEmitter = new LayoutTableEmitter(emitter);
				this.accessGroup( 0 );
			}

			//access table footer
			accessSummary( );
			tableEmitter.endBody( );
			tableEmitter.end( );
			//execute the on finish script
			context.execute( table.getOnFinish( ) );

		}
		catch ( Throwable t )
		{
			logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
			context
					.addException( new EngineException(
							MessageConstants.TABLE_PROCESSING_ERROR,
									( item.getName( ) != null ? item
											.getName( ) : "" ), t ) );//$NON-NLS-1$

		}
		finally
		{
			closeResultSet( rs );
			logger.log( Level.FINE, "end table item" ); //$NON-NLS-1$
			context.popContentObject( );
		}
		timer.stop( );
		timer.logTimeTaken( logger, Level.FINE, context.getTaskIDString( ),
				"Render table" ); // $NON-NLS-1$
	}

	/**
	 * @param tableEmitter
	 *            the table emitter
	 * @param tableItem
	 *            the table report item
	 */
	private void accessColumns( )
	{
		//      access column
		if ( table.getColumnCount( ) > 0 )
		{
			tableEmitter.startColumns( );
			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				ColumnContent colContent = (ColumnContent) ContentFactory
						.createColumnContent( table.getColumn( i ), context
								.getContentObject( ) );
				setStyles( colContent, table.getColumn( i ) );
				tableEmitter.startColumn( colContent );
				tableEmitter.endColumn( );
			}
			tableEmitter.endColumns( );
		}
	}

	/**
	 * get group header
	 * 
	 * @param index
	 *            the group index
	 * @param table
	 *            the table design item
	 * @return a table band corresponding to a group header
	 */
	private TableBandDesign getGroupHeader( int index, TableItemDesign table )
	{
		return table.getGroup( index ).getHeader( );
	}

	/**
	 * get group footer
	 * 
	 * @param index
	 *            the group index
	 * @return the table band
	 */
	private TableBandDesign getGroupFooter( int index, TableItemDesign table )
	{
		return table.getGroup( index ).getFooter( );
	}

	class TABLEINFO
	{

		/**
		 * infomation about rows in group header.
		 */
		ArrayList rows = new ArrayList( );
		/**
		 * first row of group header
		 */
		int[] firstRowOfGroup;
		/**
		 * the outmost group which contains drop cells. -1 means there are no
		 * drop cells in the table.
		 */
		int outmostDropGroup;

		public TABLEINFO( )
		{
			outmostDropGroup = -1;

			//analysis group header
			firstRowOfGroup = new int[table.getGroupCount()];
			for ( int groupId = 0; groupId < table.getGroupCount(); groupId++ )
			{
				TableBandDesign header = table.getGroup( groupId ).getHeader( );
				firstRowOfGroup[groupId] = rows.size();
				if ( header != null )
				{
					for ( int j = 0; j < header.getRowCount( ); j++ )
					{
						ROWINFO row = createRowInfo( header.getRow( j ) );
						rows.add( row );
						if ( row.hasDrop && outmostDropGroup == -1 )
						{
							outmostDropGroup = groupId;
						}
					}
				}
			}
		}

		int getFirstRowOfGroup(int groupId)
		{
			return firstRowOfGroup[groupId];
		}
		/**
		 * should we start layout in group header group index. layout should be
		 * started while handle the header of outmost drop group.
		 * 
		 * @return true: start the layout, false needn't
		 */
		boolean shouldStartLayout( int groupIndex )
		{
			if (outmostDropGroup != -1)
			{
				return outmostDropGroup == groupIndex;
			}
			return false;
		}
		
		boolean hasDropCells()
		{
			return outmostDropGroup != -1;
		}

		/**
		 * should we stop the layout in group footer. layout should stop after
		 * we handle the footer of outmost drop group.
		 * 
		 * @param groupIndex
		 * @return
		 */
		boolean shouldStopLayout( int groupIndex )
		{
			if (outmostDropGroup != -1)
			{
				return outmostDropGroup == groupIndex;
			}
			return false;
		}

		boolean hasStartLayout( int groupIndex )
		{
			return groupIndex >= outmostDropGroup;
		}

		boolean shouldEndRow(int rowId)
		{
			if (rowIndex < rows.size())
			{
				return ((ROWINFO)rows.get(rowId)).endTag;
			}
			return true;
		}
		
		public int getCellDrop( int rowId, int colId )
		{
			if ( outmostDropGroup != -1 )
			{
				if ( rowId < rows.size( ) )
				{
					return ( (ROWINFO) rows.get( rowId ) ).cellDrops[colId];
				}
			}
			return DROP_NONE;
		}

		class ROWINFO
		{

			boolean endTag;
			boolean hasDrop;
			int[] cellDrops;
		}

		/**
		 * get show status and drop status of each row
		 * 
		 * @param table
		 *            the table item
		 */

		ROWINFO createRowInfo( RowDesign row )
		{
			ROWINFO rowInfo = new ROWINFO( );
			rowInfo.cellDrops = new int[row.getCellCount( )];
			rowInfo.hasDrop = false;
			rowInfo.endTag = false;
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				int drop = getDrop( row.getCell( i ).getDrop( ) );
				if ( drop != DROP_NONE )
				{
					rowInfo.hasDrop = true;
				}
				else
				{
					rowInfo.endTag = true;
				}
				rowInfo.cellDrops[i] = drop;
			}
			return rowInfo;
		}

	}

	/*
	 * groupIndex is the execute group rowIndex is the execute row columnIndex
	 * is the executed column
	 * 
	 * outputRowEnd[] is should we terminate the ROW_END for this row
	 * 
	 * dropCells[rowCount][columCount] the drop id of each cells
	 *  
	 */

	final static int DROP_DETAIL = 1;
	final static int DROP_ALL = 2;
	final static int DROP_NONE = 0;

	int getDrop( String drop )
	{
		if ( "all".equals( drop ) )
		{
			return DROP_ALL;
		}
		if ( "detail".equals( drop ) )
		{
			return DROP_DETAIL;
		}
		return DROP_NONE;
	}

	/**
	 * if the this header band has drop cell
	 * 
	 * @param header
	 *            the hander band
	 * @return the boolean value
	 */
	private boolean hasDropCell( TableBandDesign header )
	{
		for ( int i = 0; i < header.getRowCount( ); i++ )
		{
			RowDesign row = header.getRow( i );
			for ( int j = 0; j < row.getCellCount( ); j++ )
			{
				CellDesign cell = row.getCell( j );
				if ( cell != null )
				{
					if ( cell.getDrop( ) != null )
					{
						if ( cell.getDrop( ).equalsIgnoreCase( "all" ) //$NON-NLS-1$
								|| cell.getDrop( ).equalsIgnoreCase( "detail" ) ) //$NON-NLS-1$
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * access table band without drop
	 * 
	 * @param band
	 * @param tableEmitter
	 *            the table emitter
	 */
	private void accessBand( TableBandDesign band )
	{
		assert band != null;
		for ( int i = 0; i < band.getRowCount( ); i++ )
		{
			accessRow(band.getRow(i), isRowEnd, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		super.reset( );
		table = null;
		tableInfo = null;
		isRowEnd = true;
	}

	/**
	 * @param row
	 *            the row content object
	 */
	private void setBookmarkValue( RowDesign design, RowContent row )
	{
		// bookmark
		Expression bookmark = design.getBookmark( );
		if ( bookmark != null )
		{
			Object obj = context.evaluate( bookmark );
			if ( obj != null )
			{
				row.setBookmarkValue( obj.toString( ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#getGroupCount(org.eclipse.birt.report.engine.ir.ReportItemDesign)
	 */
	protected int getGroupCount( ReportItemDesign item )
	{
		if ( item != null )
		{
			return ( (TableItemDesign) item ).getGroupCount( );
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessHeader()
	 */
	protected void accessHeader( )
	{
		TableBandDesign tHeader = table.getHeader( );
		if ( tHeader != null && tHeader.getRowCount( ) > 0 )
		{
			tableEmitter.startHeader( );
			accessBand( tHeader );
			tableEmitter.endHeader( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessFooter()
	 */
	protected void accessFooter( )
	{
		return;
	}

	protected void accessSummary( )
	{
		TableBandDesign tFooter = table.getFooter( );
		if (tFooter != null && tFooter.getRowCount() > 0)
		{
			//tableEmitter( ).startFooter( );
			accessBand( tFooter );
			//tableEmitter( ).endFooter( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessDetailOneTime()
	 */
	protected void accessDetailOnce( )
	{
		
		TableBandDesign band = table.getDetail( );
		if ( band != null && band.getRowCount() > 0)
		{
			accessBand(band);
		}
	}

	/**
	 * output the row. row is output as: row start, cess, row end.
	 * 
	 * @param row
	 * @param open
	 *            should the row start be output
	 * @param close
	 *            should the row end be output
	 */
	protected void accessRow( RowDesign row, boolean open, boolean close )
	{
		if ( open )
		{
			RowContent rowContent = (RowContent) ContentFactory
					.createRowContent( row, context.getContentObject( ) );
			context.pushContentObject( rowContent );
			setVisibility( row, rowContent );
			setBookmarkValue( row, rowContent );
			setStyles( rowContent, row );
			tableEmitter.startRow( rowContent );
			isRowEnd = false;
		}
		for ( int j = 0; j < row.getCellCount( ); j++ )
		{
			CellDesign cell = row.getCell( j );
			if ( cell != null )
			{
				columnIndex = j;
				CellContent cellContent = (CellContent) ContentFactory
						.createCellContent( cell, context.getContentObject( ) );
				context.pushContentObject( cellContent );
				setStyles( cellContent, cell );
				tableEmitter.startCell( cellContent );
				for ( int m = 0; m < cell.getContentCount( ); m++ )
				{
					ReportItemDesign item = cell.getContent( m );
					if ( item != null )
					{
						item.accept( this.visitor );
					}
				}
				tableEmitter.endCell( );
				context.popContentObject( );
			}
		}
		if ( close )
		{
			tableEmitter.endRow( );
			context.popContentObject( );
			isRowEnd = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupHeader(int)
	 */
	protected void accessGroupHeader( int index )
	{
		this.rowIndex = tableInfo.getFirstRowOfGroup(index);
		this.groupIndex = index;
		
		TableBandDesign band = getGroupHeader( index, table );
		if ( band != null && band.getRowCount() > 0)
		{
			if ( tableInfo.shouldStartLayout( groupIndex ) )
			{
				tableEmitter = this.layoutTableEmitter;
			}
	
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				accessRow( band.getRow( i ), true, tableInfo.shouldEndRow(rowIndex) );
				this.rowIndex++;
			}
		}
	}

	int getDropId( int groupIndex, int dropType )
	{
		return -( 10 + groupIndex * 2 + dropType );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupFooter(int)
	 */
	protected void accessGroupFooter( int index )
	{
		if ( tableEmitter == layoutTableEmitter )
		{
			layoutTableEmitter
					.resolveDropCells( getDropId( index, DROP_DETAIL ) );
		}
		
		TableBandDesign band = getGroupFooter( index, table );
		if (band != null && band.getRowCount() > 0)
		{
			accessBand( band );
		}

		if ( tableEmitter == layoutTableEmitter )
		{
			layoutTableEmitter.resolveDropCells( getDropId( index, DROP_ALL ) );
			if ( tableInfo.shouldStopLayout( index ) )
			{
				layoutTableEmitter.flush( );
				tableEmitter = emitter.getTableEmitter( );
			}
		}

	}

	/*
	 * Link the last row of group header with the first row of details Only if
	 * the cells in the last row of group header are all drop cells. Drop area
	 * is terminate only if there is an none empty cell exists in the drop area.
	 */

	/**
	 * the outest group which contains drop cells.
	 */
	int outestDropGroup;

	class LayoutTableEmitter implements ITableEmitter
	{

		IReportEmitter emitter;
		Table layout;
		CellContent cell;
		BufferedReportEmitter buffer;

		LayoutTableEmitter( IReportEmitter emitter )
		{
			this.emitter = emitter;
			layout = new Table( );
		}

		public void start( IReportItemContent content )
		{
		}

		public void end( )
		{
		}

		public void startColumns( )
		{
		}

		public void endColumns( )
		{
		}

		public void startColumn( IColumnContent column )
		{
		}

		public void endColumn( )
		{
		}

		public void startHeader( )
		{
		};

		public void endHeader( )
		{
		}

		public void startFooter( )
		{
		}

		public void endFooter( )
		{
		}

		public void startBody( )
		{
		}

		public void endBody( )
		{
		}

		public void startRow( IRowContent row )
		{
			layout.createRow( row );
		}

		public void endRow( )
		{
		}

		public void startCell( ICellContent cell )
		{
			this.cell = (CellContent) cell;
			buffer = new BufferedReportEmitter( emitter );
			visitor.pushEmitter( buffer );
		}

		public void endCell( )
		{
			visitor.popEmitter( );
			int colId = cell.getColumn( ) - 1;
			int colSpan = cell.getRowSpan( );
			int rowSpan = 1;
			//the current executed cell is rowIndex, columnIndex
			//get the span value of that cell.
			int dropType = tableInfo.getCellDrop( rowIndex, columnIndex );

			if ( dropType != DROP_NONE )
			{
				rowSpan = getDropId( groupIndex, dropType );
			}
			else
			{
				rowSpan = cell.getRowSpan( );
			}
			layout.createCell( colId, rowSpan, colSpan, new TableCellContent(
					cell, buffer ) );
		}

		public void resolveDropCells( int dropId )
		{
			layout.resolveDropCells( dropId );
		}

		public void flush( )
		{
			int rowCount = layout.getRowCount( );
			int colCount = layout.getColCount( );
			ITableEmitter tableEmitter = emitter.getTableEmitter( );
			for ( int i = 0; i < rowCount; i++ )
			{
				Row row = layout.getRow( i );
				tableEmitter.startRow( (IRowContent) row.getContent( ) );
				for ( int j = 0; j < colCount; j++ )
				{
					Cell cell = row.getCell( j );
					if ( cell.getStatus( ) == Cell.CELL_USED )
					{
						TableCellContent content = (TableCellContent) cell
								.getContent( );
						content.cell.setColumn(cell.getColId() + 1);
						content.cell.setRowSpan( cell.getRowSpan( ) );
						content.cell.setColSpan( cell.getColSpan( ) );

						tableEmitter.startCell( content.cell );
						if ( content.buffer != null )
						{
							content.buffer.endReport( );
						}
						tableEmitter.endCell( );
					}
				}
				tableEmitter.endRow( );
			}
			layout.reset();
		}

		class TableCellContent implements IContent
		{

			CellContent cell;
			BufferedReportEmitter buffer;

			TableCellContent( CellContent cell, BufferedReportEmitter buffer )
			{
				this.cell = cell;
				this.buffer = buffer;

			}

			public boolean isEmpty( )
			{
				return buffer == null || buffer.isEmpty();
			}
		}
	}
}