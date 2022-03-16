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

import java.io.Serializable;
import java.util.Date;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * This class represents a BIRT viewing session.
 */
public class ViewingSession implements IViewingSession, Serializable {
	private static final long serialVersionUID = -5723569084974892854L;

	private static DateFormatter sessionDateFormatter;

	private String httpSessionId;
	private ViewingCache cache;
	private String id;
	private Date lastAccess;
	private boolean expired;
	private int locks;

	ViewingSession(String httpSessionId, ViewingCache cache) {
		Date date = new Date();
		this.httpSessionId = httpSessionId;
		this.id = sessionDateFormatter.format(date);
		this.lastAccess = date;
		this.cache = cache;
		this.locks = 0;
		expired = false;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#getLastAccess()
	 */
	@Override
	public synchronized Date getLastAccess() {
		return lastAccess;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#refresh()
	 */
	@Override
	public synchronized void refresh() {
		lastAccess = new Date();
	}

	/**
	 * @throws ViewingSessionExpiredException
	 * @see org.eclipse.birt.report.session.IViewingSession#getCachedReportDocument(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public synchronized String getCachedReportDocument(String reportFile, String viewerId) {
		checkExpired();
		return cache.getReportDocument(reportFile, httpSessionId, id, viewerId);
	}

	/**
	 * @throws ViewerException
	 * @see org.eclipse.birt.report.session.IViewingSession#getImageTempFolder()
	 */
	@Override
	public synchronized String getImageTempFolder() {
		checkExpired();
		return cache.getImageTempFolder(httpSessionId, id);
	}

	/**
	 * Asserts that the session has not expired.
	 *
	 * @throws ViewerException
	 */
	private void checkExpired() {
		if (expired) {
			throw new IllegalStateException(
					BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_VIEWING_SESSION_EXPIRED));
		}
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#isExpired()
	 */
	@Override
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @throws ViewerException
	 * @see org.eclipse.birt.report.session.IViewingSession#invalidate()
	 */
	@Override
	public synchronized void invalidate() {
		checkExpired();
		if (locks > 0) {
			throw new IllegalStateException(
					BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_VIEWING_SESSION_LOCKED));
		}

		expired = true;
		deleteCache();
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#lock()
	 */
	@Override
	public synchronized void lock() {
		checkExpired();
		locks++;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#unlock()
	 */
	@Override
	public synchronized void unlock() {
		checkExpired();
		if (locks > 0) {
			locks--;
		}
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSession#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return locks > 0;
	}

	/**
	 * Deletes the cache for the current viewing session.
	 */
	private void deleteCache() {
		cache.clearSession(httpSessionId, id);
	}

	static {
		sessionDateFormatter = new DateFormatter(IBirtConstants.SESSION_ID_DATE_FORMAT);
	}

}
