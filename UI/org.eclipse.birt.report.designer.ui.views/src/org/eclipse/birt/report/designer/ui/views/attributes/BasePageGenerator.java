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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AggregateOnBindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SortingHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.swt.custom.CTabItem;

/**
 * This page generator creates the default tab content for some built-in tab
 * keys. The caller MUST ensure relevant proeprties are already available in the
 * Model. Otherwise, it should directly extend from
 * <code>AbstractPageGenerator</code> instead of this class.
 *
 * @since 2.5
 */
abstract public class BasePageGenerator extends AbstractPageGenerator {

	/**
	 * Creats the page content for each tab. Note this doesn't include the default
	 * cateogry-styled tab, which is handled separately.
	 *
	 * @param tabKey
	 */
	protected TabPage buildTabContent(String tabKey) {
		TabPage page = null;

		if (tabKey.equals(BINDINGTITLE)) {
			if (isSupportAggregationBinding()) {
				page = new AggregateOnBindingPage();
			} else {
				page = new BindingPage();
			}
		} else if (tabKey.equals(SORTINGTITLE)) {
			page = new FormPage(FormPropertyDescriptor.FULL_FUNCTION, new SortingHandleProvider(), true, true);
		} else if (tabKey.equals(FILTERTITLE)) {
			AbstractFilterHandleProvider filterProvider = (AbstractFilterHandleProvider) ElementAdapterManager
					.getAdapter(this, AbstractFilterHandleProvider.class);

			if (filterProvider == null) {
				filterProvider = new FilterHandleProvider();
			}

			page = new FormPage(FormPropertyDescriptor.FULL_FUNCTION, filterProvider, true, true);
		} else if (tabKey.equals(HIGHLIGHTSTITLE)) {
			page = new PreviewPage(true);
			((PreviewPage) page).setPreview(new HighlightPropertyDescriptor(true));
			((PreviewPage) page).setProvider(new HighlightDescriptorProvider());
		} else if (tabKey.equals(MAPTITLE)) {
			page = new PreviewPage(true);
			((PreviewPage) page).setPreview(new MapPropertyDescriptor(true));
			((PreviewPage) page).setProvider(new MapDescriptorProvider());
		}

		return page;
	}

	/**
	 * Builds and initialize the content for each tab.
	 *
	 * @param item
	 */
	protected void buildItemContent(CTabItem item) {
		if (itemMap.containsKey(item) && itemMap.get(item) == null) {
			String title = tabFolder.getSelection().getText();

			TabPage page = buildTabContent(title);

			if (page != null) {
				setPageInput(page);
				refresh(tabFolder, page, true);
				item.setControl(page.getControl());
				itemMap.put(item, page);
			}
		} else if (itemMap.get(item) != null) {
			setPageInput(itemMap.get(item));
			refresh(tabFolder, itemMap.get(item), false);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.views.attributes.CategoryPageGenerator
	 * #createTabItems(java.util.List)
	 */
	@Override
	public void createTabItems(List input) {
		super.createTabItems(input);
		this.input = input;
		addSelectionListener(this);
		createTabItems();
		if (tabFolder.getSelection() != null) {
			buildItemContent(tabFolder.getSelection());
		}
	}

	/**
	 * @return Returns if the binding page should support aggregation when requested
	 *         for creation
	 */
	protected boolean isSupportAggregationBinding() {
		return false;
	}

	/**
	 * The subclass should overwrite this to add additional tabs.
	 */
	abstract protected void createTabItems();
}
