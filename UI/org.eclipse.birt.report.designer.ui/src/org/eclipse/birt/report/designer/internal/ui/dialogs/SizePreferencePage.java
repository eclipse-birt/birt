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

/**
 * Provides block preference page.
 */

public class SizePreferencePage extends BaseStylePreferencePage {

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 *
	 */
	private ComboBoxMeasureFieldEditor widthIndent;

	private ComboBoxMeasureFieldEditor heightIndent;

	/**
	 * Constructs a new instance of block preference page.
	 *
	 * @param model the preference store( model ) for the following field editors.
	 */
	public SizePreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("SizePreferencePage.displayname.Title")); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	@Override
	protected void adjustGridLayout() {
		super.adjustGridLayout();
		((GridData) widthIndent.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;
		((GridData) heightIndent.getTextControl(getFieldEditorParent()).getLayoutData()).widthHint = 116;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors ()
	 */
	@Override
	protected void createFieldEditors() {

		widthIndent = new ComboBoxMeasureFieldEditor(StyleHandle.WIDTH_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.WIDTH_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.WIDTH_PROP)),
				getFieldEditorParent());
		widthIndent.setDefaultUnit(((StyleHandle) model).getPropertyHandle(StyleHandle.WIDTH_PROP).getDefaultUnit());

		addField(widthIndent);

		heightIndent = new ComboBoxMeasureFieldEditor(StyleHandle.HEIGHT_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.HEIGHT_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.HEIGHT_PROP)),
				getFieldEditorParent());
		heightIndent.setDefaultUnit(((StyleHandle) model).getPropertyHandle(StyleHandle.HEIGHT_PROP).getDefaultUnit());

		addField(heightIndent);

		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_SIZE_ID);

	}

	/**
	 * Gets choice array of the given choise set.
	 *
	 * @param set The given choice set.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray(IChoiceSet set) {
		return getChoiceArray(set, false);
	}

	/**
	 * Gets choice array of the given choise set.
	 *
	 * @param set The given choice set.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray(IChoiceSet set, boolean addAuto) {
		IChoice[] choices = set.getChoices();

		String[][] names = null;

		if (choices.length > 0) {
			int offset = 0;

			if (addAuto) {
				offset = 1;

				names = new String[choices.length + 1][2];
				names[0][0] = ChoiceSetFactory.CHOICE_AUTO;
				names[0][1] = ""; //$NON-NLS-1$
			} else {
				names = new String[choices.length][2];
			}

			for (int i = 0; i < choices.length; i++) {
				names[i + offset][0] = choices[i].getDisplayName();
				names[i + offset][1] = choices[i].getName();
			}
		} else if (addAuto) {
			names = new String[][] { { ChoiceSetFactory.CHOICE_AUTO, "" //$NON-NLS-1$
					} };
		}

		return names;
	}

	@Override
	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.WIDTH_PROP, StyleHandle.HEIGHT_PROP, };
	}

}
