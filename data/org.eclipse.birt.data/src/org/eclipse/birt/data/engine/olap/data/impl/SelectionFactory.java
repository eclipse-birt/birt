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

package org.eclipse.birt.data.engine.olap.data.impl;

import org.eclipse.birt.data.engine.olap.data.api.ISelection;

/**
 * 
 */

public class SelectionFactory {
	private SelectionFactory() {
	}

	/**
	 * 
	 * @return
	 */
	public static ISelection createEmptySelection() {
		return new EmptySelection();
	}

	/**
	 * create an OneKeySelection instance.
	 * 
	 * @param key
	 * @return
	 */
	public static ISelection createOneKeySelection(Object[] key) {
		return new OneKeySelection(key);
	}

	/**
	 * 
	 * @param selectedObjects
	 * @return
	 */
	public static ISelection createMutiKeySelection(Object[][] keys) {
		return new MultiKeySelection(keys);
	}

	/**
	 * 
	 * @param selectedObjects
	 * @return
	 */
	public static ISelection[] createSelectionArray(Object[][] keys) {
		ISelection[] result = new ISelection[keys.length];
		for (int i = 0; i < keys.length; i++) {
			result[i] = new OneKeySelection(keys[i]);
		}
		return result;
	}

	/**
	 * 
	 * @param minKey
	 * @param maxKey
	 * @param containsMinKey
	 * @param containsMaxKey
	 * @return
	 */
	public static ISelection createRangeSelection(Object[] minKey, Object[] maxKey, boolean containsMinKey,
			boolean containsMaxKey) {
		return new RangeSelection(minKey, maxKey, containsMinKey, containsMaxKey);
	}

}
