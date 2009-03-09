/*******************************************************************************
 * Copyright (c) 2004 , 2008 Actuate Corporation.
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
 */
public class TableItemDesign extends ListingDesign
{
	/**
	 * table caption
	 */
	protected Expression<String> captionKey;
	/**
	 * table caption resource key
	 */
	protected Expression<String> caption;
	
	/**
	 * table summary
	 */
	protected Expression<String> summary;
	

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
	public void setCaption( Expression<String> captionKey, Expression<String> caption )
	{
		this.captionKey = captionKey;
		this.caption = caption;
	}

	/**
	 * @return Returns the caption.
	 */
	public Expression<String> getCaption( )
	{
		return caption;
	}

	/**
	 * @return Returns the captionKey.
	 */
	public Expression<String> getCaptionKey( )
	{
		return captionKey;
	}
	
	/**
	 * set table summary
	 * 
	 * @param summary
	 * 	          summary
	 */
	public void setSummary(Expression<String> summary)
	{
		this.summary = summary;
	}
	
	/**
	 * get table summary
	 * 
	 * @return Return table summary
	 */
	public Expression<String> getSummary()
	{
		return summary;
	}
}
