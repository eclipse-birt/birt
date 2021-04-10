/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.session;

import org.eclipse.birt.report.exception.ViewerException;

public interface IViewingSessionManager {

	/**
	 * Returns the HTTP session ID.
	 * 
	 * @return the httpSessionId
	 */
	public abstract String getHttpSessionId();

	/**
	 * Creates a new BIRT viewing session for the given HTTP session.
	 * 
	 * @param httpSession HTTP session
	 * @throws ViewerException
	 */
	public abstract IViewingSession createSession() throws ViewerException;

	/**
	 * Returns a BIRT viewing session.
	 * 
	 * @param id     BIRT viewing session ID
	 * @param create create flag, if true and no session exists, create it
	 * @return
	 * @throws ViewingSessionExpiredException
	 */
	public abstract IViewingSession getSession(String id);

	/**
	 * Deletes all BIRT viewing sessions from the given HTTP session.
	 * 
	 * @param httpSession HTTP session
	 */
	public abstract void invalidate();

}
