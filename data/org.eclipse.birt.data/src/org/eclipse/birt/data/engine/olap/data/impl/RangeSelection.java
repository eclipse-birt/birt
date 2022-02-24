
/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;

/**
 * 
 */

public class RangeSelection implements ISelection {
	private Object[] minKey;
	private Object[] maxKey;
	private boolean containsMinKey;
	private boolean containsMaxKey;

	private static Logger logger = Logger.getLogger(RangeSelection.class.getName());

	/**
	 * 
	 * @param minKey
	 * @param maxKey
	 * @param containsMinKey
	 * @param containsMaxKey
	 */
	public RangeSelection(Object[] minKey, Object[] maxKey, boolean containsMinKey, boolean containsMaxKey) {
		Object[] params = { minKey, maxKey, Boolean.valueOf(containsMinKey), Boolean.valueOf(containsMaxKey) };
		logger.entering(RangeSelection.class.getName(), "RangeSelection", params);
		this.minKey = minKey;
		this.maxKey = maxKey;
		this.containsMinKey = containsMinKey;
		this.containsMaxKey = containsMaxKey;
		logger.exiting(RangeSelection.class.getName(), "RangeSelection");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.ISelection#isSelected(java.lang.
	 * Object[])
	 */
	public boolean isSelected(Object[] key) {
		if (minKey != null) {
			if (containsMinKey) {
				if (CompareUtil.compare(key, minKey) < 0) {
					return false;
				}
			} else {
				if (CompareUtil.compare(key, minKey) <= 0) {
					return false;
				}
			}
		}
		if (maxKey != null) {
			if (containsMaxKey) {
				if (CompareUtil.compare(key, maxKey) > 0) {
					return false;
				}
			} else {
				if (CompareUtil.compare(key, maxKey) >= 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#getMax()
	 */
	public Object[] getMax() {
		return maxKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.ISelection#getMin()
	 */
	public Object[] getMin() {
		return minKey;
	}

	/**
	 * @return the containsMinKey
	 */
	public boolean isContainsMinKey() {
		return containsMinKey;
	}

	/**
	 * @return the containsMaxKey
	 */
	public boolean isContainsMaxKey() {
		return containsMaxKey;
	}

}
