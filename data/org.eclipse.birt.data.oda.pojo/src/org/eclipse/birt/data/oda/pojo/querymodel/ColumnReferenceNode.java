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
 * A reference to a column
 */
public class ColumnReferenceNode extends ReferenceNode implements Comparable<ColumnReferenceNode> {

	private Column column; // target column

	public ColumnReferenceNode(RelayReferenceNode parent, IMappingSource reference, Column column) {

		super(parent, reference);

		assert column != null;
		this.column = column;
	}

	public Column getColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.querymodel.ReferenceNode#
	 * getColumnReferenceNodes()
	 */
	@Override
	public ColumnReferenceNode[] getColumnReferenceNodes() {
		return new ColumnReferenceNode[] { this };
	}

	public int compareTo(ColumnReferenceNode o) {
		return this.getColumn().getIndex() - o.getColumn().getIndex();
	}

	@Override
	public int hashCode() {
		return column.getIndex();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnReferenceNode other = (ColumnReferenceNode) obj;
		return column.getIndex() == other.getColumn().getIndex();
	}

}
