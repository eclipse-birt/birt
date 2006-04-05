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

package org.eclipse.birt.report.engine.presentation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.core.util.IOUtil;

public class PageHint implements IPageHint
{

	static private class PageSection
	{

		PageSection( long start, long end )
		{
			this.start = start;
			this.end = end;
		}
		long start;
		long end;
	}

	ArrayList sections = new ArrayList( );
	protected long pageNumber;
	protected long offset;

	public PageHint( )
	{
		pageNumber = 0;
		offset = -1;
	}

	public PageHint( long pageNumber, long pageOffset, long pageStart,
			long pageEnd )
	{
		this.pageNumber = pageNumber;
		offset = pageOffset;
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
		assert i >= 0 && i < sections.size( );
		PageSection section = (PageSection) sections.get( i );
		return section.start;
	}

	public long getSectionEnd( int i )
	{
		assert i >= 0 && i < sections.size( );
		PageSection section = (PageSection) sections.get( i );
		return section.end;
	}

	public void addSection( long start, long end )
	{
		PageSection section = new PageSection( start, end );
		sections.add( section );
	}

	public void writeObject( DataOutputStream out ) throws IOException
	{
		IOUtil.writeLong( out, pageNumber );
		IOUtil.writeLong( out, offset );
		int sectionCount = sections.size( );
		IOUtil.writeInt( out, sectionCount );
		for ( int i = 0; i < sectionCount; i++ )
		{
			PageSection section = (PageSection) sections.get( i );
			IOUtil.writeLong( out, section.start );
			IOUtil.writeLong( out, section.end );
		}
	}

	public void readObject( DataInputStream in ) throws IOException
	{
		pageNumber = IOUtil.readLong( in );
		offset = IOUtil.readLong( in );
		int sectionCount = IOUtil.readInt( in );
		for ( int i = 0; i < sectionCount; i++ )
		{
			long start = IOUtil.readLong( in );
			long end = IOUtil.readLong( in );
			addSection( start, end );
		}
	}

}
