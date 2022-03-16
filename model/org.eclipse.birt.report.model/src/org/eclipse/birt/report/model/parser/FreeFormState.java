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
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * This class parses the Free-form tag( free form element).
 *
 */

public class FreeFormState extends ReportItemState {

	/**
	 * The container being created.
	 */

	protected FreeForm element = null;

	/**
	 * Constructs the free form state with the design parser handler, the container
	 * element and the container slot of the free form.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public FreeFormState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs free-form state with the design parser handler, the container
	 * element and the container property name of the report element.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public FreeFormState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new FreeForm();
		initElement(attrs);
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
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.REPORT_ITEMS_TAG)) {
			return new ReportItemsState();
		}
		return super.startElement(tagName);
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

	/**
	 * Represents the ReportItems tag. This tag contains a list of report items.
	 */

	class ReportItemsState extends AbstractParseState {

		@Override
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

		@Override
		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();

			if (ParserSchemaConstants.LABEL_TAG == tagValue) {
				return new LabelState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.DATA_TAG == tagValue) {
				return new DataItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.TEXT_TAG == tagValue) {
				return new TextItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.IMAGE_TAG == tagValue) {
				return new ImageState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.LINE_TAG == tagValue) {
				return new LineItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.RECTANGLE_TAG == tagValue) {
				return new AnyElementState(handler);
			}
			if (ParserSchemaConstants.GRID_TAG == tagValue) {
				return new GridItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.BROWSER_CONTROL_TAG == tagValue) {
				return new AnyElementState(handler);
			}
			if (ParserSchemaConstants.LIST_TAG == tagValue) {
				return new ListItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.TABLE_TAG == tagValue) {
				return new TableItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.INCLUDE_TAG == tagValue) {
				return new AnyElementState(handler);
			}
			if (ParserSchemaConstants.FREE_FORM_TAG == tagValue) {
				return new FreeFormState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue) {
				return new ExtendedItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue
					|| ParserSchemaConstants.TEXT_DATA_TAG == tagValue) {
				return new TextDataItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			if (ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue) {
				return new TemplateReportItemState(handler, element, FreeForm.REPORT_ITEMS_SLOT);
			}
			return super.startElement(tagName);
		}
	}

}
