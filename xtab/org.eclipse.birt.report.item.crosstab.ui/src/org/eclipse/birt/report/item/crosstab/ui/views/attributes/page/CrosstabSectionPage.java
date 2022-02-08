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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ResetAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 * 
 */
public class CrosstabSectionPage extends ResetAttributePage {

	private SimpleComboSection masterSection;
	private SeperatorSection sepSection;
	private ComboSection beforeSection;
	private ComboSection insideSection;
	private ComboSection afterSection;

	// private PageLayoutComboSection pageLayoutComboSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		// Defines providers.

		ComboPropertyDescriptorProvider beforeProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_BEFORE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		beforeProvider.enableReset(true);

		IDescriptorProvider masterProvider = new SimpleComboPropertyDescriptorProvider(StyleHandle.MASTER_PAGE_PROP,
				ReportDesignConstants.STYLE_ELEMENT);

		ComboPropertyDescriptorProvider afterProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_AFTER_PROP, ReportDesignConstants.STYLE_ELEMENT);
		afterProvider.enableReset(true);

		ComboPropertyDescriptorProvider insideProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_INSIDE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		insideProvider.enableReset(true);

		// Defines sections.

		beforeSection = new ComboSection(beforeProvider.getDisplayName(), container, true);
		insideSection = new ComboSection(insideProvider.getDisplayName(), container, true);
		masterSection = new SimpleComboSection(masterProvider.getDisplayName(), container, true);
		afterSection = new ComboSection(afterProvider.getDisplayName(), container, true);
		sepSection = new SeperatorSection(container, SWT.HORIZONTAL);

		beforeSection.setProvider(beforeProvider);
		masterSection.setProvider(masterProvider);
		afterSection.setProvider(afterProvider);
		insideSection.setProvider(insideProvider);

		// Sets widths.

		beforeSection.setWidth(200);
		masterSection.setWidth(200);
		afterSection.setWidth(200);
		insideSection.setWidth(200);
		// repeatColumnHeaderSection.setWidth( 200 );

		// Sets layout num.

		beforeSection.setLayoutNum(2);
		afterSection.setLayoutNum(3);
		insideSection.setLayoutNum(5);
		masterSection.setLayoutNum(2);

		// Sets fill grid num.
		afterSection.setGridPlaceholder(1, true);
		insideSection.setGridPlaceholder(3, true);

		// Adds sections into container page.

		// PageLayoutPropertyDescriptorProvider pageLayoutProvider = new
		// PageLayoutPropertyDescriptorProvider(
		// ICrosstabReportItemConstants.PAGE_LAYOUT_PROP,
		// ReportDesignConstants.EXTENDED_ITEM );
		// pageLayoutComboSection = new PageLayoutComboSection(
		// pageLayoutProvider.getDisplayName( ),
		// container,
		// true );
		// pageLayoutComboSection.setProvider( pageLayoutProvider );
		// pageLayoutComboSection.setWidth( 200 );
		// pageLayoutComboSection.setGridPlaceholder( 3, true );

		addSection(PageSectionId.SECION_PAGE_BREAK_BEFORE, beforeSection);
		addSection(PageSectionId.SECION_PAGE_BREAK_AFTER, afterSection);
		addSection(PageSectionId.SECION_PAGE_BREAK_INSIDE, insideSection);
		addSection(PageSectionId.SECION_SEPERATOR, sepSection);
		addSection(PageSectionId.SECION_MASTER_PAGE, masterSection);
		// addSection( CrosstabPageSectionId.PAGE_LAYOUT, pageLayoutComboSection
		// );

		createSections();
		layoutSections();
	}

	public void refresh() {
		super.refresh();
		setVisible();
		container.layout(true);
		container.redraw();
	}

	protected void setVisible() {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof DesignElementHandle
				&& isElementInMasterPage((DesignElementHandle) DEUtil.getInputFirstElement(input))) {
			masterSection.setVisible(false);
			// sepSection.setVisible( false );
			beforeSection.getLabelControl().setEnabled(false);
			beforeSection.getComboControl().getControl().setEnabled(false);
			afterSection.getLabelControl().setEnabled(false);
			afterSection.getComboControl().getControl().setEnabled(false);
			insideSection.getLabelControl().setEnabled(false);
			insideSection.getComboControl().getControl().setEnabled(false);
		} else {
			masterSection.setVisible(true);
			sepSection.setVisible(true);
			beforeSection.getLabelControl().setEnabled(true);
			beforeSection.getComboControl().getControl().setEnabled(true);
			afterSection.getLabelControl().setEnabled(true);
			afterSection.getComboControl().getControl().setEnabled(true);
			insideSection.getLabelControl().setEnabled(true);
			insideSection.getComboControl().getControl().setEnabled(true);
		}
	}

	protected boolean isElementInMasterPage(DesignElementHandle elementHandle) {
		ModuleHandle root = elementHandle.getRoot();
		DesignElementHandle container = elementHandle;
		while (container != null && container != root) {
			if (container instanceof MasterPageHandle) {
				return true;
			}
			container = container.getContainer();
		}

		return false;
	}
}
