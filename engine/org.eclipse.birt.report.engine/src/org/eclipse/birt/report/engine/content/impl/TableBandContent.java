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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;

/**
 * 
 * table band content object There are three type: table header, table footer,
 * table body
 * 
 */
public class TableBandContent extends AbstractBandContent
		implements
			ITableBandContent
{
	private boolean lastTableBand = false;
	private boolean firstTableBand = false;
	
	TableBandContent( ITableBandContent band )
	{
		super( band );
	}
	
	TableBandContent( IReportContent report )
	{
		super( report );
	}

	public int getContentType( )
	{
		return TABLE_BAND_CONTENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ReportElementContent#accept(org.eclipse.birt.report.engine.content.ReportContentVisitor)
	 */
	public Object accept( IContentVisitor visitor, Object value )
			throws BirtException
	{
		return visitor.visitTableBand( this, value );

	}
	
	protected IContent cloneContent()
	{
		return new TableBandContent(this);
	}

	public void setLastTableBand( boolean lastTableBand )
	{
		this.lastTableBand = lastTableBand;
	}

	public boolean isLastTableBand( )
	{
		return lastTableBand;
	}
	
	public void setFirstTableBand( boolean firstTableBand )
	{
		this.firstTableBand = firstTableBand;
	}

	public boolean isFirstTableBand( )
	{
		return firstTableBand;
	}
}