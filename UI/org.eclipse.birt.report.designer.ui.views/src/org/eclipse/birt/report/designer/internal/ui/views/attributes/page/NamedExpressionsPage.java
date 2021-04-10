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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.NamedExpressionsHandleProvier;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 * Named expression page
 */

public class NamedExpressionsPage extends LibraryAttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		needCheckLibraryReadOnly(true);
		container.setLayout(WidgetUtil.createGridLayout(1));
		NamedExpressionsHandleProvier namedExpressionProvider = new NamedExpressionsHandleProvier();
		FormSection namedExpressionSection = new FormSection(namedExpressionProvider.getDisplayName(), container, true);
		namedExpressionSection.setProvider(namedExpressionProvider);
		namedExpressionSection.setButtonWithDialog(true);
		namedExpressionSection.setStyle(FormPropertyDescriptor.SIMPLE_FUNCTION);
		namedExpressionSection.setFillForm(true);
		namedExpressionSection.setHeight(200);
		addSection(PageSectionId.NAMED_EXPRESSIONS, namedExpressionSection);
		createSections();
		layoutSections();
	}

}
