/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.birt.report.designer.ui.IPreferenceConstants;

/**
 * This sorter sorts the given elements according to the category name, built-in
 * "Content" category is always placed first, the remaining is in alphabetic
 * order. Elements with same category keep the original insertion order.
 */
public class CategorizedElementSorter<T> {

	private TreeMap<String, List<T>> map;
	private List<T> contentList;

	public CategorizedElementSorter() {
		map = new TreeMap<String, List<T>>();
		contentList = new ArrayList<T>();
	}

	public void addElement(String category, T data) {
		if (IPreferenceConstants.PALETTE_CONTENT.equals(category)) {
			contentList.add(data);
		} else {
			List<T> elements = map.get(category);

			if (elements == null) {
				elements = new ArrayList<T>();
				map.put(category, elements);
			}

			elements.add(data);
		}
	}

	public List<T> getSortedElements() {
		List<T> list = new ArrayList<T>();

		list.addAll(contentList);

		for (Iterator<List<T>> itr = map.values().iterator(); itr.hasNext();) {
			List<T> elements = itr.next();

			for (Iterator<T> itr2 = elements.iterator(); itr2.hasNext();) {
				list.add(itr2.next());
			}
		}

		return list;
	}

}
