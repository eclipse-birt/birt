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

/**
 * A reference from the ROOT POJO object
 */
public abstract class ReferenceNode {
	protected RelayReferenceNode parent;
	private IMappingSource reference;

	public ReferenceNode(RelayReferenceNode parent, IMappingSource reference) {
		assert reference != null;
		this.parent = parent;
		this.reference = reference;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * @return the parent
	 */
	public RelayReferenceNode getParent() {
		return parent;
	}

	/**
	 * @return the reference
	 */
	public IMappingSource getReference() {
		return reference;
	}

	/**
	 * @return the Column References from this reference
	 */
	public abstract ColumnReferenceNode[] getColumnReferenceNodes();
}
