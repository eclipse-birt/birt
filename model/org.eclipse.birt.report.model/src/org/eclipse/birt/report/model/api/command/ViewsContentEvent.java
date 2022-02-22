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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Notification event that says that multiple views of a container have changed.
 * The target element is the report item.
 * <p>
 * If content is moved from one container element to another, the event with
 * {@link #REMOVE}and that with {@link #ADD}are received respectively.
 *
 */

public class ViewsContentEvent extends NotificationEvent {

	/**
	 * New content element is added.
	 */

	public static final int ADD = 10;

	/**
	 * Content element is removed.
	 */

	public static final int REMOVE = 11;

	/**
	 * Content element is shifted in the same container element.
	 */

	public static final int SHIFT = 12;

	/**
	 * The type of change. One of {@link #ADD},{@link #REMOVE}, or {@link #SHIFT}.
	 */

	private int action;

	/**
	 * The content element causing the event.
	 */

	private DesignElement content;

	/**
	 *
	 */
	protected ContainerContext focus = null;

	/**
	 *
	 * @param containerInfo
	 * @param theContent
	 * @param theAction
	 */

	public ViewsContentEvent(ContainerContext containerInfo, DesignElement theContent, int theAction) {
		super(containerInfo.getElement());
		action = theAction;
		content = theContent;
		focus = containerInfo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	@Override
	public int getEventType() {
		return VIEWS_CONTENT_EVENT;
	}

	/**
	 * Returns the type of action. One of {@link #ADD},{@link #REMOVE}, or
	 * {@link #SHIFT}.
	 *
	 * @return the action causing this event.
	 */

	public int getAction() {
		return action;
	}

	/**
	 * Returns the content element causing this event.
	 *
	 * @return the content element causing this event.
	 */

	public IDesignElement getContent() {
		return content;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#isSame(org.
	 * eclipse.birt.report.model.api.activity.NotificationEvent)
	 */

	@Override
	public boolean isSame(NotificationEvent event) {
		if (!super.isSame(event)) {
			return false;
		}
		ViewsContentEvent contentEvent = (ViewsContentEvent) event;
		if (action != contentEvent.getAction() || !focus.equals(contentEvent.focus)
				|| content != contentEvent.getContent()) {
			return false;
		}
		return true;
	}
}
