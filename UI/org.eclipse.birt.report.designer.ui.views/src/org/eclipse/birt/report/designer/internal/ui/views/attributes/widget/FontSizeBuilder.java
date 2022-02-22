/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A font size Builder used to build predefined or custom font size.
 */
public class FontSizeBuilder extends Composite {

	protected CCombo valueCombo, unitCombo;

	private IChoiceSet units, preValues;

	private String fontSizeValue;

	private static final String[] PRE_INT_TABLE = { "9", "10", "12", "14", "16", "18", "24", "36" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	};

	private static final String DEFAULT_CHOICE = Messages.getString("FontSizeBuilder.displayname.Auto"); //$NON-NLS-1$

	private String defaultUnit = null;

	private boolean isProcessing = false;

	public FontSizeBuilder(Composite parent, int style) {
		super(parent, style);
		initFontSizeBuilder(parent, style, false);
	}

	public FontSizeBuilder(Composite parent, int style, boolean isFormStyle) {
		super(parent, style);
		initFontSizeBuilder(parent, style, isFormStyle);

	}

	/**
	 * The constructor.
	 *
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 */
	private void initFontSizeBuilder(Composite parent, int style, boolean isFormStyle) {
		setLayout(WidgetUtil.createSpaceGridLayout(2, 0, isFormStyle));
		if (isFormStyle) {
			((GridLayout) getLayout()).horizontalSpacing = 4;
		}

		if (isFormStyle) {
			valueCombo = FormWidgetFactory.getInstance().createCCombo(this, false);
		} else {
			valueCombo = new CCombo(this, SWT.DROP_DOWN);
			valueCombo.setVisibleItemCount(30);
		}

		if (isFormStyle) {
			unitCombo = FormWidgetFactory.getInstance().createCCombo(this);
		} else {
			unitCombo = new CCombo(this, SWT.DROP_DOWN);
			unitCombo.setVisibleItemCount(30);
		}

		valueCombo.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				// does nothing.
			}

			@Override
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (!isProcessing) {
					processAction();
				}
			}

		});
		valueCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				processAction();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				isProcessing = true;
				processAction();
				isProcessing = false;
			}
		});
		valueCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String val = valueCombo.getText();

				boolean enabled = (isInPreIntTable(val) || !isPredefinedValue(val)) && DEUtil.isValidNumber(val);
				if (unitCombo.getEnabled() != enabled) {
					unitCombo.setEnabled(enabled);
				}

				if (!unitCombo.isEnabled()) {
					unitCombo.deselectAll();
				} else if (unitCombo.getSelectionIndex() < 0) {
					String unit = getDefaultUnit();
					if (unit != null) {
						if (!StringUtil.isBlank(unit)) {
							unit = units.findChoice(unit).getDisplayName();
						}
						unitCombo.setText(unit);
					} else {
						unitCombo.setText(unitCombo.getItem(0));
					}
				}
			}
		});

		unitCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				processAction();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				isProcessing = true;
				processAction();
				isProcessing = false;
			}
		});

		unitCombo.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				// does nothing.
			}

			@Override
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (!isProcessing) {
					processAction();
				}
			}

		});

		initChoice();

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		valueCombo.setLayoutData(data);
		unitCombo.setVisibleItemCount(30);
		data = new GridData();
		unitCombo.setLayoutData(data);
	}

	/**
	 * @param defaultUnit The defaultUnit to set.
	 */
	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return
	 */
	private String getDefaultUnit() {
		return defaultUnit;
	}

	protected void initChoice() {
		preValues = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.FONT_SIZE_PROP);

		valueCombo.add(DEFAULT_CHOICE);

		for (int i = 0; i < PRE_INT_TABLE.length; i++) {
			valueCombo.add(PRE_INT_TABLE[i]);
		}

		String[] cvs = ChoiceSetFactory.getDisplayNamefromChoiceSet(preValues);

		for (int i = 0; i < cvs.length; i++) {
			valueCombo.add(cvs[i]);
		}

		units = ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT, StyleHandle.FONT_SIZE_PROP);

		cvs = ChoiceSetFactory.getDisplayNamefromChoiceSet(units);

		for (int i = 0; i < cvs.length; i++) {
			unitCombo.add(cvs[i]);
		}

	}

	private boolean isPredefinedValue(String val) {
		if (DEFAULT_CHOICE.equals(val) || isInPreIntTable(val)) {
			return true;
		}

		return preValues.findChoiceByDisplayName(val) != null;
	}

	private boolean isPredefinedNameValue(String val) {
		if (DEFAULT_CHOICE.equals(val) || isInPreIntTable(val)) {
			return true;
		}

		return preValues.contains(val);
	}

	private boolean isInPreIntTable(String val) {
		if (val == null) {
			return false;
		}

		for (int i = 0; i < PRE_INT_TABLE.length; i++) {
			if (PRE_INT_TABLE[i].equals(val)) {
				return true;
			}
		}

		return false;
	}

	private String getUnitsValue(String val) {
		IChoice ci = units.findChoiceByDisplayName(val);

		if (ci != null) {
			return ci.getName();
		}

		return null;
	}

	/**
	 * Processes the save action.
	 *
	 * @param rgb The new RGB value.
	 */
	protected void processAction() {
		computerFontSizeValue();
		notifyListeners(SWT.Modify, null);
	}

	private void computerFontSizeValue() {
		String val = valueCombo.getText();

		if (val == null || val.length() == 0 || DEFAULT_CHOICE.equals(val)) {
			fontSizeValue = null;
		} else if (isPredefinedValue(val)) {
			if (isInPreIntTable(val)) {
				fontSizeValue = val + DEUtil.resolveNull(getUnitsValue(unitCombo.getText()));
			} else {
				fontSizeValue = preValues.findChoiceByDisplayName(val).getName();
			}
		} else {
			fontSizeValue = val + DEUtil.resolveNull(getUnitsValue(unitCombo.getText()));
		}
	}

	/**
	 * Sets the font size value.
	 *
	 * @param size
	 */
	public void setFontSizeValue(String size) {
		if (size == null
				// || size.length( ) == 0
				|| size.equals(DEFAULT_CHOICE)) {
			valueCombo.setText(DEFAULT_CHOICE);
			return;
		}

		String[] sp = DEUtil.splitString(size);

		if (sp[0] == null && sp[1] == null) {
			valueCombo.setText(DEFAULT_CHOICE);
			return;
		}

		if (sp[0] == null) {
			if (isPredefinedNameValue(sp[1])) {
				if (preValues.contains(sp[1])) {
					valueCombo.setText(preValues.findChoice(sp[1]).getDisplayName());
				} else {
					valueCombo.setText(sp[1]);
				}
			} else {
				valueCombo.setText(""); //$NON-NLS-1$
			}
		} else {
			valueCombo.setText(sp[0]);

			if (units.contains(sp[1])) {
				String text = units.findChoice(sp[1]).getDisplayName();
				if (!unitCombo.getText().equals(text)) {
					unitCombo.setText(text);
				}
			}

		}
	}

	/**
	 * Returns the font size value.
	 *
	 * @return
	 */
	public String getFontSizeValue() {
		return fontSizeValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		valueCombo.setEnabled(enabled);

		if (enabled) {
			String val = valueCombo.getText();
			unitCombo.setEnabled((isInPreIntTable(val) || !isPredefinedValue(val)) && DEUtil.isValidNumber(val));
		} else {
			unitCombo.setEnabled(enabled);
		}

		super.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point pt = super.computeSize(wHint, hHint, changed);

		if (pt.x < 150) {
			pt.x = 150;
		}

		return pt;
	}
}
