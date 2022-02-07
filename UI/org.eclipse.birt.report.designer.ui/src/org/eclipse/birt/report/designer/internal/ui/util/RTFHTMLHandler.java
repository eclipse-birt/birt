/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

/**
 * 
 */

public class RTFHTMLHandler implements RTFDocumentHandler {

	private HTMLNode rootNode;
	private HTMLNode parentNode;
	private HTMLNode currentNode;
	private Stack<HTMLNode> nodeStack = new Stack<HTMLNode>();

	private static class HTMLNode {

		private String name;
		private String content;
		private HashMap<String, String> attributes = new HashMap<String, String>();
		private List<HTMLNode> childNodes = new ArrayList<HTMLNode>();
		private HTMLNode parent;

		public HTMLNode name(String name) {
			this.name = name;
			return this;
		}

		public String getContent() {
			return content;
		}

		public HTMLNode attribute(String name, String value) {
			this.attributes.put(name, value);
			return this;
		}

		public HTMLNode content(String content) {
			this.content = content;
			return this;
		}

		public void setChildNodes(List<HTMLNode> childNodes) {
			this.childNodes = childNodes;
		}

		public HTMLNode parent(HTMLNode parent) {
			parent.child(this);
			return parent;
		}

		public HTMLNode child(HTMLNode child) {
			child.parent = this;
			this.childNodes.add(child);
			return child;
		}

		public HTMLNode delete(HTMLNode child) {
			this.childNodes.remove(child);
			return child;
		}

		public List<HTMLNode> getChildren() {
			return Collections.unmodifiableList(childNodes);
		}

		public HashMap<String, String> getAttributes() {
			return attributes;
		}

		public String toString() {
			return this.name;
		}
	}

	public RTFHTMLHandler() {
		this.rootNode = this.parentNode = new HTMLNode().name("div");
	}

	public void startElement(String name, AttributeSet attributeSet) {
		this.currentNode = new HTMLNode().name(name.equalsIgnoreCase("paragraph") ? "div" : name);
		this.currentNode.parent(this.parentNode);
		this.nodeStack.add(this.currentNode);
		this.parentNode = this.currentNode;

		Object fontfamily = attributeSet.getAttribute(StyleConstants.FontFamily);
		Object fontsize = attributeSet.getAttribute(StyleConstants.FontSize);
		Object fontcolor = attributeSet.getAttribute(StyleConstants.Foreground);
		if (fontfamily != null || fontsize != null || fontcolor != null) {
			HTMLNode fontnode = new HTMLNode().name("font");
			if (fontfamily != null)
				fontnode.attribute("face", fontfamily.toString());
			if (fontcolor != null) {
				Color color = (Color) fontcolor;
				fontnode.attribute("color", makeColorString(color));
			}
			if (fontsize != null) {
				int size = ((Integer) fontsize).intValue();
				fontnode.attribute("size", size / 4 + "");
			}
			this.currentNode = this.currentNode.child(fontnode);
		}
		Object italic = attributeSet.getAttribute(StyleConstants.Italic);
		if (italic != null && ((Boolean) italic).booleanValue()) {
			this.currentNode = this.currentNode.child(new HTMLNode().name("i"));
		}
		Object underline = attributeSet.getAttribute(StyleConstants.Underline);
		if (underline != null && ((Boolean) underline).booleanValue()) {
			this.currentNode = this.currentNode.child(new HTMLNode().name("u"));
		}
		Object bold = attributeSet.getAttribute(StyleConstants.Bold);
		if (bold != null && ((Boolean) bold).booleanValue()) {
			this.currentNode = this.currentNode.child(new HTMLNode().name("b"));
		}
	}

	public void content(String content) {
		content = content.replaceAll("\\t", getSpaceTab(8));
		content = content.replaceAll("\\n", "<br/>");
		this.currentNode.content(content);
	}

	public void endElement(String name) {
		this.parentNode = this.nodeStack.pop().parent;
	}

	private String makeColorString(Color color1) {
		String s = Long.toString(color1.getRGB() & 0xffffff, 16);
		if (s.length() < 6) {
			StringBuffer stringbuffer = new StringBuffer();
			for (int i = s.length(); i < 6; i++) {
				stringbuffer.append("0");
			}
			stringbuffer.append(s);
			s = stringbuffer.toString();
		}
		return s;
	}

	public String toHTML() {
		StringBuffer sb = new StringBuffer();
		trimRootNode(rootNode);
		unionRootNode(rootNode);
		serializeHTMLNode(this.rootNode, sb, 0);
		return sb.toString();
	}

	private void trimRootNode(HTMLNode rootNode) {
		List<HTMLNode> nodes = rootNode.getChildren();
		List<HTMLNode> temps = new ArrayList<HTMLNode>(nodes);
		for (int i = 0; i < nodes.size(); i++) {
			HTMLNode node = nodes.get(i);
			if (isBlankNode(node)) {
				temps.remove(node);
			}
		}
		rootNode.setChildNodes(temps);
		nodes = rootNode.getChildren();
		for (int i = 0; i < temps.size(); i++) {
			HTMLNode node = nodes.get(i);
			trimRootNode(nodes.get(i));
		}
	}

	private boolean isBlankNode(HTMLNode node) {
		boolean bool = false;
		if (node.getChildren().size() == 0) {
			bool = true;
		} else if (node.getChildren().size() == 1) {
			if (isBlankNode(node.getChildren().get(0))) {
				bool = true;
			} else {
				bool = false;
			}
		} else {
			bool = false;
		}
		return node.getAttributes().size() == 0 && node.getContent() == null && bool;
	}

	private void unionRootNode(HTMLNode rootNode) {
		List<HTMLNode> nodes = rootNode.getChildren();
		if (nodes.size() == 1) {
			HTMLNode first = nodes.get(0);
			if (rootNode.getAttributes().size() == 0 && rootNode.getContent() == null
					&& rootNode.name.equals(first.name)) {
				List<HTMLNode> temp = first.getChildren();
				rootNode.delete(first);
				for (int i = 0; i < temp.size(); i++) {
					rootNode.child(temp.get(i));
				}
				unionRootNode(rootNode);
			}
		}

		nodes = rootNode.getChildren();
		for (int i = 0; i < nodes.size(); i++) {
			unionRootNode(nodes.get(i));
		}
	}

	private void serializeHTMLNode(HTMLNode node, StringBuffer sb, int indent) {
		if (node.name.equals("content")) {
			if (node.childNodes.size() == 0) {
				if (node.content != null) {
					if (indent > 0)
						sb.append("\n");
					for (int i = 0; i < indent; i++) {
						sb.append("\t");
					}
					sb.append(node.content);
				}
			}
			for (HTMLNode childNode : node.childNodes)
				serializeHTMLNode(childNode, sb, indent);
			return;
		}
		if (indent > 0)
			sb.append("\n");
		for (int i = 0; i < indent; i++) {
			sb.append("\t");
		}
		sb.append("<").append(node.name);
		for (String key : node.attributes.keySet())
			sb.append(" ").append(key).append("=\"").append(node.attributes.get(key)).append("\"");
		sb.append(">");

		for (HTMLNode childNode : node.childNodes)
			serializeHTMLNode(childNode, sb, indent + 1);

		if (node.content != null) {
			sb.append(node.content);
		}

		if (node.name.equals("div")) {
			sb.append("\n");
			for (int i = 0; i < indent; i++) {
				sb.append("\t");
			}
		}
		sb.append("</").append(node.name).append(">");
	}

	private String getSpaceTab(int i) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int j = 0; j < i; j++) {
			stringbuffer.append("&nbsp;");
		}
		return stringbuffer.toString();
	}
}
