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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HyperLinkDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * The HyperLink attribute page of DE element.
 */
public class HyperLinkPage extends AttributePage {

	private TextAndButtonSection hyperLinkSection;
	private HyperLinkDescriptorProvider hyperLinkProvider;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(3, 15));

		hyperLinkProvider = new HyperLinkDescriptorProvider();
		hyperLinkSection = new TextAndButtonSection(hyperLinkProvider.getDisplayName(), container, true);
		hyperLinkSection.setProvider(hyperLinkProvider);
		hyperLinkSection.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (hyperLinkProvider.hyperLinkSelected())
					hyperLinkSection.load();
			}

		});
		hyperLinkSection.setWidth(300);
		hyperLinkSection.setButtonText(Messages.getString("HyperLinkPage.Button.Text")); //$NON-NLS-1$
		hyperLinkSection.setButtonTooltipText(Messages.getString("HyperLinkPage.toolTipText.Button")); //$NON-NLS-1$
		hyperLinkSection.setButtonIsComputeSize(true);
		addSection(PageSectionId.HYPERLINK_HYPERLINK, hyperLinkSection);

		createSections();
		layoutSections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (hyperLinkSection != null && hyperLinkSection.getButtonControl() != null) {
			hyperLinkSection.getButtonControl().setEnabled(hyperLinkProvider.isEnable());
		}
	}
}