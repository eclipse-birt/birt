/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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
	String getHttpSessionId();

	/**
	 * Creates a new BIRT viewing session for the given HTTP session.
	 *
	 * @param httpSession HTTP session
	 * @throws ViewerException
	 */
	IViewingSession createSession() throws ViewerException;

	/**
	 * Returns a BIRT viewing session.
	 *
	 * @param id     BIRT viewing session ID
	 * @param create create flag, if true and no session exists, create it
	 * @return
	 * @throws ViewingSessionExpiredException
	 */
	IViewingSession getSession(String id);

	/**
	 * Deletes all BIRT viewing sessions from the given HTTP session.
	 *
	 * @param httpSession HTTP session
	 */
	void invalidate();

}
