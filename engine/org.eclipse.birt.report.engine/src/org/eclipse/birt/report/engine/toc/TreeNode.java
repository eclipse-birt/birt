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

package org.eclipse.birt.report.engine.toc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.core.util.IOUtil;

abstract public class TreeNode implements ITreeNode {

	/**
	 * identifier for the node
	 */
	private String nodeId;

	/**
	 * A bookmark that is stored for the TOC, it is usually equals to the node Id.
	 */
	private String bookmark;

	/**
	 * if the TOC is created by a group element.
	 */
	private boolean isGroup;

	/**
	 * the TOC is invisible in the defined formats.
	 */
	private String hiddenFormats;

	/**
	 * Value of TOC.
	 */
	private Object tocValue;

	/**
	 * the id of the design which generates the TOC
	 */
	private long elementId = -1;

	public TreeNode() {
	}

	public TreeNode(TreeNode node) {
		this.nodeId = node.nodeId;
		this.bookmark = node.bookmark;
		this.isGroup = node.isGroup;
		this.hiddenFormats = node.hiddenFormats;
		this.tocValue = node.tocValue;
		this.elementId = node.elementId;
	}

	abstract public Collection<ITreeNode> getChildren();

	public String getNodeId() {
		return nodeId;
	}

	public String getBookmark() {
		return bookmark;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public String getHiddenFormats() {
		return hiddenFormats;
	}

	public Object getTOCValue() {
		return tocValue;
	}

	public long getElementId() {
		return elementId;
	}

	public void setNodeId(String id) {
		this.nodeId = id;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public void setHiddenFormats(String format) {
		this.hiddenFormats = format;
	}

	public void setTOCValue(Object value) {
		this.tocValue = value;
	}

	public void setElementId(long elementId) {
		this.elementId = elementId;
	}

	protected static final short FIELD_NONE = -1;
	protected static final short FIELD_NODE_ID = 1;
	protected static final short FIELD_BOOKMARK = 2;
	protected static final short FIELD_TOC_VALUE = 3;
	protected static final short FIELD_HIDDEN_FORMATS = 4;
	protected static final short FIELD_ELEMENT_ID = 5;
	protected static final short FIELD_GROUP = 6;

	/**
	 * read the node out from an input stream.
	 * 
	 * @param in     input stream to be read.
	 * @param loader the class loader used to load the values
	 * @throws IOException
	 */
	public void readNode(DataInputStream in, ClassLoader loader) throws IOException {
		int field = IOUtil.readShort(in);
		while (field != FIELD_NONE) {
			switch (field) {
			case FIELD_NODE_ID:
				nodeId = IOUtil.readString(in);
				// the book-mark will be override by following book-mark
				// field
				bookmark = nodeId;
				break;
			case FIELD_BOOKMARK:
				bookmark = IOUtil.readString(in);
				break;
			case FIELD_TOC_VALUE:
				tocValue = IOUtil.readObject(in, loader);
				break;
			case FIELD_HIDDEN_FORMATS:
				hiddenFormats = IOUtil.readString(in);
				break;
			case FIELD_ELEMENT_ID:
				elementId = IOUtil.readLong(in);
				break;
			case FIELD_GROUP:
				isGroup = IOUtil.readBool(in);
				break;
			default:
				throw new IOException("undefined toc filed:" + field);
			}
			field = IOUtil.readShort(in);
		}
	}

	/**
	 * write the node out to an output stream.
	 * 
	 * @param out the output stream used to save the node.
	 * @throws IOException
	 */
	public void writeNode(DataOutputStream out) throws IOException {
		IOUtil.writeShort(out, FIELD_NODE_ID);
		IOUtil.writeString(out, nodeId);
		if (!nodeId.equals(bookmark)) {
			IOUtil.writeShort(out, FIELD_BOOKMARK);
			IOUtil.writeString(out, bookmark);
		}
		if (tocValue != null) {
			IOUtil.writeShort(out, FIELD_TOC_VALUE);
			IOUtil.writeObject(out, tocValue);
		}
		if (hiddenFormats != null) {
			IOUtil.writeShort(out, FIELD_HIDDEN_FORMATS);
			IOUtil.writeString(out, hiddenFormats);
		}
		if (isGroup) {
			IOUtil.writeShort(out, FIELD_GROUP);
			IOUtil.writeBool(out, isGroup);
		}
		IOUtil.writeShort(out, FIELD_ELEMENT_ID);
		IOUtil.writeLong(out, elementId);
		IOUtil.writeShort(out, FIELD_NONE);
	}
}
