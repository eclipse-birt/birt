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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
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

public class TableItem extends ListingElement
{

	/**
	 * Name of the caption property.
	 */

	public static final String CAPTION_PROP = "caption"; //$NON-NLS-1$

	/**
	 * Name of the caption key property.
	 */

	public static final String CAPTION_KEY_PROP = "captionID"; //$NON-NLS-1$

	/**
	 * Name of the repeat header property.
	 */

	public static final String REPEAT_HEADER_PROP = "repeatHeader"; //$NON-NLS-1$

	/**
	 * Column definitions.
	 */

	public static final int COLUMN_SLOT = 4;

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

	private int findMaxCols( ReportDesign design )
	{
		int maxCols = 0;
		maxCols = findMaxCols( design, this, HEADER_SLOT, maxCols );
		maxCols = DroppingHelper.findMaxColsOfDropping( design, this, maxCols );
		maxCols = findMaxCols( design, this, FOOTER_SLOT, maxCols );

		ContainerSlot groups = getSlot( GROUP_SLOT );
		int groupCount = groups.getCount( );
		for ( int i = 0; i < groupCount; i++ )
		{
			TableGroup group = (TableGroup) groups.getContent( i );
			maxCols = findMaxCols( design, group, TableGroup.HEADER_SLOT,
					maxCols );
			maxCols = findMaxCols( design, group, TableGroup.FOOTER_SLOT,
					maxCols );
		}

		return maxCols;
	}

	/**
	 * Gets the number of columns described in the column definition section.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns described by column definitions
	 */

	private int getColDefnCount( ReportDesign design )
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
	 * Finds the maximum column width for a band.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the design element to compute
	 * @param slot
	 *            the slot to check
	 * @param maxCols
	 *            the current maximum number of columns
	 * @return the updated maximum number of columns
	 */

	private int findMaxCols( ReportDesign design, DesignElement element,
			int slot, int maxCols )
	{
		ContainerSlot band = element.getSlot( slot );
		int count = band.getCount( );
		for ( int i = 0; i < count; i++ )
		{
			TableRow row = (TableRow) band.getContent( i );
			int cols = row.getColumnCount( design );
			if ( cols > maxCols )
				maxCols = cols;
		}
		return maxCols;
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
		{
			columnNum = DroppingHelper.findCellColumn( design, this, target );
		}

		assert columnNum > 0;
		TableColumn column = findColumn( design, columnNum );
		if ( column != null )
			return getEffectivePropFromColumn( design, prop, column );

		return null;
	}

	/**
	 * Figures out the column according to the index of the column.
	 * 
	 * @param design
	 *            the report design
	 * @param columnNum
	 *            the column number to search
	 * 
	 * @return the index of a column.
	 */

	private TableColumn findColumn( ReportDesign design, int columnNum )
	{
		assert columnNum > 0;

		ContainerSlot columnSlot = slots[COLUMN_SLOT];
		for ( int i = 0, index = 0; i < columnSlot.getCount( ); i++ )
		{
			TableColumn column = (TableColumn) ( columnSlot.getContent( i ) );
			int repeat = column
					.getIntProperty( design, TableColumn.REPEAT_PROP );

			// in default, repeat is one.

			repeat = ( repeat == 0 ) ? 1 : repeat;

			index += repeat;

			if ( index >= columnNum )
				return column;
		}

		return null;
	}

	/**
	 * Finds the style property defined on a column. Only the local property,
	 * the shared style and the predefined values on selectors are involved.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            the property definition.
	 * @param column
	 *            the table column
	 * 
	 * @return the value of a style property
	 */

	private Object getEffectivePropFromColumn( ReportDesign design,
			ElementPropertyDefn prop, TableColumn column )
	{
		Object value = null;

		// 1). If we can find the value here, return it.

		value = column.getLocalProperty( design, prop );
		if ( value != null )
			return value;

		// 2). Does the style provide the value of this property ?

		StyleElement style = column.getLocalStyle( );
		if ( style != null )
		{
			value = style.getLocalProperty( design, prop );
			if ( value != null )
				return value;
		}

		// 3). Check if this element predefined style provides
		// the property value

		String selector = column.getDefn( ).getSelector( );
		value = column.getPropertyFromSelector( design, prop, selector );
		if ( value != null )
			return value;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// If column definitions are defined, then they must describe the
		// number of columns actually used by the table. It is legal to
		// have a table with zero columns.

		int colDefnCount = getColDefnCount( design );
		int maxCols = findMaxCols( design );
		if ( colDefnCount != maxCols && colDefnCount != 0 )
			list.add( new SemanticError( this,
					SemanticError.INCONSITENT_TABLE_COL_COUNT ) );

		// Check table's slot context containment.

		list.addAll( checkTableHeaderContainment( this ) );

		// check whether there is any overlapping cells with drop properties in
		// the group headers.

		if ( DroppingHelper.isGHDroppingValid( design, this ) == false )
			list.add( new SemanticError( this,
					SemanticError.INCONSITENT_DROP_HEADINGS ) );

		return list;
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

		if ( !containsListingElement( content ) )
			return errors;

		return checkTableHeaderContainment( container );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.metadata.ElementDefn)
	 */

	protected List checkContent( ReportDesign design, DesignElement container,
			int slotId, ElementDefn defn )
	{
		List errors = super.checkContent( design, container, slotId, defn );
		if ( !errors.isEmpty( ) )
			return errors;

		if ( defn.getParent( ) == null
				|| !defn.getParent( ).getName( ).equalsIgnoreCase(
						ReportDesignConstants.LISTING_ITEM ) )
			return errors;

		return checkTableHeaderContainment( container );
	}

	/**
	 * Checks whether the <code>element</code> recursively contains a
	 * <code>ListingItem</code>.
	 * 
	 * @param element
	 *            the element to check
	 * 
	 * @return <code>true</code> if the <code>element</code> recursively
	 *         contains a <code>ListingItem</code>. Otherwise
	 *         <code>false</code>.
	 */

	private boolean containsListingElement( DesignElement element )
	{
		if ( element instanceof ListingElement )
			return true;

		// Check contents.

		int count = element.getDefn( ).getSlotCount( );
		for ( int i = 0; i < count; i++ )
		{
			Iterator iter = element.getSlot( i ).iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElement e = (DesignElement) iter.next( );

				if ( e instanceof ListingElement )
					return true;

				if ( containsListingElement( e ) )
					return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether the element <code>container</code> recursively resides
	 * in the TABLE_HEADER slot of a table item.
	 * 
	 * @param container
	 *            the element to check
	 * 
	 * @return <code>true</code> if it resides in in the TABLE_HEADER slot of
	 *         a table item. Otherwise <code>false</code>.
	 */

	private List checkTableHeaderContainment( DesignElement container )
	{
		List errors = new ArrayList( );

		try
		{
			container.checkContextContainment(
					ReportDesignConstants.TABLE_ITEM, TableItem.HEADER_SLOT );
		}
		catch ( ContentException e )
		{
			errors.add( e );
		}

		return errors;
	}
}