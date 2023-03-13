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

import java.util.List;

import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;

class EmptyTOCView implements ITOCTree {

	protected TOCNode root = new TOCNode();

	EmptyTOCView() {
		root = new TOCNode();
		root.setNodeID("/");
	}

	@Override
	public TOCNode findTOC(String tocId) {
		if ("/".equals(tocId)) {
			return root;
		}
		return null;
	}

	@Override
	public List findTOCByValue(Object tocValue) {
		return null;
	}

	@Override
	public TOCNode getRoot() {
		return root;
	}

}
