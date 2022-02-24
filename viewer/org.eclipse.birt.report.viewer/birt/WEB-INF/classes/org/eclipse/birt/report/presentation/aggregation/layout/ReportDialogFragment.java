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

package org.eclipse.birt.report.presentation.aggregation.layout;

import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.DialogContainerFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ExceptionDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ParameterDialogFragment;

/**
 * Report dialog fragment.
 * <p>
 * 
 * @see BaseFragment
 */
public class ReportDialogFragment extends BirtBaseFragment {

	/**
	 * Build fragment by adding needed dialogs fragment root.
	 */
	protected void build() {
		addChild(new DialogContainerFragment(new ExceptionDialogFragment()));
		addChild(new DialogContainerFragment(new ParameterDialogFragment()));
	}
}
