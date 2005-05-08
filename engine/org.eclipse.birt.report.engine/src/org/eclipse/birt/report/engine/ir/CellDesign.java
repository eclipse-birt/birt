/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class CellDesign extends StyledElementDesign
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
	protected DimensionType width;
	protected DimensionType height;

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

	/**
	 * @return Returns the height.
	 */
	public DimensionType getHeight( )
	{
		return height;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight( DimensionType height )
	{
		this.height = height;
	}

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth( )
	{
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth( DimensionType width )
	{
		this.width = width;
	}
}
