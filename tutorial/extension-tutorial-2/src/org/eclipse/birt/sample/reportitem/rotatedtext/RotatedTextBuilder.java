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

package org.eclipse.birt.sample.reportitem.rotatedtext;

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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * RotatedTextBuilder
 */
public class RotatedTextBuilder extends ReportItemBuilderUI {

	public int open(ExtendedItemHandle handle) {
		try {
			IReportItem item = handle.getReportItem();

			if (item instanceof RotatedTextItem) {
				// XXX change to RotatedTextEditor2 for expression support
				RotatedTextEditor editor = new RotatedTextEditor2(Display.getCurrent().getActiveShell(),
						(RotatedTextItem) item);

				return editor.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Window.CANCEL;
	}
}

/**
 * RotatedTextEditor
 */
class RotatedTextEditor extends TrayDialog {

	protected RotatedTextItem textItem;

	protected Text txtText;
	protected Scale sclAngle;
	protected Label lbAngle;

	protected RotatedTextEditor(Shell shell, RotatedTextItem textItem) {
		super(shell);

		this.textItem = textItem;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Rotated Text Builder"); //$NON-NLS-1$
	}

	protected void createTextArea(Composite parent) {
		Label lb = new Label(parent, SWT.None);
		lb.setText("Text Content:"); //$NON-NLS-1$

		txtText = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtText.setLayoutData(gd);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTextArea(composite);

		Label lb = new Label(composite, SWT.None);
		lb.setText("Rotation Angle:"); //$NON-NLS-1$

		sclAngle = new Scale(composite, SWT.None);
		sclAngle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sclAngle.setMinimum(0);
		sclAngle.setMaximum(360);
		sclAngle.setIncrement(10);

		lbAngle = new Label(composite, SWT.None);
		GridData gd = new GridData();
		gd.widthHint = 20;
		lbAngle.setLayoutData(gd);

		sclAngle.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				lbAngle.setText(String.valueOf(sclAngle.getSelection()));
			}

			public void widgetSelected(SelectionEvent e) {
				lbAngle.setText(String.valueOf(sclAngle.getSelection()));
			}
		});

		applyDialogFont(composite);

		initValues();

		return composite;
	}

	private void initValues() {
		txtText.setText(textItem.getText());
		sclAngle.setSelection(textItem.getRotationAngle());
		lbAngle.setText(String.valueOf(textItem.getRotationAngle()));
	}

	protected void okPressed() {

		try {
			textItem.setText(txtText.getText());
			textItem.setRotationAngle(sclAngle.getSelection());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.okPressed();
	}
}

/**
 * RotatedTextEditor2
 */
class RotatedTextEditor2 extends RotatedTextEditor {

	protected RotatedTextEditor2(Shell shell, RotatedTextItem textItem) {
		super(shell, textItem);
	}

	protected void createTextArea(Composite parent) {
		Label lb = new Label(parent, SWT.None);
		lb.setText("Text Content:"); //$NON-NLS-1$

		txtText = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		txtText.setLayoutData(gd);

		Button btnExp = new Button(parent, SWT.PUSH);
		btnExp.setText("..."); //$NON-NLS-1$
		btnExp.setToolTipText("Invoke Expression Builder"); //$NON-NLS-1$

		btnExp.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				openExpression(txtText);
			}
		});

	}

	private void openExpression(Text textControl) {
		String oldValue = textControl.getText();

		ExpressionBuilder eb = new ExpressionBuilder(textControl.getShell(), oldValue);
		eb.setExpressionProvier(new ExpressionProvider(textItem.getModelHandle()));

		String result = oldValue;

		if (eb.open() == Window.OK) {
			result = eb.getResult();
		}

		if (!oldValue.equals(result)) {
			textControl.setText(result);
		}
	}

}
