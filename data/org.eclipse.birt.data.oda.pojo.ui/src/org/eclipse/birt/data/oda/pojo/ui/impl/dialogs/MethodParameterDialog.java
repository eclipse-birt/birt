/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.util.Constants;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.util.MethodParameterType;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class MethodParameterDialog extends StatusDialog {

	public interface IModifyValidator {
		boolean validateInputValue(Object value, Object[] args);
	}

	private IModifyValidator validator;

	private Text nameText;
	private Button checkBtn;
	private static String TITLE = Messages.getString("MethodParameterDialog.edit.title"); //$NON-NLS-1$
	private IMethodParameter param;
	private String name, type, value;
	private boolean toBeMapped;

	protected MethodParameterDialog(IMethodParameter param) {
		super(PlatformUI.getWorkbench().getDisplay().getActiveShell());

		this.param = param;
		if (param instanceof VariableParameter) {
			name = ((VariableParameter) param).getName();
		} else {
			name = ""; //$NON-NLS-1$
		}

		this.type = param.getDataType();
		this.value = param.getStringValue() == null ? "" //$NON-NLS-1$
				: param.getStringValue().toString();
	}

	@Override
	public void create() {
		super.create();

		Point pt = getShell().computeSize(-1, -1);
		pt.x = Math.max(pt.x, 400);
		pt.y = Math.max(pt.y, 200);
		getShell().setSize(pt);
		getShell().setText(TITLE);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createDialogContent(composite);

		HelpUtil.setSystemHelp(composite, HelpUtil.CONEXT_ID_DATASET_POJO_METHOD_PARAMETER);

		return composite;
	}

	private void createDialogContent(Composite composite) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		checkBtn = new Button(composite, SWT.CHECK);
		checkBtn.setText(Messages.getString("MethodParameterDialog.checkBox.message")); //$NON-NLS-1$
		checkBtn.setSelection(this.param instanceof VariableParameter);
		toBeMapped = checkBtn.getSelection();

		checkBtn.setLayoutData(data);
		checkBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				toBeMapped = checkBtn.getSelection();

				nameText.setEnabled(toBeMapped);
				nameText.setText(toBeMapped ? name : Constants.DISPLAY_NONE_VALUE);

				validateSyntax();
			}
		});

		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("MethodParameterDialog.label.name")); //$NON-NLS-1$

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(gd);
		nameText.setEnabled(toBeMapped);

		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (nameText.isEnabled()) {
					name = nameText.getText();
				}
				validateSyntax();
			}
		});

		Label valueLabel = new Label(composite, SWT.NONE);
		valueLabel.setText(Messages.getString("MethodParameterDialog.label.value")); //$NON-NLS-1$

		final Text valueText = new Text(composite, SWT.BORDER);
		valueText.setLayoutData(gd);
		valueText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = valueText.getText();
				validateSyntax();
			}
		});

		Label typeLabel = new Label(composite, SWT.NONE);
		typeLabel.setText(Messages.getString("MethodParameterDialog.label.dataType")); //$NON-NLS-1$

		final ComboViewer comboTypes = new ComboViewer(composite, SWT.BORDER);
		GridData comboData = new GridData(GridData.FILL_HORIZONTAL);
		comboTypes.getCombo().setLayoutData(comboData);
		comboTypes.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object arg0) {
				MethodParameterType[] types = MethodParameterType.getBuiltins();
				String names[] = new String[types.length];
				for (int i = 0; i < types.length; i++) {
					names[i] = types[i].getName();
				}
				return names;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
		});

		comboTypes.setInput(""); //$NON-NLS-1$

		comboTypes.getCombo().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				type = comboTypes.getCombo().getText();
				validateSyntax();
			}
		});

		comboTypes.getCombo().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				type = comboTypes.getCombo().getText();
				validateSyntax();
			}
		});

		if (this.param != null) {
			if (toBeMapped) {
				nameText.setText(name == null ? "" : name); //$NON-NLS-1$
			} else {
				nameText.setText(Constants.DISPLAY_NONE_VALUE);
			}
			valueText.setText(value == null ? "" : value); //$NON-NLS-1$
			comboTypes.getCombo().setText(type == null ? "" : type); //$NON-NLS-1$
		}
		validateSyntax();

	}

	private void validateSyntax() {
		IStatus status = null;

		if (this.type == null || this.type.trim().length() == 0) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("MethodParameterDialog.error.missingDataType")); //$NON-NLS-1$
		} else if (this.toBeMapped && (this.name == null || this.name.trim().length() == 0)) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("MethodParameterDialog.error.missingName")); //$NON-NLS-1$
		} else if (!this.toBeMapped && (this.value == null || this.value.trim().length() == 0)) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("MethodParameterDialog.error.missingValue")); //$NON-NLS-1$
		} else if (validator != null) {
			String[] args = { name, type };
			if ((this.param instanceof VariableParameter)
					&& !validator.validateInputValue((VariableParameter) this.param, args)) {
				status = getMiscStatus(IStatus.ERROR, Messages.getFormattedString(
						"MethodParameterDialog.error.invalidParamName", new Object[] { this.name })); //$NON-NLS-1$
			} else if (!validator.validateInputValue(new VariableParameter(name, type), args)) {
				status = getMiscStatus(IStatus.ERROR, Messages.getFormattedString(
						"MethodParameterDialog.error.invalidParamName", new Object[] { this.name })); //$NON-NLS-1$
			} else {
				status = getOKStatus();
			}
		} else {
			status = getOKStatus();
		}

		if (status != null) {
			updateStatus(status);
		}
	}

	private Status getMiscStatus(int severity, String message) {
		return new Status(severity, PlatformUI.PLUGIN_ID, severity, message, null);
	}

	private Status getOKStatus() {
		return getMiscStatus(IStatus.OK, ""); //$NON-NLS-1$
	}

	protected IMethodParameter updateMethodParameter() {
		if (toBeMapped) {
			param = new VariableParameter(name, type);
			((VariableParameter) param).setName(name);
		} else {
			param = new ConstantParameter(value.toString(), type);
		}
		param.setDataType(type);
		param.setStringValue(value);
		return this.param;
	}

	public void setValidator(IModifyValidator validator) {
		this.validator = validator;
	}

}
