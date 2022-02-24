/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.Comparator;

class FragmentComparator implements Comparator {

	Comparator comparator;

	FragmentComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public int compare(Object arg0, Object arg1) {
		if (arg0 == arg1) {
			return 0;
		}
		if (arg0 == Segment.LEFT_MOST_EDGE) {
			return -1;
		}
		if (arg0 == Segment.RIGHT_MOST_EDGE) {
			return 1;
		}
		if (arg1 == Segment.LEFT_MOST_EDGE) {
			return 1;
		}
		if (arg1 == Segment.RIGHT_MOST_EDGE) {
			return -1;
		}
		return comparator.compare(arg0, arg1);
	}

}
