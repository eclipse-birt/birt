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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ResourceKeyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.LabelSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ResourceKeySection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The i18n page for Scalarparameter
 */

public class ScalarParameterI18nPage extends AttributePage {
	private static final String MESSAGE_NOTE = Messages.getString("I18nPage.text.Note"); //$NON-NLS-1$
	public static final String I18N_I18N_PROMPT_TEXT = "I18N_I18N_PROMPT_TEXT"; //$NON-NLS-1$
	public static final String I18N_I18N_HELP_TEXT = "I18N_I18N_HELP_TEXT"; //$NON-NLS-1$

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		buildsections(ScalarParameterHandle.PROMPT_TEXT_ID_PROP, ReportDesignConstants.SCALAR_PARAMETER_ELEMENT,
				I18N_I18N_PROMPT_TEXT, 0);
		buildsections(ScalarParameterHandle.HELP_TEXT_KEY_PROP, ReportDesignConstants.SCALAR_PARAMETER_ELEMENT,
				I18N_I18N_HELP_TEXT, 1);

		LabelSection labelSection = new LabelSection(MESSAGE_NOTE, container, true);
		labelSection.setGridPlaceholder(3, true);
		labelSection.setWidth(350);
		labelSection.setFillLabel(true);
		addSection(PageSectionId.I18N_LABEL, labelSection);

		createSections();
		layoutSections();
	}

	protected void buildsections(String propertyName, String elementName, String pageSectionId, int groupIndex) {
		ResourceKeyDescriptorProvider i18nProvider = new ResourceKeyDescriptorProvider(propertyName, elementName);

		i18nProvider.setGroupIndex(groupIndex);

		ResourceKeySection i18nSection = new ResourceKeySection(i18nProvider.getDisplayName(), container, true);
		i18nSection.setProvider(i18nProvider);
		i18nSection.setWidth(350);
		i18nSection.setGridPlaceholder(3, true);
		addSection(pageSectionId, i18nSection);
	}

}
