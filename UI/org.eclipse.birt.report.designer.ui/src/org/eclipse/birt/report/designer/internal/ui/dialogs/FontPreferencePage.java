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

import java.util.Comparator;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.CSSUtil;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * Provides font preference page.
 */

public class FontPreferencePage extends BaseStylePreferencePage {

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 */
	private ColorFieldEditor color;

	private EditableComboFieldEditor name;

	private ComboBoxFieldEditor style;

	private ComboBoxFieldEditor weight;

	private ComboBoxMeasureFieldEditor size;

	private DecorationFieldEditor docoration;

	/**
	 * preview label for previewing sample text.
	 */
	private PreviewLabel sample;

	/**
	 * Constructs a new instance of font preference page.
	 * 
	 * @param model the preference store( model ) for the following field editors.
	 */
	public FontPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("FontPreferencePage.displayname.Title")); //$NON-NLS-1$

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

		((GridData) name.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 166;

		((GridData) color.getColorSelector().getLayoutData()).widthHint = 96;

		((GridData) size.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 108;
		((GridData) size.getMeasureControl(getFieldEditorParent()).getLayoutData()).widthHint = 50;

		((GridData) style.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 170;

		((GridData) weight.getComboBoxControl(getFieldEditorParent()).getLayoutData()).widthHint = 170;

	}

	/**
	 * Returns the model.
	 * 
	 * @return
	 */
	public Object getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors ()
	 */
	protected void createFieldEditors() {
		// super.createFieldEditors( );

		name = new EditableComboFieldEditor(
				StyleHandle.FONT_FAMILY_PROP, Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.FONT_FAMILY_PROP).getDefn().getDisplayNameID()),
				getFontChoiceArray(), getFieldEditorParent()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.report.designer.internal.ui.dialogs.
			 * AbstractFieldEditor#setPropValue(java.lang.String)
			 */
			protected void setPropValue(String newValue) {
				// if ( UIUtil.needAddQuote(
				// ReportDesignConstants.STYLE_ELEMENT,
				// StyleHandle.FONT_FAMILY_PROP,
				// newValue ) )
				// {
				// super.setPropValue( DEUtil.AddQuote( newValue ) );
				// }
				// else
				// {
				// super.setPropValue( newValue );
				// }
				super.setPropValue(newValue);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.report.designer.internal.ui.dialogs.
			 * EditableComboFieldEditor#updateComboForValue(java.lang.String)
			 */
			protected void updateComboForValue(String value, boolean setOldValue) {
				super.updateComboForValue(DEUtil.removeQuote(value), setOldValue);
			}
		};

		color = new ColorFieldEditor(StyleHandle.COLOR_PROP,
				Messages.getString(
						((StyleHandle) model).getPropertyHandle(StyleHandle.COLOR_PROP).getDefn().getDisplayNameID()),
				getFieldEditorParent());

		size = new ComboBoxMeasureFieldEditor(StyleHandle.FONT_SIZE_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.FONT_SIZE_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(DesignChoiceConstants.CHOICE_FONT_SIZE),
				getChoiceArray(DesignChoiceConstants.CHOICE_UNITS), getFieldEditorParent());
		size.setDefaultUnit(((StyleHandle) model).getPropertyHandle(StyleHandle.FONT_SIZE_PROP).getDefaultUnit());

		style = new ComboBoxFieldEditor(StyleHandle.FONT_STYLE_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.FONT_STYLE_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(DesignChoiceConstants.CHOICE_FONT_STYLE), getFieldEditorParent());

		weight = new ComboBoxFieldEditor(StyleHandle.FONT_WEIGHT_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.FONT_WEIGHT_PROP).getDefn()
						.getDisplayNameID()),
				getChoiceArray(DesignChoiceConstants.CHOICE_FONT_WEIGHT), getFieldEditorParent());

		docoration = new DecorationFieldEditor(StyleHandle.TEXT_UNDERLINE_PROP,
				Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.TEXT_UNDERLINE_PROP).getDefn().getDisplayNameID()),
				StyleHandle.TEXT_OVERLINE_PROP,
				Messages.getString(((StyleHandle) model)
						.getPropertyHandle(StyleHandle.TEXT_OVERLINE_PROP).getDefn().getDisplayNameID()),
				StyleHandle.TEXT_LINE_THROUGH_PROP,
				Messages.getString(((StyleHandle) model).getPropertyHandle(StyleHandle.TEXT_LINE_THROUGH_PROP).getDefn()
						.getDisplayNameID()),
				Messages.getString("FontPreferencePage.label.fontDecoration"), //$NON-NLS-1$
				getFieldEditorParent());

		addField(name);
		addField(color);
		addField(size);
		addField(style);
		addField(weight);
		addField(docoration);

		addField(new SeparatorFieldEditor(getFieldEditorParent(), false));

		Group group = new Group(getFieldEditorParent(), SWT.SHADOW_OUT);
		group.setText(Messages.getString("FontPreferencePage.text.Preview")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 400;
		gd.heightHint = 100;
		gd.horizontalSpan = 4;
		group.setLayoutData(gd);

		group.setLayout(new GridLayout());
		sample = new PreviewLabel(group, SWT.NONE);
		sample.setText(Messages.getString("FontPreferencePage.text.PreviewContent")); //$NON-NLS-1$
		sample.setLayoutData(new GridData(GridData.FILL_BOTH));
		UIUtil.bindHelp(getFieldEditorParent().getParent(), IHelpContextIds.STYLE_BUILDER_FONT_ID);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents
	 * (org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Control ct = super.createContents(parent);

		updatePreview();

		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#propertyChange
	 * (org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		updatePreview();
	}

	/**
	 * Updates sample text for preview according to the property change.
	 * 
	 */
	private void updatePreview() {
		if (sample != null) {
			String fontFamily = name.getValueForName(name.getComboBoxControl(null).getText());

			if (fontFamily == null) {
				fontFamily = "Times New Roman"; //$NON-NLS-1$
			}

			String familyValue = (String) DesignerConstants.familyMap.get(fontFamily);

			if (familyValue == null) {
				familyValue = fontFamily;
			}

			// set default font size.
			String fontSize = DesignChoiceConstants.FONT_SIZE_MEDIUM;
			int sizeValue = Integer.valueOf((String) DesignerConstants.fontMap.get(fontSize)).intValue();

			if (size.inComboNamesList(size.getComboBoxControl(getFieldEditorParent()).getText())) {
				fontSize = size.getBoxValueForName(size.getComboBoxControl(getFieldEditorParent()).getText());
				if (DesignChoiceConstants.FONT_SIZE_LARGER.equals(fontSize)) {
					fontSize = DesignChoiceConstants.FONT_SIZE_LARGE;
				} else if (DesignChoiceConstants.FONT_SIZE_SMALLER.equals(fontSize)) {
					fontSize = DesignChoiceConstants.FONT_SIZE_SMALL;
				}
				sizeValue = Integer.valueOf((String) DesignerConstants.fontMap.get(fontSize)).intValue();
			} else {
				String text = size.getComboBoxControl(getFieldEditorParent()).getText();
				String pre = size.getMeasureValueForName(size.getMeasureControl(getFieldEditorParent()).getText());

				if (DEUtil.isValidNumber(text)) {
					sizeValue = (int) CSSUtil.convertToPoint(new DimensionValue(Double.parseDouble(text), pre)) + 1;
				}
			}

			boolean italic = false;
			String fontStyle = style.getValueForName(style.getComboBoxControl(getFieldEditorParent()).getText());
			if (DesignChoiceConstants.FONT_STYLE_ITALIC.equals(fontStyle)
					|| DesignChoiceConstants.FONT_STYLE_OBLIQUE.equals(fontStyle)) {
				italic = true;
			}

			String fontWeight = weight.getValueForName(weight.getComboBoxControl(null).getText());
			boolean bold = false;
			int fw = 400;
			if (DesignChoiceConstants.FONT_WEIGHT_NORMAL.equals(fontWeight)) {
				// no change.
			} else if (DesignChoiceConstants.FONT_WEIGHT_BOLD.equals(fontWeight)) {
				bold = true;
				fw = 700;
			} else if (DesignChoiceConstants.FONT_WEIGHT_BOLDER.equals(fontWeight)) {
				bold = true;
				fw = 1000;
			} else if (DesignChoiceConstants.FONT_WEIGHT_LIGHTER.equals(fontWeight)) {
				fw = 100;
			} else {
				try {
					fw = Integer.parseInt(fontWeight);
				} catch (NumberFormatException e) {
					fw = 400;
				}

				if (fw > 700) {
					bold = true;
				}
			}

			sample.setFontFamily(familyValue);
			sample.setFontSize(sizeValue);
			sample.setBold(bold);
			sample.setItalic(italic);
			sample.setFontWeight(fw);

			// sample.setForeground( new Color( Display.getCurrent( ),
			// color.getColorSelector( ).getColorValue( ) ) );
			sample.setForeground(ColorManager.getColor(color.getColorSelector().getRGB()));

			sample.setUnderline(docoration.getUnderLinePropControl(null).getSelection());
			sample.setLinethrough(docoration.getLineThroughPropControl(null).getSelection());
			sample.setOverline(docoration.getOverLinePropControl(null).getSelection());

			sample.updateView();
		}
	}

	private String[][] getFontChoiceArray() {
		String[][] fca = getChoiceArray(DesignChoiceConstants.CHOICE_FONT_FAMILY, new AlphabeticallyComparator());

		String[] sf = DEUtil.getSystemFontNames();

		String[] af = { ChoiceSetFactory.CHOICE_AUTO, null };

		String[][] rt = new String[fca.length + sf.length + 1][2];

		rt[0] = af;
		for (int i = 0; i < rt.length - 1; i++) {
			if (i < fca.length) {
				rt[i + 1][0] = fca[i][0];
				rt[i + 1][1] = fca[i][1];
			} else {
				rt[i + 1][0] = sf[i - fca.length];
				rt[i + 1][1] = sf[i - fca.length];
			}
		}
		return rt;
	}

	/**
	 * Gets choice array of the given property name ( key ).
	 * 
	 * @param key The given property name.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray(String key) {
		return getChoiceArray(key, null);
	}

	/**
	 * Gets choice array of the given property name ( key ).
	 * 
	 * @param key The given property name.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray(String key, Comparator comparator) {
		IChoice[] choices = DEUtil.getMetaDataDictionary().getChoiceSet(key).getChoices(comparator);

		String[][] names = null;
		if (choices.length > 0) {
			names = new String[choices.length][2];
			for (int i = 0; i < choices.length; i++) {
				names[i][0] = choices[i].getDisplayName();
				names[i][1] = choices[i].getName();
			}
		}
		return names;
	}

	protected String[] getPreferenceNames() {
		return new String[] { StyleHandle.FONT_FAMILY_PROP, StyleHandle.COLOR_PROP, StyleHandle.FONT_SIZE_PROP,
				StyleHandle.FONT_STYLE_PROP, StyleHandle.FONT_WEIGHT_PROP, StyleHandle.TEXT_UNDERLINE_PROP,
				StyleHandle.TEXT_OVERLINE_PROP, StyleHandle.TEXT_LINE_THROUGH_PROP, };
	}
}