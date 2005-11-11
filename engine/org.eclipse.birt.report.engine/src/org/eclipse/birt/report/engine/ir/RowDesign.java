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
 * Row used in GridItem and TableItem.
 * 
 * @see GridItemDesign
 * @see TableItemDesign
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:59:45 $
 */
///TODO: RowDesign is not a realy styled element. It only has a style, but has
// no other attributes.
public class RowDesign extends ReportItemDesign
{

	/**
	 * cells in this row.
	 */
	protected ArrayList cells = new ArrayList( );

	/**
	 * is this row should be visible 
	 */
	protected Expression hideExpr;

	/**
	 * get cell count
	 * 
	 * @return cell count
	 */
	public int getCellCount( )
	{
		return this.cells.size( );
	}

	/**
	 * get Cell
	 * 
	 * @param index
	 *            cell index
	 * @return cell
	 */
	public CellDesign getCell( int index )
	{
		return (CellDesign) this.cells.get( index );
	}

	/**
	 * append cell into the row.
	 * 
	 * @param cell
	 *            cell to be added.
	 */
	public void addCell( CellDesign cell )
	{
		assert ( cell != null );
		this.cells.add( cell );
		/*
		 * if (cell.getColumn() != -1) { for (int i = cells.size(); i <
		 * cell.getColumn(); i++) { this.cells.add(null); }
		 * this.cells.set(cell.getColumn()-1, cell); return; } else {
		 * this.cells.add(cell); }
		 */
	}
	
	public void removeCells()
	{
		this.cells.clear();
	}
	/**
	 * @return Returns the hideExpr.
	 */
	public Expression getHideExpr( )
	{
		return hideExpr;
	}
	/**
	 * @param hideExpr The hideExpr to set.
	 */
	public void setHideExpr( Expression hideExpr )
	{
		this.hideExpr = hideExpr;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.IReportItemVisitor)
	 */
	public void accept( IReportItemVisitor visitor , Object value)
	{
		visitor.visitRow(this, value);
	}
}
