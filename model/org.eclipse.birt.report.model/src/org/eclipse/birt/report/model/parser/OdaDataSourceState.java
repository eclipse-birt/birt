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

import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the oda data source element.
 *  
 */

public class OdaDataSourceState extends DataSourceState
{

	/**
	 * Constructs the oda data source state with the design parser handler,
	 * the container element and the container slot of the oda data source.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public OdaDataSourceState( DesignParserHandler handler )
	{
		super( handler );
		element = new OdaDataSource( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( DesignSchemaConstants.DRIVER_NAME_ATTRIB
				.equalsIgnoreCase( tagName ) )
			return new TextState( handler, element,
					OdaDataSource.DRIVER_NAME_PROP );

		if ( DesignSchemaConstants.PUBLIC_DRIVER_PROPERTIES_TAG
				.equalsIgnoreCase( tagName ) )
			return new PropertiesState( handler, element,
					OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP );

		if ( DesignSchemaConstants.PRIVATE_DRIVER_PROPERTIES_TAG
				.equalsIgnoreCase( tagName ) )
			return new PropertiesState( handler, element,
					OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );

		return super.startElement( tagName );
	}

}