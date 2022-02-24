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
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a cell within a table item.
 * 
 */

public class CellState extends ReportElementState {

	/**
	 * The cell being created.
	 */

	protected Cell element = null;

	/**
	 * Constructs the cell state with the design parser handler, the container
	 * element and the container slot of the cell.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public CellState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new Cell();

		initSimpleElement(attrs);
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
			return new TextItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.AUTO_TEXT_TAG == tagValue)
			return new AutoTextState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.LABEL_TAG == tagValue)
			return new LabelState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.DATA_TAG == tagValue)
			return new DataItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.LIST_TAG == tagValue)
			return new ListItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.TABLE_TAG == tagValue)
			return new TableItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.FREE_FORM_TAG == tagValue)
			return new FreeFormState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.GRID_TAG == tagValue)
			return new GridItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.INCLUDE_TAG == tagValue)
			return new AnyElementState(handler);
		if (ParserSchemaConstants.IMAGE_TAG == tagValue)
			return new ImageState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.LINE_TAG == tagValue)
			return new LineItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.BROWSER_CONTROL_TAG == tagValue)
			return new AnyElementState(handler);
		if (ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue)
			return new ExtendedItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue || ParserSchemaConstants.TEXT_DATA_TAG == tagValue)
			return new TextDataItemState(handler, element, Cell.CONTENT_SLOT);
		if (ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue)
			return new TemplateReportItemState(handler, element, Cell.CONTENT_SLOT);

		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		makeTestExpressionCompatible();
	}

}
