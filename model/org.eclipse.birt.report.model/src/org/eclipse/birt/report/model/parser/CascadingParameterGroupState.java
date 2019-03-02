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

import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the cascading parameter group element.
 * 
 */

public class CascadingParameterGroupState extends ParameterGroupState
{

	/**
	 * Constructor.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param slot
	 *            the slot ID of the slot where the parameter is stored.
	 */
	public CascadingParameterGroupState( ModuleParserHandler handler, int slot )
	{
		super( handler, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		paramGroup = new CascadingParameterGroup( );
		initElement( attrs, true );
	}
}
