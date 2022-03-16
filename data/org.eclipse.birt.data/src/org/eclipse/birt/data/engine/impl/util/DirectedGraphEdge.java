/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.util;

/**
 * A directed graph edge
 */
public class DirectedGraphEdge {

	private GraphNode from;
	private GraphNode to;

	public DirectedGraphEdge(GraphNode from, GraphNode to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException("from node or to node is null");
		}
		this.from = from;
		this.to = to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * from.hashCode() + to.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		DirectedGraphEdge other = (DirectedGraphEdge) obj;
		return from.equals(other.getFrom()) && to.equals(other.getTo());
	}

	protected GraphNode getFrom() {
		return from;
	}

	protected GraphNode getTo() {
		return to;
	}

}
