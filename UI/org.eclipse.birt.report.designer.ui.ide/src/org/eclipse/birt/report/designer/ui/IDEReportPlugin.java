/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui;

import java.util.Hashtable;

import org.eclipse.birt.report.designer.internal.ui.ide.adapters.IDEReportClasspathResolver;
import org.eclipse.birt.report.designer.internal.ui.ide.adapters.IDEResourceSynchronizer;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerView;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * IDEReportPlugin
 */
public class IDEReportPlugin extends AbstractUIPlugin {

	private ServiceRegistration syncService;
	private ServiceRegistration reportClasspathService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		ReportPlugin.getDefault().addIgnoreViewID(LibraryExplorerView.ID);

		// make higher ranking than default service
		Hashtable<String, Object> dict = new Hashtable<>();
		dict.put(Constants.SERVICE_RANKING, Integer.valueOf(3));

		syncService = context.registerService(IReportResourceSynchronizer.class.getName(),
				new IDEResourceSynchronizer(), dict);

		reportClasspathService = context.registerService(IReportClasspathResolver.class.getName(),
				new IDEReportClasspathResolver(), dict);

	}

	@Override
	public void stop(BundleContext context) throws Exception {

		if (syncService != null) {
			syncService.unregister();
			syncService = null;
		}

		if (reportClasspathService != null) {
			reportClasspathService.unregister();
			reportClasspathService = null;
		}

		super.stop(context);
	}

}
