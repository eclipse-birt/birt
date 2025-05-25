/*************************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others.
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
import org.eclipse.birt.report.presentation.aggregation.control.ProgressBarFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.DialogContainerFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ExceptionDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.MessageDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ParameterDialogFragment;

/**
 * Fragment for parameter dialog.
 * <p>
 *
 * @see BaseFragment
 */
public class ParameterFragment extends BirtBaseFragment {
	/**
	 * Build fragment by adding parameter dialog fragment as child.
	 */
	@Override
	protected void build() {
		addChild(new ProgressBarFragment());
		addChild(new DialogContainerFragment(new ParameterDialogFragment()));
		addChild(new DialogContainerFragment(new ExceptionDialogFragment()));
		addChild(new DialogContainerFragment(new MessageDialogFragment()));
	}
}
