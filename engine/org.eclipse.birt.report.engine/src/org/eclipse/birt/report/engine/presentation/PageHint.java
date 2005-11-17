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

	protected String startId;
	protected long startOffset;
	protected String endId;
	protected long endOffset;

	public PageHint( String startId, long startOffset, String endId,
			long endOffset )
	{
		this.startId = startId;
		this.startOffset = startOffset;
		this.endId = endId;
		this.endOffset = endOffset;
	}

	public String getStartID( )
	{
		return startId;
	}

	public String getEndID( )
	{
		return endId;
	}

	public long getStartOffset( )
	{
		return startOffset;
	}

	public long getEndOffset( )
	{
		return endOffset;
	}

}
