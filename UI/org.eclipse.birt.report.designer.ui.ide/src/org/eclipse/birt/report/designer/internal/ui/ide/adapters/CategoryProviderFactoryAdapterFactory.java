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

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == ICategoryProviderFactory.class) {
			return IDECategoryProviderFactory.getInstance();
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { ICategoryProviderFactory.class };
	}

}
