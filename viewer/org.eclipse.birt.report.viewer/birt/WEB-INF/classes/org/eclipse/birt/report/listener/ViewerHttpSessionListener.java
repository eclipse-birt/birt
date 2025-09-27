/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
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

package org.eclipse.birt.report.listener;

import java.util.HashMap;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import org.eclipse.birt.report.IBirtConstants;

/**
 * HttpSession Listener for BIRT viewer web application. Do some necessary jobs
 * when create a new HttpSession or destroy it.
 * <p>
 */
public class ViewerHttpSessionListener implements HttpSessionListener {

	/**
	 * After session created
	 *
	 * @see jakarta.servlet.http.HttpSessionListener#sessionCreated(jakarta.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// Create Tasks map
		HttpSession session = event.getSession();
		session.setAttribute(IBirtConstants.TASK_MAP, new HashMap());
	}

	/**
	 * When session destroyed
	 *
	 * @see jakarta.servlet.http.HttpSessionListener#sessionDestroyed(jakarta.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
	}

}
