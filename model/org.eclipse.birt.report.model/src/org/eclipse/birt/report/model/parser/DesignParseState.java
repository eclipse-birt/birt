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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Base class for report element parse states.
 * 
 */

public abstract class DesignParseState extends AbstractParseState {

	/**
	 * Pointer to the design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler SAX handler for the design file parser
	 */

	public DesignParseState(ModuleParserHandler theHandler) {
		handler = theHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler() {
		return handler;
	}

	/**
	 * Returns the element being created.
	 * 
	 * @return the report element being created
	 */

	public abstract DesignElement getElement();

	/**
	 * Sets the value of a property with a string parsed from the XML file. Performs
	 * any required semantic checks.
	 * 
	 * @param propName property name
	 * @param value    value string from the XML file
	 */

	protected void setProperty(String propName, String value) {
		// Ensure that the property is defined.

		DesignElement element = getElement();
		ElementPropertyDefn prop = element.getPropertyDefn(propName);
		assert prop != null;

		// Validate the value.

		Object propValue = null;
		try {
			propValue = prop.validateXml(handler.getModule(), element, value);
		} catch (PropertyValueException ex) {
			ex.setElement(element);
			ex.setPropertyName(propName);
			handler.getErrorHandler().semanticError(ex);
			return;
		}
		element.setProperty(propName, propValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.PROPERTY_TAG == tagValue)
			return new PropertyState(handler, getElement());
		if (ParserSchemaConstants.LIST_PROPERTY_TAG == tagValue)
			return new ListPropertyState(handler, getElement());
		if (ParserSchemaConstants.EXPRESSION_TAG == tagValue)
			return new ExpressionState(handler, getElement());
		if (ParserSchemaConstants.XML_PROPERTY_TAG == tagValue)
			return new XmlPropertyState(handler, getElement());
		if (ParserSchemaConstants.STRUCTURE_TAG == tagValue)
			return new StructureState(handler, getElement());
		if (ParserSchemaConstants.METHOD_TAG == tagValue)
			return new PropertyState(handler, getElement());
		if (ParserSchemaConstants.TEXT_PROPERTY_TAG == tagValue)
			return new TextPropertyState(handler, getElement());
		if (ParserSchemaConstants.HTML_PROPERTY_TAG == tagValue)
			return new TextPropertyState(handler, getElement());
		if (ParserSchemaConstants.ENCRYPTED_PROPERTY_TAG == tagValue)
			return new EncryptedPropertyState(handler, getElement());
		if (ParserSchemaConstants.SIMPLE_PROPERTY_LIST_TAG == tagValue)
			return new SimplePropertyListState(handler, getElement());

		return super.startElement(tagName);
	}

	/**
	 * Parses and sets the element id.
	 * 
	 * @param attrs   the SAX attributes object.
	 * @param element the design element.
	 */
	protected void initElementID(Attributes attrs, DesignElement element) {
		try {
			String theID = attrs.getValue(DesignSchemaConstants.ID_ATTRIB);

			if (!StringUtil.isBlank(theID)) {
				// if the id is not null, parse it

				long id = Long.parseLong(theID);
				if (id <= 0) {
					handler.getErrorHandler().semanticError(new DesignParserException(
							new String[] { element.getIdentifier(), attrs.getValue(DesignSchemaConstants.ID_ATTRIB) },
							DesignParserException.DESIGN_EXCEPTION_INVALID_ELEMENT_ID));
				}
				element.setID(id);
			} else {
				// id is empty or null, then add it to the unhandle element
				// list

				handler.unhandleIDElements.add(element);
			}
		} catch (NumberFormatException e) {
			handler.getErrorHandler()
					.semanticError(new DesignParserException(
							new String[] { element.getIdentifier(), attrs.getValue(DesignSchemaConstants.ID_ATTRIB) },
							DesignParserException.DESIGN_EXCEPTION_INVALID_ELEMENT_ID));
		}
	}

	/**
	 * Adds an element to the id-to-element map.
	 * 
	 * @param module  the module.
	 * @param content the design element.
	 * @return <true> if the element can be added to the id-to-element map,
	 *         otherwise return <false>.
	 */
	protected boolean addElementID(Module module, DesignElement content) {
		// Add the item to the element ID map, check whether the id is unique
		// if the element has no ID, we will allocate it in the endDocument
		long elementID = content.getID();

		if (elementID > 0) {
			DesignElement element = module.getElementByID(elementID);

			// the content never add to the container before

			assert element != content;
			if (element == null)
				module.addElementID(content);
			else {
				// fire a semantic warning and make a unique id for it
				handler.getErrorHandler()
						.semanticWarning(new DesignParserException(
								new String[] { content.getIdentifier(), element.getIdentifier() },
								DesignParserException.DESIGN_EXCEPTION_DUPLICATE_ELEMENT_ID));

				// reset id to 0 and add it to unhandled id map
				content.setID(0);
				handler.unhandleIDElements.add(content);
				return true;
			}
		}
		return true;
	}

	protected void markLineNumber(DesignElement element) {
		if (handler.markLineNumber)
			handler.tempLineNumbers.put(element, Integer.valueOf(handler.getCurrentLineNo()));

	}
}