/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a list item.
 * 
 */

public class ListItemState extends ListingItemState {

	/**
	 * Constructs the list state with the design parser handler, the container
	 * element and the container slot of the list.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ListItemState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs list item state with the design parser handler, the container
	 * element and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ListItemState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new ListItem();
		initElement(attrs);
		super.parseAttrs(attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.HEADER_TAG == tagValue)
			return new ListBandState(handler, element, ListItem.HEADER_SLOT);
		if (ParserSchemaConstants.GROUP_TAG == tagValue)
			return new ListGroupState(handler, element, ListItem.GROUP_SLOT);
		if (ParserSchemaConstants.DETAIL_TAG == tagValue)
			return new ListBandState(handler, element, ListItem.DETAIL_SLOT);
		if (ParserSchemaConstants.FOOTER_TAG == tagValue)
			return new ListBandState(handler, element, ListItem.FOOTER_SLOT);
		return super.startElement(tagName);
	}

	/**
	 * Parses the contents of the groups list.
	 */

	static class ListGroupState extends GroupState {

		/**
		 * Constructs the group state with the design parser handler, the container
		 * element and the container slot of the group element.
		 * 
		 * @param handler      the design file parser handler
		 * @param theContainer the element that contains this one
		 * @param slot         the slot in which this element appears
		 */

		public ListGroupState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
			super(handler, theContainer, slot);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
		 * Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			group = new ListGroup();
			super.parseAttrs(attrs);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
		 * String)
		 */

		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.HEADER_TAG == tagValue)
				return new ListBandState(handler, group, ListGroup.HEADER_SLOT);
			if (ParserSchemaConstants.FOOTER_TAG == tagValue)
				return new ListBandState(handler, group, ListGroup.FOOTER_SLOT);
			return super.startElement(tagName);
		}
	}

	/**
	 * Parses the contents of a list band: Header, Footer, ColumnHeader or Detail.
	 */

	static class ListBandState extends ReportElementState {

		/**
		 * Construcuts the state to parse list band.
		 * 
		 * @param handler      the design file parser handler
		 * @param theContainer the element that contains this one
		 * @param slot         the slot in which this element appears
		 */
		public ListBandState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
			super(handler, theContainer, slot);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
		 */

		public DesignElement getElement() {
			return container;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
		 * String)
		 */

		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.TEXT_TAG == tagValue)
				return new TextItemState(handler, container, slotID);
			if (ParserSchemaConstants.GRID_TAG == tagValue)
				return new GridItemState(handler, container, slotID);
			if (ParserSchemaConstants.FREE_FORM_TAG == tagValue)
				return new FreeFormState(handler, container, slotID);
			if (ParserSchemaConstants.LIST_TAG == tagValue)
				return new ListItemState(handler, container, slotID);
			if (ParserSchemaConstants.TABLE_TAG == tagValue)
				return new TableItemState(handler, container, slotID);
			if (ParserSchemaConstants.DATA_TAG == tagValue)
				return new DataItemState(handler, container, slotID);
			if (ParserSchemaConstants.IMAGE_TAG == tagValue)
				return new ImageState(handler, container, slotID);
			if (ParserSchemaConstants.LABEL_TAG == tagValue)
				return new LabelState(handler, container, slotID);
			if (ParserSchemaConstants.INCLUDE_TAG == tagValue)
				return new AnyElementState(handler);
			if (ParserSchemaConstants.TOC_TAG == tagValue)
				return new AnyElementState(handler);
			if (ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue)
				return new ExtendedItemState(handler, container, slotID);
			if (ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue
					|| ParserSchemaConstants.TEXT_DATA_TAG == tagValue)
				return new TextDataItemState(handler, container, slotID);
			if (ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue)
				return new TemplateReportItemState(handler, container, slotID);
			return super.startElement(tagName);
		}

	}
}
