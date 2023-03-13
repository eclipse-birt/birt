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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * BIRT viewing session manager.<br />
 * This class manages the BIRT viewing sessions that reside under a master HTTP
 * session. Each HTTP session can have more than one BIRT viewing session. This
 * class has two ways to trigger the cleanup:<br />
 * <ul>
 * <li>After a given timeout value is reached. This value is checked after each
 * call to createSession().</li>
 * <li>After a given BIRT viewing session count threshold has been reached.
 * After each cleanup, the session count threshold will be increased using the
 * following formula: <code>sessionCountThreshold = remainingSessionsCount +
 * remainingSessionsCount * sessionCountThreshold</code>. The
 * remainingSessionsCount is the number of sessions that remain after cleanup.
 * If the result of this calculation is smaller than
 * minimumSessionCountThreshold, then sessionCountThreshold will take the value
 * of minimumSessionCountThreshold.</li>
 * </ul>
 */
public class ViewingSessionManager implements IViewingSessionManager, HttpSessionBindingListener, Serializable {
	private static final long serialVersionUID = -7623325281275814412L;

	private ViewingCache viewingCache;
	private long nextCleanupTime;

	/**
	 * Linked hash map containing the ViewingSession objects in access order.
	 */
	private Map<String, IViewingSession> sessions;

	/**
	 * HTTP session ID to which the contained BIRT viewing sessions belong.
	 */
	private String httpSessionId;

	/**
	 * Expired flag.
	 */
	private boolean expired;

	/**
	 * Session count threshold after which the cleanup process will be triggered.
	 * This value will change dynamically according to the number of remaining
	 * sessions after cleanup.
	 */
	private int sessionCountThreshold;

	private ViewingSessionConfig config;

	/**
	 * Wrapper for the IViewingSession interface, to hook the interface's methods.
	 */
	private static class ViewingSessionWrapper implements IViewingSession, Serializable {
		private static final long serialVersionUID = -5837896305154946951L;

		private IViewingSession session;
		private ViewingSessionManager manager;

		public ViewingSessionWrapper(ViewingSessionManager manager, IViewingSession session) {
			this.manager = manager;
			this.session = session;
		}

		public IViewingSession getWrappedSession() {
			return session;
		}

		@Override
		public String getCachedReportDocument(String reportFile, String viewerId) {
			return session.getCachedReportDocument(reportFile, viewerId);
		}

		@Override
		public String getId() {
			return session.getId();
		}

		@Override
		public String getImageTempFolder() {
			return session.getImageTempFolder();
		}

		@Override
		public Date getLastAccess() {
			return session.getLastAccess();
		}

		@Override
		public void invalidate() {
			synchronized (manager) {
				session.invalidate();
				// remove the session from the map
				manager.sessions.remove(session.getId());
			}
		}

		@Override
		public boolean isExpired() {
			return session.isExpired();
		}

		@Override
		public boolean isLocked() {
			return session.isLocked();
		}

		@Override
		public void lock() {
			session.lock();
		}

		@Override
		public void unlock() {
			session.unlock();
			manager.refreshSession(session);
			manager.cleanUp();
		}

		@Override
		public void refresh() {
			manager.refreshSession(session);
		}
	}

	/**
	 * Instantiates a new viewing session manager.
	 *
	 * @param viewingCache  viewing cache instance
	 * @param httpSessionId HTTP session ID
	 * @param config        viewing session configuration
	 */
	public ViewingSessionManager(ViewingCache viewingCache, String httpSessionId, ViewingSessionConfig config) {
		this.httpSessionId = httpSessionId;
		this.viewingCache = viewingCache;
		this.config = config;
		this.sessionCountThreshold = config.getMinimumSessionCountThreshold();

		// using LinkedHashMap to keep the insertion order and access time (LRU)
		this.sessions = new LinkedHashMap<>(config.getMinimumSessionCountThreshold(),
				config.getSessionCountThresholdFactor(), true);
		this.expired = false;
		this.nextCleanupTime = new Date().getTime() + config.getSessionTimeout() * 1000l;
	}

	/**
	 * Returns the cache manager used by this manager.
	 *
	 * @return the cacheManager
	 */
	public ViewingCache getCacheManager() {
		return viewingCache;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSessionManager#getHttpSessionId()
	 */
	@Override
	public String getHttpSessionId() {
		return httpSessionId;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSessionManager#createSession()
	 */
	@Override
	public synchronized IViewingSession createSession() throws ViewerException {
		checkExpired();
		cleanUp();

		if (config.getMaximumSessionCount() > 0 && sessions.size() >= config.getMaximumSessionCount()) {
			switch (config.getMaxSessionCountPolicy()) {
			case SESSION_POLICY_DISCARD_NEW:
				throw new ViewerException(
						BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_VIEWING_SESSION_MAX_REACHED));
			case SESSION_POLICY_DISCARD_OLDEST:
				if (!deleteOldestSession()) {
					// no oldest session could be removed (lock)
					throw new ViewerException(
							BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_VIEWING_SESSION_MAX_REACHED));
				}
				break;
			}
		}
		IViewingSession newSession = new ViewingSessionWrapper(this, new ViewingSession(httpSessionId, viewingCache));
		sessions.put(newSession.getId(), newSession);
		return newSession;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSessionManager#getSession(java
	 *      .lang.String)
	 */
	@Override
	public synchronized IViewingSession getSession(String id) {
		checkExpired();

		ViewingSessionWrapper session = (ViewingSessionWrapper) sessions.get(id);
		if (session != null) {
			session.getWrappedSession().refresh();
		}
		return session;
	}

	/**
	 * @see org.eclipse.birt.report.session.IViewingSessionManager#invalidate()
	 */
	@Override
	public synchronized void invalidate() {
		if (expired) {
			return;
		}
		expired = true;
		try {
			for (Iterator<Map.Entry<String, IViewingSession>> i = sessions.entrySet().iterator(); i.hasNext();) {
				Map.Entry<String, IViewingSession> entry = i.next();
				IViewingSession session = ((ViewingSessionWrapper) entry.getValue()).getWrappedSession();
				if (!session.isExpired()) {
					while (session.isLocked()) {
						session.unlock();
					}
					session.invalidate();
				}
			}
		} finally {
			// clear master session cache
			viewingCache.clearSession(httpSessionId, null);
			sessions.clear();
		}
	}

	/**
	 * Refreshes the given session by calling its refresh() method and by updating
	 * the internal map order.
	 *
	 * @param session viewing session
	 */
	private synchronized void refreshSession(IViewingSession session) {
		// touches the linked hash map to make it move
		// the entry to the end
		sessions.get(session.getId());
		session.refresh();
	}

	/**
	 * Removes a session id from the map.
	 *
	 * @param id session id
	 */
	public synchronized void removeSession(String id) {
		sessions.remove(id);
	}

	/**
	 * Requests a cleanup operation. The operation is only performed if the session
	 * count threshold or the timeout value has been reached.
	 *
	 * @param sessions sessions
	 */
	private synchronized void cleanUp() {
		long now = new Date().getTime();
		if (now >= nextCleanupTime || sessions.size() > sessionCountThreshold) {
			doCleanup();
			if (sessions.size() > 0) {
				// the oldest date is the first entry in the linked hash map
				IViewingSession oldestSession = sessions.values().iterator().next();
				nextCleanupTime = oldestSession.getLastAccess().getTime() + config.getSessionTimeout() * 1000l;
			} else {
				nextCleanupTime = now + config.getSessionTimeout() * 1000l;
			}

			int minimumThreshold = config.getMinimumSessionCountThreshold();
			float factor = config.getSessionCountThresholdFactor();
			sessionCountThreshold = sessions.size() + (int) (sessions.size() * factor);
			if (sessionCountThreshold < minimumThreshold) {
				sessionCountThreshold = minimumThreshold;
			}
		}
	}

	/**
	 * Checks whether there are existing sessions that have expired and clean them
	 * up accordingly.
	 *
	 * @param sessions sessions
	 */
	private synchronized void doCleanup() {
		if (sessions.size() == 0) {
			return;
		}
		long sessionTimeout = config.getSessionTimeout() * 1000l;
		long currentTime = new java.util.Date().getTime() - sessionTimeout;
		for (Iterator<Map.Entry<String, IViewingSession>> i = sessions.entrySet().iterator(); i.hasNext();) {
			Map.Entry<String, IViewingSession> entry = i.next();
			IViewingSession session = entry.getValue();
			Date lastAccess = session.getLastAccess();
			if (currentTime >= lastAccess.getTime() && !session.isLocked()) {
				if (!session.isExpired()) {
					((ViewingSessionWrapper) session).getWrappedSession().invalidate();
				}
				i.remove();
			} else {
				// since the linked hash map is ordered by insertion, the
				// iteration will stop as soon as the first non-expired session
				// is met.
				break;
			}
		}
	}

	/**
	 * Deletes the oldest session that is not locked regardless whether it is has
	 * expired or not.
	 */
	private boolean deleteOldestSession() {
		for (Iterator<Map.Entry<String, IViewingSession>> i = sessions.entrySet().iterator(); i.hasNext();) {
			Map.Entry<String, IViewingSession> entry = i.next();
			IViewingSession session = entry.getValue();
			if (!session.isLocked()) {
				((ViewingSessionWrapper) session).getWrappedSession().invalidate();
				i.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Asserts that the session has not expired.
	 *
	 * @throws ViewingSessionExpiredException
	 */
	private void checkExpired() {
		if (expired) {
			throw new IllegalStateException(
					BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_VIEWING_SESSION_EXPIRED));
		}
	}

	/**
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
	}

	/**
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		synchronized (this) {
			if (!expired) {
				invalidate();
			}
		}
	}

}
