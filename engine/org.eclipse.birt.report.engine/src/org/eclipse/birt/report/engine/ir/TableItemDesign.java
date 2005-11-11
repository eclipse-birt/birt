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
 * Tabel item.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/05/08 06:59:45 $
 */
public class TableItemDesign extends ListingDesign
{

	/**
	 * table caption
	 */
	protected String captionKey;
	/**
	 * table caption resource key
	 */
	protected String caption;

	/**
	 * column defined
	 */
	protected ArrayList columns = new ArrayList( );
	/**
	 * header band
	 */
	protected TableBandDesign header;

	/**
	 * detail band
	 */
	protected TableBandDesign detail;
	/**
	 * footer band
	 */
	protected TableBandDesign footer;

	protected boolean repeatHeader;

	/**
	 * 
	 * @return
	 */
	public boolean getRepeatHeader()
	{
		return repeatHeader;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.ir.ReportItem#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public void accept( IReportItemVisitor visitor, Object value )
	{
		visitor.visitTableItem( this, value );
	}

	/**
	 * @return Returns the repeatHeader.
	 */
	public boolean isRepeatHeader( )
	{
		return repeatHeader;
	}

	/**
	 * @param repeatHeader
	 *            The repeatHeader to set.
	 */
	public void setRepeatHeader( boolean repeatHeader )
	{
		this.repeatHeader = repeatHeader;
	}

	/**
	 * add column into the column define.
	 * 
	 * @param column
	 *            column to be added.
	 */
	public void addColumn( ColumnDesign column )
	{
		assert ( column != null );
		columns.add( column );
	}

	/**
	 * get column count.
	 * 
	 * @return count of the column.
	 */
	public int getColumnCount( )
	{
		return columns.size( );
	}

	/**
	 * get column defines.
	 * 
	 * @param index
	 *            index of the column.
	 * @return column define.
	 */
	public ColumnDesign getColumn( int index )
	{
		assert ( index >= 0 && index < columns.size( ) );
		return (ColumnDesign) columns.get( index );
	}

	/**
	 * @return Returns the detail.
	 */
	public TableBandDesign getDetail( )
	{
		return detail;
	}

	/**
	 * @param detail
	 *            The detail to set.
	 */
	public void setDetail( TableBandDesign detail )
	{
		this.detail = detail;
	}

	/**
	 * @return Returns the footer.
	 */
	public TableBandDesign getFooter( )
	{
		return footer;
	}

	/**
	 * @param footer
	 *            The footer to set.
	 */
	public void setFooter( TableBandDesign footer )
	{
		this.footer = footer;
	}

	public TableGroupDesign getGroup( int index )
	{
		assert ( index >= 0 && index < groups.size( ) );
		return (TableGroupDesign) this.groups.get( index );
	}

	public void addGroup( TableGroupDesign group )
	{
		assert ( group != null );
		this.groups.add( group );
	}

	/**
	 * @return Returns the header.
	 */
	public TableBandDesign getHeader( )
	{
		return header;
	}

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeader( TableBandDesign header )
	{
		this.header = header;
	}

	/**
	 * set tabel caption.
	 * 
	 * @param captionKey
	 *            resource key
	 * @param caption
	 *            caption
	 */
	public void setCaption( String captionKey, String caption )
	{
		this.captionKey = captionKey;
		this.caption = caption;
	}

	/**
	 * @return Returns the caption.
	 */
	public String getCaption( )
	{
		return caption;
	}

	/**
	 * @return Returns the captionKey.
	 */
	public String getCaptionKey( )
	{
		return captionKey;
	}
}
