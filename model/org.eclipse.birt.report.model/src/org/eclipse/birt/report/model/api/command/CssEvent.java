/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.css.CssStyleSheet;

/**
 * Included css style sheet event
 *
 */

public class CssEvent extends NotificationEvent {
	/**
	 * New css is added.
	 */

	public static final int ADD = 1;

	/**
	 * Css is dropped.
	 */

	public static final int DROP = 2;

	/**
	 * Css is shifted.
	 */

	public static final int SHIFT = 3;

	/**
	 * The type of change. One of {@link #ADD}, or {@link #DROP}.
	 */

	private int action;

	/**
	 * The css causing the event.
	 */

	private CssStyleSheet css;

	/**
	 * Constructs the event with the added or dropped css and action.
	 * 
	 * @param css    the css causing this event
	 * @param action th etype of change
	 */

	public CssEvent(CssStyleSheet css, int action) {
		this.css = css;
		this.action = action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType() {
		return CSS_EVENT;
	}

	/**
	 * Returns the type of change.
	 * 
	 * @return the type of change.
	 */

	public int getAction() {
		return action;
	}

	/**
	 * Returns the css causing this event.
	 * 
	 * @return the css causing this event
	 */

	public CssStyleSheet getCss() {
		return css;
	}

}
