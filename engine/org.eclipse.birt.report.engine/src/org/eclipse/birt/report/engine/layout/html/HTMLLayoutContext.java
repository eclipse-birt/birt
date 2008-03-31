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
import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;
import org.eclipse.birt.report.engine.layout.html.buffer.PageBufferFactory;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class HTMLLayoutContext
{

	protected String masterPage = null;

	protected boolean allowPageBreak = true;

	protected boolean finished;

	// default page number is 1
	protected long pageNumber = 1;

	protected long pageCount = 1;

	protected HTMLReportLayoutEngine engine;

	protected HashMap layoutHint = new HashMap( );

	protected ArrayList pageHints = new ArrayList( );

	protected IPageBuffer bufferMgr;

	protected boolean needLayoutPageContent = true;

	protected String newMasterPage = null;

	protected PageBufferFactory bufferFactory = new PageBufferFactory( this );

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

	public void setPageHint( List hints )
	{
		pageHints.addAll( hints );
	}

	public ArrayList getPageHint( )
	{
		ArrayList hints = new ArrayList( );
		hints.addAll( pageHints );
		return hints;
	}

	/**
	 * whether emitter need to output the display:none or process it in layout
	 * engine. true: output display:none in emitter and do not process it in
	 * layout engine. false: process it in layout engine, not output it in
	 * emitter.
	 */
	protected boolean outputDisplayNone = false;

	public void reset( )
	{
		layoutHint = new HashMap( );
		finished = false;
		allowPageBreak = true;
		masterPage = null;
	}

	public void addLayoutHint( IContent content, boolean finished )
	{
		layoutHint.put( content, new Boolean( finished ) );
	}

	public boolean getLayoutHint( IContent content )
	{
		Object finished = layoutHint.get( content );
		if ( finished != null && finished instanceof Boolean )
		{
			return ( (Boolean) finished ).booleanValue( );
		}
		return true;
	}

	public void removeLayoutHint( )
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

	protected ArrayList hints = new ArrayList( );
	protected ArrayList currentHints = new ArrayList( );

	public List getUnresolvedRowHints( )
	{
		return hints;
	}

	protected ArrayList columnHints = new ArrayList( );

	public List getTableColumnHints( )
	{
		return columnHints;
	}

	public void addTableColumnHints( List hints )
	{
		columnHints.addAll( hints );
	}

	public UnresolvedRowHint getUnresolvedRowHint( ITableContent table )
	{
		if ( hints.size( ) > 0 )
		{
			String idStr = table.getInstanceID( ).toUniqueString( );
			Iterator iter = hints.iterator( );
			while ( iter.hasNext( ) )
			{
				UnresolvedRowHint rowHint = (UnresolvedRowHint) iter.next( );
				if ( idStr.equals( rowHint.getTableId( ) ) )
				{
					return rowHint;
				}
			}
		}
		return null;
	}

	public void addUnresolvedRowHint( UnresolvedRowHint hint )
	{
		currentHints.add( hint );
	}

	public void clearPageHint( )
	{
		columnHints.clear( );
		pageHints.clear( );
		hints.clear( );
		hints.addAll( currentHints );
		currentHints.clear( );
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

	public void setLayoutPageHint( IPageHint pageHint )
	{
		if ( pageHint != null )
		{
			pageNumber = pageHint.getPageNumber( );
			masterPage = pageHint.getMasterPage( );
			int count = pageHint.getUnresolvedRowCount( );
			for ( int i = 0; i < count; i++ )
			{
				hints.add( pageHint.getUnresolvedRowHint( i ) );
			}
			count = pageHint.getTableColumnHintCount( );
			for ( int i = 0; i < count; i++ )
			{
				columnHints.add( pageHint.getTableColumnHint( i ) );
			}
		}
	}

	public TableColumnHint getTableColumnHint( String tableId )
	{
		if ( columnHints.size( ) > 0 )
		{
			Iterator iter = columnHints.iterator( );
			while ( iter.hasNext( ) )
			{
				TableColumnHint hint = (TableColumnHint) iter.next( );
				if ( tableId.equals( hint.getTableId( ) ) )
				{
					return hint;
				}
			}
		}
		return null;
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
