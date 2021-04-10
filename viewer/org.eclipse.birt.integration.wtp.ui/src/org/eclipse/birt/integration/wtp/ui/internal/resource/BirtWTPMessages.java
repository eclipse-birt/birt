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

package org.eclipse.birt.integration.wtp.ui.internal.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used by Birt Project Wizard UI
 * 
 */
public class BirtWTPMessages extends NLS {

	// Bundle Name
	private static final String BUNDLE_NAME = "org.eclipse.birt.integration.wtp.ui.internal.resource.Messages";//$NON-NLS-1$
	private static ResourceBundle bundle;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, BirtWTPMessages.class);
	}

	/**
	 * Make default constructor is private. Can not create new instance.
	 * 
	 */
	private BirtWTPMessages() {
	}

	/**
	 * Return current resource bundle
	 * 
	 * @return
	 */
	public static ResourceBundle getResourceBundle() {
		try {
			if (bundle == null)
				bundle = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (MissingResourceException x) {
			bundle = null;
		}
		return bundle;
	}

	// =========================== Message Field ===========================

	/***********************************************************************
	 * BIRT Wizard Configuration Page
	 ***********************************************************************/
	public static String BIRTProjectCreationWizard_title;

	public static String BIRTProjectConfigurationPage_title;
	public static String BIRTProjectConfigurationPage_desc;

	/***********************************************************************
	 * BIRT Configuration Dialog
	 ***********************************************************************/
	public static String BIRTConfigurationDialog_title;

	/***********************************************************************
	 * BIRT Configuration Components
	 ***********************************************************************/
	public static String BIRTConfiguration_group_paths;
	public static String BIRTConfiguration_group_others;
	public static String BIRTConfiguration_resource_folder_button_text;
	public static String BIRTConfiguration_working_folder_button_text;
	public static String BIRTConfiguration_document_folder_button_text;
	public static String BIRTConfiguration_image_folder_button_text;
	public static String BIRTConfiguration_scriptlib_folder_button_text;
	public static String BIRTConfiguration_log_folder_button_text;

	public static String BIRTConfiguration_resource_label;
	public static String BIRTConfiguration_resource_dialog_title;
	public static String BIRTConfiguration_resource_dialog_message;

	public static String BIRTConfiguration_working_label;
	public static String BIRTConfiguration_working_dialog_title;
	public static String BIRTConfiguration_working_dialog_message;

	public static String BIRTConfiguration_document_label;
	public static String BIRTConfiguration_document_dialog_title;
	public static String BIRTConfiguration_document_dialog_message;

	public static String BIRTConfiguration_image_label;
	public static String BIRTConfiguration_image_dialog_title;
	public static String BIRTConfiguration_image_dialog_message;

	public static String BIRTConfiguration_scriptlib_label;
	public static String BIRTConfiguration_scriptlib_dialog_title;
	public static String BIRTConfiguration_scriptlib_dialog_message;

	public static String BIRTConfiguration_log_label;
	public static String BIRTConfiguration_log_dialog_title;
	public static String BIRTConfiguration_log_dialog_message;

	public static String BIRTConfiguration_report_access_message;
	public static String BIRTConfiguration_overwrite_message;

	public static String BIRTConfiguration_maxrows_label;
	public static String BIRTConfiguration_maxrowlevels_label;
	public static String BIRTConfiguration_maxcolumnlevels_label;
	public static String BIRTConfiguration_cubememsize_label;

	public static String BIRTConfiguration_loglevel_label;
	public static String BIRTConfiguration_loglevel_all;
	public static String BIRTConfiguration_loglevel_severe;
	public static String BIRTConfiguration_loglevel_warning;
	public static String BIRTConfiguration_loglevel_info;
	public static String BIRTConfiguration_loglevel_config;
	public static String BIRTConfiguration_loglevel_fine;
	public static String BIRTConfiguration_loglevel_finer;
	public static String BIRTConfiguration_loglevel_finest;
	public static String BIRTConfiguration_loglevel_off;

	public static String BIRTConfiguration_printserver_label;

	public static String BIRTConfiguration_import_clear_message;

	/***********************************************************************
	 * Overwrite Query Dialog
	 ***********************************************************************/
	public static String BIRTOverwriteQuery_title;
	public static String BIRTOverwriteQuery_message;

	public static String BIRTOverwriteQuery_webartifact_title;
	public static String BIRTOverwriteQuery_webartifact_message;

	/***********************************************************************
	 * Error Messages
	 ***********************************************************************/
	public static String BIRTErrors_miss_source;
	public static String BIRTErrors_wrong_webcontent;
}
