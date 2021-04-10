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
 * A node in graph 2 graph nodes with the equal values are considered as the
 * same
 */
public class GraphNode {

	// value saved in this node
	private Object value;

	public GraphNode(Object value) {
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		this.value = value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNode other = (GraphNode) obj;
		return value.equals(other.getValue());
	}

	public Object getValue() {
		return value;
	}
}
