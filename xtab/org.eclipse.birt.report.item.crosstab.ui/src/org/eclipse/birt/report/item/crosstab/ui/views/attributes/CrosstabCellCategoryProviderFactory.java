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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabCellPage;

/**
 * CrosstabCellCategoryProviderFactory
 */
public class CrosstabCellCategoryProviderFactory extends CategoryProviderFactory {

	private static ICategoryProviderFactory instance = new CrosstabCellCategoryProviderFactory();

	protected CrosstabCellCategoryProviderFactory() {
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
				new String[] { null, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS, CATEGORY_KEY_USERPROPERTIES,
						CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
				new String[] { CATEGORY_KEY_GENERAL },
				new String[] { Messages.getString("CrosstabPageGenerator.List.General") //$NON-NLS-1$
				}, new Class[] { CrosstabCellPage.class });
	}
}
