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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.Comparator;

public class CascadingComparator implements Comparator<Object[]> {

	private Comparator comparator;

	public CascadingComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compare(Object[] arg0, Object[] arg1) {
		for (int i = 0; i < arg0.length && i < arg1.length; i++) {
			int result = comparator.compare(arg0[i], arg1[i]);
			if (result != 0) {
				return result;
			}
		}
		if (arg0.length > arg1.length) {
			return 1;
		} else if (arg0.length < arg1.length) {
			return -1;
		} else {
			return 0;
		}
	}
}
