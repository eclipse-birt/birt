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
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.Attributes;

/**
 * Base class for parsing all kinds of parameter.
 * 
 */

public abstract class ParameterState extends ReportElementState
{

	/**
	 * Constructs the parameter state with the design parser handler, the
	 * container element and the container slot of the parameter.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the container of this parameter
	 * @param slot
	 *            the slot ID of the slot where the parameter is stored.
	 */
	
	public ParameterState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Constructs the parameter state with design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	ParameterState( DesignParserHandler handler )
	{
		super( handler, handler.getDesign( ), ReportDesign.PARAMETER_SLOT );
	}

	/**
	 * sets the property owned by Class Parameter but not in the properties of
	 * its parent Class ReportElement.
	 * 
	 * @param attrs
	 *            the Attributes instance
	 */

	protected void initParameter( Attributes attrs )
	{
		setProperty( Parameter.HIDDEN_PROP, attrs,
				DesignSchemaConstants.HIDDEN_ATTRIB );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HELP_TEXT_TAG ) )
			return new ExternalTextState( handler, getElement( ),
					Parameter.HELP_TEXT_PROP );
		return super.startElement( tagName );
	}
}
