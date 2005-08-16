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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Parses the "expression" tag. If the element property or structure member is
 * expression type, then we will use the expression not the property tag.
 */

class ExpressionState extends PropertyState
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, )
	 */

	public AbstractParseState jumpTo( )
	{
		if ( !valid )
			return new AnyElementState( getHandler( ) );

		if ( ( element instanceof TextDataItem )
				&& "contentTypeExpr".equalsIgnoreCase( name ) ) //$NON-NLS-1$
		{
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(
					handler, element, "contentTypeExpr" ); //$NON-NLS-1$
			state.setName( TextDataItem.CONTENT_TYPE_PROP );
			return state;
		}

		if ( ( ( element instanceof StyleElement )
				|| ( element instanceof DataItem ) || ( element instanceof TableItem ) )
				&& "highlightTestExpr".equalsIgnoreCase( name ) ) //$NON-NLS-1$ //$NON-NLS-2$
		{
			if ( handler.isVersion( "0" ) || handler.isVersion( "1" ) ) //$NON-NLS-1$//$NON-NLS-2$
				return new CompatibleTestExpreState( handler, element, Style.HIGHLIGHT_RULES_PROP);
		}
		if ( ( ( element instanceof StyleElement )
				|| ( element instanceof DataItem ) || ( element instanceof TableItem ) )
				&& ( "mapTestExpr".equalsIgnoreCase( name ) ) ) //$NON-NLS-1$
			return new CompatibleTestExpreState( handler, element, Style.MAP_RULES_PROP );

		return super.jumpTo( );
	}

	ExpressionState( ModuleParserHandler theHandler, DesignElement element )
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

	ExpressionState( ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element, propDefn, struct );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		super.parseAttrs( attrs );
	}

}
