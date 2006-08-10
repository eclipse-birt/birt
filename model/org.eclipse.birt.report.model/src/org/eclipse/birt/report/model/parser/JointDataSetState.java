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
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Parser of the joint data set element.
 * 
 */

public class JointDataSetState extends ReportElementState
{

	/**
	 * The joint data set being built.
	 */

	protected JointDataSet element;

	/**
	 * Constructs the joint data set state with design parser handler, container
	 * element and container slot of the data source.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public JointDataSetState( ModuleParserHandler handler )
	{
		super( handler, handler.getModule( ), Module.DATA_SET_SLOT );
		element = new JointDataSet( );
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
		initElement( attrs, true );
	}
}