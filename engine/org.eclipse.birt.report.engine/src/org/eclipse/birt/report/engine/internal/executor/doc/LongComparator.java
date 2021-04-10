/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
