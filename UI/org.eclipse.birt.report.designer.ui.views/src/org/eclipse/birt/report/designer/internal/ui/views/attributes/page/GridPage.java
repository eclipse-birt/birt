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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontSizeSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontStyleSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.RadioGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Grid element.
 */
public class GridPage extends GeneralPage {

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * GeneralPage#buildContent()
	 */
	@Override
	protected void buildContent() {
		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.GRID_ITEM);

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.GRID_ITEM);

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.GRID_ITEM);

		PropertyDescriptorProvider canShrinkProvider = new PropertyDescriptorProvider(StyleHandle.CAN_SHRINK_PROP,
				ReportDesignConstants.GRID_ITEM);
		canShrinkProvider.enableReset(true);

		ComboPropertyDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.VERTICAL_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT);
		vAlignProvider.enableReset(true);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM);

		ColorPropertyDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider(
				StyleHandle.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		backgroundProvider.enableReset(true);

		// Defines sections.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);

		ComplexUnitSection widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);

		ComplexUnitSection heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);

		CheckSection canShrinkSection = new CheckSection(container, true);

		ComboSection vAlignSection = new ComboSection(vAlignProvider.getDisplayName(), container, true);

		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		ColorSection backgroundSection = new ColorSection(backgroundProvider.getDisplayName(), container, true);

		// Sets providers.

		nameSection.setProvider(nameProvider);
		widthSection.setProvider(widthProvider);
		heightSection.setProvider(heightProvider);
		canShrinkSection.setProvider(canShrinkProvider);
		vAlignSection.setProvider(vAlignProvider);
		styleSection.setProvider(styleProvider);
		backgroundSection.setProvider(backgroundProvider);

		// Sets widths.

		nameSection.setWidth(200);
		widthSection.setWidth(200);
		heightSection.setWidth(200);
		vAlignSection.setWidth(200);
		styleSection.setWidth(200);
		backgroundSection.setWidth(200);

		// Sets layout num.

		nameSection.setLayoutNum(2);
		widthSection.setLayoutNum(2);
		heightSection.setLayoutNum(4);
		canShrinkSection.setLayoutNum(2);
		vAlignSection.setLayoutNum(2);
		styleSection.setLayoutNum(2);
		backgroundSection.setLayoutNum(4);

		// Sets fill grid num.

		nameSection.setGridPlaceholder(0, true);
		widthSection.setGridPlaceholder(0, true);
		heightSection.setGridPlaceholder(2, true);
		canShrinkSection.setGridPlaceholder(2, true);
		vAlignSection.setGridPlaceholder(0, true);
		styleSection.setGridPlaceholder(0, true);
		backgroundSection.setGridPlaceholder(2, true);

		addSection(PageSectionId.GRID_NAME, nameSection); // $NON-NLS-1$

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.GRID_ELEMENT_ID, elementIdSection);

		addSection(PageSectionId.GRID_SEPERATOR, seperatorSection); // $NON-NLS-1$
		addSection(PageSectionId.GRID_WIDTH, widthSection); // $NON-NLS-1$
		addSection(PageSectionId.GRID_HEIGHT, heightSection); // $NON-NLS-1$
		addSection(PageSectionId.GRID_VERTICAL_ALIGN, vAlignSection); // $NON-NLS-1
		addSection(PageSectionId.GRID_CAN_SHRINK, canShrinkSection); // $NON-NLS-1$

		Section seperatorSection1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.GRID_SEPERATOR1, seperatorSection1); // $NON-NLS-1$

		ComboPropertyDescriptorProvider fontFamilyProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.FONT_FAMILY_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontFamilyProvider.enableReset(true);
		ComboSection fontFamilySection = new ComboSection(fontFamilyProvider.getDisplayName(), container, true);
		fontFamilySection.setProvider(fontFamilyProvider);
		fontFamilySection.setLayoutNum(2);
		fontFamilySection.setWidth(200);
		addSection(PageSectionId.FONT_FAMILY, fontFamilySection);

		FontSizePropertyDescriptorProvider fontSizeProvider = new FontSizePropertyDescriptorProvider(
				StyleHandle.FONT_SIZE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontSizeProvider.enableReset(true);
		FontSizeSection fontSizeSection = new FontSizeSection(fontSizeProvider.getDisplayName(), container, true);
		fontSizeSection.setProvider(fontSizeProvider);
		fontSizeSection.setLayoutNum(4);
		fontSizeSection.setWidth(200);
		fontSizeSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.FONT_SIZE, fontSizeSection);

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider(StyleHandle.COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		colorProvider.enableReset(true);
		ColorSection colorSection = new ColorSection(colorProvider.getDisplayName(), container, true);
		colorSection.setProvider(colorProvider);
		colorSection.setLayoutNum(2);
		colorSection.setWidth(200);
		addSection(PageSectionId.FONT_COLOR, colorSection);

		addSection(PageSectionId.GRID_BACKGROUND_COLOR, backgroundSection); // $NON-NLS-1$

		IDescriptorProvider[] fontStyleProviders = createFontStyleProviders();

		FontStyleSection fontStyleSection = new FontStyleSection(container, true, false);
		fontStyleSection.setProviders(fontStyleProviders);
		fontStyleSection.setLayoutNum(3);
		addSection(PageSectionId.FONT_STYLE, fontStyleSection);

		ComboPropertyDescriptorProvider wordwrapProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.WHITE_SPACE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		wordwrapProvider.enableReset(true);
		RadioGroupSection wordwrapSection = new RadioGroupSection(wordwrapProvider.getDisplayName(), container, true);
		wordwrapSection.setProvider(wordwrapProvider);
		wordwrapSection.setLayoutNum(4);
		addSection(PageSectionId.WODR_WRAP, wordwrapSection); // $NON-NLS-1$

		SeperatorSection seperator1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.GRID_SEPERATOR2, seperator1);

		addSection(PageSectionId.GRID_STYLE, styleSection);

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		displayProvider.enableReset(true);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(200);
		addSection(PageSectionId.GRID_DISPLAY, displaySection);
	}

	private IDescriptorProvider[] createFontStyleProviders() {
		IDescriptorProvider[] providers = {

				new FontStylePropertyDescriptorProvider(StyleHandle.FONT_WEIGHT_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.FONT_STYLE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.TEXT_UNDERLINE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(StyleHandle.TEXT_LINE_THROUGH_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new PropertyDescriptorProvider(StyleHandle.TEXT_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT) };

		for (int i = 0; i < providers.length; i++) {
			if (providers[i] instanceof PropertyDescriptorProvider) {
				((PropertyDescriptorProvider) providers[i]).enableReset(true);
			}
		}

		return providers;
	}

	@Override
	protected void applyCustomSections() {
		Object[] helperProviders = ElementAdapterManager.getAdapters(this, ISectionHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				ISectionHelperProvider helperProvider = (ISectionHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					ISectionHelper helper = helperProvider.createHelper(this, PageConstants.THEME_HELPER_KEY);
					if (helper != null) {
						Section section = helper.createSection(container, GridHandle.THEME_PROP,
								ReportDesignConstants.GRID_ITEM, true);
						if (section instanceof SimpleComboSection) {
							((SimpleComboSection) section).setWidth(200);
						}
						section.setLayoutNum(6);
						section.setGridPlaceholder(4, true);
						addSectionAfter(PageSectionId.GRID_THEME, section, PageSectionId.GRID_DISPLAY);
					}
				}
			}
		}
	}
}
