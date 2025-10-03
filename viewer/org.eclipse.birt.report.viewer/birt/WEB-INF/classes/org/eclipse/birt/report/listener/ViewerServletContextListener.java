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

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.BirtViewerReportService;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Servlet Context Listener for BIRT viewer web application. Do some necessary
 * jobs when web application servelt loading it or destroying it.
 * <p>
 */
public class ViewerServletContextListener implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContextListener#contextDestroyed(jakarta.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// When trying to destroy application, shutdown Platform and
		// ReportEngineService.
		Platform.shutdown();
		ReportEngineService.shutdown();

		// Reset initialized parameter
		ParameterAccessor.reset();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContextListener#contextInitialized(jakarta.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ParameterAccessor.initParameters(event.getServletContext());
		IViewerReportService instance = new BirtViewerReportService(event.getServletContext());
		BirtReportServiceFactory.init(instance);
		try {
			BirtReportServiceFactory.getReportService().setContext(event.getServletContext(), null);
		} catch (BirtException e) {
			e.printStackTrace();
		}
	}
}
