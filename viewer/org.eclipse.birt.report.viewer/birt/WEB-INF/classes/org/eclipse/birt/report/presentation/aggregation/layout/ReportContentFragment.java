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
import org.eclipse.birt.report.presentation.aggregation.control.NavigationbarFragment;

/**
 * Report content fragment containing engine fragment.
 * <p>
 * 
 * @see BaseFragment
 */
public class ReportContentFragment extends BirtBaseFragment {
	/**
	 * Build fragment by adding engine fragment as child.
	 */
	protected void build() {
		addChild(new NavigationbarFragment());
		addChild(new DocumentFragment());
	}
}
