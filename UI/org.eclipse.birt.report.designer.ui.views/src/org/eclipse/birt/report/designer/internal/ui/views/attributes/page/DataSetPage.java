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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class DataSetPage extends GeneralPage {

	protected void buildContent() {
		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.DATA_SET_ELEMENT);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setGridPlaceholder(4, true);
		nameSection.setWidth(500);
		addSection(PageSectionId.DATASET_NAME, nameSection);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(500);
		elementIdSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.DATASET_ELEMENT_ID, elementIdSection);

		TextPropertyDescriptorProvider dataSetProvider = new TextPropertyDescriptorProvider(
				DataSetHandle.DATA_SOURCE_PROP, ReportDesignConstants.DATA_SET_ELEMENT) {

			public String getDisplayName() {
				return Messages.getString("DataSetPageGenerator.DataSourceName"); //$NON-NLS-1$
			}
		};
		TextSection dataSetSection = new TextSection(dataSetProvider.getDisplayName(), container, true);
		dataSetSection.setProvider(dataSetProvider);
		dataSetSection.setGridPlaceholder(4, true);
		dataSetSection.setWidth(500);
		addSection(PageSectionId.DATASET_DATASET, dataSetSection);

	}

	public boolean canReset() {
		return false;
	}
}
