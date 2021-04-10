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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 */

public class ScriptLaunchShortcut implements ILaunchShortcut {

	private static final Logger logger = Logger.getLogger(ScriptLaunchShortcut.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.
	 * ISelection, java.lang.String)
	 */
	public void launch(ISelection selection, String mode) {
		// don't support now

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
	 * java.lang.String)
	 */
	public void launch(IEditorPart editor, String mode) {
		Object obj = editor.getEditorInput();
		if (!(obj instanceof FileEditorInput)) {
			return;
		}

		FileEditorInput input = (FileEditorInput) obj;
		String fileName = input.getPath().toOSString();
		ILaunchConfiguration config = findLaunchConfiguration(fileName, getConfigurationType());
		if (config != null) {
			DebugUITools.launch(config, mode);
		}

	}

	/**
	 * @return
	 */
	public static ILaunchConfigurationType getConfigurationType() {
		return getLaunchManager()
				.getLaunchConfigurationType("org.eclipse.birt.report.debug.ui.launchConfigurationType.script");//$NON-NLS-1$
	}

	/**
	 * @return
	 */
	protected static ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}

	/**
	 * @param fileName
	 * @param configType
	 * @return
	 */
	public static ILaunchConfiguration findLaunchConfiguration(String fileName, ILaunchConfigurationType configType) {
		// String fileName = input.getPath( ).toOSString( );
		List candidateConfigs = Collections.EMPTY_LIST;
		try {
			ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurations(configType);
			candidateConfigs = new ArrayList(configs.length);
			for (int i = 0; i < configs.length; i++) {
				ILaunchConfiguration config = configs[i];
				if (config.getAttribute(IReportLaunchConstants.ATTR_REPORT_FILE_NAME, "")//$NON-NLS-1$
						.equals(fileName)) {
					candidateConfigs.add(config);
				}
			}
		} catch (CoreException e) {
			logger.warning(e.getMessage());
		}

		int candidateCount = candidateConfigs.size();
		if (candidateCount < 1) {
			return createConfiguration(fileName);
		} else
			return (ILaunchConfiguration) candidateConfigs.get(0);
	}

	/**
	 * @param fileName
	 * @return
	 */
	protected static ILaunchConfiguration createConfiguration(String fileName) {
		// String fileName = input.getPath( ).toOSString( );
		// int index = fileName.indexOf( File.separator );
		String name = "New_configuration";//$NON-NLS-1$

		name = DebugPlugin.getDefault().getLaunchManager().generateUniqueLaunchConfigurationNameFrom(name);
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			ILaunchConfigurationType configType = getConfigurationType();
			wc = configType.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(name));
			wc.setAttribute(IReportLaunchConstants.ATTR_REPORT_FILE_NAME, fileName);

			config = wc.doSave();
		} catch (CoreException exception) {
			logger.warning(exception.getMessage());
		}
		return config;
	}
}
