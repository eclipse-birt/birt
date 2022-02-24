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

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentNode;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Parses the property content. The XML file is like:
 * 
 * <pre>
 *                            &lt;property-tag name=&quot;propName&quot;&gt;property value&lt;/property-tag&gt;
 * </pre>
 * 
 * The supported tags are:
 * <ul>
 * <li>property,
 * <li>expression,
 * <li>xml,
 * <li>method,
 * <li>structure,
 * <li>list-property,
 * <li>text-property,
 * <li>html-property
 * </ul>
 * This class parses the "name" attribute and keeps it. Other attributes are
 * parsed by the inherited classes.
 */

public class PropertyContentState extends AbstractParseState {

	/**
	 * The design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * The element holding this property.
	 */

	protected DesignElement element = null;

	/**
	 * The parent node of this content.
	 */

	protected ContentNode parentNode = null;

	/**
	 * The element property name or structure member name.
	 */

	protected String name = null;

	/**
	 * The tag name of this state.
	 */

	protected String tagName = null;

	/**
	 * Status indicates whether this state is valid or not.
	 */

	protected boolean valid = true;

	/**
	 * Attributes map for key/value pairs.
	 */

	protected Map attributes = new LinkedHashMap(ModelUtil.MAP_CAPACITY_LOW);

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this property to parse is a property of one element.
	 * 
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 * @param tagName
	 * @param parent
	 */

	public PropertyContentState(ModuleParserHandler theHandler, DesignElement element, String tagName,
			ContentNode parent) {
		handler = theHandler;
		this.tagName = tagName;
		this.element = element;
		this.parentNode = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public final void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);

		// if name is not specified, throw an error
		name = attrs.getValue(DesignSchemaConstants.NAME_ATTRIB);
		if (StringUtil.isBlank(name)) {
			DesignParserException e = new DesignParserException(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED);
			handler.getErrorHandler().semanticError(e);
			valid = false;
			return;
		}
		for (int i = 0; i < attrs.getLength(); i++) {
			String name = attrs.getQName(i);
			String value = attrs.getValue(i);
			attributes.put(name, value);
		}
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#jumpTo()
	 */

	public final AbstractParseState jumpTo() {
		// If this state can not be parsed properly, any states in it are
		// ignored.

		if (!valid)
			return new AnyElementState(handler);

		AbstractParseState state = null;

		// general jump to

		int maxJump = 5;
		state = generalJumpTo();
		for (int i = 1; i < maxJump; i++) {
			if (state != null && state.jumpTo() != null)
				state = state.jumpTo();
		}
		if (state != null)
			return state;

		// super jump to
		return super.jumpTo();
	}

	/**
	 * Jumps to the specified state that the current state needs to go when some
	 * version controlled condition is satisfied.
	 * 
	 * @return the other state.
	 */

	protected final AbstractParseState generalJumpTo() {
		PropertyDefn defn = element.getPropertyDefn(name);
		if (defn == null)
			return new ContentNodeState(tagName, handler, parentNode, attributes);

		AbstractPropertyState state = null;
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG)) {
			state = new PropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.LIST_PROPERTY_TAG)) {
			state = new ListPropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.EXPRESSION_TAG)) {
			state = new ExpressionState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.XML_PROPERTY_TAG)) {
			state = new XmlPropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG)) {
			state = new StructureState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.METHOD_TAG)) {
			state = new PropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.TEXT_PROPERTY_TAG)) {
			state = new TextPropertyState(handler, element);
			state.setName(name);
			((TextPropertyState) state).setKeyValue((String) attributes.get(DesignSchemaConstants.KEY_ATTRIB));
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.HTML_PROPERTY_TAG)) {
			state = new TextPropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG)) {
			state = new EncryptedPropertyState(handler, element);
			state.setName(name);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG)) {
			state = new SimplePropertyListState(handler, element);
			state.setName(name);
		}

		return state;

	}
}
