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
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a column within a table or grid item.
 * 
 */

class TableColumnState extends ReportElementState
{

	protected TableColumn element = null;

	/**
	 * Constructs the state to parse table column.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TableColumnState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
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
		element = new TableColumn( );
		if ( !addToSlot( container, slotID, element ) )
			return;
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