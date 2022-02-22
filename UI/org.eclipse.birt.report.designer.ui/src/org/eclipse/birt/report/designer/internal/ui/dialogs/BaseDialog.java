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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.swt.widgets.Shell;

/**
 * Base class for most dialog in BIRT, BaseDialog extends JFace TrayDialog
 *
 * @deprecated use
 *             {@link org.eclipse.birt.report.designer.ui.dialogs.BaseDialog}
 */
@Deprecated
public abstract class BaseDialog extends org.eclipse.birt.report.designer.ui.dialogs.BaseDialog {

	protected BaseDialog(String title) {
		super(title);
	}

	protected BaseDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

}
