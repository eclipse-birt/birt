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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * Represents one row in a Grid or Table. Each row contains some number of
 * cells. And one row can define its height.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.TableRow
 */

public class RowHandle extends ReportElementHandle
{

	/**
	 * Constructs the handle for a row with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public RowHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the cell slot of row. Through SlotHandle, each cell can be
	 * obtained.
	 * 
	 * @return the handle to the cell slot
	 * 
	 * @see SlotHandle
	 */

	public SlotHandle getCells( )
	{
		return getSlot( TableRow.CONTENT_SLOT );
	}

	/**
	 * Gets a handle to deal with the row's height.
	 * 
	 * @return a DimensionHandle for the row's height.
	 */

	public DimensionHandle getHeight( )
	{
		return super.getDimensionProperty( TableRow.HEIGHT_PROP );
	}

	/**
	 * Returns the bookmark of this row.
	 * 
	 * @return the bookmark of this row
	 */

	public String getBookmark( )
	{
		return getStringProperty( TableRow.BOOKMARK_PROP );
	}

	/**
	 * Sets the bookmark of this row.
	 * 
	 * @param value
	 *            the bookmark to set
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setBookmark( String value ) throws SemanticException
	{
		setStringProperty( TableRow.BOOKMARK_PROP, value );
	}

	/**
	 * Returns visibility rules defined on the table row. The element in the
	 * iterator is the corresponding <code>StructureHandle</code> that deal
	 * with a <code>Hide</code> in the list.
	 * 
	 * @return the iterator for visibility rules defined on this row. 
	 * 
	 * @see org.eclipse.birt.report.model.elements.structures.Hide
	 */

	public Iterator visibilityRulesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( TableRow.VISIBILITY_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}
}