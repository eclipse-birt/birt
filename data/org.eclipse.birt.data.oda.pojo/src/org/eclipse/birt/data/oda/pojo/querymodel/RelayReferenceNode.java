/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.querymodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A relay reference
 */
public class RelayReferenceNode extends ReferenceNode {

	private List<ReferenceNode> children = new ArrayList<>();

	public RelayReferenceNode(RelayReferenceNode parent, IMappingSource reference) {
		super(parent, reference);
	}

	public void addChild(ReferenceNode child) {
		assert child != null;
		children.add(child);
		child.parent = this;
	}

	public ReferenceNode[] getChildren() {
		return children.toArray(new ReferenceNode[0]);
	}

	public boolean isAAncestorFor(ReferenceNode node) {
		assert node != null;
		RelayReferenceNode parent = node.getParent();
		while (parent != null) {
			if (parent == this) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.oda.pojo.querymodel.ReferenceNode#
	 * getColumnReferenceNodes()
	 */
	@Override
	public ColumnReferenceNode[] getColumnReferenceNodes() {
		List<ColumnReferenceNode> result = new ArrayList<>();
		for (ReferenceNode rn : children) {
			result.addAll(Arrays.asList(rn.getColumnReferenceNodes()));
		}
		return result.toArray(new ColumnReferenceNode[0]);
	}
}
