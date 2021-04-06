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

import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FormatStringDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormatStringSection;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Format string sttribute page for formatting strings.
 */

public class FormatStringAttributePage extends ResetAttributePage {

	private FormatStringDescriptorProvider provider;
	private FormatStringSection formatSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(new GridLayout(1, false));

		provider = new FormatStringDescriptorProvider();
		formatSection = new FormatStringSection(container, IFormatPage.PAGE_ALIGN_VIRTICAL, true);
		formatSection.setProvider(provider);
		addSection(PageSectionId.FORMATSTRING_FORMAT, formatSection);

		createSections();
		layoutSections();
		formatSection.addFormatChangeListener(new IFormatChangeListener() {

			public void formatChange(FormatChangeEvent event) {
				if (formatSection.getFormatControl().isDirty() && formatSection.getFormatControl().isFormatModified()) {
					try {
						provider.save(new String[] { event.getCategory(), event.getPattern(), event.getLocale() });
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
					if (event.getCategory() != null || event.getPattern() != null || event.getLocale() != null) {
						refresh();
					}
				}
			}
		});
	}

}