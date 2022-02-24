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
import org.eclipse.birt.report.presentation.aggregation.control.ProgressBarFragment;
import org.eclipse.birt.report.presentation.aggregation.control.TocFragment;

/**
 * Fragment for report tool bar.
 * <p>
 * 
 * @see BaseFragment
 */
public class DocumentFragment extends BirtBaseFragment {
	/**
	 * Build fragment by adding engine fragment as child.
	 */
	protected void build() {
		addChild(new ProgressBarFragment());
		addChild(new TocFragment());
	}
}
