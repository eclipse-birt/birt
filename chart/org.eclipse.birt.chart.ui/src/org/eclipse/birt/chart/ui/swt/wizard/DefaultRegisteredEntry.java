/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
				priority = Integer.valueOf(sPriority);
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
		List<T> newCollection = new ArrayList<T>();
		for (DefaultRegisteredEntry<T> d : collection) {
			newCollection.add(d.getInstance());
		}
		return newCollection;
	}
}
