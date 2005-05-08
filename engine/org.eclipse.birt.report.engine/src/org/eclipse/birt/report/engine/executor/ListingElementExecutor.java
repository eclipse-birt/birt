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

import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;


/**
 * An abstract class that defines execution logic for a Listing element, 
 * which is the base element for table and list items.
 */
public abstract class ListingElementExecutor extends StyledItemExecutor
{

	/**
	 * resultset for listing
	 */
	protected IResultSet rs;

	/**
	 * group count
	 */
	protected int groupCount = 0;

	/**
	 * report emitter
	 */
	protected IReportEmitter emitter;
	
	/**
	 * listing item design
	 */
	protected ListingDesign listingItem; 

	/**
	 * @param context execution context
	 * @param visitor the visitor object that drives exection 
	 */
	protected ListingElementExecutor( ExecutionContext context,
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
		this.emitter = emitter;
		this.listingItem = (ListingDesign)item;
		groupCount = getGroupCount( item );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		rs = null;
		groupCount = 0;
		emitter = null;
		listingItem = null;
	}


	/**
	 * This function access a goup level of report The top level group is 0 The
	 * steps as follows:
	 * <li>access group header
	 * <li>access lower group level
	 * <li>access group footer
	 * <li>if the group level is not finished, move the cursor to next, and go
	 * to the first step
	 * 
	 * @param index
	 *            the group index
	 */
	protected void accessGroupOnce( int index )
	{
		accessGroupHeader( index );
		accessGroup( index + 1 );
		accessGroupFooter( index );
	}

	/**
	 * access a group defined for a listing element
	 * 
	 * @param index group index
	 */
	protected void accessGroup( int index )
	{
		if ( index == groupCount )
		{
			accessDetailOnce( );
			//while ( !rs.isGroupEnd( index ) )
			while ( rs.getEndingGroupLevel() > groupCount)
			{
				rs.next( );
				context.execute(listingItem.getOnRow());
				accessDetailOnce( );
			}
			return;
		}
		accessGroupOnce( index );
		while (rs.getEndingGroupLevel( ) > index )
		{
			rs.next( );
			accessGroupOnce( index );
		}
	}
	
	/**
	 * get group count
	 * @param item the report item that is a listing type element
	 * @return the number of groups defined for the element
	 */
	protected abstract int getGroupCount( ReportItemDesign item );

	/**
	 * access Listing header
	 */
	protected abstract void accessHeader( );

	/**
	 * access Listing footer
	 */
	protected abstract void accessFooter( );

	/**
	 * access detail band
	 */
	protected abstract void accessDetailOnce( );

	/**
	 * access listing group header
	 * @param index the group index
	 */
	protected abstract void accessGroupHeader( int index );

	/**
	 * access listing group footer
	 * @param index the group index
	 */
	protected abstract void accessGroupFooter( int index );

}