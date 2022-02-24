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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Utility class to easily access ViewingSessionManager from HTTP requests.
 */
public class ViewingSessionUtil {
	public static ViewingCache viewingCache = null;

	public static ViewingSessionConfig defaultConfig = new ViewingSessionConfig();

	/**
	 * Returns the BIRT viewing session manager for the current HTTP session. If
	 * none is available, creates one.
	 * 
	 * @param request request
	 * @param create  create flag, if true and no BIRT viewing session manager
	 *                exists in the session, then create one.
	 * @return instance of BIRT viewing session manager associated with the current
	 *         HTTP session
	 */
	public static IViewingSessionManager getSessionManager(HttpServletRequest request, boolean create) {
		HttpSession httpSession = request.getSession(create);
		if (httpSession != null) {
			IViewingSessionManager sessionManager = (IViewingSessionManager) httpSession
					.getAttribute(IBirtConstants.ATTRIBUTE_VIEWING_SESSION_MANAGER);
			if (sessionManager == null && create) {
				long aSessionTimeout = defaultConfig.getSessionTimeout();
				if (aSessionTimeout == 0l) {
					// defaults to the master session value
					aSessionTimeout = httpSession.getMaxInactiveInterval();
					// infinite session is not allowed because it would cause
					// the cache to increase without ever being cleaned
					if (aSessionTimeout <= 0) {
						aSessionTimeout = ViewingSessionConfig.DEFAULT_SESSION_TIMEOUT;
					}
					defaultConfig.setSessionTimeout(aSessionTimeout);
				}
				sessionManager = new ViewingSessionManager(viewingCache, httpSession.getId(), defaultConfig);
				httpSession.setAttribute(IBirtConstants.ATTRIBUTE_VIEWING_SESSION_MANAGER, sessionManager);
			}
			return sessionManager;
		} else {
			return null;
		}
	}

	/**
	 * Returns an existing BIRT viewing session. The BIRT viewing session is first
	 * searched inside the ATTR_VIEWING_SESSION request. If it is not found,
	 * retrieves it from the ViewingSessionManager.
	 * 
	 * @param request request
	 * @return BIRT viewing session
	 * @throws IllegalStateException if the session expired
	 */
	public static IViewingSession getSession(HttpServletRequest request) {
		IViewingSession session = (IViewingSession) request.getAttribute(ParameterAccessor.ATTR_VIEWING_SESSION);
		if (session == null) {
			// the session is retrieved from the manager only the first time
			// and is this way also internally refreshed (access time update)
			IViewingSessionManager manager = getSessionManager(request, false);
			if (manager != null) {
				session = manager.getSession(getSessionId(request));
				// save the object in a request attribute
				request.setAttribute(ParameterAccessor.ATTR_VIEWING_SESSION, session);
			}
		}
		return session;
	}

	/**
	 * Creates a new BIRT viewing session. The session is automatically cached in
	 * the ATTR_VIEWING_SESSION request attribute for the duration of this request
	 * 
	 * @param request request
	 * @param create  create flag, if true, creates a new session
	 * @return BIRT viewing session
	 * @throws IllegalStateException if the session manager expired
	 * @throws ViewerException       if no new session could be created
	 */
	public static IViewingSession createSession(HttpServletRequest request) throws ViewerException {
		IViewingSessionManager manager = getSessionManager(request, true);
		IViewingSession session = manager.createSession();
		// save the object in a request attribute
		request.setAttribute(ParameterAccessor.ATTR_VIEWING_SESSION, session);
		return session;
	}

	/**
	 * Returns the sub-session id appended, if available.
	 * 
	 * @param request request
	 * @return sub session id
	 */
	public static String getSessionId(HttpServletRequest request) {
		String sessionId = request.getParameter(ParameterAccessor.PARAM_VIEWING_SESSION_ID);
		if (sessionId == null) {
			sessionId = (String) request.getAttribute(ParameterAccessor.PARAM_VIEWING_SESSION_ID);
		}
		return sessionId;
	}

}
