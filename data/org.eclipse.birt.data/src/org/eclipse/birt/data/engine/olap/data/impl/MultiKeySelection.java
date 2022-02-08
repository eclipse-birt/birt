
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

public class MultiKeySelection implements ISelection {
	private Object[][] keyValues;
	private Object[] minKey = null;
	private Object[] maxKey = null;
	private static Logger logger = Logger.getLogger(MultiKeySelection.class.getName());

	/**
	 * @return the keys
	 */
	public Object[][] getKeyValues() {
		return keyValues;
	}

	/**
	 * 
	 * @param keys
	 */
	public MultiKeySelection(Object[][] keys) {
		logger.entering(MultiKeySelection.class.getName(), "MultiKeySelection", keys);
		assert keys != null && keys.length > 0;
		minKey = keys[0];
		maxKey = keys[0];
		for (int i = 1; i < keys.length; i++) {
			if (CompareUtil.compare(keys[i], minKey) < 0) {
				minKey = keys[i];
			}
			if (CompareUtil.compare(keys[i], maxKey) > 0) {
				maxKey = keys[i];
			}
		}
		this.keyValues = keys;
		logger.exiting(MultiKeySelection.class.getName(), "MultiKeySelection");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.ISelection#isSelected(java.lang.
	 * Object[])
	 */
	public boolean isSelected(Object[] key) {
		for (int i = 0; i < keyValues.length; i++) {
			if (CompareUtil.compare(keyValues[i], key) == 0) {
				return true;
			}
		}
		return false;
	}

}
