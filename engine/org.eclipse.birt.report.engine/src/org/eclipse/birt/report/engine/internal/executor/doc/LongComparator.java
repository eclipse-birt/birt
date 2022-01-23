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

public class LongComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		Long l1 = (Long) arg0;
		Long l2 = (Long) arg1;
		return l1.compareTo(l2);
	}
}
