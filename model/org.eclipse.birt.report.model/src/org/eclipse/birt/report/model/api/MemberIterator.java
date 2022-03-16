/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.StructureContextUtil;

/**
 * An iterator over the members of a structure. Each call to
 * <code>getNext( )</code> returns a <code>MemberHandle</code> for the next
 * structure member.
 */

public class MemberIterator implements Iterator {

	/**
	 * Handle to the structure itself.
	 */

	protected StructureHandle structHandle;

	/**
	 * The definition of the structure.
	 */

	protected StructureDefn structDefn;

	/**
	 * Iterator over the members.
	 */

	protected Iterator iter;

	protected boolean isValid;

	/**
	 * Constructs a member iterator with the given structure handle.
	 *
	 * @param struct handle to the structure over which to iterate
	 */

	public MemberIterator(StructureHandle struct) {
		structHandle = struct;
		structDefn = (StructureDefn) struct.getDefn();
		iter = structDefn.propertiesIterator();
		isValid = StructureContextUtil.isValidStructureHandle(struct);
	}

	/**
	 * The remove operation is not supported when iterating over a structure; the
	 * application cannot remove members of a structure.
	 */

	// Implementation of iterator.remove( )
	@Override
	public void remove() {
		// Not supported here. Cannot remove structure members.
	}

	// Implementation of iterator.hasNext( )

	@Override
	public boolean hasNext() {
		if (!isValid) {
			return false;
		}
		return iter.hasNext();
	}

	/**
	 * Returns a handle to the next member. Implementation of iterator.next( )
	 *
	 * @return a handle to the next member.
	 * @see MemberHandle
	 */

	@Override
	public Object next() {
		if (!hasNext()) {
			return null;
		}
		return new MemberHandle(structHandle, (StructPropertyDefn) iter.next());
	}

}
