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

import java.util.Date;

/**
 * This interface provides methods for a BIRT viewing session.
 * 
 */
public interface IViewingSession {

	/**
	 * Returns the BIRT viewing session ID.
	 * 
	 * @return BIRT viewing session id
	 */
	public abstract String getId();

	/**
	 * Returns the last access Date.
	 * 
	 * @return last access date
	 */
	public abstract Date getLastAccess();

	/**
	 * Refreshes the session by setting its last access date to the current date.
	 * 
	 * @throws IllegalStateException if the session has expired
	 */
	public abstract void refresh();

	/**
	 * Returns the document path name according to given report name.
	 * 
	 * @param request    HTTP request
	 * @param reportFile name of the report design file
	 * @param id         viewer id, or null
	 * @return cached document file path name
	 * @throws IllegalStateException if the session has expired
	 */
	public abstract String getCachedReportDocument(String reportFile, String viewerId);

	/**
	 * Returns the image temp folder for the current session.
	 * 
	 * @param request request
	 * @return temp folder
	 * @throws IllegalStateException if the session has expired
	 */
	public abstract String getImageTempFolder();

	/**
	 * Returns whether this session has expired or was invalidated.
	 * 
	 * @return true if the session has expired
	 */
	public abstract boolean isExpired();

	/**
	 * Invalidates the session.
	 * 
	 * @throws IllegalStateException if the session is locked
	 */
	public abstract void invalidate();

	/**
	 * Adds a lock to this session to prevents its expiration.
	 * 
	 * @throws IllegalStateException if the session has expired
	 */
	public abstract void lock();

	/**
	 * Removes a lock to this session to prevents its expiration.
	 * 
	 * @throws IllegalStateException if the session has expired
	 */
	public abstract void unlock();

	/**
	 * Returns whether this session is locked.
	 * 
	 * @return true if the session is locked, false otherwise
	 */
	public abstract boolean isLocked();

}
