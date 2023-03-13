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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor.HandlerPage;
import org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor.IDECategoryProviderFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * CategoryProviderFactoryAdapterFactory
 */
public class CategoryProviderFactoryAdapterFactory implements IAdapterFactory {

	static {
		// fix bugzilla 224316, defer the class loading to this stage to avoid
		// class loading circulation.
		AttributesUtil.addCategory(AttributesUtil.EVENTHANDLER,
				Messages.getString("ReportPageGenerator.List.EventHandler"), HandlerPage.class); //$NON-NLS-1$

	}

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ICategoryProviderFactory.class) {
			return IDECategoryProviderFactory.getInstance();
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { ICategoryProviderFactory.class };
	}

}
