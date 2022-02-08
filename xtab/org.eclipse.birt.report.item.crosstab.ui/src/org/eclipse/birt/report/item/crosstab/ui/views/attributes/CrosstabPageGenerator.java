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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.HighlightHandleProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.MapHandleProvider;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabBindingPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabHighlightDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabMapDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabSortingHandleProvider;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class CrosstabPageGenerator extends AbstractPageGenerator {

	public static final String ROWAREATITLE = Messages.getString("CrosstabPageGenerator.TabItem.RowArea"); //$NON-NLS-1$
	public static final String COLUMNAREATITLE = Messages.getString("CrosstabPageGenerator.TabItem.ColumnArea"); //$NON-NLS-1$

	protected CrosstabBindingPage bindingPage;
	protected BaseAttributePage rowAreaPage;
	protected BaseAttributePage columnAreaPage;
	protected FormPage filterPage;
	protected FormPage sortingPage;
	protected PreviewPage mapPage;
	protected PreviewPage highlightsPage;

	protected void buildItemContent(CTabItem item) {
		if (itemMap.containsKey(item) && itemMap.get(item) == null) {
			String title = tabFolder.getSelection().getText();
			if (title.equals(BINDINGTITLE)) {
				bindingPage = new CrosstabBindingPage();
				setPageInput(bindingPage);
				refresh(tabFolder, bindingPage, true);
				item.setControl(bindingPage.getControl());
				itemMap.put(item, bindingPage);
			} else if (title.equals(ROWAREATITLE)) {
				rowAreaPage = new BaseAttributePage();
				rowAreaPage.buildUI(item.getParent());
				rowAreaPage
						.setCategoryProvider(RowAreaCategoryProviderFactory.getInstance().getCategoryProvider(input));
				setPageInput(rowAreaPage);
				refresh(tabFolder, rowAreaPage, true);
				item.setControl(rowAreaPage.getControl());
				itemMap.put(item, rowAreaPage);
			} else if (title.equals(COLUMNAREATITLE)) {
				columnAreaPage = new BaseAttributePage();
				columnAreaPage.buildUI(item.getParent());
				columnAreaPage.setCategoryProvider(
						ColumnAreaCategoryProviderFactory.getInstance().getCategoryProvider(input));
				setPageInput(columnAreaPage);
				refresh(tabFolder, columnAreaPage, true);
				item.setControl(columnAreaPage.getControl());
				itemMap.put(item, columnAreaPage);
			} else if (title.equals(FILTERTITLE)) {
				filterPage = new FormPage(FormPropertyDescriptor.NO_UP_DOWN, new CrosstabFilterHandleProvider(), true,
						true);
				setPageInput(filterPage);
				refresh(tabFolder, filterPage, true);
				item.setControl(filterPage.getControl());
				itemMap.put(item, filterPage);
			} else if (title.equals(SORTINGTITLE)) {
				sortingPage = new FormPage(FormPropertyDescriptor.NO_UP_DOWN, new CrosstabSortingHandleProvider(), true,
						true);
				setPageInput(sortingPage);
				refresh(tabFolder, sortingPage, true);
				item.setControl(sortingPage.getControl());
				itemMap.put(item, sortingPage);
			} else if (title.equals(MAPTITLE)) {
				mapPage = new PreviewPage(true);
				mapPage.setPreview(new MapPropertyDescriptor(true));
				mapPage.setProvider(new CrosstabMapDescriptorProvider(MapHandleProvider.EXPRESSION_TYPE_DATA));
				setPageInput(mapPage);
				refresh(tabFolder, mapPage, true);
				item.setControl(mapPage.getControl());
				itemMap.put(item, mapPage);
			} else if (title.equals(HIGHLIGHTSTITLE)) {
				highlightsPage = new PreviewPage(true);
				highlightsPage.setPreview(new HighlightPropertyDescriptor(true));
				highlightsPage.setProvider(
						new CrosstabHighlightDescriptorProvider(HighlightHandleProvider.EXPRESSION_TYPE_DATA));
				setPageInput(highlightsPage);
				refresh(tabFolder, highlightsPage, true);
				item.setControl(highlightsPage.getControl());
				itemMap.put(item, highlightsPage);
			}
		} else if (itemMap.get(item) != null) {
			setPageInput(itemMap.get(item));
			refresh(tabFolder, itemMap.get(item), false);
		}
	}

	public void createTabItems(List input) {
		super.createTabItems(input);
		this.input = input;
		basicPage.setInput(input);
		addSelectionListener(this);
		basicPage.refresh();
		createTabItems();
		if (tabFolder.getSelection() != null)
			buildItemContent(tabFolder.getSelection());
	}

	protected void createTabItems() {
		createTabItem(BINDINGTITLE, ATTRIBUTESTITLE);
		createTabItem(ROWAREATITLE, BINDINGTITLE);
		createTabItem(COLUMNAREATITLE, ROWAREATITLE);
		createTabItem(MAPTITLE, COLUMNAREATITLE);
		createTabItem(HIGHLIGHTSTITLE, MAPTITLE);
		createTabItem(SORTINGTITLE, HIGHLIGHTSTITLE);
		createTabItem(FILTERTITLE, SORTINGTITLE);

	}

	public void createControl(Composite parent, Object input) {
		setCategoryProvider(CrosstabCategoryProviderFactory.getInstance().getCategoryProvider(input));
		super.createControl(parent, input);
	}

}
