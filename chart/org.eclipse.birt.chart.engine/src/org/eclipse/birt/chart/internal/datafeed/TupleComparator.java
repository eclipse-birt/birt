/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.Comparator;

import org.eclipse.birt.chart.model.attribute.SortOption;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.Calendar;

/**
 * An internal Comparator implementation compares two tuples.
 */
public class TupleComparator implements Comparator {

	private GroupKey[] iaSortKeys;

	private Collator collator;

	TupleComparator(GroupKey[] keys) {
		iaSortKeys = keys;
		collator = Collator.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		final Object[] oaTuple1 = (Object[]) o1;
		final Object[] oaTuple2 = (Object[]) o2;
		Object oValue1, oValue2;
		int iResult = 0;
		for (int i = 0; i < iaSortKeys.length; i++) {
			final int keyIndex = iaSortKeys[i].getKeyIndex();
			oValue1 = oaTuple1[keyIndex];
			oValue2 = oaTuple2[keyIndex];
			final SortOption direction = iaSortKeys[i].getDirection();
			if (direction == null) {
				// no sorting
				iResult = 0;
			} else if (direction.getValue() == SortOption.ASCENDING) {
				iResult = compareObjects(oValue1, oValue2, collator);
			} else if (direction.getValue() == SortOption.DESCENDING) {
				iResult = compareObjects(oValue2, oValue1, collator);
			}
			if (iResult != 0) {
				return iResult;
			}
		}
		return 0;
	}

	/**
	 * Compare two objects of the same data type
	 */
	public static int compareObjects(Object a, Object b, Collator collator) {
		// a == b
		if (a == null && b == null) {
			return 0;
		}

		// a < b
		else if (a == null && b != null) {
			return -1;
		}

		// a > b
		else if (a != null && b == null) {
			return 1;
		}

		else if (a instanceof String) {
			int iC;

			if (collator != null) {
				iC = collator.compare(a.toString(), b.toString());
			} else {
				iC = a.toString().compareTo(b.toString());
			}

			if (iC != 0) {
				iC = (iC < 0) ? -1 : 1;
			}
			return iC;
		} else if (a instanceof Number) {
			final double d1 = ((Number) a).doubleValue();
			final double d2 = ((Number) b).doubleValue();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof java.util.Date) {
			final long d1 = ((java.util.Date) a).getTime();
			final long d2 = ((java.util.Date) b).getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof Calendar) {
			final long d1 = ((Calendar) a).getTime().getTime();
			final long d2 = ((Calendar) b).getTime().getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else
		// HANDLE AS STRINGs
		{
			return compareObjects(a.toString(), b.toString(), collator);
		}
	}
}
