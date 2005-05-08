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
 * Row used in GridItem and TableItem.
 * 
 * @see GridItemDesign
 * @see TableItemDesign
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
///TODO: RowDesign is not a realy styled element. It only has a style, but has
// no other attributes.
public class RowDesign extends StyledElementDesign
{

	/**
	 * cells in this row.
	 */
	protected ArrayList cells = new ArrayList( );

	/**
	 * row height
	 */
	protected DimensionType height;
	/**
	 * bookmark associated with this row
	 */
	protected Expression bookmark;
	/**
	 * is this row should be visible 
	 */
	protected Expression hideExpr;

	/**
	 * Visibility property.
	 */
	protected VisibilityDesign visibility;
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
	
	/**
	 * @return Returns the bookmark.
	 */
	public Expression getBookmark( )
	{
		return bookmark;
	}
	/**
	 * @param bookmark The bookmark to set.
	 */
	public void setBookmark( Expression bookmark )
	{
		this.bookmark = bookmark;
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
	/**
	 * @return Returns the visibility.
	 */
	public VisibilityDesign getVisibility( )
	{
		return visibility;
	}
	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility( VisibilityDesign visibility )
	{
		this.visibility = visibility;
	}
}
