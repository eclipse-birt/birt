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

import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * An abstract class that defines execution logic for a Listing element, which
 * is the base element for table and list items.
 */
public abstract class ListingElementExecutor extends QueryItemExecutor
{

	private static final int FIRST_GROUP_INDEX = 0;

	/**
	 * the cursor position in the query result.
	 */
	protected int rsetCursor;

	protected boolean needPageBreak;

	/**
	 * emitter used to output the content
	 */
	protected IContentEmitter outputEmitter;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            the visitor object that drives exection
	 */
	protected ListingElementExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * access the query and create the contents. the execution process is:
	 * <li> the cursor is at the begin of result set.
	 * <li> call listing's onStart event
	 * <li> create the header
	 * <li> for each row:
	 * <ul>
	 * <li> call onRow event.
	 * <li> if the row start some groups, create the group header for that
	 * group.
	 * <li> create the detail row.
	 * <li> if the row end some groups, create the group footer for that group.
	 * </ul>
	 * <li> create the footer.
	 * <li> call the onFinish event.
	 */
	protected void accessQuery( ReportItemDesign design, IContentEmitter emitter )
	{
		ListingDesign listing = (ListingDesign) design;

		rsetCursor = -1;
		outputEmitter = emitter;

		int groupCount = listing.getGroupCount( );
		int NONE_GROUP = groupCount + 1;
		int groupIndex = -1;
		int pageBreakInterval = listing.getPageBreakInterval( );

		if ( rset == null || rsetEmpty == true )
		{
			// empty rset
			startTOCEntry( null );
			accessHeader( listing, outputEmitter, null );
			finishTOCEntry( );

			startTOCEntry( null );
			accessFooter( listing, outputEmitter, null );
			finishTOCEntry( );
			return;
		}

//		IResultIterator rsIterator = ( (DteResultSet) rset )
//				.getResultIterator( );

		startTOCEntry( null );
		accessHeader( listing, outputEmitter, rset );
		finishTOCEntry( );
		if ( groupCount == 0 )
		{
			// no group tables
			do
			{
				if ( context.isCanceled( ) )
				{
					break;
				}
				rsetCursor++;
				startTOCEntry( null );
				accessDetail( listing, FIRST_GROUP_INDEX, outputEmitter, rset );
				finishTOCEntry( );
				if ( pageBreakInterval > 0 )
				{
					if ( ( rsetCursor + 1 ) % pageBreakInterval == 0 )
					{
						needPageBreak = true;
					}
				}
				if ( rset.getEndingGroupLevel( ) == 0 )
				{
					// we never add page break before the table header and
					// the last row
					needPageBreak = false;
					startTOCEntry( null );
					accessFooter( listing, outputEmitter, rset );
					finishTOCEntry( );
				}
			} while ( rset.next( ) );
			return;
		}
		// multiple group tables

		// bug130622
		// if hideDetail be setted in some group,
		// all sub groups and details will be hidden.
		boolean hideDetail = false;
		int hideGroupStartIndex = -1;
		do
		{
			if ( context.isCanceled( ) )
			{
				break;
			}
			rsetCursor++;
			int startGroup = rset.getStartingGroupLevel( );

			ArrayList groupList = listing.getGroups( );

			if ( startGroup != NONE_GROUP )
			{
				// It start the group startGroup. It also start the
				// groups from startGroup to groupCount.
				groupIndex = startGroup - 1;
				if ( groupIndex < FIRST_GROUP_INDEX )
				{
					groupIndex = FIRST_GROUP_INDEX;
				}
				while ( groupIndex < groupCount
						&& ( hideGroupStartIndex == -1 || groupIndex < hideGroupStartIndex ) )
				{
					startGroupTOCEntry( );// open the group
					startTOCEntry( null );// open the group header
					accessGroupHeader( listing, groupIndex, outputEmitter, rset );
					finishTOCEntry( );// close the group header
					groupIndex++;

					GroupDesign groupDesign = (GroupDesign) groupList
							.get( groupIndex - 1 );
					if ( groupDesign.getHideDetail( )
							&& hideGroupStartIndex == -1 )
					{
						hideDetail = true;
						hideGroupStartIndex = groupIndex;
						break;
					}

				}
				// for each group, we should restart the duplicate state
				clearDuplicateFlags( listing );
			}
			startGroupTOCEntry( );

			if ( !hideDetail )
			{
				accessDetail( listing, groupIndex, outputEmitter, rset );
			}

			finishGroupTOCEntry( );
			int endGroup = rset.getEndingGroupLevel( );
			if ( endGroup != NONE_GROUP )
			{
				// the endGroup has terminate, it also termiate the
				// groups
				// from endGroup-1
				// to groupCount-1.
				endGroup = endGroup - 1;
				if ( endGroup < 0 )
				{
					endGroup = 0;
				}
				if ( hideGroupStartIndex == -1 )
				{
					groupIndex = groupCount - 1;
				}
				else
				{
					groupIndex = hideGroupStartIndex - 1;
				}
				while ( groupIndex >= endGroup )
				{
					startTOCEntry( null ); // open the group footer
					accessGroupFooter( listing, groupIndex, outputEmitter, rset );
					finishTOCEntry( ); // close the group footer
					finishGroupTOCEntry( ); // close the group
					groupIndex--;
				}
			}
			if ( pageBreakInterval > 0 )
			{
				if ( ( rsetCursor + 1 ) % pageBreakInterval == 0 )
				{
					needPageBreak = true;
				}
			}
			if ( rset.getEndingGroupLevel( ) == 0 )
			{
				// we never add page break before the table header and
				// the last row
				//needPageBreak = false;
				startTOCEntry( null );
				accessFooter( listing, outputEmitter, rset );
				finishTOCEntry( );
			}
		} while ( rset.next( ) );

	}

	/**
	 * create the group header
	 * 
	 * @param list
	 *            listing design
	 * @param index
	 *            group index.
	 * @param emitter
	 *            output emitter
	 */
	abstract protected void accessGroupHeader( ListingDesign list, int index,
			IContentEmitter emitter, IResultSet resultSet );

	/**
	 * create the group footer.
	 * 
	 * @param list
	 *            list design
	 * @param index
	 *            group index
	 * @param emitter
	 *            output emitter
	 */
	abstract protected void accessGroupFooter( ListingDesign list, int index,
			IContentEmitter emitter, IResultSet resultSet );

	/**
	 * create detail band.
	 * 
	 * @param list
	 *            listing design.
	 * @param emitter
	 *            output emitter
	 */
	abstract protected void accessDetail( ListingDesign list, int index,
			IContentEmitter emitter, IResultSet resultSet );

	/**
	 * create the header band
	 * 
	 * @param list
	 *            listing design
	 * @param emitter
	 *            output emitter
	 */
	abstract protected void accessHeader( ListingDesign list,
			IContentEmitter emitter, IResultSet resultSet );

	/**
	 * create the footer band.
	 * 
	 * @param list
	 *            listing design.
	 * @param emitter
	 *            output emitter
	 */
	abstract protected void accessFooter( ListingDesign list,
			IContentEmitter emitter, IResultSet resultSet );

	public void reset( )
	{
		this.rsetCursor = -1;
		super.reset( );
	}

	/**
	 * clear the execution state of the elements
	 * 
	 * @param list
	 */
	protected void clearDuplicateFlags( ListingDesign list )
	{
		list.accept( new ClearDuplicateFlagVisitor( ), null );
	}

	protected class ClearDuplicateFlagVisitor implements IReportItemVisitor
	{

		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			for ( int i = 0; i < container.getItemCount( ); i++ )
			{
				container.getItem( i ).accept( this, value );
			}
			return value;
		}

		public Object visitListItem( ListItemDesign list, Object value )
		{
			value = clearListBand( list.getHeader( ), value );
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				ListGroupDesign group = list.getGroup( i );
				value = clearListBand( group.getHeader( ), value );
				value = clearListBand( group.getFooter( ), value );
			}

			value = clearListBand( list.getDetail( ), value );

			clearListBand( list.getFooter( ), value );
			return null;
		}

		protected Object clearListBand( ListBandDesign band, Object value )
		{
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				value = band.getContent( i ).accept( this, value );
			}
			return value;
		}

		public Object visitTextItem( TextItemDesign text, Object value )
		{
			return value;
		}

		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			return value;
		}
		
		public Object visitAutoTextItem( AutoTextItemDesign autoText, Object value )
		{
			return value;
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			data.setExecutionState( null );
			return value;
		}

		public Object visitMultiLineItem( MultiLineItemDesign multiLine,
				Object value )
		{
			return null;
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				value = visitRow( grid.getRow( i ), value );
			}
			return value;
		}

		protected Object visitTableBand( TableBandDesign band, Object value )
		{
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				value = visitRow( band.getRow( i ), value );
			}
			return value;
		}

		public Object visitTableItem( TableItemDesign table, Object value )
		{
			value = visitTableBand( table.getHeader( ), value );
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				TableGroupDesign group = table.getGroup( i );
				value = visitTableBand( group.getHeader( ), value );
				value = visitTableBand( group.getFooter( ), value );
			}
			value = visitTableBand( table.getDetail( ), value );
			value = visitTableBand( table.getFooter( ), value );
			return value;
		}

		public Object visitRow( RowDesign row, Object value )
		{
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				value = visitCell( row.getCell( i ), value );
			}
			return value;
		}

		public Object visitCell( CellDesign cell, Object value )
		{
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				value = cell.getContent( i ).accept( this, value );
			}
			return value;
		}

		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			return value;
		}

		public Object visitExtendedItem( ExtendedItemDesign item, Object value )
		{
			return value;
		}
		public Object visitTemplate(TemplateDesign template, Object value)
		{
			return value;
		}
	}

}