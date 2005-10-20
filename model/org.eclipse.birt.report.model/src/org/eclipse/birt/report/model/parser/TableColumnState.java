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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a column within a table or grid item.
 *  
 */

class TableColumnState extends ReportElementState
{

	protected TableColumn element = null;

	/**
	 * Constructs the state to parse table column.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TableColumnState( ModuleParserHandler handler,
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TableColumn( );
		// get the "id" of the element

		try
		{
			String theID = attrs.getValue( DesignSchemaConstants.ID_ATTRIB );

			if ( !StringUtil.isBlank( theID ) )
			{
				// if the id is not null, parse it

				long id = Long.parseLong( theID );
				element.setID( id );
			}
		}
		catch ( NumberFormatException e )
		{
			handler
					.getErrorHandler( )
					.semanticError(
							new DesignParserException(
									new String[]{
											element.getIdentifier( ),
											attrs
													.getValue( DesignSchemaConstants.ID_ATTRIB )},
									DesignParserException.DESIGN_EXCEPTION_INVALID_ELEMENT_ID ) );
		}		
		if ( !addToSlot( container, slotID, element ) )
			return;
	}

	public void end( )
	{
		makeTestExpressionCompatible( );
	}
}