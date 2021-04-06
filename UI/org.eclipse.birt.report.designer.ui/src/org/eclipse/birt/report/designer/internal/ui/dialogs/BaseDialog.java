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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.swt.widgets.Shell;

/**
 * Base class for most dialog in BIRT, BaseDialog extends JFace TrayDialog
 * 
 * @deprecated use
 *             {@link org.eclipse.birt.report.designer.ui.dialogs.BaseDialog}
 */
public abstract class BaseDialog extends org.eclipse.birt.report.designer.ui.dialogs.BaseDialog {

	protected BaseDialog(String title) {
		super(title);
	}

	protected BaseDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

}