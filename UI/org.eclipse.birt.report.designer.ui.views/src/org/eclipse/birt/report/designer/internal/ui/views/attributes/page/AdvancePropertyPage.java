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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AdvancePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.AdvancePropertySection;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 *
 */
public class AdvancePropertyPage extends AttributePage {

	private AdvancePropertyDescriptorProvider provider;
	private AdvancePropertySection propertySection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));
		provider = new AdvancePropertyDescriptorProvider();

		propertySection = new AdvancePropertySection(provider.getDisplayName(), container, true, false);
		propertySection.setHeight(200);
		propertySection.setWidth(200);
		propertySection.setProvider(provider);
		propertySection.setFillControl(true);
		addSection(PageSectionId.ADVANCE_PROPERTY, propertySection);

		createSections();
		layoutSections();

	}

	public Object getAdapter(Class adapter) {
		if (adapter == IAction.class) {
			return provider.getActions(propertySection.getControl());
		}
		return null;
	}

}
