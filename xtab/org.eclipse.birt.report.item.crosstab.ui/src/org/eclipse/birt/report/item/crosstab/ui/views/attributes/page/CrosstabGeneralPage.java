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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageConstants;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.HideMeasureHeaderProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.MeasureComboPropertyDescriptorProvider;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.swt.SWT;

public class CrosstabGeneralPage extends GeneralPage {

	IDescriptorProvider grandTotalColumnProvider, grandTotalRowProvider;
	IDescriptorProvider layoutMeasuresProvider;
	private ComboSection layoutMeasureSection;

	@Override
	protected void buildContent() {

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setLayoutNum(2);
		nameSection.setWidth(200);
		addSection(CrosstabPageSectionId.CROSSTAB_NAME, nameSection);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(200);
		elementIdSection.setLayoutNum(4);
		elementIdSection.setGridPlaceholder(2, true);
		addSection(CrosstabPageSectionId.CROSSTAB_ELEMENT_ID, elementIdSection);

		Section seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(CrosstabPageSectionId.CROSSTAB_SEPERATOR_1, seperatorSection);

		layoutMeasuresProvider = new MeasureComboPropertyDescriptorProvider(
				ICrosstabReportItemConstants.MEASURE_DIRECTION_PROP, ICrosstabConstants.CROSSTAB_EXTENSION_NAME);
		layoutMeasureSection = new ComboSection(Messages.getString("LayoutMeasuresSection.DisplayName"), //$NON-NLS-1$
				container, true);
		layoutMeasureSection.setProvider(layoutMeasuresProvider);
		layoutMeasureSection.setWidth(200);
		layoutMeasureSection.setLayoutNum(2);
		addSection(CrosstabPageSectionId.LAYOUT_MEASURES, layoutMeasureSection);

		HideMeasureHeaderProvider hideMeasureProvider = new HideMeasureHeaderProvider(
				ICrosstabReportItemConstants.HIDE_MEASURE_HEADER_PROP, ReportDesignConstants.EXTENDED_ITEM);
		CheckSection hideMeasureSection = new CheckSection(container, true);
		hideMeasureSection.setProvider(hideMeasureProvider);
		hideMeasureSection.setLayoutNum(4);
		hideMeasureSection.setGridPlaceholder(2, true);
		addSection(CrosstabPageSectionId.HIDE_MEASURE_HEADER, hideMeasureSection);

		SeperatorSection seperator1 = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(CrosstabPageSectionId.CROSSTAB_SEPERATOR_2, seperator1);

		SimpleComboPropertyDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider(
				ReportItemHandle.STYLE_PROP, ReportDesignConstants.REPORT_ITEM);
		SimpleComboSection styleSection = new SimpleComboSection(styleProvider.getDisplayName(), container, true);
		styleSection.setProvider(styleProvider);
		styleSection.setLayoutNum(2);
		styleSection.setWidth(200);
		addSection(CrosstabPageSectionId.CROSSTAB_STYLE, styleSection);

		ComboPropertyDescriptorProvider displayProvider = new ComboPropertyDescriptorProvider(IStyleModel.DISPLAY_PROP,
				ReportDesignConstants.STYLE_ELEMENT);
		ComboSection displaySection = new ComboSection(displayProvider.getDisplayName(), container, true);
		displaySection.setProvider(displayProvider);
		displaySection.setLayoutNum(4);
		displaySection.setGridPlaceholder(2, true);
		displaySection.setWidth(200);
		addSection(CrosstabPageSectionId.CROSSTAB_DISPLAY, displaySection);

	}

	@Override
	public boolean canReset() {
		return false;
	}

	@Override
	public void refresh() {
		super.refresh();
		checkLayoutProperty();
	}

	private void checkLayoutProperty() {
		if (input instanceof List && DEUtil.getMultiSelectionHandle((List) input).isExtendedElements()) {
			if (checkControl()) {
				layoutMeasureSection.getComboControl().getControl().setEnabled(false);
			}
		} else if (checkControl()) {
			layoutMeasureSection.getComboControl().getControl().setEnabled(true);
		}
	}

	private boolean checkControl() {
		if (layoutMeasureSection != null && layoutMeasureSection.getComboControl() != null
				&& layoutMeasureSection.getComboControl().getControl() != null
				&& !layoutMeasureSection.getComboControl().getControl().isDisposed()) {
			return true;
		}
		return false;
	}

	@Override
	public void postElementEvent() {
		super.postElementEvent();
		checkLayoutProperty();
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
						Section section = helper.createSection(container, ISupportThemeElementConstants.THEME_PROP,
								ICrosstabConstants.CROSSTAB_EXTENSION_NAME, true);
						if (section instanceof SimpleComboSection) {
							((SimpleComboSection) section).setWidth(200);
						}
						section.setLayoutNum(6);
						section.setGridPlaceholder(4, true);
						addSectionAfter(CrosstabPageSectionId.CROSSTAB_THEME, section,
								CrosstabPageSectionId.CROSSTAB_DISPLAY);
					}
				}
			}
		}
	}
}
