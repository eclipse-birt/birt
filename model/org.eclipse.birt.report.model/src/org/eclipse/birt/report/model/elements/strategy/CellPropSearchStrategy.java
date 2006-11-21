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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Provides the specific property searching route for <code>Cell</code>.
 */

public class CellPropSearchStrategy extends PropertySearchStrategy
{

	private final static CellPropSearchStrategy instance = new CellPropSearchStrategy( );

	/**
	 * Protected constructor.
	 */
	protected CellPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>CellPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>Cell</code>.
	 * 
	 * @return the instance of <code>CellPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyRelatedToContainer(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getPropertyRelatedToContainer( Module module,
			DesignElement cell, ElementPropertyDefn prop )
	{
		// Get property from the container of this cell. If the container
		// has column, get property from column.

		DesignElement e = cell.getContainer( );
		while ( e != null )
		{
			Object value = e.getStrategy( ).getPropertyFromElement( module, e,
					prop );
			if ( value != null )
				return value;

			// check property values on the columns.

			DesignElement tmpContainer = e.getContainer( );
			if ( tmpContainer instanceof TableItem
					|| tmpContainer instanceof GridItem )
				return getColumnProperty( module, tmpContainer, (Cell) cell,
						prop );

			e = tmpContainer;
		}

		return super.getPropertyRelatedToContainer( module, cell, prop );
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

	public Object getColumnProperty( Module module, DesignElement container,
			Cell cell, ElementPropertyDefn prop )
	{
		Object value = null;
		if ( container instanceof TableItem )
		{
			TableItem table = (TableItem) container;
			value = getPropertyFromColumn( module, table, cell, prop );
		}
		else if ( container instanceof GridItem )
		{
			GridItem grid = (GridItem) container;
			value = getPropertyFromColumn( module, grid, cell, prop );
		}

		return value;
	}

	/**
	 * Returns the style property defined on the column for the cell
	 * <code>target</code>.
	 * 
	 * @param module
	 *            the module
	 * @param table
	 *            the container
	 * @param target
	 *            the target cell to search
	 * @param prop
	 *            the property definition.
	 * 
	 * @return the value of a style property
	 */

	protected Object getPropertyFromColumn( Module module, TableItem table,
			Cell target, ElementPropertyDefn prop )
	{
		assert prop.isStyleProperty( );

		ContainerSlot columnSlot = table.getSlot( ITableItemModel.COLUMN_SLOT );
		if ( columnSlot.getCount( ) == 0 )
			return null;

		int columnNum = target.getColumn( module );
		if ( columnNum == 0 )
			columnNum = table.getColumnPosition4Cell( module, target );

		// if the layout still not updated yet.

		if ( columnNum == 0 )
			return null;

		TableColumn column = ColumnHelper.findColumn( module, columnSlot,
				columnNum );

		if ( column != null )
			return column.getStrategy( ).getPropertyFromElement( module,
					column, prop );

		return null;
	}

	/**
	 * Returns the style property defined on the column for the cell
	 * <code>target</code>.
	 * 
	 * @param module
	 *            the report design
	 * @param grid
	 *            the container
	 * @param target
	 *            the target cell to search
	 * @param prop
	 *            the property definition.
	 * 
	 * @return the value of a style property
	 */

	protected Object getPropertyFromColumn( Module module, GridItem grid,
			Cell target, ElementPropertyDefn prop )
	{
		assert prop.isStyleProperty( );

		ContainerSlot columnSlot = grid.getSlot( IGridItemModel.COLUMN_SLOT );
		if ( columnSlot.getCount( ) == 0 )
			return null;

		int columnNum = grid.getCellPositionInColumn( module, target );

		assert columnNum > 0;
		TableColumn column = ColumnHelper.findColumn( module, columnSlot,
				columnNum );

		if ( column != null )
			return column.getStrategy( ).getPropertyFromElement( module,
					column, prop );

		return null;
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

	protected boolean isInheritableProperty( DesignElement element,
			ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( IStyleModel.VERTICAL_ALIGN_PROP.equalsIgnoreCase( prop.getName( ) )
				&& element.getContainer( ) instanceof TableRow )
			return true;

		return super.isInheritableProperty( element, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyFromSelfSelector(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelfSelector( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		assert element instanceof Cell;

		TableRow row = (TableRow) element.getContainer( );
		if ( row == null )
			return null;

		DesignElement rowContainer = row.getContainer( );
		if ( rowContainer == null )
			return null;

		String selector = "cell"; //$NON-NLS-1$
		if ( rowContainer instanceof TableItem )
		{
			selector = "table-" //$NON-NLS-1$
					+ rowContainer.getDefn( ).getSlot( row.getContainerSlot( ) )
							.getName( ) + "-" + selector; //$NON-NLS-1$
		}
		else if ( rowContainer instanceof TableGroup )
		{
			selector = "table-group-" //$NON-NLS-1$
					+ rowContainer.getDefn( ).getSlot( row.getContainerSlot( ) )
							.getName( ) + "-" + selector; //$NON-NLS-1$
		}
		else
			return null;

		return getPropertyFromSelector( module, prop, selector );
	}
}
