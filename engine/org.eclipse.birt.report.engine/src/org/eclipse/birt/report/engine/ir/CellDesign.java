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
 * Cell type
 * 
 * Cell type is used by Row, which is the base element of the table item and
 * grid item.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:41 $
 */
public class CellDesign extends ReportItemDesign
{

	/**
	 * column id.
	 */
	protected int column = -1;
	/**
	 * column span
	 */
	protected int colSpan = 1;
	/**
	 * row span
	 */
	protected int rowSpan = 1;
	/**
	 * describe the drop.
	 */
	protected String drop;
	/**
	 * content in this cell
	 */
	protected ArrayList contents = new ArrayList( );

	/**
	 * @return Returns the colSpan.
	 */
	public int getColSpan( )
	{
		return colSpan;
	}

	/**
	 * @param colSpan
	 *            The colSpan to set.
	 */
	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}

	/**
	 * @return Returns the column.
	 */
	public int getColumn( )
	{
		return column;
	}

	/**
	 * @param column
	 *            The column to set.
	 */
	public void setColumn( int column )
	{
		this.column = column;
	}

	/**
	 * @return count of the content items.
	 */
	public int getContentCount( )
	{
		return this.contents.size( );
	}

	/**
	 * get the content of index.
	 * 
	 * @param index
	 *            content index
	 * @return Returns the content.
	 */
	public ReportItemDesign getContent( int index )
	{
		assert index >= 0 && index < this.contents.size( );
		return (ReportItemDesign) this.contents.get( index );
	}

	/**
	 * add content into the cell.
	 * 
	 * @param content
	 *            The content to set.
	 */
	public void addContent( ReportItemDesign content )
	{
		this.contents.add( content );
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		return rowSpan;
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	/**
	 * @return Returns the drop.
	 */
	public String getDrop( )
	{
		return drop;
	}

	/**
	 * @param drop
	 *            The drop to set.
	 */
	public void setDrop( String drop )
	{
		this.drop = drop;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.IReportItemVisitor)
	 */
	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitCell(this, value);
	}
}
