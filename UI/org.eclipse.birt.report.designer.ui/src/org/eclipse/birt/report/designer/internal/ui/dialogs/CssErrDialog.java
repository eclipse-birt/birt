/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class CssErrDialog extends BaseTitleAreaDialog {

	private List errorList;
	private int level;
	public static final int FATAL_ERROR = 1;
	public static final int ERROR = 2;
	public static final int WARNING = 3;

	public CssErrDialog(Shell parentShell) {
		super(parentShell);
	}

	public CssErrDialog(Shell parentShell, List list, int level) {
		this(parentShell);
		this.errorList = list;
		this.level = level;
	}

	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		setMessage(Messages.getString("CssErrDialog.AreaMessage")); //$NON-NLS-1$
		setTitle(Messages.getString("CssErrDialog.AreaTitle")); //$NON-NLS-1$
		getShell().setText(Messages.getString("CssErrDialog.shellTitle.ImportCssStyleMssageTitle")); //$NON-NLS-1$

		return control;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createComposite(composite);

		UIUtil.bindHelp(parent, IHelpContextIds.CSS_ERROR_DIALOG_ID);
		return composite;

	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		composite.setData(gd);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		new Label(composite, SWT.NONE).setText(Messages.getString("CssErrDialog.Severity")); //$NON-NLS-1$
		Label messageLine = new Label(composite, SWT.NONE);
		Label messageLabel = new Label(composite, SWT.NONE);
		messageLabel.setText(Messages.getString("CssErrDialog.Message")); //$NON-NLS-1$
		messageLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		Text messageText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		gd = new GridData();
		gd.heightHint = 200;
		gd.widthHint = 340;
		messageText.setLayoutData(gd);
		messageText.setEditable(false);
		switch (level) {
		case FATAL_ERROR:
			messageLine.setText(Messages.getString("CssErrDialog.FatalError")); //$NON-NLS-1$
			break;
		case ERROR:
			messageLine.setText(Messages.getString("CssErrDialog.Error")); //$NON-NLS-1$
			break;
		case WARNING:
			messageLine.setText(Messages.getString("CssErrDialog.Warning")); //$NON-NLS-1$
			break;
		default:
		}

		Iterator errorIter = errorList.listIterator();
		while (errorIter.hasNext()) {
			messageText.append(errorIter.next().toString());
			messageText.append("\n"); //$NON-NLS-1$
		}

		return parent;
	}
}
