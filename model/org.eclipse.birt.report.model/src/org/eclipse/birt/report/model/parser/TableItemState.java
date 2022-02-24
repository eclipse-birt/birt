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

import org.eclipse.birt.report.model.api.elements.table.BasicLayoutStrategies;
import org.eclipse.birt.report.model.api.elements.table.LayoutTable;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Table (table item) tag.
 * 
 */

public class TableItemState extends ListingItemState {

	/**
	 * Constructs the table item state with the design parser handler, the container
	 * element and the container slot of the table item.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public TableItemState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs table item state with the design parser handler, the container
	 * element and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public TableItemState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
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
		element = new TableItem();
		initElement(attrs);
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
		if (ParserSchemaConstants.COLUMN_TAG == tagValue)
			return new TableColumnState(handler, element, TableItem.COLUMN_SLOT);
		if (ParserSchemaConstants.HEADER_TAG == tagValue)
			return new TableBandState(handler, element, TableItem.HEADER_SLOT);
		if (ParserSchemaConstants.GROUP_TAG == tagValue)
			return new TableGroupState(handler, element, TableItem.GROUP_SLOT);
		if (ParserSchemaConstants.DETAIL_TAG == tagValue)
			return new TableBandState(handler, element, TableItem.DETAIL_SLOT);
		if (ParserSchemaConstants.FOOTER_TAG == tagValue)
			return new TableBandState(handler, element, TableItem.FOOTER_SLOT);
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		super.end();

		assert element instanceof TableItem;

		LayoutTable layoutTable = ((TableItem) element).getLayoutModel(handler.getModule());
		BasicLayoutStrategies.appliesStrategies(layoutTable, false);

		setCompatibleOnRowMethod();
	}

	/**
	 * Sets onRow method to detail rows' onCreate method.
	 */

	private void setCompatibleOnRowMethod() {
		String onRowValue = (String) handler.tempValue.get(element);
		if (onRowValue == null)
			return;

		ContainerSlot detail = element.getSlot(IListingElementModel.DETAIL_SLOT);
		for (int i = 0; i < detail.getCount(); i++) {
			TableRow row = (TableRow) detail.getContent(i);

			// if onCreate property value is null, then set the compatible
			// value. Otherwise not.

			if (row.getLocalProperty(handler.getModule(), ITableRowModel.ON_CREATE_METHOD) == null)
				row.setProperty(ITableRowModel.ON_CREATE_METHOD, onRowValue);
		}
	}

	/**
	 * Parses the contents of the list of TableGroup.
	 */

	static class TableGroupState extends GroupState {

		/**
		 * Constructs the group state with the design parser handler, the container
		 * element and the container slot of the group element.
		 * 
		 * @param handler      the design file parser handler
		 * @param theContainer the element that contains this one
		 * @param slot         the slot in which this element appears
		 */

		public TableGroupState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
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
			group = new TableGroup();
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
				return new TableBandState(handler, group, TableGroup.HEADER_SLOT);
			if (ParserSchemaConstants.FOOTER_TAG == tagValue)
				return new TableBandState(handler, group, TableGroup.FOOTER_SLOT);
			return super.startElement(tagName);
		}

	}

	/**
	 * Parses the contents of the list of table bands.
	 */

	static class TableBandState extends SlotState {

		TableBandState(ModuleParserHandler handler, DesignElement container, int slot) {
			super(handler, container, slot);
		}

		public XMLParserHandler getHandler() {
			return handler;
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
			if (ParserSchemaConstants.ROW_TAG == tagValue)
				return new TableRowState(handler, container, slotID);
			return super.startElement(tagName);
		}

	}
}
