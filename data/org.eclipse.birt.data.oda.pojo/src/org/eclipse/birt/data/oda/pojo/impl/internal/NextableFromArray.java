/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.impl.internal;

/**
 * Nextable Wrapper for Array
 */
public class NextableFromArray extends Nextable {
	private int index = -1;
	private Object[] array;

	public NextableFromArray(Object[] array) {
		this.array = array;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#getValue()
	 */
	@Override
	public Object getValue() {
		if (array == null || index == -1 || index >= array.length) {
			return null;
		}
		return array[index];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#next()
	 */
	@Override
	public boolean next() {
		if ((array == null) || (index >= array.length)) {
			return false;
		}
		index++;
		return index < array.length;
	}
}
