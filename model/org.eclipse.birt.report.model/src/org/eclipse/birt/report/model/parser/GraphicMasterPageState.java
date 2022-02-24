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

import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * This class parses a graphic master page.
 * 
 */

public class GraphicMasterPageState extends MasterPageState {

	/**
	 * Constructs the graphic master page state with the design file parser handler.
	 * 
	 * @param handler the design file parser handler
	 */

	public GraphicMasterPageState(ModuleParserHandler handler) {
		super(handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new GraphicMasterPage();
		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */

	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.CONTENTS_TAG))
			return new ContentsState();
		return super.startElement(tagName);
	}

	/**
	 * Parses the list of "page decoration" items on the master page itself.
	 */

	class ContentsState extends AbstractParseState {

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
			if (ParserSchemaConstants.LABEL_TAG == tagValue)
				return new LabelState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.DATA_TAG == tagValue)
				return new DataItemState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.TEXT_TAG == tagValue)
				return new TextItemState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.IMAGE_TAG == tagValue)
				return new ImageState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.LINE_TAG == tagValue)
				return new LineItemState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.RECTANGLE_TAG == tagValue)
				return new RectangleState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.GRID_TAG == tagValue)
				return new GridItemState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.BROWSER_CONTROL_TAG == tagValue)
				return new AnyElementState(handler);
			if (ParserSchemaConstants.FREE_FORM_TAG == tagValue)
				return new FreeFormState(handler, element, GraphicMasterPage.CONTENT_SLOT);
			if (ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue)
				return new AnyElementState(handler);
			return super.startElement(tagName);
		}
	}

}
