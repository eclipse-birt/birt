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

import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;
import org.eclipse.birt.report.engine.layout.html.buffer.PageBufferFactory;

public class HTMLLayoutContext
{

	protected String masterPage = null;

	protected boolean allowPageBreak = true;

	protected boolean finished;

	// default page number is 1
	protected long pageNumber = 1;

	protected long pageCount = 1;

	protected HTMLReportLayoutEngine engine;

	protected HTMLLayoutPageHintManager pageHintMgr = new HTMLLayoutPageHintManager( this );

	protected IPageBuffer bufferMgr;

	protected boolean needLayoutPageContent = true;

	protected String newMasterPage = null;

	protected PageBufferFactory bufferFactory = new PageBufferFactory( this );
	
	protected boolean emptyPage = false;

	/**
	 * The flag to indicate whether the emitter need output the display:none or process it in layout
	 * engine. true: output display:none in emitter and do not process it in
	 * layout engine. false: process it in layout engine, not output it in
	 * emitter.
	 */
	protected boolean outputDisplayNone = false;

	public PageBufferFactory getBufferFactory( )
	{
		return bufferFactory;
	}

	public void setNextMasterPage( String newMasterPage )
	{
		this.newMasterPage = newMasterPage;
	}

	public void initilizePage( )
	{
		if ( newMasterPage != null )
		{
			masterPage = newMasterPage;
			newMasterPage = null;
		}
	}

	public void setLayoutPageContent( boolean needLayoutPageContent )
	{
		this.needLayoutPageContent = needLayoutPageContent;
	}

	public boolean needLayoutPageContent( )
	{
		return needLayoutPageContent;
	}

	public HTMLReportLayoutEngine getLayoutEngine( )
	{
		return engine;
	}

	public void setPageBufferManager( IPageBuffer bufferMgr )
	{
		this.bufferMgr = bufferMgr;
	}

	public IPageBuffer getPageBufferManager( )
	{
		return this.bufferMgr;
	}
	
	public HTMLLayoutPageHintManager getPageHintManager( )
	{
		return pageHintMgr;
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

	public void setFinish( boolean finished )
	{
		this.finished = finished;
	}

	public boolean isFinished( )
	{
		return finished;
	}

	boolean cancelFlag = false;

	void setCancelFlag( boolean flag )
	{
		cancelFlag = flag;
	}

	public boolean getCancelFlag( )
	{
		return cancelFlag;
	}

	public void setEmptyPage(boolean emptyPage)
	{
		this.emptyPage = emptyPage;
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

	public void setOutputDisplayNone( boolean outputDisplayNone )
	{
		this.outputDisplayNone = outputDisplayNone;
	}

	public boolean getOutputDisplayNone( )
	{
		return outputDisplayNone;
	}

	public long getPageCount( )
	{
		return pageCount;
	}

	public void setPageCount( long pageCount )
	{
		this.pageCount = pageCount;
	}
	
}
