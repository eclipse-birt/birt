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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ExpressionSection;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * Attribute page for data item expression property.
 */

public class ExpressionPage extends AttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(1, 15));

		// Defines provider.

		IDescriptorProvider expressionProvider = new ExpressionPropertyDescriptorProvider(
				TextDataHandle.VALUE_EXPR_PROP, ReportDesignConstants.TEXT_DATA_ITEM);

		// Defines section.

		ExpressionSection expressionSection = new ExpressionSection(expressionProvider.getDisplayName(), container,
				true);

		expressionSection.setProvider(expressionProvider);
		expressionSection.setWidth(500);

		// Adds section into this page.

		addSection(PageSectionId.EXPRESSION_VALUE_EXPR, expressionSection); // $NON-NLS-1$

		createSections();
		layoutSections();
	}
}
