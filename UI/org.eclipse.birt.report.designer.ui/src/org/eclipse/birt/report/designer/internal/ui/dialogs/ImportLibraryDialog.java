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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 *
 */

public class ImportLibraryDialog extends BaseDialog {

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
	private static final String MSG_DIALOG_TITLE = Messages.getString("ImportLibraryDialog.Title"); //$NON-NLS-1$
	private static final String MSG_DIALOG_MESSAGE = Messages.getString("ImportLibraryAction.Message"); //$NON-NLS-1$
	private static final String MSG_DIALOG_NAMESPACE = Messages.getString("ImportLibraryAction.Prefix"); //$NON-NLS-1$
	private static final String MSG_DIALOG_ERROR_CANNOT_BE_EMPTY = Messages
			.getString("ImportLibraryAction.Error.CannotBeEmpty"); //$NON-NLS-1$
	private static final String MSG_DIALOG_ERROR_HAS_BEEN_USED = Messages
			.getString("ImportLibraryAction.Error.HasBeenUsed"); //$NON-NLS-1$
	private static final String MSG_DIALOG_ERROR_HAS_DOT = Messages.getString("ImportLibraryAction.Error.HasDot"); //$NON-NLS-1$

	private String namespace;
	private Text namespaceEditor;
	private CLabel messageLine;

	public ImportLibraryDialog(String namespace) {
		super(MSG_DIALOG_TITLE);
		this.namespace = namespace;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Label label = new Label(composite, SWT.NONE);
		label.setText(MSG_DIALOG_MESSAGE);
		Composite inputArea = new Composite(composite, SWT.NONE);
		inputArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		inputArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(inputArea, SWT.NONE).setText(MSG_DIALOG_NAMESPACE);
		namespaceEditor = new Text(inputArea, SWT.BORDER | SWT.SINGLE);
		namespaceEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		namespaceEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				namespace = UIUtil.convertToModelString(namespaceEditor.getText(), true);
				boolean canFinish = false;
				if (namespace == null) {
					messageLine.setText(MSG_DIALOG_ERROR_CANNOT_BE_EMPTY);
				} else if (SessionHandleAdapter.getInstance().getReportDesignHandle().getLibrary(namespace) != null) {
					messageLine.setText(MSG_DIALOG_ERROR_HAS_BEEN_USED);
				} else if (namespace.indexOf('.') != -1) {
					messageLine.setText(MSG_DIALOG_ERROR_HAS_DOT);
				} else {
					canFinish = true;
					messageLine.setText(""); //$NON-NLS-1$
				}
				if (canFinish) {
					messageLine.setImage(null);
				} else {
					messageLine.setImage(ERROR_ICON);
				}
				getOkButton().setEnabled(canFinish);
			}
		});
		messageLine = new CLabel(composite, SWT.NONE);
		messageLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.bindHelp(parent, IHelpContextIds.IMPORT_LIBRARY_DIALOG_ID);
		return composite;
	}

	@Override
	protected boolean initDialog() {
		namespaceEditor.setText(UIUtil.convertToGUIString(namespace));
		return true;
	}

	@Override
	protected void okPressed() {
		setResult(namespace);
		super.okPressed();
	}

}
