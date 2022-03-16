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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeACLExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GroupRenameDialog extends BaseDialog {

	/**
	 * The message to display, or <code>null</code> if none.
	 */
	private String message;

	/**
	 * Input text widget.
	 */
	private Text text;

	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;

	/**
	 * Error message string.
	 */
	private String errorMessage;

	private IDialogHelper helper;

	private DimensionHandle dimension;

	public GroupRenameDialog(Shell parentShell, String dialogTitle, String dialogMessage) {
		super(dialogTitle);
		// this.title = dialogTitle;
		message = dialogMessage;
	}

	public void setInput(DimensionHandle dimension) {
		this.dimension = dimension;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		// create message
		if (message != null) {
			Label label = new Label(container, SWT.WRAP);
			label.setText(message);
			label.setLayoutData(new GridData());
			label.setFont(parent.getFont());
		}
		text = new Text(container, SWT.BORDER | SWT.SINGLE);
		if (dimension.getName() != null) {
			text.setText(dimension.getName());
		}

		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.widthHint = 250;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (text.getText().trim().length() == 0) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(Messages.getString("RenameInputDialog.Message.BlankName")); //$NON-NLS-1$
				} else if (!UIUtil.validateDimensionName(text.getText())) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(Messages.getString("RenameInputDialog.Message.NumericName")); //$NON-NLS-1$
				} else if (checkDuplicateName(text.getText())) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					setErrorMessage(Messages.getString("RenameInputDialog.Message.DuplicateName")); //$NON-NLS-1$
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					setErrorMessage(null);
				}
			}
		});

		if (text.getText().trim().length() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			setErrorMessage(Messages.getString("RenameInputDialog.Message.BlankName")); //$NON-NLS-1$
		} else if (!UIUtil.validateDimensionName(text.getText())) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
			setErrorMessage(Messages.getString("RenameInputDialog.Message.NumericName")); //$NON-NLS-1$
		}

		createSecurityPart(container);

		errorMessageText = new Text(container, SWT.READ_ONLY | SWT.WRAP);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 3;
		errorMessageText.setLayoutData(gd);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		// Set the error message text
		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=66292
		setErrorMessage(errorMessage);

		applyDialogFont(composite);
		UIUtil.bindHelp(parent, IHelpContextIds.GROUP_RENAME_DIALOG_ID);
		return composite;
	}

	protected boolean checkDuplicateName(String name) {
		try {
			DimensionHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle().findDimension(name);
			if (handle != null && handle != dimension) {
				return true;
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		return false;
	}

	private void createSecurityPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(dimension, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null && helper == null) {
					helper = helperProvider.createHelper(this, BuilderConstants.SECURITY_HELPER_KEY);
					if (helper != null) {
						helper.setProperty(BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString("GroupRenameDialog.Access.Control.List.Expression")); //$NON-NLS-1$
						helper.setProperty(BuilderConstants.SECURITY_EXPRESSION_CONTEXT, dimension);
						helper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeACLExpressionProvider(dimension));
						helper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY, dimension.getACLExpression());
						helper.createContent(parent);
						helper.addListener(SWT.Modify, new Listener() {

							@Override
							public void handleEvent(Event event) {
								helper.update(false);
							}
						});
						helper.update(true);
					}
				}
			}
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
			// Disable the error message text control if there is no error, or
			// no error text (empty or whitespace only). Hide it also to avoid
			// color change.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
			boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();
			// Access the ok button by id, in case clients have overridden
			// button creation.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage == null);
			}
		}
	}

	@Override
	protected void okPressed() {
		try {
			dimension.setName(text.getText().trim());
		} catch (NameException e1) {
			ExceptionUtil.handle(e1);
		}
		if (helper != null) {
			try {
				helper.validate();
				dimension.setExpressionProperty(DimensionHandle.ACL_EXPRESSION_PROP,
						(Expression) helper.getProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY));
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
		super.okPressed();
	}
}
