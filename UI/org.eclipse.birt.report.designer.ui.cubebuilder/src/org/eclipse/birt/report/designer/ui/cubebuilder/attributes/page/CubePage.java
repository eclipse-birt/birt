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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.ui.cubebuilder.attributes.provider.PrimaryDatasetDescriptorProvider;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class CubePage extends GeneralPage {

	protected void buildContent() {

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.CUBE_ELEMENT);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setGridPlaceholder(4, true);
		nameSection.setWidth(500);
		addSection(CubePageSectionId.CUBE_NAME, nameSection);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(500);
		elementIdSection.setGridPlaceholder(4, true);
		addSection(CubePageSectionId.CUBE_ELEMENT_ID, elementIdSection);

		PrimaryDatasetDescriptorProvider dataSetProvider = new PrimaryDatasetDescriptorProvider();
		TextSection dataSetSection = new TextSection(dataSetProvider.getDisplayName(), container, true);
		dataSetSection.setProvider(dataSetProvider);
		dataSetSection.setGridPlaceholder(4, true);
		dataSetSection.setWidth(500);
		addSection(CubePageSectionId.CUBE_PRIMARY_DATASET, dataSetSection);

	}

	public boolean canReset() {
		return false;
	}

}
