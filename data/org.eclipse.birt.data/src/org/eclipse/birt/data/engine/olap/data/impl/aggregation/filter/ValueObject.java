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
