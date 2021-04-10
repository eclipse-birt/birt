/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.transform.group;

import org.eclipse.birt.data.engine.cache.ICachedObject;
import org.eclipse.birt.data.engine.cache.ICachedObjectCreator;

/**
 * Structure to hold information about a group instance at a particular grouping
 * level.
 */
public final class GroupInfo implements ICachedObject {

	/**
	 * Index of the the parent group, i.e., the immediate outer group that this
	 * group belongs to
	 */
	public int parent = -1;

	/**
	 * Index of the first child group. If the current group is the innermost group,
	 * this is the ID of the first data row in the group
	 */
	public int firstChild = -1;

	/**
	 * 
	 * @return
	 */
	public static ICachedObjectCreator getCreator() {
		return new GroupInfoCreator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.cache.ICachedObject#getFieldValues()
	 */
	public Object[] getFieldValues() {
		Object[] fields = new Object[2];
		fields[0] = Integer.valueOf(parent);
		fields[1] = Integer.valueOf(firstChild);
		return fields;
	}

}

/**
 * A creator class implemented ICachedObjectCreator. This class is used to
 * create GroupInfo object.
 * 
 * @author Administrator
 * 
 */
class GroupInfoCreator implements ICachedObjectCreator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.cache.ICachedObjectCreator#createInstance(java.
	 * lang.Object[])
	 */
	public ICachedObject createInstance(Object[] fields) {
		GroupInfo groupInfo = new GroupInfo();
		groupInfo.parent = ((Integer) fields[0]).intValue();
		groupInfo.firstChild = ((Integer) fields[1]).intValue();
		return groupInfo;
	}
}
