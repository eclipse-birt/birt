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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.IEventFilter;

/**
 * Implements an EventFilter class. It does the filter operations by the
 * included filter conditions.
 */

public class EventFilter implements IEventFilter {

	/**
	 * A list of filter condition instance.
	 */

	private List<IFilterCondition> conditions = new ArrayList<>();

	/**
	 * Constructs the event filter with filter condition list.
	 *
	 * @param conds
	 */

	public EventFilter(List<IFilterCondition> conds) {
		this.conditions = conds;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.activity.IEventFilter#filter(java.util
	 * .List)
	 */

	@Override
	public List<RecordTask> filter(List<RecordTask> events) {
		// do some boundary checks

		if (events == null || events.isEmpty()) {
			return Collections.emptyList();
		}
		if (conditions == null || conditions.isEmpty()) {
			return events;
		}

		List<RecordTask> filteredEvents = new ArrayList<>(events);

		int size = filteredEvents.size();

		if (size == 1) {
			return events;
		}

		for (Iterator<IFilterCondition> iter = conditions.iterator(); iter.hasNext();) {
			IFilterCondition filter = iter.next();

			if (filter == null) {
				continue;
			}

			for (int i = 0; i < size; i++) {
				NotificationRecordTask wrapper1 = (NotificationRecordTask) filteredEvents.get(i);

				for (int j = 0; j < size; j++) {
					if (j == i) { // self compare
						continue;
					}

					NotificationRecordTask wrapper2 = (NotificationRecordTask) filteredEvents.get(j);

					// if both are filtered, do nothing

					if (wrapper2.isFiltered() && wrapper1.isFiltered()) {
						continue;
					}

					// do filter operations

					int filteredEvent = filter.getFilterEvent(wrapper1.getEvent(), wrapper2.getEvent());
					if (filteredEvent != IFilterCondition.NO_EVENT) {
						doFilter(filteredEvent, wrapper1, wrapper2);
					}
				}
			}
		}

		return new ArrayList<>(filteredEvents);
	}

	/**
	 * Applies filter condition to the tasks.
	 *
	 * @param filteredEvent the filter condition to apply
	 * @param wrapper1      the task to do
	 * @param wrapper2      the task to do
	 */

	private void doFilter(int filteredEvent, NotificationRecordTask wrapper1, NotificationRecordTask wrapper2) {
		assert wrapper1 != null && wrapper2 != null;
		switch (filteredEvent) {
		case IFilterCondition.LEFT_EVENT:
			wrapper1.setFiltered(true);
			return;
		case IFilterCondition.RIGHT_EVENT:
			wrapper2.setFiltered(true);
			return;
		case IFilterCondition.BOTH_EVENT:
			wrapper1.setFiltered(true);
			wrapper2.setFiltered(true);
		}
	}

}
