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
 * @version $Revision: 1.8 $ $Date: 2005/11/17 16:50:43 $
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.ir.ReportItem#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitTableItem( this, value );
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
