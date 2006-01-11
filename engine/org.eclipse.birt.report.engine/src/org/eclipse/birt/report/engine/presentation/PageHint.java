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

import org.eclipse.birt.core.util.IOUtil;

public class PageHint
{

	protected long pageNumber;
	protected long pageOffset;
	protected long pageStart;
	protected long pageEnd;

	public PageHint( )
	{
		pageNumber = 0;
		pageOffset = -1;
		pageStart = -1;
		pageEnd = -1;
	}

	public PageHint( long pageNumber, long pageOffset, long pageStart,
			long pageEnd )
	{
		this.pageNumber = pageNumber;
		this.pageOffset = pageOffset;
		this.pageStart = pageStart;
		this.pageEnd = pageEnd;
	}

	/**
	 * @return Returns the pageEnd.
	 */
	public long getPageEnd( )
	{
		return pageEnd;
	}

	/**
	 * @return Returns the pageNumber.
	 */
	public long getPageNumber( )
	{
		return pageNumber;
	}

	/**
	 * @return Returns the pageOffset.
	 */
	public long getPageOffset( )
	{
		return pageOffset;
	}

	/**
	 * @return Returns the pageStart.
	 */
	public long getPageStart( )
	{
		return pageStart;
	}

	public void writeObject( DataOutputStream out ) throws IOException
	{
		IOUtil.writeLong( out, pageNumber );
		IOUtil.writeLong( out, pageOffset );
		IOUtil.writeLong( out, pageStart );
		IOUtil.writeLong( out, pageEnd );
	}

	public void readObject( DataInputStream in ) throws IOException
	{
		pageNumber = IOUtil.readLong( in );
		pageOffset = IOUtil.readLong( in );
		pageStart = IOUtil.readLong( in );
		pageEnd = IOUtil.readLong( in );
	}

}
