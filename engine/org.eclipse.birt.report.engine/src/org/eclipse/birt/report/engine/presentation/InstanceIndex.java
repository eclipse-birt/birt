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

package org.eclipse.birt.report.engine.presentation;

import org.eclipse.birt.report.engine.api.InstanceID;

public class InstanceIndex {

	/**
	 * the id of the content object.
	 */
	private InstanceID iid;
	/**
	 * the offset of the content object. If the content is saved into the document,
	 * the offset is the index. otherwise, it is the index of the previous object.
	 */
	private long offset;

	public InstanceIndex(long offset) {
		this.iid = null;
		this.offset = offset;
	}

	public InstanceIndex(InstanceID iid, long offset) {
		this.iid = iid;
		this.offset = offset;
	}

	@Override
	public String toString() {
		if (iid == null) {
			return Long.toString(offset);
		}
		return iid.toString() + "@" + offset;
	}

	public InstanceID getInstanceID() {
		return iid;
	}

	public long getOffset() {
		return offset;
	}

	/**
	 * Two InstanceIndex are equal when their instanceIDs are equal
	 */
	@Override
	public boolean equals(Object index) {
		if (!(index instanceof InstanceIndex)) {
			return false;
		}

		InstanceIndex ii = (InstanceIndex) index;
		if (this.iid == null || ii.iid == null) {
			return false;
		}
		long uid_a = this.iid.getUniqueID();
		long uid_b = ii.iid.getUniqueID();
		if (uid_a == uid_b) {
			return true;
		}
		return false;
	}
}
