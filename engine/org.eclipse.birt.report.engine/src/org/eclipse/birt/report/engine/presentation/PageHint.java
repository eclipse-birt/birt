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

import java.io.Serializable;

public class PageHint implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7666468796696037741L;

	protected long pageNumber;
	protected long pageOffset;
	protected long pageStart;
	protected long pageEnd;

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

}
