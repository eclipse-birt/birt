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

import org.eclipse.birt.report.presentation.aggregation.IFragment;

/**
 * Fragment for report tool bar.
 * <p>
 * 
 * @see BaseFragment
 */
public class DialogContainerFragment extends BaseDialogFragment {

	/**
	 * Constructor.
	 * 
	 * @param child
	 */
	public DialogContainerFragment(IFragment child) {
		if (child != null) {
			addChild(child);
		}
	}

	/**
	 * Overwrite the parent. Get front end id.
	 */
	public String getClientId() {
		IFragment dialog = (IFragment) children.get(0);
		if (dialog != null) {
			return dialog.getClientId();
		}

		return null;
	}

	/**
	 * Overwrite the parent. Get front end id.
	 */
	public String getClientName() {
		IFragment dialog = (IFragment) children.get(0);
		if (dialog != null) {
			return dialog.getClientName();
		}

		return null;
	}

	/**
	 * Gets the title ID for the html page.
	 * 
	 * @return title id
	 */

	public String getTitle() {
		IFragment dialog = (IFragment) children.get(0);
		if (dialog != null)
			return dialog.getTitle();
		return null;
	}
}
