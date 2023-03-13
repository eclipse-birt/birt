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

/**
 * Notification event that says that the module need refreshing. The listener
 * can find out which module changed by calling
 * {@link org.eclipse.birt.report.model.api.activity.NotificationEvent#getTarget}(
 * ).
 */

public class LibraryChangeEvent extends ResourceChangeEvent {

	/**
	 * Constructor.
	 *
	 * @param path the file path of the changed library.
	 */

	public LibraryChangeEvent(String path) {
		super(path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	@Override
	public int getEventType() {
		return LIBRARY_CHANGE_EVENT;
	}

}
