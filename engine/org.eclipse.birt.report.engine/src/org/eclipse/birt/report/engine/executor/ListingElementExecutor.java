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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * An abstract class that defines execution logic for a Listing element, which
 * is the base element for table and list items.
 */
public abstract class ListingElementExecutor extends QueryItemExecutor
		implements
			IPageBreakListener
{

	/**
	 * the cursor position in the query result.
	 */
	protected int rsetCursor;

	protected boolean needPageBreak;
	
	protected int pageRowCount = 0;
	
	protected int pageBreakInterval = -1;
	
	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            the visitor object that drives exection
	 */
	protected ListingElementExecutor( ExecutorManager manager )
	{
		super( manager );
	}
	
	

	protected void initializeContent( ReportElementDesign design, IContent content )
	{
		super.initializeContent( design, content );
		if(isPageBreakIntervalValid((ListingDesign)design))
		{
			pageBreakInterval = ((ListingDesign)design).getPageBreakInterval( );
			context.addPageBreakListener( this );
		}
	}

	boolean isPageBreakIntervalValid(ListingDesign design)
	{
		BandDesign detailBand = design.getDetail( );
		if(detailBand==null || detailBand.getContentCount( )==0)
		{
			return false;
		}
		for(int i=0; i<design.getGroupCount( ); i++)
		{
			if(design.getGroup( i ).getHideDetail( ))
			{
				return false;
			}
		}
		return true;
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
	public void close( )
	{
		if ( pageBreakInterval != -1 )
		{
			context.removePageBreakListener( this );
		}
		rsetCursor = -1;
		needPageBreak = false;
		pageRowCount = 0;
		pageBreakInterval = -1;
		executableElements = null;
		// total bands in the executabelBands
		totalElements = 0;
		// band to be executed
		currentElement = 0;
		endOfListing = false;
		super.close( );
	}

	void nextRow( )
	{
		if ( pageBreakInterval > 0 )
		{
			pageRowCount++;
		}
	}
	
	boolean needSoftBreakBefore()
	{
		return (pageBreakInterval>0) && (pageBreakInterval<=pageRowCount);
	}
	
	public boolean hasNextChild( )
	{
		if ( currentElement < totalElements )
		{
			return true;
		}
		if ( endOfListing )
		{
			return false;
		}
		while ( !endOfListing )
		{
			int endGroup = rset.getEndingGroupLevel( );
			if ( endGroup <= 0 )
			{
				ListingDesign listingDesign = (ListingDesign) getDesign( );
				totalElements = 0;
				currentElement = 0;
				if ( listingDesign.getFooter( ) != null )
				{
					executableElements[totalElements++] = listingDesign
							.getFooter( );
				}
				endOfListing = true;
				return currentElement < totalElements;
			}
			if ( rset.next( ) )
			{
				nextRow( );
				collectExecutableElements( );
				if( currentElement < totalElements )
				{
					return true;
				}
			}
		}
		return false;
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			assert ( currentElement < totalElements );
			ReportItemDesign nextDesign = executableElements[currentElement++];

			ReportItemExecutor nextExecutor = manager.createExecutor( this,
					nextDesign );
			if ( nextExecutor instanceof GroupExecutor )
			{
				GroupExecutor groupExecutor = (GroupExecutor) nextExecutor;
				groupExecutor.setLisingExecutor( this );
			}
			return nextExecutor;
		}
		return null;
	}

	// bands to be execute in current row.
	ReportItemDesign[] executableElements;
	// total bands in the executabelBands
	int totalElements;
	// band to be executed
	int currentElement;
	boolean endOfListing;

	protected void prepareToExecuteChildren( )
	{
		ListingDesign listingDesign = (ListingDesign) getDesign( );

		// prepare the bands to be executed.
		executableElements = new ReportItemDesign[3];
		if ( rset == null || rsetEmpty )
		{
			BandDesign header = listingDesign.getHeader( );
			if ( header != null )
			{
				executableElements[totalElements++] = header;
			}
			BandDesign footer = listingDesign.getFooter( );
			if ( footer != null )
			{
				executableElements[totalElements++] = footer;
			}
			endOfListing = true;
		}
		else
		{
			collectExecutableElements( );
		}
		
		// clear the duplicate flag in the listing
		SuppressDuplicateUtil.clearDuplicateFlags( listingDesign );
	}

	void collectExecutableElements( )
	{
		currentElement = 0;
		totalElements = 0;
		endOfListing = false;
		ListingDesign listingDesign = (ListingDesign) getDesign( );
		int groupCount = listingDesign.getGroupCount( );
		int startGroup = rset.getStartingGroupLevel( );
		if ( startGroup == 0 )
		{
			// this is the first record
			BandDesign header = listingDesign.getHeader( );
			if ( header != null )
			{
				executableElements[totalElements++] = header;
			}
		}
		if ( groupCount > 0 )
		{
			executableElements[totalElements++] = listingDesign.getGroup( 0 );
		}
		else
		{
			BandDesign detail = listingDesign.getDetail( );
			if ( detail != null )
			{
				executableElements[totalElements++] = detail;
			}
		}
		int endGroup = rset.getEndingGroupLevel( );
		if ( endGroup <= 0 )
		{
			// this is the last record
			BandDesign footer = listingDesign.getFooter( );
			if ( footer != null )
			{
				executableElements[totalElements++] = footer;
			}
			endOfListing = true;
		}

	}

	public void onPageBreak( )
	{
		pageRowCount = 0;
	}
}