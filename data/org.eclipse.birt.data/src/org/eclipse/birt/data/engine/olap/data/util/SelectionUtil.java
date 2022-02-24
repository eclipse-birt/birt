/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.impl.EmptySelection;
import org.eclipse.birt.data.engine.olap.data.impl.MultiKeySelection;
import org.eclipse.birt.data.engine.olap.data.impl.OneKeySelection;
import org.eclipse.birt.data.engine.olap.data.impl.RangeSelection;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;

/**
 * 
 */

public class SelectionUtil {

	private SelectionUtil() {
	}

	public static ISelection[] intersect(ISelection[] selections1, ISelection[] selections2) {
		ISelection[] temp = new ISelection[selections1.length + selections2.length];
		int i = 0;
		for (int j = 0; j < selections1.length; j++) {
			temp[i++] = selections1[j];
		}
		for (int j = 0; j < selections2.length; j++) {
			temp[i++] = selections2[j];
		}
		return intersect(temp);
	}

	public static ISelection[] intersect(ISelection[] selections) {
		List rangeSelectionList = new ArrayList();
		List multiKeySelectionList = new ArrayList();
		List oneKeySelectionList = new ArrayList();
		for (int i = 0; i < selections.length; i++) {
			if (selections[i] instanceof EmptySelection) {
				return new ISelection[] { SelectionFactory.createEmptySelection() };
			} else if (selections[i] instanceof RangeSelection) {
				rangeSelectionList.add(selections[i]);
			} else if (selections[i] instanceof MultiKeySelection) {
				multiKeySelectionList.add(selections[i]);
			} else if (selections[i] instanceof OneKeySelection) {
				oneKeySelectionList.add(selections[i]);
			}
		}

		List intersecttList = new ArrayList();
		if (rangeSelectionList.isEmpty() == false) {
			RangeSelection[] rangeSelections = new RangeSelection[rangeSelectionList.size()];
			rangeSelectionList.toArray(rangeSelections);
			intersecttList.add(intersect(rangeSelections));
		}
		if (multiKeySelectionList.isEmpty() == false) {
			MultiKeySelection[] multiKeySelections = new MultiKeySelection[multiKeySelectionList.size()];
			multiKeySelectionList.toArray(multiKeySelections);
			intersecttList.add(intersect(multiKeySelections));
		}
		if (oneKeySelectionList.isEmpty() == false) {
			OneKeySelection[] oneKeySelections = new OneKeySelection[oneKeySelectionList.size()];
			oneKeySelectionList.toArray(oneKeySelections);
			intersecttList.add(intersect(oneKeySelections));
		}
		ISelection[] result = new ISelection[intersecttList.size()];
		intersecttList.toArray(result);
		return result;
	}

	public static ISelection intersect(RangeSelection rangeSelection, OneKeySelection oneSelection) {
		Object[] min = rangeSelection.getMin();
		Object[] max = rangeSelection.getMax();
		boolean containsMin = rangeSelection.isContainsMinKey();
		boolean containsMax = rangeSelection.isContainsMaxKey();
		Object[] key = oneSelection.getKeyValue();
		int ret = CompareUtil.compare(key, min);
		if (ret < 0 || (!containsMin && ret == 0)) {
			return SelectionFactory.createEmptySelection();
		}
		ret = CompareUtil.compare(key, max);
		if (ret > 0 || (!containsMax && ret == 0)) {
			return SelectionFactory.createEmptySelection();
		}
		return SelectionFactory.createOneKeySelection(key);
	}

	public static ISelection intersect(MultiKeySelection mutiKeySelection, OneKeySelection oneKeySelection) {
		Object[] key = oneKeySelection.getKeyValue();
		int ret = CompareUtil.compare(key, mutiKeySelection.getMin());
		if (ret < 0) {
			return SelectionFactory.createEmptySelection();
		}
		ret = CompareUtil.compare(key, mutiKeySelection.getMax());
		if (ret > 0) {
			return SelectionFactory.createEmptySelection();
		}
		if (mutiKeySelection.isSelected(key) == false) {
			return SelectionFactory.createEmptySelection();
		}
		return SelectionFactory.createOneKeySelection(key);
	}

	public static ISelection intersect(RangeSelection s1, MultiKeySelection s2) {
		Object[][] keyValues = s2.getKeyValues();
		List keyValueList = new ArrayList();
		for (int i = 0; i < keyValues.length; i++) {
			if (s1.isSelected(keyValues[i])) {
				keyValueList.add(keyValues[i]);
			}
		}
		if (keyValueList.isEmpty()) {
			return SelectionFactory.createEmptySelection();
		}

		keyValues = new Object[keyValueList.size()][];
		for (int i = 0; i < keyValues.length; i++) {
			keyValues[i] = (Object[]) keyValueList.get(i);
		}
		return SelectionFactory.createMutiKeySelection(keyValues);
	}

	public static ISelection intersect(RangeSelection[] selections) {
		if (selections == null || selections.length == 0)
			return SelectionFactory.createEmptySelection();
		Object[] min = null;
		Object[] max = null;
		boolean containsMin = false;
		boolean containsMax = false;

		min = selections[0].getMin();
		containsMin = selections[0].isContainsMinKey();
		for (int i = 1; i < selections.length; i++) {
			// get the greater minKey as the lower boundary
			if (CompareUtil.compare(selections[i].getMin(), min) > 0) {
				min = selections[i].getMin();
				containsMin = selections[i].isContainsMinKey();
			}
		}

		max = selections[0].getMax();
		containsMax = selections[0].isContainsMaxKey();
		for (int i = 0; i < selections.length; i++) {
			// get the lesser maxKey as the upper boundary
			if (CompareUtil.compare(selections[i].getMax(), max) < 0) {
				max = selections[i].getMax();
				containsMax = selections[i].isContainsMaxKey();
			}
		}

		int ret = CompareUtil.compare(min, max);
		if (ret > 0 || (ret == 0 && (!containsMin || !containsMax))) {
			return SelectionFactory.createEmptySelection();
		}
		return SelectionFactory.createRangeSelection(min, max, containsMin, containsMax);
	}

	public static ISelection intersect(MultiKeySelection[] selections) {
		if (selections == null || selections.length == 0)
			return SelectionFactory.createEmptySelection();
		Object[][] uniqueKeyValues = selections[0].getKeyValues();
		boolean[] removed = new boolean[uniqueKeyValues.length];
		int size = uniqueKeyValues.length;
		for (int i = 1; i < selections.length; i++) {
			Object[][] keyValues = selections[i].getKeyValues();
			for (int k = 0; k < uniqueKeyValues.length; k++) {
				boolean found = false;
				for (int j = 0; j < keyValues.length; j++) {
					if (removed[k] == false && CompareUtil.compare(uniqueKeyValues[k], keyValues[j]) == 0) {
						found = true;
						break;
					}
				}
				if (found == false) {
					removed[k] = true;
					size--;
				}
			}
		}
		if (size == 0)
			return SelectionFactory.createEmptySelection();
		Object[][] intersectedKeyValues = new Object[size][];
		for (int i = 0, j = 0; i < uniqueKeyValues.length; i++) {
			if (removed[i] == false) {
				intersectedKeyValues[j++] = uniqueKeyValues[i];
			}
		}
		return SelectionFactory.createMutiKeySelection(intersectedKeyValues);
	}

	public static ISelection intersect(OneKeySelection[] selections) {
		if (selections == null || selections.length == 0)
			return SelectionFactory.createEmptySelection();
		Object[] key = selections[0].getKeyValue();
		for (int i = 0; i < selections.length; i++) {
			if (CompareUtil.compare(key, selections[i].getKeyValue()) != 0) {
				return SelectionFactory.createEmptySelection();
			}
		}
		return SelectionFactory.createOneKeySelection(key);
	}

	// ================================================================================
	public static ISelection union(OneKeySelection[] selections) {
		List keyValueList = new ArrayList();
		for (int i = 0; i < selections.length; i++) {
			boolean found = false;
			for (int j = 0; j < keyValueList.size(); j++) {
				Object[] keyValue = (Object[]) keyValueList.get(j);
				if (CompareUtil.compare(keyValue, selections[i].getKeyValue()) == 0) {
					found = true;
					break;
				}
			}
			if (found == false)
				keyValueList.add(selections[i].getKeyValue());
		}

		Object[][] keyValues = new Object[keyValueList.size()][];
		for (int i = 0; i < keyValues.length; i++) {
			keyValues[i] = (Object[]) keyValueList.get(i);
		}
		return SelectionFactory.createMutiKeySelection(keyValues);
	}
}
