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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class LevelStaticAttributeDialog extends BaseDialog {

	private Text errorMessageText;

	private Text nameText;

	private Text expressionText;

	public LevelStaticAttributeDialog(String title) {
		super(title);
	}

	private TabularLevelHandle input;
	private RuleHandle rule;

	public void setInput(TabularLevelHandle input) {
		this.input = input;
	}

	public void setInput(TabularLevelHandle input, RuleHandle rule) {
		this.input = input;
		this.rule = rule;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);

		Label nameLabel = new Label(container, SWT.WRAP);
		nameLabel.setText(Messages.getString("LevelStaticAttributeDialog.Label.Member")); //$NON-NLS-1$
		nameLabel.setLayoutData(new GridData());
		nameLabel.setFont(parent.getFont());

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);

		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.widthHint = 250;
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkButtonStatus();
			}
		});

		Label expressionLabel = new Label(container, SWT.WRAP);
		expressionLabel.setText(Messages.getString("LevelStaticAttributeDialog.Label.Expression")); //$NON-NLS-1$
		expressionLabel.setLayoutData(new GridData());
		expressionLabel.setFont(parent.getFont());

		expressionText = new Text(container, SWT.BORDER | SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = expressionText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - expressionText.getBorderWidth() * 2;
		expressionText.setLayoutData(gd);

		ExpressionButtonUtil.createExpressionButton(container, expressionText, new CubeExpressionProvider(input),
				input);

		errorMessageText = new Text(container, SWT.READ_ONLY | SWT.WRAP);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		errorMessageText.setLayoutData(gd);
		errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		applyDialogFont(composite);

		UIUtil.bindHelp(parent, IHelpContextIds.LEVEL_STATIC_ATTRIBUTE_DIALOG);

		initDialog();

		return composite;
	}

	protected boolean initDialog() {
		if (rule != null) {
			nameText.setText(DEUtil.resolveNull(rule.getDisplayExpression()));
			ExpressionButtonUtil.initExpressionButtonControl(expressionText,
					rule.getExpressionProperty(Rule.RULE_EXPRE_MEMBER));
		}
		return super.initDialog();
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control bar = super.createButtonBar(parent);
		checkButtonStatus();
		return bar;
	}

	private void checkButtonStatus() {
		if (nameText.getText().trim().length() == 0) {
			if (getButton(IDialogConstants.OK_ID) != null) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				setErrorMessage(Messages.getString("LevelStaticAttributeDialog.Error.Message")); //$NON-NLS-1$
			}
		} else {
			if (getButton(IDialogConstants.OK_ID) != null) {
				getButton(IDialogConstants.OK_ID).setEnabled(true);
				setErrorMessage(null);
			}
		}
	}

	public void setErrorMessage(String errorMessage) {
		if (errorMessageText != null && !errorMessageText.isDisposed()) {
			errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$

			boolean hasError = errorMessage != null && (StringConverter.removeWhiteSpaces(errorMessage)).length() > 0;
			errorMessageText.setEnabled(hasError);
			errorMessageText.setVisible(hasError);
			errorMessageText.getParent().update();

			Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage == null);
			}
		}
	}

	protected void okPressed() {
		if (rule != null) {
			if (nameText.getText().trim().length() > 0)
				rule.setDisplayExpression(nameText.getText().trim());
			rule.setRuleExpression(expressionText.getText().trim());
		} else {
			Rule rule = StructureFactory.createRule();
			rule.setProperty(Rule.DISPLAY_EXPRE_MEMBER, nameText.getText().trim());
			rule.setProperty(Rule.RULE_EXPRE_MEMBER, expressionText.getText().trim());
			try {
				input.getPropertyHandle(ILevelModel.STATIC_VALUES_PROP).addItem(rule);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
		super.okPressed();
	}
}
