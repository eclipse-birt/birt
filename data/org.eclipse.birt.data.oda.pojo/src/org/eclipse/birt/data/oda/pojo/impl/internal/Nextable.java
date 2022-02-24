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

import java.util.Collection;
import java.util.Iterator;

/**
 * A common wrapper for Array/Collection/Iterator
 */
public abstract class Nextable {
	/**
	 * Move to next
	 * 
	 * @return
	 */
	public abstract boolean next();

	/**
	 * 
	 * @return current value. just return null if no more next element is available
	 */
	public abstract Object getValue();

	@SuppressWarnings("unchecked")
	public static boolean isNextable(Object o) {
		return o instanceof Iterator || o instanceof Collection || o instanceof Object[];
	}

	@SuppressWarnings("unchecked")
	public static Nextable createNextable(Object o) {
		if (o instanceof Iterator) {
			return new NextableFromIterator((Iterator) o);
		}
		if (o instanceof Collection) {
			Iterator itr = ((Collection) o).iterator();
			return new NextableFromIterator(itr);
		}
		if (o instanceof Object[]) {
			return new NextableFromArray((Object[]) o);
		}
		throw new IllegalArgumentException("o is not nextable"); //$NON-NLS-1$
	}
}
