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

package org.eclipse.birt.extension.qrcode;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * QRCodeBuilder
 */
public class QRCodeBuilder extends ReportItemBuilderUI {

	@Override
	public int open(ExtendedItemHandle handle) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof QRCodeItem) {
				QRCodeEditor editor = new QRCodeEditor(Display.getCurrent().getActiveShell(),
						(QRCodeItem) item);

				return editor.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Window.CANCEL;
	}
}

/**
 * QRCodeEditor
 */
class QRCodeEditor extends TrayDialog {

	private QRCodeItem qrItem;

	private Text txtText;
	private Spinner spDotsWidth;
	private Text txtEncoding;
	private Label lbText;
	private Label lbDotsWidth;
	private Label lbEncoding;

	protected QRCodeEditor(Shell shell, QRCodeItem qrItem) {
		super(shell);

		this.qrItem = qrItem;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("QRCode Builder"); //$NON-NLS-1$
	}

	private void openExpression(Text textControl) {
		String oldValue = textControl.getText();

		ExpressionBuilder eb = new ExpressionBuilder(textControl.getShell(), oldValue);
		eb.setExpressionProvider(new ExpressionProvider(qrItem.getModelHandle()));

		String result = oldValue;

		if (eb.open() == Window.OK) {
			result = eb.getResult();
		}

		if (!oldValue.equals(result)) {
			textControl.setText(result);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		lbText = new Label(composite, SWT.None);
		lbText.setText("Text Content:"); //$NON-NLS-1$

		txtText = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		txtText.setLayoutData(gd);

		Button btnExp = new Button(composite, SWT.PUSH);
		btnExp.setText("..."); //$NON-NLS-1$
		btnExp.setToolTipText("Invoke Expression Builder");

		btnExp.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				openExpression(txtText);
			}
		});

		lbDotsWidth = new Label(composite, SWT.None);
		lbDotsWidth.setText("Width (dots):");

		spDotsWidth = new Spinner(composite, SWT.None);
		spDotsWidth.setMinimum(21);
		spDotsWidth.setMaximum(2000);
		spDotsWidth.setDigits(0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		spDotsWidth.setLayoutData(gd);

		lbEncoding = new Label(composite, SWT.None);
		lbEncoding.setText("Encoding:");

		txtEncoding = new Text(composite, SWT.None);
		txtEncoding.setLayoutData(gd);

		applyDialogFont(composite);

		initValues();

		return composite;
	}

	private void initValues() {
		txtText.setText(qrItem.getText());
		spDotsWidth.setSelection(qrItem.getDotsWidth());
		txtEncoding.setText(qrItem.getEncoding());
	}

	@Override
	protected void okPressed() {

		try {
			qrItem.setText(txtText.getText());
			qrItem.setDotsWidth(Integer.parseInt(spDotsWidth.getText()));
			qrItem.setEncoding(txtEncoding.getText());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.okPressed();
	}
}
