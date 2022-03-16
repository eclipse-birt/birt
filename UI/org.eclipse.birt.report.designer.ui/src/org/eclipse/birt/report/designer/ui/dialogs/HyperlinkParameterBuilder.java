/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class HyperlinkParameterBuilder extends BaseDialog {

	public static final String HYPERLINK_PARAMETER = "HyperlinkParameter"; //$NON-NLS-1$
	public static final String TARGET_REPORT = "TargetReport"; //$NON-NLS-1$
	public static final String PARAMETER_HANDLE = "ParameterHandle"; //$NON-NLS-1$
	public static final String PARAMETER_VALUE = "ParameterValue"; //$NON-NLS-1$
	public static final String HYPERLINK_EXPRESSIONPROVIDER = "HyperlinkExpressionProvider";//$NON-NLS-1$
	public static final String HYPERLINK_EXPRESSIONCONTEXT = "HyperlinkExpressionContext";//$NON-NLS-1$
	private String[] items;
	private Combo paramChooser;
	private HyperlinkBuilder hyperlinkBuilder;
	private Composite valueControl;
	private Label valueLabel;
	private Composite container;

	public void setHyperlinkBuilder(HyperlinkBuilder hyperlinkBuilder) {
		this.hyperlinkBuilder = hyperlinkBuilder;
	}

	protected HyperlinkParameterBuilder(String title) {
		super(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		container = new Composite(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 80;
		container.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 10;
		container.setLayout(layout);

		new Label(container, SWT.NONE).setText(Messages.getString("HyperlinkParameterBuilder.Label.Parameter")); //$NON-NLS-1$
		paramChooser = new Combo(container, SWT.BORDER);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		paramChooser.setLayoutData(gd);

		paramChooser.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateValueControl();
			}

		});

		paramChooser.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateValueControl();
				checkOkButton();
			}

		});

		Label requiredLabel = new Label(container, SWT.NONE);
		requiredLabel.setText(Messages.getString("HyperlinkParameterBuilder.Lable.Required")); //$NON-NLS-1$

		requiredValue = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		requiredValue.setLayoutData(gd);

		Label typeLabel = new Label(container, SWT.NONE);
		typeLabel.setText(Messages.getString("HyperlinkParameterBuilder.Label.DataType")); //$NON-NLS-1$

		typeValue = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		typeValue.setLayoutData(gd);

		valueLabel = new Label(container, SWT.NONE);
		valueLabel.setText(Messages.getString("HyperlinkParameterBuilder.Label.Value")); //$NON-NLS-1$
		gd = new GridData();
		gd.exclude = true;
		valueLabel.setLayoutData(gd);
		valueLabel.setVisible(false);
		UIUtil.bindHelp(parent, IHelpContextIds.HYPERLINK_PARAMETER_DIALOG_ID);

		populateComboBoxItems();

		return composite;
	}

	protected void updateValueControl() {
		if (hyperlinkBuilder != null) {
			final Object object = hyperlinkBuilder.getParameter(paramChooser.getText());
			if (valueControl != null && !valueControl.isDisposed()) {
				valueControl.dispose();
			}

			if (object instanceof ScalarParameterHandle || object == null) {
				GridData gd = (GridData) valueLabel.getLayoutData();
				gd.exclude = false;
				valueLabel.setLayoutData(gd);
				valueLabel.setVisible(true);

				valueControl = new Composite(container, SWT.NONE);
				gd = new GridData(GridData.FILL_HORIZONTAL);
				valueControl.setLayoutData(gd);

				GridLayout layout = new GridLayout();
				layout.marginWidth = layout.marginHeight = 0;
				layout.numColumns = 2;
				valueControl.setLayout(layout);

				text = new Text(valueControl, SWT.BORDER | SWT.MULTI);
				gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.heightHint = text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - text.getBorderWidth() * 2;
				text.setLayoutData(gd);
				text.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						checkOkButton();
					}
				});

				ExpressionButtonUtil.createExpressionButton(valueControl, text,
						hyperlinkBuilder.getExpressionProvider(), handle == null ? null : handle.getElementHandle());
				if (paramBinding != null) {
					ExpressionButtonUtil.initExpressionButtonControl(text,
							hyperlinkBuilder.getParamBindingExpression(paramBinding));
					text.setFocus();
				}

				if (object instanceof ScalarParameterHandle) {
					typeValue.setText(
							hyperlinkBuilder.getDisplayDataType(((ScalarParameterHandle) object).getDataType()));
					requiredValue.setText(((ScalarParameterHandle) object).isRequired()
							? Messages.getString("HyperlinkParameterBuilder.Required.Choice.Yes") //$NON-NLS-1$
							: Messages.getString("HyperlinkParameterBuilder.Required.Choice.No")); //$NON-NLS-1$
				} else {
					typeValue.setText(""); //$NON-NLS-1$
					requiredValue.setText(""); //$NON-NLS-1$
				}
			} else {
				if (object instanceof AbstractScalarParameterHandle) {
					typeValue.setText(hyperlinkBuilder
							.getDisplayDataType(((AbstractScalarParameterHandle) object).getDataType()));
					requiredValue.setText(((AbstractScalarParameterHandle) object).isRequired()
							? Messages.getString("HyperlinkParameterBuilder.Required.Choice.Yes") //$NON-NLS-1$
							: Messages.getString("HyperlinkParameterBuilder.Required.Choice.No")); //$NON-NLS-1$
				}
				valueEditor = createValueEditor(container, object);
				if (valueEditor == null) {
					GridData gd = (GridData) valueLabel.getLayoutData();
					gd.exclude = true;
					valueLabel.setLayoutData(gd);
					valueLabel.setVisible(false);
				} else {
					GridData gd = (GridData) valueLabel.getLayoutData();
					gd.exclude = false;
					valueLabel.setLayoutData(gd);
					valueLabel.setVisible(true);
					valueEditor.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					valueEditor.addListener(SWT.Modify, new Listener() {

						@Override
						public void handleEvent(Event event) {
							checkOkButton();
						}

					});
					valueEditor.setProperty(PARAMETER_HANDLE, object);
					valueEditor.setProperty(TARGET_REPORT, hyperlinkBuilder.getTargetReportFile());
					valueEditor.setProperty(PARAMETER_VALUE,
							paramBinding == null ? null : hyperlinkBuilder.getParamBindingExpression(paramBinding));
					valueEditor.update(true);
					valueControl = (Composite) valueEditor.getControl();

					if (paramBinding != null) {
						valueEditor.getControl().setFocus();
					}
				}
			}

			container.layout();
		}
	}

	private IDialogHelper createValueEditor(Composite parent, Object parameter) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(parameter, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					final IDialogHelper helper = helperProvider.createHelper(this, HYPERLINK_PARAMETER);
					if (helper != null) {
						helper.setProperty(HYPERLINK_EXPRESSIONPROVIDER, hyperlinkBuilder.getExpressionProvider());
						helper.setProperty(HYPERLINK_EXPRESSIONCONTEXT, handle.getElementHandle());
						helper.createContent(parent);
						helper.update(true);
						return helper;
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void okPressed() {
		if (paramBinding != null) {
			List<Expression> expressions = new ArrayList<>();
			if (text != null && !text.isDisposed()) {
				expressions.add(ExpressionButtonUtil.getExpression(text));
				paramBinding.setExpression(expressions);
			} else if (valueEditor != null && !valueEditor.getControl().isDisposed()) {
				valueEditor.update(false);
				expressions.add((Expression) valueEditor.getProperty(PARAMETER_VALUE));
				paramBinding.setExpression(expressions);
			}
		} else {
			ParamBinding paramBinding = StructureFactory.createParamBinding();
			paramBinding.setParamName(paramChooser.getText());
			List<Expression> expressions = new ArrayList<>();
			if (text != null && !text.isDisposed()) {
				expressions.add(ExpressionButtonUtil.getExpression(text));
				paramBinding.setExpression(expressions);
			} else if (valueEditor != null && !valueEditor.getControl().isDisposed()) {
				valueEditor.update(false);
				expressions.add((Expression) valueEditor.getProperty(PARAMETER_VALUE));
				paramBinding.setExpression(expressions);
			}
			setResult(paramBinding);
		}
		super.okPressed();
	}

	private ParamBinding paramBinding;

	public void setParamBinding(ParamBinding paramBinding) {
		this.paramBinding = paramBinding;
		if (paramBinding != null) {
			this.items = new String[] { paramBinding.getParamName() };
		}
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (paramChooser != null && items != null) {
			paramChooser.removeAll();
			for (int i = 0; i < items.length; i++) {
				paramChooser.add(items[i], i);
			}
			if (items.length > 0) {
				paramChooser.select(0);
				updateValueControl();
			}
			if (paramBinding != null) {
				paramChooser.setEnabled(false);
			}
		}
	}

	private ActionHandle handle;
	private Text text;
	private IDialogHelper valueEditor;
	private Text typeValue;
	private Text requiredValue;

	public void setActionHandle(ActionHandle handle) {
		this.handle = handle;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		checkOkButton();
		return control;
	}

	private void checkOkButton() {
		if (hyperlinkBuilder == null || HyperlinkParameterBuilder.this.getOkButton() == null) {
			return;
		}
		Object object = hyperlinkBuilder.getParameter(paramChooser.getText());

		if (object instanceof AbstractScalarParameterHandle) {
			if (((AbstractScalarParameterHandle) object).isRequired()) {
				if (text != null && !text.isDisposed()) {
					HyperlinkParameterBuilder.this.getOkButton().setEnabled(text.getText().trim().length() != 0);
				} else if (valueEditor != null && !valueEditor.getControl().isDisposed()) {
					Expression expression = (Expression) valueEditor.getProperty(PARAMETER_VALUE);
					if (expression == null) {
						HyperlinkParameterBuilder.this.getOkButton().setEnabled(false);
					} else if (expression.getStringExpression() == null
							|| expression.getStringExpression().trim().length() == 0) {
						HyperlinkParameterBuilder.this.getOkButton().setEnabled(false);
					} else {
						HyperlinkParameterBuilder.this.getOkButton().setEnabled(true);
					}
				}

			} else {
				HyperlinkParameterBuilder.this.getOkButton().setEnabled(true);
			}
		} else if (paramChooser.getText().trim().length() == 0) {
			HyperlinkParameterBuilder.this.getOkButton().setEnabled(false);
		} else {
			HyperlinkParameterBuilder.this.getOkButton().setEnabled(true);
		}
	}
}
