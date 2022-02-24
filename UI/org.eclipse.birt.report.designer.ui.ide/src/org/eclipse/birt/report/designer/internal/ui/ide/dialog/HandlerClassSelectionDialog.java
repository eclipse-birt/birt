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

package org.eclipse.birt.report.designer.internal.ui.ide.dialog;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

/**
 * 
 */

public class HandlerClassSelectionDialog extends TwoPaneElementSelector {
	private IType[] fTypes;

	private static class PackageRenderer extends JavaElementLabelProvider {
		public PackageRenderer() {
			super(JavaElementLabelProvider.SHOW_PARAMETERS | JavaElementLabelProvider.SHOW_POST_QUALIFIED
					| JavaElementLabelProvider.SHOW_ROOT);
		}

		public Image getImage(Object element) {
			return super.getImage(((IType) element).getPackageFragment());
		}

		public String getText(Object element) {
			return super.getText(((IType) element).getPackageFragment());
		}
	}

	public HandlerClassSelectionDialog(Shell shell, IType[] types) {
		super(shell,
				new JavaElementLabelProvider(
						JavaElementLabelProvider.SHOW_BASICS | JavaElementLabelProvider.SHOW_OVERLAY_ICONS),
				new PackageRenderer());
		setHelpAvailable(false);
		fTypes = types;
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, IJavaHelpContextIds.MAINTYPE_SELECTION_DIALOG);
		UIUtil.bindHelp(newShell, IHelpContextIds.HANDLER_CLASS_SELECTION_DIALOG);
	}

	/*
	 * @see Window#open()
	 */
	public int open() {
		if (fTypes == null) {
			return CANCEL;
		}

		setElements(fTypes);
		return super.open();
	}

}
