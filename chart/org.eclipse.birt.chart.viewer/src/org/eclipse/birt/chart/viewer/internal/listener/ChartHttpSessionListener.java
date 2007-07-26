/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;
import org.eclipse.birt.core.framework.PlatformConfig;

/**
 * 
 */

public class ChartHttpSessionListener implements HttpSessionListener
{

	/**
	 * After session created
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated( HttpSessionEvent event )
	{
		// Initialize chart engine in standalone mode
		PlatformConfig config = new PlatformConfig( );
		config.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
		ChartEngine.instance( config );
	}

	/**
	 * When session destroyed
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed( HttpSessionEvent event )
	{
		String sessionId = event.getSession( ).getId( );
		ChartImageManager.clearSessionFiles( sessionId );
	}

}
