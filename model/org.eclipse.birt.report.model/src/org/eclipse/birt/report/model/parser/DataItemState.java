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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the Data (data item) tag.
 * 
 */

public class DataItemState extends ReportItemState
{

	/**
	 * The data item being created.
	 */

	public DataItem element;

	/**
	 * Constructs the data item state with the design parser handler, the
	 * container element and the container slot of the data item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public DataItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new DataItem( );
		initElement( attrs );
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

	public void end( )
	{

		// the compatible code for highlight rule move from element to
		// highlightRule structure
		if ( handler.tempValue.get( Style.HIGHLIGHT_RULES_PROP ) != null )
		{
			List highlightRules = element.getListProperty(
					handler.getDesign( ), Style.HIGHLIGHT_RULES_PROP );
			if ( highlightRules != null )
			{

				for ( int i = 0; i < highlightRules.size( ); i++ )
				{
					HighlightRule highlightRule = (HighlightRule) highlightRules
							.get( i );
					highlightRule.setTestExpression( (String) handler.tempValue
							.get( Style.HIGHLIGHT_RULES_PROP ) );
				}
			}
			handler.tempValue.remove( Style.HIGHLIGHT_RULES_PROP );
		}
		if ( handler.tempValue.get( Style.MAP_RULES_PROP ) != null )
		{
			List mapRules = element.getListProperty( handler.getDesign( ),
					Style.MAP_RULES_PROP );
			if ( mapRules != null )
			{

				for ( int i = 0; i < mapRules.size( ); i++ )
				{
					MapRule mapRule = (MapRule) mapRules.get( i );
					mapRule.setTestExpression( (String) handler.tempValue
							.get( Style.MAP_RULES_PROP ) );
				}
			}
			handler.tempValue.remove( Style.MAP_RULES_PROP );

		}
	}
}
