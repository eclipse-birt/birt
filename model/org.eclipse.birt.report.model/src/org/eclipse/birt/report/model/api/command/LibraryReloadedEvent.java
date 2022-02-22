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
import org.eclipse.birt.report.model.elements.Library;

/**
 * Events indicating that the library is reloaded.
 */

public class LibraryReloadedEvent extends NotificationEvent {

	/**
	 * The library causing the event.
	 */

	private Library library;

	/**
	 * Constructor.
	 *
	 * @param host    the host module in which the library lies in
	 *
	 * @param library the element that is to reload library.
	 */

	public LibraryReloadedEvent(Module host, Library library) {
		super(host);
		this.library = library;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	@Override
	public int getEventType() {
		return LIBRARY_RELOADED_EVENT;
	}

	/**
	 * Returns the library causing this event. If the library is <code>null</code>,
	 * it means that the library is not found during the reload-action.
	 *
	 * @return the library causing this event, or null if the library is not found
	 */

	public Library getLibrary() {
		return library;
	}

}
