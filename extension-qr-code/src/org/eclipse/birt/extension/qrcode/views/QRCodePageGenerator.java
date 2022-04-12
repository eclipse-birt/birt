/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.extension.qrcode.views;

import java.util.List;

import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;

/**
 * QRCodePageGenerator
 */
public class QRCodePageGenerator extends AbstractPageGenerator {

	private IPropertyTabUI generalPage;

	protected void buildItemContent(CTabItem item) {
		if (itemMap.get(item) != null) {
			setPageInput(itemMap.get(item));
			refresh(tabFolder, itemMap.get(item), false);
		}
	}

	@Override
	public void refresh() {
		createTabItems(input);

		generalPage.setInput(input);
		addSelectionListener(this);
		((TabPage) generalPage).refresh();

	}

	@Override
	public void createTabItems(List input) {
		if (generalPage == null || generalPage.getControl().isDisposed()) {
			tabFolder.setLayout(new FillLayout());
			generalPage = AttributesUtil.buildGeneralPage(tabFolder,
					new String[] { null, AttributesUtil.BORDER, AttributesUtil.MARGIN, AttributesUtil.SECTION,
							AttributesUtil.VISIBILITY, AttributesUtil.TOC, AttributesUtil.BOOKMARK,
							AttributesUtil.USERPROPERTIES, AttributesUtil.NAMEDEXPRESSIONS,
							AttributesUtil.ADVANCEPROPERTY },
					new String[] { "General" },
					new String[] { "General"
					}, new AttributesUtil.PageWrapper[] { new QRCodeGeneralPage() }, input);

			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
			tabItem.setText(ATTRIBUTESTITLE);
			tabItem.setControl(generalPage.getControl());
		}

		this.input = input;
		generalPage.setInput(input);
		addSelectionListener(this);
		((TabPage) generalPage).refresh();

		if (tabFolder.getSelection() != null) {
			buildItemContent(tabFolder.getSelection());
		}
	}

}
