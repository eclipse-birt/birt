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

import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a simple master page.
 * 
 */

public class SimpleMasterPageState extends MasterPageState
{

	/**
	 * Constructs the simple master page with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public SimpleMasterPageState( ModuleParserHandler handler )
	{
		super( handler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new SimpleMasterPage( );
		initElement( attrs, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		int tagValue = tagName.toLowerCase( ).hashCode( );
		if ( ParserSchemaConstants.PAGE_HEADER_TAG == tagValue )
			return new PageState( SimpleMasterPage.PAGE_HEADER_SLOT );
		if ( ParserSchemaConstants.PAGE_FOOTER_TAG == tagValue )
			return new PageState( SimpleMasterPage.PAGE_FOOTER_SLOT );
		return super.startElement( tagName );
	}

	/**
	 * This state is related to the 'page-header' & 'page-footer' element of
	 * simple master page. Since the 'page-header' & 'page-footer' look exactly
	 * the same except the parent element tag, only one inner class, PageState,
	 * is provided.
	 */
	class PageState extends SlotState
	{

		/**
		 * Constructor
		 * 
		 * @param slot
		 *            the slot.
		 */
		public PageState( int slot )
		{

			super( SimpleMasterPageState.this.handler,
					SimpleMasterPageState.this.element, slot );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			// MasterPage slot can contain any report item is not variable size
			// or is bound to data, such as Data, Label, Text, Grid, Image.

			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.TEXT_TAG == tagValue )
				return new TextItemState( handler, element, slotID );
			else if ( ParserSchemaConstants.GRID_TAG == tagValue )
				return new GridItemState( handler, element, slotID );
			else if ( ParserSchemaConstants.FREE_FORM_TAG == tagValue )
				return new FreeFormState( handler, element, slotID );
			else if ( ParserSchemaConstants.LABEL_TAG == tagValue )
				return new LabelState( handler, element, slotID );
			else if ( ParserSchemaConstants.IMAGE_TAG == tagValue )
				return new ImageState( handler, element, slotID );
			else if ( ParserSchemaConstants.DATA_TAG == tagValue )
				return new DataItemState( handler, element, slotID );
			else if ( ParserSchemaConstants.TEXT_DATA_TAG == tagValue )
				return new TextDataItemState( handler, element, slotID );
			else if ( ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue )
				return new TemplateReportItemState( handler, element, slotID );
			else if ( ParserSchemaConstants.AUTO_TEXT_TAG == tagValue )
				return new AutoTextState( handler, element, slotID );
			return super.startElement( tagName );
		}
	}
}