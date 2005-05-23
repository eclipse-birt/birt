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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Represents the list property state which is used to reading structure list of
 * private ODA driver properties.
 * <p>
 * The compatible version is 0.
 */

public class CompatibleOdaDriverPropertyStructureListState
		extends
			PropertyListState
{

	CompatibleOdaDriverPropertyStructureListState(
			DesignParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );

		setProperty( DesignSchemaConstants.EXTENSION_NAME_ATTRIB, "jdbc" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EX_PROPERTY_TAG ) )
			return new CompatibleOdaDriverPropertyStructureState( handler, element,
					propDefn, list );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end( ) throws SAXException
	{
	}

	class CompatibleOdaDriverPropertyStructureState extends StructureState
	{

		String propertyName = null;
		String propertyValue = null;

		CompatibleOdaDriverPropertyStructureState( DesignParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, ArrayList theList )
		{
			super( theHandler, element );
			this.list = theList;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.NAME_ATTRIB ) )
				return new CompatibleTextState( handler, true, this );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.VALUE_TAG ) )
				return new CompatibleTextState( handler, false, this );

			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			if ( propertyValue != null && propertyName != null )
			{
				String newPropertyName = CompatibleOdaDriverPropertyState
						.getNewOdaDriverProperty( propertyName );

				element.setProperty( newPropertyName, propertyValue );
			}
		}
	}

	class CompatibleTextState extends DesignParseState
	{

		boolean isPropertyName = false;
		CompatibleOdaDriverPropertyStructureState state;

		CompatibleTextState( DesignParserHandler handler, boolean isPropertyName,
				CompatibleOdaDriverPropertyStructureState state )
		{
			super( handler );

			this.isPropertyName = isPropertyName;
			this.state = state;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
		 */
		public DesignElement getElement( )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			String value = text.toString( );

			if ( isPropertyName )
				state.propertyName = value;
			else
				state.propertyValue = value;
		}

	}
}