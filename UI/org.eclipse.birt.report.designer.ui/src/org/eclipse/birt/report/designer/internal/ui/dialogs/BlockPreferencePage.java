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

public class BlockPreferencePage extends BaseStylePreferencePage {

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 * 
	 */
	private ComboBoxMeasureFieldEditor lineHeight;

	private ComboBoxMeasureFieldEditor charSpacing;

	private ComboBoxMeasureFieldEditor wordSpacing;

	private ComboBoxMeasureFieldEditor textIndent;

	private ComboBoxFieldEditor verticalAlign;

	private ComboBoxFieldEditor textAlign;

	private ComboBoxFieldEditor textTrans;

	private ComboBoxFieldEditor whiteSpace;

	private ComboBoxFieldEditor display;

	private ComboBoxFieldEditor direction; // bidi_hcg

	/**
	 * Constructs a new instance of block preference page.
	 * 
	 * @param model the preference store( model ) for the following field editors.
	 */
	public BlockPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("BlockPreferencePage.displayname.Title")); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout() {
		super.adjustGridLayout();

		((GridData) verticalAlign.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;

		((GridData) textAlign.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;

		((GridData) textTrans.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;

		((GridData) whiteSpace.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;

		((GridData) display.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;

		// bidi_hcg
		((GridData) direction.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 167;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		lineHeight = new ComboBoxMeasureFieldEditor(StyleHandle.LINE_HEIGHT_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.LINE_HEIGHT_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.LINE_HEIGHT_PROP)),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.LINE_HEIGHT_PROP)),
				getFieldEditorParent());
		lineHeight
				.setDefaultUnit(((StyleHandle) model).getPropertyHandle(StyleHandle.LINE_HEIGHT_PROP).getDefaultUnit());

		charSpacing = new ComboBoxMeasureFieldEditor(StyleHandle.LETTER_SPACING_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.LETTER_SPACING_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.LETTER_SPACING_PROP)),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.LETTER_SPACING_PROP)),
				getFieldEditorParent());
		charSpacing.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.LETTER_SPACING_PROP).getDefaultUnit());

		wordSpacing = new ComboBoxMeasureFieldEditor(StyleHandle.WORD_SPACING_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.WORD_SPACING_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.WORD_SPACING_PROP)),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.WORD_SPACING_PROP)),
				getFieldEditorParent());
		wordSpacing.setDefaultUnit(
				((StyleHandle) model).getPropertyHandle(StyleHandle.WORD_SPACING_PROP).getDefaultUnit());

		verticalAlign = new ComboBoxFieldEditor(StyleHandle.VERTICAL_ALIGN_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.VERTICAL_ALIGN_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.VERTICAL_ALIGN_PROP), true),
				getFieldEditorParent());

		textAlign = new ComboBoxFieldEditor(StyleHandle.TEXT_ALIGN_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_ALIGN_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.TEXT_ALIGN_PROP), true),
				getFieldEditorParent());

		textIndent = new ComboBoxMeasureFieldEditor(StyleHandle.TEXT_INDENT_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_INDENT_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getDimensionChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.TEXT_INDENT_PROP)),
				getFieldEditorParent());
		textIndent
				.setDefaultUnit(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_INDENT_PROP).getDefaultUnit());

		textTrans = new ComboBoxFieldEditor(StyleHandle.TEXT_TRANSFORM_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_TRANSFORM_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.TEXT_TRANSFORM_PROP)),
				getFieldEditorParent());

		whiteSpace = new ComboBoxFieldEditor(StyleHandle.WHITE_SPACE_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.WHITE_SPACE_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.WHITE_SPACE_PROP)),
				getFieldEditorParent());

		display = new ComboBoxFieldEditor(StyleHandle.DISPLAY_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.DISPLAY_PROP).getDefn().getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.DISPLAY_PROP)),
				getFieldEditorParent());

		addField(lineHeight);
		addField(charSpacing);
		addField(wordSpacing);
		addField(verticalAlign);
		addField(textAlign);
		addField(textIndent);
		addField(textTrans);
		addField(whiteSpace);
		addField(display);

		// bidi_hcg start
		direction = new ComboBoxFieldEditor(StyleHandle.TEXT_DIRECTION_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_DIRECTION_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT,
						StyleHandle.TEXT_DIRECTION_PROP), true),
				getFieldEditorParent());
		addField(direction);
		// bidi_hcg end

		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_TEXTBLOCK_ID);

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

	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.LINE_HEIGHT_PROP, StyleHandle.LETTER_SPACING_PROP,
				StyleHandle.WORD_SPACING_PROP, StyleHandle.VERTICAL_ALIGN_PROP, StyleHandle.TEXT_ALIGN_PROP,
				StyleHandle.TEXT_INDENT_PROP, StyleHandle.TEXT_TRANSFORM_PROP, StyleHandle.WHITE_SPACE_PROP,
				StyleHandle.DISPLAY_PROP, StyleHandle.TEXT_DIRECTION_PROP, };
	}

}