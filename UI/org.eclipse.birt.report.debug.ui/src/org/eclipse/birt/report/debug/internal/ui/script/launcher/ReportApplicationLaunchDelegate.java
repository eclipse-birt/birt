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

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

/**
 * ReportApplicationLaunchDelegate
 */
public class ReportApplicationLaunchDelegate extends LaunchConfigurationDelegate implements IReportLaunchConstants {

	private LaunchConfigurationDelegate delegate;

	private synchronized void ensureDelegate(ILaunchConfiguration configuration) throws CoreException {
		boolean useDefaultEngineHome = configuration.getAttribute(ATTR_USE_DEFULT_ENGINE_HOME, true);

		if (delegate == null || !match(useDefaultEngineHome)) {
			if (useDefaultEngineHome) {
				delegate = new ReportOSGiLaunchDelegate();
			} else {
				delegate = new ReportStandardAppLaunchDelegate();
			}
		}
	}

	private boolean match(boolean useDefaultEngineHome) {
		if (useDefaultEngineHome) {
			return delegate instanceof ReportOSGiLaunchDelegate;
		} else {
			return delegate instanceof ReportStandardAppLaunchDelegate;
		}
	}

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		ensureDelegate(configuration);
		delegate.launch(configuration, mode, launch, monitor);
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		ensureDelegate(configuration);
		return delegate.preLaunchCheck(configuration, mode, monitor);
	}

	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		ensureDelegate(configuration);
		return delegate.finalLaunchCheck(configuration, mode, monitor);
	}

	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		ensureDelegate(configuration);

		if (delegate instanceof ReportOSGiLaunchDelegate) {
			return ((ReportOSGiLaunchDelegate) delegate).getBuildOrder(configuration, mode);
		} else {
			return ((ReportStandardAppLaunchDelegate) delegate).getBuildOrder(configuration, mode);
		}

	}

}
