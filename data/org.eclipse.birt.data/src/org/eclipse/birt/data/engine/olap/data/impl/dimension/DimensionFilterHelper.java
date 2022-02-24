
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.util.BaseDiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;
import org.eclipse.birt.data.engine.olap.data.util.PrimitiveDiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.SetUtil;

/**
 * 
 */

public class DimensionFilterHelper {
	/**
	 * 
	 * @param levels
	 * @param filters
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static IDiskArray find(Level[] levels, ISelection[][] filters) throws IOException, DataException {
		ArrayList filterResults = new ArrayList();
		for (int i = 0; i < levels.length; i++) {
			filterResults.add(find(levels[i], filters[i]));
		}
		PrimitiveDiskSortedStack[] stackResults = new PrimitiveDiskSortedStack[filterResults.size()];
		System.arraycopy(filterResults.toArray(), 0, stackResults, 0, stackResults.length);
		int maxLen = 0;
		for (int i = 0; i < stackResults.length; i++) {
			if (maxLen < stackResults[i].size())
				maxLen = stackResults[i].size();
		}
		IDiskArray AndFilterResults = SetUtil.getIntersection(stackResults, maxLen);
		return AndFilterResults;
	}

	/**
	 * 
	 * @param level
	 * @param filter
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private static BaseDiskSortedStack find(Level level, ISelection[] filter) throws IOException, DataException {
		IDiskArray indexKeyArray = null;
		if (level.getDiskIndex() != null)
			indexKeyArray = level.getDiskIndex().find(filter);
		if (indexKeyArray != null) {
			int len = 0;
			for (int i = 0; i < indexKeyArray.size(); i++) {
				IndexKey key = (IndexKey) indexKeyArray.get(i);
				len += key.getDimensionPos().length;
			}
			PrimitiveDiskSortedStack resultStack = new PrimitiveDiskSortedStack(len, true, true);
			for (int i = 0; i < indexKeyArray.size(); i++) {
				IndexKey key = (IndexKey) indexKeyArray.get(i);
				int[] pos = key.getDimensionPos();
				for (int j = 0; j < pos.length; j++) {
					resultStack.push(Integer.valueOf(pos[j]));
				}
			}
			return resultStack;
		} else {
			return new PrimitiveDiskSortedStack(1, true, true);
		}
	}
}
