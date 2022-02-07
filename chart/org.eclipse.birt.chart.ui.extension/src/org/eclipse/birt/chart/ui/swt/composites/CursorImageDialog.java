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

package org.eclipse.birt.chart.ui.swt.composites;

import java.net.URI;

import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The dialog is used to edit cursor image uri.
 */

public class CursorImageDialog extends TrayDialog implements SelectionListener {
	private Composite inputArea;

	private IconCanvas previewCanvas;

	private Text txtUriEditor;

	private Button btnPreview;

	private Cursor cursor;

	private Label title;

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public CursorImageDialog(Shell parentShell, Cursor cursor) {
		super(parentShell);
		this.cursor = cursor;
		setHelpAvailable(false);
	}

	protected Control createContents(Composite parent) {
		Control ct = super.createContents(parent);
//		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.DIALOG_COLOR_IMAGE );
		initDialog();
		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite topCompostie = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(topCompostie, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		createInputArea(composite);
		createPreviewArea(composite);

		new Label(topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return topCompostie;
	}

	private void createInputArea(Composite parent) {
		inputArea = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		inputArea.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		inputArea.setLayout(gl);

		title = new Label(inputArea, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		title.setLayoutData(gd);

		Label l = new Label(inputArea, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		l.setLayoutData(gd);
		l.setText(Messages.getString("ImageDialog.uri.tooltip")); //$NON-NLS-1$

		txtUriEditor = new Text(inputArea, SWT.SINGLE | SWT.BORDER);
		txtUriEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtUriEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});
		txtUriEditor.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				preview(removeQuote(txtUriEditor.getText()));
			}
		});

		btnPreview = new Button(inputArea, SWT.NONE);
		gd = new GridData();
		btnPreview.setLayoutData(gd);
		btnPreview.addSelectionListener(this);
		btnPreview.setText(Messages.getString("ImageDialog.label.Preview")); //$NON-NLS-1$

	}

	private void createPreviewArea(Composite composite) {
		Composite previewArea = new Composite(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 250;
		gd.heightHint = 200;
		previewArea.setLayoutData(gd);
		previewArea.setLayout(new FillLayout());

		previewCanvas = new IconCanvas(previewArea);
		previewCanvas.setSize(250, 200);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	protected void okPressed() {
		if (cursor.getImage().size() > 0) {
			cursor.getImage().clear();
		}
		cursor.getImage().add(0, ImageImpl.create(txtUriEditor.getText()));

		super.okPressed();
	}

	protected boolean initDialog() {
		getShell().setText(Messages.getString("ImageDialog.label.SelectImage")); //$NON-NLS-1$
		title.setText(Messages.getString("ImageDialog.label.EnterURL")); //$NON-NLS-1$
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		initURIEditor();
		return true;
	}

	private void initURIEditor() {
		String uri = ""; //$NON-NLS-1$
		if (cursor.getImage().size() > 0) {
			uri = cursor.getImage().get(0).getURL();
		}

		txtUriEditor.setText(uri);
		txtUriEditor.setFocus();
		// Listener will be called automatically
		updateButtons();
		preview(removeQuote(txtUriEditor.getText()));
	}

	private void updateButtons() {
		boolean complete = txtUriEditor.getText() != null && txtUriEditor.getText().trim().length() > 0;

		btnPreview.setEnabled(complete);
		getButton(IDialogConstants.OK_ID).setEnabled(complete);

	}

	/**
	 * Remove the quote if the string enclosed width quote .
	 * 
	 * @param string
	 * @return string
	 */
	public String removeQuote(String string) {
		if (string != null && string.length() >= 2 && string.startsWith("\"") //$NON-NLS-1$
				&& string.endsWith("\"")) //$NON-NLS-1$
		{
			return string.substring(1, string.length() - 1);
		}
		return string;
	}

	private void preview(String uri) {
		try {
			previewCanvas.loadImage(new URI(uri).toURL());
		} catch (Exception e) {
			// Here ignores exception if image is invalid.
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	public void widgetSelected(SelectionEvent e) {
		Object source = e.getSource();
		if (source == btnPreview) {
			preview(removeQuote(txtUriEditor.getText()));
		}
	}
}
