/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.report.engine.content.IContent;

public class HTMLLayoutContext
{

	protected String masterPage = null;

	protected boolean allowPageBreak = true;

	protected boolean pageEmpty = true;

	protected boolean finished;

	protected long pageNumber;

	protected HTMLReportLayoutEngine engine;

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
	
	boolean getCancelFlag()
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

	public void clearPageHint( )
	{
		pageHint.clear( );
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

}
