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
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;


/**
 * This class parses a group tag in list or table.
 * 
 */

abstract class GroupState extends ReportElementState
{
	protected GroupElement group = null;
	
	/**
	 * Constructs the group state with the design parser handler, the
	 * container element and the container slot of the group element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public GroupState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */
	
	public DesignElement getElement( )
	{
		return group;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		setProperty( GroupElement.GROUP_NAME_PROP, attrs
				.getValue( DesignSchemaConstants.NAME_ATTRIB ) );
		setProperty( TableGroup.INTERVAL_PROP, attrs
				.getValue( DesignSchemaConstants.INTERVAL_ATTRIB ) );
		setProperty( TableGroup.INTERVAL_RANGE_PROP, attrs
				.getValue( DesignSchemaConstants.INTERVAL_RANGE_ATTRIB ) );
		setProperty( TableGroup.SORT_DIRECTION_PROP, attrs
				.getValue( DesignSchemaConstants.SORT_DIRECTION_ATTRIB ) );

		if ( !addToSlot( container, slotID, group ) )
			return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.KEY_EXPR_TAG ) )
			return new TextState( handler, group, TableGroup.KEY_EXPR_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FILTER_TAG ) )
			return new FiltersState( handler, group,
					GroupElement.FILTER_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.SORT_TAG ) )
			return new SortState( handler, group, GroupElement.SORT_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
			return new TextState( handler, group, TableGroup.TOC_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG ) )
			return new MethodState( handler, getElement( ) );
		return super.startElement( tagName );
	}

}
