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
 * Used to fetch the next innermost object. For example if you pass a List as
 * its <code>root</code> and the content of the List is:
 * --------------------------------------------- Index Value ValueType 0 "s1"
 * String 1 {null, "s2", "s3"} String[] 2 {} String[] 3 {{"s4", "s5"}, {"s6"}}
 * String[][]
 * 
 * Then, the value sequence from this InnermostNextable is: {"s1", null, "s2",
 * "s3", "s4", "s5", "s6"}
 */
public class InnermostNextable extends Nextable {
	private Nextable root;
	private Object currentValue;
	private InnermostNextable innerNextable;

	public InnermostNextable(Nextable nextable) {
		this.root = nextable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#getValue()
	 */
	@Override
	public Object getValue() {
		return currentValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.impl.internal.Nextable#next()
	 */
	@Override
	public boolean next() {
		if (root == null) {
			currentValue = null;
			return false;
		}
		if (innerNextable == null) {
			// need to get the next root
			if (root.next()) {
				Object nextRoot = root.getValue();
				if (Nextable.isNextable(nextRoot)) {
					innerNextable = new InnermostNextable(Nextable.createNextable(nextRoot));
					if (innerNextable.next()) {
						currentValue = innerNextable.getValue();
						return true;
					} else {
						// nextRoot is just empty nextable
						innerNextable = null;
						return next();
					}
				} else {
					innerNextable = null;
					currentValue = nextRoot;
					return true;
				}
			} else {
				currentValue = null;
				return false;
			}
		} else {
			if (innerNextable.next()) {
				currentValue = innerNextable.getValue();
				return true;
			} else {
				// go to the end of the current root object, need to continue to fetch next root
				// object
				innerNextable = null;
				return next();
			}
		}
	}
}
