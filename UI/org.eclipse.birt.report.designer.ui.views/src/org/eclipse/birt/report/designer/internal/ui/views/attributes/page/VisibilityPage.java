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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.OutputPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.OutputSection;
import org.eclipse.swt.widgets.Composite;

/**
 * The visibility attribute page of DE element. Note: Visibility Not support
 * multi-selection.
 */
public class VisibilityPage extends AttributePage {
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