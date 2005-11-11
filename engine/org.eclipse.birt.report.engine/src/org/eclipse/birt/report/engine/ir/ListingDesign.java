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

import java.util.ArrayList;


/**
 * 
 * @version $Revision: 1.6 $ $Date: 2005/05/08 06:59:45 $
 */
abstract public class ListingDesign extends ReportItemDesign
{
	/**
	 * the page break interval. it will create a softpage break
	 * for each page break interval rows.
	 */
	protected int pageBreakInterval = -1;
	/**
	 * groups. which corresponds to the group in query. which is created at
	 * begin & end of each group.
	 */
	protected ArrayList groups = new ArrayList( );

	/**
	 * array list store the IFilterDefn
	 */
	protected ArrayList filters = new ArrayList();
	/**
	 * array list store the ISortDefn
	 */
	protected ArrayList sorts = new ArrayList();
	/**
	 * on start script
	 */
	protected String onStart;
	/**
	 * on row script
	 */
	protected String onRow;
	/**
	 * on finish script
	 */
	protected String onFinish;
	
	/**
	 * get all the groups in this listing.
	 * 
	 * @return collection of groups.
	 */
	public ArrayList getGroups( )
	{
		return this.groups;
	}

	/**
	 * get group count
	 * 
	 * @return group count
	 */
	public int getGroupCount( )
	{
		return this.groups.size( );
	}

	
	/**
	 * @return Returns the filters.
	 */
	public ArrayList getFilters( )
	{
		return filters;
	}
	/**
	 * @return Returns the sorts.
	 */
	public ArrayList getSorts( )
	{
		return sorts;
	}
	
	/**
	 * @return Returns the onFinish.
	 */
	public String getOnFinish( )
	{
		return onFinish;
	}
	/**
	 * @param onFinish The onFinish to set.
	 */
	public void setOnFinish( String onFinish )
	{
		this.onFinish = onFinish;
	}
	/**
	 * @return Returns the onRow.
	 */
	public String getOnRow( )
	{
		return onRow;
	}
	/**
	 * @param onRow The onRow to set.
	 */
	public void setOnRow( String onRow )
	{
		this.onRow = onRow;
	}
	/**
	 * @return Returns the onStart.
	 */
	public String getOnStart( )
	{
		return onStart;
	}
	/**
	 * @param onStart The onStart to set.
	 */
	public void setOnStart( String onStart )
	{
		this.onStart = onStart;
	}
	
	public void setPageBreakInterval(int interval)
	{
		this.pageBreakInterval = interval;
	}
	
	public int getPageBreakInterval()
	{
		return pageBreakInterval;
	}
}
