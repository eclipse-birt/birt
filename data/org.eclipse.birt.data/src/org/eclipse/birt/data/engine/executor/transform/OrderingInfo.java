/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.executor.transform;

import java.util.ArrayList;
import java.util.List;

/**
 * The OrderingInfo instance is used by SmartCache to generate a new SmartCache
 * instance according to the existing SmartCache instance and the information in
 * OrderingInfo instance.
 */
public final class OrderingInfo {

	// The group start index array
	private List startIndex = new ArrayList();

	// The group end index array
	private List endIndex = new ArrayList();

	/**
	 * 
	 * @param i
	 * @return
	 */
	public int getStartIndex(int i) {
		return Integer.valueOf(startIndex.get(i).toString()).intValue();
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public int getEndIndex(int i) {
		return Integer.valueOf(endIndex.get(i).toString()).intValue();
	}

	/**
	 * 
	 * @param startIdx
	 * @param endIdx
	 */
	public void add(int startIdx, int endIdx) {
		this.startIndex.add(String.valueOf(startIdx));
		this.endIndex.add(String.valueOf(endIdx));
	}

	/**
	 * 
	 * @return
	 */
	public int getCount() {
		return startIndex.size();
	}
}
