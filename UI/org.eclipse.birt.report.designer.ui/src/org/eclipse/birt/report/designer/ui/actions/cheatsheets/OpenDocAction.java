/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions.cheatsheets;

import org.eclipse.birt.report.designer.internal.ui.actions.helper.IOpenDocActionHelper;
import org.eclipse.birt.report.designer.internal.ui.actions.helper.IOpenDocActionHelperProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 */
public class OpenDocAction extends Action implements ICheatSheetAction {
	IOpenDocActionHelper helper;

	private void initHelper() {
		// *********** try using a helper provider ****************
		IOpenDocActionHelperProvider helperProvider = (IOpenDocActionHelperProvider) ElementAdapterManager
				.getAdapter(this, IOpenDocActionHelperProvider.class);

		if (helperProvider != null) {
			this.helper = helperProvider.createHelper();// $NON-NLS-1$
		} else {
			this.helper = null;
		}
	}

	public OpenDocAction() {
		this.initHelper();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
	 * org.eclipse.ui.cheatsheets.ICheatSheetManager)
	 */
	@Override
	public void run(String[] params, ICheatSheetManager manager) {
		if (this.helper != null) {
			this.helper.run(params, manager);
			return;
		}
		if (params.length < 1) {
			throw new IllegalArgumentException();
		}
		PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(params[0]);
	}

}
