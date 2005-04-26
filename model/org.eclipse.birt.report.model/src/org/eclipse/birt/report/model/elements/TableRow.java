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
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.CellOverlappingValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * This class represents a row in a Grid or a table.
 * 
 */

public class TableRow extends StyledElement implements ITableRowModel
{

	/**
	 * Holds the cells that reside directly on the row.
	 */

	protected ContainerSlot contents = new MultiElementSlot( );

	/**
	 * Default constructor.
	 */

	public TableRow( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		TableRow row = (TableRow) super.clone( );
		row.contents = contents.copy( row, CONTENT_SLOT );
		return row;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert ( slot == CONTENT_SLOT );
		return contents;
	}

	/**
	 * Gets the contents of of the Contents slot. DO NOT change the returned
	 * list, use the handle class to make changes.
	 * 
	 * @return the contents as an array
	 */

	public List getContentsSlot( )
	{
		return contents.getContents( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitRow( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.ROW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.element.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design of the row
	 * 
	 * @return an API handle for this element
	 */

	public RowHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new RowHandle( design, this );
		}
		return (RowHandle) handle;
	}

	/**
	 * Computes the number of columns defined by this row.
	 * 
	 * @param design
	 *            the report design
	 * @return the number of columns defined in this row
	 */

	public int getColumnCount( ReportDesign design )
	{
		int colCount = 0;
		int cellCount = contents.getCount( );
		for ( int i = 0; i < cellCount; i++ )
		{
			Cell cell = (Cell) contents.getContent( i );
			int posn = cell.getColumn( design );
			int span = cell.getColSpan( design );

			// One-based indexing. Position is optional.

			if ( posn > 0 )
			{
				int end = posn + span - 1;
				if ( end > colCount )
					colCount = end;
			}
			else
				colCount += span;
		}
		return colCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		list.addAll( CellOverlappingValidator.getInstance( ).validate( design,
				this ) );

		return list;
	}

}