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
	@Override
	public ICategoryProvider getCategoryProvider(Object input) {
		return AttributesUtil.createCategoryProvider(
				new String[] { null, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_ADVANCEPROPERTY },
				new String[] { CATEGORY_KEY_GENERAL },
				new String[] { Messages.getString("CubePageGenerator.List.General") //$NON-NLS-1$
				}, new Class[] { CubePage.class });
	}
}
