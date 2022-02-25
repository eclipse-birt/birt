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
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a row within a table item.
 *
 */

public class TableRowState extends ReportElementState {

	/**
	 * The row being created.
	 */

	protected TableRow element;

	/**
	 * Constructs the table or list row state with the design parser handler, the
	 * container element and the container slot of the table row.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public TableRowState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new TableRow();

		initSimpleElement(attrs);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */

	@Override
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.CELL_TAG == tagValue) {
			return new CellState(handler, element, TableRow.CONTENT_SLOT);
		}
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	@Override
	public void end() throws SAXException {
		makeTestExpressionCompatible();
	}
}
