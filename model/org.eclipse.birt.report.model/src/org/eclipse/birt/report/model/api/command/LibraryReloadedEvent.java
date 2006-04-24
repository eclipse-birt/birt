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
import org.eclipse.birt.report.model.elements.Library;

/**
 * Events indicating that the library is reloaded.
 */

public class LibraryReloadedEvent extends NotificationEvent
{

	/**
	 * The library causing the event.
	 */

	private Library library;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element that is to reload library.
	 */

	public LibraryReloadedEvent( Library library )
	{
		super( library.getHost( ) );
		this.library = library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.NotificationEvent#getEventType()
	 */

	public int getEventType( )
	{
		return LIBRARY_RELOADED_EVENT;
	}

	/**
	 * Returns the library causing this event.
	 * 
	 * @return the library causing this event
	 */

	public Library getLibrary( )
	{
		return library;
	}

}
