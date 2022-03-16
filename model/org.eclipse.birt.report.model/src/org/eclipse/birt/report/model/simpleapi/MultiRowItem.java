/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Implements of multi row item for extension elements.
 *
 */
public class MultiRowItem extends ReportItem implements IMultiRowItem {

	private final String filterPropName;
	private final String sortPropName;

	/**
	 * Constructor
	 *
	 * @param item
	 */

	public MultiRowItem(ReportItemHandle item) {
		super(item);

		if (item instanceof ListingHandle) {
			filterPropName = IListingElementModel.FILTER_PROP;
			sortPropName = IListingElementModel.SORT_PROP;
		} else if (item instanceof ExtendedItemHandle) {
			filterPropName = IExtendedItemModel.FILTER_PROP;
			sortPropName = null;
		} else {
			filterPropName = null;
			sortPropName = null;
		}
	}

	@Override
	public IFilterCondition[] getFilterConditions() {
		if (filterPropName == null) {
			return null;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(filterPropName);
		Iterator iterator = propHandle.iterator();

		List rList = new ArrayList();
		int count = 0;

		while (iterator.hasNext()) {
			FilterConditionHandle conditionHandle = (FilterConditionHandle) iterator.next();
			FilterConditionImpl f = new FilterConditionImpl(conditionHandle);
			rList.add(f);
			++count;
		}

		return (IFilterCondition[]) rList.toArray(new IFilterCondition[count]);
	}

	@Override
	public ISortCondition[] getSortConditions() {
		if (sortPropName == null) {
			return null;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(sortPropName);
		Iterator iterator = propHandle.iterator();

		List rList = new ArrayList();
		int count = 0;

		while (iterator.hasNext()) {
			SortKeyHandle sortHandle = (SortKeyHandle) iterator.next();
			SortConditionImpl s = new SortConditionImpl(sortHandle);
			rList.add(s);
			++count;
		}

		return (ISortCondition[]) rList.toArray(new ISortCondition[count]);
	}

	/**
	 * Add FilterCondition
	 *
	 * @param condition
	 * @throws SemanticException
	 */

	@Override
	public void addFilterCondition(IFilterCondition condition) throws SemanticException {

		if (filterPropName == null || condition == null) {
			return;
		}
		PropertyHandle propHandle = handle.getPropertyHandle(filterPropName);

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.addItem(condition.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Add SortCondition
	 *
	 * @param condition
	 * @throws SemanticException
	 */

	@Override
	public void addSortCondition(ISortCondition condition) throws SemanticException {
		if (sortPropName == null || condition == null) {
			return;
		}
		PropertyHandle propHandle = handle.getPropertyHandle(sortPropName);
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.addItem(condition.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void removeFilterCondition(IFilterCondition condition) throws SemanticException {
		if (filterPropName == null) {
			return;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(filterPropName);
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.removeItem(condition.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void removeFilterConditions() throws SemanticException {
		if (filterPropName == null) {
			return;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(filterPropName);
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.clearValue();
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void removeSortCondition(ISortCondition condition) throws SemanticException {
		if (sortPropName == null) {
			return;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(sortPropName);

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.removeItem(condition.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	@Override
	public void removeSortConditions() throws SemanticException {
		if (sortPropName == null) {
			return;
		}

		PropertyHandle propHandle = handle.getPropertyHandle(sortPropName);

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			propHandle.clearValue();
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

}
