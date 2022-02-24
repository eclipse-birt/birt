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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MarignPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.MarignSection;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Zhipeng Zhang
 */
public class ItemMarginPage extends ResetAttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		String[] padProperties = { StyleHandle.MARGIN_TOP_PROP, StyleHandle.MARGIN_BOTTOM_PROP,
				StyleHandle.MARGIN_LEFT_PROP, StyleHandle.MARGIN_RIGHT_PROP };
		String[] padIDs = { PageSectionId.ITEM_MARGIN_TOP, PageSectionId.ITEM_MARGIN_BOTTOM,
				PageSectionId.ITEM_MARGIN_LEFT, PageSectionId.ITEM_MARGIN_RIGHT };

		for (int i = 0; i < padProperties.length; i++) {
			MarignPropertyDescriptorProvider provider = new MarignPropertyDescriptorProvider(padProperties[i],
					ReportDesignConstants.STYLE_ELEMENT);
			provider.enableReset(true);
			MarignSection marginSection = new MarignSection(provider.getDisplayName(), container, true);
			marginSection.setProvider(provider);
			marginSection.setGridPlaceholder(3, true);
			// marginSection.setWidth( 400 );
			addSection(padIDs[i], marginSection);
		}
		createSections();
		layoutSections();
	}

}
