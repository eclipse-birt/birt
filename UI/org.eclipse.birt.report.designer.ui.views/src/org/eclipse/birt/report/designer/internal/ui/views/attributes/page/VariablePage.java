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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ExpressionSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IVariableElementModel;

/**
 *
 */

public class VariablePage extends GeneralPage {

	protected void buildContent() {

		// Defines provider.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(IDesignElementModel.NAME_PROP,
				ReportDesignConstants.VARIABLE_ELEMENT);

		// Defines section.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		nameSection.setProvider(nameProvider);
		nameSection.setLayoutNum(6);
		nameSection.setWidth(500);
		addSection(PageSectionId.VARIABLE_NAME, nameSection); // $NON-NLS-1$

		ComboPropertyDescriptorProvider variableTypeProvider = new ComboPropertyDescriptorProvider(
				IVariableElementModel.TYPE_PROP, ReportDesignConstants.VARIABLE_ELEMENT);
		variableTypeProvider.enableReset(true);

		ComboSection variableTypeSection = new ComboSection(variableTypeProvider.getDisplayName(), container, true);
		variableTypeSection.setProvider(variableTypeProvider);
		variableTypeSection.setLayoutNum(6);
		variableTypeSection.setWidth(500);
		addSection(PageSectionId.VARIABLE_TYPE, variableTypeSection);

		ExpressionPropertyDescriptorProvider variableValueProvider = new ExpressionPropertyDescriptorProvider(
				IVariableElementModel.VALUE_PROP, ReportDesignConstants.VARIABLE_ELEMENT);
		ExpressionSection variableValueSection = new ExpressionSection(variableValueProvider.getDisplayName(),
				container, true);
		variableValueSection.setMulti(false);
		variableValueSection.setProvider(variableValueProvider);
		variableValueSection.setWidth(500);
		variableValueSection.setLayoutNum(6);
		addSection(PageSectionId.VARIABLE_VALUE, variableValueSection);

	}

	public boolean canReset() {
		return false;
	}
}
