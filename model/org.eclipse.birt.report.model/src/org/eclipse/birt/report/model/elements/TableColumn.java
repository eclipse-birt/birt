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

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.StyledElement;

/**
 * This class represents a column element within a table. If an element
 * references a column that does not exist, then BIRT creates the column
 * implicitly as a variable-width column. The developer defines columns to aid
 * in report layout. Each column has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Width </strong></dt>
 * <dd>a column can be variable width or fixed width.</dd>
 * 
 * <dt><strong>Style </strong></dt>
 * <dd>a column can use the style defined for the grid as a whole, or can
 * define a separate style. The developer uses this to create a distinct border
 * around the column, use a different background color, etc.</dd>
 * 
 * <dt><strong>Alignment </strong></dt>
 * <dd>how to align items with the column: left, center or right.</dd>
 * </dl>
 *  
 */

public class TableColumn extends StyledElement
{

	/**
	 * Name of the property that says how many columns are described by this
	 * element.
	 */

	public static final String REPEAT_PROP = "repeat"; //$NON-NLS-1$

	/**
	 * Name of the width property of the column. If not width is provided, then
	 * the column is variable-width and will resize to fit its content and the
	 * width of the page.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Default Constructor.
	 */

	public TableColumn( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitColumn( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.COLUMN_ELEMENT;
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
	 *            the report design of the column
	 * 
	 * @return an API handle for this element
	 */

	public ColumnHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ColumnHandle( design, this );
		}
		return (ColumnHandle) handle;
	}
}
