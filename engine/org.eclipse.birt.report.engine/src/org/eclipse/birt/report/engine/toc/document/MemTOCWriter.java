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

	@Override
	public void close() {
	}

	@Override
	public ITreeNode getTree() {
		return rootNode;
	}

	@Override
	public void startTOCEntry(TOCEntry entry) {
		MemTreeNode treeNode = new MemTreeNode(entry);
		entry.setTreeNode(treeNode);
		MemTreeNode parentTreeNode = getParentTreeNode(entry);
		parentTreeNode.addChild(treeNode);
	}

	MemTreeNode getParentTreeNode(TOCEntry entry) {
		TOCEntry parent = entry.getParent();
		if (parent != null) {
			return (MemTreeNode) parent.getTreeNode();
		}
		return rootNode;
	}

	@Override
	public void closeTOCEntry(TOCEntry entry) {
	}
}
