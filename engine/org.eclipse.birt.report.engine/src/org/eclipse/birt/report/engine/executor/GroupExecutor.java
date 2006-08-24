
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

abstract public class GroupExecutor extends ReportItemExecutor
{
	boolean endOfGroup;
	boolean hiddenDetail;
	boolean needPageBreak;

	ListingElementExecutor listingExecutor;

	protected GroupExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	public void reset( )
	{
		super.reset( );
		endOfGroup = false;
		hiddenDetail = false;
		needPageBreak = false;
		listingExecutor = null;
	}
	
	void setLisingExecutor(ListingElementExecutor executor)
	{
		listingExecutor = executor;
		rset = listingExecutor.rset;
	}
	
	public boolean hasNextChild()
	{
		if ( currentElement < totalElements )
		{
			return true;
		}
		if ( endOfGroup )
		{
			return false;
		}
		
		//FIXME: is it right? (hiden detail)
		while ( !endOfGroup )
		{
			IResultSet rset = listingExecutor.getResultSet( );
			GroupDesign groupDesign = (GroupDesign)getDesign();
			int endGroup = rset.getEndingGroupLevel( );
			int groupLevel = groupDesign.getGroupLevel( ) + 1;
			if (endGroup <= groupLevel)
			{
				totalElements = 0;
				currentElement = 0;
				BandDesign footer = groupDesign.getFooter();
				if (footer != null)
				{
					executableElements[totalElements++] = footer;
				}
				endOfGroup = true;
				return currentElement < totalElements ;
			}
			if ( rset.next( ) )
			{
				collectExecutableElements( );
				if ( currentElement < totalElements )
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
			
			if (nextExecutor instanceof GroupExecutor )
			{
				GroupExecutor groupExecutor = (GroupExecutor)nextExecutor;
				groupExecutor.setLisingExecutor( listingExecutor );
			}

			return nextExecutor;
		}
		return null;
	}
	
	//bands to be execute in current row.
	ReportItemDesign[] executableElements = new ReportItemDesign[3];
	//total bands in the executabelBands
	int totalElements;
	//band to be executed
	int currentElement;
	
	protected void prepareToExecuteChildren()
	{
		//prepare the bands to be executed. 
		collectExecutableElements();
	}
	
	void collectExecutableElements()
	{
		currentElement = 0;
		totalElements = 0;
		endOfGroup = false;
		
		ListingDesign listingDesign = (ListingDesign) listingExecutor
				.getDesign( );
		IResultSet rset = listingExecutor.getResultSet( );
		GroupDesign groupDesign = (GroupDesign)getDesign();
		int groupCount = listingDesign.getGroupCount();
		//compare with the start group, the start group 
		//start with 0 --> listing
		//1 --> first group (0)
		int groupLevel = groupDesign.getGroupLevel( ) + 1;
		int startGroup = rset.getStartingGroupLevel( );
		hiddenDetail = groupDesign.getHideDetail( );
		if (startGroup <= groupLevel)
		{
			//this is the first record
			BandDesign header = groupDesign.getHeader( );
			if (header != null)
			{
				executableElements[totalElements++] = header;
			}
		}
		if ( !hiddenDetail )
		{
			if (groupCount > groupLevel)
			{
				executableElements[totalElements++] = listingDesign.getGroup( groupLevel );
			}
			else 
			{
				BandDesign detail = listingDesign.getDetail( );
				if (detail != null)
				{
					executableElements[totalElements++] = listingDesign.getDetail( );
				}
			}
		}
		int endGroup = rset.getEndingGroupLevel( );
		if (endGroup <= groupLevel )
		{
			//this is the last record
			BandDesign footer = groupDesign.getFooter( );
			if (footer != null)
			{
				executableElements[totalElements++] = groupDesign.getFooter( );
			}
			if (endGroup <= groupLevel )
			{
				endOfGroup = true;
			}
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
			if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO
					.equals( pageBreakBefore )
					&& groupLevel == 0 )
			{
				if ( isTopLevelListing( ) )
				{
					int startGroupLevel = rset.getStartingGroupLevel( );
					if ( startGroupLevel > 0 )
					{
						needPageBreak = true;
					}
				}
			}
			if ( needPageBreak )
			{
				content.getStyle( ).setProperty(
						IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE );
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
	
	protected void handlePageBreakAfterExclusingLast()
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
	
	protected void handlePageBreakAfterOfPreviousGroup( )
	{
		if (parent instanceof GroupExecutor)
		{
			GroupExecutor pGroup = (GroupExecutor)parent; 
			if (pGroup.needPageBreak)
			{
				content.getStyle( ).setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
				pGroup.needPageBreak = false;
			}
		}
		else if (parent instanceof ListingElementExecutor)
		{
			ListingElementExecutor pList = (ListingElementExecutor)parent;
			if (pList.needPageBreak)
			{
				content.getStyle( ).setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.ALWAYS_VALUE);
				pList.needPageBreak = false;
			}
		}
	}
	
	protected void setPageBreakBeforeForNextGroup()
	{
		if (parent instanceof GroupExecutor)
		{
			GroupExecutor pGroup = (GroupExecutor)parent; 
			pGroup.needPageBreak = true;
		}
		else if (parent instanceof ListingElementExecutor)
		{
			ListingElementExecutor pList = (ListingElementExecutor)parent;
			pList.needPageBreak = true;
		}
	}
	
	boolean isTopLevelListing( )
	{
		DesignElementHandle listing = listingExecutor.getDesign( ).getHandle( );
		if ( listing != null )
		{
			DesignElementHandle parent = listing.getContainer( );
			while ( parent != null )
			{
				if ( parent instanceof ListingHandle )
				{
					return false;
				}
				parent = parent.getContainer( );
			}
		}
		return true;
	}
}
