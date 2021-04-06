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

public final class GroupRange {

	protected int first;
	protected int length;
	protected int lastIndex;

	GroupRange() {
	}

	GroupRange(int first, int length) {
		this.first = first;
		this.length = length;
		this.lastIndex = first + length;
	}

	public int getLast() {
		return lastIndex;
	}

}
