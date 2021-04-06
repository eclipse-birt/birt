/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
