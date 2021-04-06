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

package org.eclipse.birt.integration.wtp.ui.internal.dialogs;

import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * The dialog to query result if overwrite the items from web.xml file
 * 
 */
public class WebArtifactOverwriteQuery implements IOverwriteQuery {

	// if all
	private boolean isALL = false;

	private Shell shell;

	/**
	 * default constructor
	 * 
	 * @param shell
	 */
	public WebArtifactOverwriteQuery(Shell shell) {
		this.shell = shell;
	}

	/**
	 * Open confirm dialog
	 * 
	 * @param file
	 * @return
	 */
	private int openDialog(final String item) {
		final int[] result = { IDialogConstants.CANCEL_ID };
		shell.getDisplay().syncExec(new Runnable() {

			public void run() {
				String title = BirtWTPMessages.BIRTOverwriteQuery_webartifact_title;
				String msg = NLS.bind(BirtWTPMessages.BIRTOverwriteQuery_webartifact_message, item);
				String[] options = { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
						IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.CANCEL_LABEL };
				MessageDialog dialog = new MessageDialog(shell, title, null, msg, MessageDialog.QUESTION, options, 0);
				result[0] = dialog.open();
			}
		});
		return result[0];
	}

	/**
	 * Returns the query result. If has selected ALL, always return ALL.
	 * 
	 * @param item
	 */
	public String queryOverwrite(String item) {
		if (isALL)
			return ALL;

		String[] returnCodes = { YES, NO, ALL, CANCEL };
		int returnVal = openDialog(item);
		String result = returnVal < 0 ? CANCEL : returnCodes[returnVal];

		// check if selected ALL
		isALL = result.equalsIgnoreCase(ALL) ? true : false;

		return result;
	}
}
