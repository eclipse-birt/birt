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

package org.eclipse.birt.report.model.parser.treebuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents an element in the XML file. This is used to parse a xml trunk when
 * the extension is not found or some other config.
 */

public class ContentNode implements Cloneable {

	protected ContentNode parent = null;
	protected List<ContentNode> children = null;
	protected LinkedHashMap<String, Object> attributes = null;
	protected String name = null;
	protected boolean isCDATASection = false;
	protected String value = null;

	/**
	 * Constructs the content node with the name.
	 * 
	 * @param name name of the content node
	 */

	public ContentNode(String name) {
		this.name = name;
		children = new ArrayList<ContentNode>();
		attributes = new LinkedHashMap<String, Object>(ModelUtil.MAP_CAPACITY_LOW);
	}

	/**
	 * Adds one child to this node.
	 * 
	 * @param child the child to add
	 */

	public void addChild(ContentNode child) {
		if (child == null)
			return;
		children.add(child);
		child.parent = this;
	}

	/**
	 * Sets one attribute to this node.
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	/**
	 * Sets all the attributes to the node.
	 * 
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
	}

	/**
	 * Gets the attribute value with the given name.
	 * 
	 * @param name the attribute name to get
	 * @return value with the give name if set, otherwise <code>null</code>
	 */

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Gets the children of this node. Each item in the list is instance of
	 * <code>ContentNode</code>.
	 * 
	 * @return the children
	 */
	public List<ContentNode> getChildren() {
		if (children == null || children.isEmpty())
			return Collections.emptyList();
		return children;
	}

	/**
	 * Gets name of this node.
	 * 
	 * @return name of this node
	 */

	public String getName() {
		return name;
	}

	/**
	 * Gets the attribute map defined in this node.
	 * 
	 * @return the attribute map
	 */

	public Map<String, Object> getAttributes() {
		if (attributes == null || attributes.isEmpty())
			return Collections.emptyMap();
		return attributes;
	}

	/**
	 * @return the value
	 */

	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */

	public void setValue(String value) {
		this.value = StringUtil.trimString(value);
	}

	/**
	 * @return the isCDATASection
	 */
	public boolean isCDATASection() {
		if (DesignSchemaConstants.XML_PROPERTY_TAG.equalsIgnoreCase(name)
				|| DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase(name))
			return true;
		return isCDATASection;
	}

	/**
	 * @param isCDATASection the isCDATASection to set
	 */
	public void setCDATASection(boolean isCDATASection) {
		this.isCDATASection = isCDATASection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		ContentNode clonedNode = (ContentNode) super.clone();

		// clone attribute map
		clonedNode.attributes = new LinkedHashMap<String, Object>(ModelUtil.MAP_CAPACITY_LOW);
		clonedNode.attributes.putAll(attributes);

		// clone children
		if (children != null) {
			clonedNode.children = new ArrayList<ContentNode>();
			for (int i = 0; i < children.size(); i++) {
				ContentNode child = children.get(i);
				ContentNode clonedChild = (ContentNode) child.clone();

				clonedNode.children.add(clonedChild);
				clonedChild.parent = clonedNode;
			}
		}

		return clonedNode;
	}
}
