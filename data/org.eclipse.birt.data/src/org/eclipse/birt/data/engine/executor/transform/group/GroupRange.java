/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
