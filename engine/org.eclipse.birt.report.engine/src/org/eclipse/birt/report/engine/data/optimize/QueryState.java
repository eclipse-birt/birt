/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.optimize;

import java.util.ArrayList;
import java.util.List;

class QueryState {

	List ownerList;
	boolean state;

	QueryState() {
		ownerList = new ArrayList();
	}

	public boolean isOwnerAdded(Object owner) {
		return ownerList.contains(owner);
	}

	public void addOwner(Object owner) {
		ownerList.add(owner);
	}

	public void resetOwner(Object owner) {
		int count = count();
		int which = -1;
		for (int index = 0; index < count; index++) {
			Object obj = ownerList.get(index);
			if (obj == owner) {
				which = index;
				break;
			}
		}
		while (count() > which + 1) {
			ownerList.remove(count() - 1);
		}
	}

	public boolean cached() {
		return this.state;
	}

	public void setCached(boolean state) {
		this.state = state;
	}

	public int count() {
		return ownerList.size();
	}
}
