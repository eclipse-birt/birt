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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
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
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Text element.
 */
public class TextPage extends GeneralPage {

	@Override
	protected void buildContent() {
		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(IDesignElementModel.NAME_PROP,
				ReportDesignConstants.TEXT_ITEM);

		IDescriptorProvider contentTypeProvider = new ComboPropertyDescriptorProvider(ITextItemModel.CONTENT_TYPE_PROP,
				ReportDesignConstants.TEXT_ITEM);

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(IStyledElementModel.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM);

		ComboPropertyDescriptorProvider fontFamilyProvider = new ComboPropertyDescriptorProvider(
				IStyleModel.FONT_FAMILY_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontFamilyProvider.enableReset(true);

		FontSizePropertyDescriptorProvider fontSizeProvider = new FontSizePropertyDescriptorProvider(
				IStyleModel.FONT_SIZE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		fontSizeProvider.enableReset(true);

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider(IStyleModel.COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		colorProvider.enableReset(true);

		ColorPropertyDescriptorProvider bgColorProvider = new ColorPropertyDescriptorProvider(
				IStyleModel.BACKGROUND_COLOR_PROP, ReportDesignConstants.STYLE_ELEMENT);
		bgColorProvider.enableReset(true);

		IDescriptorProvider[] fontStyleProviders = createFontStyleProviders();

		// Defines sections.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		Section seperator1Section = new SeperatorSection(container, SWT.HORIZONTAL);

		ComboSection contentTypeSection = new ComboSection(contentTypeProvider.getDisplayName(), container, true);

		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);

		ComboSection fontFamilySection = new ComboSection(fontFamilyProvider.getDisplayName(), container, true);

		FontSizeSection fontSizeSection = new FontSizeSection(fontSizeProvider.getDisplayName(), container, true);

		ColorSection colorSection = new ColorSection(colorProvider.getDisplayName(), container, true);

		ColorSection bgColorSection = new ColorSection(bgColorProvider.getDisplayName(), container, true);

		FontStyleSection fontStyleSection = new FontStyleSection(container, true, false);

		// Sets providers.

		nameSection.setProvider(nameProvider);
		contentTypeSection.setProvider(contentTypeProvider);
		styleSection.setProvider(styleProvider);
		fontFamilySection.setProvider(fontFamilyProvider);
		fontSizeSection.setProvider(fontSizeProvider);
		colorSection.setProvider(colorProvider);
		bgColorSection.setProvider(bgColorProvider);
		fontStyleSection.setProviders(fontStyleProviders);

		// Sets widths.

		nameSection.setWidth(200);
		contentTypeSection.setWidth(200);
		styleSection.setWidth(200);
		fontFamilySection.setWidth(200);
		fontSizeSection.setWidth(200);
		colorSection.setWidth(200);
		bgColorSection.setWidth(200);

		// Sets layout num.

		nameSection.setLayoutNum(2);
		contentTypeSection.setLayoutNum(6);
		styleSection.setLayoutNum(2);
		fontFamilySection.setLayoutNum(2);
		fontSizeSection.setLayoutNum(4);
		colorSection.setLayoutNum(2);
		bgColorSection.setLayoutNum(4);
		fontStyleSection.setLayoutNum(3);

		// Sets fill grid num.

		nameSection.setGridPlaceholder(0, true);
		contentTypeSection.setGridPlaceholder(4, true);
		styleSection.setGridPlaceholder(0, true);
		fontFamilySection.setGridPlaceholder(0, true);
		fontSizeSection.setGridPlaceholder(2, true);
		colorSection.setGridPlaceholder(0, true);
		bgColorSection.setGridPlaceholder(2, true);

		// Adds sections into container page.

		addSection(PageSectionId.TEXT_NAME, nameSection); // $NON-NLS-1$

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.TEXT_ELEMENT_ID, elementIdSection);

		ComboPropertyDescriptorProvider wordwrapProvider = new ComboPropertyDescriptorProvider(
				IStyleModel.WHITE_SPACE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		wordwrapProvider.enableReset(true);
		RadioGroupSection wordwrapSection = new RadioGroupSection(wordwrapProvider.getDisplayName(), container, true);
		wordwrapSection.setProvider(wordwrapProvider);
		wordwrapSection.setLayoutNum(4);

		addSection(PageSectionId.TEXT_SEPERATOR_1, seperator1Section); // $NON-NLS-1$
		addSection(PageSectionId.TEXT_CONTENT_TYPE, contentTypeSection); // $NON-NLS-1$
		addSection(PageSectionId.TEXT_FONT_FAMILY, fontFamilySection); // $NON-NLS-1$
		addSection(PageSectionId.TEXT_FONT_SIZE, fontSizeSection); // $NON-NLS-1$
		addSection(PageSectionId.TEXT_COLOR, colorSection); // $NON-NLS-1$
		addSection(PageSectionId.TEXT_BACKGROUND_COLOR, bgColorSection); // $NON-NLS-1$

		addSection(PageSectionId.TEXT_FONT_STYLE, fontStyleSection); // $NON-NLS-1$
		addSection(PageSectionId.WODR_WRAP, wordwrapSection); // $NON-NLS-1$

		SeperatorSection seperator1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.TEXT_SEPERATOR_3, seperator1);

		addSection(PageSectionId.TEXT_STYLE, styleSection);

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		displayProvider.enableReset(true);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(200);
		addSection(PageSectionId.TEXT_DISPLAY, displaySection);
	}

	/**
	 * Creates provider's array for font style controls.
	 *
	 * @return the provider's array(elements are instances of
	 *         <code>IDescriptorProvider</code>).
	 */
	private IDescriptorProvider[] createFontStyleProviders() {
		PropertyDescriptorProvider[] providers = {

				new FontStylePropertyDescriptorProvider(IStyleModel.FONT_WEIGHT_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(IStyleModel.FONT_STYLE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(IStyleModel.TEXT_UNDERLINE_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new FontStylePropertyDescriptorProvider(IStyleModel.TEXT_LINE_THROUGH_PROP,
						ReportDesignConstants.STYLE_ELEMENT),

				new PropertyDescriptorProvider(IStyleModel.TEXT_ALIGN_PROP, ReportDesignConstants.STYLE_ELEMENT)
				// bidi_hcg
		};

		for (int i = 0; i < providers.length; i++) {
			providers[i].enableReset(true);
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
						Section section = helper.createSection(container, IInternalReportItemModel.THEME_PROP,
								ReportDesignConstants.TEXT_ITEM, true);
						if (section instanceof SimpleComboSection) {
							((SimpleComboSection) section).setWidth(200);
						}
						section.setLayoutNum(6);
						section.setGridPlaceholder(4, true);
						addSectionAfter(PageSectionId.TEXT_THEME, section, PageSectionId.TEXT_DISPLAY);

					}
				}
			}
		}
	}
}
