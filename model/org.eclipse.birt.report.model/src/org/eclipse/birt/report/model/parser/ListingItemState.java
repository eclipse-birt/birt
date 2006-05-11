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

import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.xml.sax.SAXException;

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

	public ListingItemState( ModuleParserHandler handler,
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
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		makeTestExpressionCompatible( );

		Set elements = handler.tempValue.keySet( );
		ContainerSlot groups = element.getSlot( ListingElement.GROUP_SLOT );
		for ( int i = 0; i < groups.getCount( ); i++ )
		{
			GroupElement group = (GroupElement) groups.getContent( i );

			handler.getModule( ).getNameManager( ).makeUniqueName( group );
			
			String groupName = (String) group.getLocalProperty( handler
					.getModule( ), GroupElement.GROUP_NAME_PROP );

			if ( !elements.contains( group ) )
				continue;

			List columns = (List) handler.tempValue.get( group );
			for ( int j = 0; j < columns.size( ); j++ )
			{
				ComputedColumn column = (ComputedColumn) columns.get( j );
				column.setAggregrateOn( groupName );
			}

			List tmpList = (List) element.getLocalProperty( handler.module,
					ListingElement.BOUND_DATA_COLUMNS_PROP );
			if ( tmpList != null )
				tmpList.addAll( columns );
			else
				element.setProperty( ListingElement.BOUND_DATA_COLUMNS_PROP,
						columns );
		}

		super.end( );
	}
}