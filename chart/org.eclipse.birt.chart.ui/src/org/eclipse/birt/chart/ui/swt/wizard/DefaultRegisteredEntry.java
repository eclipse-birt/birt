/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Used for general registered UI entry with priority.
 */

public class DefaultRegisteredEntry<T> {

	private int priority = 0;
	private final T instance;
	private final String name;

	public DefaultRegisteredEntry(T instance, String name, String sPriority) {
		this.instance = instance;
		this.name = name;
		try {
			if (sPriority != null) {
				priority = Integer.parseInt(sPriority);
			}
		} catch (NumberFormatException e) {
		}
	}

	public T getInstance() {
		return instance;
	}

	public int getPriority() {
		return priority;
	}

	public String getName() {
		return name;
	}

	public static <T> Collection<T> convert(Collection<DefaultRegisteredEntry<T>> collection) {
		if (collection == null || collection.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> newCollection = new ArrayList<>();
		for (DefaultRegisteredEntry<T> d : collection) {
			newCollection.add(d.getInstance());
		}
		return newCollection;
	}
}
