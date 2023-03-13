/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.core.btree;

import java.io.Serializable;
import java.util.Comparator;

public class JavaComparator<K> implements Comparator<K>, Serializable {
	private static final long serialVersionUID = -2374485501916583296L;

	@Override
	public int compare(K o1, K o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			}
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1 instanceof Comparable) {
			return ((Comparable) o1).compareTo(o2);
		}
		if (o2 instanceof Comparable) {
			return -((Comparable) o2).compareTo(o1);
		}
		return 0;
	}
}
