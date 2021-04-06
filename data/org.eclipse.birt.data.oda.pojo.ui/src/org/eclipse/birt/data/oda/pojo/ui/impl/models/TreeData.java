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

package org.eclipse.birt.data.oda.pojo.ui.impl.models;

public class TreeData {

	private Object obj;
	private int level;

	public TreeData(Object obj, int level) {
		this.obj = obj;
		this.level = level;
	}

	public Object getWrappedObject() {
		return obj;
	}

	public int getLevel() {
		return this.level;
	}

}
