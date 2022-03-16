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

import java.util.Iterator;

/**
 * Nextable Wrapper for Iterator
 */
public class NextableFromIterator extends Nextable {
	@SuppressWarnings("unchecked")
	private Iterator itr;
	private Object currValue;

	@SuppressWarnings("unchecked")
	public NextableFromIterator(Iterator itr) {
		this.itr = itr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#getValue()
	 */
	@Override
	public Object getValue() {
		return currValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#next()
	 */
	@Override
	public boolean next() {
		if (itr == null) {
			return false;
		}
		if (itr.hasNext()) {
			currValue = itr.next();
			return true;
		} else {
			currValue = null;
			return false;
		}
	}

}
