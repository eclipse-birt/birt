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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Table element.
 */
public class TablePage extends GeneralPage {

	protected void buildContent() {
		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.TABLE_ITEM);

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.TABLE_ITEM);

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider(ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.TABLE_ITEM);

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
		vAlignSection.setLayoutNum(2);
		styleSection.setLayoutNum(2);
		backgroundSection.setLayoutNum(4);
		canShrinkSection.setLayoutNum(4);

		// Sets fill grid num.

		nameSection.setGridPlaceholder(0, true);
		widthSection.setGridPlaceholder(0, true);
		heightSection.setGridPlaceholder(2, true);
		vAlignSection.setGridPlaceholder(0, true);
		styleSection.setGridPlaceholder(0, true);
		backgroundSection.setGridPlaceholder(2, true);
		canShrinkSection.setGridPlaceholder(2, true);

		// Adds sections into container page.

		addSection(PageSectionId.TABLE_NAME, nameSection); // $NON-NLS-1$

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.TABEL_ELEMENT_ID, elementIdSection);

		addSection(PageSectionId.TABLE_SEPERATOR, seperatorSection); // $NON-NLS-1$
		addSection(PageSectionId.TABLE_WIDTH, widthSection); // $NON-NLS-1$
		addSection(PageSectionId.TABLE_HEIGHT, heightSection); // $NON-NLS-1$
		addSection(PageSectionId.TABLE_VERTICAL_ALIGN, vAlignSection); // $NON-NLS-1$
		addSection(PageSectionId.TABLE_BACKGROUND_COLOR, backgroundSection); // $NON-NLS-1$
		addSection(PageSectionId.TABLE_STYLE, styleSection); // $NON-NLS-1$

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		displayProvider.enableReset(true);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(200);
		addSection(PageSectionId.TABLE_DISPLAY, displaySection);

		addSection(PageSectionId.TABLE_CAN_SHRINK, canShrinkSection); // $NON-NLS-1$

		Section seperatorSection1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.TABLE_SEPERATOR1, seperatorSection1); // $NON-NLS-1$

		addFontsSection();

	}

	protected void applyCustomSections() {
		Object[] helperProviders = ElementAdapterManager.getAdapters(this, ISectionHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				ISectionHelperProvider helperProvider = (ISectionHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					ISectionHelper helper = helperProvider.createHelper(this, PageConstants.THEME_HELPER_KEY);
					if (helper != null) {
						Section section = helper.createSection(container, TableHandle.THEME_PROP,
								ReportDesignConstants.TABLE_ITEM, true);
						if (section instanceof SimpleComboSection)
							((SimpleComboSection) section).setWidth(200);
						section.setLayoutNum(2);
						addSectionBefore(PageSectionId.TABLE_THEME, section, PageSectionId.TABLE_CAN_SHRINK);
					}
				}
			}
		}
	}
}
