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

import org.eclipse.birt.report.engine.content.CellContent;
import org.eclipse.birt.report.engine.content.ColumnContent;
import org.eclipse.birt.report.engine.content.RowContent;
import org.eclipse.birt.report.engine.content.TableContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.emitter.buffer.BufferedReportEmitter;
import org.eclipse.birt.report.engine.executor.buffermgr.DropBufferManager;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * Defines execution logic for a List report item. <p>
 * Currently table header and footer do not support data items
 * 
 * @version $Revision: #6 $ $Date: 2005/02/04 $
 */
public class TableItemExecutor extends ListingElementExecutor
{

	/**
	 * the table design
	 */
	protected TableItemDesign table;
	/**
	 * current report emitter;
	 */
	protected IReportEmitter emitter;

	/**
	 * the drop buffer manager
	 */
	protected DropBufferManager bufferManager;

	/**
	 * the buffer status of this excutor
	 */
	protected boolean bufferStatus = false;

	/**
	 * the current of row ID in content IR
	 */
	protected int rowID = 0;

	/**
	 * the facticity of each row in design IR
	 */
	protected boolean[][] show;

	/**
	 * the drop property of each band
	 */
	protected boolean[] drop;

	/**
	 * identify if current row is end
	 */
	protected boolean isRowEnd = true;

	/**
	 * @param context execution context
	 * @param visitor visitor object for driving the execution 
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
		super.execute( item, emitter );
		ITableEmitter tableEmitter = emitter.getTableEmitter( );
		if ( tableEmitter == null )
		{
			return;
		}

		this.emitter = emitter;

		table = (TableItemDesign) item;
		if ( logger.isInfoEnabled( ) )
		{
			logger.info( "start table item" );
		}
		//execute the on start script
		context.execute( table.getOnStart( ) );
		TableContent tableObj = new TableContent( table );
		tableObj.setCaption( getLocalizedString( table.getCaptionKey( ), table
				.getCaption( ) ) );
		setStyles( tableObj, item );
		setVisibility( item, tableObj );
		
		String bookmarkStr = evalBookmark( item );
		if (bookmarkStr != null)
			tableObj.setBookmarkValue(bookmarkStr);
		
		rs = openResultSet( table );
		boolean isRowAvailable = false;
		if ( rs != null )
		{
			isRowAvailable = rs.next( );
		}

		tableEmitter.start( tableObj );

		accessColumns( tableEmitter, table );
		//access table header
		accessHeader( );

		//data driving report
		tableEmitter.startBody( );
		setUpDropProperties( table );
		if ( isRowAvailable )
		{
			context.execute( table.getOnRow( ) );
			this.accessGroup( 0 );
		}

		//access table footer
		accessSummary( );
		tableEmitter.endBody( );
		tableEmitter.end( );

		closeResultSet( rs );

		if ( logger.isInfoEnabled( ) )
		{
			logger.info( "end table item" );
		}
		//execute the on finish script
		context.execute( table.getOnFinish( ) );
	}

	/**
	 * @param tableEmitter the table emitter
	 * @param tableItem the table report item
	 */
	private void accessColumns( ITableEmitter tableEmitter,
			TableItemDesign tableItem )
	{
		//      access column
		if ( tableItem.getColumnCount( ) > 0 )
		{
			tableEmitter.startColumns( );
			for ( int i = 0; i < tableItem.getColumnCount( ); i++ )
			{
				ColumnContent colContent = new ColumnContent( tableItem
						.getColumn( i ) );
				setStyles( colContent, tableItem.getColumn( i ) );
				tableEmitter.startColumn( colContent );
				tableEmitter.endColumn( );
			}
			tableEmitter.endColumns( );
		}
	}

	/**
	 * get group header
	 * 
	 * @param index the group index
	 * @param table the table design item
	 * @return a table band corresponding to a group header
	 */
	private TableBandDesign getGroupHeader( int index, TableItemDesign table )
	{
		return ( (TableGroupDesign) ( table.getGroup( index ) ) ).getHeader( );
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
		return ( (TableGroupDesign) ( table.getGroup( index ) ) ).getFooter( );
	}

	/**
	 * get show status and drop status of each row
	 * 
	 * @param table the table item
	 */
	private void setUpDropProperties( TableItemDesign table )
	{
		int grpCount = table.getGroupCount( );
		show = new boolean[grpCount][];
		drop = new boolean[grpCount];
		for ( int i = 0; i < grpCount; i++ )
		{
			TableBandDesign header = getGroupHeader( i, table );
			show[i] = setUpRowDropProperties( header );
			drop[i] = hasDropCell( header );
		}
	}

	/**
	 * get show status of row
	 * 
	 * @param band the table band           
	 * @return the boolean array
	 */
	private boolean[] setUpRowDropProperties( TableBandDesign band )
	{
		if ( band != null )
		{
			boolean[] rowShow = new boolean[band.getRowCount( )];
			for ( int j = 0; j < band.getRowCount( ); j++ )
			{
				RowDesign row = band.getRow( j );
				rowShow[j] = false;
				for ( int k = 0; k < row.getCellCount( ); k++ )
				{
					CellDesign cell = row.getCell( k );
					if ( cell != null )
					{
						if ( cell.getDrop( ) == null )
						{
							rowShow[j] = true;
						}
						else if ( cell.getDrop( ).equalsIgnoreCase( "none" ) )
						{
							rowShow[j] = true;
						}
					}
				}
			}
			return rowShow;
		}
		return null;
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
						if ( cell.getDrop( ).equalsIgnoreCase( "all" )
								|| cell.getDrop( ).equalsIgnoreCase( "detail" ) )
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
	 * @param tableEmitter the table emitter
	 */
	private void accessNoDropBand( TableBandDesign band,
			ITableEmitter tableEmitter )
	{
		if ( ( band == null ) || ( band.getRowCount( ) == 0 ) )
		{
			return;
		}
		for ( int i = 0; i < band.getRowCount( ); i++ )
		{

			RowDesign row = band.getRow( i );
			//			if ( !isRowVisible( row ) )
			//			{
			//				break;
			//			}
			RowContent rowContent = new RowContent( row );
			setVisibility( row, rowContent );
			setBookmarkValue( rowContent );
			setStyles( rowContent, row );
			tableEmitter.startRow( rowContent );

			for ( int j = 0; j < row.getCellCount( ); j++ )
			{
				CellDesign cell = row.getCell( j );
				if ( cell != null )
				{
					CellContent cellContent = new CellContent( cell );
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
				}
			}
			tableEmitter.endRow( );
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
		bufferManager = null;
		bufferStatus = false;
		rowID = 0;
		show = null;
		drop = null;
		isRowEnd = true;
	}

	/**
	 * @param row the row content object
	 */
	private void setBookmarkValue( RowContent row )
	{
		// bookmark
		Expression bookmark = row.getBookmark( );
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
			emitter.getTableEmitter( ).startHeader( );
			accessNoDropBand( tHeader, emitter.getTableEmitter( ) );
			emitter.getTableEmitter( ).endHeader( );
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
		//emitter.getTableEmitter( ).startFooter( );
		accessNoDropBand( tFooter, emitter.getTableEmitter( ) );
		//emitter.getTableEmitter( ).endFooter( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessDetailOneTime()
	 */
	protected void accessDetailOnce( )
	{
		TableBandDesign band = table.getDetail( );
		if ( band == null )
		{
			return;
		}
		context.execute( table.getOnRow( ) );
		for ( int i = 0; i < band.getRowCount( ); i++ )
		{

			RowDesign row = band.getRow( i );
			//			if ( !isRowVisible( row ) )
			//			{
			//				break;
			//			}
			if ( isRowEnd )
			{
				RowContent rowContent = new RowContent( row );
				setVisibility( row, rowContent );
				setBookmarkValue( rowContent );
				setStyles( rowContent, row );
				emitter.getTableEmitter( ).startRow( rowContent );
				isRowEnd = false;
			}
			for ( int j = 0; j < row.getCellCount( ); j++ )
			{
				CellDesign cell = row.getCell( j );
				if ( cell != null )
				{
					CellContent cellContent = new CellContent( cell );
					setStyles( cellContent, cell );
					emitter.getTableEmitter( ).startCell( cellContent );
					for ( int m = 0; m < cell.getContentCount( ); m++ )
					{
						ReportItemDesign item = cell.getContent( m );
						if ( item != null )
						{
							/* int level = context.getCurrentGroupLevel(); */
							item.accept( this.visitor );
							/* context.setCurrentGroupLevel(level); */
						}
					}
					emitter.getTableEmitter( ).endCell( );
				}
			}
			emitter.getTableEmitter( ).endRow( );
			isRowEnd = true;
			rowID++;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupHeader(int)
	 */
	protected void accessGroupHeader( int index )
	{
		TableBandDesign band = getGroupHeader( index, table );
		if ( band == null )
		{
			return;
		}
		context.execute( table.getOnRow( ) );
		//if this band has drop cells
		if ( drop[index] && ( !bufferStatus ) )
		{
			//start buffering for this executor
			bufferManager = new DropBufferManager( );
			bufferStatus = true;
			emitter = new BufferedReportEmitter( emitter );
			emitter.initialize(null);
			visitor.pushEmitter( emitter );
			emitter.startReport( context.getReport( ) );
		}
		for ( int i = 0; i < band.getRowCount( ); i++ )
		{
			RowDesign row = band.getRow( i );
			//			if ( !isRowVisible( row ) )
			//			{
			//				break;
			//			}
			RowContent newRow = new RowContent( row );

			if ( isRowEnd )
			{
				setVisibility( row, newRow );
				setBookmarkValue( newRow );
				setStyles( newRow, row );
				emitter.getTableEmitter( ).startRow( newRow );
				isRowEnd = false;
			}
			for ( int j = 0; j < row.getCellCount( ); j++ )
			{
				CellDesign cell = row.getCell( j );
				if ( cell != null )
				{
					CellContent cellContent = new CellContent( cell );
					setStyles( cellContent, cell );
					if ( cell.getDrop( ) != null )
					{
						if ( !cell.getDrop( ).equalsIgnoreCase( "none" ) )
						{
							bufferManager.addDropInfo( cellContent, rowID, cell
									.getDrop( ), index );
						}
					}
					emitter.getTableEmitter( ).startCell( cellContent );

					for ( int m = 0; m < cell.getContentCount( ); m++ )
					{
						ReportItemDesign item = cell.getContent( m );
						if ( item != null )
						{
							item.accept( this.visitor );
						}
					}

					emitter.getTableEmitter( ).endCell( );
				}
			}
			if ( show[index][i] )
			{
				emitter.getTableEmitter( ).endRow( );
				isRowEnd = true;
				rowID++;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupFooter(int)
	 */
	protected void accessGroupFooter( int index )
	{
		TableBandDesign band = getGroupFooter( index, table );

		if ( bufferStatus )
		{
			bufferStatus = !( bufferManager.detailEnd( index, rowID ) );

			if ( !bufferStatus )
			{
				emitter.endReport( );
				emitter = visitor.popEmitter( );

			}
		}
		accessNoDropBand( band, emitter.getTableEmitter( ) );

		if ( bufferStatus )
		{
			bufferStatus = !( bufferManager.footerEnd( index, rowID ) );
			if ( !bufferStatus )
			{
				emitter.endReport( );
				emitter = visitor.popEmitter( );

			}
		}

	}
}