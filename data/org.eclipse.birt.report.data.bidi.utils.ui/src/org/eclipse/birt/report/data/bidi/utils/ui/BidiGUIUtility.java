/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.bidi.utils.ui;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.bidi.utils.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * @author bidi_hcg
 *
 */
public class BidiGUIUtility {

	private Combo orderingSchemeCombo, textDirectionCombo, symSwapCombo, shapingCombo, numShapingCombo;

	public static final String EXTERNAL_SYSTEM_METADATA_SETTING = "External_system_metadata_setting_area"; //$NON-NLS-1$
	public static final String EXTERNAL_SYSTEM_CONTENT_SETTING = "External_system_content_setting_area"; //$NON-NLS-1$

	public BidiGUIUtility() {
	}

	public static BidiGUIUtility INSTANCE = new BidiGUIUtility();

	public Group addBiDiFormatFrame(Composite mainComposite, String biDiFormatOption, BidiFormat bidiFormat) {
		Group externalBiDiFormatFrame = new Group(mainComposite, SWT.NONE);
		externalBiDiFormatFrame.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.verticalSpan = 5;
		data.verticalIndent = 5;
		boolean isMetadataSetting = EXTERNAL_SYSTEM_METADATA_SETTING.equalsIgnoreCase(biDiFormatOption);
		if (isMetadataSetting) {
			externalBiDiFormatFrame.setText(Messages.getString("metadata.bidiframe.title")); //$NON-NLS-1$
		} else {
			externalBiDiFormatFrame.setText(Messages.getString("content.bidiframe.title")); //$NON-NLS-1$
		}
		externalBiDiFormatFrame.setLayoutData(data);

		externalBiDiFormatFrame.setEnabled(true);

		GridData innerFrameGridData = new GridData(GridData.FILL_HORIZONTAL);
		innerFrameGridData.grabExcessHorizontalSpace = true;
		innerFrameGridData.horizontalSpan = 1;
		innerFrameGridData.horizontalAlignment = GridData.FILL;
		innerFrameGridData.verticalIndent = 5;
		innerFrameGridData.minimumWidth = SWT.DEFAULT;

		GridLayout innerFrameLayout = new GridLayout();
		innerFrameLayout.numColumns = 2;
		innerFrameLayout.marginWidth = 5;
		innerFrameLayout.marginHeight = 10;

		// bidi_acgc added start
		GridData arabicGridData = new GridData();
		arabicGridData.grabExcessHorizontalSpace = true;
		arabicGridData.horizontalSpan = 2;
		arabicGridData.horizontalAlignment = GridData.FILL;
		arabicGridData.verticalIndent = 5;

		GridLayout arabicInnerFrameLayout = new GridLayout();
		arabicInnerFrameLayout.numColumns = 2;
		arabicInnerFrameLayout.marginWidth = 1;
		arabicInnerFrameLayout.marginHeight = 15;
		arabicInnerFrameLayout.horizontalSpacing = 1;
		// bidi_acgc added end

		externalBiDiFormatFrame.setLayout(innerFrameLayout);

		// create ordering scheme setting field
		createOrderSchemaField(bidiFormat, externalBiDiFormatFrame, innerFrameGridData, isMetadataSetting);

		// create text direction setting field
		createTextDirectionField(bidiFormat, externalBiDiFormatFrame, innerFrameGridData, isMetadataSetting);

		// create system swap setting field
		createSystemSwapSettingField(bidiFormat, externalBiDiFormatFrame, innerFrameGridData, isMetadataSetting);

		createContentSettingArea(bidiFormat, externalBiDiFormatFrame, innerFrameGridData, arabicGridData,
				arabicInnerFrameLayout, isMetadataSetting);

		return externalBiDiFormatFrame;
	}

	private void createSystemSwapSettingField(BidiFormat bidiFormat, Group externalBiDiFormatFrame,
			GridData innerFrameGridData, boolean isMetadataSetting) {
		Label symSwapLabel = new Label(externalBiDiFormatFrame, SWT.NONE);
		symSwapLabel.setText(
				isMetadataSetting ? BidiConstants.SYMSWAP_TITLE_METADATA : BidiConstants.SYMSWAP_TITLE_CONTENT);
		symSwapLabel.setLayoutData(innerFrameGridData);

		symSwapCombo = new Combo(externalBiDiFormatFrame, SWT.DROP_DOWN | SWT.READ_ONLY);
		symSwapCombo.setToolTipText(BidiConstants.SYMSWAP_TOOLTIP);
		symSwapCombo.add(BidiConstants.SYMSWAP_TRUE, BidiConstants.SYMSWAP_TRUE_INDX);
		symSwapCombo.add(BidiConstants.SYMSWAP_FALSE, BidiConstants.SYMSWAP_FALSE_INDX);
		if (bidiFormat.getSymSwap()) {
			symSwapCombo.select(BidiConstants.SYMSWAP_TRUE_INDX);
		} else {
			symSwapCombo.select(BidiConstants.SYMSWAP_FALSE_INDX);
		}
		symSwapCombo.setLayoutData(innerFrameGridData);
	}

	private void createTextDirectionField(BidiFormat bidiFormat, Group externalBiDiFormatFrame,
			GridData innerFrameGridData, boolean isMetadataSetting) {
		Label textDirectionLabel = new Label(externalBiDiFormatFrame, SWT.NONE);
		textDirectionLabel.setText(isMetadataSetting ? BidiConstants.TEXT_DIRECTION_TITLE_METADATA
				: BidiConstants.TEXT_DIRECTION_TITLE_CONTENT);
		textDirectionLabel.setLayoutData(innerFrameGridData);
		textDirectionCombo = new Combo(externalBiDiFormatFrame, SWT.DROP_DOWN | SWT.READ_ONLY);
		textDirectionCombo.setToolTipText(BidiConstants.TEXT_DIRECTION_TOOLTIP);
		textDirectionCombo.add(BidiConstants.TEXT_DIRECTION_LTR, BidiConstants.TEXT_DIRECTION_LTR_INDX);
		textDirectionCombo.add(BidiConstants.TEXT_DIRECTION_RTL, BidiConstants.TEXT_DIRECTION_RTL_INDX);
		textDirectionCombo.add(BidiConstants.TEXT_DIRECTION_CONTEXTLTR, BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX);
		textDirectionCombo.add(BidiConstants.TEXT_DIRECTION_CONTEXTRTL, BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX);
		textDirectionCombo.select(getTextDirectionComboIndx(bidiFormat.getTextDirection()));
		textDirectionCombo.setLayoutData(innerFrameGridData);
	}

	private void createOrderSchemaField(BidiFormat bidiFormat, Group externalBiDiFormatFrame,
			GridData innerFrameGridData, boolean isMetadataSetting) {
		Label orderingSchemeLabel = new Label(externalBiDiFormatFrame, SWT.NONE);
		orderingSchemeLabel.setText(isMetadataSetting ? BidiConstants.ORDERING_SCHEME_TITLE_METADATA
				: BidiConstants.ORDERING_SCHEME_TITLE_CONTENT);

		orderingSchemeLabel.setLayoutData(innerFrameGridData);
		orderingSchemeCombo = new Combo(externalBiDiFormatFrame, SWT.DROP_DOWN | SWT.READ_ONLY);
		orderingSchemeCombo.setToolTipText(BidiConstants.ORDERING_SCHEME_TOOLTIP);
		orderingSchemeCombo.add(BidiConstants.ORDERING_SCHEME_LOGICAL, BidiConstants.ORDERING_SCHEME_LOGICAL_INDX);
		orderingSchemeCombo.add(BidiConstants.ORDERING_SCHEME_VISUAL, BidiConstants.ORDERING_SCHEME_VISUAL_INDX);
		orderingSchemeCombo.select(getOrderingSchemeComboIndx(bidiFormat.getOrderingScheme()));
		orderingSchemeCombo.setLayoutData(innerFrameGridData);
	}

	private void createContentSettingArea(BidiFormat bidiFormat, Group externalBiDiFormatFrame,
			GridData innerFrameGridData, GridData arabicGridData, GridLayout arabicInnerFrameLayout,
			boolean isMetadataSetting) {
		// bidi_acgc added start
		Group arabicBiDiFormatFrame = new Group(externalBiDiFormatFrame, SWT.NONE);
		arabicBiDiFormatFrame.setText(BidiConstants.ARABIC_TITLE);
		arabicBiDiFormatFrame.setLayout(arabicInnerFrameLayout);
		arabicBiDiFormatFrame.setLayoutData(arabicGridData);
		// bidi_acgc added end
		Label shapingLabel = new Label(arabicBiDiFormatFrame, SWT.NONE);
		shapingLabel.setText(
				isMetadataSetting ? BidiConstants.SHAPING_TITLE_METADATA : BidiConstants.SHAPING_TITLE_CONTENT);
		shapingLabel.setLayoutData(innerFrameGridData);

		shapingCombo = new Combo(arabicBiDiFormatFrame, SWT.DROP_DOWN | SWT.READ_ONLY);
		shapingCombo.setToolTipText(BidiConstants.SHAPING_TOOLTIP);
		shapingCombo.add(BidiConstants.SHAPING_SHAPED, BidiConstants.SHAPING_SHAPED_INDX);
		shapingCombo.add(BidiConstants.SHAPING_NOMINAL, BidiConstants.SHAPING_NOMINAL_INDX);
		shapingCombo.select(getShapingComboIndx(bidiFormat.getTextShaping()));
		shapingCombo.setLayoutData(innerFrameGridData);

		Label numShapingLabel = new Label(arabicBiDiFormatFrame, SWT.NONE);
		numShapingLabel.setText(
				isMetadataSetting ? BidiConstants.NUMSHAPING_TITLE_METADATA : BidiConstants.NUMSHAPING_TITLE_CONTENT);
		numShapingLabel.setLayoutData(innerFrameGridData);
		numShapingCombo = new Combo(arabicBiDiFormatFrame, SWT.DROP_DOWN | SWT.READ_ONLY);
		numShapingCombo.setToolTipText(BidiConstants.NUMSHAPING_TOOLTIP);
		numShapingCombo.add(BidiConstants.NUMSHAPING_NOMINAL, BidiConstants.NUMSHAPING_NOMINAL_INDX);
		numShapingCombo.add(BidiConstants.NUMSHAPING_NATIONAL, BidiConstants.NUMSHAPING_NATIONAL_INDX);
		numShapingCombo.add(BidiConstants.NUMSHAPING_CONTEXT, BidiConstants.NUMSHAPING_CONTEXT_INDX);
		numShapingCombo.select(getNumShapingComboIndx(bidiFormat.getNumeralShaping()));
		numShapingCombo.setLayoutData(innerFrameGridData);
		numShapingLabel.setEnabled(numShapingCombo.isEnabled());
	}

	public static int getOrderingSchemeComboIndx(String orderingScheme) {
		if (orderingScheme.equals(BidiConstants.ORDERING_SCHEME_LOGICAL)) {
			return BidiConstants.ORDERING_SCHEME_LOGICAL_INDX;
		}
		return BidiConstants.ORDERING_SCHEME_VISUAL_INDX;
	}

	public static int getTextDirectionComboIndx(String textDirection) {
		if (textDirection.equals(BidiConstants.TEXT_DIRECTION_LTR)) {
			return BidiConstants.TEXT_DIRECTION_LTR_INDX;
		}
		if (textDirection.equals(BidiConstants.TEXT_DIRECTION_RTL)) {
			return BidiConstants.TEXT_DIRECTION_RTL_INDX;
		}
		if (textDirection.equals(BidiConstants.TEXT_DIRECTION_CONTEXTLTR)) {
			return BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX;
		}
		return BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX;
	}

	public static int getShapingComboIndx(String textShaping) {
		if (textShaping.equals(BidiConstants.SHAPING_NOMINAL)) {
			return BidiConstants.SHAPING_NOMINAL_INDX;
		}
		return BidiConstants.SHAPING_SHAPED_INDX;
	}

	public static int getNumShapingComboIndx(String numShaping) {
		if (numShaping.equals(BidiConstants.NUMSHAPING_CONTEXT)) {
			return BidiConstants.NUMSHAPING_CONTEXT_INDX;
		}
		if (numShaping.equals(BidiConstants.NUMSHAPING_NATIONAL)) {
			return BidiConstants.NUMSHAPING_NATIONAL_INDX;
		}
		return BidiConstants.NUMSHAPING_NOMINAL_INDX;
	}

	public BidiFormat getBiDiFormat(Group bidiFormatFrame) {
		String orderingScheme;
		String textDirection;
		String numeralShaping;
		String textShaping;
		boolean symSwap;
		// bidi_acgc added start
		Group arabicGroup = null;
		Control[] arabicSubControls = null;
		// bidi_acgc added end
		Control[] controls = bidiFormatFrame.getChildren();
		for (int i = 0; i < controls.length; i++) {
			if (controls[i] instanceof Combo) {
				if (BidiConstants.ORDERING_SCHEME_TOOLTIP.equals(((Combo) controls[i]).getToolTipText())) {
					orderingSchemeCombo = (Combo) controls[i];
				} else if (BidiConstants.TEXT_DIRECTION_TOOLTIP.equals(((Combo) controls[i]).getToolTipText())) {
					textDirectionCombo = (Combo) controls[i];
					// bidi_acgc deleted started : The lines are replaced below in
					// order to add a condition
					// to define Arabic specific features controls
					// else if ( BidiConstants.SHAPING_TOOLTIP
					// .equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					// shapingCombo = (Combo) controls[i];
					// else if ( BidiConstants.NUMSHAPING_TOOLTIP
					// .equals( ( (Combo) controls[i] ).getToolTipText( ) ) )
					// numShapingCombo = (Combo) controls[i];
					// bidi_acgc deleted end :
				} else if (BidiConstants.SYMSWAP_TOOLTIP.equals(((Combo) controls[i]).getToolTipText())) {
					symSwapCombo = (Combo) controls[i];
				}
			}

			// bidi_acgc added start :Check if the type of control is Group then
			// retrieve
			// the Arabic specific features controls
			if (controls[i] instanceof Group) {
				arabicGroup = (Group) controls[i];
				arabicSubControls = arabicGroup.getChildren();
				for (int j = 0; j < arabicSubControls.length; j++) {
					if (arabicSubControls[j] instanceof Combo) {
						if (BidiConstants.SHAPING_TOOLTIP.equals(((Combo) arabicSubControls[j]).getToolTipText())) {
							shapingCombo = (Combo) arabicSubControls[j];
						} else if (BidiConstants.NUMSHAPING_TOOLTIP
								.equals(((Combo) arabicSubControls[j]).getToolTipText())) {
							numShapingCombo = (Combo) arabicSubControls[j];
						}
					}
				}
			}
			// bidi_acgc added end

		} // end for loop

		switch (orderingSchemeCombo.getSelectionIndex()) {
		case BidiConstants.ORDERING_SCHEME_LOGICAL_INDX:
			orderingScheme = BidiConstants.ORDERING_SCHEME_LOGICAL;
			break;
		case BidiConstants.ORDERING_SCHEME_VISUAL_INDX:
			orderingScheme = BidiConstants.ORDERING_SCHEME_VISUAL;
			break;
		default:
			orderingScheme = ""; // shouldn't happen //$NON-NLS-1$
		}
		switch (textDirectionCombo.getSelectionIndex()) {
		case BidiConstants.TEXT_DIRECTION_LTR_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_LTR;
			break;
		case BidiConstants.TEXT_DIRECTION_RTL_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_RTL;
			break;
		case BidiConstants.TEXT_DIRECTION_CONTEXTLTR_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_CONTEXTLTR;
			break;
		case BidiConstants.TEXT_DIRECTION_CONTEXTRTL_INDX:
			textDirection = BidiConstants.TEXT_DIRECTION_CONTEXTRTL;
			break;
		default:
			textDirection = ""; // shouldn't happen //$NON-NLS-1$
		}

		symSwap = (symSwapCombo.getSelectionIndex() == BidiConstants.SYMSWAP_TRUE_INDX);

		switch (shapingCombo.getSelectionIndex()) {
		case BidiConstants.SHAPING_NOMINAL_INDX:
			textShaping = BidiConstants.SHAPING_NOMINAL;
			break;
		case BidiConstants.SHAPING_SHAPED_INDX:
			textShaping = BidiConstants.SHAPING_SHAPED;
			break;
		default:
			textShaping = ""; // shouldn't happen //$NON-NLS-1$
			break;
		}

		switch (numShapingCombo.getSelectionIndex()) {
		case BidiConstants.NUMSHAPING_NOMINAL_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_NOMINAL;
			break;
		case BidiConstants.NUMSHAPING_NATIONAL_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_NATIONAL;
			break;
		case BidiConstants.NUMSHAPING_CONTEXT_INDX:
			numeralShaping = BidiConstants.NUMSHAPING_CONTEXT;
			break;
		default:
			numeralShaping = "";// shouldn't happen //$NON-NLS-1$
			break;
		}
		return new BidiFormat(orderingScheme, textDirection, symSwap, textShaping, numeralShaping);
	}

	public void performDefaults() {
		orderingSchemeCombo.select(BidiConstants.ORDERING_SCHEME_LOGICAL_INDX);
		textDirectionCombo.select(BidiConstants.TEXT_DIRECTION_LTR_INDX);
		symSwapCombo.select(BidiConstants.SYMSWAP_TRUE_INDX);
		shapingCombo.select(BidiConstants.SHAPING_NOMINAL_INDX);
		numShapingCombo.select(BidiConstants.NUMSHAPING_NOMINAL_INDX);
	}

}
