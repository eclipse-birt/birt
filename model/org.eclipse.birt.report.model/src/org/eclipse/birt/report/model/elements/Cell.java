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
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a cell element. Each grid row or table row contains
 * some number of cells. Cell is a point at which a row and column intersect. A
 * cell can span rows and columns. A cell can span multiple columns. The design
 * need not specify a cell for each column; Columns without cells are presumed
 * empty. Use the {@link org.eclipse.birt.report.model.api.CellHandle}class to change
 * the properties.
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
	 * @see org.eclipse.birt.report.model.core.DesignElement#getProperty(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getProperty( ReportDesign design, ElementPropertyDefn prop )
	{

		if ( !prop.isStyleProperty( ) )
			return super.getProperty( design, prop );

		DesignElement e = this;
		Object value;
		Cell cell = this;

		while ( e != null )
		{
			// 1). If we can find the value here, return it.

			value = e.getLocalProperty( design, prop );
			if ( value != null )
				return value;

			// 2). Does the style provide the value of this property ?

			StyleElement style = e.getLocalStyle( );
			if ( style != null )
			{
				value = style.getLocalProperty( design, prop );
				if ( value != null )
					return value;
			}

			// returns if the property can not be inherited.
			
			if (!prop.canInherit())
				return getDefaultValue( design, prop );
			
			// 3). Check if this element predefined style provides
			// the property value

			String selector = e.getDefn( ).getSelector( );
			value = e.getPropertyFromSelector( design, prop, selector );
			if ( value != null )
				return value;

			// 4).Check if the container/slot predefined style provides
			// the property value

			if ( e.getContainer( ) != null )
			{
				// check property values on the Table columns.

				if ( e.getContainer( ) instanceof TableItem )
				{
					TableItem table = (TableItem) e.getContainer( );
					value = table.getPropertyFromColumn( design, cell, prop );
					if ( value != null )
						return value;
				}
				if ( e.getContainer( ) instanceof Cell )
					cell = (Cell) e.getContainer( );

				String[] selectors = e.getContainer( ).getSelectors(
						e.getContainerSlot( ) );
				for ( int i = 0; i < selectors.length; i++ )
				{
					value = e.getPropertyFromSelector( design, prop,
							selectors[i] );
					if ( value != null )
						return value;
				}

			}
			e = e.getContainer( );
		}

		// Still not found. Use the default.

		return getDefaultValue( design, prop );
	}

}
