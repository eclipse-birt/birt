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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * This class parses common properties for both list and table report items.
 * 
 * @see org.eclipse.birt.report.model.elements.ListingElement
 */

public abstract class ListingItemState extends ReportItemState
{

	/**
	 * The listing element (table or list) being built.
	 */

	protected ListingElement element;

	/**
	 * Constructs a state to parse the common properties of the list and table
	 * report items.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ListingItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.SORT_TAG ) )
			return new SortState( handler, element, ListingElement.SORT_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FILTER_TAG ) )
			return new FiltersState( handler, element,
					ListingElement.FILTER_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG ) )
			return new MethodState( handler, getElement( ) );
		return super.startElement( tagName );
	}

}