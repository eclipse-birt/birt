
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;

/**
 * This is an implementation to IGroupInstanceInfo.
 */

public class GroupInstanceInfo implements IGroupInstanceInfo {
	private int level;
	private int rowId;

	/**
	 *
	 * @param level
	 * @param rowId
	 */
	public GroupInstanceInfo(int level, int rowId) {
		this.level = level;
		this.rowId = rowId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IGroupInstanceInfo#getGroupLevel()
	 */
	@Override
	public int getGroupLevel() {
		return this.level;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IGroupInstanceInfo#getRowId()
	 */
	@Override
	public int getRowId() {
		return this.rowId;
	}

}
