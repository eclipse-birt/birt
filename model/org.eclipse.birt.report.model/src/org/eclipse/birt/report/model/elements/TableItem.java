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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.validators.InconsistentColumnsValidator;
import org.eclipse.birt.report.model.api.validators.TableHeaderContextContainmentValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.elements.table.LayoutHelper;
import org.eclipse.birt.report.model.elements.table.LayoutTable;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a table in design.A table is a list that is structured
 * into a rows and columns.The columns are defined for the entire table. Rows
 * are created in response to the same events as for a list.Like a list, a table
 * is defined by a series of bands. A table defines the same bands as a list.
 * Like a list, each band is divided into a number of sections. Each section
 * contains one or more rows. Each row is further divided into a set of cells.
 * 
 */

public class TableItem extends ListingElement implements ITableItemModel
{

	/**
	 * The table model.
	 */

	private LayoutTable table = null;

	/**
	 * Default constructor.
	 */

	public TableItem( )
	{
		super( );
	}

	/**
	 * Constructs the table item with an optional name.
	 * 
	 * @param theName
	 *            the optional name of the table
	 */

	public TableItem( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitTable( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.TABLE_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element.
	 */

	public TableHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new TableHandle( design, this );
		}
		return (TableHandle) handle;
	}

	/**
	 * Computes the number of columns in the table. The number is defined as 1)
	 * the sum of columns describe in the Columns slot, or 2) the widest row
	 * defined in the other slots.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns in the table
	 */

	public int getColumnCount( ReportDesign design )
	{
		// Method 1: sum columns in the column slot.

		int colCount = getColDefnCount( design );
		if ( colCount != 0 )
			return colCount;

		// Method 2: find the widest row.

		return findMaxCols( design );
	}

	/**
	 * Computes the maximum column count in the table.
	 * 
	 * @param design
	 *            the report design
	 * @return the maximum column count in the table
	 */

	public int findMaxCols( ReportDesign design )
	{
		refreshRenderModel( design );
		return table.getColumnCount( );
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns described by column definitions
	 */

	public int getColDefnCount( ReportDesign design )
	{
		int colCount = 0;
		ContainerSlot cols = getSlot( COLUMN_SLOT );
		int colDefnCount = cols.getCount( );
		for ( int i = 0; i < colDefnCount; i++ )
		{
			TableColumn col = (TableColumn) cols.getContent( i );
			colCount += col.getIntProperty( design, TableColumn.REPEAT_PROP );
		}
		return colCount;
	}

	/**
	 * Returns the style property defined on the column for the cell
	 * <code>target</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param target
	 *            the target cell to search
	 * @param prop
	 *            the property definition.
	 * 
	 * @return the value of a style property
	 */

	protected Object getPropertyFromColumn( ReportDesign design, Cell target,
			ElementPropertyDefn prop )
	{
		assert prop.isStyleProperty( );

		ContainerSlot columnSlot = slots[COLUMN_SLOT];
		if ( columnSlot.getCount( ) == 0 )
			return null;

		int columnNum = target.getColumn( design );
		if ( columnNum == 0 )
			columnNum = getColumnPosition4Cell( design, target );

		assert columnNum > 0;
		TableColumn column = ColumnHelper.findColumn( design,
				slots[COLUMN_SLOT], columnNum );

		if ( column != null )
			return column.getPropertyFromElement( design, prop );

		return null;
	}

	/**
	 * Returns the column number with a specified <code>Cell</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param target
	 *            the cell to find
	 * @return 1-based the column number
	 */

	public int getColumnPosition4Cell( ReportDesign design, Cell target )
	{
		if ( target == null )
			return 0;

		int slotId = target.getContainer( ).getContainerSlot( );

		TableRow row = (TableRow) target.getContainer( );
		DesignElement grandPa = row.getContainer( );
		int rowId = grandPa.getSlot( slotId ).findPosn( row );

		if ( grandPa instanceof TableItem )
		{
			assert grandPa == this;
			refreshRenderModel( design );

			return table.getColumnPos( slotId, rowId, target );
		}

		return table.getColumnPos( ( (TableGroup) grandPa ).getGroupLevel( ),
				slotId, rowId, target );
	}

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// If column definitions are defined, then they must describe the
		// number of columns actually used by the table. It is legal to
		// have a table with zero columns.

		list.addAll( InconsistentColumnsValidator.getInstance( ).validate(
				design, this ) );

		// Check table's slot context containment.

		list.addAll( TableHeaderContextContainmentValidator.getInstance( )
				.validate( design, this ) );

		return list;
	}

	/**
	 * Returns the table model of <code>TableItem</code>. This model is
	 * different from the natural of <code>TableItem</code> since "colSpan",
	 * "rowSpan" and "dropping cells" are applied. Mainly uses this model to
	 * render the <code>TableItem</code>.
	 * 
	 * @param design
	 *            the report design
	 * @return the table model for rendering
	 */

	public LayoutTable getRenderModel( ReportDesign design )
	{
		if ( table == null )
			table = LayoutHelper.applyLayout( design, this );
		
		return table;
	}

	/**
	 * Refreshes the table model of <code>TableItem</code>.
	 * 
	 * @param design
	 *            the report design
	 * @return the table model for rendering
	 * 
	 * @see {@link #getRenderModel(ReportDesign)}
	 */

	public void refreshRenderModel( ReportDesign design )
	{
		table = LayoutHelper.applyLayout( design, this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected List checkContent( ReportDesign design, DesignElement container,
			int slotId, DesignElement content )
	{
		List errors = super.checkContent( design, container, slotId, content );
		if ( !errors.isEmpty( ) )
			return errors;

		errors.addAll( TableHeaderContextContainmentValidator.getInstance( )
				.validateForAdding( design, container, slotId, content ) );

		return errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.metadata.IElementDefn)
	 */

	protected List checkContent( ReportDesign design, DesignElement container,
			int slotId, IElementDefn defn )
	{
		List errors = super.checkContent( design, container, slotId, defn );
		if ( !errors.isEmpty( ) )
			return errors;

		errors.addAll( TableHeaderContextContainmentValidator.getInstance( )
				.validateForAdding( design, container, defn ) );

		return errors;
	}

}