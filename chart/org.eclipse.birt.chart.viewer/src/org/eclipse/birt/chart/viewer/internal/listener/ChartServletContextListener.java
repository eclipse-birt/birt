/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
