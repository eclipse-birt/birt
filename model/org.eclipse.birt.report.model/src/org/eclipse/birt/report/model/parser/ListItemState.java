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
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a list item.
 * 
 */

public class ListItemState extends ListingItemState
{

	/**
	 * Constructs the list state with the design parser handler, the container
	 * element and the container slot of the list.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ListItemState( ModuleParserHandler handler,
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
		element = new ListItem( );
		initElement( attrs );
		super.parseAttrs( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HEADER_TAG ) )
			return new ListBandState( handler, element,
					IListingElementModel.HEADER_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GROUP_TAG ) )
			return new ListGroupState( handler, element,
					IListingElementModel.GROUP_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DETAIL_TAG ) )
			return new ListBandState( handler, element,
					IListingElementModel.DETAIL_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FOOTER_TAG ) )
			return new ListBandState( handler, element,
					IListingElementModel.FOOTER_SLOT );
		return super.startElement( tagName );
	}

	/**
	 * Parses the contents of the groups list.
	 */

	class ListGroupState extends GroupState
	{

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

		public ListGroupState( ModuleParserHandler handler,
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
			group = new ListGroup( );
			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HEADER_TAG ) )
				return new ListBandState( handler, group,
						IGroupElementModel.HEADER_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FOOTER_TAG ) )
				return new ListBandState( handler, group,
						IGroupElementModel.FOOTER_SLOT );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of a list band: Header, Footer, ColumnHeader or
	 * Detail.
	 */

	class ListBandState extends ReportElementState
	{

		/**
		 * Construcuts the state to parse list band.
		 * 
		 * @param handler
		 *            the design file parser handler
		 * @param theContainer
		 *            the element that contains this one
		 * @param slot
		 *            the slot in which this element appears
		 */
		public ListBandState( ModuleParserHandler handler,
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
			return container;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
				return new GridItemState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
				return new FreeFormState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_TAG ) )
				return new ListItemState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
				return new TableItemState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
				return new DataItemState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
				return new LabelState( handler, container, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new ExtendedItemState( handler, container, slotID );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
					|| tagName
							.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
				return new TextDataItemState( handler, container, slotID );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.TEMPLATE_REPORT_ITEM_TAG ) )
				return new TemplateReportItemState( handler, container, slotID );
			return super.startElement( tagName );
		}

	}
}