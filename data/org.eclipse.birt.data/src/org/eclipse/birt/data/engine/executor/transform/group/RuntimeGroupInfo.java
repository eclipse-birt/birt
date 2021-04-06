/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.transform.group;

public class RuntimeGroupInfo {

	protected GroupInfo group;
	protected int parentIdxAdj;
	protected int childIdxAdj;
	protected int count;
	protected int removed;
	protected int groupId;

	RuntimeGroupInfo(GroupInfo group, int groupIndex, int count, int parentAdjust, int childAdjust) {
		this.group = group;
		this.groupId = groupIndex;
		this.count = count;
		this.parentIdxAdj = parentAdjust;
		this.childIdxAdj = childAdjust;
		this.removed = 0;
	}

	boolean isRemoved() {
		return count - removed == 0;
	}
}
