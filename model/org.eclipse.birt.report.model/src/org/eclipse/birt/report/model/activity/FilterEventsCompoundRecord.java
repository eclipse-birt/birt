/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.birt.report.model.api.activity.IEventFilter;
import org.eclipse.birt.report.model.api.activity.TransactionOption;

/**
 * This compound record will do event filtering when it is executed, undone or
 * redone.
 * 
 */

public class FilterEventsCompoundRecord extends CompoundRecord {

	/**
	 * Indicates if it is the outer most filter event transaction.
	 */

	protected boolean isOutermostFilterTrans = false;

	/**
	 * Constructor.
	 * 
	 * @param text      the localized label text
	 * @param outerMost indicates if it is the outer most filter event transaction.
	 */

	public FilterEventsCompoundRecord(String text, boolean outerMost) {
		super(text);
		this.isOutermostFilterTrans = outerMost;
		buildOption();
	}

	/**
	 * Builds the options for this transaction.
	 */

	protected void buildOption() {
		if (!isOutermostFilterTrans)
			return;
		options = new TransactionOption();
		List<IFilterCondition> conds = new ArrayList<IFilterCondition>();
		conds.add(FilterConditionFactory.createFilterCondition(FilterConditionFactory.ELEMENT_ADDED_FILTER_CONDITION));
		conds.add(
				FilterConditionFactory.createFilterCondition(FilterConditionFactory.ELEMENT_DELETED_FILTER_CONDITION));
		conds.add(FilterConditionFactory.createFilterCondition(FilterConditionFactory.SAME_EVENT_FILTER_CONDITION));
		IEventFilter filter = new EventFilter(conds);
		options.setEventfilter(filter);
		options.setSendTime(TransactionOption.OUTMOST_TRANSACTION_SEND_TIME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#sendNotifcations
	 * (java.util.Stack)
	 */

	protected void performPostTasks(Stack<CompoundRecord> transStack) {
		// The current transaction is started as hold events. Checks all its
		// outer transactions, if any of them is started as hold events, the
		// notifications should still be held. Otherwise, all events
		// relating to this transaction is collected, filtered and then sent
		// out.

		if (!isOutermostFilterTrans)
			return;

		super.performPostTasks(transStack);
	}

}
