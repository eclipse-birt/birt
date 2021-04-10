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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.HighlightHandleProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.MapHandleProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabHighlightDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabMapDescriptorProvider;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class CrosstabCellPageGenerator extends AbstractPageGenerator {

	protected PreviewPage mapPage;
	protected PreviewPage highlightsPage;

	public void createControl(Composite parent, Object input) {
		setCategoryProvider(CrosstabCellCategoryProviderFactory.getInstance().getCategoryProvider(input));
		super.createControl(parent, input);
	}

	protected void buildItemContent(CTabItem item) {
		if (itemMap.containsKey(item) && itemMap.get(item) == null) {
			String title = tabFolder.getSelection().getText();
			if (title.equals(MAPTITLE)) {
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
		createTabItem(MAPTITLE, ATTRIBUTESTITLE);
		createTabItem(HIGHLIGHTSTITLE, MAPTITLE);
	}
}
