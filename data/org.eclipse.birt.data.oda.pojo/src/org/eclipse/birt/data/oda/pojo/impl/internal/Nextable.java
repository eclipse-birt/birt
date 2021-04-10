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
