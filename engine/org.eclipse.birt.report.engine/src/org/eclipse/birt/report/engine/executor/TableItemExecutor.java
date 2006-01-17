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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.BirtTimer;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.emitter.BufferedReportEmitter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.Table;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.RowData;
import org.eclipse.birt.report.engine.script.internal.RowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;

/**
 * Defines execution logic for a List report item.
 * <p>
 * Currently table header and footer do not support data items
 * 
 * <p>
 * if the table contains any drop cells, we need buffer the cell contents unitl
 * we resolved all the drop cells. we resovles the drop cells at the end of each
 * group as the drop cells can only start from the group header and terminate in
 * the group footer.
 * 
 * @version $Revision: 1.36 $ $Date: 2005/12/12 19:48:47 $
 */
public class TableItemExecutor extends ListingElementExecutor
{

	protected static Logger logger = Logger.getLogger( TableItemExecutor.class
			.getName( ) );

	private int groupIndex;

	private TableLayoutEmitter layoutEmitter;

	private int rowId = 0;

	/**
	 * a structure contains the table group/row/drop information
	 */
	protected TABLEINFO tableInfo;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected TableItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#execute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		BirtTimer timer = new BirtTimer( );
		timer.start( );

		TableItemDesign tableDesign = ( TableItemDesign ) item;

		tableInfo = new TABLEINFO( tableDesign );

		logger.log( Level.FINEST, "start table item" ); //$NON-NLS-1$

		ITableContent tableContent = report.createTableContent( );
		assert ( tableContent instanceof TableContent );
		( ( TableContent ) tableContent ).setHeaderRepeat( tableDesign
				.getRepeatHeader( ) );
		IContent parent = context.getContent( );
		context.pushContent( tableContent );

		logger.log( Level.FINEST, "start get table data" ); //$NON-NLS-1$
		openResultSet( item );
		logger.log( Level.FINEST, "end get table data" ); //$NON-NLS-1$

		initializeContent( parent, item, tableContent );
		processStyle( item, tableContent );
		processVisibility( item, tableContent );
		processBookmark( item, tableContent );
		processAction( item, tableContent );

		for ( int i = 0; i < tableDesign.getColumnCount( ); i++ )
		{
			ColumnDesign columnDesign = tableDesign.getColumn( i );
			Column column = new Column( );
			column.setStyleClass( columnDesign.getStyleName( ) );
			column.setWidth( columnDesign.getWidth( ) );
			tableContent.addColumn( column );
		}
		if ( context.isInFactory( ) )
		{
			TableScriptExecutor.handleOnCreate( ( TableContent ) tableContent,
					context );
		}

		startTOCEntry( tableContent );
		if ( emitter != null )
		{
			emitter.startTable( tableContent );
		}

		accessQuery( tableDesign, emitter );

		if ( emitter != null )
		{
			emitter.endTable( tableContent );
		}

		finishTOCEntry( );
		context.popContent( );
		closeResultSet( );

		logger.log( Level.FINEST, "end table item" ); //$NON-NLS-1$

		timer.stop( );
		timer.logTimeTaken( logger, Level.FINEST, context.getTaskIDString( ),
				"Render table" ); // $NON-NLS-1$
	}

	/**
	 * structure used to cache the information of a table.
	 * 
	 * @version $Revision: 1.36 $ $Date: 2005/12/12 19:48:47 $
	 */
	private static class TABLEINFO
	{

		/**
		 * the table infomation
		 */
		private TableItemDesign table;

		/**
		 * does the group contains drop cells
		 */
		private boolean[] dropCellsInGroup;

		/**
		 * total row count in the table
		 */
		private int rowCount;

		TABLEINFO( TableItemDesign table )
		{
			this.table = table;
			initRowCount( );
			initDropCellsInGroup( );
		}

		/**
		 * total rows in the table design. The number is a total of all rows in
		 * table header, table footer, group header, group footer and details.
		 * 
		 * @return number of rows.
		 */
		private void initRowCount( )
		{
			for ( int groupId = 0; groupId < table.getGroupCount( ); groupId++ )
			{
				TableGroupDesign group = table.getGroup( groupId );
				TableBandDesign header = group.getHeader( );
				if ( header != null )
				{
					rowCount += header.getRowCount( );
				}
				TableBandDesign footer = group.getHeader( );
				if ( footer != null )
				{
					rowCount += footer.getRowCount( );
				}
			}

			TableBandDesign detail = table.getDetail( );
			if ( detail != null )
			{
				rowCount += detail.getRowCount( );
			}
		}

		/**
		 * get the total rows in the table.
		 * 
		 * @return row count.
		 */
		public int getRowCount( )
		{
			return rowCount;
		}

		/**
		 * test if the group (index) has any drop cells.
		 * 
		 * @param groupIndex
		 *            group index
		 * @return true: the group has drop cells, false: no.
		 */
		public boolean hasDropCells( int groupIndex )
		{
			return dropCellsInGroup[groupIndex];
		}

		/**
		 * scan the table design to set if the group has drop cells.
		 */
		private void initDropCellsInGroup( )
		{
			dropCellsInGroup = new boolean[table.getGroupCount( )];
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				TableGroupDesign group = table.getGroup( i );
				if ( group != null && hasDropCellsInGroup( group ) )
				{
					dropCellsInGroup[i] = true;
				} else
				{
					dropCellsInGroup[i] = false;
				}
			}
		}

		/**
		 * scan the row to see if the row has drop cells.
		 * 
		 * @param row
		 *            row design to be tested.
		 * @return true: the row has drop cells. false: no drop cells.
		 */
		private boolean hasDropCellsInRow( RowDesign row )
		{
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				CellDesign cell = row.getCell( i );
				String drop = cell.getDrop( );
				if ( drop != null && !"none".equals( drop ) )
				{
					return true;
				}
			}

			return false;
		}

		/**
		 * scan the group header to see if the group has any drop cells.
		 * 
		 * @param group
		 *            group to be scaned.
		 * @return true, the group has drop cells, false, no.
		 */
		private boolean hasDropCellsInGroup( TableGroupDesign group )
		{
			TableBandDesign header = group.getHeader( );
			if ( header != null )
			{
				for ( int i = 0; i < header.getRowCount( ); i++ )
				{
					RowDesign row = header.getRow( i );
					if ( row != null && hasDropCellsInRow( row ) )
					{
						return true;
					}
					return false;
				}
			}
			return false;
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
		tableInfo = null;
		rowId = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessHeader()
	 */
	protected void accessHeader( ListingDesign list, IContentEmitter emitter,
			IResultIterator rsIterator )
	{
		ITableContent tableContent = ( ITableContent ) context.getContent( );

		// start table header
		TableBandDesign bandDesign = ( ( TableItemDesign ) list ).getHeader( );
		if ( bandDesign != null )
		{
			ITableBandContent header = report.createTableHeader( );
			context.pushContent( header );
			initializeContent( tableContent, bandDesign, header );
			if ( emitter != null )
			{
				emitter.startTableHeader( header );
			}
			accessTableBand( bandDesign, emitter, rsIterator );
			if ( emitter != null )
			{
				emitter.endTableHeader( header );
			}
			context.popContent( );
		}

		// start table body
		ITableBandContent body = report.createTableBody( );
		initializeContent( tableContent, null, body );

		context.pushContent( body );
		if ( emitter != null )
		{
			emitter.startTableBody( body );
		}
	}

	protected void accessFooter( ListingDesign list, IContentEmitter emitter,
			IResultIterator rsIterator )
	{
		if ( layoutEmitter != null )
		{
			layoutEmitter.flush( );
			outputEmitter = layoutEmitter.emitter;
			emitter = outputEmitter;
			layoutEmitter = null;
		}

		// end table body
		ITableBandContent body = ( ITableBandContent ) context.getContent( );
		if ( emitter != null )
		{
			emitter.endTableBody( body );
		}
		context.popContent( );

		// start table footer
		TableBandDesign bandDesign = ( ( TableItemDesign ) list ).getFooter( );
		if ( bandDesign != null )
		{
			ITableBandContent footer = report.createTableFooter( );
			IContent parent = context.getContent( );
			context.pushContent( footer );
			initializeContent( parent, bandDesign, footer );
			if ( emitter != null )
			{
				emitter.startTableFooter( footer );
			}
			accessTableBand( bandDesign, emitter, rsIterator );
			if ( emitter != null )
			{
				emitter.endTableFooter( footer );
			}
			context.popContent( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	protected void accessDetail( ListingDesign list, IContentEmitter emitter,
			IResultIterator rsIterator )
	{
		accessTableBand( ( ( TableItemDesign ) list ).getDetail( ), emitter,
				rsIterator );
	}

	protected void accessGroupHeader( ListingDesign list, int index,
			IContentEmitter emitter )
	{
		groupIndex = index;
		TableGroupDesign group = ( ( TableItemDesign ) list ).getGroup( index );

		if ( group != null )
		{
			TableBandDesign band = group.getHeader( );
			if ( layoutEmitter == null && tableInfo.hasDropCells( index ) )
			{
				layoutEmitter = new TableLayoutEmitter(
						( TableItemDesign ) list, emitter );
				outputEmitter = layoutEmitter;
			}
			accessTableBand( band, outputEmitter, null );
		}
	}

	protected void accessGroupFooter( ListingDesign list, int index,
			IContentEmitter emitter )
	{
		// all cells with drop detail can be resolved.
		if ( layoutEmitter != null )
		{
			assert layoutEmitter == emitter;
			layoutEmitter.resolveCellsOfDropDetail( index );
		}
		TableGroupDesign group = ( ( TableItemDesign ) list ).getGroup( index );
		if ( group != null )
		{
			accessTableBand( group.getFooter( ), emitter, null );
		}
		// all cells with drop all can be resolved.
		if ( layoutEmitter != null )
		{
			assert layoutEmitter == emitter;
			layoutEmitter.resolveCellsOfDropAll( index );
		}
	}

	protected void accessTableBand( TableBandDesign band,
			IContentEmitter emitter, IResultIterator rsIterator )
	{
		if ( band != null )
		{
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				accessRow( band.getRow( i ), emitter, rsIterator );
			}
		}
	}

	/**
	 * not all row design create corrsponds row content. To some row designs,
	 * they share one row content, each design creates only cells of that row.
	 * If the row start is true, the next row design should call startRow().
	 */
	transient private boolean rowClosed = true;

	/**
	 * output the row. row is output as: row start, cells*, row end.
	 * 
	 * @param rowIndex
	 *            the index in the rows
	 * @param row
	 *            row design
	 * @param emitter
	 *            output emitter
	 */
	protected void accessRow( RowDesign row, IContentEmitter emitter,
			IResultIterator rsIterator )
	{
		IRowData rowData = null;
		if ( context.isInFactory( ) )
		{
			rowData = new RowData( rsIterator, getValueExpressions( row ) );
		}

		if ( rowClosed )
		{
			IRowContent rowContent = report.createRowContent( );
			rowContent.setRowID( rowId++ );
			assert ( rowContent instanceof RowContent );
			IContent parent = context.getContent( );
			context.pushContent( rowContent );
			initializeContent( parent, row, rowContent );

			processStyle( row, rowContent );
			processVisibility( row, rowContent );
			processBookmark( row, rowContent );
			processAction( row, rowContent );

			if ( needPageBreak )
			{
				rowContent.getStyle( ).setProperty(
						IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE );
				needPageBreak = false;
			}

			if ( context.isInFactory( ) )
			{
				RowScriptExecutor.handleOnCreate( ( RowContent ) rowContent,
						rowData, context );
			}

			startTOCEntry( rowContent );
			if ( emitter != null )
			{
				emitter.startRow( rowContent );
			}
			rowClosed = false;
		}

		for ( int j = 0; j < row.getCellCount( ); j++ )
		{
			CellDesign cell = row.getCell( j );
			if ( cell != null )
			{
				ICellContent cellContent = report.createCellContent( );
				assert ( cellContent instanceof CellContent );
				IContent parent = context.getContent( );
				context.pushContent( cellContent );

				initializeContent( parent, cell, cellContent );
				cellContent.setColumn( cell.getColumn( ) );
				cellContent.setColSpan( cell.getColSpan( ) );
				cellContent.setRowSpan( cell.getRowSpan( ) );

				processStyle( cell, cellContent );
				processVisibility( cell, cellContent );
				processBookmark( cell, cellContent );
				processAction( cell, cellContent );

				if ( context.isInFactory( ) )
				{
					CellScriptExecutor.handleOnCreate(
							( CellContent ) cellContent, rowData, context,
							false );
				}

				startTOCEntry( cellContent );
				if ( emitter != null )
				{
					emitter.startCell( cellContent );
				}
				for ( int m = 0; m < cell.getContentCount( ); m++ )
				{
					ReportItemDesign item = cell.getContent( m );
					if ( item != null )
					{
						if ( layoutEmitter != null )
						{
							item.accept( this.visitor, layoutEmitter
									.getCellEmitter( ) );
						} else
						{
							item.accept( this.visitor, emitter );
						}
					}
				}
				if ( emitter != null )
				{
					emitter.endCell( cellContent );
				}
				finishTOCEntry( );
				context.popContent( );
			}
		}

		boolean closeRow = true;
		if ( closeRow )
		{
			emitter.endRow( ( IRowContent ) context.getContent( ) );
			finishTOCEntry( );
			context.popContent( );
			rowClosed = true;
		}
	}

	static List getValueExpressions( RowDesign row )
	{
		List ret = new ArrayList( );
		for ( int j = 0; j < row.getCellCount( ); j++ )
		{
			CellDesign cell = row.getCell( j );
			if ( cell != null )
			{
				for ( int m = 0; m < cell.getContentCount( ); m++ )
				{
					ReportItemDesign item = cell.getContent( m );
					if ( item instanceof DataItemDesign )
					{
						Expression valueExpr = ( ( DataItemDesign ) item )
								.getValue( );
						ret.add( valueExpr );
					} else if ( item instanceof MultiLineItemDesign )
					{
						Expression valueExpr = ( ( MultiLineItemDesign ) item )
								.getContent( );
						ret.add( valueExpr );
					}
				}
			}
		}
		return ret;
	}

	/**
	 * the outest group which contains drop cells.
	 */
	int outestDropGroup;

	private class TableLayoutEmitter extends ContentEmitterAdapter
	{

		private Table layout;

		private IContentEmitter emitter;

		private BufferedReportEmitter cellEmitter;

		TableLayoutEmitter( TableItemDesign design, IContentEmitter emitter )
		{
			layout = new Table( 0, design.getColumnCount( ) );
			this.emitter = emitter;
		}

		public void startRow( IRowContent row )
		{
			layout.createRow( row );
		}

		public void endRow( IRowContent row )
		{
			// if (!layout.hasDropCell())
			// {
			// flush();
			// }
		}

		public void startCell( ICellContent cell )
		{
			cellEmitter = new BufferedReportEmitter( emitter );
			int colId = cell.getColumn( );
			int colSpan = cell.getColSpan( );
			int rowSpan = cell.getRowSpan( );

			// the current executed cell is rowIndex, columnIndex
			// get the span value of that cell.
			CellDesign cellDesign = ( CellDesign ) cell.getGenerateBy( );
			String dropType = cellDesign.getDrop( );
			if ( dropType != null && !"none".equals( dropType ) )
			{
				rowSpan = createDropID( groupIndex, dropType );
			}
			layout.createCell( colId, rowSpan, colSpan, new CellContent( cell,
					cellEmitter ) );
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

		public void endCell( ICellContent cell )
		{
		}

		public void resolveCellsOfDropDetail( int groupIndex )
		{
			layout.resolveDropCells( createDropID( groupIndex, "detail" ) );
		}

		public void resolveCellsOfDropAll( int groupIndex )
		{
			layout.resolveDropCells( createDropID( groupIndex, "all" ) );
		}

		public IContentEmitter getCellEmitter( )
		{
			assert cellEmitter != null;
			return cellEmitter;
		}

		public void flush( )
		{
			int rowCount = layout.getRowCount( );
			int colCount = layout.getColCount( );
			for ( int i = 0; i < rowCount; i++ )
			{
				Row row = layout.getRow( i );
				IRowContent rowContent = ( IRowContent ) row.getContent( );
				emitter.startRow( rowContent );
				for ( int j = 0; j < colCount; j++ )
				{
					Cell cell = row.getCell( j );
					if ( cell.getStatus( ) == Cell.CELL_USED )
					{
						CellContent content = ( CellContent ) cell.getContent( );
						content.cell.setColumn( cell.getColId( ) );
						content.cell.setRowSpan( cell.getRowSpan( ) );
						content.cell.setColSpan( cell.getColSpan( ) );

						emitter.startCell( content.cell );
						if ( content.buffer != null )
						{
							content.buffer.flush( );
						}
						emitter.endCell( content.cell );
					}
					if ( cell.getStatus( ) == Cell.CELL_EMPTY )
					{
						ICellContent cellContent = report.createCellContent( );
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

			protected CellContent( ICellContent cell,
					BufferedReportEmitter buffer )
			{
				this.cell = cell;
				this.buffer = buffer;

			}

			public boolean isEmpty( )
			{
				return buffer == null || buffer.isEmpty( );
			}
		}
	}
}