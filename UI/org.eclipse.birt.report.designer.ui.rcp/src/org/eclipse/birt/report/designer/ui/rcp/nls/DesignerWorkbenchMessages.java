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

package org.eclipse.birt.report.designer.ui.rcp.nls;

import org.eclipse.osgi.util.NLS;

/**
 * Defines i18n string
 */

public class DesignerWorkbenchMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.birt.report.designer.ui.rcp.nls.messages";//$NON-NLS-1$

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, DesignerWorkbenchMessages.class);
	}

	public static String Workbench_title;

	// --- File Menu ---
	public static String Workbench_file;
	public static String Workbench_openFile;
	public static String Workbench_new;
	public static String Action_newReport;
	public static String Action_openReport;
	public static String Action_newLibrary;
	public static String Action_openLibrary;
	public static String Action_newTemplate;
	public static String Action_openTemplate;

	// --- Edit Menu ---
	public static String Workbench_edit;

	// --- Window Menu ---
	public static String Workbench_window;
	public static String Workbench_openPerspective;
	public static String Workbench_showView;
	public static String Workbench_openNewWindow;

	// --- Help Menu ---
	public static String Workbench_help;

	public static String Dialog_openFile;

	public static String SaveAsWizardWindowTitle;
	public static String SaveAsWizardPageTitle;
	public static String SaveAsWizardPageDesc;
	public static String ReportSettingPageTitle;
	// public static String ReportSettingPageDesc;

}
