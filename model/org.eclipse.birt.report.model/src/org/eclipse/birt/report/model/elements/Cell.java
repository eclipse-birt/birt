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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a cell element. Each grid row or table row contains
 * some number of cells. Cell is a point at which a row and column intersect. A
 * cell can span rows and columns. A cell can span multiple columns. The design
 * need not specify a cell for each column; Columns without cells are presumed
 * empty. Use the {@link org.eclipse.birt.report.model.api.CellHandle}class to
 * change the properties.
 *  
 */

public class Cell extends StyledElement
{

	/**
	 * Name of the property that gives the column in which the cell starts.
	 * Columns are numbered from 1.
	 */

	public static final String COLUMN_PROP = "column"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the number of columns that this cell
	 * spans. Defaults to 1, meaning that the cell appears in only one column.
	 */

	public static final String COL_SPAN_PROP = "colSpan"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the number of rows that this cell spans.
	 * Defaults to 1, meaning the cell appears in only one row. The row span is
	 * used to create drop content within a table. The special value of -1 means
	 * that the cell spans all rows for this particular table or group.
	 */

	public static final String ROW_SPAN_PROP = "rowSpan"; //$NON-NLS-1$

	/**
	 * Name of the drop property that gives the drop options for cells. Controls
	 * how cells in one row overlap subsequent rows: None, detail or all.
	 */

	public static final String DROP_PROP = "drop"; //$NON-NLS-1$ 

	/**
	 * Name of the height property that gives the height of the cell.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$ 

	/**
	 * Name of the width property that gives the width of the cell.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$ 

	/**
	 * Identifier of the slot that holds the page decoration.
	 */

	public static final int CONTENT_SLOT = 0;

	/**
	 * Holds the report items that reside directly on the cell.
	 */

	protected ContainerSlot content = new MultiElementSlot( );

	/**
	 * Default Constructor.
	 */

	public Cell( )
	{
	}

	/**
	 * Makes a clone of this cell element. The cloned cell contains the cloned
	 * content which was in the original cell if any.
	 * 
	 * @return the cloned cell.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		Cell cell = (Cell) super.clone( );
		cell.content = content.copy( cell, CONTENT_SLOT );
		return cell;
	}

	/**
	 * Returns the slot in this cell defined by the slot ID.
	 * 
	 * @param slot
	 *            the slot ID
	 * 
	 * @return the retrieved slot.
	 * 
	 *  
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert ( slot == CONTENT_SLOT );
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitCell( this );
	}

	/**
	 * Returns the name of this cell element. The name will be the predefined
	 * name for this element.
	 * 
	 * @return the cell element's name.
	 *  
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.CELL_ELEMENT;
	}

	/**
	 * Returns the corresponding handle to this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle of this element
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design of the cell
	 * 
	 * @return an API handle for this element.
	 */

	public CellHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new CellHandle( design, this );
		}
		return (CellHandle) handle;
	}

	/**
	 * Returns the number of columns spanned by this cell.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns spanned by this cell
	 */

	public int getColSpan( ReportDesign design )
	{
		return getIntProperty( design, COL_SPAN_PROP );
	}

	/**
	 * Returns the number of rows spanned by this cell.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of rows spanned by this cell
	 */

	public int getRowSpan( ReportDesign design )
	{
		return getIntProperty( design, ROW_SPAN_PROP );
	}

	/**
	 * Returns the column position.
	 * 
	 * @param design
	 *            the report design
	 * @return the column position, or 0 if the columns is to occupy the next
	 *         available column position.
	 */

	public int getColumn( ReportDesign design )
	{
		return getIntProperty( design, COLUMN_PROP );
	}

	/**
	 * Gets a property value given its definition. If <code>prop</code> is a
	 * style property definition, also check style values defined on the Table
	 * columns.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyFromElement(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getPropertyFromElement( ReportDesign design,
			ElementPropertyDefn prop )
	{
		// Get property from cell itself.

		Object value = super.getPropertyFromElement( design, prop );
		if ( value != null )
			return value;

		if ( !prop.canInherit( ) || !prop.isStyleProperty( ) )
			return null; 

		// Get property from the container of this cell. If the container
		// has column, get property from column.

		DesignElement e = getContainer( );
		while ( e != null )
		{
			value = e.getPropertyFromElement( design, prop );
			if ( value != null )
				return value;

			// check property values on the columns.

			if ( e.getContainer( ) instanceof TableItem
					|| e.getContainer( ) instanceof GridItem )
				return getColumnProperty( design, e.getContainer( ), this, prop );

			e = e.getContainer( );
		}

		return null;
	}

	/**
	 * Gets a property value on the container column with the given definition.
	 * If <code>prop</code> is a style property definition, also check style
	 * values defined on the Table/Grid columns.
	 * 
	 * @param design
	 *            the report design
	 * @param container
	 *            the container, must be Table or Grid
	 * @param cell
	 *            the cell on which the property value to find
	 * @param prop
	 *            the property definition
	 * @return the property value
	 */

	private Object getColumnProperty( ReportDesign design,
			DesignElement container, Cell cell, ElementPropertyDefn prop )
	{
		Object value = null;
		if ( container instanceof TableItem )
		{
			TableItem table = (TableItem) container;
			value = table.getPropertyFromColumn( design, cell, prop );
		}

		if ( container instanceof GridItem )
		{
			GridItem grid = (GridItem) container;
			value = grid.getPropertyFromColumn( design, cell, prop );
		}

		return value;
	}
}