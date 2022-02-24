/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
