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

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Class to create a IFilterCondition.
 */

public class FilterConditionFactory {

	/**
	 * Condition for an element deleted action.
	 */

	public final static int ELEMENT_DELETED_FILTER_CONDITION = 0;

	/**
	 * Condition for an element added action.
	 */

	public final static int ELEMENT_ADDED_FILTER_CONDITION = 1;

	/**
	 * Condition to filter the same event.
	 */

	public final static int SAME_EVENT_FILTER_CONDITION = 2;

	/**
	 * Creates a filter condition by the condition id.
	 *
	 * @param filterConditionID the filter condition id
	 * @return the generated filter condition with the given id, otherwise null
	 */

	static public IFilterCondition createFilterCondition(int filterConditionID) {
		switch (filterConditionID) {
		case ELEMENT_DELETED_FILTER_CONDITION:
			return new ElementDeletedFilterCondition();
		case ELEMENT_ADDED_FILTER_CONDITION:
			return new ElementAddedFilterCondition();
		case SAME_EVENT_FILTER_CONDITION:
			return new SameEventFilterCondition();
		}
		return null;
	}

	/**
	 * Container element is deleted, events from its contents should be filtered.
	 */

	static class ElementDeletedFilterCondition implements IFilterCondition {
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.activity.IFilterCondition#getFilterEvent(org.
		 * eclipse.birt.report.model.api.activity.NotificationEvent,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */

		@Override
		public int getFilterEvent(NotificationEvent ev1, NotificationEvent ev2) {
			if (!(ev1 instanceof ElementDeletedEvent)) {
				return NO_EVENT;
			}

			ElementDeletedEvent event1 = (ElementDeletedEvent) ev1;
			DesignElement deleted = event1.getTarget();

			if (ev2 instanceof ContentEvent && ((ContentEvent) ev2).getAction() == ContentEvent.REMOVE) {
				ContentEvent event = (ContentEvent) ev2;
				DesignElement container = event.getTarget();

				// Container is deleted, event from the content is filtered.
				if (container == deleted) {
					return RIGHT_EVENT;
				}
			} else if (ev2 instanceof ElementDeletedEvent) {
				ElementDeletedEvent event = (ElementDeletedEvent) ev2;

				// Container is deleted, deleted event from the content is
				// filtered.
				if (event.getContainer() == deleted) {
					return RIGHT_EVENT;
				}
			} else if (ev2 instanceof PropertyEvent) {
				PropertyEvent event = (PropertyEvent) ev2;
				DesignElement target = event.getTarget();

				// Element is deleted, property from the element is filtered.
				if (target == deleted) {
					return RIGHT_EVENT;
				}
			}

			return NO_EVENT;
		}
	}

	/**
	 * Container element is added, events from its contents should be filtered.
	 */

	static class ElementAddedFilterCondition implements IFilterCondition {
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.activity.IFilterCondition#getFilterEvent(org.
		 * eclipse.birt.report.model.api.activity.NotificationEvent,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */

		@Override
		public int getFilterEvent(NotificationEvent ev1, NotificationEvent ev2) {
			if (!(ev1 instanceof ContentEvent && ((ContentEvent) ev1).getAction() == ContentEvent.ADD)) {
				return NO_EVENT;
			}

			// Event 1 says that an element is added.
			ContentEvent event1 = (ContentEvent) ev1;
			DesignElement added = (DesignElement) event1.getContent();

			if (ev2 instanceof ContentEvent) {
				ContentEvent event2 = (ContentEvent) ev2;
				DesignElement container = event2.getTarget();

				// Event from the content is filtered.
				if (container == added) {
					return RIGHT_EVENT;
				}
			} else if (ev2 instanceof PropertyEvent) {
				PropertyEvent event2 = (PropertyEvent) ev2;
				DesignElement target = event2.getTarget();

				// Property from the element is filtered.
				if (target == added) {
					return RIGHT_EVENT;
				}
			}

			return NO_EVENT;
		}
	}

	/**
	 * If second event is the same as the first one, filter it.
	 */

	static class SameEventFilterCondition implements IFilterCondition {
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.model.activity.IFilterCondition#getFilterEvent(org.
		 * eclipse.birt.report.model.api.activity.NotificationEvent,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */

		@Override
		public int getFilterEvent(NotificationEvent ev1, NotificationEvent ev2) {
			if (ev1.isSame(ev2)) {
				return LEFT_EVENT;
			}
			return NO_EVENT;
		}
	}
}
