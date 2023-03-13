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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ReferenceDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * The reference attribute page of Image element.
 */
public class ReferencePage extends AttributePage {

	private TextAndButtonSection referenceSection;
	private ReferenceDescriptorProvider referenceProvider;

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(3, 15));

		referenceProvider = new ReferenceDescriptorProvider();
		referenceSection = new TextAndButtonSection(referenceProvider.getDisplayName(), container, true) {

			@Override
			public void load() {
				super.load();
				if (referenceSection != null && referenceSection.getButtonControl() != null) {
					referenceSection.getButtonControl().setEnabled(referenceProvider.isEnableButton());
				}
			}
		};
		referenceSection.setProvider(referenceProvider);
		referenceSection.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				referenceProvider.handleSelectEvent();
			}

		});
		referenceSection.setWidth(300);
		referenceSection.setButtonText(Messages.getString("ReferencePage.Button.Edit")); //$NON-NLS-1$
		referenceSection.setButtonIsComputeSize(true);
		addSection(PageSectionId.REFERENCE_REFERENCE, referenceSection);

		createSections();
		layoutSections();
	}
}
