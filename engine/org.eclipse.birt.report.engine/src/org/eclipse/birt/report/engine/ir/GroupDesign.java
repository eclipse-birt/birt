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
 * Group type.
 * 
 * used by ListItem and TableItem.
 * 
 * @version $Revision: 1.7 $ $Date: 2006/03/17 02:18:27 $
 */
public class GroupDesign
{
	/**
	 * group expression
	 */
	protected String name;

	/**
	 * the page break before property 
	 */
	protected String pageBreakBefore;
	
	/**
	 * the page break after property
	 */
	protected String pageBreakAfter;
	
	/**
	 * group hideDetail
	 */
	protected boolean hideDetail;

	/**
	 * @return Returns the name.
	 */
	public String getName( )
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}
	
	/**
	 * @param hide
	 *            The hideDetail to set.
	 */
	public void setHideDetail( boolean hide )
	{
		hideDetail = hide;
	}
	
	/**
	 * @return Returns the hideDetail.
	 */
	public boolean getHideDetail( )
	{
		return hideDetail;
	}
	
	public String getPageBreakBefore()
	{
		return pageBreakBefore;
	}
	
	public void setPageBreakBefore(String pageBreak)
	{
		pageBreakBefore = pageBreak;
	}
	
	public String getPageBreakAfter()
	{
		return pageBreakAfter;
	}
	
	public void setPageBreakAfter(String pageBreak)
	{
		pageBreakAfter = pageBreak;
	}
}
