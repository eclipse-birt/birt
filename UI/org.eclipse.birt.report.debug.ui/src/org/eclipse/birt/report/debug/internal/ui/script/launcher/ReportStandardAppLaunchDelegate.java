/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.birt.report.debug.internal.core.launcher.ReportLauncher;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * ReportStandardAppLaunchDelegate
 */
public class ReportStandardAppLaunchDelegate extends JavaLaunchDelegate implements IReportLaunchConstants {

	// add the path for the developer
	static final String CORE_BIN = "CoreOutput"; //$NON-NLS-1$
	static final String UI_BIN = "UIOutput"; //$NON-NLS-1$

	ReportLaunchHelper helper;

	public ReportStandardAppLaunchDelegate() {
		helper = new ReportLaunchHelper();
	}

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		helper.init(configuration);

		super.launch(configuration, mode, launch, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#
	 * verifyWorkingDirectory(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public File verifyWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#
	 * getJavaLibraryPath(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public String[] getJavaLibraryPath(ILaunchConfiguration configuration) throws CoreException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#getVMRunner
	 * (org.eclipse.debug.core.ILaunchConfiguration, java.lang.String)
	 */
	public IVMRunner getVMRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		IVMInstall vm = verifyVMInstall(configuration);
		return new StandardScriptVMRunner(vm, this);
	}

	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		return ReportLauncher.class.getName();
	}

	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		return super.getBuildOrder(configuration, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.LaunchConfigurationDelegate#finalLaunchCheck(org
	 * .eclipse.debug.core.ILaunchConfiguration, java.lang.String,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean finalLaunchCheck(final ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {

		boolean bool = super.finalLaunchCheck(configuration, mode, monitor);

		if (!bool) {
			return bool;
		}

		return helper.finalLaunchCheck(configuration, mode, monitor);
	}

	String getFileName() {
		return helper.fileName;
	}

	String getEngineHome() {
		return helper.engineHome;
	}

	String getTempFolder() {
		return helper.tempFolder;
	}

	String getTargetFormat() {
		return helper.targetFormat;
	}

	boolean isOpenTargetFile() {
		return helper.isOpenTargetFile;
	}

	int getDebugType() {
		return helper.debugType;
	}

	int getTaskType() {
		return helper.taskType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#
	 * getClasspath(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
		ScriptDebugClasspathProvider provider = new ScriptDebugClasspathProvider();
		IRuntimeClasspathEntry[] entries = provider.computeExtraBootClasspath(configuration);
		entries = JavaRuntime.resolveRuntimeClasspath(entries, configuration);
		List userEntries = new ArrayList(entries.length);
		Set set = new HashSet(entries.length);
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].getClasspathProperty() == IRuntimeClasspathEntry.USER_CLASSES) {
				String location = entries[i].getLocation();
				if (location != null) {
					if (!set.contains(location)) {
						userEntries.add(location);
						set.add(location);
					}
				}
			}
		}

		return (String[]) userEntries.toArray(new String[userEntries.size()]);
	}

}
