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
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;



/**
 * This class parses the text item.
 *
 */

public class TextItemState extends ReportItemState
{
	/**
	 * The text item being created.
	 */
	
	protected TextItem element;
	
	/**
	 * Constructs the text item state with the design parser handler, the
	 * container element and the container slot of the text item.
	 * 
	 * @param handler the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot the slot in which this element appears
	 */
	
	public TextItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */
	
	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TextItem( );
		initElement( attrs );
		setProperty( TextItem.CONTENT_TYPE_PROP, attrs, DesignSchemaConstants.CONTENT_TYPE_ATTRIB );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CONTENT_TAG ) )
			return new ExternalTextState( handler, element,	TextItem.CONTENT_PROP );
		
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
