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

package org.eclipse.birt.chart.integration.wtp.ui.internal.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Messages used by Birt Project Wizard UI
 * 
 */
public class BirtWTPMessages extends NLS {

	// Bundle Name
	private static final String BUNDLE_NAME = "org.eclipse.birt.chart.integration.wtp.ui.internal.i18n.Messages";//$NON-NLS-1$
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

	/***************************************************************************
	 * BIRT Wizard Configuration Page
	 **************************************************************************/
	public static String BIRTProjectCreationWizard_title;

	public static String BIRTProjectConfigurationPage_title;
	public static String BIRTProjectConfigurationPage_desc;

	/***************************************************************************
	 * Overwrite Query Dialog
	 **************************************************************************/
	public static String BIRTOverwriteQuery_title;
	public static String BIRTOverwriteQuery_message;

	public static String BIRTOverwriteQuery_webartifact_title;
	public static String BIRTOverwriteQuery_webartifact_message;

	/***************************************************************************
	 * Error Messages
	 **************************************************************************/
	public static String BIRTErrors_miss_source;
	public static String BIRTErrors_wrong_webcontent;
}
