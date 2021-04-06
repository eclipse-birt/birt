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

package org.eclipse.birt.report.engine.toc.document;

import org.eclipse.birt.report.engine.toc.ITOCConstants;
import org.eclipse.birt.report.engine.toc.ITOCWriter;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCEntry;

public class MemTOCWriter implements ITOCWriter, ITOCConstants {

	private MemTreeNode rootNode;

	/**
	 * @param tocTree the root for the TOC tree
	 */
	public MemTOCWriter() {
		rootNode = new MemTreeNode();
		rootNode.setNodeId("/");
	}

	public void close() {
	}

	public ITreeNode getTree() {
		return rootNode;
	}

	public void startTOCEntry(TOCEntry entry) {
		MemTreeNode treeNode = new MemTreeNode(entry);
		entry.setTreeNode(treeNode);
		MemTreeNode parentTreeNode = getParentTreeNode(entry);
		parentTreeNode.addChild(treeNode);
		return;
	}

	MemTreeNode getParentTreeNode(TOCEntry entry) {
		TOCEntry parent = entry.getParent();
		if (parent != null) {
			return (MemTreeNode) parent.getTreeNode();
		}
		return rootNode;
	}

	public void closeTOCEntry(TOCEntry entry) {
	}
}
