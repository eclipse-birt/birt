/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.presentation;

import java.util.ArrayList;
import java.util.Collection;

public class PageHint implements IPageHint
{

	ArrayList sections = new ArrayList( );
	protected long pageNumber;
	protected long offset;
	ArrayList hints = new ArrayList();
	protected String masterPage;

	public PageHint( )
	{
		pageNumber = 0;
		offset = -1;
	}
	
	public PageHint( long pageNumber, long pageOffset  )
	{
		this.pageNumber = pageNumber;
		offset = pageOffset;
	}

	/**
	 * @param pageNumber
	 * @param pageOffset
	 * @param pageStart
	 * @param pageEnd
	 */
	public PageHint( long pageNumber, long pageOffset, long pageStart,
			long pageEnd )
	{
		this.pageNumber = pageNumber;
		offset = pageOffset;
		addSection( pageStart, pageEnd );
	}
	
	/**
	 * @param pageNumber
	 * @param maserPage
	 */
	public PageHint( long pageNumber, String masterPage  )
	{
		this.pageNumber = pageNumber;
		this.masterPage = masterPage;
	}

	/**
	 * @param pageNumber
	 * @param maserPage
	 * @param pageStart
	 * @param pageEnd
	 */
	public PageHint( long pageNumber, String maserPage, long pageStart,
			long pageEnd )
	{
		this.pageNumber = pageNumber;
		this.masterPage = masterPage;
		addSection( pageStart, pageEnd );
	}


	/**
	 * @return Returns the pageNumber.
	 */
	public long getPageNumber( )
	{
		return pageNumber;
	}

	public int getSectionCount( )
	{
		return sections.size( );
	}

	public long getOffset( )
	{
		return offset;
	}

	public long getSectionStart( int i )
	{
		PageSection section = (PageSection) sections.get( i );
		return section.startOffset;
	}

	public long getSectionEnd( int i )
	{
		PageSection section = (PageSection) sections.get( i );
		return section.endOffset;
	}

	public void addSection( long start, long end )
	{
		PageSection section = new PageSection( );
		section.startOffset = start;
		section.endOffset = end;
		sections.add( section );
	}

	public PageSection getSection( int i )
	{
		return (PageSection) sections.get( i );
	}

	public void addSection( PageSection section )
	{
		sections.add( section );
	}

	public void addUnresolvedRowHints( Collection hints )
	{
		this.hints.addAll( hints );
	}
	
	public int getUnresolvedRowCount( )
	{
		return hints.size( );
	}

	public UnresolvedRowHint getUnresolvedRowHint( int index )
	{
		assert index >= 0 && index < hints.size( );
		return (UnresolvedRowHint)hints.get( index );
	}
	
	public void addUnresolvedRowHint(UnresolvedRowHint hint)
	{
		hints.add( hint );
	}
	
	public void setMasterPage( String masterPage )
	{
		this.masterPage = masterPage;
	}

	public String getMasterPage( )
	{
		return this.masterPage;
	}
	
	public void setOffset(long offset)
	{
		this.offset = offset;
	}

}
