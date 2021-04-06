/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.launcher;

/**
 * IReportLauncherSettings
 * 
 * @deprecated
 */
public interface IReportLauncherSettings {

	/**
	 * Key for the selection project name
	 */
	String IMPORTPROJECT = "importproject"; //$NON-NLS-1$
	String IMPORTPROJECTNAMES = "importprojectnames"; //$NON-NLS-1$
	String OPENFILENAMES = "openfilenames"; //$NON-NLS-1$
	String PROPERTYSEPARATOR = ";"; //$NON-NLS-1$ separator
	String WORKESPACENAME = "birt-debugger-workspace"; //$NON-NLS-1$

	String WSPROJECT = "wsproject"; //$NON-NLS-1$

	String LOCATION = "location"; //$NON-NLS-1$
	String DOCLEAR = "clearws"; //$NON-NLS-1$
	String ASKCLEAR = "askclear"; //$NON-NLS-1$

	// Program to run
	String APPLICATION = "application"; //$NON-NLS-1$
	String PRODUCT = "product"; //$NON-NLS-1$
	String USE_PRODUCT = "useProduct"; //$NON-NLS-1$
	String APP_TO_TEST = "testApplication"; //$NON-NLS-1$

	// Command line settings
	String VMINSTALL = "vminstall"; //$NON-NLS-1$
	String VMARGS = "vmargs"; //$NON-NLS-1$
	String PROGARGS = "progargs"; //$NON-NLS-1$
	String BOOTSTRAP_ENTRIES = "bootstrap"; //$NON-NLS-1$

	// Plug-ins and Fragments settings
	String USECUSTOM = "default"; //$NON-NLS-1$
	String USEFEATURES = "usefeatures"; //$NON-NLS-1$
	String EXTPLUGINS = "extplugins"; //$NON-NLS-1$

	// Tracing settings
	String TRACING = "tracing"; //$NON-NLS-1$
	String TRACING_OPTIONS = "tracingOptions"; //$NON-NLS-1$
	String TRACING_SELECTED_PLUGIN = "selectedPlugin"; //$NON-NLS-1$
	String TRACING_CHECKED = "checked"; //$NON-NLS-1$
	String TRACING_NONE = "[NONE]"; //$NON-NLS-1$

	// Configuration tab
	String CONFIG_USE_DEFAULT = "useDefaultConfig"; //$NON-NLS-1$
	String CONFIG_AUTO_START = "autoStartList"; //$NON-NLS-1$
	String CONFIG_CLEAR = "clearConfig"; //$NON-NLS-1$

	// config file location
	String CONFIG_LOCATION = "configLocation"; //$NON-NLS-1$
}
