/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Add Show Properties View Action in editor context menu.
 */

public class ShowPropertiesViewMenu implements IMenuListener {

	private static class ShowPropertiesViewAction extends Action {

		ShowPropertiesViewAction() {
			setText(Messages.getString("ShowPropertiesViewMenu.ActionName")); //$NON-NLS-1$
		}

		@Override
		public void run() {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AttributeView.ID);
			} catch (PartInitException e) {
				ExceptionUtil.handle(e, Messages.getString("ShowPropertiesViewMenu.ErrorTitle"), //$NON-NLS-1$
						Messages.getString("ShowPropertiesViewMenu.ErrorMessage")); //$NON-NLS-1$
			}
		}
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		manager.appendToGroup("additions", new ShowPropertiesViewAction()); //$NON-NLS-1$
	}

}
