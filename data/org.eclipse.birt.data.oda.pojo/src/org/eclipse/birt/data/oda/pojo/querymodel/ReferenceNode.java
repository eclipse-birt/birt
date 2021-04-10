/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
