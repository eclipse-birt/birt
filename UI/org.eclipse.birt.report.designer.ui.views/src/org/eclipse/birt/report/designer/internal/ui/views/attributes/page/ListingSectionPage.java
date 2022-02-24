/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.RepeatHeaderDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The section attribute page of DE Listing element.
 */
public class ListingSectionPage extends ResetAttributePage {

	private CheckSection repeatHeaderSection;

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		ComboPropertyDescriptorProvider breakBeforeProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_BEFORE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		breakBeforeProvider.enableReset(true);
		ComboSection breakBeforeSection = new ComboSection(breakBeforeProvider.getDisplayName(), container, true);
		breakBeforeSection.setProvider(breakBeforeProvider);
		breakBeforeSection.setLayoutNum(2);
		breakBeforeSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_BREAK_BEFORE, breakBeforeSection);

		ComboPropertyDescriptorProvider breakAfterProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_AFTER_PROP, ReportDesignConstants.STYLE_ELEMENT);
		breakAfterProvider.enableReset(true);
		ComboSection breakAfterSection = new ComboSection(breakAfterProvider.getDisplayName(), container, true);
		breakAfterSection.setProvider(breakAfterProvider);
		breakAfterSection.setLayoutNum(4);
		breakAfterSection.setGridPlaceholder(2, true);
		breakAfterSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_BREAK_AFTER, breakAfterSection);

		ComboPropertyDescriptorProvider breakInsideProvider = new ComboPropertyDescriptorProvider(
				StyleHandle.PAGE_BREAK_INSIDE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		breakInsideProvider.enableReset(true);
		ComboSection breakInsideSection = new ComboSection(breakInsideProvider.getDisplayName(), container, true);
		breakInsideSection.setProvider(breakInsideProvider);
		breakInsideSection.setLayoutNum(2);
		breakInsideSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_BREAK_INSIDE, breakInsideSection);

		TextPropertyDescriptorProvider internalProvider = new TextPropertyDescriptorProvider(
				ListingHandle.PAGE_BREAK_INTERVAL_PROP, ReportDesignConstants.LISTING_ITEM);
		internalProvider.enableReset(true);
		TextSection intervalSection = new TextSection(internalProvider.getDisplayName(), container, true);
		intervalSection.setProvider(internalProvider);
		intervalSection.setLayoutNum(4);
		intervalSection.setGridPlaceholder(2, true);
		intervalSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_INTERVAL, intervalSection);

		SeperatorSection seperator = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.LISTING_SECTION_SEPERATOR, seperator);

		SimpleComboPropertyDescriptorProvider masterPageProvider = new SimpleComboPropertyDescriptorProvider(
				StyleHandle.MASTER_PAGE_PROP, ReportDesignConstants.STYLE_ELEMENT);
		SimpleComboSection masterPageSection = new SimpleComboSection(masterPageProvider.getDisplayName(), container,
				true);
		masterPageSection.setProvider(masterPageProvider);
		masterPageSection.setLayoutNum(2);
		masterPageSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_MASTER_PAGE, masterPageSection);
		/*
		 *
		 * String[] properties = { StyleHandle.PAGE_BREAK_BEFORE_PROP,
		 * StyleHandle.PAGE_BREAK_AFTER_PROP, // StyleHandle.PAGE_BREAK_INSIDE_PROP, };
		 * for ( int i = 0; i < properties.length; i++ ) {
		 *
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.STYLE_ELEMENT, properties[i], 1, 200 );
		 * if(properties[i].equals( StyleHandle.PAGE_BREAK_BEFORE_PROP )) {
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.STYLE_ELEMENT, StyleHandle.MASTER_PAGE_PROP, 1, 200 );
		 * WidgetUtil.createGridPlaceholder( container, 1, true ); }else {
		 * WidgetUtil.createGridPlaceholder( container, 3, true ); } }
		 */
		/*
		 * WidgetUtil.createHorizontalLine( container, 5 ); repeatHeaderButton = new
		 * Button( container, SWT.CHECK ); GridData gd = new GridData();
		 * gd.horizontalSpan = 2; repeatHeaderButton.setLayoutData(gd);
		 * repeatHeaderButton.setText(
		 * Messages.getString("ListingSectionPage.RepeatHeader") ); //$NON-NLS-1$
		 * repeatHeaderButton.addSelectionListener( new SelectionListener(){
		 *
		 * public void widgetSelected( SelectionEvent e ) {
		 * setRepeatHeader(repeatHeaderButton.getSelection( )); }
		 *
		 * public void widgetDefaultSelected( SelectionEvent e ) { // TODO
		 * Auto-generated method stub }} );
		 */
		// Page break interval has been removed by Model, so the following code
		// should also be removed.
		// WidgetUtil.createHorizontalLine( container, 5 );
		//
		// WidgetUtil.buildGridControl( container,
		// propertiesMap,
		// ReportDesignConstants.LISTING_ITEM,
		// ListingHandle.PAGE_BREAK_INTERVAL_PROP,
		// 1,
		// true );
		RepeatHeaderDescriptorProvider repeatHeaderProvider = new RepeatHeaderDescriptorProvider();
		repeatHeaderSection = new CheckSection(container, true) {

			@Override
			public void load() {
				super.load();

				checkRepeatHeaderStatus();
			}
		};
		repeatHeaderSection.setProvider(repeatHeaderProvider);
		repeatHeaderSection.setLayoutNum(4);
		repeatHeaderSection.setGridPlaceholder(2, true);
		repeatHeaderSection.setWidth(200);
		addSection(PageSectionId.LISTING_SECTION_REPEAT_HEADER, repeatHeaderSection);

		createSections();
		layoutSections();

	}

	protected void setRepeatHeader(boolean b) {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof ListingHandle) {
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement(input);
			try {
				listingHandle.setRepeatHeader(b);
			} catch (SemanticException e) {
			}
		}
	}

	@Override
	public void postElementEvent() {
		super.postElementEvent();

		checkRepeatHeaderStatus();
	}

	private void checkRepeatHeaderStatus() {
		if (repeatHeaderSection != null && repeatHeaderSection.getCheckControl() != null
				&& repeatHeaderSection.getCheckControl().getControl() != null
				&& !repeatHeaderSection.getCheckControl().getControl().isDisposed() && DEUtil.getInputSize(input) == 1
				&& DEUtil.getInputFirstElement(input) instanceof ListingHandle) {
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement(input);

			if (!Boolean.valueOf(listingHandle.cascadeACL()).equals(
					listingHandle.getPropertyHandle(IReportItemModel.CASCADE_ACL_PROP).getPropertyDefn().getDefault())
					|| listingHandle.getPropertyHandle(IReportDesignModel.ACL_EXPRESSION_PROP).isLocal()) {
				repeatHeaderSection.getCheckControl().getControl().setEnabled(false);
			} else {
				repeatHeaderSection.getCheckControl().getControl().setEnabled(true);
			}
		}
	}
}
