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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.ITemplateParameterDefinitionModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a template parameter definition within
 * template-parameter-definition slot of report design.
 */

public class TemplateParameterDefinitionState extends ReportElementState {

	/**
	 * The template parameter definition being created.
	 */

	protected TemplateParameterDefinition element = null;

	/**
	 * Constructs the template parameter definition state with the design parser
	 * handler, the container element and the container slot of the template
	 * parameter definition.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public TemplateParameterDefinitionState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
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
		element = new TemplateParameterDefinition();

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
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.DEFAULT_TAG))
			return new DefaultState();
		return super.startElement(tagName);
	}

	/**
	 * This state is related to the 'page-header' & 'page-footer' element of simple
	 * master page. Since the 'page-header' & 'page-footer' look exactly the same
	 * except the parent element tag, only one inner class, PageState, is provided.
	 */
	class DefaultState extends AbstractParseState {

		/**
		 * Constructor.
		 */
		public DefaultState() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
		 */

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
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.TEXT_TAG))
				return new TextItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.LABEL_TAG))
				return new LabelState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.DATA_TAG))
				return new DataItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.LIST_TAG))
				return new ListItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.TABLE_TAG))
				return new TableItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.FREE_FORM_TAG))
				return new FreeFormState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.GRID_TAG))
				return new GridItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.INCLUDE_TAG))
				return new AnyElementState(handler);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.IMAGE_TAG))
				return new ImageState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.LINE_TAG))
				return new LineItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.BROWSER_CONTROL_TAG))
				return new AnyElementState(handler);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.EXTENDED_ITEM_TAG))
				return new ExtendedItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.MULTI_LINE_DATA_TAG)
					|| tagName.equalsIgnoreCase(DesignSchemaConstants.TEXT_DATA_TAG))
				return new TextDataItemState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.SCRIPT_DATA_SET_TAG))
				return new ScriptDataSetState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.ODA_DATA_SET_TAG)
					|| tagName.equalsIgnoreCase("extended-data-set")) //$NON-NLS-1$
			{
				return new OdaDataSetState(handler, element, ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			}
			return super.startElement(tagName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		DesignElement defaultElement = element.getDefaultElement();

		if (defaultElement == null) {
			// the default element of the template parameter definition must not
			// be null

			handler.getErrorHandler().semanticError(new DesignParserException(new String[] { element.getIdentifier() },
					DesignParserException.DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_DEFAULT));
		} else {
			// if allowed type is empty or inconsistent with the default
			// element, then set it to the type of the default element

			String allowedType = element.getAllowedType(handler.getModule());

			if (defaultElement instanceof ReportItem) {
				if (!defaultElement.getElementName().equals(allowedType))
					setProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP, defaultElement.getElementName());
			} else if (defaultElement instanceof SimpleDataSet) {
				if (!DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET.equals(allowedType))
					setProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP,
							DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET);
			} else {
				assert false;
			}

		}
		super.end();
	}
}
