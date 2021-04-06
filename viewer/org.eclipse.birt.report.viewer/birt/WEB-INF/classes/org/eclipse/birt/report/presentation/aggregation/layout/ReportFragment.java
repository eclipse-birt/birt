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
	protected void build() {
		addChild(new SidebarFragment());
		addChild(new ReportContentFragment());
	}
}
