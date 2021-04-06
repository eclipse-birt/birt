/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

/**
 * The constants used for Birt Project Wizard
 * 
 */
public interface IBirtWizardConstants {

	// image file path
	public final static String BIRT_PROJECT_WIZBANNER = "icons/create_project_wizbanner.gif"; //$NON-NLS-1$

	// Reference Extension Points
	public final static String EXAMPLE_WIZARD_EXTENSION_POINT = "org.eclipse.wst.common.ui.exampleProjectCreationWizard"; //$NON-NLS-1$
	public final static String NEW_WIZARDS_EXTENSION_POINT = "org.eclipse.ui.newWizards"; //$NON-NLS-1$
	public final static String BIRT_RESOURCES_EXTENSION_POINT = "org.eclipse.birt.integration.wtp.ui.birtResourcesDefinition"; //$NON-NLS-1$

	// Extension ID
	public final static String BIRTEXAMPLE_WIZARD_ID = "org.eclipse.birt.integration.wtp.ui.BirtExampleProjectCreationWizard"; //$NON-NLS-1$
	public final static String BIRT_WIZARD_ID = "org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWebProjectCreationWizard"; //$NON-NLS-1$
	public final static String DEFAULT_BIRT_WEBAPP_ID = "org.eclipse.birt.integration.wtp.ui.defaultBirtWebappDefinition"; //$NON-NLS-1$

	// Web Browser ID
	public final static String WEB_BROWSER_ID = "org.eclipse.ui.browser.editor"; //$NON-NLS-1$

	// Configuration Wizard Page
	public final static String BIRT_CONFIGURATION_PAGE_NAME = "BirtWebProjectConfigurationWizardPage"; //$NON-NLS-1$

	// deployment settings
	public final static String BIRT_RESOURCE_FOLDER_SETTING = "BIRT_RESOURCE_PATH"; //$NON-NLS-1$
	public final static String BIRT_WORKING_FOLDER_SETTING = "BIRT_VIEWER_WORKING_FOLDER"; //$NON-NLS-1$
	public final static String BIRT_DOCUMENT_FOLDER_SETTING = "BIRT_VIEWER_DOCUMENT_FOLDER"; //$NON-NLS-1$
	public final static String BIRT_REPORT_ACCESSONLY_SETTING = "WORKING_FOLDER_ACCESS_ONLY"; //$NON-NLS-1$
	public final static String BIRT_IMAGE_FOLDER_SETTING = "BIRT_VIEWER_IMAGE_DIR"; //$NON-NLS-1$
	public final static String BIRT_SCRIPTLIB_FOLDER_SETTING = "BIRT_VIEWER_SCRIPTLIB_DIR"; //$NON-NLS-1$
	public final static String BIRT_LOG_FOLDER_SETTING = "BIRT_VIEWER_LOG_DIR"; //$NON-NLS-1$
	public final static String BIRT_MAX_ROWS_SETTING = "BIRT_VIEWER_MAX_ROWS"; //$NON-NLS-1$
	public final static String BIRT_MAX_ROWLEVELS_SETTING = "BIRT_VIEWER_MAX_CUBE_ROWLEVELS"; //$NON-NLS-1$
	public final static String BIRT_MAX_COLUMNLEVELS_SETTING = "BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS"; //$NON-NLS-1$
	public final static String BIRT_CUBE_MEMORYSIZE_SETTING = "BIRT_VIEWER_CUBE_MEMORY_SIZE"; //$NON-NLS-1$
	public final static String BIRT_LOG_LEVEL_SETTING = "BIRT_VIEWER_LOG_LEVEL"; //$NON-NLS-1$
	public final static String BIRT_PRINT_SERVER_SETTING = "BIRT_VIEWER_PRINT_SERVERSIDE"; //$NON-NLS-1$

	// ReportPlugin class
	public final static String REPORT_PLUGIN_ID = "org.eclipse.birt.report.designer.ui"; //$NON-NLS-1$
	public final static String REPORT_PLUGIN_CLASS = "org.eclipse.birt.report.designer.ui.ReportPlugin"; //$NON-NLS-1$

	public final static String BLANK_STRING = ""; //$NON-NLS-1$

	// Extension Item
	public final static String EXT_WEBAPP = "webapp"; //$NON-NLS-1$
	public final static String EXT_CONTEXT_PARAM = "context-param"; //$NON-NLS-1$
	public final static String EXT_FILTER = "filter"; //$NON-NLS-1$
	public final static String EXT_FILTER_MAPPING = "filter-mapping"; //$NON-NLS-1$
	public final static String EXT_LISTENER = "listener"; //$NON-NLS-1$
	public final static String EXT_SERVLET = "servlet"; //$NON-NLS-1$
	public final static String EXT_SERVLET_MAPPING = "servlet-mapping"; //$NON-NLS-1$
	public final static String EXT_TAGLIB = "taglib"; //$NON-NLS-1$

	public final static String EXT_CONFLICT = "conflict"; //$NON-NLS-1$
	public final static String EXT_FOLDER = "folder"; //$NON-NLS-1$
	public final static String EXT_FILE = "file"; //$NON-NLS-1$

	// Attribute for Extension Item
	public final static String EXTATTR_DESCRIPTION = "description"; //$NON-NLS-1$
}
