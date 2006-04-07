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

/**
 * Structure to hold information about a group instance at a particular grouping
 * level.
 */
public final class GroupInfo
{
	
	/**
	 * Index of the the parent group, i.e., the immediate outer group that this
	 * group belongs to
	 */
	public int parent = -1;

	/**
	 * Index of the first child group. If the current group is the innermost
	 * group, this is the ID of the first data row in the group
	 */
	public int firstChild = -1;
	
}
