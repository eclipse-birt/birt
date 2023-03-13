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

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

/**
 * ScriptClasspathTab
 */
public class ScriptClasspathTab extends JavaClasspathTab {

	private static final String PROVIDER_ID = "org.eclipse.birt.report.debug.script.ScriptDebugClasspathProvider"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab#isShowBootpath
	 * ()
	 */
	@Override
	public boolean isShowBootpath() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab#performApply(
	 * org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);

		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);

		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, PROVIDER_ID);
	}

}
