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
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the Data (data item) tag.
 * 
 */

public class DataItemState extends ReportItemState
{

	/**
	 * The data item being created.
	 */

	public DataItem element;

	/**
	 * Constructs the data item state with the design parser handler, the
	 * container element and the container slot of the data item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public DataItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new DataItem( );
		initElement( attrs );
		setProperty( DataItem.DISTINCT_PROP, attrs
				.getValue( DesignSchemaConstants.DISTINCT_ATTRIB ) );
		setProperty( DataItem.DISTINCT_RESET_PROP, attrs
				.getValue( DesignSchemaConstants.DISTINCT_RESET_ATTRIB ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.VALUE_EXPR_TAG ) )
			return new TextState( handler, element, DataItem.VALUE_EXPR_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ACTION_TAG ) )
			return new ActionState( handler, element, DataItem.ACTION_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HELP_TEXT_TAG ) )
			return new ExternalTextState( handler, element,
					DataItem.HELP_TEXT_PROP );
		return super.startElement( tagName );
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

}
