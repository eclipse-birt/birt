/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.service;

import org.eclipse.birt.report.service.api.IViewerReportService;

public class BirtReportServiceFactory {
	private static IViewerReportService service;

	public synchronized static final void init(IViewerReportService instance) {
		if (service == null) {
			service = instance;
		}
	}

	public static final IViewerReportService getReportService() {
		// TODO: Throw better exception??
		if (service == null) {
			throw new RuntimeException(
					"The service is not initialized!." + " Use BirtReportServiceFactory.init(ServletConfig) to init.");
		}
		return service;
	}
}
