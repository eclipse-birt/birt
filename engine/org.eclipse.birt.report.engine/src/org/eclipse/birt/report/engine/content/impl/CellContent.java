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
import org.eclipse.birt.report.engine.ir.CellDesign;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class CellContent extends StyledElementContent implements ICellContent
{

	/**
	 * row span
	 */
	protected int rowSpan = 1;

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	public CellContent( CellDesign item )
	{
		this.designReference = item;
		this.rowSpan = ( (CellDesign) designReference ).getRowSpan( );
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
		return ( (CellDesign) designReference ).getColSpan( );
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		return ( (CellDesign) designReference ).getColumn( );
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

}