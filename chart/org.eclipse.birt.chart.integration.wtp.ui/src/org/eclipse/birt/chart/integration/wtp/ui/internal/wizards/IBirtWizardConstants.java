/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.integration.wtp.ui.internal.wizards;

/**
 * The constants used for Birt Project Wizard
 *
 */
public interface IBirtWizardConstants {
	// image file path
	String BIRT_PROJECT_WIZBANNER = "icons/create_project_wizbanner.gif"; //$NON-NLS-1$

	// Reference Extension Points
	String EXAMPLE_WIZARD_EXTENSION_POINT = "org.eclipse.wst.common.ui.exampleProjectCreationWizard"; //$NON-NLS-1$
	String NEW_WIZARDS_EXTENSION_POINT = "org.eclipse.ui.newWizards"; //$NON-NLS-1$
	String BIRT_RESOURCES_EXTENSION_POINT = "org.eclipse.birt.chart.integration.wtp.ui.birtResourcesDefinition"; //$NON-NLS-1$

	// Extension ID
	String BIRTEXAMPLE_WIZARD_ID = "org.eclipse.birt.chart.integration.wtp.ui.BirtExampleProjectCreationWizard"; //$NON-NLS-1$
	String BIRT_WIZARD_ID = "org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.BirtWebProjectCreationWizard"; //$NON-NLS-1$
	String DEFAULT_BIRT_WEBAPP_ID = "org.eclipse.birt.chart.integration.wtp.ui.defaultBirtWebappDefinition"; //$NON-NLS-1$

	// Web Browser ID
	String WEB_BROWSER_ID = "org.eclipse.ui.browser.editor"; //$NON-NLS-1$

	// Configuration Wizard Page
	String BIRT_CONFIGURATION_PAGE_NAME = "BirtWebProjectConfigurationWizardPage"; //$NON-NLS-1$

	// deployment settings
	String BIRT_RESOURCE_FOLDER_SETTING = "BIRT_RESOURCE_PATH"; //$NON-NLS-1$
	String BIRT_WORKING_FOLDER_SETTING = "BIRT_VIEWER_WORKING_FOLDER"; //$NON-NLS-1$
	String BIRT_DOCUMENT_FOLDER_SETTING = "BIRT_VIEWER_DOCUMENT_FOLDER"; //$NON-NLS-1$
	String BIRT_REPORT_ACCESSONLY_SETTING = "WORKING_FOLDER_ACCESS_ONLY"; //$NON-NLS-1$
	String BIRT_IMAGE_FOLDER_SETTING = "BIRT_VIEWER_IMAGE_DIR"; //$NON-NLS-1$
	String BIRT_SCRIPTLIB_FOLDER_SETTING = "BIRT_VIEWER_SCRIPTLIB_DIR"; //$NON-NLS-1$
	String BIRT_LOG_FOLDER_SETTING = "BIRT_VIEWER_LOG_DIR"; //$NON-NLS-1$
	String BIRT_OVERWRITE_DOCUMENT_SETTING = "BIRT_OVERWRITE_DOCUMENT"; //$NON-NLS-1$
	String BIRT_MAX_ROWS_SETTING = "BIRT_VIEWER_MAX_ROWS"; //$NON-NLS-1$
	String BIRT_LOG_LEVEL_SETTING = "BIRT_VIEWER_LOG_LEVEL"; //$NON-NLS-1$

	// ReportPlugin class
	String REPORT_PLUGIN_ID = "org.eclipse.birt.report.designer.ui"; //$NON-NLS-1$
	String REPORT_PLUGIN_CLASS = "org.eclipse.birt.report.designer.ui.ReportPlugin"; //$NON-NLS-1$

	String BLANK_STRING = ""; //$NON-NLS-1$

	// Extension Item
	String EXT_WEBAPP = "webapp"; //$NON-NLS-1$
	String EXT_CONTEXT_PARAM = "context-param"; //$NON-NLS-1$
	String EXT_LISTENER = "listener"; //$NON-NLS-1$
	String EXT_SERVLET = "servlet"; //$NON-NLS-1$
	String EXT_SERVLET_MAPPING = "servlet-mapping"; //$NON-NLS-1$
	String EXT_TAGLIB = "taglib"; //$NON-NLS-1$

	String EXT_CONFLICT = "conflict"; //$NON-NLS-1$
	String EXT_FOLDER = "folder"; //$NON-NLS-1$
	String EXT_FILE = "file"; //$NON-NLS-1$

	// Attribute for Extension Item
	String EXTATTR_DESCRIPTION = "description"; //$NON-NLS-1$
}
