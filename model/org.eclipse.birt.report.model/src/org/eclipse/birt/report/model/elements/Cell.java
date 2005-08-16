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
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
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

public class Cell extends StyledElement implements ICellModel
{

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
	 * @param module
	 *            the report design
	 * @return an API handle of this element
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design of the cell
	 * 
	 * @return an API handle for this element.
	 */

	public CellHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new CellHandle( module, this );
		}
		return (CellHandle) handle;
	}

	/**
	 * Returns the number of columns spanned by this cell.
	 * 
	 * @param module
	 *            the module
	 * @return the number of columns spanned by this cell
	 */

	public int getColSpan( Module module )
	{
		return getIntProperty( module, COL_SPAN_PROP );
	}

	/**
	 * Returns the number of rows spanned by this cell.
	 * 
	 * @param module
	 *            the module
	 * @return the number of rows spanned by this cell
	 */

	public int getRowSpan( Module module )
	{
		return getIntProperty( module, ROW_SPAN_PROP );
	}

	/**
	 * Returns the column position.
	 * 
	 * @param module
	 *            the module
	 * @return the column position, or 0 if the columns is to occupy the next
	 *         available column position.
	 */

	public int getColumn( Module module )
	{
		return getIntProperty( module, COLUMN_PROP );
	}

	/**
	 * Gets a property value on the container column with the given definition.
	 * If <code>prop</code> is a style property definition, also check style
	 * values defined on the Table/Grid columns.
	 * 
	 * @param module
	 *            the module
	 * @param container
	 *            the container, must be Table or Grid
	 * @param cell
	 *            the cell on which the property value to find
	 * @param prop
	 *            the property definition
	 * @return the property value
	 */

	private Object getColumnProperty( Module module,
			DesignElement container, Cell cell, ElementPropertyDefn prop )
	{
		Object value = null;
		if ( container instanceof TableItem )
		{
			TableItem table = (TableItem) container;
			value = table.getPropertyFromColumn( module, cell, prop );
		}

		if ( container instanceof GridItem )
		{
			GridItem grid = (GridItem) container;
			value = grid.getPropertyFromColumn( module, cell, prop );
		}

		return value;
	}
	
	
	/* 
	 * Gets a property value given its definition. If <code>prop</code> is a
	 * style property definition, also check style values defined on the Table
	 * columns.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyFromContainer(org.eclipse.birt.report.model.elements.ReportDesign, org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	
	protected Object getPropertyRelatedToContainer( Module module,
			ElementPropertyDefn prop )
	{
		// Get property from the container of this cell. If the container
		// has column, get property from column.

		DesignElement e = getContainer( );
		while ( e != null )
		{
			Object value = e.getPropertyFromElement( module, prop );
			if ( value != null )
				return value;

			// check property values on the columns.

			if ( e.getContainer( ) instanceof TableItem
					|| e.getContainer( ) instanceof GridItem )
				return getColumnProperty( module, e.getContainer( ), this, prop );

			e = e.getContainer( );
		}

		return super.getPropertyRelatedToContainer( module, prop );
	}

	/**
	 * Tests if the property of a cell is inheritable in the context.
	 * <p>
	 * If the cell resides in the row and the property is "vertical-align",
	 * return <code>true</code>. Otherwise, return the value from its super
	 * class.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#isInheritableProperty(org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected boolean isInheritableProperty( ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( Style.VERTICAL_ALIGN_PROP.equalsIgnoreCase( prop.getName( ) )
				&& getContainer( ) instanceof TableRow )
			return true;

		return super.isInheritableProperty( prop );
	}
}