
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.util.Comparator;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 *
 */

public class AggregationResultRowComparator implements Comparator<IAggregationResultRow> {
	private int[] keyLevelIndexs;
	private int[] sortTypes;

	public AggregationResultRowComparator(int[] keyLevelIndexs, int[] sortTypes) {
		this.keyLevelIndexs = keyLevelIndexs;
		this.sortTypes = sortTypes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(IAggregationResultRow o1, IAggregationResultRow o2) {
		Member[] member1 = ((IAggregationResultRow) o1).getLevelMembers();
		Member[] member2 = ((IAggregationResultRow) o2).getLevelMembers();

		for (int i = 0; i < keyLevelIndexs.length; i++) {
			// only for drill operation, the member key value will be null
			if (member1[keyLevelIndexs[i]] == null || member2[keyLevelIndexs[i]] == null) {
				continue;
			}
			int result = (member1[keyLevelIndexs[i]]).compareTo(member2[keyLevelIndexs[i]]);
			if (sortTypes != null && sortTypes[i] == IDimensionSortDefn.SORT_DESC) {
				result = result * -1;
			}
			if (result < 0) {
				return result;
			} else if (result > 0) {
				return result;
			}
		}
		return 0;
	}
}
