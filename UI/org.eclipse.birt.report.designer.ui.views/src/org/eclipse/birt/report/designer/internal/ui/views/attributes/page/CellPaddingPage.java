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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The Cell Padding attribute page of DE element.
 */
public class CellPaddingPage extends ResetAttributePage {

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		String[] padProperties = { StyleHandle.PADDING_TOP_PROP, StyleHandle.PADDING_BOTTOM_PROP,
				StyleHandle.PADDING_LEFT_PROP, StyleHandle.PADDING_RIGHT_PROP };

		String[] sectionKeys = { PageSectionId.CELLPADDING_TOP, // $NON-NLS-1$
				PageSectionId.CELLPADDING_BOTTOM, // $NON-NLS-1$
				PageSectionId.CELLPADDING_LEFT, // $NON-NLS-1$
				PageSectionId.CELLPADDING_RIGHT
				// $NON-NLS-1$
		};

		for (int i = 0; i < padProperties.length; i++) {
			UnitPropertyDescriptorProvider provider = new UnitPropertyDescriptorProvider(padProperties[i],
					ReportDesignConstants.STYLE_ELEMENT);
			provider.enableReset(true);

			ComplexUnitSection section = new ComplexUnitSection(provider.getDisplayName(), container, true);

			section.setProvider(provider);
			section.setWidth(200);
			// section.setLayoutNum( 5 );
			section.setGridPlaceholder(3, true);

			addSection(sectionKeys[i], section);
		}
		createSections();
		layoutSections();
	}
}
