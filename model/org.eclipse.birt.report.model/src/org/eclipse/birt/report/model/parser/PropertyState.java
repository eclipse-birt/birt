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
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.StringUtil;
import org.xml.sax.SAXException;

/**
 * Parses the "property" tag. The tag may give the property value of the element
 * or the member of the structure.
 */

class PropertyState extends AbstractPropertyState
{

	protected PropertyDefn propDefn = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, )
	 */

	PropertyState( DesignParserHandler theHandler, DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, String propName, IStructure
	 *      struct)
	 */

	PropertyState( DesignParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element );

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/**
	 * Sets the name in attribute.
	 * 
	 * @param name
	 *            the value of the attribute name
	 */

	protected void setName( String name )
	{
		super.setName( name );

		if ( struct != null )
		{
			propDefn = struct.getDefn( ).getMember( name );
		}
		else
		{
			propDefn = element.getPropertyDefn( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );
		if ( StringUtil.isBlank( value ) )
			return;

		if ( struct != null )
		{
			setMember( struct, propDefn.getName( ), name, value );
			return;
		}

		if ( StyledElement.STYLE_PROP.equalsIgnoreCase( name ) )
		{
			( (StyledElement) element ).setStyleName( value );
		}
		else
		{
			setProperty( name, value );
		}
	}
}