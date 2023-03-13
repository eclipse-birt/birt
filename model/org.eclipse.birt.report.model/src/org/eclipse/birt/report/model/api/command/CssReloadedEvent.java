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
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;

/**
 * Included css style sheet reload event
 *
 */

public class CssReloadedEvent extends NotificationEvent {

	/**
	 * The css style sheet causing the event.
	 */

	private CssStyleSheet css;

	/**
	 * Constructor.
	 *
	 * @param host the host module in which the css lies in
	 *
	 * @param css  the element that is to reload css.
	 */

	public CssReloadedEvent(Module host, CssStyleSheet css) {
		super(host);
		this.css = css;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	@Override
	public int getEventType() {
		return CSS_RELOADED_EVENT;
	}

	/**
	 * Returns the css causing this event. If the css is <code>null</code>, it means
	 * that the css is not found during the reload-action.
	 *
	 * @return the css causing this event, or null if the css is not found
	 */

	public CssStyleSheet getCss() {
		return css;
	}
}
