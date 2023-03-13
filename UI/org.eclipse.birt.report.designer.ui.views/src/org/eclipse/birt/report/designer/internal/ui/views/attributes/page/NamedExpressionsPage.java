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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.NamedExpressionsHandleProvier;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 * Named expression page
 */

public class NamedExpressionsPage extends LibraryAttributePage {

	@Override
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
