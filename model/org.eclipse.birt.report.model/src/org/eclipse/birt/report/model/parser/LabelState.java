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
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a label item.
 *  
 */

public class LabelState extends ReportItemState
{

	/**
	 * The label being created.
	 */

	protected Label element;

	/**
	 * Constructs the label state with the design parser handler, the container
	 * element and the container slot of the label.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public LabelState( DesignParserHandler handler, DesignElement theContainer,
			int slot )
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
		element = new Label( );
		initElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
			return new ExternalTextState( handler, element, Label.TEXT_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HELP_TEXT_TAG ) )
			return new ExternalTextState( handler, element,
					Label.HELP_TEXT_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ACTION_TAG ) )
			 return new ActionState( handler, element, Label.ACTION_PROP );
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