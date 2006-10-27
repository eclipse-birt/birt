
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.content.impl;

import java.util.Map;

import org.eclipse.birt.report.engine.content.IDrillThroughAction;


/**
 * 
 */

public class DrillThroughAction implements IDrillThroughAction
{
	/**
	 * report name
	 */
	protected String reportName;

	/**
	 * bookmark string
	 */
	protected String bookmark;
	
	/**
	 * Flag indicating if this is a bookmark. False means this is a TOC.
	 */
	protected boolean isBookmark;

	protected String format;

	/**
	 * parameters and their values for running drillthrough reports
	 */
	protected Map parameterBindings;

	/**
	 * search keys and their values for searching drillthrough reports
	 */
	protected Map searchCriteria;

	/**
	 * the name of a frame where a document is to be opened.
	 */
	protected String target;
	
	public DrillThroughAction( )
	{
		
	}
	
	public DrillThroughAction( String bookmark, boolean isBookmark,
				String reportName, Map parameterBindings, Map searchCriteria,
				String target, String format )
	{
		this.bookmark = bookmark;
		this.isBookmark = isBookmark;
		this.reportName = reportName;
		this.parameterBindings = parameterBindings;
		this.searchCriteria = searchCriteria;
		this.target = target;
		this.format = format;
	}
	
	public String getBookmark( )
	{
		return bookmark;
	}

	public String getFormat( )
	{
		return format;
	}

	public Map getParameterBindings( )
	{
		return parameterBindings;
	}

	public String getReportName( )
	{
		return reportName;
	}

	public Map getSearchCriteria( )
	{
		return searchCriteria;
	}

	public String getTargetWindow( )
	{
		return target;
	}

	public boolean isBookmark( )
	{
		return isBookmark;
	}

	public void setReportName( String reportName )
	{
		this.reportName = reportName;
	}

	public void setBookmark( String bookmark )
	{
		this.isBookmark = true;
		this.bookmark = bookmark;
	}

	public void setBookmarkType( boolean isBookmark )
	{
		this.isBookmark = isBookmark;
	}

	public void setParameterBindings( Map parameterBindings )
	{
		this.parameterBindings = parameterBindings;
	}

	public void setSearchCriteria( Map searchCriteria )
	{
		this.searchCriteria = searchCriteria;
	}

	public void setTargetWindow( String target )
	{
		this.target = target;
	}

	public void setFormat( String format )
	{
		this.format = format;
	}
}
