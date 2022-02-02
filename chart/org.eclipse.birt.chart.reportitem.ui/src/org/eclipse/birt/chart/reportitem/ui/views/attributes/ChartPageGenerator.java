/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartBindingPage;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartFilterProviderDelegate;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FilterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * ChartReportItemPropertyEditUIImpl
 */
public class ChartPageGenerator extends AbstractPageGenerator {

	protected PreviewPage highlightsPage;
	protected FormPage filterPage;
	protected BindingPage bindingPage;
	protected FormPage sortingPage;

	protected void buildItemContent(CTabItem item) {
		if (itemMap.containsKey(item) && itemMap.get(item) == null) {
			String title = tabFolder.getSelection().getText();
			if (title.equals(BINDINGTITLE)) {
				bindingPage = new ChartBindingPage();
				setPageInput(bindingPage);

				refresh(tabFolder, bindingPage, true);
				item.setControl(bindingPage.getControl());
				itemMap.put(item, bindingPage);
			} else if (title.equals(FILTERTITLE)) {
				setFilterPage(item);
			}
		} else if (itemMap.get(item) != null) {
			// Since the contents of filters between different item handle are
			// different, so here still need to create new filter page.
			String title = tabFolder.getSelection().getText();
			if (title.equals(FILTERTITLE)) {
				setFilterPage(item);
			} else {
				setPageInput(itemMap.get(item));
				refresh(tabFolder, itemMap.get(item), false);
			}
		}
	}

	/**
	 * @param item
	 * @since 2.3
	 */
	private void setFilterPage(CTabItem item) {
		AbstractFilterHandleProvider providerDelegate = getFilterProviderDelegate();

		filterPage = new FilterPage(FormPropertyDescriptor.FULL_FUNCTION, providerDelegate, true, true);
		setPageInput(filterPage);
		refresh(tabFolder, filterPage, true);
		item.setControl(filterPage.getControl());
		itemMap.put(item, filterPage);
	}

	/**
	 * Returns a delegate of filter handle provider, it wraps actual filter provider
	 * for chart consuming data set, cube set, including sharing query, grand total
	 * view cases.
	 * 
	 * @return
	 */
	private AbstractFilterHandleProvider getFilterProviderDelegate() {
		AbstractFilterHandleProvider provider = ChartFilterProviderDelegate.createFilterProvider(input, null);
		return provider;
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
		createTabItem(FILTERTITLE, BINDINGTITLE);
	}

	public void createControl(Composite parent, Object input) {
		setCategoryProvider(ChartCategoryProviderFactory.getInstance().getCategoryProvider(input));
		super.createControl(parent, input);
	}
}
