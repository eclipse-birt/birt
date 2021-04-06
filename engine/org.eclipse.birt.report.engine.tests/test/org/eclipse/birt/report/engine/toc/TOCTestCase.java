/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

abstract public class TOCTestCase extends EngineCase {

	protected void createEntry(TOCBuilder builder, TOCEntry parent, String name, String hiddenFormats) {
		TOCEntry entry = startEntry(builder, parent, name, hiddenFormats);
		closeEntry(builder, entry);
	}

	protected void createEntry(TOCBuilder builder, TOCEntry parent, String name) {
		TOCEntry entry = startEntry(builder, parent, name);
		closeEntry(builder, entry);
	}

	protected TOCEntry startEntry(TOCBuilder builder, TOCEntry parent, String name) {
		return builder.startEntry(parent, name, null, -1);
	}

	protected TOCEntry startEntry(TOCBuilder builder, TOCEntry parent, String name, String hiddenFormats) {
		return builder.startEntry(parent, name, null, hiddenFormats, -1);
	}

	protected void closeEntry(TOCBuilder builder, TOCEntry entry) {
		builder.closeEntry(entry);
	}

	protected TOCEntry startGroupEntry(TOCBuilder builder, TOCEntry parent, String name) {
		return builder.startGroupEntry(parent, name, null, null, -1);
	}

	protected TOCEntry startGroupEntry(TOCBuilder builder, TOCEntry parent, String name, String hiddenFormats) {
		return builder.startGroupEntry(parent, name, null, hiddenFormats, -1);
	}

	protected void closeGroupEntry(TOCBuilder builder, TOCEntry entry) {
		builder.closeEntry(entry);
	}

	protected TOCEntry startDummyEntry(TOCBuilder builder, TOCEntry parent, String hiddenFormats) {
		return builder.startDummyEntry(parent, hiddenFormats);
	}

	/**
	 * create the expected toc tree
	 * 
	 * @return
	 */
	protected ITreeNode createTree() {
		TOCBuilder builder = new TOCBuilder();

		TOCEntry reportHeader = startEntry(builder, null, "report-header");
		closeEntry(builder, reportHeader);

		TOCEntry list = startEntry(builder, null, "list");

		{
			TOCEntry listHeader = startEntry(builder, list, "list-header");
			closeEntry(builder, listHeader);

			TOCEntry group = startGroupEntry(builder, list, "group");
			{
				TOCEntry group21Header = startEntry(builder, group, "list-group-header");
				closeEntry(builder, group21Header);

				TOCEntry detail = startEntry(builder, group, "detail");
				closeEntry(builder, detail);

				TOCEntry group21Footer = startEntry(builder, group, "group-footer");
				closeEntry(builder, group21Footer);
			}
			closeGroupEntry(builder, group);

			TOCEntry listFooter = startEntry(builder, list, "list-footer");
			closeEntry(builder, listFooter);
		}
		closeEntry(builder, list);

		TOCEntry footer = startEntry(builder, null, "footer");
		closeEntry(builder, footer);

		return builder.getTOCTree();
	}

	String toString(ITreeNode node) {
		StringBuffer buffer = new StringBuffer();
		outputTreeNode(buffer, node, 0);
		return buffer.toString();
	}

	void outputTreeNode(StringBuffer buffer, ITreeNode node, int level) {
		String nodeId = node.getNodeId();
		String bookmark = node.getBookmark();
		long elementId = node.getElementId();
		String hiddenFormats = node.getHiddenFormats();
		Object tocValue = node.getTOCValue();
		boolean isGroup = node.isGroup();

		indent(buffer, level);
		buffer.append("<");
		buffer.append(isGroup ? "group" : "entry");
		buffer.append(" nodeId=\"");
		buffer.append(nodeId);
		buffer.append("\"");
		if (tocValue != null) {
			buffer.append(" tocValue=\"");
			buffer.append(tocValue);
			buffer.append("\"");
		}
		if (bookmark != null && !nodeId.equals(bookmark)) {
			buffer.append(" bookmark=\"");
			buffer.append(bookmark);
			buffer.append("\"");
		}
		if (elementId != -1) {
			buffer.append(" elementId=\"");
			buffer.append(elementId);
			buffer.append("\"");
		}
		if (hiddenFormats != null) {
			buffer.append(" hiddenFormats=\"");
			buffer.append(hiddenFormats);
			buffer.append("\"");
		}

		Collection<ITreeNode> children = node.getChildren();
		if (!children.isEmpty()) {
			buffer.append(">");
			buffer.append("\r\n");
			for (ITreeNode child : children) {
				outputTreeNode(buffer, child, level + 1);
			}
			indent(buffer, level);
			buffer.append("</");
			buffer.append(isGroup ? "group" : "entry");
			buffer.append(">");
		} else {
			buffer.append("/>");
		}
		buffer.append("\r\n");
	}

	String toString(TOCNode node) {
		StringBuffer buffer = new StringBuffer();
		outputTOCNode(buffer, node, 0, false);
		return buffer.toString();
	}

	String toStringWithStyle(TOCNode node) {
		StringBuffer buffer = new StringBuffer();
		outputTOCNode(buffer, node, 0, true);
		return buffer.toString();
	}

	void outputTOCNode(StringBuffer buffer, TOCNode node, int level, boolean withStyle) {
		String nodeId = node.getNodeID();
		String displayText = node.getDisplayString();
		String bookmark = node.getBookmark();
		IScriptStyle style = node.getTOCStyle();

		indent(buffer, level);
		buffer.append("<toc");
		buffer.append(" nodeId=\"");
		buffer.append(nodeId);
		buffer.append("\"");
		if (displayText != null) {
			buffer.append(" displayText=\"");
			buffer.append(displayText);
			buffer.append("\"");
		}
		if (bookmark != null && !nodeId.equals(bookmark)) {
			buffer.append(" bookmark=\"");
			buffer.append(bookmark);
			buffer.append("\"");
		}
		if (withStyle) {
			if (style != null) {
				buffer.append(" style=\"");
				buffer.append(style.toString());
				buffer.append("\"");
			}
		}

		List children = node.getChildren();
		if (!children.isEmpty()) {
			buffer.append(">");
			buffer.append("\r\n");
			for (int i = 0; i < children.size(); i++) {
				TOCNode child = (TOCNode) children.get(i);
				outputTOCNode(buffer, child, level + 1, withStyle);
			}
			indent(buffer, level);
			buffer.append("</toc>");
		} else {
			buffer.append("/>");
		}
		buffer.append("\r\n");
	}

	private void indent(StringBuffer buffer, int level) {
		for (int i = 0; i < level; i++) {
			buffer.append("    ");
		}
	}

}
