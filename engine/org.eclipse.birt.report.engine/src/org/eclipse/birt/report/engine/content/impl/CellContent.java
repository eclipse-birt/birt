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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IReportContentVisitor;
import org.eclipse.birt.report.engine.content.IReportElementContent;
import org.eclipse.birt.report.engine.ir.CellDesign;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:59:46 $
 */
public class CellContent extends StyledElementContent implements ICellContent
{

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

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	public CellContent( CellDesign item, IReportElementContent parent )
	{
		super( item, parent );
		this.rowSpan = item.getRowSpan( );
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		if (rowSpan == -1)
		{
			return ( (CellDesign) designReference ).getRowSpan( );
		}
		return this.rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		if ( colSpan == -1 )
		{
			return ( (CellDesign) designReference ).getColSpan( );
		}
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		if (column == -1)
		{
			return ( (CellDesign) designReference ).getColumn( );
		}
		return column;
	}

	/**
	 * 
	 * @return drop type
	 */
	public String getDrop( )
	{
		return ( (CellDesign) designReference ).getDrop( );
	}

	public void accept( IReportContentVisitor visitor )
	{
		visitor.visitCellContent( this );
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
	
	public void setColumn(int column)
	{
		this.column = column;
	}
}