/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
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
