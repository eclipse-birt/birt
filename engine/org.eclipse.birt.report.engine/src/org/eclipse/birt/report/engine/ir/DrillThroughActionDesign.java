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


/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class DrillThroughActionDesign
{
	protected String reportName;
	protected String bookmark;
	
	public DrillThroughActionDesign(String report, String bookmark)
	{
		this.reportName = report;
		this.bookmark = bookmark;
	}
	/**
	 * @return Returns the bookmark.
	 */
	public String getBookmark( )
	{
		return bookmark;
	}
	/**
	 * @param bookmark The bookmark to set.
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
	 * @param reportName The reportName to set.
	 */
	public void setReportName( String reportName )
	{
		this.reportName = reportName;
	}
}
