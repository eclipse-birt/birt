/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.listener;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

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
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent event) {
		// Create Tasks map
		HttpSession session = event.getSession();
		session.setAttribute(IBirtConstants.TASK_MAP, new HashMap());
	}

	/**
	 * When session destroyed
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
	}

}
