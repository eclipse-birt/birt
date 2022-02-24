/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;

/**
 * Notification event that says that the contents of user-defined message is
 * added, dropped, or changed. The target element is the report design.
 * 
 */

public class CustomMsgEvent extends NotificationEvent {

	/**
	 * The translation was added.
	 */

	public static final int ADD = 0;

	/**
	 * The translation was dropped.
	 */

	public static final int DROP = 1;

	/**
	 * The translation changed.
	 */

	public static final int CHANGE = 2;

	/**
	 * The translation that changed.
	 */

	private Translation translation = null;

	/**
	 * The type of change. One of {@link #ADD},{@link #DROP}, or {@link #CHANGE}.
	 */

	private int action;

	/**
	 * Constructs the event with the user-defined message and the action causing
	 * this event.
	 * 
	 * @param design      the report design
	 * @param translation the user-defined message
	 * @param theAction   the action causing this event.
	 */

	public CustomMsgEvent(ReportDesign design, Translation translation, int theAction) {
		super(design);
		this.translation = translation;
		this.action = theAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType(
	 * )
	 */
	public int getEventType() {
		return CUSTOM_MSG_EVENT;
	}

	/**
	 * Returns the action causing this event. One of {@link #ADD}, {@link #DROP}, or
	 * {@link #CHANGE}.
	 * 
	 * @return the action causing this event.
	 */

	public int getAction() {
		return action;
	}

	/**
	 * Returns the user-defined message that changes.
	 * 
	 * @return the user-defined message that changes.
	 */

	public Translation getTranslation() {
		return translation;
	}
}
