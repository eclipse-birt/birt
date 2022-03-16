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
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		GraphNode other = (GraphNode) obj;
		return value.equals(other.getValue());
	}

	public Object getValue() {
		return value;
	}
}
