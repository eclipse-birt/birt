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

package org.eclipse.birt.report.engine.ir;

import java.util.Map;

/**
 * 
 */
public class DrillThroughActionDesign
{
	protected Expression<String> reportName;
	protected Expression<String> bookmark;
	protected Expression<String> bookmarkType;
	protected Expression<String> format;

	protected Map<String, Expression<Object>> parameters;
	protected Map search;


	/**
	 * @return Returns the bookmark.
	 */
	public Expression<String> getBookmark( )
	{
		return bookmark;
	}

	/**
	 * @param bookmark
	 *            The bookmark to set.
	 */
	public void setBookmark( Expression<String> bookmark )
	{
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the reportName.
	 */
	public Expression<String> getReportName( )
	{
		return reportName;
	}

	/**
	 * @param reportName
	 *            The reportName to set.
	 */
	public void setReportName( Expression<String> reportName )
	{
		this.reportName = reportName;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map<String, Expression<Object>> getParameters( )
	{
		return parameters;
	}

	/**
	 * @param parameters
	 *            The parameters to set.
	 */
	public void setParameters( Map<String, Expression<Object>> parameters )
	{
		this.parameters = parameters;
	}

	/**
	 * @return Returns the search.
	 */
	public Map getSearch( )
	{
		return search;
	}

	/**
	 * @param search
	 *            The search to set.
	 */
	public void setSearch( Map search )
	{
		this.search = search;
	}

	
	public Expression<String> getFormat( )
	{
		return format;
	}
	
	public void setFormat( Expression<String> format )
	{
		this.format = format;
	}

	public void setBookmarkType( Expression<String> bookmarkType )
	{
		this.bookmarkType = bookmarkType;
	}

	public Expression<String> getBookmarkType( )
	{
		return bookmarkType;
	}
}