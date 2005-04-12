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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;

/**
 * Represents a table element. A table has a localized caption and can repeat
 * its heading at the top of each page. The table is a list that is structured
 * into a rows and columns. The columns are defined for the entire table. Rows
 * are clustered into a set of groups.
 * 
 * @see org.eclipse.birt.report.model.elements.TableItem
 *  
 */

public class TableHandle extends ListingHandle
{

	/**
	 * Constructs a handle for the table with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public TableHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the column slot. The column slot represents a list of Column
	 * elements that describe the table columns.
	 * 
	 * @return a handle to the detail slot
	 * @see SlotHandle
	 */

	public SlotHandle getColumns( )
	{
		return getSlot( TableItem.COLUMN_SLOT );
	}

	/**
	 * Returns the number of columns in the table. The number is defined as 1)
	 * the sum of columns described in the "column" slot, or 2) the widest row
	 * defined in the detail, header or footer slots if column slot is empty.
	 * 
	 * @return the number of columns in the table
	 */

	public int getColumnCount( )
	{
		return ( (TableItem) getElement( ) ).getColumnCount( design );
	}

	/**
	 * Tests whether to repeat the headings at the top of each page.
	 * 
	 * @return <code>true</code> if repeat the headings, otherwise
	 *         <code>false</code>.
	 */

	public boolean repeatHeader( )
	{
		return getBooleanProperty( TableItem.REPEAT_HEADER_PROP );
	}

	/**
	 * Sets whether to repeat the headings at the top of each page.
	 * 
	 * @param value
	 *            <code>true</code> if repeat the headings, otherwise
	 *            <code>false</code>.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setRepeatHeader( boolean value ) throws SemanticException
	{
		setProperty( TableItem.REPEAT_HEADER_PROP, Boolean.valueOf( value ) );
	}

	/**
	 * Returns the caption text of this table.
	 * 
	 * @return the caption text
	 */

	public String getCaption( )
	{
		return getStringProperty( TableItem.CAPTION_PROP );
	}

	/**
	 * Sets the caption text of this table.
	 * 
	 * @param caption
	 *            the caption text
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setCaption( String caption ) throws SemanticException
	{
		setStringProperty( TableItem.CAPTION_PROP, caption );
	}

	/**
	 * Returns the resource key of the caption.
	 * 
	 * @return the resource key of the caption
	 */

	public String getCaptionKey( )
	{
		return getStringProperty( TableItem.CAPTION_KEY_PROP );
	}

	/**
	 * Sets the resource key of the caption.
	 * 
	 * @param captionKey
	 *            the resource key of the caption
	 * @throws SemanticException
	 *             if the caption resource-key property is locked.
	 */

	public void setCaptionKey( String captionKey ) throws SemanticException
	{
		setStringProperty( TableItem.CAPTION_KEY_PROP, captionKey );
	}
}