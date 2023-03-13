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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MasterColumnsDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 *
 */

public class MasterColumnsPropertyDescriptor extends PropertyDescriptor {

	public MasterColumnsPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	private Composite content;
	private Button oneColumnsButton;
	private Button twoColumnsButton;
	private Button threeColumnsButton;
	private Button customColumnsButton;
	private Spinner spinner;

	SelectionAdapter listener = new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (((Button) e.widget).getSelection()) {
				checkButtonSelection((Button) e.widget, true);
			}
		}

	};

	@Override
	public Control createControl(Composite parent) {
		content = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		content.setLayout(layout);

		GridData spanGd = new GridData();
		spanGd.widthHint = 40;
		spanGd.verticalSpan = 2;

		oneColumnsButton = FormWidgetFactory.getInstance().createButton(content, SWT.RADIO, isFormStyle());
		oneColumnsButton.setText(Messages.getString("MasterColumnsPropertyDescriptor.Button.Text.OneColumn")); //$NON-NLS-1$
		oneColumnsButton.addKeyListener(new KeyAdapter() {
		});
		oneColumnsButton.addSelectionListener(listener);

		FormWidgetFactory.getInstance().createLabel(content, isFormStyle()).setLayoutData(spanGd);

		twoColumnsButton = FormWidgetFactory.getInstance().createButton(content, SWT.RADIO, isFormStyle());
		twoColumnsButton.setText(Messages.getString("MasterColumnsPropertyDescriptor.Button.Text.TwoColumns")); //$NON-NLS-1$
		twoColumnsButton.addSelectionListener(listener);
		twoColumnsButton.addKeyListener(new KeyAdapter() {
		});

		FormWidgetFactory.getInstance().createLabel(content, isFormStyle()).setLayoutData(spanGd);

		threeColumnsButton = FormWidgetFactory.getInstance().createButton(content, SWT.RADIO, isFormStyle());
		threeColumnsButton.setText(Messages.getString("MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns")); //$NON-NLS-1$
		threeColumnsButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		threeColumnsButton.addSelectionListener(listener);
		threeColumnsButton.addKeyListener(new KeyAdapter() {
		});

		FormWidgetFactory.getInstance().createLabel(content, isFormStyle()).setLayoutData(spanGd);

		customColumnsButton = FormWidgetFactory.getInstance().createButton(content, SWT.RADIO, isFormStyle());
		customColumnsButton.setText(Messages.getString("MasterColumnsPropertyDescriptor.Button.Text.Custom")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		customColumnsButton.setLayoutData(gd);
		customColumnsButton.addSelectionListener(listener);
		customColumnsButton.addKeyListener(new KeyAdapter() {
		});

		Label oneColumnLabel = FormWidgetFactory.getInstance().createLabel(content, isFormStyle());
		oneColumnLabel.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ATTRIBUTE_ONE_COLUMN));

		Label twoColumnLabel = FormWidgetFactory.getInstance().createLabel(content, isFormStyle());
		twoColumnLabel.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ATTRIBUTE_TWO_COLUMNS));

		Label threeColumnLabel = FormWidgetFactory.getInstance().createLabel(content, isFormStyle());
		threeColumnLabel
				.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ATTRIBUTE_THTREE_COLUMNS));

		Label customNumberLabel = FormWidgetFactory.getInstance().createLabel(content, isFormStyle());
		customNumberLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		customNumberLabel.setText(Messages.getString("MasterColumnsPropertyDescriptor.Combo.Text.Column.Number")); //$NON-NLS-1$
		if (isFormStyle()) {
			spinner = FormWidgetFactory.getInstance().createSpinner(content, SWT.NONE);
		} else {
			spinner = new Spinner(content, SWT.BORDER);
		}
		spinner.setEnabled(false);
		spinner.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (customColumnsButton.getSelection()) {
					try {
						save(spinner.getText());
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
					}
				}
			}

		});
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 60;
		spinner.setLayoutData(gd);
		spinner.setMinimum(4);
		return content;
	}

	private void checkButtonSelection(Button button) {
		checkButtonSelection(button, false);
	}

	private void checkButtonSelection(Button button, boolean save) {
		try {
			if (oneColumnsButton == button && oneColumnsButton.getSelection()) {
				oneColumnsButton.setSelection(true);
				twoColumnsButton.setSelection(false);
				threeColumnsButton.setSelection(false);
				customColumnsButton.setSelection(false);
				spinner.setEnabled(false);
				if (save) {
					save(MasterColumnsDescriptorProvider.ONE_COLUMN);
				}
			} else if (twoColumnsButton == button && twoColumnsButton.getSelection()) {
				twoColumnsButton.setSelection(true);
				oneColumnsButton.setSelection(false);
				threeColumnsButton.setSelection(false);
				customColumnsButton.setSelection(false);
				spinner.setEnabled(false);
				if (save) {
					save(MasterColumnsDescriptorProvider.TWO_COLUMNS);
				}
			} else if (threeColumnsButton == button && threeColumnsButton.getSelection()) {
				threeColumnsButton.setSelection(true);
				oneColumnsButton.setSelection(false);
				twoColumnsButton.setSelection(false);
				customColumnsButton.setSelection(false);
				spinner.setEnabled(false);
				if (save) {
					save(MasterColumnsDescriptorProvider.THREE_COLUMNS);
				}
			} else if (customColumnsButton == button && customColumnsButton.getSelection()) {
				customColumnsButton.setSelection(true);
				oneColumnsButton.setSelection(false);
				twoColumnsButton.setSelection(false);
				threeColumnsButton.setSelection(false);
				spinner.setEnabled(true);
				if (save) {
					save(spinner.getText());
				}
			}
		} catch (SemanticException e1) {
			ExceptionUtil.handle(e1);
		}
	}

	@Override
	public Control getControl() {
		return content;
	}

	@Override
	public void load() {
		if (getDescriptorProvider() != null) {

			Object value = getDescriptorProvider().load();
			if (value != null) {
				try {
					int columns = Integer.parseInt(value.toString());
					if (columns == 1) {
						oneColumnsButton.setSelection(true);
						checkButtonSelection(oneColumnsButton);
					} else if (columns == 2) {
						twoColumnsButton.setSelection(true);
						checkButtonSelection(twoColumnsButton);
					} else if (columns == 3) {
						threeColumnsButton.setSelection(true);
						checkButtonSelection(threeColumnsButton);
					} else if (columns > 1) {
						customColumnsButton.setSelection(true);
						spinner.setSelection(columns);
						spinner.setEnabled(true);
						checkButtonSelection(customColumnsButton);
					} else {
						oneColumnsButton.setSelection(true);
						checkButtonSelection(oneColumnsButton);
					}
				} catch (NumberFormatException e) {
					oneColumnsButton.setSelection(true);
					checkButtonSelection(oneColumnsButton);
				}
			} else {
				oneColumnsButton.setSelection(true);
				checkButtonSelection(oneColumnsButton);
			}
			spinner.setEnabled(customColumnsButton.getSelection());

		}
	}

	@Override
	public void save(Object obj) throws SemanticException {
		if (oneColumnsButton.getSelection()) {
			getDescriptorProvider().save(MasterColumnsDescriptorProvider.ONE_COLUMN);
		}
		if (twoColumnsButton.getSelection()) {
			getDescriptorProvider().save(MasterColumnsDescriptorProvider.TWO_COLUMNS);
		}
		if (threeColumnsButton.getSelection()) {
			getDescriptorProvider().save(MasterColumnsDescriptorProvider.THREE_COLUMNS);
		} else if (customColumnsButton.getSelection()) {
			if (spinner.getText().trim().length() == 0) {
				getDescriptorProvider().save(null);
			} else {
				getDescriptorProvider().save(spinner.getText().trim());
			}
		}
	}

	@Override
	public void setInput(Object handle) {
		getDescriptorProvider().setInput(handle);
	}

}
