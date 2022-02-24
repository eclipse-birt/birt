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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;

/**
 * 
 */
public class ValueObject implements Comparable {

	public Object value;
	public Object index;

	public ValueObject(Object value, Object index) {
		this.value = value;
		this.index = index;
	}

	public int compareTo(Object obj) {
		if (obj instanceof ValueObject) {
			ValueObject objValue = (ValueObject) obj;
			return CompareUtil.compare(value, objValue.value);
		}
		return -1;
	}
}
