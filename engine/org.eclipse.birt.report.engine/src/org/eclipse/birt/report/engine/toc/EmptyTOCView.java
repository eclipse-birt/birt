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

import java.util.List;

import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;

class EmptyTOCView implements ITOCTree {

	protected TOCNode root = new TOCNode();

	EmptyTOCView() {
		root = new TOCNode();
		root.setNodeID("/");
	}

	public TOCNode findTOC(String tocId) {
		if ("/".equals(tocId)) {
			return root;
		}
		return null;
	}

	public List findTOCByValue(Object tocValue) {
		return null;
	}

	public TOCNode getRoot() {
		return root;
	}

}
