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

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.StandardClasspathProvider;

/**
 * ScriptDebugClasspathProvider
 */
public class ScriptDebugClasspathProvider extends StandardClasspathProvider {

	public IRuntimeClasspathEntry[] computeExtraBootClasspath(ILaunchConfiguration configuration) throws CoreException {
		return recoverRuntimePath(configuration, IReportLaunchConstants.ATTR_CLASSPATH);
	}

	public IRuntimeClasspathEntry[] computeUserClasspath(ILaunchConfiguration configuration) throws CoreException {
		return recoverRuntimePath(configuration, IJavaLaunchConfigurationConstants.ATTR_CLASSPATH);
	}

}
