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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 * 
 */

class FilterUtil {

	private FilterUtil() {
	};

	/**
	 * compare two level arrays to determine whether they are equal or not.
	 * 
	 * @param levels1
	 * @param levels2
	 * @return
	 */
	static boolean isEqualLevels(DimLevel[] levels1, DimLevel[] levels2) {
		if (levels1 == null && levels2 == null)
			return true;
		else if (levels1 == null || levels2 == null)
			return false;

		if (levels1.length != levels2.length)
			return false;
		for (int i = 0; i < levels1.length; i++) {
			if (levels1[i].equals(levels2[i]) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get the target level index in the specified <code>levels</code>, which
	 * assumes that they are under the same dimension.
	 * 
	 * @param levels
	 * @param targetLevelName
	 * @return
	 */
	static int getTargetLevelIndex(ILevel[] levels, String targetLevelName) {
		int index = 0;
		for (index = 0; index < levels.length; index++) {
			if (levels[index].getName().equals(targetLevelName)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * @param total
	 * @param N
	 * @return
	 */
	static int getTargetN(long total, double N) {
		return (int) Math.round(N / 100 * total);
	}

	/**
	 * To check whether two dimension rows share the same parent levels regarding
	 * the specified target level.
	 * 
	 * @param members1
	 * @param member2
	 * @param targetIndex - the member index of the target level.
	 * @return
	 */
	static boolean shareParentLevels(Member[] members1, Member[] member2, int targetIndex) {
		assert members1 != null && member2 != null;
		for (int i = 0; i < targetIndex; i++) {
			if (members1[i] == null || member2[i] == null) {// ignore the empty member value
				continue;
			}
			if (members1[i].equals(member2[i]) == false) {
				return false;
			}
		}
		return true;
	}
}
