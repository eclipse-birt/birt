/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import org.eclipse.birt.report.designer.internal.ui.dialogs.MessageLine;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.preferences.StatusInfo;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class ExpressionEditor extends BaseDialog {

	private MessageLine fStatusLine;
	private Text exprText;
	private Object contextObject;
	private boolean allowConstant;
	private IExpressionProvider provider;
	private Expression expression;

	public ExpressionEditor(String title) {
		super(title);
	}

	public void setInput(Object input, IExpressionProvider provider, boolean allowConstant) {
		this.contextObject = input;
		this.allowConstant = allowConstant;
		this.provider = provider;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		container.setLayout(layout);

		new Label(container, SWT.NONE).setText(Messages.getString("ExpressionEditor.Label.Expression")); //$NON-NLS-1$
		exprText = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData();
		gd.widthHint = 200;
		gd.heightHint = exprText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - exprText.getBorderWidth() * 2;
		exprText.setLayoutData(gd);
		exprText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus();
			}

		});

		ExpressionButtonUtil.createExpressionButton(container, exprText, provider, contextObject, allowConstant,
				SWT.PUSH);

		ExpressionButtonUtil.initExpressionButtonControl(exprText, expression);

		UIUtil.bindHelp(parent, IHelpContextIds.EXPRESSION_EDITOR_ID);

		return composite;
	}

	/**
	 * Update the dialog's status line to reflect the given status. It is safe to
	 * call this method before the dialog has been opened.
	 * 
	 * @param status
	 */
	protected void updateStatus(IStatus status) {
		if (fStatusLine != null && !fStatusLine.isDisposed()) {
			updateButtonsEnableState(status);
			fStatusLine.setErrorStatus(status);
		}
	}

	private void checkStatus() {
		if (exprText.getText().length() == 0) {
			IStatus status = new StatusInfo(ReportPlugin.REPORT_UI, IStatus.ERROR,
					Messages.getString("ExpressionEditor.Error.EmptyExpression")); //$NON-NLS-1$
			updateStatus(status);
			return;
		} else {
			IStatus status = new StatusInfo(ReportPlugin.REPORT_UI);
			updateStatus(status);
		}
	}

	protected Control createButtonBar(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginLeft = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setFont(font);

		fStatusLine = new MessageLine(composite);
		fStatusLine.setAlignment(SWT.LEFT);
		GridData statusData = new GridData(GridData.FILL_HORIZONTAL);
		fStatusLine.setErrorStatus(null);
		fStatusLine.setFont(font);
		fStatusLine.setLayoutData(statusData);

		super.createButtonBar(composite);

		checkStatus();

		return composite;
	}

	/**
	 * Update the status of the ok button to reflect the given status. Subclasses
	 * may override this method to update additional buttons.
	 * 
	 * @param status
	 */
	protected void updateButtonsEnableState(IStatus status) {
		Button okButton = getOkButton();
		if (okButton != null && !okButton.isDisposed()) {
			okButton.setEnabled(!status.matches(IStatus.ERROR));
		}
	}

	public void okPressed() {
		this.expression = ExpressionButtonUtil.getExpression(exprText);
		super.okPressed();
	}

	public Expression getExpression() {
		return this.expression;
	}
}
