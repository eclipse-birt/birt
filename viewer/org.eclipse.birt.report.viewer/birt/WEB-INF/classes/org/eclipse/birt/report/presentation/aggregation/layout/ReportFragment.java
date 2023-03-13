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

/**
 * Fragment for report. It contains report tool bar and content fragments.
 * <p>
 *
 * @see BaseFragment
 */
public class ReportFragment extends BirtBaseFragment {
	/**
	 * Build fragment by adding toolbar and content fragment as children.
	 */
	@Override
	protected void build() {
		addChild(new SidebarFragment());
		addChild(new ReportContentFragment());
	}
}
