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
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * the gridItem excutor
 * 
 * @version $Revision: #3 $ $Date: 2005/02/02 $
 */
public class GridItemExecutor extends StyledItemExecutor
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
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemExcutor#excute()
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		GridItemDesign gridItem = (GridItemDesign) item;
		ITableEmitter tableEmitter = emitter.getTableEmitter( );
		if ( tableEmitter == null )
		{
			return;
		}
		TableContent tableObj = new TableContent( gridItem );
		setStyles( tableObj, item );
		setVisibility( item, tableObj );

		String bookmarkStr = evalBookmark( gridItem );
		if (bookmarkStr != null)
			tableObj.setBookmarkValue(bookmarkStr);
		
		IResultSet rs = openResultSet(item);
		if(rs != null)
		{
			rs.next();
		}
		tableEmitter.start( tableObj );

		if ( gridItem.getColumnCount( ) > 0 )
		{
			tableEmitter.startColumns( );
			for ( int i = 0; i < gridItem.getColumnCount( ); i++ )
			{
				ColumnContent colContent = new ColumnContent( gridItem
						.getColumn( i ) );
				setStyles( colContent, gridItem.getColumn( i ) );
				emitter.getTableEmitter( ).startColumn( colContent );
				emitter.getTableEmitter( ).endColumn( );
			}
			tableEmitter.endColumns( );
		}
		tableEmitter.startBody( );
		for ( int i = 0; i < gridItem.getRowCount( ); i++ )
		{
			RowDesign row = gridItem.getRow( i );
//			if ( !isRowVisible( row ) )
//			{
//				break;
//			}
			RowContent rowContent = new RowContent( row );
			setVisibility(row,rowContent);
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
						ReportItemDesign ri = cell.getContent( m );
						if ( ri != null )
						{					
							ri.accept( this.visitor );
						}
					}

					tableEmitter.endCell( );
				}
			}
			tableEmitter.endRow( );
		}
		tableEmitter.endBody( );
		tableEmitter.end( );
		closeResultSet(rs);
		context.exitScope( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}

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
}
