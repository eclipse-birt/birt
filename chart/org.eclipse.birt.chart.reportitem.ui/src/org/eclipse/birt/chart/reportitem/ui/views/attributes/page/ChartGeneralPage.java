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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.page;

import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIFactory;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartUnitPropertyDescriptorProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChoicePropertyDescriptorProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.section.ChoiceSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageConstants;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
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
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.swt.SWT;

public class ChartGeneralPage extends GeneralPage {

	protected void buildContent() {
		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setLayoutNum(2);
		nameSection.setGridPlaceholder(0, true);
		nameSection.setWidth(200);
		addSection(ChartPageSectionId.CHART_NAME, nameSection);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(ChartPageSectionId.CHART_ELEMENT_ID, elementIdSection);

		SeperatorSection seperator1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(ChartPageSectionId.CHART_SEPERATOR_1, seperator1);

		IDescriptorProvider widthProvider = new ChartUnitPropertyDescriptorProvider(ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		ComplexUnitSection widthSection = new ComplexUnitSection(widthProvider.getDisplayName(), container, true);
		widthSection.setWidth(200);
		widthSection.setProvider(widthProvider);
		widthSection.setLayoutNum(2);
		addSection(ChartPageSectionId.CHART_WIDTH, widthSection);

		IDescriptorProvider heightProvider = new ChartUnitPropertyDescriptorProvider(ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		ComplexUnitSection heightSection = new ComplexUnitSection(heightProvider.getDisplayName(), container, true);
		heightSection.setProvider(heightProvider);
		heightSection.setWidth(200);
		heightSection.setLayoutNum(4);
		heightSection.setGridPlaceholder(2, true);
		addSection(ChartPageSectionId.CHART_HEIGHT, heightSection);

		SeperatorSection seperator2 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(ChartPageSectionId.CHART_SEPERATOR_2, seperator2);

		IDescriptorProvider styleProvider = new ChoicePropertyDescriptorProvider(ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		ChoiceSection styleSection = new ChoiceSection(styleProvider.getDisplayName(), container, true);
		styleSection.setProvider(styleProvider);
		styleSection.setWidth(200);
		styleSection.setLayoutNum(2);
		addSection(ChartPageSectionId.CHART_STYLE, styleSection);

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(200);
		addSection(ChartPageSectionId.CHART_DISPLAY, displaySection);
	}

	public boolean canReset() {
		return false;
	}

	protected void applyCustomSections() {
		Object[] helperProviders = ElementAdapterManager.getAdapters(this, ISectionHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				ISectionHelperProvider helperProvider = (ISectionHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					ISectionHelper helper = helperProvider.createHelper(this, PageConstants.THEME_HELPER_KEY);
					if (helper != null) {
						helper = ChartReportItemUIFactory.instance().updateChartPageSectionHelper(helper);
						Section section = helper.createSection(container, ISupportThemeElementConstants.THEME_PROP,
								ChartReportItemConstants.CHART_EXTENSION_NAME, true);
						if (section instanceof SimpleComboSection)
							((SimpleComboSection) section).setWidth(200);
						section.setLayoutNum(6);
						section.setGridPlaceholder(4, true);
						addSectionAfter(ChartPageSectionId.CHART_THEME, section, ChartPageSectionId.CHART_DISPLAY);
					}
				}
			}
		}
	}
}
