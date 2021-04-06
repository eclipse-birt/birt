/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		if (array == null) {
			return false;
		}
		if (index >= array.length) {
			return false;
		}
		index++;
		return index < array.length;
	}
}
