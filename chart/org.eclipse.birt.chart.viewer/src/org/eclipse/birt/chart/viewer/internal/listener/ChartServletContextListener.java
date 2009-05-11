/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;

/**
 * 
 */

public class ChartServletContextListener implements ServletContextListener
{

	public void contextDestroyed( ServletContextEvent event )
	{
		ChartImageManager.dispose( event.getServletContext( ) );
	}

	public void contextInitialized( ServletContextEvent event )
	{
		ChartImageManager.init( event.getServletContext( ) );
	}

}
