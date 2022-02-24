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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Preference page for Border Style.
 */

public class BorderPreferencePage extends BaseStylePreferencePage {

	private ComboBoxFieldEditor styleTop, styleBottom, styleLeft, styleRight;
	private ColorFieldEditor colorTop, colorBottom, colorRight, colorLeft;
	private ComboBoxMeasureFieldEditor widthTop, widthBottom, widthLeft, widthRight;

	private Group gpStyle, gpColor, gpWidth;

	private SeparatorFieldEditor styleSep, colorSep, widthSep;

	private Object model;

	/**
	 * Default constructor.
	 * 
	 * @param model the model of preference page.
	 */
	public BorderPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("BorderPreferencePage.displayname.Title")); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout() {
		((GridLayout) getFieldEditorParent().getLayout()).numColumns = 3;

		((GridData) styleSep.getLabelControl().getLayoutData()).heightHint = 3;
		((GridData) styleSep.getLabelControl().getLayoutData()).horizontalSpan = 2;

		((GridData) styleTop.getLabelControl(gpStyle).getLayoutData()).horizontalIndent = 8;
		((GridData) styleBottom.getLabelControl(gpStyle).getLayoutData()).horizontalIndent = 8;
		((GridData) styleRight.getLabelControl(gpStyle).getLayoutData()).horizontalIndent = 8;
		((GridData) styleLeft.getLabelControl(gpStyle).getLayoutData()).horizontalIndent = 8;

		((GridData) styleTop.getComboBoxControl(gpStyle).getLayoutData()).widthHint = 100;
		((GridData) styleBottom.getComboBoxControl(gpStyle).getLayoutData()).widthHint = 100;
		((GridData) styleRight.getComboBoxControl(gpStyle).getLayoutData()).widthHint = 100;
		((GridData) styleLeft.getComboBoxControl(gpStyle).getLayoutData()).widthHint = 100;

		((GridData) colorSep.getLabelControl().getLayoutData()).heightHint = 3;
		((GridData) colorSep.getLabelControl().getLayoutData()).horizontalSpan = 2;

		((GridData) colorTop.getLabelControl(gpColor).getLayoutData()).horizontalIndent = 8;
		((GridData) colorBottom.getLabelControl(gpColor).getLayoutData()).horizontalIndent = 8;
		((GridData) colorRight.getLabelControl(gpColor).getLayoutData()).horizontalIndent = 8;
		((GridData) colorLeft.getLabelControl(gpColor).getLayoutData()).horizontalIndent = 8;

		((GridData) widthSep.getLabelControl().getLayoutData()).heightHint = 3;
		((GridData) widthSep.getLabelControl().getLayoutData()).horizontalSpan = 3;

		((GridData) widthTop.getLabelControl(gpWidth).getLayoutData()).horizontalIndent = 8;
		((GridData) widthBottom.getLabelControl(gpWidth).getLayoutData()).horizontalIndent = 8;
		((GridData) widthRight.getLabelControl(gpWidth).getLayoutData()).horizontalIndent = 8;
		((GridData) widthLeft.getLabelControl(gpWidth).getLayoutData()).horizontalIndent = 8;

		((GridData) widthTop.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) widthBottom.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) widthRight.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) widthLeft.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.dialogs.BaseStylePreferencePage#
	 * createFieldEditors()
	 */
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		gpStyle = createGroupControl(getFieldEditorParent(),
				Messages.getString("BorderPreferencePage.displayname.Style"), 1, 2); //$NON-NLS-1$

		styleSep = new SeparatorFieldEditor(gpStyle, false);

		styleTop = new ComboBoxFieldEditor(
				StyleHandle.BORDER_TOP_STYLE_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.BORDER_TOP_STYLE_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_TOP_STYLE_PROP), gpStyle);

		styleBottom = new ComboBoxFieldEditor(
				StyleHandle.BORDER_BOTTOM_STYLE_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.BORDER_BOTTOM_STYLE_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_BOTTOM_STYLE_PROP), gpStyle);

		styleLeft = new ComboBoxFieldEditor(
				StyleHandle.BORDER_LEFT_STYLE_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.BORDER_LEFT_STYLE_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_LEFT_STYLE_PROP), gpStyle);

		styleRight = new ComboBoxFieldEditor(
				StyleHandle.BORDER_RIGHT_STYLE_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.BORDER_RIGHT_STYLE_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_RIGHT_STYLE_PROP), gpStyle);

		gpColor = createGroupControl(getFieldEditorParent(),
				Messages.getString("BorderPreferencePage.displayname.Color"), 1, 2); //$NON-NLS-1$

		colorSep = new SeparatorFieldEditor(gpColor, false);

		colorTop = new ColorFieldEditor(StyleHandle.BORDER_TOP_COLOR_PROP, Messages.getString(((StyleHandle) model)
				.getPropertyHandle(StyleHandle.BORDER_TOP_COLOR_PROP).getDefn().getDisplayNameID()), gpColor);
		colorBottom = new ColorFieldEditor(
				StyleHandle.BORDER_BOTTOM_COLOR_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.BORDER_BOTTOM_COLOR_PROP).getDefn().getDisplayNameID()),
				gpColor);
		colorLeft = new ColorFieldEditor(StyleHandle.BORDER_LEFT_COLOR_PROP, Messages.getString(((StyleHandle) model)
				.getPropertyHandle(StyleHandle.BORDER_LEFT_COLOR_PROP).getDefn().getDisplayNameID()), gpColor);
		colorRight = new ColorFieldEditor(StyleHandle.BORDER_RIGHT_COLOR_PROP, Messages.getString(((StyleHandle) model)
				.getPropertyHandle(StyleHandle.BORDER_RIGHT_COLOR_PROP).getDefn().getDisplayNameID()), gpColor);

		gpWidth = createGroupControl(getFieldEditorParent(),
				Messages.getString("BorderPreferencePage.displayname.Width"), 2, 3); //$NON-NLS-1$

		widthSep = new SeparatorFieldEditor(gpWidth, false);

		widthTop = new ComboBoxMeasureFieldEditor(StyleHandle.BORDER_TOP_WIDTH_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_TOP_WIDTH_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_TOP_WIDTH_PROP),
				getMeasureChoiceArray(StyleHandle.BORDER_TOP_WIDTH_PROP), gpWidth);
		widthTop.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_TOP_WIDTH_PROP).getDefaultUnit());

		widthBottom = new ComboBoxMeasureFieldEditor(StyleHandle.BORDER_BOTTOM_WIDTH_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_BOTTOM_WIDTH_PROP)
						.getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_BOTTOM_WIDTH_PROP),
				getMeasureChoiceArray(StyleHandle.BORDER_BOTTOM_WIDTH_PROP), gpWidth);
		widthBottom.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_BOTTOM_WIDTH_PROP).getDefaultUnit());

		widthLeft = new ComboBoxMeasureFieldEditor(StyleHandle.BORDER_LEFT_WIDTH_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_LEFT_WIDTH_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_LEFT_WIDTH_PROP),
				getMeasureChoiceArray(StyleHandle.BORDER_LEFT_WIDTH_PROP), gpWidth);
		widthLeft.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_LEFT_WIDTH_PROP).getDefaultUnit());

		widthRight = new ComboBoxMeasureFieldEditor(StyleHandle.BORDER_RIGHT_WIDTH_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_RIGHT_WIDTH_PROP)
						.getDefn().getDisplayNameID()),
				getChoiceArray(StyleHandle.BORDER_RIGHT_WIDTH_PROP),
				getMeasureChoiceArray(StyleHandle.BORDER_RIGHT_WIDTH_PROP), gpWidth);
		widthRight.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.BORDER_RIGHT_WIDTH_PROP).getDefaultUnit());

		addField(styleTop);
		addField(styleRight);
		addField(styleBottom);
		addField(styleLeft);

		addField(colorTop);
		addField(colorRight);
		addField(colorBottom);
		addField(colorLeft);

		addField(widthTop);
		addField(widthRight);
		addField(widthBottom);
		addField(widthLeft);

		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_BORDER_ID);

	}

	private Group createGroupControl(Composite parent, String labelText, int horizontalSpan, int numColumns) {
		Group gp = new Group(parent, 0);
		gp.setText(labelText);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		// gdata.heightHint = 125;
		gdata.horizontalSpan = horizontalSpan;
		gp.setLayoutData(gdata);
		gp.setLayout(new GridLayout(numColumns, false));

		return gp;
	}

	private String[][] getChoiceArray(String propName) {
		IChoiceSet ci = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT, propName);

		if (ci != null) {
			IChoice[] cs = ci.getChoices();

			String[][] rt = new String[cs.length][2];

			for (int i = 0; i < cs.length; i++) {
				rt[i][0] = cs[i].getDisplayName();
				rt[i][1] = cs[i].getName();
			}

			return rt;
		}

		return new String[0][2];
	}

	private String[][] getMeasureChoiceArray(String propName) {
		IChoiceSet ci = ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT, propName);

		if (ci != null) {
			IChoice[] cs = ci.getChoices();

			String[][] rt = new String[cs.length][2];

			for (int i = 0; i < cs.length; i++) {
				rt[i][0] = cs[i].getDisplayName();
				rt[i][1] = cs[i].getName();
			}

			return rt;
		}

		return new String[0][2];
	}

	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.BORDER_TOP_STYLE_PROP, StyleHandle.BORDER_BOTTOM_STYLE_PROP,
				StyleHandle.BORDER_LEFT_STYLE_PROP, StyleHandle.BORDER_RIGHT_STYLE_PROP,
				StyleHandle.BORDER_TOP_WIDTH_PROP, StyleHandle.BORDER_BOTTOM_WIDTH_PROP,
				StyleHandle.BORDER_LEFT_WIDTH_PROP, StyleHandle.BORDER_RIGHT_WIDTH_PROP,
				StyleHandle.BORDER_TOP_COLOR_PROP, StyleHandle.BORDER_BOTTOM_COLOR_PROP,
				StyleHandle.BORDER_LEFT_COLOR_PROP, StyleHandle.BORDER_RIGHT_COLOR_PROP, };
	}
}
