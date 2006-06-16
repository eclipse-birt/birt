/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

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
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.TableContentLayout;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;

public class HTMLTableLM extends HTMLBlockStackingLM
{

	/**
	 * emitter used to layout the table
	 */
	protected TableLayoutEmitter tableEmitter;

	public HTMLTableLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_TABLE;
	}

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		ITableContent tableContent = (ITableContent) content;
		tableEmitter = new TableLayoutEmitter( tableContent, emitter );
		super.initialize( parent, content, executor, tableEmitter );
	}

	protected void repeatHeader( )
	{
		ITableContent table = (ITableContent) content;
		if ( table.isHeaderRepeat( ) )
		{
			IBandContent header = table.getHeader( );
			if ( header != null )
			{
				boolean pageBreak = context.allowPageBreak( );
				boolean skipPageHint = context.getSkipPageHint( );
				context.setAllowPageBreak( pageBreak );
				context.setSkipPageHint( true );
				engine.layout( header, emitter );
				context.setAllowPageBreak( pageBreak );
				context.setSkipPageHint( skipPageHint );
			}
		}
	}

	protected boolean layoutChildren( )
	{
		repeatHeader( );
		boolean hasNext = super.layoutChildren( );
		tableEmitter.resolveAll( );
		tableEmitter.flush( );
		return hasNext;
	}

	public void updateDropCells( int groupLevel, boolean dropAll )
	{
		tableEmitter.resolveCellsOfDrop( groupLevel, dropAll );
	}

	/**
	 * FIXME: we may need keep the group/band information. To do this, we need a
	 * list to save the group/band/row events, in flush the row out, we need
	 * first flush out the kept group/band, then flush out the row.
	 * 
	 */
	private class TableLayoutEmitter extends ContentEmitterAdapter
	{

		private ITableContent tableContent;

		private TableContentLayout layout;

		private IContentEmitter emitter;

		/**
		 * emitter used to
		 */
		private BufferedReportEmitter cellEmitter;

		private Stack groupStack = new Stack( );

		TableLayoutEmitter( ITableContent tableContent, IContentEmitter emitter )
		{
			this.tableContent = tableContent;
			layout = new TableContentLayout( tableContent,
					EngineIRConstants.FORMAT_TYPE_VIEWER );
			this.emitter = emitter;
		}

		public void startTable( ITableContent table )
		{
			if ( cellEmitter != null )
			{
				cellEmitter.startTable( table );
			}
			else
			{
				ContentEmitterUtil.startContent( table, emitter );
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
				ContentEmitterUtil.endContent( table, emitter );
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
			}
		}

		public void startRow( IRowContent row )
		{
			if ( cellEmitter != null )
			{
				ContentEmitterUtil.startContent( row, emitter );
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
			if ( cellEmitter == null )
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
				layout.createCell( colId, rowSpan, colSpan, new CellContent(
						cell, cellEmitter ) );
			}
			else
			{
				cellEmitter.startCell( cell );
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
				assert cellEmitter != null;
				cellEmitter.endCell( cell );
			}
		}

		public void startContent( IContent content )
		{
			assert cellEmitter != null;
			ContentEmitterUtil.startContent( content, cellEmitter );
		}

		public void endContent( IContent content )
		{
			assert cellEmitter != null;
			ContentEmitterUtil.endContent( content, cellEmitter );
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
						IReportContent report = tableContent.getReportContent( );
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

			public void reset( )
			{
				buffer = null;
			}
		}
	}
}
