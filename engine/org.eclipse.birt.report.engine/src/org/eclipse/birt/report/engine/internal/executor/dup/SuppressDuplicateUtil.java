/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.executor.dup;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.executor.DataItemExecutionState;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.model.api.TableHandle;

public class SuppressDuplicateUtil
{

	private HashMap<DataItemDesign, DataItemExecutionState> states = new HashMap<DataItemDesign, DataItemExecutionState>( );

	private Report report;
	private ClearDuplicateFlagVisitor resetVisitor;

	public SuppressDuplicateUtil( Report report )
	{
		this.report = report;
		resetVisitor = new ClearDuplicateFlagVisitor( );
	}

	/**
	 * clear the execution state of the elements
	 * 
	 * @param list
	 */
	public void clearDuplicateFlags( IContent content )
	{
		Object itemDesign = content.getGenerateBy( );
		if ( itemDesign instanceof GroupDesign )
		{
			GroupDesign groupDesign = (GroupDesign) itemDesign;
			ListingDesign listingDesign = getListingDesign( groupDesign );

			if ( !isGroupEmpty( groupDesign ) )
			{
				int groupLevel = groupDesign.getGroupLevel( );
				int groupCount = listingDesign.getGroupCount( );

				for ( int i = groupLevel + 1; i < groupCount; i++ )
				{
					GroupDesign group = listingDesign.getGroup( i );
					group.accept( resetVisitor, null );
				}

				BandDesign detail = listingDesign.getDetail( );
				if ( detail != null )
				{
					detail.accept( resetVisitor, null );
				}
			}
		}
		else if ( itemDesign instanceof ListingDesign )
		{
			( (ListingDesign) itemDesign ).accept( resetVisitor, null );
		}
	}

	protected ListingDesign getListingDesign( GroupDesign groupDesign )
	{
		long listingId = groupDesign.getHandle( ).getContainer( ).getID( );
		return (ListingDesign) report.getReportItemByID( listingId );
	}

	protected boolean isGroupEmpty( GroupDesign groupDesign )
	{
		BandDesign header = groupDesign.getHeader( );
		BandDesign footer = groupDesign.getFooter( );
		if ( header != null && header.getContentCount( ) != 0 || footer != null
				&& footer.getContentCount( ) != 0 )
		{
			return false;
		}
		return true;
	}

	public IContent suppressDuplicate( IContent content ) throws BirtException
	{
		int contentType = content.getContentType( );
		if ( contentType != IContent.DATA_CONTENT )
		{
			return content;
		}
		IDataContent dataContent = (IDataContent) content;
		Object genBy = dataContent.getGenerateBy( );
		if ( genBy instanceof DataItemDesign )
		{
			DataItemDesign dataDesign = (DataItemDesign) genBy;
			if ( dataDesign.getSuppressDuplicate( )
					&& states.containsKey( dataDesign ) )
			{
				Object value = dataContent.getValue( );
				DataItemExecutionState state = states.get( dataDesign );
				if ( state != null )
				{
					Object lastValue = state.lastValue;
					if ( lastValue == value
							|| ( lastValue != null && lastValue.equals( value ) ) )
					{
						return null;
					}
				}
				if ( state == null )
				{
					state = new DataItemExecutionState( );
					states.put( dataDesign, state );
				}
				state.lastValue = value;
			}
		}
		return content;
	}

	protected class ClearDuplicateFlagVisitor
			extends
				DefaultReportItemVisitorImpl
	{

		Stack<Boolean> isInDetailBand = new Stack<Boolean>( );

		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			for ( int i = 0; i < container.getItemCount( ); i++ )
			{
				container.getItem( i ).accept( this, value );
			}
			return value;
		}
		
		public Object visitTableItem( TableItemDesign table, Object value )
		{
			int detailBandId = getDetailBand( table );
			BandDesign header = table.getHeader( );
			if ( header != null )
			{
				value = header.accept( this, value );
			}
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				GroupDesign group = table.getGroup( i );
				if ( detailBandId == i )
				{
					isInDetailBand.push( Boolean.TRUE );
					value = group.accept( this, value );
					isInDetailBand.pop( );
				}
				else
				{
					value = group.accept( this, value );
				}
			}

			BandDesign detail = table.getDetail( );
			if ( detail != null )
			{
				if ( detailBandId == -1 )
				{
					isInDetailBand.push( Boolean.TRUE );
					value = detail.accept( this, value );
					isInDetailBand.pop( );
				}
				else
				{
					value = detail.accept( this, value );
				}
			}

			BandDesign footer = table.getFooter( );
			if ( footer != null )
			{
				value = footer.accept( this, value );
			}
			
			return value;
		}

		protected int getDetailBand( TableItemDesign table )
		{
			BandDesign detail = table.getDetail( );
			if ( !isBandEmpty( detail ) )
			{
				return -1;
			}
			
			for ( int i = table.getGroupCount( ) - 1; i >= 0; i-- )
			{
				GroupDesign group = table.getGroup( i );
				if ( !isBandEmpty( group.getHeader( ) )
						|| !isBandEmpty( group.getFooter( ) ) )
				{
					return i;
				}
			}
			return -1;
		}
		
		protected boolean isBandEmpty( BandDesign band )
		{
			if ( band != null )
			{
				for ( int i = 0; i < band.getContentCount( ); i++ )
				{
					RowDesign row = (RowDesign) band.getContent( i );
					for ( int j = 0; j < row.getCellCount( ); j++ )
					{
						CellDesign cell = row.getCell( j );
						if ( cell.getContentCount( ) > 0 )
						{
							return false;
						}
					}
				}
			}
			return true;
		}

		public Object visitListing( ListingDesign list, Object value )
		{
			BandDesign header = list.getHeader( );
			if ( header != null )
			{
				value = header.accept( this, value );
			}
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				GroupDesign group = list.getGroup( i );
				value = group.accept( this, value );
			}

			BandDesign detail = list.getDetail( );
			if ( detail != null )
			{
				value = detail.accept( this, value );
			}

			BandDesign footer = list.getFooter( );
			if ( footer != null )
			{
				value = footer.accept( this, value );
			}
			return value;
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			if(!isInDetailBand.isEmpty( ) && isInDetailBand.peek( ))
			{
				states.put( data, null );
			}
			return value;
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				value = grid.getRow( i ).accept( this, value );
			}
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

		public Object visitBand( BandDesign band, Object value )
		{
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				value = band.getContent( i ).accept( this, value );
			}
			return value;
		}

		public Object visitGroup( GroupDesign group, Object value )
		{
			BandDesign header = group.getHeader( );
			if ( header != null )
			{
				value = header.accept( this, value );
			}
			BandDesign footer = group.getFooter( );
			if ( footer != null )
			{
				value = footer.accept( this, value );
			}
			return value;
		}
	}
}
