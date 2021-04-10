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
