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
 * List Item IR. Listing is a base element in report design. it has a query,
 * many groups (corresponds to the query), header, footer and detail.
 * 
 * In creating, a listing will be replace by one header, one footer, several
 * details (surround by groups, each row in dataset will create a detail).
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class ListItemDesign extends ListingDesign
{

	/**
	 * listing header
	 */
	protected ListBandDesign header = new ListBandDesign( );
	/**
	 * groups. which corresponds to the group in query. which is created at
	 * begin & end of each group.
	 */
	protected ArrayList groups = new ArrayList( );
	/**
	 * detail, is created in report instance for each row in the data set.
	 */
	protected ListBandDesign detail = new ListBandDesign( );

	/**
	 * listing header, will be created in report instance at the end of list
	 */
	protected ListBandDesign footer = new ListBandDesign( );

	/**
	 * default constructor.
	 */
	public ListItemDesign( )
	{
	}

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
	 * get group at index.
	 * 
	 * @param index
	 *            group index
	 * @return group at index
	 */
	public ListGroupDesign getGroup( int index )
	{
		return (ListGroupDesign) groups.get( index );
	}

	/**
	 * append a group into this listing. the group will be appended at the end
	 * of this listing.
	 * 
	 * @param group
	 *            group to be added
	 */
	public void addGroup( ListGroupDesign group )
	{
		this.groups.add( group );
	}

	/**
	 * @return Returns the detail.
	 */
	public ListBandDesign getDetail( )
	{
		return detail;
	}

	/**
	 * @param detail
	 *            The detail to set.
	 */
	public void setDetail( ListBandDesign detail )
	{
		this.detail = detail;
	}

	/**
	 * @return Returns the footer.
	 */
	public ListBandDesign getFooter( )
	{
		return footer;
	}

	/**
	 * @param footer
	 *            The footer to set.
	 */
	public void setFooter( ListBandDesign footer )
	{
		this.footer = footer;
	}

	/**
	 * @return Returns the header.
	 */
	public ListBandDesign getHeader( )
	{
		return header;
	}

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeader( ListBandDesign header )
	{
		this.header = header;
	}

	public void accept( ReportItemVisitor visitor )
	{
		visitor.visitListItem( this );
	}
}
