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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
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
			propDefn = (PropertyDefn) struct.getDefn( ).getMember( name );
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

	public AbstractParseState jumpTo( )
	{
		if ( !valid )
			return new AnyElementState( getHandler( ) );

		IPropertyDefn jmpDefn = null;
		if ( struct != null )
			jmpDefn = struct.getDefn( ).getMember( name );
		else
			jmpDefn = element.getPropertyDefn( name );

		if ( element instanceof ListGroup
				&& ListGroup.GROUP_START_PROP.equalsIgnoreCase( name ) )
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, ListGroup.GROUP_START_PROP );
			state.setName( ListGroup.INTERVAL_BASE_PROP );
			return state;
		}

		if ( jmpDefn != null
				&& ( FilterCondition.OPERATOR_MEMBER.equalsIgnoreCase( jmpDefn
						.getName( ) ) || MapRule.OPERATOR_MEMBER
						.equalsIgnoreCase( jmpDefn.getName( ) ) ) )
		{
			CompatibleOperatorState state = new CompatibleOperatorState(
					handler, element, propDefn, struct );
			state.setName( name );
			return state;
		}

		if ( ( jmpDefn != null ) && ( jmpDefn.getStructDefn( ) != null ) )
		{
			if ( DateTimeFormatValue.FORMAT_VALUE_STRUCT
					.equalsIgnoreCase( jmpDefn.getStructDefn( ).getName( ) )
					|| NumberFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) )
					|| StringFormatValue.FORMAT_VALUE_STRUCT
							.equalsIgnoreCase( jmpDefn.getStructDefn( )
									.getName( ) ) )
			{
				CompatibleFormatPropertyState state = new CompatibleFormatPropertyState(
						handler, element, propDefn, struct );
				state.setName( name );
				state.createStructure( );
				return state;
			}
		}
		if ( "GraphicMasterPage" //$NON-NLS-1$
				.equalsIgnoreCase( element.getDefn( ).getName( ) )
				&& ( name.equalsIgnoreCase( "headerHeight" ) || name //$NON-NLS-1$
						.equalsIgnoreCase( "footerHeight" ) ) ) //$NON-NLS-1$
			return new CompatibleIgnorePropertyState( handler, element );

		return super.jumpTo( );
	}
}