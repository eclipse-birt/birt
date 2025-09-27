/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.viewer.internal.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;

/**
 * 
 */

public class ChartHttpSessionListener implements HttpSessionListener
{

	/**
	 * After session created
	 * 
	 * @see jakarta.servlet.http.HttpSessionListener#sessionCreated(jakarta.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated( HttpSessionEvent event )
	{
	}

	/**
	 * When session destroyed
	 * 
	 * @see jakarta.servlet.http.HttpSessionListener#sessionDestroyed(jakarta.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed( HttpSessionEvent event )
	{
		String sessionId = event.getSession( ).getId( );
		ChartImageManager.clearSessionFiles( sessionId, event.getSession( )
				.getServletContext( ) );
	}

}
