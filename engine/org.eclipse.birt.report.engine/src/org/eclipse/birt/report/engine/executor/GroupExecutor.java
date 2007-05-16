
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

abstract public class GroupExecutor extends ReportItemExecutor
{

	boolean endOfGroup;
	boolean hiddenDetail;
	boolean needPageBreak;

	ListingElementExecutor listingExecutor;

	protected GroupExecutor( ExecutorManager manager, int type )
	{
		super( manager, type );
	}

	public void close( )
	{
		endOfGroup = false;
		hiddenDetail = false;
		needPageBreak = false;
		listingExecutor = null;
		super.close( );
	}

	void setLisingExecutor( ListingElementExecutor executor )
	{
		listingExecutor = executor;
		rset = listingExecutor.rset;
	}

	public boolean hasNextChild( )
	{
		if ( currentElement < totalElements )
		{
			return true;
		}
		if ( endOfGroup )
		{
			return false;
		}

		try
		{
			// FIXME: is it right? (hiden detail)
			while ( !endOfGroup )
			{
				IQueryResultSet rset = listingExecutor.getResultSet( );
				GroupDesign groupDesign = (GroupDesign) getDesign( );
				int endGroup = rset.getEndingGroupLevel( );
				int groupLevel = groupDesign.getGroupLevel( ) + 1;
				if ( endGroup <= groupLevel )
				{
					totalElements = 0;
					currentElement = 0;
					BandDesign footer = groupDesign.getFooter( );
					if ( footer != null )
					{
						executableElements[totalElements++] = footer;
					}
					endOfGroup = true;
					return currentElement < totalElements;
				}
				if ( rset.next( ) )
				{
					listingExecutor.nextRow( );
					collectExecutableElements( );
					if ( currentElement < totalElements )
					{
						return true;
					}
				}
			}
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
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
				groupExecutor.setLisingExecutor( listingExecutor );
			}

			return nextExecutor;
		}
		return null;
	}

	// bands to be execute in current row.
	ReportItemDesign[] executableElements = new ReportItemDesign[3];
	// total bands in the executabelBands
	int totalElements;
	// band to be executed
	int currentElement;

	protected void prepareToExecuteChildren( )
	{
		// prepare the bands to be executed.
		collectExecutableElements( );
		// clear the duplicate flags in the group
		clearDuplicateFlags( );
	}

	void collectExecutableElements( )
	{
		currentElement = 0;
		totalElements = 0;
		endOfGroup = false;

		try
		{
			ListingDesign listingDesign = (ListingDesign) listingExecutor
					.getDesign( );
			IQueryResultSet rset = listingExecutor.getResultSet( );
			GroupDesign groupDesign = (GroupDesign) getDesign( );
			int groupCount = listingDesign.getGroupCount( );
			// compare with the start group, the start group
			// start with 0 --> listing
			// 1 --> first group (0)
			int groupLevel = groupDesign.getGroupLevel( ) + 1;
			int startGroup = rset.getStartingGroupLevel( );
			hiddenDetail = groupDesign.getHideDetail( );
			if ( startGroup <= groupLevel )
			{
				// this is the first record
				BandDesign header = groupDesign.getHeader( );
				if ( header != null )
				{
					executableElements[totalElements++] = header;
				}
			}
			if ( !hiddenDetail )
			{
				if ( groupCount > groupLevel )
				{
					executableElements[totalElements++] = listingDesign
							.getGroup( groupLevel );
				}
				else
				{
					BandDesign detail = listingDesign.getDetail( );
					if ( detail != null )
					{
						executableElements[totalElements++] = listingDesign
								.getDetail( );
					}
				}
			}
			int endGroup = rset.getEndingGroupLevel( );
			if ( endGroup <= groupLevel )
			{
				// this is the last record
				BandDesign footer = groupDesign.getFooter( );
				if ( footer != null )
				{
					executableElements[totalElements++] = groupDesign
							.getFooter( );
				}
				if ( endGroup <= groupLevel )
				{
					endOfGroup = true;
				}
			}
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
	}

	/**
	 * handle the page-break-before of group. AUTO:
	 * page-break-always-excluding_fist for top level group, none for others.
	 * PAGE_BREAK_BEFORE_ALWAYS: always create page break
	 * PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST: create page-break for all
	 * groups except the first one.
	 * 
	 * @param bandDesign
	 */
	protected void handlePageBreakBeforeOfGroup( )
	{
		try
		{
			boolean needPageBreak = false;
			GroupDesign groupDesign = (GroupDesign) design;
			if ( groupDesign != null )
			{
				String pageBreakBefore = groupDesign.getPageBreakBefore( );
				int groupLevel = groupDesign.getGroupLevel( );
				if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS
						.equals( pageBreakBefore ) )
				{
					needPageBreak = true;
				}
				if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST
						.equals( pageBreakBefore ) )
				{
					if ( rset.getStartingGroupLevel( ) > groupLevel )
					{
						needPageBreak = true;
					}
				}
				if ( needPageBreak )
				{
					content.getStyle( )
							.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
									IStyle.ALWAYS_VALUE );
				}
			}
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
	}

	protected void handlePageBreakInsideOfGroup( )
	{
		GroupDesign groupDesign = (GroupDesign) design;
		if ( groupDesign != null )
		{
			String pageBreakInside = groupDesign.getPageBreakInside( );
			if ( DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID
					.equals( pageBreakInside ) )
			{
				content.getStyle( ).setProperty(
						IStyle.STYLE_PAGE_BREAK_INSIDE, IStyle.AVOID_VALUE );
			}
		}
	}

	protected void handlePageBreakAfterOfGroup( )
	{
		boolean needPageBreak = false;
		GroupDesign groupDesign = (GroupDesign) design;
		if ( groupDesign != null )
		{
			String pageBreakAfter = groupDesign.getPageBreakAfter( );
			if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS
					.equals( pageBreakAfter ) )
			{
				needPageBreak = true;
			}
			if ( needPageBreak )
			{
				content.getStyle( ).setProperty( IStyle.STYLE_PAGE_BREAK_AFTER,
						IStyle.ALWAYS_VALUE );
			}
		}
	}

	protected void handlePageBreakAfter( )
	{
		// if(IStyle.ALWAYS_VALUE.equals( content.getStyle( ).getProperty(
		// IStyle.STYLE_PAGE_BREAK_AFTER) ))
		// {
		// listingExecutor.clearSoftBreak( );
		// }
	}

	protected void handlePageBreakBefore( )
	{
		// if(IStyle.ALWAYS_VALUE.equals( content.getStyle( ).getProperty(
		// IStyle.STYLE_PAGE_BREAK_BEFORE) ))
		// {
		// listingExecutor.clearSoftBreak( );
		// }
	}

	protected void handlePageBreakAfterExclusingLast( )
	{
		try
		{
			GroupDesign groupDesign = (GroupDesign) design;
			if ( groupDesign != null )
			{
				String pageBreakAfter = groupDesign.getPageBreakAfter( );
				int groupLevel = groupDesign.getGroupLevel( );
				if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST
						.equals( pageBreakAfter ) )
				{
					int endGroup = rset.getEndingGroupLevel( );
					if ( endGroup >= groupLevel + 1 )
					{
						setPageBreakBeforeForNextGroup( );
					}
				}
			}
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
	}

	protected void handlePageBreakAfterOfPreviousGroup( )
	{
		if ( parent instanceof GroupExecutor )
		{
			GroupExecutor pGroup = (GroupExecutor) parent;
			if ( pGroup.needPageBreak )
			{
				content.getStyle( ).setProperty(
						IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE );
				pGroup.needPageBreak = false;
			}
		}
		else if ( parent instanceof ListingElementExecutor )
		{
			ListingElementExecutor pList = (ListingElementExecutor) parent;
			if ( pList.needPageBreak )
			{
				content.getStyle( ).setProperty(
						IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE );
				pList.needPageBreak = false;
			}
		}
	}

	protected void setPageBreakBeforeForNextGroup( )
	{
		if ( parent instanceof GroupExecutor )
		{
			GroupExecutor pGroup = (GroupExecutor) parent;
			pGroup.needPageBreak = true;
		}
		else if ( parent instanceof ListingElementExecutor )
		{
			ListingElementExecutor pList = (ListingElementExecutor) parent;
			pList.needPageBreak = true;
		}
	}

	protected void clearDuplicateFlags( )
	{
		GroupDesign groupDesign = (GroupDesign) getDesign( );
		ListingDesign listingDesign = (ListingDesign) listingExecutor
				.getDesign( );
		for ( int i = groupDesign.getGroupLevel( ); i < listingDesign
				.getGroupCount( ); i++ )
		{
			GroupDesign group = listingDesign.getGroup( i );
			SuppressDuplicateUtil.clearDuplicateFlags( group );
		}
		SuppressDuplicateUtil.clearDuplicateFlags( listingDesign.getDetail( ) );
	}
}
