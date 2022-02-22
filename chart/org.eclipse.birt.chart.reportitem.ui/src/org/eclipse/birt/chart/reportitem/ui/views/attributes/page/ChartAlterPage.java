/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

public class ChartAlterPage extends AlterPage {

	@Override
	protected void applyCustomSections() {
		IDescriptorProvider provider = new TextPropertyDescriptorProvider(ExtendedItemHandle.ALT_TEXT_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		((TextSection) getSection(PageSectionId.ALTER_ALT_TEXT)).setProvider(provider);

		IDescriptorProvider keyProvider = new TextPropertyDescriptorProvider(ExtendedItemHandle.ALT_TEXT_KEY_PROP,
				ReportDesignConstants.EXTENDED_ITEM);
		((TextSection) getSection(PageSectionId.ALTER_ALT_TEXT_KEY)).setProvider(keyProvider);
	}
}
