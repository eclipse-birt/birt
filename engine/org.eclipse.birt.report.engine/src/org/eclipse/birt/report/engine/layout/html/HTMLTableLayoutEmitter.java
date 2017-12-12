/***********************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
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
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class HTMLTableLayoutEmitter extends ContentEmitterAdapter
{

	final static Logger logger = Logger.getLogger( HTMLTableLayoutEmitter.class
			.getName( ) );

	/**
	 * the emitter used to output the table content
	 */
	protected final IContentEmitter emitter;

	/**
	 * the current table layout
	 */
	protected TableContentLayout layout;

	/**
	 * the cached start/end content events
	 */
	protected Stack layoutEvents;

	/**
	 * emitter used to cache the content in current cell.
	 */
	protected IContentEmitter cellEmitter;
	
	protected HTMLLayoutContext context; 


	/**
	 * the group level information used to resovle the drop cells.
	 */
	protected Stack groupStack = new Stack( );

	protected HashMap<String, UnresolvedRowHint> hintMap = new HashMap<String, UnresolvedRowHint>();
	
	protected boolean isFirst = true;
	
	int nestTableCount = 0;
	
	protected int lastRowId = -1;
	
	public HTMLTableLayoutEmitter( IContentEmitter emitter,HTMLLayoutContext context  )
	{
		this.emitter = emitter;
		this.context = context;
	}
	
	public void end( IReportContent report ) throws BirtException
	{
		emitter.end( report );
	}

	public String getOutputFormat( )
	{
		return emitter.getOutputFormat( );
	}

	public void initialize( IEmitterServices service ) throws BirtException
	{
		emitter.initialize( service );
	}

	public void start( IReportContent report ) throws BirtException
	{
		emitter.start( report );
	}

	protected int getGroupLevel( )
	{
		if ( !groupStack.isEmpty( ) )
		{
			return ( (Integer) groupStack.peek( ) ).intValue( );
		}
		return -1;
	}
	
	protected boolean isContentFinished(IContent content)
	{
		if(context!=null)
		{
			return context.getPageHintManager( ).getLayoutHint( content );
		}
		return true;
	}
	
	protected boolean allowPageBreak()
	{
		if(context!=null)
		{
			return context.allowPageBreak( );
		}
		return false;
	}

	public void startContent( IContent content ) throws BirtException
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

	public void endContent( IContent content ) throws BirtException
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

	boolean hasDropCell = false;

	public void resetLayout( )
	{
		layout.reset( );
		layoutEvents.clear( );
		hasDropCell = false;
	}

	public void initLayout( ITableContent table )
	{
		String keyString = context.getPageHintManager( ).getHintMapKey(table.getInstanceID( ).toUniqueString( ));
		this.layout = new TableContentLayout( table,
				getOutputFormat( ), context, keyString );
		this.layoutEvents = new Stack( );
		UnresolvedRowHint hint = null;
		if(isFirst)
		{
			if(context!=null)
			{
				hint = context.getPageHintManager( ).getUnresolvedRowHint( keyString );
				isFirst = false;
			}
		}
		if(hint == null )
		{
			hint = hintMap.get( keyString );
		}
		layout.setUnresolvedRowHint( hint );
	}
	
	public boolean isLayoutStarted()
	{
		return layout!=null;
	}

	protected boolean hasDropCell( )
	{
		return hasDropCell;
	}

	protected int createDropID( int groupIndex, String dropType )
	{
		int dropId = -10 * ( groupIndex + 1 );
		if ( "all".equals( dropType ) ) //$NON-NLS-1$
		{
			dropId--;
		}
		return dropId;
	}

	public void resolveCellsOfDrop( int groupLevel, boolean dropAll, boolean finished )
	{
		if ( hasDropCell )
		{
			if ( dropAll )
			{
				layout.resolveDropCells( createDropID( groupLevel, "all" ), finished ); //$NON-NLS-1$
			}
			else
			{
				layout.resolveDropCells( createDropID( groupLevel, "detail" ), finished ); //$NON-NLS-1$
			}
			hasDropCell = layout.hasDropCell( );
		}
	}
	
	protected static class LayoutEvent
	{
		final static int START_GROUP = 0;
		final static int START_BAND = 1;
		final static int END_GROUP = 2;
		final static int END_BAND = 3;
		final static int ON_ROW = 4;
		final static int ON_FIRST_DROP_CELL = 5;
		
		LayoutEvent(int type, Object value )
		{
			this.eventType= type;
			this.value = value;
		}
		int eventType;
		Object value;
	}
	
	protected static class StartInfo
	{
		StartInfo(int rowId, int cellId)
		{
			this.rowId = rowId;
			this.cellId = cellId;
		}
		int rowId;
		int cellId;
	}

	public static class CellContent implements Cell.Content
	{

		protected ICellContent cell;

		protected BufferedReportEmitter buffer;

		public CellContent( ICellContent cell, BufferedReportEmitter buffer )
		{
			this.cell = cell;
			this.buffer = buffer;

		}

		public ICellContent getContent()
		{
			return cell;
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
	
	public void flush( ) throws BirtException
	{
		if ( hasDropCell( ) )
		{
			return;
		}
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
					flushRow( ( (StartInfo) event.value ).rowId , 0, true );
					break;
				case LayoutEvent.ON_FIRST_DROP_CELL:
					flushRow( ( (StartInfo) event.value ).rowId,
							( (StartInfo) event.value ).cellId, false );
					break;
			}
		}
		resetLayout();
	}
	
	protected void flushRow( int rowId, int colId, boolean withStart )
			throws BirtException
	{
		int colCount = layout.getColCount( );
		int columnId = layout.getColumnId( colId );
		Row row = layout.getRow( rowId );
		IRowContent rowContent = (IRowContent) row.getContent( );
		if ( withStart )
		{
			emitter.startRow( rowContent );
		}
		for ( int j = columnId; j < colCount; j++ )
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
				cellContent.setColumn( j );
				cellContent.setRowSpan( cell.getRowSpan( ) );
				cellContent.setColSpan( cell.getColSpan( ) );
				emitter.startCell( cellContent );
				emitter.endCell( cellContent );
			}
		}
		emitter.endRow( rowContent );
	}

	private boolean isNestTable( )
	{
		return nestTableCount > 1;
	}

	public void startTable( ITableContent table ) throws BirtException
	{
		nestTableCount++;
		if ( cellEmitter != null )
		{
			cellEmitter.startTable( table );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				UnresolvedRowHint hint = null;
				initLayout( table );
				emitter.startTable( layout.getWrappedTableContent( ) );
				this.lastRowId = -1;
			}
			else
			{
				emitter.startTable( table );
			}
		}
	}

	public void resolveAll( boolean finished )
	{
		layout.resolveDropCells( finished );
		UnresolvedRowHint hint = layout.getUnresolvedRow( );
		if ( hint != null )
		{
			hintMap.put( layout.getKeyString( ), hint );
			if(context!=null )
			{
				context.getPageHintManager( ).addUnresolvedRowHint(layout.getKeyString( ), hint );
			}
		}
		
		hasDropCell = layout.hasDropCell( );
	}
		
	public void createCell( int colId, int rowSpan, int colSpan,
			Cell.Content cellContent )
	{
		layout.createCell( colId, rowSpan, colSpan, cellContent );
		if ( rowSpan < 0  || rowSpan > 1)
		{
			hasDropCell = true;
		}
	}
	
	public void endTable( ITableContent table ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTable( table );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				resolveAll( isContentFinished(table) );
				flush( );
				emitter.endTable( layout.getWrappedTableContent( ) );
			}
			else
			{
				emitter.endTable( table );
			}
		}
		nestTableCount--;
	}

	public void startTableGroup( ITableGroupContent group )
			throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startTableGroup( group );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				int groupLevel = group.getGroupLevel( );
				groupStack.push( Integer.valueOf( groupLevel ) );
				if ( hasDropCell( ) )
				{
					layoutEvents.push( new LayoutEvent(
							LayoutEvent.START_GROUP, group ) );
					return;
				}
			}
			emitter.startTableGroup( group );
		}
	}
	

	public void endTableGroup( ITableGroupContent group ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTableGroup( group );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				// if there is no group footer, we still need to do with the
				// drop.
				int groupLevel = getGroupLevel( );
				resolveCellsOfDrop( groupLevel, false, isContentFinished( group ) );
				resolveCellsOfDrop( groupLevel, true, isContentFinished( group ) );
				assert !groupStack.isEmpty( );
				groupStack.pop( );
				if ( hasDropCell( ) )
				{
					layoutEvents.push( new LayoutEvent( LayoutEvent.END_GROUP,
							group ) );
					return;
				}
				flush( );
			}
			emitter.endTableGroup( group );
		}
	}

	public void startTableBand( ITableBandContent band ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startTableBand( band );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				if ( band.getBandType( ) == IBandContent.BAND_GROUP_FOOTER )
				{
					int groupLevel = getGroupLevel( );
					resolveCellsOfDrop( groupLevel, false, true );
				}
				if ( hasDropCell( ) )
				{
					layoutEvents.push( new LayoutEvent( LayoutEvent.START_BAND,
							band ) );
					return;
				}
				flush( );
			}
			emitter.startTableBand( band );
		}
	}

	public void endTableBand( ITableBandContent band ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endTableBand( band );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				if(LayoutUtil.isRepeatableBand( band ))
				{
					lastRowId = -1;
				}
				if ( band.getBandType( ) == IBandContent.BAND_GROUP_FOOTER )
				{
					int groupLevel = getGroupLevel( );
					resolveCellsOfDrop( groupLevel, true, isContentFinished(band) );
				}
				if ( hasDropCell( ) )
				{
					layoutEvents.push( new LayoutEvent( LayoutEvent.END_BAND,
							band ) );
					return;
				}
				flush( );
			}
			emitter.endTableBand( band );
		}
	}

	public void startRow( IRowContent row ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startRow( row );
		}
		else
		{
			// For fixed layout reports and in run task, we need to emit the
			// invisible content to PDF layout engine.
			boolean hiddenMask = context.isFixedLayout( )
					&& (Integer) context.getLayoutEngine( ).getOption(
							EngineTask.TASK_TYPE ) == IEngineTask.TASK_RUN;
			boolean isHidden = LayoutUtil.isHidden( row, emitter
					.getOutputFormat( ), context.getOutputDisplayNone( ), hiddenMask );
			
			if ( !isNestTable( ) )
			{
				int rowId = row.getRowID( );
				if(lastRowId>=0 && rowId>lastRowId+1)
				{
					for(int i=lastRowId+1; i<rowId; i++)
					{
						IRowContent newRow = (IRowContent) row
								.cloneContent( false );
						newRow.setHeight( new DimensionType( 0,
								EngineIRConstants.UNITS_IN ) );
						newRow.setParent( row.getParent( ) );
						newRow.setRowID( i );
						startRow( newRow );
						layout.setNeedFormalize( true );
						endRow( newRow );
					}
				}
				layout.createRow( row, isHidden );
				if(!isHidden)
				{
					if ( hasDropCell( ) )
					{
						layoutEvents.push( new LayoutEvent( LayoutEvent.ON_ROW,
								new StartInfo( layout.getRowCount( ) - 1, 0 ) ) );
						return;
					}
					else if(layout.hasUnResolvedRow( ) && !LayoutUtil.isRepeatableRow( row ))
					{
						layoutEvents.push( new LayoutEvent( LayoutEvent.ON_ROW,
								new StartInfo( layout.getRowCount( ) - 1, 0) ) );
						hasDropCell = true;
						return;
					}
				}
					 
				// TODO: here we need handle the hidden row and change the row
				// id.
			}
			if(!isHidden)
			{
				emitter.startRow( row );
			}
			
		}
	}

	public void endRow( IRowContent row ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.endRow( row );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				layout.endRow(row);
				lastRowId = row.getRowID( );
				hasDropCell = layout.hasDropCell( );
				if ( hasDropCell( ) )
				{
					// Page break only if multiple page is enabled and cache exceed max limit
					if ( emitter.isMultiplePagesEnabled( ) && layout.exceedMaxCache( ) )
					{
						context.softRowBreak = true;
					}
					return;
				}
				if(layoutEvents.size( )>0)
				{
					flush( );
					return;
				}
			}
			// For fixed layout reports and in run task, we need to emit the
			// invisible content to PDF layout engine.
			boolean hiddenMask = context.isFixedLayout( )
					&& (Integer) context.getLayoutEngine( ).getOption(
							EngineTask.TASK_TYPE ) == IEngineTask.TASK_RUN;
			boolean isHidden = LayoutUtil.isHidden( row, emitter
					.getOutputFormat( ), context.getOutputDisplayNone( ), hiddenMask );

			if(!isHidden)
			{
				emitter.endRow( row );
			}
		}
	}

	public void startCell( ICellContent cell ) throws BirtException
	{
		if ( cellEmitter != null )
		{
			cellEmitter.startCell( cell );
		}
		else
		{
			if ( !isNestTable( ) )
			{
				BufferedReportEmitter buffer = null;
				int colId = cell.getColumn( );
				int colSpan = cell.getColSpan( );
				int rowSpan = cell.getRowSpan( );

				// the current executed cell is rowIndex, columnIndex
				// get the span value of that cell.
				if ( cell.getGenerateBy( ) instanceof CellDesign )
				{
					CellDesign cellDesign = (CellDesign) cell.getGenerateBy( );
					if ( cellDesign != null )
					{
						String dropType = cellDesign.getDrop( );
						if ( dropType != null && !"none".equals( dropType ) ) //$NON-NLS-1$
						{
							rowSpan = createDropID( getGroupLevel( ), dropType );
						}
					}
				}

				// the table has no cache, the cell is the first drop or spanned cell
				if ( !hasDropCell( ) && (rowSpan < 0 || rowSpan > 1) )
				{
					layoutEvents.push( new LayoutEvent(
							LayoutEvent.ON_FIRST_DROP_CELL, new StartInfo( layout
									.getRowCount( ) - 1, colId ) ) );
				}
				if ( hasDropCell( ) || rowSpan < 0 || rowSpan > 1)
				{
					buffer = new BufferedReportEmitter( emitter );
					cellEmitter = buffer;
				}
				// we need cache the cell
				createCell( colId, rowSpan, colSpan, new CellContent( cell,
						buffer ) );
				if ( hasDropCell( ) )
				{
					return;
				}
				// TODO: changes the column id and output it.
				emitter.startCell( layout.getWrappedCellContent( cell ) );
			}
			else
			{
				emitter.startCell( cell );
			}
		}
	}

	public void endCell( ICellContent cell ) throws BirtException
	{
		if ( !isNestTable( ) )
		{
			if ( cellEmitter != null )
			{
				cellEmitter = null;
				return;
			}
			else
			{
				emitter.endCell( layout.getWrappedCellContent( cell ) );
			}
		}
		else
		{
			if ( cellEmitter != null )
			{
				cellEmitter.endCell( cell );
			}
			else
			{
				emitter.endCell( cell );
			}
		}
	}

	public IContentEmitter getInternalEmitter( )
	{
		return emitter;
	}
}
