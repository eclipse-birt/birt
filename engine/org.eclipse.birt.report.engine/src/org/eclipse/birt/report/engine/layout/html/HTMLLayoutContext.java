/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class HTMLLayoutContext
{

	protected String masterPage = null;

	protected boolean allowPageBreak = true;

	protected boolean pageEmpty = true;

	protected boolean finished;

	protected long pageNumber;

	protected HTMLReportLayoutEngine engine;
	
	protected HashMap layoutHint = new HashMap();
	
	public void reset()
	{
		pageEmpty = true;
		layoutHint = new HashMap();
		finished = false;
		allowPageBreak = true;
		masterPage = null;
	}
	
	public void addLayoutHint(IContent content, boolean finished)
	{
		layoutHint.put( content, new Boolean(finished) );
	}
	
	public boolean getLayoutHint(IContent content)
	{
		Object finished = layoutHint.get( content );
		if(finished!=null && finished instanceof Boolean)
		{
			return ((Boolean)finished).booleanValue( );
		}
		return true;
	}
	
	public void removeLayoutHint()
	{
		layoutHint.clear( );
	}

	public String getMasterPage( )
	{
		return masterPage;
	}

	public void setMasterPage( String masterPage )
	{
		this.masterPage = masterPage;
	}

	public HTMLLayoutContext( HTMLReportLayoutEngine engine )
	{
		this.engine = engine;
	}

	public boolean allowPageBreak( )
	{
		return this.allowPageBreak;
	}

	public void setAllowPageBreak( boolean allowPageBreak )
	{
		this.allowPageBreak = allowPageBreak;
	}

	public boolean isPageEmpty( )
	{
		return pageEmpty;
	}

	public void setPageEmpty( boolean pageEmpty )
	{
		this.pageEmpty = pageEmpty;
	}

	public void setFinish( boolean finished )
	{
		this.finished = finished;
	}

	public boolean isFinished( )
	{
		return finished;
	}
	
	boolean cancelFlag = false;
	void setCancelFlag(boolean flag)
	{
		cancelFlag = flag;
	}
	
	public boolean getCancelFlag()
	{
		return cancelFlag;
	}

	boolean skipPageHint = false;
	ArrayList pageHint = new ArrayList( );
	IContent startContent = null;
	IContent currentContent = null;

	void setSkipPageHint( boolean skip )
	{
		skipPageHint = skip;
	}

	boolean getSkipPageHint( )
	{
		return skipPageHint;
	}

	public void startContent( IContent content )
	{
		if ( !skipPageHint )
		{
			if ( startContent == null )
			{
				startContent = content;
			}
			currentContent = content;
		}
	}

	public void continueContent( IContent content )
	{
		if ( !skipPageHint )
		{
			if ( startContent != null )
			{
				pageHint.add( new IContent[]{startContent, currentContent} );
				startContent = null;
				currentContent = null;
			}
		}
	}

	public void endContent( IContent content )
	{
	}

	public void endContentWithPageBreak( IContent content )
	{
		if ( !skipPageHint )
		{
			if ( startContent != null )
			{
				pageHint.add( new IContent[]{startContent, currentContent} );
				startContent = null;
				currentContent = null;
			}
		}
	}

	public void skipContent(IContent content)
	{
		if ( !skipPageHint )
		{
			if ( startContent != null )
			{
				pageHint.add( new IContent[]{startContent, currentContent} );
				startContent = null;
				currentContent = null;
			}
		}
	}
	
	public ArrayList getPageHint( )
	{
		ArrayList hint = new ArrayList( );
		hint.addAll( pageHint );
		if ( startContent != null )
		{
			assert currentContent != null;
			hint.add( new IContent[]{startContent, currentContent} );
		}
		return hint;
	}
	
	protected ArrayList hints = new ArrayList();
	protected ArrayList currentHints = new ArrayList();
	
	public List getUnresolvedRowHints()
	{
		return hints;
	}
	
	public UnresolvedRowHint getUnresolvedRowHint(ITableContent table)
	{
		if(hints.size( )>0)
		{
			String idStr = table.getInstanceID( ).toString( );
			Iterator iter = hints.iterator( );
			while(iter.hasNext())
			{
				UnresolvedRowHint rowHint = (UnresolvedRowHint) iter.next();
				if(idStr.equals( rowHint.getTableId( ).toString( ) ))
				{
					return rowHint;
				}
			}
		}
		return null;
	}
	
	public void addUnresolvedRowHint(UnresolvedRowHint hint)
	{
		currentHints.add( hint );
	}
	

	public void clearPageHint( )
	{
		pageHint.clear( );
		hints.clear( );
		hints.addAll( currentHints );
		currentHints.clear();
		startContent = null;
		currentContent = null;
	}

	/**
	 * @return the pageNumber
	 */
	public long getPageNumber( )
	{
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber( long pageNumber )
	{
		this.pageNumber = pageNumber;
	}
	
	public void setLayoutPageHint(IPageHint pageHint)
	{
		if(pageHint!=null)
		{
			pageNumber = pageHint.getPageNumber( );
			int count = pageHint.getUnresolvedRowCount( );
			for(int i=0; i<count; i++)
			{
				hints.add(  pageHint.getUnresolvedRowHint( i ) );
			}
		}
	}

}
