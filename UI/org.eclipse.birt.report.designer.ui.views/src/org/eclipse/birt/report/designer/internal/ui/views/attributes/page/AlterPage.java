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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The Alter-Text attribute page of Image element.
 */
public class AlterPage extends AttributePage {

	/**
	 * @param parent A widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  The style of widget to construct
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.attributes.page.AttributePage#buildUI()
	 */
	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		// Defines provider.

		IDescriptorProvider provider = new TextPropertyDescriptorProvider(IImageItemModel.ALT_TEXT_PROP,
				ReportDesignConstants.IMAGE_ITEM);

		// Defines section.

		TextSection section = new TextSection(provider.getDisplayName(), container, true);

		section.setProvider(provider);
		section.setStyle(SWT.SINGLE);
		section.setWidth(500);

		addSection(PageSectionId.ALTER_ALT_TEXT, section); // $NON-NLS-1$

		IDescriptorProvider keyProvider = new TextPropertyDescriptorProvider(IImageItemModel.ALT_TEXT_KEY_PROP,
				ReportDesignConstants.IMAGE_ITEM);

		// Defines section.

		TextSection keySection = new TextSection(keyProvider.getDisplayName(), container, true);

		keySection.setProvider(keyProvider);
		keySection.setStyle(SWT.SINGLE);
		keySection.setWidth(500);

		addSection(PageSectionId.ALTER_ALT_TEXT_KEY, keySection); // $NON-NLS-1$

		createSections();
		layoutSections();
	}
}
