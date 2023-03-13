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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.OutputPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.OutputSection;
import org.eclipse.swt.widgets.Composite;

/**
 * The visibility attribute page of DE element. Note: Visibility Not support
 * multi-selection.
 */
public class VisibilityPage extends AttributePage {
	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(1));

		OutputPropertyDescriptorProvider provider = new OutputPropertyDescriptorProvider();
		OutputSection output = new OutputSection(container, true);
		output.setProvider(provider);
		addSection(PageSectionId.VISIBILITY_OUTPUT, output);

		createSections();
		layoutSections();
	}

}
