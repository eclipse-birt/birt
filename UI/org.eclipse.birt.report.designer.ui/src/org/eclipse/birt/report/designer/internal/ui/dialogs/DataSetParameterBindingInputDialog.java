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

import org.eclipse.birt.report.designer.internal.ui.script.JSExpressionContext;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
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

public class DataSetParameterBindingInputDialog extends BaseDialog {

	private static final String LABEL_NAME = Messages.getString("DataSetParameterBindingInputDialog.Label.Name"); //$NON-NLS-1$
	private static final String LABEL_DATA_TYPE = Messages
			.getString("DataSetParameterBindingInputDialog.Label.DataType"); //$NON-NLS-1$
	private static final String LABEL_VALUE = Messages.getString("DataSetParameterBindingInputDialog.Label.Value"); //$NON-NLS-1$
	private static final String DIALOG_TITLE = Messages.getString("DataSetParameterBindingInputDialog.Title"); //$NON-NLS-1$

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(DataSetParameter.STRUCT_NAME).getMember(DataSetParameter.DATA_TYPE_MEMBER)
			.getAllowedChoices();

	private Label nameLabel, typeLabel;
	private Text valueEditor;
	private Expression value;
	private DataSetParameterHandle handle;
	private JSExpressionContext provider;

	public DataSetParameterBindingInputDialog(Shell parentShell, DataSetParameterHandle handle,
			JSExpressionContext provider) {
		super(parentShell, DIALOG_TITLE);
		this.handle = handle;
		this.provider = provider;
	}

	public DataSetParameterBindingInputDialog(DataSetParameterHandle handle, JSExpressionContext provider) {
		this(UIUtil.getDefaultShell(), handle, provider);
	}

	protected boolean initDialog() {
		nameLabel.setText(handle.getName());
		typeLabel.setText(getParameterDataTypeDisplayName(handle.getParameterDataType()));
		ExpressionButtonUtil.initExpressionButtonControl(valueEditor, value);
		return true;
	}

	private String getParameterDataTypeDisplayName(String type) {
		IChoice choice = DATA_TYPE_CHOICE_SET.findChoice(type);
		if (choice != null)
			return choice.getDisplayName();
		return type;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = layout.marginHeight = 15;

		composite.setLayout(layout);

		UIUtil.bindHelp(composite, IHelpContextIds.DATA_SET_PARAMETER_BINDING_DIALOG);

		new Label(composite, SWT.NONE).setText(LABEL_NAME);
		nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(composite, SWT.NONE).setText(LABEL_DATA_TYPE);
		typeLabel = new Label(composite, SWT.NONE);
		typeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(composite, SWT.NONE).setText(LABEL_VALUE);
		Composite valueComposite = new Composite(composite, SWT.NONE);
		valueComposite.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		valueComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		valueEditor = new Text(valueComposite, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 250;
		gd.heightHint = valueEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - valueEditor.getBorderWidth() * 2;
		valueEditor.setLayoutData(gd);

		ExpressionButtonUtil.createExpressionButton(valueComposite, valueEditor, provider.getExpressionProvider(),
				provider.getContextObject());

		gd = new GridData(GridData.FILL_HORIZONTAL);
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(gd);

		return composite;
	}

	protected void okPressed() {
		setResult(new Expression(valueEditor.getText(), (String) valueEditor.getData(ExpressionButtonUtil.EXPR_TYPE)));
		super.okPressed();
	}

	public void setValue(Expression value) {
		this.value = value;
	}

}
