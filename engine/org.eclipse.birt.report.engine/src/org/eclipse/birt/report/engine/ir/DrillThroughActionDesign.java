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
 * @version $Revision: 1.7 $ $Date: 2005/12/20 06:48:34 $
 */
public class DrillThroughActionDesign
{
	protected String reportName;
	protected String bookmark;
	protected String format;

	protected Map parameters;
	protected Map search;


	/**
	 * @return Returns the bookmark.
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	/**
	 * @param bookmark
	 *            The bookmark to set.
	 */
	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName( )
	{
		return reportName;
	}

	/**
	 * @param reportName
	 *            The reportName to set.
	 */
	public void setReportName( String reportName )
	{
		this.reportName = reportName;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map getParameters( )
	{
		return parameters;
	}

	/**
	 * @param parameters
	 *            The parameters to set.
	 */
	public void setParameters( Map parameters )
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

	
	public String getFormat( )
	{
		return format;
	}

	
	public void setFormat( String format )
	{
		this.format = format;
	}
}