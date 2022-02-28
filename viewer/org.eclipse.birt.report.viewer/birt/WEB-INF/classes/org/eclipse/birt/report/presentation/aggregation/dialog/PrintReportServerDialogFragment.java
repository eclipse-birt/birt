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

package org.eclipse.birt.report.presentation.aggregation.dialog;

import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * Fragment help rendering print report on the server dialog in side bar.
 * <p>
 *
 * @see BaseFragment
 */
public class PrintReportServerDialogFragment extends BaseDialogFragment {

	/**
	 * Get unique id of the corresponding UI gesture.
	 *
	 * @return id
	 */
	@Override
	public String getClientId() {
		return "printReportServerDialog"; //$NON-NLS-1$
	}

	/**
	 * Get name of the corresponding UI gesture.
	 *
	 * @return id
	 */
	@Override
	public String getClientName() {
		return "Print report on the server"; //$NON-NLS-1$
	}

	/**
	 * Gets the title ID for the html page.
	 *
	 * @return title id
	 */

	@Override
	public String getTitle() {
		return BirtResources.getMessage(ResourceConstants.PRINT_REPORTSERVER_DIALOG_TITLE);
	}
}
