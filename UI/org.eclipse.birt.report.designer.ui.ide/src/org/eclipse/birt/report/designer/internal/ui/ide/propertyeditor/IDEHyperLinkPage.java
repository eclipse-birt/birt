/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndTwoButtonSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class IDEHyperLinkPage extends AttributePage {

	private TextAndTwoButtonSection hyperLinkSection;
	private IDEHyperLinkDescriptorProvider hyperLinkProvider;

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(4, 15));

		hyperLinkProvider = new IDEHyperLinkDescriptorProvider();
		hyperLinkSection = new TextAndTwoButtonSection(hyperLinkProvider.getDisplayName(), container, true);
		hyperLinkSection.setProvider(hyperLinkProvider);
		hyperLinkSection.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hyperLinkProvider.hyperLinkSelected()) {
					hyperLinkSection.load();
				}
			}

		});
		hyperLinkSection.addSecondSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hyperLinkProvider.hyperLinkDeleted()) {
					hyperLinkSection.load();
				}
			}
		});
		hyperLinkSection.setWidth(300);
		hyperLinkSection.setButtonText(Messages.getString("HyperLinkPage.Button.Text")); //$NON-NLS-1$
		hyperLinkSection.setButtonTooltipText(Messages.getString("HyperLinkPage.toolTipText.Button")); //$NON-NLS-1$
		hyperLinkSection.setSecondButtonText(Messages.getString("HyperLinkPage.RemoveButton.Text")); //$NON-NLS-1$
		hyperLinkSection.setSecondButtonTooltipText(Messages.getString("HyperLinkPage.toolTipText.RemoveButton")); //$NON-NLS-1$
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
	@Override
	public void refresh() {
		super.refresh();
		if (hyperLinkSection != null && hyperLinkSection.getButtonControl() != null) {
			hyperLinkSection.getButtonControl().setEnabled(hyperLinkProvider.isEnable());
		}
	}
}
