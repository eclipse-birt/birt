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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes;

import org.eclipse.birt.report.designer.ui.cubebuilder.attributes.page.CubePage;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;

/**
 * CubeCategoryProviderFactory
 */
public class CubeCategoryProviderFactory extends CategoryProviderFactory {

	private static ICategoryProviderFactory instance = new CubeCategoryProviderFactory();

	protected CubeCategoryProviderFactory() {
	}

	/**
	 * 
	 * @return The unique CategoryProviderFactory instance
	 */
	public static ICategoryProviderFactory getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * ICategoryProviderFactory#getCategoryProvider(java.lang.Object)
	 */
	public ICategoryProvider getCategoryProvider(Object input) {
		return AttributesUtil.createCategoryProvider(
				new String[] { null, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_ADVANCEPROPERTY },
				new String[] { CATEGORY_KEY_GENERAL },
				new String[] { Messages.getString("CubePageGenerator.List.General") //$NON-NLS-1$
				}, new Class[] { CubePage.class });
	}
}
