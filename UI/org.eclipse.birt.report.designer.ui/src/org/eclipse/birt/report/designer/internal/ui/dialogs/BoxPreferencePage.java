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
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

/**
 * Preference page for Box Style.
 */
public class BoxPreferencePage extends BaseStylePreferencePage {

	private Object model;

	private ComboBoxMeasureFieldEditor paddingTop, paddingRight, paddingBottom, paddingLeft;
	private ComboBoxMeasureFieldEditor marginTop, marginRight, marginBottom, marginLeft;

	private SeparatorFieldEditor paddingSep1, marginSep1;
	private Group gpPadding, gpMargin;

	/**
	 * Default constructor.
	 *
	 * @param model the model of preference page.
	 */
	public BoxPreferencePage(Object model) {
		super(model);
		this.model = model;
		setTitle(Messages.getString("BoxPreferencePage.displayname.Title")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	@Override
	protected void adjustGridLayout() {
		((GridData) paddingSep1.getLabelControl().getLayoutData()).heightHint = 3;
		((GridData) paddingSep1.getLabelControl().getLayoutData()).horizontalSpan = 3;

		((GridData) paddingTop.getLabelControl(gpPadding).getLayoutData()).horizontalIndent = 8;
		((GridData) paddingBottom.getLabelControl(gpPadding).getLayoutData()).horizontalIndent = 8;
		((GridData) paddingRight.getLabelControl(gpPadding).getLayoutData()).horizontalIndent = 8;
		((GridData) paddingLeft.getLabelControl(gpPadding).getLayoutData()).horizontalIndent = 8;

		((GridData) paddingTop.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;
		((GridData) paddingBottom.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;
		((GridData) paddingRight.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;
		((GridData) paddingLeft.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;

		((GridData) marginSep1.getLabelControl().getLayoutData()).heightHint = 3;
		((GridData) marginSep1.getLabelControl().getLayoutData()).horizontalSpan = 3;

		((GridData) marginTop.getLabelControl(gpMargin).getLayoutData()).horizontalIndent = 8;
		((GridData) marginBottom.getLabelControl(gpMargin).getLayoutData()).horizontalIndent = 8;
		((GridData) marginRight.getLabelControl(gpMargin).getLayoutData()).horizontalIndent = 8;
		((GridData) marginLeft.getLabelControl(gpMargin).getLayoutData()).horizontalIndent = 8;

		((GridData) marginTop.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) marginBottom.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) marginRight.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
		((GridData) marginLeft.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 100;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.designer.internal.ui.dialogs.BaseStylePreferencePage#
	 * createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		getFieldEditorParent().setLayout(new GridLayout());

		gpPadding = new Group(getFieldEditorParent(), 0);
		gpPadding.setText(Messages.getString("BoxPreferencePage.text.Padding")); //$NON-NLS-1$
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gpPadding.setLayoutData(gdata);
		gpPadding.setLayout(new GridLayout(3, false));

		paddingSep1 = new SeparatorFieldEditor(gpPadding, false);

		paddingTop = new ComboBoxMeasureFieldEditor(
				IStyleModel.PADDING_TOP_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(IStyleModel.PADDING_TOP_PROP).getDefn().getDisplayNameID()),
				getMeasureChoiceArray(IStyleModel.PADDING_TOP_PROP), gpPadding);
		paddingTop
				.setDefaultUnit(((StyleHandle) model).getPropertyHandle(IStyleModel.PADDING_TOP_PROP).getDefaultUnit());

		paddingBottom = new ComboBoxMeasureFieldEditor(
				IStyleModel.PADDING_BOTTOM_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(IStyleModel.PADDING_BOTTOM_PROP).getDefn().getDisplayNameID()),
				getMeasureChoiceArray(IStyleModel.PADDING_BOTTOM_PROP), gpPadding);
		paddingBottom.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(IStyleModel.PADDING_BOTTOM_PROP).getDefaultUnit());

		paddingLeft = new ComboBoxMeasureFieldEditor(
				IStyleModel.PADDING_LEFT_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(IStyleModel.PADDING_LEFT_PROP).getDefn().getDisplayNameID()),
				getMeasureChoiceArray(IStyleModel.PADDING_LEFT_PROP), gpPadding);
		paddingLeft.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(IStyleModel.PADDING_LEFT_PROP).getDefaultUnit());

		paddingRight = new ComboBoxMeasureFieldEditor(
				IStyleModel.PADDING_RIGHT_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(IStyleModel.PADDING_RIGHT_PROP).getDefn().getDisplayNameID()),
				getMeasureChoiceArray(IStyleModel.PADDING_RIGHT_PROP), gpPadding);
		paddingRight.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(IStyleModel.PADDING_RIGHT_PROP).getDefaultUnit());

		gpMargin = new Group(getFieldEditorParent(), 0);
		gpMargin.setText(Messages.getString("BoxPreferencePage.text.Margin")); //$NON-NLS-1$
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gpMargin.setLayoutData(gdata);
		gpMargin.setLayout(new GridLayout(3, false));

		marginSep1 = new SeparatorFieldEditor(gpMargin, false);

		marginTop = new ComboBoxMeasureFieldEditor(IStyleModel.MARGIN_TOP_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_TOP_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(IStyleModel.MARGIN_TOP_PROP), getMeasureChoiceArray(IStyleModel.MARGIN_TOP_PROP),
				gpMargin);
		marginTop.setDefaultUnit(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_TOP_PROP).getDefaultUnit());

		marginBottom = new ComboBoxMeasureFieldEditor(IStyleModel.MARGIN_BOTTOM_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_BOTTOM_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(IStyleModel.MARGIN_BOTTOM_PROP), getMeasureChoiceArray(IStyleModel.MARGIN_BOTTOM_PROP),
				gpMargin);
		marginBottom.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_BOTTOM_PROP).getDefaultUnit());

		marginLeft = new ComboBoxMeasureFieldEditor(IStyleModel.MARGIN_LEFT_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_LEFT_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(IStyleModel.MARGIN_LEFT_PROP), getMeasureChoiceArray(IStyleModel.MARGIN_LEFT_PROP),
				gpMargin);
		marginLeft
				.setDefaultUnit(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_LEFT_PROP).getDefaultUnit());

		marginRight = new ComboBoxMeasureFieldEditor(IStyleModel.MARGIN_RIGHT_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_RIGHT_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(IStyleModel.MARGIN_RIGHT_PROP), getMeasureChoiceArray(IStyleModel.MARGIN_RIGHT_PROP),
				gpMargin);
		marginRight.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(IStyleModel.MARGIN_RIGHT_PROP).getDefaultUnit());

		addField(paddingTop);
		addField(paddingBottom);
		addField(paddingLeft);
		addField(paddingRight);

		addField(marginTop);
		addField(marginBottom);
		addField(marginLeft);
		addField(marginRight);

		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_BOX_ID);

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

	@Override
	protected String[] getPreferenceNames() {
		return new String[] { IStyleModel.PADDING_TOP_PROP, IStyleModel.PADDING_BOTTOM_PROP,
				IStyleModel.PADDING_LEFT_PROP, IStyleModel.PADDING_RIGHT_PROP, IStyleModel.MARGIN_TOP_PROP,
				IStyleModel.MARGIN_BOTTOM_PROP, IStyleModel.MARGIN_LEFT_PROP, IStyleModel.MARGIN_RIGHT_PROP, };
	}
}
