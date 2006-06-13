
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

abstract public class GroupExecutor extends ReportItemExecutor
{
	boolean needPageBreak;
	boolean endOfGroup;

	ListingElementExecutor listingExecutor;

	protected GroupExecutor( ExecutorManager manager )
	{
		super( manager );
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
		IResultSet rset = listingExecutor.getResultSet( );
		GroupDesign groupDesign = (GroupDesign)getDesign();
		int endGroup = rset.getEndingGroupLevel( );
		int groupLevel = groupDesign.getGroupLevel( ) + 1;
		if (endGroup <= groupLevel)
		{
			totalElements = 0;
			currentElement = 0;
			executableElements[totalElements++] = groupDesign.getFooter();
			endOfGroup = true;
			return true;
		}
		if ( rset.next( ) )
		{
			collectExecutableElements( );
			return true;
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
		if (startGroup <= groupLevel)
		{
			//this is the first record
			executableElements[totalElements++] = groupDesign.getHeader( );
		}
		if (groupCount > groupLevel)
		{
			executableElements[totalElements++] = listingDesign.getGroup( groupLevel );
		}
		else
		{
			executableElements[totalElements++] = listingDesign.getDetail( );
		}
		int endGroup = rset.getEndingGroupLevel( );
		if (endGroup <= groupLevel)
		{
			//this is the last record
			executableElements[totalElements++] = groupDesign.getFooter( );
			if (endGroup <= groupLevel )
			{
				endOfGroup = true;
			}
		}
	}
	
	protected void handlePageBreakOfGroup( )
	{
		handlePageBreakBeforeOfGroup( );
		handlePageBreakAfterOfGroup( );
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
				int startGroupLevel = rset.getStartingGroupLevel( ); 
				if ( startGroupLevel > 0 )
				{
					needPageBreak = true;
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
		GroupDesign groupDesign = (GroupDesign) design;
		if ( groupDesign != null )
		{
			String pageBreakAfter = groupDesign.getPageBreakAfter( );
			int groupLevel = groupDesign.getGroupLevel( );
			if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS
					.equals( pageBreakAfter ) )
			{
				needPageBreak = true;
			}
			if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST
					.equals( pageBreakAfter ) )
			{
				if ( rset.getEndingGroupLevel( ) > groupLevel )
				{
					needPageBreak = true;
				}
			}
			if ( needPageBreak )
			{
				content.getStyle( ).setProperty( IStyle.STYLE_PAGE_BREAK_AFTER,
						IStyle.ALWAYS_VALUE );
			}
		}
	}
}
