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

import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.parser.treebuild.ContentNode;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * State to parse a trunk of XML file. Store them to a content string structure
 * rather than the layout structure of design elements.
 */

public class ContentNodeState extends AbstractParseState {

	/**
	 * Status indicates whether this text is a CDATA section or not.
	 */

	protected boolean isCDataSection = false;

	/**
	 * The design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * Parent node of this content state.
	 */

	protected ContentNode parentNode = null;

	/**
	 * Current node to parse.
	 */

	protected ContentNode node = null;

	/**
	 * The tag name of this node.
	 */
	protected String nodeName = null;

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this property to parse is a property of one element.
	 * 
	 * @param tagName    the tag name of this state
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 * @param parent
	 */

	public ContentNodeState(String tagName, ModuleParserHandler theHandler, ContentNode parent) {
		this.nodeName = tagName;
		handler = theHandler;
		this.parentNode = parent;
	}

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this property to parse is a property of one element.
	 * 
	 * @param tagName    the tag name of this state
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 * @param parent
	 * @param attributes
	 */

	public ContentNodeState(String tagName, ModuleParserHandler theHandler, ContentNode parent,
			Map<String, Object> attributes) {
		this.nodeName = tagName;
		handler = theHandler;
		this.parentNode = parent;
		node = new ContentNode(nodeName);
		node.setAttributes(attributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler() {
		return this.handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);
		node = new ContentNode(nodeName);

		initAttrs(attrs);
	}

	/**
	 * Sets all the attributes to the node.
	 * 
	 * @param attrs the attributes to set
	 */

	private void initAttrs(Attributes attrs) {
		for (int i = 0; i < attrs.getLength(); i++) {
			String name = attrs.getQName(i);
			String value = attrs.getValue(i);
			node.setAttribute(name, value);
		}

		ElementDefn elementDefn = (ElementDefn) MetaDataDictionary.getInstance().getElementByXmlName(node.getName());

		if (elementDefn != null) {
			String name = (String) node.getAttribute(DesignSchemaConstants.NAME_ATTRIB);
			String idString = (String) node.getAttribute(DesignSchemaConstants.ID_ATTRIB);

			// handler name
			if (!StringUtil.isBlank(name))
				handler.module.getNameHelper().addContentName(elementDefn.getNameSpaceID(), name);
			// handler id
			if (!StringUtil.isBlank(idString)) {
				try {
					long id = Long.parseLong(idString);
					if (id > DesignElement.NO_ID)
						handler.module.addElementID(id);
				} catch (NumberFormatException e) {
					// do nothing
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		return new ContentNodeState(tagName, handler, node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		node.setValue(text.toString());
		node.setCDATASection(isCDataSection);
		parentNode.addChild(node);
		super.end();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#setIsCDataSection
	 * (boolean)
	 */

	public void setIsCDataSection(boolean isCDataSection) {
		this.isCDataSection = isCDataSection;
	}

}
