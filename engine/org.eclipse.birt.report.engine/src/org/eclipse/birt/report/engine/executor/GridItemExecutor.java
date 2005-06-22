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

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ColumnContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * the gridItem excutor
 * 
 * @version $Revision: 1.12 $ $Date: 2005/05/18 02:15:18 $
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
		TableContent tableObj = (TableContent)ContentFactory.createTableContent( gridItem, context.getContentObject( ) );
		
		IResultSet rs = null;
		try
		{
			rs = openResultSet( item );
			if ( rs != null )
			{
				rs.next( );
			}
			setStyles( tableObj, item );
			setVisibility( item, tableObj );

			String bookmarkStr = evalBookmark( gridItem );
			if ( bookmarkStr != null )
				tableObj.setBookmarkValue( bookmarkStr );
			
			tableEmitter.start( tableObj );

			if ( gridItem.getColumnCount( ) > 0 )
			{
				tableEmitter.startColumns( );
				for ( int i = 0; i < gridItem.getColumnCount( ); i++ )
				{
					ColumnContent colContent = (ColumnContent) ContentFactory
							.createColumnContent( gridItem.getColumn( i ),
									tableObj );
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
				RowContent rowContent = (RowContent) ContentFactory
						.createRowContent( row, tableObj );
				setVisibility( row, rowContent );
				setBookmarkValue( row, rowContent );
				setStyles( rowContent, row );

				tableEmitter.startRow( rowContent );
				for ( int j = 0; j < row.getCellCount( ); j++ )
				{
					CellDesign cell = row.getCell( j );
					if ( cell != null )
					{
						CellContent cellContent = (CellContent) ContentFactory
								.createCellContent( cell, rowContent );
						context.pushContentObject( cellContent );
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
						context.popContentObject( );
					}
				}
				tableEmitter.endRow( );
			}
			tableEmitter.endBody( );
			tableEmitter.end( );
		}
		catch ( Throwable t )
		{
			logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
			context.addException( new EngineException( MessageConstants.GRID_PROCESSING_ERROR,
					( item.getName( ) != null ? item.getName( ) : "" ),
					t ) );//$NON-NLS-1$

		}
		finally
		{
			closeResultSet( rs );			
		}
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
}