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
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a scalar parameter.
 * 
 */

public class ScalarParameterState extends ParameterState
{

	/**
	 * The scalar parameter being created.
	 */

	protected ScalarParameter param;

	/**
	 * Constructs the scalar parameter state with the design parser handler, the
	 * container element and the container slot of the scalar parameter.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the container of this parameter.
	 * @param slot
	 *            the slot ID of the slot where the parameter is stored.
	 */

	public ScalarParameterState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Constructs the scalar parameter state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            the parse handler
	 */

	ScalarParameterState( DesignParserHandler theHandler )
	{
		super( theHandler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		// First we create the ScalarParameter.

		param = new ScalarParameter( );

		// Then we initialize the properties derived from the
		// the Report Item element. The name is required for a parameter.
		// <code>initElement</code> adds the parameter to the parameters slot
		// of the report design.

		initElement( attrs, true );
	}

	/**
	 * Returns the scalar parameter being built.
	 * 
	 * @return the parameter instance
	 */

	public DesignElement getElement( )
	{
		return param;
	}	
}
