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
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * This class parses the data source element.
 * 
 */

public abstract class DataSourceState extends ReportElementState
{

	protected DataSource element;

	/**
	 * Constructs the data source state with the design parser handler, the
	 * container element and the container slot of the data source.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public DataSourceState( DesignParserHandler handler )
	{
		super( handler, handler.getDesign( ), ReportDesign.DATA_SOURCE_SLOT );
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	
	public AbstractParseState startElement( String tagName )
	{
		if ( DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase( tagName ) )
			return new MethodState( handler, element );
		return super.startElement( tagName );
	}
}
