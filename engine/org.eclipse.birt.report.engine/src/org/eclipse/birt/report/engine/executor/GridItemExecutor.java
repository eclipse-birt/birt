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

import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.data.dte.DteResultSet;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.script.element.RowData;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridRowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridScriptExecutor;

/**
 * the gridItem excutor
 * 
 * @version $Revision: 1.23 $ $Date: 2005/12/03 05:34:28 $
 */
public class GridItemExecutor extends QueryItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public GridItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * execute a grid. The execution process is:
	 * <li> create a Table content
	 * <li> push it into the stack
	 * <li> execute the query and seek to the first record
	 * <li> process the table style, visiblity, action, bookmark
	 * <li> execute the onCreate if necessary.
	 * <li> call emitter to start the grid.
	 * <li> for each row, execute the row.
	 * <li> call emitter to close the grid.
	 * <li> close the query
	 * <li> popup the table.
	 * 
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemExcutor#excute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		GridItemDesign gridItem = ( GridItemDesign ) item;
		ITableContent tableObj = report.createTableContent( );
		IContent parent = context.getContent( );
		context.pushContent( tableObj );

		openResultSet( item );
		accessQuery( item, emitter );

		initializeContent( parent, item, tableObj );

		processAction( item, tableObj );
		processBookmark( item, tableObj );
		processStyle( item, tableObj );
		processVisibility( item, tableObj );

		for ( int i = 0; i < gridItem.getColumnCount( ); i++ )
		{
			ColumnDesign columnDesign = gridItem.getColumn( i );
			Column column = new Column( );
			column.setStyleClass( columnDesign.getStyleName( ) );
			column.setWidth( columnDesign.getWidth( ) );
			tableObj.addColumn( column );
		}

		IBaseQueryDefinition query = item.getQuery( );
		IRowData rowData = null;
		if(query!=null)
		{
			Collection rowExpressions = ( query
					.getRowExpressions( ) );
			
			IResultIterator rsIterator = ( ( DteResultSet ) rset )
					.getResultIterator( );
			rowData = new RowData( rsIterator, rowExpressions );
		}
		if ( context.isInFactory( ) )
		{

			GridScriptExecutor.handleOnCreate( ( TableContent ) tableObj,
					rowData, context );

		}
		startTOCEntry( tableObj );
		if ( emitter != null )
		{
			emitter.startTable( tableObj );
		}

		ITableBandContent body = report.createTableBody( );
		initializeContent( tableObj, null, body );

		context.pushContent( body );

		startTOCEntry( body );
		if ( emitter != null )
		{
			emitter.startTableBody( body );
		}

		for ( int i = 0; i < gridItem.getRowCount( ); i++ )
		{
			RowDesign row = gridItem.getRow( i );
			if ( row != null )
			{
				executeRow( i, body, row, emitter );
			}
		}
		if ( emitter != null )
		{
			emitter.endTableBody( body );
		}
		finishTOCEntry( );
		context.popContent( );

		if ( emitter != null )
		{
			emitter.endTable( tableObj );
		}
		finishTOCEntry( );
		closeResultSet( );
		context.popContent( );
	}

	/**
	 * execute the row. The execution process is:
	 * <li> create a row content
	 * <li> push it into the context
	 * <li> intialize the content.
	 * <li> process bookmark, action, style and visibility
	 * <li> call onCreate if necessary
	 * <li> call emitter to start the row
	 * <li> for each cell, execute the cell
	 * <li> call emitter to close the row
	 * <li> pop up the row.
	 * 
	 * @param rowId
	 *            row id.
	 * @param body
	 *            table body.
	 * @param row
	 *            row design
	 * @param emitter
	 *            output emitter
	 */
	private void executeRow( int rowId, ITableBandContent body, RowDesign row,
			IContentEmitter emitter )
	{
		IRowContent rowContent = report.createRowContent( );
		rowContent.setRowID( rowId );
		assert ( rowContent instanceof RowContent );
		context.pushContent( rowContent );

		initializeContent( body, row, rowContent );

		processAction( row, rowContent );
		processBookmark( row, rowContent );
		processStyle( row, rowContent );
		processVisibility( row, rowContent );

		//TODO: Right now row.getQuery() will always return null
		//This is filed as bug #119153 
		IRowData rowData = null;
		IBaseQueryDefinition query = row.getQuery( );
		if(query!=null)
		{
			Collection rowExpressions = ( query
					.getRowExpressions( ) );
			IResultIterator rsIterator = ( ( DteResultSet ) rset )
					.getResultIterator( );
			rowData = new RowData( rsIterator, rowExpressions );
		}
		if ( context.isInFactory( ) )
		{
			GridRowScriptExecutor.handleOnCreate( ( RowContent ) rowContent,
					rowData, context );
		}
		

		startTOCEntry( rowContent );
		if ( emitter != null )
		{
			emitter.startRow( rowContent );
		}

		for ( int j = 0; j < row.getCellCount( ); j++ )
		{
			CellDesign cell = row.getCell( j );
			if ( cell != null )
			{
				executeCell( rowContent, cell, emitter, rowData );
			}
		}
		if ( emitter != null )
		{
			emitter.endRow( rowContent );
		}
		finishTOCEntry( );
		context.popContent( );
	}

	/**
	 * execute a cell. the execution process is:
	 * <li> create a cell content
	 * <li> push the content into the stack
	 * <li> intialize the cell
	 * <li> process the action, bookmark, style, visibility
	 * <li> call onCreate if necessary
	 * <li> call emitter to start the cell
	 * <li> for each element in the cell, execute the element.
	 * <li> call emiter to close the cell
	 * <li> popup the cell.
	 * 
	 * @param rowContent
	 *            row content
	 * @param cell
	 *            cell design
	 * @param emitter
	 *            output emitter
	 */
	private void executeCell( IRowContent rowContent, CellDesign cell,
			IContentEmitter emitter, IRowData rowData )
	{
		ICellContent cellContent = report.createCellContent( );
		assert ( cellContent instanceof CellContent );
		context.pushContent( cellContent );

		initializeContent( rowContent, cell, cellContent );

		cellContent.setColumn( cell.getColumn( ) );
		cellContent.setColSpan( cell.getColSpan( ) );
		cellContent.setRowSpan( cell.getRowSpan( ) );

		processAction( cell, cellContent );
		processBookmark( cell, cellContent );
		processStyle( cell, cellContent );
		processVisibility( cell, cellContent );

		if ( context.isInFactory( ) )
		{
			CellScriptExecutor.handleOnCreate( ( CellContent ) cellContent,
					rowData, context );
		}

		startTOCEntry( cellContent );
		if ( emitter != null )
		{
			emitter.startCell( cellContent );
		}

		for ( int m = 0; m < cell.getContentCount( ); m++ )
		{
			ReportItemDesign ri = cell.getContent( m );
			if ( ri != null )
			{
				ri.accept( this.visitor, emitter );
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