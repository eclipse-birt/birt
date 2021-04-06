/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO: to implement a disk based IDiskArray class which can insert element
 * randomly.
 */

public class OrderedDiskArray extends ArrayList implements IDiskArray {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8408837939375607822L;
	private int maxSize = -1;
	private boolean isTop = false;

	/**
	 * 
	 */
	public OrderedDiskArray() {
		super();
	}

	public OrderedDiskArray(int maxSize, boolean isTop) {
		this.maxSize = maxSize;
		this.isTop = isTop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#add(java.lang.Object)
	 */
	public boolean add(Object o) {
		if (maxSize != 0) {
			orderInsert(o);
			return true;
		} else
			return false;
	}

	private void orderInsert(Object value) {
		// search for the right insert position: the list is sorted ascending
		int i = size() - 1;
		// the break condition assures it to preserve the oldest elements
		// If the filter is top, using ret > 0 as the break condition, otherwise
		// if it is bottom, using ret >=0 as the break condition.
		for (; i >= 0; i--) {
			int ret = CompareUtil.compare(value, get(i));
			if (ret > 0 || !isTop && ret == 0)// isTop&&ret>0 || !isTop&&ret>=0
				break;
		}
		int pos = i + 1; // pos is the correct position should be inserted
		if (maxSize < 0 || size() < maxSize) {
			super.add(value);
			// shift to the right insert position
			for (int j = size() - 1; j > pos; j--) {
				Object obj = get(j - 1);
				set(j, obj);
			}
			set(pos, value);
		} else {
			if (isTop) {
				// shift left one element: remove the littlest one
				if (pos > 0) {
					for (int j = 0; j < pos - 1; j++) {
						Object obj = get(j + 1);
						set(j, obj);
					}
					set(pos - 1, value);
				}
			} else if (pos < maxSize) {// shift right one element: remove the greatest one
				for (int j = size() - 1; j > pos; j--) {
					Object obj = get(j - 1);
					set(j, obj);
				}
				set(pos, value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#close()
	 */
	public void close() throws IOException {
		super.clear();
	}
}
