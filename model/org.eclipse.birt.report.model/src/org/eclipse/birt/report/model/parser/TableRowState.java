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
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a row within a table item.
 *  
 */

public class TableRowState extends ReportElementState
{

	/**
	 * The row being created.
	 */

	protected TableRow element;

	/**
	 * Constructs the table or list row state with the design parser handler,
	 * the container element and the container slot of the table row.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TableRowState( ModuleParserHandler handler,
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

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TableRow( );
		
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CELL_TAG ) )
			return new CellState( handler, element, TableRow.CONTENT_SLOT );
		return super.startElement( tagName );
	}
}