/*
 *************************************************************************
 * Copyright (c) 2008, 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.nls.messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String sqbWizPage_cannotOpenConnectionMsg;
	public static String sqbWizPage_cannotOpenConnectionTitle;
	public static String sqbWizPage_dataSourceDesignError;
	public static String sqbWizPage_dbErrorMsg;
	public static String sqbWizPage_defaultMessage;
	public static String sqbWizPage_detectExternalSqlTextChangedMsg;
	public static String sqbWizPage_detectSqlTextChangedTitle;
	public static String sqbWizPage_inputFailOnOpenAskUserMessage;
	public static String sqbWizPage_invalidSqbStateMsg;
	public static String sqbWizPage_invalidSqbStateTitle;
	public static String sqbWizPage_noConnProfileMsg;
	public static String sqbWizPage_connectingDB;
	public static String sqbDesignState_invalidSqbStateVersion;

}
