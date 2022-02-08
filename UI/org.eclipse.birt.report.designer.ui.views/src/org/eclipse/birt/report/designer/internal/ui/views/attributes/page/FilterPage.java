/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;

/**
 * The class implements a filter page.
 * 
 * @since 2.3
 */
public class FilterPage extends FormPage {

	public FilterPage(int style, IFormProvider provider) {
		super(style, provider);
	}

	public FilterPage(int style, IFormProvider provider, boolean withDialog) {
		super(style, provider, withDialog);
	}

	public FilterPage(int style, IFormProvider provider, boolean withDialog, boolean isTabbed) {
		super(style, provider, withDialog, isTabbed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#
	 * createFormSection()
	 */
	protected void createFormSection() {
		super.createFormSection();
		IDescriptorProvider provider = ((FormSection) getSection(PageSectionId.FORM_FORM)).getProvider();
		if (provider instanceof AbstractFilterHandleProvider) {
			// Set correct filter provider.
			this.provider = ((AbstractFilterHandleProvider) provider).getConcreteFilterProvider();
			((FormSection) getSection(PageSectionId.FORM_FORM)).setProvider(this.provider);
		}
	}

}
