/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.build;

import java.util.ArrayList;

import org.apache.tools.ant.types.DataType;

/**
 * Represent a project list
 *
 */
public class ProjectList extends DataType implements Cloneable {
	protected ArrayList list = new ArrayList();

	/**
	 * add a project
	 *
	 * @param pro
	 */
	public void addProjectInfo(ProjectInfo pro) {
		list.add(pro);
	}

	/**
	 * get project by index
	 *
	 * @param index
	 * @return
	 */
	public ProjectInfo getProject(int index) {
		assert (index >= 0 && index < list.size());
		return (ProjectInfo) list.get(index);
	}

	/**
	 * get count
	 *
	 * @return
	 */
	public int getCount() {
		return list.size();
	}

}
