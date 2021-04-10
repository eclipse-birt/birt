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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration;

/**
 * ReportOSGiLaunchDelegate
 */
public class ReportOSGiLaunchDelegate extends EclipseApplicationLaunchConfiguration implements IReportLaunchConstants {

	ReportLaunchHelper helper;
	public static final String APP_NAME = "application name";//$NON-NLS-1$

	public ReportOSGiLaunchDelegate() {
		helper = new ReportLaunchHelper();
	}

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		helper.init(configuration);

		super.launch(configuration, mode, launch, monitor);
	}

	public String[] getVMArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] args = super.getVMArguments(configuration);

		List arguments = new ArrayList();

		for (int i = 0; i < args.length; i++) {
			arguments.add(args[i]);
		}

		helper.addPortArgs(arguments);
		helper.addUserClassPath(arguments, configuration);
		helper.addFileNameArgs(arguments);
		helper.addEngineHomeArgs(arguments);
		helper.addResourceFolder(arguments);
		helper.addTempFolder(arguments);
		helper.addTypeArgs(arguments);
		helper.addDataLimitArgs(arguments);
		helper.addParameterArgs(arguments);

		return (String[]) arguments.toArray(new String[arguments.size()]);
	}

	public String[] getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] args = super.getProgramArguments(configuration);

		List list = new ArrayList();

		for (int i = 0; i < args.length; i++) {
			list.add(args[i]);
		}

		int idx = list.indexOf("-application"); //$NON-NLS-1$

		if (idx != -1 && (idx + 1) < list.size()) {
			list.set(idx + 1, getApplicationName()); // $NON-NLS-1$
		} else {
			list.add("-application"); //$NON-NLS-1$
			list.add(getApplicationName()); // $NON-NLS-1$
		}

		list.add("-nosplash"); //$NON-NLS-1$

		return (String[]) list.toArray(new String[list.size()]);
	}

	private String getApplicationName() {
		String name = System.getProperty(APP_NAME);
		if (name == null || name.length() == 0) {
			name = "org.eclipse.birt.report.debug.core.ReportDebugger";
		}
		return name;
	}

	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		if ((helper.debugType & DEBUG_TYPE_JAVA_CLASS) == DEBUG_TYPE_JAVA_CLASS) {
			mode = ILaunchManager.DEBUG_MODE;
		} else {
			mode = ILaunchManager.RUN_MODE;
		}

		return new ReportDebuggerVMRunner(super.getVMRunner(configuration, mode),
				(helper.debugType & DEBUG_TYPE_JAVA_SCRIPT) == DEBUG_TYPE_JAVA_SCRIPT, this);
	}

	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		return super.getBuildOrder(configuration, mode);
	}

	public boolean finalLaunchCheck(final ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {

		boolean bool = super.finalLaunchCheck(configuration, mode, monitor);

		if (!bool) {
			return bool;
		}

		return helper.finalLaunchCheck(configuration, mode, monitor);
	}

}
