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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The comments attribute page of Report element.
 */
public class CommentsPage extends AttributePage {

	private TextSection commentSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		TextPropertyDescriptorProvider commentProvider = new TextPropertyDescriptorProvider(
				ReportDesignHandle.COMMENTS_PROP, ReportDesignConstants.REPORT_ITEM);
		commentSection = new TextSection(commentProvider.getDisplayName(), container, true);
		commentSection.setStyle(SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		commentSection.setProvider(commentProvider);

		commentSection.setWidth(500);
		commentSection.setHeight(200);
		commentSection.setFillText(true);

		addSection(PageSectionId.COMMENTS_AUTHOR, commentSection);
		createSections();
		layoutSections();

	}

}
