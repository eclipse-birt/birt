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

package org.eclipse.birt.report.engine.internal.executor.dup;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.DataItemExecutionState;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.executor.SuppressDuplicateUtil;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class SuppressDuplciateReportExecutor implements IReportExecutor
{

	private IReportContent report;

	private IReportExecutor executor;

	private SuppressDuplicateVisitor visitor;

	private LinkedList executors = new LinkedList( );

	public SuppressDuplciateReportExecutor( IReportExecutor executor )
	{
		this.executor = executor;
		this.visitor = new SuppressDuplicateVisitor( );
	}

	public void close( )
	{
		executor.close( );
	}

	public IReportItemExecutor createPageExecutor( long pageNumber,
			MasterPageDesign pageDesign )
	{
		return executor.createPageExecutor( pageNumber, pageDesign );
	}

	public IReportContent execute( )
	{
		if ( report == null )
		{
			report = executor.execute( );
		}
		return report;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = executor.getNextChild( );
		if ( childExecutor != null )
		{
			return createExecutor( childExecutor );
		}
		return null;
	}

	public boolean hasNextChild( )
	{
		return executor.hasNextChild( );
	}

	IReportItemExecutor createExecutor( IReportItemExecutor executor )
	{
		SuppressDuplicateItemExecutor pooledExecutor = null;
		if ( executors.isEmpty( ) )
		{
			pooledExecutor = new SuppressDuplicateItemExecutor( this );
		}
		else
		{
			pooledExecutor = (SuppressDuplicateItemExecutor) executors
					.removeLast( );
		}
		pooledExecutor.executor = executor;
		return pooledExecutor;
	}

	void release( IReportItemExecutor executor )
	{
		executors.addLast( executor );
	}

	IContent suppressDuplicate( IContent content )
	{
		return (IContent) content.accept( visitor, content );
	}

	private class SuppressDuplicateVisitor extends ContentVisitorAdapter
	{

		public Object visitContent( IContent content, Object param )
		{
			return content;
		}

		public Object visitData( IDataContent data, Object param )
		{
			IDataContent dataContent = (IDataContent) data;
			Object genBy = dataContent.getGenerateBy( );
			if ( genBy instanceof DataItemDesign )
			{
				DataItemDesign dataDesign = (DataItemDesign) genBy;
				if ( dataDesign.getSuppressDuplicate( ) )
				{
					Object value = dataContent.getValue( );
					DataItemExecutionState state = (DataItemExecutionState) dataDesign
							.getExecutionState( );
					if ( state != null )
					{
						Object lastValue = state.lastValue;
						if ( lastValue == value
								|| ( lastValue != null && lastValue
										.equals( value ) ) )
						{
							return null;
						}
					}
					if ( state == null )
					{
						state = new DataItemExecutionState( );
						dataDesign.setExecutionState( state );
					}
					state.lastValue = value;
				}
			}
			return data;
		}

		public Object visitGroup( IGroupContent group, Object value )
		{
			Object genBy = group.getGenerateBy( );
			if ( genBy instanceof GroupDesign )
			{
				clearDuplicateFlags( (GroupDesign) genBy );
			}
			return group;

		}

		public Object visitList( IListContent list, Object value )
		{
			Object genBy = list.getGenerateBy( );
			if ( genBy instanceof ListingDesign )
			{
				clearDuplicateFlags( (ListingDesign) genBy );
			}
			return list;
		}

		public Object visitTable( ITableContent table, Object value )
		{
			Object genBy = table.getGenerateBy( );
			if ( genBy instanceof ListingDesign )
			{
				clearDuplicateFlags( (ListingDesign) genBy );
			}
			return table;
		}

		protected void clearDuplicateFlags( ListingDesign listingDesign )
		{
			SuppressDuplicateUtil.clearDuplicateFlags( listingDesign );
		}

		protected void clearDuplicateFlags( GroupDesign groupDesign )
		{
			ListingDesign listingDesign = getListingDesign( groupDesign );
			int groupLevel = groupDesign.getGroupLevel( );
			int groupCount = listingDesign.getGroupCount( );

			for ( int i = groupLevel; i < groupCount; i++ )
			{
				GroupDesign group = listingDesign.getGroup( i );
				SuppressDuplicateUtil.clearDuplicateFlags( group );
			}
			SuppressDuplicateUtil.clearDuplicateFlags( listingDesign
					.getDetail( ) );
		}

		protected ListingDesign getListingDesign( GroupDesign groupDesign )
		{
			long listingId = groupDesign.getHandle( ).getContainer( ).getID( );
			return (ListingDesign) report.getDesign( ).getReportItemByID(
					listingId );
		}

	}

}
