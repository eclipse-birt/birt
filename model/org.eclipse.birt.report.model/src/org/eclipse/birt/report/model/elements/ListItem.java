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
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.validators.ContextContainmentValidator;

/**
 * This class represents a List element. List is a free-form layout driven by
 * data from a data set. See the base class, <code>ListingElement</code> for
 * details.
 * 
 * @see ListingElement
 */

public class ListItem extends ListingElement
{

	/**
	 * Default constructor.
	 */

	public ListItem( )
	{
	}

	/**
	 * Constructs the list item with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ListItem( String theName )
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
		visitor.visitList( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.LIST_ITEM;
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
	 * @return an API handle for this element
	 */

	public ListHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ListHandle( design, this );
		}
		return (ListHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// Check header slot context containment of table.

		list.addAll( ContextContainmentValidator.getInstance( ).validate(
				design, this, ReportDesignConstants.TABLE_ITEM,
				TableItem.HEADER_SLOT ) );

		return list;
	}
}
