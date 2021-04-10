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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The Header/Footer attribute page of SimpleMasterPage element.
 */
public class HeaderFooterPage extends AttributePage {
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		PropertyDescriptorProvider headerProvider = new PropertyDescriptorProvider(
				SimpleMasterPageHandle.SHOW_HEADER_ON_FIRST_PROP, ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT);
		CheckSection headerSection = new CheckSection(container, true);
		headerSection.setProvider(headerProvider);
		headerSection.setLayoutNum(1);
		addSection("HEADERSECTION", headerSection); //$NON-NLS-1$

		PropertyDescriptorProvider footerProvider = new PropertyDescriptorProvider(
				SimpleMasterPageHandle.SHOW_FOOTER_ON_LAST_PROP, ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT);
		CheckSection footerSection = new CheckSection(container, true);
		footerSection.setProvider(footerProvider);
		footerSection.setLayoutNum(1);
		addSection("FOOTERSECTION", footerSection); //$NON-NLS-1$

		createSections();
		layoutSections();

	}
}