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
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.ir.CellDesign;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:46 $
 */
public class CellContent extends AbstractContent implements ICellContent
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
	 * row id
	 */
	protected int row = -1;

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	public CellContent( ReportContent report)
	{
		super(report);
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		return this.rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		return column;
	}
	
	public int getRow()
	{
		return row;
	}

	public void setDrop( String drop )
	{
		if ( generateBy instanceof CellDesign)
			( (CellDesign)generateBy ).setDrop( drop );
	}

	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitCell( this , value);
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
	
	public void setRow(int row)
	{
		this.row = row;
	}
}