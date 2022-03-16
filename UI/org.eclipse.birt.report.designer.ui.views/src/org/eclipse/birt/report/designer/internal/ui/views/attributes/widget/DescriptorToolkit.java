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

public class DescriptorToolkit {

	public static TextPropertyDescriptor createTextPropertyDescriptor(boolean formStyle) {
		TextPropertyDescriptor text = new TextPropertyDescriptor(formStyle);
		return text;
	}

	public static SimpleComboPropertyDescriptor createSimpleComboPropertyDescriptor(boolean formStyle) {
		SimpleComboPropertyDescriptor descriptor = new SimpleComboPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static SimpleComboPropertyDescriptor createTocSimpleComboPropertyDescriptor(boolean formStyle) {
		SimpleComboPropertyDescriptor descriptor = new TocSimpleComboPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static ComboPropertyDescriptor createComboPropertyDescriptor(boolean formStyle) {
		ComboPropertyDescriptor descriptor = new ComboPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static CComboPropertyDescriptor createCComboPropertyDescriptor(boolean formStyle) {
		CComboPropertyDescriptor descriptor = new CComboPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static StyleComboPropertyDescriptor createStyleComboPropertyDescriptor(boolean formStyle) {
		StyleComboPropertyDescriptor descriptor = new StyleComboPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static ColorPropertyDescriptor createColorPropertyDescriptor(boolean formStyle) {
		ColorPropertyDescriptor descriptor = new ColorPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static UnitPropertyDescriptor createUnitPropertyDescriptor(boolean formStyle) {
		UnitPropertyDescriptor descriptor = new UnitPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static ExpressionPropertyDescriptor createExpressionPropertyDescriptor(boolean formStyle) {
		ExpressionPropertyDescriptor expression = new ExpressionPropertyDescriptor(formStyle);
		return expression;
	}

	public static TextAndButtonDescriptor createTextAndButtonDescriptor(boolean formStyle) {
		TextAndButtonDescriptor descriptor = new TextAndButtonDescriptor(formStyle);
		return descriptor;
	}

	public static FontSizePropertyDescriptor createFontSizePropertyDescriptor(boolean formStyle) {
		FontSizePropertyDescriptor descriptor = new FontSizePropertyDescriptor(formStyle);
		return descriptor;
	}

	public static TogglePropertyDescriptor createTogglePropertyDescriptor() {
		TogglePropertyDescriptor descriptor = new TogglePropertyDescriptor();
		return descriptor;
	}

	public static CheckPropertyDescriptor createCheckPropertyDescriptor(boolean formStyle) {
		CheckPropertyDescriptor descriptor = new CheckPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static FormPropertyDescriptor createFormPropertyDescriptor(boolean formStyle) {
		FormPropertyDescriptor descriptor = new FormPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static SortingFormPropertyDescriptor createSortingFormPropertyDescriptor(boolean formStyle) {
		SortingFormPropertyDescriptor descriptor = new SortingFormPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static FontAlignPropertyDescriptor createFontAlignPropertyDescriptor() {
		FontAlignPropertyDescriptor descriptor = new FontAlignPropertyDescriptor();
		return descriptor;
	}

	public static FontStylePropertyDescriptor createFontStylePropertyDescriptor(boolean formStyle) {
		FontStylePropertyDescriptor descriptor = new FontStylePropertyDescriptor(formStyle);
		return descriptor;
	}

	public static MarignPropertyDescriptor createMarignPropertyDescriptor(boolean formStyle) {
		MarignPropertyDescriptor descriptor = new MarignPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static MarginsPropertyDescriptor createSpinnerPropertyDescriptor(boolean formStyle) {
		MarginsPropertyDescriptor descriptor = new MarginsPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static ResourceKeyDescriptor createResourceKeyDescriptor(boolean formStyle) {
		ResourceKeyDescriptor descriptor = new ResourceKeyDescriptor(formStyle);
		return descriptor;
	}

	public static BorderPropertyDescriptor createBorderPropertyDescriptor(boolean formStyle) {
		BorderPropertyDescriptor descriptor = new BorderPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static AdvancePropertyDescriptor createAdvancePropertyDescriptor(boolean formStyle) {
		AdvancePropertyDescriptor descriptor = new AdvancePropertyDescriptor(formStyle);
		return descriptor;
	}

	public static ComplexUnitPropertyDescriptor createComplexUnitPropertyDescriptor(boolean formStyle) {
		ComplexUnitPropertyDescriptor descriptor = new ComplexUnitPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static DualRadioButtonPropertyDescriptor createRadioButtonPropertyDescriptor(boolean formStyle) {
		DualRadioButtonPropertyDescriptor descriptor = new DualRadioButtonPropertyDescriptor(formStyle);
		return descriptor;
	}

	public static RadioGroupPropertyDescriptor createRadioGroupPropertyDescriptor(boolean formStyle) {
		RadioGroupPropertyDescriptor descriptor = new RadioGroupPropertyDescriptor(formStyle);
		return descriptor;
	}

}
