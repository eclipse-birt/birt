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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormTextSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;

public abstract class GeneralPage extends GeneralFontPage {

	private TextSection librarySection;
	private SeperatorSection seperatorSection;
	private FormTextSection noteSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(6, 15));

		LibraryDescriptorProvider provider = new LibraryDescriptorProvider();
		librarySection = new TextSection(provider.getDisplayName(), container, true);
		librarySection.setWidth(500);
		librarySection.setFillText(false);
		librarySection.setProvider(provider);
		addSection(PageSectionId.GENERAL_LIBRARY, librarySection);

		noteSection = new FormTextSection("", container, true); //$NON-NLS-1$
		noteSection.setWidth(500);
		noteSection.setFillText(false);
		noteSection.setText("<form><p><span color=\"color\">" + //$NON-NLS-1$
				Messages.getFormattedString("GeneralPage.Library.Modified.Note",
						new Object[] { "</span> <img href=\"image\"/> <span color=\"color\">" }) //$NON-NLS-1$
				+ "</span></p></form>");
		noteSection.setImage("image", //$NON-NLS-1$
				ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ENABLE_RESTORE_PROPERTIES));
		noteSection.setColor("color", ColorManager.getColor(127, 127, 127)); //$NON-NLS-1$
		addSection(PageSectionId.GENERAL_LIBRARY_NOTE, noteSection);

		seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.GENERAL_SEPERATOR, seperatorSection);

		buildContent();

		createSections();
		layoutSections();
	}

	public void refresh() {
		if (input instanceof List && DEUtil.getMultiSelectionHandle((List) input).isExtendedElements()) {

			librarySection.setHidden(false);

			if (hasLocalProperties()) {

				noteSection.setHidden(false);
				Font font = JFaceResources.getDialogFont();
				FontData fData = font.getFontData()[0];
				fData.setHeight(fData.getHeight() - 1);
				noteSection.getTextControl().setFont(FontManager.getFont(fData));
			} else
				noteSection.setHidden(true);

			seperatorSection.setHidden(false);
			librarySection.load();
		} else {
			librarySection.setHidden(true);
			noteSection.setHidden(true);
			seperatorSection.setHidden(true);
		}
		super.refresh();
		container.layout(true);
		container.redraw();

	}

	private boolean hasLocalProperties() {
		GroupElementHandle groupHandle = DEUtil.getGroupElementHandle((List) input);

		return groupHandle.hasLocalPropertiesForExtendedElements();
	}

	/**
	 * Builds UI content of this page.
	 * 
	 * @param content parent composite.
	 */
	protected abstract void buildContent();

}
