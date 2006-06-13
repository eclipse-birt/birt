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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContentVisitor;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.3 $ $Date: 2006/06/08 12:55:33 $
 */
public class CellContentWrapper extends AbstractContentWrapper implements ICellContent
{

	protected ICellContent cell;
	/**
	 * row span
	 */
	protected int rowSpan = -1;

	/**
	 * col span, if equals to 1, then get it from the design.
	 */
	protected int colSpan = -1;

	/**
	 * column id, if equals to 0, get it from the design
	 */
	protected int column = -1;
	
	protected int row = -1;

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	public CellContentWrapper( ICellContent cell )
	{
		super( cell );
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		if (rowSpan != -1)
		{
			return rowSpan;
		}
		return cell.getRowSpan();
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		if (colSpan != -1)
		{
			return colSpan;
		}
		return cell.getColSpan();
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		if (column != -1)
		{
			return column;
		}
		return cell.getColumn();
	}

	public int getRow( )
	{
		if (row != -1)
		{
			return row;
		}
		return cell.getRow();
	}

	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitCell( this, value );
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}

	public void setColumn( int column )
	{
		this.column = column;
	}

	public boolean isStartOfGroup( )
	{
		return cell.isStartOfGroup( );
	}

	public void setStartOfGroup( boolean isStartOfGroup )
	{
		cell.setStartOfGroup( isStartOfGroup );
	}

	public IColumn getColumnInstance( )
	{
		return cell.getColumnInstance( );
	}
}