/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;


/**
 * 
 */

public class BaseTitleAreaDialog extends TitleAreaDialog
{

	public BaseTitleAreaDialog( Shell parentShell )
	{
		super( parentShell );
		// TODO Auto-generated constructor stub
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

}
